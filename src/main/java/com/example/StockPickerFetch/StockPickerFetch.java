    package com.example.StockPickerFetch;

    import com.example.StockPickerModel.StockData;
    import com.fasterxml.jackson.databind.JsonNode;
    import com.fasterxml.jackson.databind.ObjectMapper;
    import org.springframework.stereotype.Service;

    import java.io.BufferedReader;
    import java.io.IOException;
    import java.io.InputStreamReader;
    import java.net.HttpURLConnection;
    import java.net.URL;
    import java.util.logging.Level;
    import java.util.logging.Logger;

    @Service
    public class StockPickerFetch {
        private static final Logger LOGGER = Logger.getLogger(StockPickerFetch.class.getName());
        private static final String ALPHA_API_KEY = "RQQLH20HE8X5D9RF";
        private static final String POLYGON_API_KEY = "pUwEx7PQH9FayYl_nNpNH8wK3R0mKKrE";

        private static final String FINNHUB_API_KEY = "cqm0to9r01qoqqs7t3s0cqm0to9r01qoqqs7t3sg";


        public StockData fetchStockData(String symbol, String timeSeries, String apiKey) {
            String finalApiKey = (apiKey == null || apiKey.isEmpty()) ? ALPHA_API_KEY : apiKey;
            try {
                String function = timeSeries.equalsIgnoreCase("daily") ? "TIME_SERIES_DAILY" : "TIME_SERIES_MONTHLY";
                URL url = new URL("https://www.alphavantage.co/query?function=" + function + "&symbol=" + symbol + "&apikey=" + ALPHA_API_KEY);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                int responseCode = conn.getResponseCode();
                LOGGER.log(Level.INFO, "Alpha Vantage Response Code: {0}", responseCode);

                if (responseCode != 200) {
                    LOGGER.log(Level.SEVERE, "Failed : HTTP error code : {0}", responseCode);
                    throw new RuntimeException("Failed : HTTP error code : " + responseCode);
                }

                BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                StringBuilder response = new StringBuilder();
                String output;
                while ((output = br.readLine()) != null) {
                    response.append(output);
                }

                conn.disconnect();
                LOGGER.log(Level.INFO, "Alpha Vantage Response: {0}", response.toString());

                StockData stockData = parseStockData(response.toString(), timeSeries);
                if (stockData == null) {
                    throw new RuntimeException("Parsed stock data is null");
                }
                return stockData;
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "IOException in fetchStockDataFromAlphaVantage", e);
                throw new RuntimeException("Failed to fetch stock data from Alpha Vantage due to IOException", e);
            } catch (RuntimeException e) {
                LOGGER.log(Level.SEVERE, "RuntimeException in fetchStockDataFromAlphaVantage", e);
                throw new RuntimeException("Failed to fetch stock data from Alpha Vantage due to RuntimeException", e);
            }
        }

        public StockData fetchStockDataFromPolygon(String symbol, String apiKey) {
            String finalApiKey = (apiKey == null || apiKey.isEmpty()) ? POLYGON_API_KEY : apiKey;
            try {
                //            URL url = new URL("https://api.polygon.io/v2/aggs/ticker/" + symbol + "/prev?adjusted=true&apiKey=" + POLYGON_API_KEY);
                //            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                //            conn.setRequestMethod("GET");
                String date = "2023-01-09"; // Set the desired date here
                URL url = new URL("https://api.polygon.io/v2/aggs/ticker/" + symbol + "/range/1/day/" + date + "/" + date + "?adjusted=true&apiKey=" + POLYGON_API_KEY);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");


                int responseCode = conn.getResponseCode();
                LOGGER.log(Level.INFO, "Polygon Response Code: {0}", responseCode);

                if (responseCode != 200) {
                    LOGGER.log(Level.SEVERE, "Failed : HTTP error code : {0}", responseCode);
                    throw new RuntimeException("Failed : HTTP error code : " + responseCode);
                }

                BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                StringBuilder response = new StringBuilder();
                String output;
                while ((output = br.readLine()) != null) {
                    response.append(output);
                }

                conn.disconnect();
                LOGGER.log(Level.INFO, "Polygon Response: {0}", response.toString());

                StockData stockData = parseStockDataFromPolygon(response.toString());
                if (stockData == null) {
                    throw new RuntimeException("Parsed stock data is null");
                }
                return stockData;
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "IOException in fetchStockDataFromPolygon", e);
                throw new RuntimeException("Failed to fetch stock data from Polygon due to IOException", e);
            } catch (RuntimeException e) {
                LOGGER.log(Level.SEVERE, "RuntimeException in fetchStockDataFromPolygon", e);
                throw new RuntimeException("Failed to fetch stock data from Polygon due to RuntimeException", e);
            }
        }

        // New method to fetch live stock data from Finnhub
        public StockData fetchLiveStockDataFromFinnhub(String symbol, String apiKey) {
            String finalApiKey = (apiKey == null || apiKey.isEmpty()) ? FINNHUB_API_KEY : apiKey;
            try {
                URL url = new URL("https://finnhub.io/api/v1/quote?symbol=" + symbol + "&token=" + FINNHUB_API_KEY);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                int responseCode = conn.getResponseCode();
                LOGGER.log(Level.INFO, "Finnhub Response Code: {0}", responseCode);

                if (responseCode != 200) {
                    LOGGER.log(Level.SEVERE, "Failed : HTTP error code : {0}", responseCode);
                    throw new RuntimeException("Failed : HTTP error code : " + responseCode);
                }

                BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                StringBuilder response = new StringBuilder();
                String output;
                while ((output = br.readLine()) != null) {
                    response.append(output);
                }

                conn.disconnect();
                LOGGER.log(Level.INFO, "Finnhub Response: {0}", response.toString());

                return parseLiveStockDataFromFinnhub(response.toString());
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "IOException in fetchLiveStockDataFromFinnhub", e);
                throw new RuntimeException("Failed to fetch live stock data from Finnhub due to IOException", e);
            }
        }


        private StockData parseStockData(String json, String timeSeries) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode rootNode = mapper.readTree(json);

                String timeSeriesKey = timeSeries.equalsIgnoreCase("daily") ? "Time Series (Daily)" : "Monthly Time Series";
                JsonNode timeSeriesNode = rootNode.path(timeSeriesKey);
                if (timeSeriesNode.isMissingNode() || !timeSeriesNode.fields().hasNext()) {
                    LOGGER.log(Level.WARNING, "No or empty '{0}' found in JSON response", timeSeriesKey);
                    return null;
                }

                String latestDate = timeSeriesNode.fieldNames().next();
                JsonNode latestData = timeSeriesNode.path(latestDate);

                String symbol = rootNode.path("Meta Data").path("2. Symbol").asText();
                double currentPrice = latestData.path("1. open").asDouble();
                double previousClose = latestData.path("4. close").asDouble();

                String growthPotential = calculateGrowthPotential(currentPrice, previousClose);
                String bargainPotential = calculateBargainPotential(currentPrice, previousClose);
                String riskLevel = calculateRiskLevel(currentPrice, previousClose);
                String sentiment = calculateSentiment(currentPrice, previousClose);
                String analystRating = calculateAnalystRating(growthPotential, bargainPotential, riskLevel, sentiment);

                double entryPoint = currentPrice - 5;
                double exitPoint = currentPrice + 5;

                String earningsDate = "N/A";
                int quarter = 0;


                return new StockData(symbol, currentPrice, entryPoint, exitPoint, earningsDate, growthPotential, bargainPotential, riskLevel, sentiment, analystRating, quarter);
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Exception in parseStockData", e);
                return null;
            }
        }

        private StockData parseStockDataFromPolygon(String json) {

            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode rootNode = mapper.readTree(json);

                JsonNode resultsNode = rootNode.path("results");
                if (resultsNode.isMissingNode() || !resultsNode.isArray() || resultsNode.size() == 0) {
                    LOGGER.log(Level.WARNING, "No or empty 'results' found in JSON response");
                    return null;
                }

                JsonNode latestData = resultsNode.get(0);

                String symbol = rootNode.path("ticker").asText();
                double currentPrice = latestData.path("o").asDouble();
                double previousClose = latestData.path("c").asDouble();

                String growthPotential = calculateGrowthPotential(currentPrice, previousClose);
                String bargainPotential = calculateBargainPotential(currentPrice, previousClose);
                String riskLevel = calculateRiskLevel(currentPrice, previousClose);
                String sentiment = calculateSentiment(currentPrice, previousClose);
                String analystRating = calculateAnalystRating(growthPotential, bargainPotential, riskLevel, sentiment);

                double entryPoint = currentPrice - 5;
                double exitPoint = currentPrice + 5;

                String earningsDate = "N/A";
                int quarter = 0;

                return new StockData(symbol, currentPrice, entryPoint, exitPoint, earningsDate, growthPotential, bargainPotential, riskLevel, sentiment, analystRating, quarter);
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Exception in parseStockDataFromPolygon", e);
                return null;
            }
        }

        private StockData parseLiveStockDataFromFinnhub(String json) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode rootNode = mapper.readTree(json);

                String symbol = rootNode.path("symbol").asText();  // Adjust as necessary
                double currentPrice = rootNode.path("c").asDouble();
                double previousClose = rootNode.path("pc").asDouble();

                String growthPotential = calculateGrowthPotential(currentPrice, previousClose);
                String bargainPotential = calculateBargainPotential(currentPrice, previousClose);
                String riskLevel = calculateRiskLevel(currentPrice, previousClose);
                String sentiment = calculateSentiment(currentPrice, previousClose);
                String analystRating = calculateAnalystRating(growthPotential, bargainPotential, riskLevel, sentiment);


                double entryPoint = currentPrice - 5;
                double exitPoint = currentPrice + 5;
                int quarter = 0;

                String earningsDate = "N/A";  // Adjust if earnings data is included

                return new StockData(symbol, currentPrice, entryPoint, exitPoint, earningsDate, growthPotential, bargainPotential, riskLevel, sentiment, analystRating, quarter);
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Exception in parseLiveStockDataFromFinnhub", e);
                return null;
            }
        }


        // New methods for fetching and parsing earnings data from Alpha Vantage
        public StockData fetchEarningsDataFromAlphaVantage(String symbol, String apiKey) {
            String finalApiKey = (apiKey == null || apiKey.isEmpty()) ? ALPHA_API_KEY : apiKey;
            try {
                URL url = new URL("https://www.alphavantage.co/query?function=EARNINGS&symbol=" + symbol + "&apikey=" + ALPHA_API_KEY);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                int responseCode = conn.getResponseCode();
                LOGGER.log(Level.INFO, "Alpha Vantage Earnings Response Code: {0}", responseCode);

                if (responseCode != 200) {
                    LOGGER.log(Level.SEVERE, "Failed : HTTP error code : {0}", responseCode);
                    throw new RuntimeException("Failed : HTTP error code : " + responseCode);
                }

                BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                StringBuilder response = new StringBuilder();
                String output;
                while ((output = br.readLine()) != null) {
                    response.append(output);
                }

                conn.disconnect();
                LOGGER.log(Level.INFO, "Alpha Vantage Earnings Response: {0}", response.toString());

                return parseEarningsDataFromAlphaVantage(response.toString());
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "IOException in fetchEarningsDataFromAlphaVantage", e);
                throw new RuntimeException("Failed to fetch earnings data from Alpha Vantage due to IOException", e);
            }
        }

        private StockData parseEarningsDataFromAlphaVantage(String json) {

            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode rootNode = mapper.readTree(json);

                JsonNode earningsNode = rootNode.path("quarterlyEarnings");
                if (earningsNode.isMissingNode() || !earningsNode.isArray() || earningsNode.size() == 0) {
                    LOGGER.log(Level.WARNING, "No or empty 'quarterlyEarnings' found in JSON response");
                    return null;
                }

                JsonNode latestEarnings = earningsNode.get(0);
                String earningsDate = latestEarnings.path("reportedDate").asText();
                int quarter = latestEarnings.path("quarter").asInt();


                return new StockData("", 0.0, 0.0, 0.0, earningsDate, "", "", "", "", "", quarter);
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Exception in parseEarningsDataFromAlphaVantage", e);
                return null;
            }
        }

        // New methods for fetching and parsing earnings data from Polygon.io
        public StockData fetchEarningsDataFromPolygon(String symbol, String apiKey) {
            String finalApiKey = (apiKey == null || apiKey.isEmpty()) ? POLYGON_API_KEY : apiKey;
            try {
                URL url = new URL("https://api.polygon.io/v2/reference/financials/" + symbol + "?limit=1&apiKey=" + POLYGON_API_KEY);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                int responseCode = conn.getResponseCode();
                LOGGER.log(Level.INFO, "Polygon Earnings Response Code: {0}", responseCode);

                if (responseCode != 200) {
                    LOGGER.log(Level.SEVERE, "Failed : HTTP error code : {0}", responseCode);
                    throw new RuntimeException("Failed : HTTP error code : " + responseCode);
                }

                BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                StringBuilder response = new StringBuilder();
                String output;
                while ((output = br.readLine()) != null) {
                    response.append(output);
                }

                conn.disconnect();
                LOGGER.log(Level.INFO, "Polygon Earnings Response: {0}", response.toString());

                return parseEarningsDataFromPolygon(response.toString());
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "IOException in fetchEarningsDataFromPolygon", e);
                throw new RuntimeException("Failed to fetch earnings data from Polygon due to IOException", e);
            }
        }

        private StockData parseEarningsDataFromPolygon(String json) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode rootNode = mapper.readTree(json);

                JsonNode resultsNode = rootNode.path("results");
                if (resultsNode.isMissingNode() || !resultsNode.isArray() || resultsNode.size() == 0) {
                    LOGGER.log(Level.WARNING, "No or empty 'results' found in JSON response");
                    return null;
                }

                JsonNode latestEarnings = resultsNode.get(0);
                String earningsDate = latestEarnings.path("period_of_report").asText();
                int quarter = latestEarnings.path("quarter").asInt();


                return new StockData("", 0.0, 0.0, 0.0, earningsDate, "", "", "", "", "", quarter);
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Exception in parseEarningsDataFromPolygon", e);
                return null;
            }
        }

        public StockData fetchEarningsDataFromFinnhub(String symbol, String apiKey) {
            String finalApiKey = (apiKey == null || apiKey.isEmpty()) ? FINNHUB_API_KEY : apiKey;
            try {
                URL url = new URL("https://www.alphavantage.co/query?function=EARNINGS&symbol=" + symbol + "&apikey=" + ALPHA_API_KEY);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                int responseCode = conn.getResponseCode();
                LOGGER.log(Level.INFO, "Finnhub Earnings Response Code: {0}", responseCode);

                if (responseCode != 200) {
                    LOGGER.log(Level.SEVERE, "Failed : HTTP error code : {0}", responseCode);
                    throw new RuntimeException("Failed : HTTP error code : " + responseCode);
                }

                BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                StringBuilder response = new StringBuilder();
                String output;
                while ((output = br.readLine()) != null) {
                    response.append(output);
                }

                conn.disconnect();
                LOGGER.log(Level.INFO, "Finnhub Earnings Response: {0}", response.toString());

                return parseEarningsDataFromFinnhub(response.toString());
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "IOException in fetchEarningsDataFromFinnhub", e);
                throw new RuntimeException("Failed to fetch earnings data from Finnhub due to IOException", e);
            }
        }

        private StockData parseEarningsDataFromFinnhub(String json) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode rootNode = mapper.readTree(json);

                JsonNode earningsNode = rootNode.path("quarterlyEarnings");
                if (earningsNode.isMissingNode() || !earningsNode.isArray() || earningsNode.size() == 0) {
                    LOGGER.log(Level.WARNING, "No or empty 'quarterlyEarnings' found in JSON response");
                    return null;
                }

                JsonNode latestEarnings = earningsNode.get(0);
                String earningsDate = latestEarnings.path("reportedDate").asText();
                int quarter = latestEarnings.path("quarter").asInt();


                return new StockData("", 0.0, 0.0, 0.0, earningsDate, "", "", "", "", "", quarter);
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Exception in parseEarningsDataFromAlphaVantage", e);
                return null;
            }
        }

        private String calculateGrowthPotential(double currentPrice, double previousClose) {
            if (currentPrice > previousClose * 1.07) {
                return "High";
            } else if (currentPrice > previousClose * 1.03) {
                return "Moderate";
            } else {
                return "Low";
            }
        }

        private String calculateBargainPotential(double currentPrice, double previousClose) {
            if (currentPrice < previousClose * 0.93) {
                return "High";
            } else if (currentPrice < previousClose * 0.97) {
                return "Moderate";
            } else {
                return "Low";
            }
        }

        private String calculateRiskLevel(double currentPrice, double previousClose) {
            double change = Math.abs(currentPrice - previousClose) / previousClose;
            if (change < 0.01) {
                return "Low";
            } else if (change < 0.03) {
                return "Moderate";
            } else {
                return "High";
            }
        }

        private String calculateSentiment(double currentPrice, double previousClose) {
            if (currentPrice > previousClose * 1.7) {
                return "Positive";
            } else if (currentPrice < previousClose) {
                return "Negative";
            }
            else if (currentPrice > previousClose * 1.4) {
                return "Potential";
            }
            else {
                return "Neutral";
            }
        }

        private String calculateAnalystRating(String growthPotential, String bargainPotential, String riskLevel, String sentiment) {
            if ("High".equals(growthPotential) && "High".equals(bargainPotential) && "Low".equals(riskLevel) && "Positive".equals(sentiment)) {
                return "Strong Buy";
            } else if ("Moderate".equals(growthPotential) && "Moderate".equals(bargainPotential) && "Moderate".equals(riskLevel) && "Positive".equals(sentiment)) {
                return "Buy";
            } else if ("Low".equals(riskLevel) && "Positive".equals(sentiment)) {
                return "Hold";
            } else {
                return "Sell";
            }
        }

//    @Value("${alpha.api.key}")
//    private String apiKey;
//
//    private final RestTemplate restTemplate = new RestTemplate();
//
//    }
}

//    public String getLiveStockData(String symbols) {
//        String url = "htt