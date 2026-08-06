package co.nxtgrid.api;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

/**
 * Deserializes {@link TokenType} from the JSON {@code type} string.
 *
 * <p>Accepts deprecated wire value {@code TOP_UP} and maps it to
 * {@link TokenType#TOP_UP_KWH} immediately.
 */
public class TokenTypeDeserializer extends JsonDeserializer<TokenType> {

    @Override
    public TokenType deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        String value = parser.getValueAsString();
        if (value == null) {
            return null;
        }
        if ("TOP_UP".equals(value) || "TOP_UP_KWH".equals(value)) {
            return TokenType.TOP_UP_KWH;
        }
        try {
            return TokenType.valueOf(value);
        } catch (IllegalArgumentException ex) {
            throw InvalidFormatException.from(parser, "Invalid TokenType value", value, TokenType.class);
        }
    }
}
