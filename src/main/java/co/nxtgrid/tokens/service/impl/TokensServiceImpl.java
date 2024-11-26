package co.nxtgrid.tokens.service.impl;

// import co.nxtgrid.token.exceptions.InvalidTokenException;
// import co.nxtgrid.token.exceptions.TokenNotFoundException;
// import co.nxtgrid.tokens.constant.StringConstants;
import co.nxtgrid.tokens.entity.Token;
// import co.nxtgrid.tokens.repository.TokensRepository;
import co.nxtgrid.tokens.service.TokensService;
import co.nxtgrid.tokens.service.impl.decoder.TokenDecoderManager;
import co.nxtgrid.tokens.service.impl.exceptions.InvalidTokenNoException;
import co.nxtgrid.tokens.service.impl.exceptions.InvalidTokenParamsException;
import co.nxtgrid.tokens.service.impl.exceptions.InvalidTokenRefException;
import co.nxtgrid.tokens.service.impl.exceptions.InvalidUserRefException;
import co.nxtgrid.tokens.service.impl.generate.TokenGeneratorManager;
import co.nxtgrid.tokens.service.impl.validate.TokenTypeCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static co.nxtgrid.tokens.service.impl.validate.Validator.validate;

@Service
public class TokensServiceImpl {

    // @PersistenceContext
    // private EntityManager entityManager;

    // @Autowired
    // private TokensRepository tokensRepository;

    // @Autowired
    // private TokenGeneratorManager generatorManager;

    // @Autowired
    // private TokenDecoderManager decoderManager;

    // @Override
    // public Token getToken(String ref) throws Exception {
    //     Token token = tokensRepository.findByRef(ref);
    //     if (token != null) {
    //         return token;
    //     }
    //     throw new InvalidTokenRefException(
    //             String.format("%s %s",
    //             StringConstants.INVALID_TOKEN_REF, ref));
    // }

    // @Override
    // public  List<Token> getTokens(String userRef) throws Exception {
    //     return tokensRepository.findByUserRefOrderByCreatedAtDesc(userRef);
    // }

    // @Override
    // public List<TimelineRequest> getTimelineRequests(String userRef, int months)
    //         throws Exception {
    //     return tokensRepository.getTimelineRequests(userRef, months);
    // }

    // @Override
    // public List<TokenTypeCount> getTokenTypes(String userRef) throws Exception {
    //     return tokensRepository.getTokenTypes(userRef);
    // }

    // @Override
    // public List<Token> generateTokens(String requestID, Map<String, String> params, String userRef) throws Exception {
    //     if (validate(params)) {
    //         List<Token> generatedTokens = generatorManager.generate(requestID, params, userRef);
    //         for (Token token : generatedTokens) {
    //             tokensRepository.save(token);
    //         }
    //         return generatedTokens;
    //     }
    //     throw new InvalidTokenParamsException(StringConstants.INVALID_TOKEN_PARAMS);
    // }

    // @Override
    // public void deleteToken(String ref, String userRef) throws Exception {
    //     Token token = tokensRepository.findByRef(ref);
    //     if (token != null) {
    //         if (token.getUserRef().equals(userRef)) {
    //             tokensRepository.delete(token);
    //         } else {
    //             throw new InvalidUserRefException(StringConstants.INVALID_USER_REF);
    //         }
    //     } else {
    //         throw new InvalidTokenRefException(
    //                 String.format("%s %s", StringConstants.INVALID_TOKEN_REF, ref));
    //     }
    // }

    // @Override
    // public void deleteTokenByTokenNo(String tokenNo, String userRef) throws Exception {
    //     Token token = tokensRepository.findByTokenNo(tokenNo);
    //     if (token != null) {
    //         if (token.getUserRef().equals(userRef)) {
    //             tokensRepository.deleteByTokenNo(tokenNo);
    //         } else {
    //             throw new InvalidUserRefException(StringConstants.INVALID_USER_REF);
    //         }
    //     } else {
    //         throw new InvalidTokenNoException(
    //                 String.format("%s %s", StringConstants.INVALID_TOKEN_NO, tokenNo));
    //     }
    // }

    // @Override
    // public  int getGeneratedNoOfTokens(String userRef) throws Exception {
    //     return tokensRepository.countByUserRef(userRef);
    // }

    // @Override
    // public int getNoOfTokenTypes(String userRef) throws Exception {
    //     return tokensRepository.countTokenTypeByUserRef(userRef);
    // }

    // @Override
    // public int getUniqueMetersNo(String userRef) throws Exception {
    //     return tokensRepository.countDistinctMeterNoByUserRef(userRef);
    // }

    // @Override
    // public HashMap<String,Object> decodeToken(String requestID, String token,
    //                                            Map<String, String> params,
    //                                            String userRef) throws Exception {
    //     if (token.matches("[0-9]{20}")) {
    //         Token extractedToken = tokensRepository.findByTokenNo(token);
    //         if (extractedToken != null) {
    //             if (extractedToken.getUserRef().equals(userRef)) {
    //                 HashMap<String, Object> decodedToken
    //                         = decoderManager.decode(requestID, token, params).getParams();
    //                 decodedToken.put("ref", extractedToken.getRef());
    //                 decodedToken.put("user_ref", extractedToken.getUserRef());
    //                 decodedToken.put("created_at", extractedToken.getCreatedAt());
    //                 return decodedToken;
    //             }
    //             throw new InvalidUserRefException(StringConstants.INVALID_USER_REF);
    //         }
    //         throw new TokenNotFoundException(String.format("%s not found in database", token));
    //     }
    //     throw new InvalidTokenException(StringConstants.INVALID_TOKEN);
    // }
}
