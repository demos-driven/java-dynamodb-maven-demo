package com.example.myproject.processor;

import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import com.example.myproject.builder.DynamoDBBuilder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.example.myproject.config.DynamoDBConfig.*;
import static org.junit.Assert.*;

public class MoviesItemCRUD {

    private static final Table table = DynamoDBBuilder.getInstance().getTable(MOVIES);

    private static final int year = 2015;
    private static final String title = "The Big New Movie";

    public static void main(String[] args) {
        createItem();
        readItem();
        updateItem();
        deleteItem();
    }

    private static void createItem() {
        System.out.println("Adding item...");

        final Map<String, Object> infoMap = new HashMap<>();
        infoMap.put("plot", "Nothing happens at all.");
        infoMap.put("rating", 0);

        Item item = new Item()
                .withPrimaryKey(YEAR, year, TITLE, title)
                .withMap(INFO, infoMap);

        PutItemOutcome outcome = table.putItem(item);

        System.out.println("PutItem succeed: " + outcome.getPutItemResult());
    }

    private static void readItem() {
        System.out.println("Getting a item...");

        GetItemSpec spec = new GetItemSpec().withPrimaryKey(YEAR, year, TITLE, title);

        Item outcome = table.getItem(spec);

        assertNotNull(outcome);
        assertEquals(year, outcome.getInt(YEAR));
        assertEquals(title, outcome.getString(TITLE));

        System.out.println("GetItem succeed: " + outcome);
    }

    private static void updateItem() {
        System.out.println("Updating item...");

        final Map<String, Object> infoMap = new HashMap<>();
        infoMap.put("plot", "Everything happens all at once.");
        infoMap.put("rating", 5.5);
        infoMap.put("actors", Arrays.asList("Larry", "Moe", "Curly"));

        AttributeUpdate infoUpdate = new AttributeUpdate(INFO).put(infoMap);

        UpdateItemSpec updateSpec = new UpdateItemSpec()
                .withPrimaryKey(YEAR, year, TITLE, title)
                .withAttributeUpdate(infoUpdate)
                .withReturnValues(ReturnValue.UPDATED_NEW);

        UpdateItemOutcome updateOutcome = table.updateItem(updateSpec);

        // assert validate
        Item readOutcome = table.getItem(new GetItemSpec().withPrimaryKey(YEAR, year, TITLE, title));

        assertEquals(infoMap.get("plot"), readOutcome.getMap("info").get("plot"));
        assertEquals(infoMap.get("rating").toString(), readOutcome.getMap("info").get("rating").toString());
        assertEquals(infoMap.get("actors"), readOutcome.getMap("info").get("actors"));

        System.out.println("UpdateItem succeeded: " + updateOutcome.getUpdateItemResult());
    }

    private static void deleteItem() {
        System.out.println("Delete item...");

        DeleteItemSpec deleteSpec = new DeleteItemSpec().withPrimaryKey(YEAR, year, TITLE, title);

        DeleteItemOutcome deleteOutcome = table.deleteItem(deleteSpec);

        // assert validate
        Item readOutcome = table.getItem(new GetItemSpec().withPrimaryKey(YEAR, year, TITLE, title));

        assertNull(readOutcome);

        System.out.println("DeleteItem succeeded: " + deleteOutcome.getDeleteItemResult());
    }
}
