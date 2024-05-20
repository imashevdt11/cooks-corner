package kg.neobis.cookscorner.common;

public final class EndpointConstants {

    private static final String API_PREFIX = "/api";

    public static final String AUTH_ENDPOINT = API_PREFIX + "/auth";

    public static final String LIKE_RECIPE_ENDPOINT = API_PREFIX + "/like-recipe";

    public static final String RECIPE_ENDPOINT = API_PREFIX + "/recipe";

    public static final String SAVE_RECIPE_ENDPOINT = API_PREFIX + "/save-recipe";

    public static final String[] WHITE_LIST_URL = {
            AUTH_ENDPOINT + "/signUp",
            AUTH_ENDPOINT + "/logIn",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html"
    };

    private EndpointConstants() {}
}