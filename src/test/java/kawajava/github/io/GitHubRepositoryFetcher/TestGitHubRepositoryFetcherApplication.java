package kawajava.github.io.GitHubRepositoryFetcher;

import org.springframework.boot.SpringApplication;

public class TestGitHubRepositoryFetcherApplication {

	public static void main(String[] args) {
		SpringApplication.from(GitHubRepositoryFetcherApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
