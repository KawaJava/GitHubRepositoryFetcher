package kawajava.github.io.GitHubRepositoryFetcher.model;

import java.util.List;

public record RepositoryWithBranches(String repositoryName, String ownerLogin, List<Branch> branches) {
}
