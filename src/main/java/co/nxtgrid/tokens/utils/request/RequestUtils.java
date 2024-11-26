package co.nxtgrid.tokens.utils.request;

import co.nxtgrid.tokens.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

// @Component
public class RequestUtils implements CrudOperations {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public ApiResponse get(BasicAuthCredentials credentials,
                           String host)
        throws ApiResponseException {
        ApiResponse response = restTemplate
                .exchange(host,
                        HttpMethod.GET,
                        generatePayload(null, credentials),
                        ApiResponse.class)
                .getBody();

        if (response.getStatus().getCode() == 200) {
            return response;
        }
        throw new ApiResponseException(response.getStatus().getMessage());
    }

    @Override
    public ApiResponse post(BasicAuthCredentials credentials,
                            String host,
                            String payload)
        throws ApiResponseException {
        ApiResponse response = restTemplate
                .exchange(host,
                        HttpMethod.POST,
                        generatePayload(payload, credentials),
                        ApiResponse.class)
                .getBody();

        if (response.getStatus().getCode() == 200) {
            return response;
        }
        throw new ApiResponseException(response.getStatus().getMessage());
    }

    @Override
    public ApiResponse delete(BasicAuthCredentials credentials,
                              String host)
        throws ApiResponseException {
        ApiResponse response = restTemplate
                .exchange(host,
                        HttpMethod.DELETE,
                        generatePayload(null, credentials),
                        ApiResponse.class)
                .getBody();

        if (response.getStatus().getCode() == 200) {
            return response;
        }
        throw new ApiResponseException(response.getStatus().getMessage());
    }

    @Override
    public ApiResponse put(BasicAuthCredentials credentials,
                           String host,
                           String payload)
        throws ApiResponseException {
        ApiResponse response = restTemplate
                .exchange(host,
                        HttpMethod.PUT,
                        generatePayload(payload, credentials),
                        ApiResponse.class)
                .getBody();

        if (response.getStatus().getCode() == 200) {
            return response;
        }
        throw new ApiResponseException(response.getStatus().getMessage());
    }

    private HttpEntity<String> generatePayload(String payload,
                                               BasicAuthCredentials credentials) {
        String authenticationStr = String.format("%s:%s", credentials.getUsername(), credentials.getPassword());
        String base64Credentials = Base64.getEncoder().encodeToString(authenticationStr.getBytes());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64Credentials);

        if (payload != null) {
            headers.setContentType(MediaType.APPLICATION_JSON);
            return new HttpEntity<>(payload, headers);
        }

        return new HttpEntity<>(headers);
    }
 }
