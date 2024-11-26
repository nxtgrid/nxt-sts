package co.nxtgrid.tokens.service.impl.generate;

// import co.nxtgrid.token.domain.token.class2.Class2Token;
// import co.nxtgrid.tokens.constant.StringConstants;
import co.nxtgrid.tokens.entity.Token;
import co.nxtgrid.tokens.service.impl.generate.class0.TransferElectricityTokenGenerator;
import co.nxtgrid.tokens.service.impl.generate.class0.TransferGasTokenGenerator;
import co.nxtgrid.tokens.service.impl.generate.class0.TransferWaterTokenGenerator;
import co.nxtgrid.tokens.service.impl.generate.class1.InitiateMeterTestOrDisplay1TokenGenerator;
import co.nxtgrid.tokens.service.impl.generate.class1.InitiateMeterTestOrDisplay2TokenGenerator;
import co.nxtgrid.tokens.service.impl.generate.class2.*;
import co.nxtgrid.tokens.service.impl.generate.exceptions.InvalidTokenClassSubclassException;
import co.nxtgrid.tokens.service.impl.generate.exceptions.TokenNotPresentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class TokenGeneratorManager {

    // @Autowired
    // private RedisTemplate redisTemplate;

    // public List<Token> generate(String requestID, Map<String, String> params, String userRef)
    //         throws Exception {

    //     String drn = params.get("decoder_reference_number");

    //     if (params.containsKey("token_id")) {
    //         String tid = params.get("token_id");
    //         LocalDateTime currTid = convertToLocalDateTime(tid);
    //         LocalDateTime availableTid = currTid;

    //         if (params.get("is_stid").equals("false")) {

    //             if (checkIfSpecialReservedTokenIdentifier(currTid))
    //                 availableTid = currTid.plusMinutes(1);

    //             availableTid = getAvailableTid(drn, availableTid);
    //         }

    //         String key = hash(String.format("%s%s", drn,
    //                 availableTid.toLocalDate().toString()));
    //         redisStore(key, tid);

    //         params.put("token_id", currTid.toString());
    //     }

    //     return generateToken(requestID, params, userRef);
    // }

    // private LocalDateTime convertToLocalDateTime(String tid)
    //         throws UnsupportedEncodingException {
    //     return LocalDateTime.parse(URLDecoder
    //             .decode(tid, StandardCharsets.UTF_8.toString()));
    // }

    // private boolean checkIfSpecialReservedTokenIdentifier(LocalDateTime tid) {
    //     return (tid.getHour() == 0 && tid.getMinute() == 1);
    // }

    // private LocalDateTime getAvailableTid(String drn, LocalDateTime tid) {
    //     String key = hash(String.format("%s%s", drn, tid.toLocalDate().toString()));
    //     if (redisHasKey(key)) {
    //         getAvailableTid(drn, tid.plusMinutes(1));
    //     }
    //     return tid;
    // }

    // private String hash(String str) {
    //     return Base64.getEncoder().encodeToString(str.getBytes());
    // }

    // private List<Token> generateToken(String requestID, Map<String, String> params, String userRef)
    //         throws Exception {
    //     String meterNo = params.get("decoder_reference_number");

    //     List<Optional<? extends co.nxtgrid.token.domain.token.Token>>
    //             tokens = new ArrayList<>();

    //     Generator.TokenType tokenType = (params.containsKey("type") &&
    //             params.get("type").equals("prism-thrift")) ? Generator.TokenType.PRISM_THRIFT
    //             : Generator.TokenType.NATIVE;

    //     switch (String.format("%s,%s",
    //             params.get("class"),
    //             params.get("subclass"))) {
    //         // -- class 0
    //         case ("0,0"):
    //             tokens.add(new TransferElectricityTokenGenerator(requestID, tokenType)
    //                             .generateElectricityToken(params));
    //             break;

    //         case ("0,1"):
    //             tokens.add(new TransferWaterTokenGenerator(requestID, tokenType)
    //                     .generateWaterToken(params));
    //             break;

    //         case ("0,2"):
    //             tokens.add(new TransferGasTokenGenerator(requestID, tokenType)
    //                     .generateGasToken(params));
    //             break;

    //         // -- class 1
    //         case ("1,0"):
    //             tokens.add(new InitiateMeterTestOrDisplay1TokenGenerator(requestID, tokenType)
    //                     .generateInitiateMeterTestOrDisplay1Token(params));
    //             break;

    //         case ("1,1"):
    //             tokens.add(new InitiateMeterTestOrDisplay2TokenGenerator(requestID, tokenType)
    //                     .generateInitiateMeterTestOrDisplay2Token(params));
    //             break;

    //         // -- class 2
    //         case ("2,0"):
    //             tokens.add(new SetMaximumPowerLimitTokenGenerator(requestID, tokenType)
    //                     .generateSetMaximumPowerLimitToken(params));
    //             break;

    //         case ("2,1"):
    //             tokens.add(new ClearCreditTokenGenerator(requestID, tokenType)
    //                     .generateClearCreditToken(params));
    //             break;

    //         case ("2,2"):
    //             tokens.add(new SetTariffRateTokenGenerator(requestID, tokenType)
    //                     .generateSetTariffRateToken(params));
    //             break;

    //         // -- KCTs
    //         case ("2,3"):
    //         case ("2,4"):
    //             List<Class2Token> genTokens = new KeyChangeTokensGenerator(requestID, tokenType)
    //                                             .generateKeyChangeTokens(params).get();
    //             for (Class2Token token : genTokens) {
    //                 tokens.add(Optional.of(token));
    //             }
    //             break;

    //         case ("2,5"):
    //             tokens.add(new ClearTamperConditionTokenGenerator(requestID, tokenType)
    //                     .generateClearTamperConditionToken(params));
    //             break;

    //         case ("2,6"):
    //             tokens.add(new SetMaximumPhasePowerUnbalanceLimitTokenGenerator(requestID, tokenType)
    //                     .generateSetMaximumPhasePowerUnbalanceLimitToken(params));
    //             break;

    //         case ("2,7"):
    //             tokens.add(new SetWaterMeterFactorTokenGenerator(requestID, tokenType)
    //                     .generateSetWaterMeterFactorToken(params));
    //             break;

    //         default:
    //             throw new InvalidTokenClassSubclassException(
    //                     String.format("Invalid class %s, subclass %s exception",
    //                             params.get("class"),
    //                             params.get("subclass")));
    //     }
    //     return convertToken(requestID, tokens, userRef, meterNo);
    // }

    // private List<Token> convertToken(String requestID, List<Optional<? extends co.nxtgrid.token.domain.token.Token>> tokens,
    //                                   String userRef, String meterNo)
    //         throws TokenNotPresentException {
    //     List<Token> generatedTokens = new ArrayList<>();
    //     for (Optional<? extends co.nxtgrid.token.domain.token.Token> token : tokens) {
    //         if (token.isPresent()) {
    //             Token generatedToken = new Token();
    //             generatedToken.setRequestID(requestID);
    //             generatedToken.setRef(AppUtils.generateRef());
    //             generatedToken.setTokenNo(token.get().getTokenNo());
    //             generatedToken.setUserRef(userRef);
    //             generatedToken.setTokenType(token.get().getType());
    //             generatedToken.setMeterNo(meterNo);
    //             generatedToken.setCreatedAt(Instant.now());
    //             generatedTokens.add(generatedToken);
    //         } else {
    //             throw new TokenNotPresentException(StringConstants.TOKEN_NOT_AVAILABLE);
    //         }
    //     }
    //     return generatedTokens;
    // }

    // private void redisStore(String key, String value) {
    //     redisTemplate.opsForValue().set(key, value);
    //     redisTemplate.expire(key, Duration.ofMinutes(1));
    // }

    // private boolean redisHasKey(String key) {
    //     return redisTemplate.hasKey(key);
    // }
}
