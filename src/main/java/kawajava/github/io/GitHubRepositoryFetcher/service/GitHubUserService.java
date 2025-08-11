package kawajava.github.io.GitHubRepositoryFetcher.service;

import kawajava.github.io.GitHubRepositoryFetcher.exception.NotExistingUserException;
import kawajava.github.io.GitHubRepositoryFetcher.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GitHubUserService {

    private final WebClient webClient;

    public Mono<List<RepositoryWithBranches>> getGitHubUserRepositoriesData(String username) {
        return dropForks(username)
                .flatMap(repository -> getRepositories(repository, username))
                .collectList();
    }

    private Flux<RepositoryWithBranches> getRepositories(UserRepository repository, String username) {
        return fetchBranches(username, repository.repositoryName())
                .map(this::mapToBranch)
                .collectList()
                .flatMapMany(branches -> Flux.just(new RepositoryWithBranches(repository.repositoryName(), repository.ownerLogin(), branches)));
    }

    private Flux<RepositoryDto> fetchRepositories(String username) {
        return this.webClient.get()
                .uri("/users/{username}/repos", username)
                .retrieve()
                .onStatus(status -> status.value() == 404,
                        response -> Mono.error(new NotExistingUserException(username)))
                .bodyToFlux(RepositoryDto.class);
    }

    private Branch mapToBranch(BranchDto branch) {
        return new Branch(branch.name(), branch.commit().sha());
    }

    private UserRepository mapToUserRepository(RepositoryDto repository) {
        return new UserRepository(repository.name(), repository.owner().login());
    }

    private Flux<BranchDto> fetchBranches(String username, String repoName) {
        return this.webClient.get()
                .uri("/repos/{user}/{repo}/branches", username, repoName)
                .retrieve()
                .bodyToFlux(BranchDto.class);
    }

    private Flux<UserRepository> dropForks(String username) {
        return fetchRepositories(username)
                .filter(repository -> !repository.isFork())
                .map(this::mapToUserRepository);
    }
}
