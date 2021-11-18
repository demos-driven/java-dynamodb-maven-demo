package com.example.myproject.processor;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.*;
import com.example.myproject.builder.DynamoDBBuilder;

import static com.example.myproject.config.DynamoDBConfig.*;
import static java.util.Arrays.asList;

public class MoviesCreateTable {

    public static void main(String[] args) throws InterruptedException {
        DynamoDB dynamoDB = DynamoDBBuilder.getInstance();

        System.out.println("Attempting to create table, please wait...");

        Table table = dynamoDB.createTable(MOVIES,
                // key definition
                asList(
                        new KeySchemaElement(YEAR, KeyType.HASH),
                        new KeySchemaElement(TITLE, KeyType.RANGE)
                ),
                // attributes definition
                asList(
                        new AttributeDefinition(YEAR, ScalarAttributeType.N),
                        new AttributeDefinition(TITLE, ScalarAttributeType.S)
                ),
                // read-write capacity config
                new ProvisionedThroughput(10L, 10L)
        );

        table.waitForActive();

        System.out.println("Success. Table status: " + table.getDescription());
    }
}
