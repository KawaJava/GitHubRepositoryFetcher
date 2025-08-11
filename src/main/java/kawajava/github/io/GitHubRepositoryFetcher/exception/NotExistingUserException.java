package kawajava.github.io.GitHubRepositoryFetcher.exception;

public class NotExistingUserException extends RuntimeException {

    public NotExistingUserException(String username) {
        super("GitHub User: " + username + " Not Found!");
    }
}
