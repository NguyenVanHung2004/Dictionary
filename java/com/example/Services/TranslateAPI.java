package com.example.Services;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class TranslateAPI extends ApiConnection {

  private final String webUrl = "https://api.mymemory.translated.net/get?q=";
  private String inputLanguage = "English";

    public void setInputLanguage(String inputLanguage) {
        this.inputLanguage = inputLanguage;
    }

    public void setOutputLanguage(String outputLanguage) {
        this.outputLanguage = outputLanguage;
    }

    private String outputLanguage = "Vietnamese";
    public TranslateAPI() {

    }

    @Override
    public void prepareQuery(String query) {

        query = URLEncoder.encode(query , StandardCharsets.UTF_8);
        finalQuery = webUrl + query + "&langpair=" + inputLanguage + "|" + outputLanguage;
    }

    public JSONObject getJSONObject() {
        getConnection();
        System.out.println( finalQuery);
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
