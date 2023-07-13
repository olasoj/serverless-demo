package com.aws.lambda.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class AddressJsonSerializer extends JsonSerializer<Address> {

    @Override
    public void serialize(Address value, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {

        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("streetName", value.getStreetName());
        jsonGenerator.writeStringField("city", value.getCity());
        jsonGenerator.writeStringField("state", value.getState());
        jsonGenerator.writeStringField("country", value.getCountry());
        jsonGenerator.writeEndObject();
    }

}
