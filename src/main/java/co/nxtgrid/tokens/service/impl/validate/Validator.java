package co.nxtgrid.tokens.service.impl.validate;

import co.nxtgrid.tokens.constant.StringConstants;
import co.nxtgrid.tokens.service.impl.exceptions.InvalidTokenParamsException;
import co.nxtgrid.tokens.service.impl.generate.Generator;
import co.nxtgrid.tokens.service.impl.validate.exceptions.RuleNotFoundException;
import co.nxtgrid.tokens.service.impl.validate.exceptions.RuleNotImplementedException;

import java.util.Map;

public class Validator {

    public static boolean validate(Map<String, String> params)
        throws Exception {
        if (params.containsKey("class")
                && params.containsKey("subclass")) {
            Generator.TokenType tokenType
                    = params.containsKey("type") && params.get("type").toUpperCase().equals("PRISM-THRIFT")
                            ? Generator.TokenType.PRISM_THRIFT : Generator.TokenType.NATIVE;
            Map<String, String> rules = getRules(params.get("class"),
                                                    params.get("subclass"),
                                                    tokenType);
            return validateRules(rules, params);
        }
        throw new InvalidTokenParamsException(StringConstants.NO_CLASS_OR_SUBCLASS);
    }

    private static Map<String, String> getRules(String tokenClass,
                                                String tokenSubclass,
                                                Generator.TokenType tokenType)
        throws Exception {
        if (tokenType == Generator.TokenType.NATIVE) {
            for (Map.Entry<String, Map<String, String>> entry
                    : RulesNative.mapping.entrySet()) {
                if (String.format("%s,%s", tokenClass, tokenSubclass)
                        .matches(entry.getKey())) {
                    return entry.getValue();
                }
            }
        } else if (tokenType == Generator.TokenType.PRISM_THRIFT) {
            for (Map.Entry<String, Map<String, String>> entry
                    : RulesPrism.mapping.entrySet()) {
                if (String.format("%s,%s", tokenClass, tokenSubclass)
                        .matches(entry.getKey())) {
                    return entry.getValue();
                }
            }
        }
        throw new RuleNotFoundException(String.format("Rule not found for class %s subclass %s"));
    }

    private static boolean validateRules(Map<String, String> rules,
                                 Map<String, String>params)
        throws Exception {
        for (Map.Entry<String, String> rule : rules.entrySet()) {
            if (params.containsKey(rule.getKey())) {
                if (!params.get(rule.getKey()).matches(rule.getValue()))
                    throw new InvalidTokenParamsException(
                            String.format("%s %s %s", StringConstants.INVALID_TOKEN_PARAM,
                                                            rule.getKey(),
                                                            params.get(rule.getKey()))
                    );
            } else {
                throw new RuleNotImplementedException(String.format("Rule [%s] not implemented in params", rule.getKey()));
            }
        }
        return true;
    }
}
