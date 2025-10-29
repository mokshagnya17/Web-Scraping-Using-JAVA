// Feature Extraction for Unstructured Textual News Feed Data
// Directly related
// Scrape online news data for ML feature extraction

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebScraper {
    public static void main(String[] args) {
        String urlString = "https://www.sphoorthyengg.ac.in/";
        String htmlContent = fetchHtmlContent(urlString);

        if (htmlContent != null) {
            // Extract and print links
            extractLinks(htmlContent);

            // Extract and print phone numbers
            extractPhoneNumbers(htmlContent);
        } else {
            System.out.println("Failed to fetch HTML content.");
        }
    }

    private static String fetchHtmlContent(String urlString) {
        StringBuilder content = new StringBuilder();
        HttpURLConnection connection = null;

        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), "UTF-8"));
                String line;
                while ((line = in.readLine()) != null) {
                    content.append(line).append("\n");
                }
                in.close();
            } else {
                System.out.println("HTTP Error: " + responseCode);
                return null;
            }

        } catch (IOException e) {
            System.out.println("Error fetching URL: " + e.getMessage());
            return null;
        } finally {
            if (connection != null) connection.disconnect();
        }
        return content.toString();
    }

    private static void extractLinks(String html) {
        Pattern linkPattern = Pattern.compile("href=[\"'](https?://[^\"']+)[\"']", Pattern.CASE_INSENSITIVE);
        Matcher linkMatcher = linkPattern.matcher(html);

        System.out.println("\n=== Extracted Links ===");
        while (linkMatcher.find()) {
            System.out.println(linkMatcher.group(1));
        }
    }

    private static void extractPhoneNumbers(String html) {
        Pattern phonePattern = Pattern.compile("\\b(?:\\+91[\\s-]?)?[6789]\\d{9}\\b");
        Matcher phoneMatcher = phonePattern.matcher(html);

        System.out.println("\n=== Extracted Phone Numbers ===");
        while (phoneMatcher.find()) {
            System.out.println(phoneMatcher.group());
        }
    }
}
