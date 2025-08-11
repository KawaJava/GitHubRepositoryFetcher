package kawajava.github.io.GitHubRepositoryFetcher.service;

import kawajava.github.io.GitHubRepositoryFetcher.exception.NotExistingUserException;
import kawajava.github.io.GitHubRepositoryFetcher.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class GitHubUserService {

    private final RestTemplate restTemplate;
    @Value("${github.api.base.url}")
    private String githubApiUrl;

    public List<RepositoryWithBranches> getGitHubUserRepositoriesData(String username) {
        List<RepositoryDto> repos = fetchRepositories(username);

        return repos.stream()
                .filter(repo -> !repo.isFork())
                .map(repo -> new RepositoryWithBranches(
                        repo.name(),
                        repo.owner().login(),
                        fetchBranches(username, repo.name()).stream()
                                .map(b -> new Branch(b.name(), b.commit().sha()))
                                .toList()
                ))
                .toList();
    }

    private List<RepositoryDto> fetchRepositories(String username) {
        String url = githubApiUrl + "/users/{username}/repos";
        try {
            ResponseEntity<RepositoryDto[]> response = restTemplate.getForEntity(url, RepositoryDto[].class, username);
            return Arrays.asList(Objects.requireNonNull(response.getBody()));
        } catch (HttpClientErrorException.NotFound e) {
            throw new NotExistingUserException(username);
        }
    }

    private List<BranchDto> fetchBranches(String username, String repoName) {
        String url = githubApiUrl + "/repos/{user}/{repo}/branches";
        ResponseEntity<BranchDto[]> response = restTemplate.getForEntity(url, BranchDto[].class, username, repoName);
        return Arrays.asList(Objects.requireNonNull(response.getBody()));
    }
}

