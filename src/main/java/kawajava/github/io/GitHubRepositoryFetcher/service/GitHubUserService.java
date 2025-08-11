package kawajava.github.io.GitHubRepositoryFetcher.service;

import kawajava.github.io.GitHubRepositoryFetcher.exception.NotExistingUserException;
import kawajava.github.io.GitHubRepositoryFetcher.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

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
        return fetchRepositories(username).stream()
                .filter(repository -> !repository.isFork())
                .map(repository -> {
                    var branches = fetchBranches(username, repository.name());
                    return mapToRepositoryWithBranches(repository, branches);
                })
                .toList();
    }

    private RepositoryWithBranches mapToRepositoryWithBranches(RepositoryDto repositoryDto,
                                                               List<BranchDto> branchDtos) {
        return new RepositoryWithBranches(
                repositoryDto.name(),
                repositoryDto.owner().login(),
                branchDtos.stream()
                        .map(this::mapToBranch)
                        .toList()
        );
    }

    private Branch mapToBranch(BranchDto branchDto) {
        return new Branch(branchDto.name(), branchDto.commit().sha());
    }

    private List<RepositoryDto> fetchRepositories(String username) {
        var url = githubApiUrl + "/users/{username}/repos";
        try {
            ResponseEntity<RepositoryDto[]> response = restTemplate.getForEntity(url, RepositoryDto[].class, username);
            return Arrays.asList(Objects.requireNonNull(response.getBody()));
        } catch (HttpClientErrorException.NotFound e) {
            throw new NotExistingUserException(username);
        }
    }

    private List<BranchDto> fetchBranches(String username, String repoName) {
        var url = githubApiUrl + "/repos/{user}/{repo}/branches";
        ResponseEntity<BranchDto[]> response = restTemplate.getForEntity(url, BranchDto[].class, username, repoName);
        return Arrays.asList(Objects.requireNonNull(response.getBody()));
    }
}

