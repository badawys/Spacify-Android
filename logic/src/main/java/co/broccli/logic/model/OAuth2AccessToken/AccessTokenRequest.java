package co.broccli.logic.model.OAuth2AccessToken;


public class AccessTokenRequest {

    private String grant_type = "password";
    private Integer client_id = 2;
    private String client_secret = "nSlVw9unrh7BQ7SR4lPdtYsSUIVSbSbFk67SnBkB";
    private String scope = "*";

    private String username;
    private String password;

    public AccessTokenRequest(String username, String password) {
        this.username = username;
        this.password = password;

    }
}
