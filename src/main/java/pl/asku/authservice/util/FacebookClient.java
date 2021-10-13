package pl.asku.authservice.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.asku.authservice.dto.facebook.FacebookUserDto;

import java.util.HashMap;
import java.util.Map;

@Service
public class FacebookClient {

    private final RestTemplate restTemplate;

    private final String FACEBOOK_GRAPH_API_BASE = "https://graph.facebook.com";

    public FacebookClient(@Autowired RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public FacebookUserDto getUser(String accessToken) {
        var path = "/me?fields={fields}&redirect={redirect}&access_token={access_token}";
        var fields = "email,first_name,last_name,id,picture.width(720).height(720)";
        final Map<String, String> variables = new HashMap<>();
        variables.put("fields", fields);
        variables.put("redirect", "false");
        variables.put("access_token", accessToken);
        return restTemplate
                .getForObject(FACEBOOK_GRAPH_API_BASE + path, FacebookUserDto.class, variables);
    }
}
