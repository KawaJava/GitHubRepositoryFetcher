package kawajava.github.io.GitHubRepositoryFetcher.controlller;

import kawajava.github.io.GitHubRepositoryFetcher.model.RepositoryWithBranches;
import kawajava.github.io.GitHubRepositoryFetcher.service.GitHubUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GitHubUserController {


    private final GitHubUserService userService;

    @GetMapping("/{username}")
    public Mono<List<RepositoryWithBranches>> getUserData(@PathVariable String username,
                                                          @RequestHeader(HttpHeaders.ACCEPT) String accept) {
        return userService.getGitHubUserRepositoriesData(username);
    }
}
