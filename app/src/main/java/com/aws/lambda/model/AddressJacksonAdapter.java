package com.aws.lambda.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;

public class AddressJacksonAdapter {

    public static final JsonDeserializer<Address> instantJsonDeserializer = new JsonDeserializer<>() {
        @Override
        public Address deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {

            ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();
            ObjectNode root = mapper.readTree(jsonParser);
            return mapper.readValue(root.toString(), Address.class);
        }
    };

    public static final JsonSerializer<Address> instantJsonSerializer = new JsonSerializer<>() {
        @Override
        public void serialize(Address instant, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

            String str = instant.toString();
            jsonGenerator.writeString(str);
        }
    };

    private AddressJacksonAdapter() {
    }

}
