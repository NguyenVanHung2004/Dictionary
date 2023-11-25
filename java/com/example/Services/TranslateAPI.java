package com.example.Services;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.NoRouteToHostException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class TranslateAPI extends ApiConnection {

  private final String webUrl = "https://api.mymemory.translated.net/get?q=";
  private String inputLanguage = "en";
  private String outputLanguage = "vi";
  private static TranslateAPI instance = null;

    public void setInputLanguage(String inputLanguage) {
        this.inputLanguage = inputLanguage;
    }

    public void setOutputLanguage(String outputLanguage) {
        this.outputLanguage = outputLanguage;
    }


    private TranslateAPI() {

    }
    public static synchronized TranslateAPI getInstance(){
        if (instance == null)
            instance = new TranslateAPI();
        return instance;
    }

    @Override
    public void prepareQuery(String query) {

       // query = query.replace(" ","%20");
       // query =  query.replace("\n",  "|" );

        query = URLEncoder.encode(query , StandardCharsets.UTF_8);
        finalQuery = webUrl + query + "&langpair="  + "en|vi" +  "&key=270b9e8973cce9404f77";
    }

  public JSONObject getJSONObject() throws IOException {
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
        } catch (  ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
    public String getOutPutString(){
        try{
            JSONObject jsonObject = this.getJSONObject();
            JSONObject responseData = (JSONObject) jsonObject.get("responseData");
            return responseData.get("translatedText").toString().replace("|", "\n");
        }catch (IOException noRouteToHostException ){
            return "No Internet connection. Please check your Internet.";
        }


    }
}
