package com.example.StockPickerController;

import com.example.StockPickerFetch.StockPickerFetch;
import com.example.StockPickerModel.StockData;
import com.example.StockScalperFetch.StockScalperFetch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
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

        // Fetch data based on the source
        if ("alpha_vantage".equals(source)) {
            StockData stockData = stockPickerFetch.fetchStockData(symbols, "daily"); // Adjust the time series as needed
            return Collections.singletonList(stockData); // Wrap in a list
        } else if ("polygon".equals(source)) {
            StockData stockData = stockPickerFetch.fetchStockDataFromPolygon(symbols);
            return Collections.singletonList(stockData); // Wrap in a list
        }

        return List.of(); // Return an empty list if no valid source is specified
    }

    @GetMapping("/earnings/alpha")
    public StockData getEarningsDataFromAlphaVantage(@RequestParam String symbol) {
        return stockPickerFetch.fetchEarningsDataFromAlphaVantage(symbol);
    }

    @GetMapping("/earnings/polygon")
    public StockData getEarningsDataFromPolygon(@RequestParam String symbol) {
        return stockPickerFetch.fetchEarningsDataFromPolygon(symbol);
    }

    @PostMapping("/scalper")
    public List<StockData> getScalperStocks(@RequestBody Map<String, Object> filters) {
        String source = (String) filters.get("source");

        if ("alpha_vantage".equals(source)) {
            return stockScalperFetch.fetchScalperStockData(filters);
        } else if ("polygon".equals(source)) {
            return stockScalperFetch.fetchScalperStockData(filters);
        }

        return List.of(); // Return an empty list if no valid source is specified
    }
}
