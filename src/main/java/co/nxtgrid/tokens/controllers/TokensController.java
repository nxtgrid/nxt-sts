package co.nxtgrid.tokens.controllers;

import co.nxtgrid.tokens.annotation.Notify;
import co.nxtgrid.tokens.constant.StringConstants;
import co.nxtgrid.tokens.controllers.exceptions.InvalidAggregateTokenParamException;
import co.nxtgrid.tokens.entity.Token;
import co.nxtgrid.tokens.response.ApiResponse;
import co.nxtgrid.tokens.service.TokensService;
import co.nxtgrid.tokens.service.impl.TimelineRequest;
import co.nxtgrid.tokens.service.impl.validate.TokenTypeCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.util.*;

@RestController
@RequestMapping("/v1")
public class TokensController {

    @Autowired
    private TokensService tokensService;

    @GetMapping(value = "/tokens", params = {"request_id", "ref"})
    public ApiResponse getToken(@RequestParam(value = "request_id") @NotNull String requestID,
                                      @RequestParam(value = "ref") String ref) {
        ApiResponse apiResponse;
        try {
            if (ref != null && !ref.isBlank()) {
                Token token = tokensService.getToken(ref);
                Map<String, Object> output = new LinkedHashMap<>();
                output.put("token", token);
                apiResponse = new ApiResponse(StringConstants.SUCCESS_CODE,
                                                StringConstants.SUCCESS_TOKEN_DETAILS,
                                                requestID,
                                                output);
            } else {
                apiResponse = new ApiResponse(StringConstants.SUCCESS_CODE,
                                                StringConstants.EMPTY_USER_NAME_VALUE,
                                                requestID,
                                                null);
            }
        } catch (Exception e) {
            apiResponse = new ApiResponse(StringConstants.INTERNAL_SERVER_ERROR,
                                            e.getMessage(),
                                            requestID,
                                            null);
        }
        return apiResponse;
    }

    @GetMapping(value = "/tokens", params = {"request_id", "user_ref"})
    public ApiResponse getTokens(@RequestParam(value = "request_id") @NotNull String requestID,
                                 @RequestParam(value = "user_ref") String userRef) {
        ApiResponse apiResponse;
        try {
            if (userRef != null && !userRef.isBlank()) {
                List<Token> token = tokensService.getTokens(userRef);
                Map<String, Object> output = new LinkedHashMap<>();
                output.put("tokens", token);
                apiResponse = new ApiResponse(StringConstants.SUCCESS_CODE,
                                                StringConstants.SUCCESS_TOKEN_DETAILS,
                                                requestID,
                                                output);
            } else {
                apiResponse = new ApiResponse(StringConstants.SUCCESS_CODE,
                                                StringConstants.EMPTY_USER_NAME_VALUE,
                                                requestID,
                                                null);
            }
        } catch (Exception e) {
            apiResponse = new ApiResponse(StringConstants.INTERNAL_SERVER_ERROR,
                                            e.getMessage(),
                                            requestID,
                                            null);
        }
        return apiResponse;
    }

    @GetMapping(value = "/tokens", params = {"request_id", "user_ref", "months"})
    public ApiResponse getTimelineRequests(@RequestParam(value = "request_id") @NotNull String requestID,
                                              @RequestParam(value = "user_ref") @NotNull String userRef,
                                              @RequestParam(value = "months") Optional<Integer> months) {
        ApiResponse apiResponse;
        try {
            if (months.isEmpty()) months = Optional.of(5);
            List<TimelineRequest> tokens = tokensService.getTimelineRequests(userRef, months.get());
            Map<String, Object> output = new LinkedHashMap<>();
            output.put("requests", tokens);
            apiResponse = new ApiResponse(StringConstants.SUCCESS_CODE,
                                            StringConstants.SUCCESS_USER_TOKEN_DETAILS,
                                            requestID,
                                            output);

        } catch (Exception e) {
            return new ApiResponse(StringConstants.INTERNAL_SERVER_ERROR,
                                    e.getMessage(),
                                    requestID,
                                    null);
        }
        return apiResponse;
    }

    @GetMapping(value = "/tokens", params = {"request_id", "user_ref", "detailed_param"})
    public ApiResponse getTokenTypes(@RequestParam(value = "request_id") @NotNull String requestID,
                                     @RequestParam(value = "user_ref") @NotNull String userRef,
                                     @RequestParam(value = "detailed_param") @NotNull String detailedParam) {
        try {
            List<TokenTypeCount> result;
            switch (detailedParam){
                case("token-types"):
                    result = tokensService.getTokenTypes(userRef);
                    break;
                default:
                    throw new InvalidAggregateTokenParamException(
                            String.format("%s %s", StringConstants.INVALID_AGGREGATE_TOKEN_PARAM, detailedParam));
            }

            Map<String, Object> output = new LinkedHashMap<>();
            output.put("result", result);
            return new ApiResponse(StringConstants.SUCCESS_CODE,
                                    StringConstants.SUCCESS_USER_TOKEN_DETAILS,
                                    requestID,
                                    output);

        } catch (Exception e) {
            return new ApiResponse(StringConstants.INTERNAL_SERVER_ERROR,
                                    e.getMessage(),
                                    requestID,
                                    null);
        }
    }

    @GetMapping(value = "/tokens", params = {"request_id", "user_ref", "param"})
    public ApiResponse getAggregateTokenParam(@RequestParam(value = "request_id") @NotNull String requestID,
                                              @RequestParam(value = "user_ref") @NotNull String userRef,
                                              @RequestParam(value = "param") @NotNull String param) {
        try {
            int result;
            switch (param){
                case ("generated-tokens"):
                    result = tokensService.getGeneratedNoOfTokens(userRef);
                    break;
                case("token-types"):
                    result = tokensService.getNoOfTokenTypes(userRef);
                    break;
                case ("unique-meters"):
                    result = tokensService.getUniqueMetersNo(userRef);
                    break;
                default:
                    throw new InvalidAggregateTokenParamException(
                            String.format("%s %s", StringConstants.INVALID_AGGREGATE_TOKEN_PARAM, param));
            }

            Map<String, Object> output = new LinkedHashMap<>();
            output.put("result", result);
            return new ApiResponse(StringConstants.SUCCESS_CODE,
                                    StringConstants.SUCCESS_USER_TOKEN_DETAILS,
                                    requestID,
                                    output);

        } catch (Exception e) {
            return new ApiResponse(StringConstants.INTERNAL_SERVER_ERROR,
                                    e.getMessage(),
                                    requestID,
                                    null);
        }
    }

    @PostMapping("/tokens/{token}")
    @Notify(category = "DECODE_TOKEN",
            description = "Decoded token {token} [Request-ID: {requestID}]")
    public ApiResponse decodeToken(@RequestParam(value = "request_id") @NotNull String requestID,
                                   @RequestParam(value = "user_ref") @NotNull String userRef,
                                   @PathVariable(value = "token") @NotNull String token,
                                   @RequestBody Map<String, String> params) {
        try {
            HashMap<String, Object> decodedToken
                    = tokensService.decodeToken(requestID, token, params, userRef);
            Map<String, Object> output = new LinkedHashMap<>();
            output.put("token_details", decodedToken);
            return new ApiResponse(StringConstants.SUCCESS_CODE,
                                    StringConstants.SUCCESS_TOKEN_DECODED,
                                    requestID,
                                    output);
        } catch (Exception e) {
            return new ApiResponse(StringConstants.INTERNAL_SERVER_ERROR,
                                    e.getMessage(),
                                    requestID,
                                    null);
        }
    }

    @PostMapping("/tokens")
    @Notify(category = "GENERATE_TOKEN",
            description = "Generated token [Request-ID: {requestID}]")
    public ApiResponse generateToken(@RequestParam(value = "request_id") @NotNull String requestID,
                                     @RequestParam(value = "user_ref") @NotNull String userRef,
                                     @RequestBody Map<String, String> params) {
        try {
            List<Token> token = tokensService.generateTokens(requestID, params, userRef);
            Map<String, Object> output = new LinkedHashMap<>();
            output.put("tokens", token);
            return new ApiResponse(StringConstants.SUCCESS_CODE,
                                    StringConstants.SUCCESS_TOKEN_GENERATED,
                                    requestID,
                                    output);
        } catch (Exception e) {
            return new ApiResponse(StringConstants.INTERNAL_SERVER_ERROR,
                                    e.getMessage(),
                                    requestID,
                                    null);
        }
    }

    @DeleteMapping("/tokens")
    @Notify(category = "DELETE_TOKEN",
            description = "Deleted token {ref} [Request-ID: {requestID}]")
    public ApiResponse deleteToken(HttpServletRequest request,
                                  @RequestParam(value = "request_id") @NotNull String requestID,
                                   @RequestParam(value = "user_ref") @NotNull String userRef,
                                   @RequestParam(value = "ref") String ref) {
        ApiResponse apiResponse;
        try {
            if (ref != null && !ref.isBlank()) {
                tokensService.deleteToken(ref, userRef);
                apiResponse = new ApiResponse(StringConstants.SUCCESS_CODE,
                                                StringConstants.DELETED_MSG_TOKEN_BY_REF,
                                                requestID,
                                                null);

            } else {
                apiResponse = new ApiResponse(StringConstants.SUCCESS_CODE,
                                                StringConstants.EMPTY_REF_VALUE,
                                                requestID,
                                                null);
            }
        } catch (Exception e) {
            apiResponse = new ApiResponse(StringConstants.INTERNAL_SERVER_ERROR,
                                            e.getMessage(),
                                            requestID,
                                            null);
        }
        return apiResponse;
    }

    @DeleteMapping(value = "/tokens", params = { "request_id", "token" })
    @Notify(category = "DELETE_TOKEN",
            description = "Deleted token {token} [Request-ID: {requestID}]")
    public ApiResponse deleteTokenByTokenNo(HttpServletRequest request,
                                            @RequestParam(value = "request_id") @NotNull String requestID,
                                            @RequestParam(value = "user_ref") @NotNull String userRef,
                                            @Pattern(regexp = "^[0-9]{20}$") @RequestParam(value = "token") String token) {
        ApiResponse apiResponse;
        try {
            if (token != null && !token.isBlank()) {
                tokensService.deleteTokenByTokenNo(token, userRef);
                apiResponse = new ApiResponse(StringConstants.SUCCESS_CODE,
                                                StringConstants.DELETED_MSG_TOKEN_BY_TOKEN_NO,
                                                requestID,
                                                null);

            } else {
                apiResponse = new ApiResponse(StringConstants.SUCCESS_CODE,
                                                StringConstants.EMPTY_TOKEN_NO_VALUE,
                                                requestID,
                                                null);
            }
        } catch (Exception e) {
            apiResponse = new ApiResponse(StringConstants.INTERNAL_SERVER_ERROR,
                                            e.getMessage(),
                                            requestID,
                                            null);
        }
        return apiResponse;
    }

}
