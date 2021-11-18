package com.example.myproject.processor;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.example.myproject.builder.DynamoDBBuilder;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import static com.example.myproject.config.DynamoDBConfig.*;

public class MoviesDataLoad {

    public static void main(String[] args) throws IOException {
        Table table = DynamoDBBuilder.getInstance().getTable(MOVIES);

        String dataPath = Thread.currentThread()
                .getContextClassLoader()
                .getResource(MOVIES_DATA_FILE)
                .getFile();

        JsonParser parser = new JsonFactory().createParser(new File(dataPath));

        JsonNode rootNode = new ObjectMapper().readTree(parser);
        Iterator<JsonNode> iter = rootNode.iterator();

        ObjectNode currentNode;

        while (iter.hasNext()) {
            currentNode = (ObjectNode)iter.next();

            int year = currentNode.path(YEAR).asInt();
            String title = currentNode.path(TITLE).asText();

            Item item = new Item()
                    .withPrimaryKey(YEAR, year, TITLE, title)
                    .withJSON(INFO, currentNode.path(INFO).toString());

            table.putItem(item);

            System.out.println("PutItem succeeded: " + year + " " + title);
        }

        parser.close();
    }
}
