package com.example.StockScalperFetch;

        import com.example.StockPickerModel.StockData;
        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;
        import org.springframework.beans.factory.annotation.Value;
        import org.springframework.stereotype.Service;
        import org.springframework.web.client.RestTemplate;
        import org.springframework.http.ResponseEntity;

        import java.util.ArrayList;
        import java.util.List;
        import java.util.Map;

@Service
public class StockScalperFetch {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${alpha.api.key}")
    private String alphaApiKey;

    @Value("${polygon.api.key}")
    private String polygonApiKey;

    public List<StockData> fetchLiveStockData(String symbols) {
        // Default to Alpha Vantage for live data
        String url = "https://www.alphavantage.co/query?function=BATCH_STOCK_QUOTES&symbols=" + symbols + "&apikey=" + alphaApiKey;

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        String responseBody = response.getBody();

        // Parse the response
        return parseAlphaVantageResponse(responseBody);
    }

    public List<StockData> fetchScalperStockData(Map<String, Object> filters, String apiKey) {
        String source = (String) filters.get("source");
        List<StockData> stockDataList = new ArrayList<>();

        if ("alpha_vantage".equals(source)) {
            // Fetch and parse Alpha Vantage data
            String symbols = getSymbolsFromFilters(filters);
            String url = "https://www.alphavantage.co/query?function=BATCH_STOCK_QUOTES&symbols=" + symbols + "&apikey=" + alphaApiKey;
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            String responseBody = response.getBody();
            stockDataList = parseAlphaVantageResponse(responseBody);
        } else if ("polygon".equals(source)) {
            // Fetch and parse Polygon data
            String symbols = getSymbolsFromFilters(filters);
            String url = "https://api.polygon.io/v1/last_quote/stocks?tickers=" + symbols + "&apiKey=" + polygonApiKey;
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            String responseBody = response.getBody();
            stockDataList = parsePolygonResponse(responseBody);
        }

        // Apply additional filtering logic based on other filter criteria
        return applyAdditionalFilters(stockDataList, filters);
    }

    private List<StockData> parseAlphaVantageResponse(String responseBody) {
        List<StockData> stockDataList = new ArrayList<>();
        // Implement JSON parsing logic
        try {
            JSONObject jsonObject = new JSONObject(responseBody);
            JSONArray stockArray = jsonObject.getJSONArray("Stock Quotes");
            for (int i = 0; i < stockArray.length(); i++) {
                JSONObject stockObject = stockArray.getJSONObject(i);
                StockData stockData = new StockData();
                stockData.setSymbol(stockObject.getString("1. symbol"));
                stockData.setCurrentPrice(stockObject.getDouble("2. price"));
                stockData.setVolume(stockObject.getInt("3. volume"));
                stockDataList.add(stockData);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return stockDataList;
    }

    private List<StockData> parsePolygonResponse(String responseBody) {
        List<StockData> stockDataList = new ArrayList<>();
        // Implement JSON parsing logic
        try {
            JSONObject jsonObject = new JSONObject(responseBody);
            JSONArray stockArray = jsonObject.getJSONArray("results");
            for (int i = 0; i < stockArray.length(); i++) {
                JSONObject stockObject = stockArray.getJSONObject(i);
                StockData stockData = new StockData();
                stockData.setSymbol(stockObject.getString("ticker"));
                stockData.setCurrentPrice(stockObject.getDouble("last"));
                stockData.setVolume(stockObject.getInt("volume"));
                stockDataList.add(stockData);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return stockDataList;
    }

    private String getSymbolsFromFilters(Map<String, Object> filters) {
        // Generate the symbols string based on the filter criteria
        // For simplicity, we assume a predefined list of symbols here
        return "DOW,NASDAQ,AAPL,GOOGL";
    }

    private List<StockData> applyAdditionalFilters(List<StockData> stockDataList, Map<String, Object> filters) {
        // Implement additional filtering logic based on the filters map
        return stockDataList;
    }
}
