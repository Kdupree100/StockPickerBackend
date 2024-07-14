package com.example.StockDataParser;


import com.example.StockQuote.StockQuote;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class StockDataParser {
    public static void parseStockData(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            StockQuote quote = mapper.readValue(json, StockQuote.class);
            System.out.println("Symbol: " + quote.getSymbol());
            System.out.println("Latest Price: $" + quote.getLatestPrice());
            // Extract more data as needed
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void parseStockData(List<String> someCollection) {
        try {
            // Assume data fetching and parsing logic
            Iterator<String> iterator = someCollection.iterator();
            while (iterator.hasNext()) {
                String data = iterator.next();
                // Process data
            }
        } catch (NoSuchElementException e) {
            // Handle the case where there are no more elements
            // Log the error or handle it gracefully
            e.printStackTrace(); // Example, replace with appropriate handling
        } catch (Exception e) {
            // Handle other exceptions if necessary
            e.printStackTrace();
        }
    }

}
