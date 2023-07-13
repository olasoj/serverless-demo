package com.aws.lambda;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JacksonConfig {

    public static final ObjectMapper objectMapper;

    static {
        ObjectMapper objectMapperLocal = new ObjectMapper();
        objectMapperLocal.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        objectMapperLocal.configure(SerializationFeature.INDENT_OUTPUT, true);

        objectMapperLocal.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapperLocal.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);


        objectMapper = objectMapperLocal;
    }

    private JacksonConfig() {
    }
}
