package com.aws.lambda;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.aws.lambda.model.Address;
import com.aws.lambda.model.Response;

import java.text.MessageFormat;
import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DestinationLambda {

    private static final Logger LOGGER = Logger.getLogger(DestinationLambda.class.getSimpleName());

    private static final AmazonDynamoDB ddb = AmazonDynamoDBClientBuilder.defaultClient();
    private static final DynamoDB dynamoDB = new DynamoDB(ddb);
    private static final String TABLE_NAME = "Address-1689763516";


    public void handler(Response input) {

        if (input == null) throw new IllegalStateException("Invalid state: No address");

        LOGGER.info("Starting  operation: ");

        Table table = dynamoDB.getTable(TABLE_NAME);

        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.log(Level.INFO, String.valueOf(input.toString()));
        }

        Collection<Address> addresses = Objects.isNull(input.getAddressList()) ? Collections.emptyList() : input.getAddressList();

        try {

            for (Address address : addresses) {
                insertIntoDynamoDB(table, address);
            }

        } catch (AmazonServiceException e) {
            LOGGER.info(e.getMessage());
        }

        LOGGER.info("Ending  operation: ");
    }

    private void insertIntoDynamoDB(Table table, Address address) {

        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.log(Level.INFO, MessageFormat.format("Inserting  address: {0}", address));
        }

        //TODO Replace with bulk upload
        Item item = new Item()
                .withPrimaryKey("AddressId", Instant.now().toEpochMilli() + Math.random())
                .withString("StreetName", Objects.toString(address.getStreetName(), ""))
                .withString("City", Objects.toString(address.getCity(), ""))
                .withString("State", Objects.toString(address.getState(), ""))
                .withString("Country", Objects.toString(address.getCountry(), ""));
        table.putItem(item);

        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.log(Level.INFO, MessageFormat.format("Inserting  item: {0}", item));
        }
    }

}

