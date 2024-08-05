package com.example.StockPickerController;

import com.example.StockPickerFetch.StockPickerFetch;
import com.example.StockPickerModel.StockData;
import com.example.StockScalperFetch.StockScalperFetch;
import com.example.SharedData.SharedData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController

@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/stocks")
public class StockPickerController {

    @Autowired
    private StockPickerFetch stockPickerFetch;

    @Autowired
    private StockScalperFetch stockScalperFetch;
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/live")
    public List<StockData> getLiveStockData(
            @RequestParam String symbols,
            @RequestParam String source,
            @RequestParam(required = false) String earningsOption
    ) {
        // Validate the input parameters
        Objects.requireNonNull(symbols, "symbols must not be null");
        Objects.requireNonNull(source, "source must not be null");

        // Create the filters map
        Map<String, Object> filters = new HashMap<>();
        filters.put("symbols", symbols);
        if (earningsOption != null) {
            filters.put("earningsOption", earningsOption);
        }

        // Fetch the appropriate API key from SharedData
        String apiKey = SharedData.getInstance().getApiKey(source);

        // Fetch data based on the source
        if ("alpha_vantage".equals(source)) {
            StockData stockData = stockPickerFetch.fetchStockData(symbols, "daily", apiKey); // Adjust the time series as needed
            return Collections.singletonList(stockData); // Wrap in a list
        } else if ("polygon".equals(source)) {
            StockData stockData = stockPickerFetch.fetchStockDataFromPolygon(symbols, apiKey);
            return Collections.singletonList(stockData); // Wrap in a list
        }
        else if ("finnhub".equals(source)) {
            StockData stockData = stockPickerFetch.fetchLiveStockDataFromFinnhub(symbols, apiKey);
            return Collections.singletonList(stockData);
        }

        return List.of(); // Return an empty list if no valid source is specified
    }


    @GetMapping("/earnings/alpha")
    public StockData getEarningsDataFromAlphaVantage(@RequestParam String symbol) {
        String apiKey = SharedData.getInstance().getApiKey("alpha_vantage");
        return stockPickerFetch.fetchEarningsDataFromAlphaVantage(symbol, apiKey);
    }

    @GetMapping("/earnings/polygon")
    public StockData getEarningsDataFromPolygon(@RequestParam String symbol) {
        String apiKey = SharedData.getInstance().getApiKey("polygon");
        return stockPickerFetch.fetchEarningsDataFromPolygon(symbol, apiKey);
    }
    @GetMapping("/earnings/finnhub")
    public StockData getEarningsDataFromFinnhub(@RequestParam String symbol) {
        String apiKey = SharedData.getInstance().getApiKey("finnhub");
        return stockPickerFetch.fetchEarningsDataFromFinnhub(symbol, apiKey);
    }

    @PostMapping("/scalper")
    public List<StockData> getScalperStocks(@RequestBody Map<String, Object> filters) {
        String source = (String) filters.get("source");
        String apiKey = SharedData.getInstance().getApiKey(source);

        if ("alpha_vantage".equals(source)) {
            return stockScalperFetch.fetchScalperStockData(filters, apiKey);
        } else if ("polygon".equals(source)) {
            return stockScalperFetch.fetchScalperStockData(filters, apiKey);
        }

        return List.of(); // Return an empty list if no valid source is specified
    }
}
