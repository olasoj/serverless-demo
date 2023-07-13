package com.aws.lambda.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;

public class AddressJsonDeserializer extends JsonDeserializer<Address> {

    @Override
    public Address deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException {
        ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();
        ObjectNode root = mapper.readTree(jsonParser);
        return mapper.readValue(root.toString(), Address.class);
    }
}
