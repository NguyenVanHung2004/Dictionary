package com.example.englishapp;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.Scanner;

public class TranslateAPI extends ApiConnection {

  private final String webUrl = "https://api.mymemory.translated.net/get?q=";
  private String inputLanguage = "English";
  private String outputLanguage = "Vietnamese";
    public TranslateAPI() {

    }

    @Override
    public void prepareQuery(String query) {
        query = query.replace(" ", "%20");
        finalQuery = webUrl + query + "&langpair=" + inputLanguage + "|" + outputLanguage;
    }

    public JSONObject getJSONObject() {
        getConnection();
        try {
            // Check if connect is made
            int responseCode = connection.getResponseCode();

            if (responseCode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            } else {

                StringBuilder informationString = new StringBuilder();
                Scanner scanner = new Scanner(url.openStream());
                while (scanner.hasNext()) {
                    informationString.append(scanner.nextLine());
                }
                scanner.close();

                JSONParser parse = new JSONParser();

                return (JSONObject) parse.parse(String.valueOf(informationString));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getOutPutString(){
        JSONObject jsonObject = this.getJSONObject();
        JSONObject responseData = (JSONObject) jsonObject.get("responseData");
        String outPut = responseData.get("translatedText").toString();
        return outPut;
    }
}
