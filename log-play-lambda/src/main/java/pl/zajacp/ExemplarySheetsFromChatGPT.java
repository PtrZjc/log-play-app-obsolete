package pl.zajacp;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;
import lombok.SneakyThrows;

import java.util.Arrays;
import java.util.List;

public class ExemplarySheetsFromChatGPT implements RequestHandler<String, List<List<Object>>> {

    private static final String APPLICATION_NAME = "My Application Name";
    private static final String SPREADSHEET_ID = "my-spreadsheet-id";
    private static final String RANGE = "Sheet1!A1:C3";

    @SneakyThrows
    public List<List<Object>> handleRequest(String input, Context context){
        HttpTransport httpTransport = new NetHttpTransport();
        JsonFactory jsonFactory = new GsonFactory();

        GoogleCredential credential = GoogleCredential.getApplicationDefault()
            .createScoped(Arrays.asList(SheetsScopes.SPREADSHEETS_READONLY));

        Sheets sheetsService = new Sheets.Builder(httpTransport, jsonFactory, credential)
            .setApplicationName(APPLICATION_NAME)
            .build();

        ValueRange response = sheetsService.spreadsheets().values()
            .get(SPREADSHEET_ID, RANGE)
            .execute();

        List<List<Object>> values = response.getValues();
        return values;
    }

}
