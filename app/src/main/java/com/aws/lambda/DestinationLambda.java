package com.aws.lambda;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.aws.lambda.model.Address;
import com.aws.lambda.model.Response;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.Instant;
import java.util.*;

public class DestinationLambda implements RequestStreamHandler {

    private static final AmazonDynamoDB ddb = AmazonDynamoDBClientBuilder.defaultClient();
    private static final DynamoDB dynamoDB = new DynamoDB(ddb);
    private static final String TABLE_NAME = "Address";


    public Void handleRequest(Response input, Context context) {

        if (input == null) throw new IllegalStateException("Invalid state: No address");
        if (context == null) throw new IllegalStateException("Invalid state: No context");

        LambdaLogger logger = context.getLogger();
        if (logger == null) throw new IllegalStateException("Invalid state: No logger");

        logger.log("Starting  operation: ");

        Table table = dynamoDB.getTable(TABLE_NAME);

        logger.log(input.toString());
        Collection<Address> addresses = Objects.isNull(input.getAddressList()) ? Collections.emptyList() : input.getAddressList();

        try {

            for (Address address : addresses) {
                insertIntoDynamoDB(logger, table, address);

            }
        } catch (AmazonServiceException e) {
            logger.log(e.getMessage());
        }

        logger.log("Ending  operation: ");
        return null;
    }

    private void insertIntoDynamoDB(LambdaLogger logger, Table table, Address address) {
        logger.log("Inserting  address: " + address);

        Item item = new Item()
                .withPrimaryKey("AddressId", Instant.now().toEpochMilli() + Math.random())
                .withString("StreetName", Objects.toString(address.getStreetName(), ""))
                .withString("City", Objects.toString(address.getCity(), ""))
                .withString("State", Objects.toString(address.getState(), ""))
                .withString("Country", Objects.toString(address.getCountry(), ""));
        table.putItem(item);
        logger.log("Inserting  item: " + item);
    }

    private List<Address> toAddresses(byte[] input) {
        List<Address> addresses = new LinkedList<>();

        try {
            ArrayNode jsonNodes = JacksonConfig.objectMapper.readValue(input, ArrayNode.class);

            for (JsonNode jsonNode : jsonNodes) {
                Address address = JacksonConfig.objectMapper.convertValue(jsonNode, Address.class);
                addresses.add(address);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return addresses;
    }

    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {

        try {
            if (input == null) throw new IllegalStateException("Invalid state: No address");
            if (context == null) throw new IllegalStateException("Invalid state: No context");

            Response response = JacksonConfig.objectMapper.readValue(input, Response.class);
            handleRequest(response, context);

        } finally {
            if (input != null) input.close();
            if (output != null) output.close();
        }
    }
}

