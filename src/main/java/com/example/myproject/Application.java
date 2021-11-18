package com.example.myproject;

import com.example.myproject.processor.MoviesCreateTable;
import com.example.myproject.processor.MoviesDataLoad;
import com.example.myproject.processor.MoviesItemCRUD;

public class Application {

    public static void main(String[] args) throws Exception {
        // one-time proceed
        MoviesCreateTable.main(args);

        MoviesDataLoad.main(args);

        MoviesItemCRUD.main(args);
    }
}
