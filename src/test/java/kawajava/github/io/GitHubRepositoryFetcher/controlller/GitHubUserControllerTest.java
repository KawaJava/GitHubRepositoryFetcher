package kawajava.github.io.GitHubRepositoryFetcher.controlller;

import kawajava.github.io.GitHubRepositoryFetcher.model.RepositoryWithBranches;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("integration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GitHubUserControllerHappyPathIT {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @DynamicPropertySource
    static void registerProps(DynamicPropertyRegistry registry) {
        registry.add("github.api.url", () -> "https://api.github.com");
    }

    @Test
    void shouldReturnNonForkRepositoriesWithBranches() {
        // given
        var username = "octocat";
        var url = "http://localhost:" + port + "/" + username;

        var headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        // when
        ResponseEntity<RepositoryWithBranches[]> response =
                restTemplate.exchange(url, HttpMethod.GET, entity, RepositoryWithBranches[].class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        RepositoryWithBranches[] repos = response.getBody();
        assertThat(repos).isNotNull().isNotEmpty();
        assertThat(repos).allMatch(r -> r.repositoryName() != null && !r.repositoryName().isBlank());

        RepositoryWithBranches helloWorld = Arrays.stream(repos)
                .filter(r -> "Hello-World".equalsIgnoreCase(r.repositoryName()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Brak repo Hello-World"));

        assertThat(helloWorld.ownerLogin()).isEqualToIgnoringCase(username);
        assertThat(helloWorld.branches()).isNotEmpty()
                .allSatisfy(branch -> {
                    assertThat(branch.name()).isNotBlank();
                    assertThat(branch.lastCommitSha()).matches("^[0-9a-f]{40}$");
                });
    }
}
