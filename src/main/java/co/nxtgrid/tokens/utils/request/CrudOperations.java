package co.nxtgrid.tokens.utils.request;


import co.nxtgrid.tokens.response.ApiResponse;

public interface CrudOperations {

    ApiResponse get(BasicAuthCredentials credential,
                    String path)
            throws ApiResponseException;

    ApiResponse post(BasicAuthCredentials credential,
                     String path,
                     String payload)
            throws ApiResponseException;

    ApiResponse delete(BasicAuthCredentials credential,
                       String path)
            throws ApiResponseException;

    ApiResponse put(BasicAuthCredentials credential,
                    String path,
                    String payload)
            throws ApiResponseException;

}
