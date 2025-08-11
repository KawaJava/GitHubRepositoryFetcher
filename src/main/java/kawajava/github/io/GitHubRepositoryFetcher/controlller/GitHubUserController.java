package kawajava.github.io.GitHubRepositoryFetcher.controlller;

import kawajava.github.io.GitHubRepositoryFetcher.model.RepositoryWithBranches;
import kawajava.github.io.GitHubRepositoryFetcher.service.GitHubUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GitHubUserController {

    private final GitHubUserService userService;

    @GetMapping("/{username}")
    public List<RepositoryWithBranches> getUserData(@PathVariable String username,
            @RequestHeader(HttpHeaders.ACCEPT) String accept
    ) {
        if (!MediaType.APPLICATION_JSON_VALUE.equalsIgnoreCase(accept)) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE);
        }
        return userService.getGitHubUserRepositoriesData(username);
    }
}

