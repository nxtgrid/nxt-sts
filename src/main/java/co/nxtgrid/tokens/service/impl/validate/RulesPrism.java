package co.nxtgrid.tokens.service.impl.validate;

import java.util.HashMap;
import java.util.Map;

public interface RulesPrism {

    Map<String, String> baseMapping = new HashMap<>() {{
        put("host", "^((?:[0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])[.]){3}(?:[0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])$");
        put("port", "^((6553[0-5])|(655[0-2][0-9])|(65[0-4][0-9]{2})|(6[0-4][0-9]{3})|([1-5][0-9]{4})|([0-5]{0,5})|([0-9]{1,4}))$");
        put("realm", "^\\S+$");
        put("username", "^\\S+$");
        put("password", "^\\S+$");
    }};

    Map<String, Map<String, String>> mapping = new HashMap<>() {{

        Map<String, String> mapping_0_0_3 = new HashMap<>(){{
            put("decoder_reference_number", "^[0-9]{11,13}$");
            put("encryption_algorithm", "^sta$|^dea$|^misty1$");
            put("token_carrier_type", "^numeric$");
            put("supply_group_code", "^[0-9]{6}$");
            put("key_revision_no", "^[1-9]{1}$");
            put("key_expiry_no", "^([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])$");
            put("tariff_index", "^[0-9]{2}$");
            put("amount", "^\\d+$|^[0-9].*\\.[0-9]{2}$");
        }};
        mapping_0_0_3.putAll(baseMapping);

        put("0,[0-3]", mapping_0_0_3);

        Map<String, String> mapping_1_0_1 = new HashMap<>() {{
            put("control", "^\\d+$");
            put("manufacturer_code", "^[0-9]{2}$|^[0-9]{4}$");
        }};
        mapping_1_0_1.putAll(baseMapping);

        put("1,[0-1]", mapping_1_0_1);

        Map<String, String> mapping_2_0 = new HashMap<>(){{
            put("maximum_power_limit", "^\\d+$");
            put("flag_token_type", "^[0-9]$|^10|11$");
            put("flag_token_value", "^0|1$");
        }};
        mapping_2_0.putAll(baseMapping);

        put("2,0", mapping_2_0);

        Map<String, String> mapping_2_1 = new HashMap<>() {{
            put("decoder_reference_number", "^[0-9]{11,13}$");
            put("encryption_algorithm", "^sta$|^dea$|^misty1$");
            put("token_carrier_type", "^numeric$");
            put("supply_group_code", "^[0-9]{6}$");
            put("key_revision_no", "^[1-9]{1}$");
            put("key_expiry_no", "^([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])$");
            put("tariff_index", "^[0-9]{2}$");
        }};
        mapping_2_1.putAll(baseMapping);

        put("2,1", mapping_2_1);

        Map<String, String> mapping_2_2 = new HashMap<>() {{
            put("decoder_reference_number", "^[0-9]{11,13}$");
            put("encryption_algorithm", "^sta$|^dea$|^misty1$");
            put("token_carrier_type", "^numeric$");
            put("supply_group_code", "^[0-9]{6}$");
            put("key_revision_no", "^[1-9]{1}$");
            put("key_expiry_no", "^([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])$");
            put("tariff_index", "^[0-9]{2}$");
        }};
        mapping_2_2.putAll(baseMapping);

        put("2,2", mapping_2_2);

        Map<String, String> mapping_2_3_4 = new HashMap<>() {{
            put("decoder_reference_number", "^[0-9]{11,13}$");
            put("encryption_algorithm", "^sta$|^dea$|^misty1$");
            put("token_carrier_type", "^numeric$");
            put("supply_group_code", "^[0-9]{6}$");
            put("key_revision_no", "^[1-9]{1}$");
            put("key_expiry_no", "^([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])$");
            put("tariff_index", "^[0-9]{2}$");
            put("allow_3Kct", "^true|false$");
            put("new_supply_group_code", "^[0-9]{6}$");
            put("new_key_revision_no", "^[1-9]{1}$");
            put("new_tariff_index", "^[0-9]{2}$");
        }};
        mapping_2_3_4.putAll(baseMapping);

        put("2,[3-4]", mapping_2_3_4);

        Map<String, String> mapping_2_5 = new HashMap<>() {{
            put("decoder_reference_number", "^[0-9]{11,13}$");
            put("encryption_algorithm", "^sta$|^dea$|^misty1$");
            put("token_carrier_type", "^numeric$");
            put("supply_group_code", "^[0-9]{6}$");
            put("key_revision_no", "^[1-9]{1}$");
            put("key_expiry_no", "^([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])$");
            put("tariff_index", "^[0-9]{2}$");
        }};
        mapping_2_5.putAll(baseMapping);

        put("2,5", mapping_2_5);

        Map<String, String> mapping_2_6 = new HashMap<>() {{
            put("decoder_reference_number", "^[0-9]{11,13}$");
            put("encryption_algorithm", "^sta$|^dea$|^misty1$");
            put("token_carrier_type", "^numeric$");
            put("supply_group_code", "^[0-9]{6}$");
            put("key_revision_no", "^[1-9]{1}$");
            put("key_expiry_no", "^([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])$");
            put("tariff_index", "^[0-9]{2}$");
        }};
        mapping_2_6.putAll(baseMapping);

        put("2,6", mapping_2_6);

        Map<String, String> mapping_2_7  = new HashMap<>() {{
            put("decoder_reference_number", "^[0-9]{11,13}$");
            put("encryption_algorithm", "^sta$|^dea$|^misty1$");
            put("token_carrier_type", "^numeric$");
            put("supply_group_code", "^[0-9]{6}$");
            put("key_revision_no", "^[1-9]{1}$");
            put("key_expiry_no", "^([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])$");
            put("tariff_index", "^[0-9]{2}$");
        }};
        mapping_2_7.putAll(baseMapping);

        put("2,7", mapping_2_7);
    }};
}
