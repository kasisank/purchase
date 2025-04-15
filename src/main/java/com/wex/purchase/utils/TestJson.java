package com.wex.purchase.utils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.wex.purchase.utils.dataformat.Root;

import java.util.ArrayList;
import java.util.List;

public class TestJson {

    public static void main(String[] args) {
        String json = "{\n" +
                "  \"data\" : [ {\n" +
                "    \"country_currency_desc\" : \"Canada-Dollar\",\n" +
                "    \"exchange_rate\" : \"1.355\",\n" +
                "    \"record_date\" : \"2024-03-31\"\n" +
                "  }, {\n" +
                "    \"country_currency_desc\" : \"Canada-Dollar\",\n" +
                "    \"exchange_rate\" : \"1.37\",\n" +
                "    \"record_date\" : \"2024-06-30\"\n" +
                "  }, {\n" +
                "    \"country_currency_desc\" : \"Canada-Dollar\",\n" +
                "    \"exchange_rate\" : \"1.352\",\n" +
                "    \"record_date\" : \"2024-09-30\"\n" +
                "  }, {\n" +
                "    \"country_currency_desc\" : \"Canada-Dollar\",\n" +
                "    \"exchange_rate\" : \"1.438\",\n" +
                "    \"record_date\" : \"2024-12-31\"\n" +
                "  }, {\n" +
                "    \"country_currency_desc\" : \"Canada-Dollar\",\n" +
                "    \"exchange_rate\" : \"1.435\",\n" +
                "    \"record_date\" : \"2025-03-31\"\n" +
                "  }, {\n" +
                "    \"country_currency_desc\" : \"Mexico-Peso\",\n" +
                "    \"exchange_rate\" : \"16.558\",\n" +
                "    \"record_date\" : \"2024-03-31\"\n" +
                "  }, {\n" +
                "    \"country_currency_desc\" : \"Mexico-Peso\",\n" +
                "    \"exchange_rate\" : \"18.566\",\n" +
                "    \"record_date\" : \"2024-03-31\"\n" +
                "  }, {\n" +
                "    \"country_currency_desc\" : \"Mexico-Peso\",\n" +
                "    \"exchange_rate\" : \"18.296\",\n" +
                "    \"record_date\" : \"2024-06-30\"\n" +
                "  }, {\n" +
                "    \"country_currency_desc\" : \"Mexico-Peso\",\n" +
                "    \"exchange_rate\" : \"19.655\",\n" +
                "    \"record_date\" : \"2024-09-30\"\n" +
                "  }, {\n" +
                "    \"country_currency_desc\" : \"Mexico-Peso\",\n" +
                "    \"exchange_rate\" : \"20.704\",\n" +
                "    \"record_date\" : \"2024-12-31\"\n" +
                "  }, {\n" +
                "    \"country_currency_desc\" : \"Mexico-Peso\",\n" +
                "    \"exchange_rate\" : \"20.386\",\n" +
                "    \"record_date\" : \"2025-03-31\"\n" +
                "  } ],\n" +
                "  \"meta\" : {\n" +
                "    \"count\" : 11,\n" +
                "    \"labels\" : {\n" +
                "      \"country_currency_desc\" : \"Country - Currency Description\",\n" +
                "      \"exchange_rate\" : \"Exchange Rate\",\n" +
                "      \"record_date\" : \"Record Date\"\n" +
                "    },\n" +
                "    \"dataTypes\" : {\n" +
                "      \"country_currency_desc\" : \"STRING\",\n" +
                "      \"exchange_rate\" : \"NUMBER\",\n" +
                "      \"record_date\" : \"DATE\"\n" +
                "    },\n" +
                "    \"dataFormats\" : {\n" +
                "      \"country_currency_desc\" : \"String\",\n" +
                "      \"exchange_rate\" : \"10.2\",\n" +
                "      \"record_date\" : \"YYYY-MM-DD\"\n" +
                "    },\n" +
                "    \"total_count\" : 11,\n" +
                "    \"total_pages\" : 1\n" +
                "  },\n" +
                "  \"links\" : {\n" +
                "    \"self\" : \"&page%5Bnumber%5D=1&page%5Bsize%5D=100\",\n" +
                "    \"first\" : \"&page%5Bnumber%5D=1&page%5Bsize%5D=100\",\n" +
                "    \"prev\" : null,\n" +
                "    \"next\" : null,\n" +
                "    \"last\" : \"&page%5Bnumber%5D=1&page%5Bsize%5D=100\"\n" +
                "  }\n" +
                "}";
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
        Root root;

        {
            try {
                root = objectMapper.readValue(json, new TypeReference<Root>() {
                });
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }
}


