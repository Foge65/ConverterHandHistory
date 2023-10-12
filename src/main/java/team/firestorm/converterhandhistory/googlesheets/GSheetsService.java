package team.firestorm.converterhandhistory.googlesheets;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GSheetsService {
    public static final String APPLICATION = "HandHistoryConverter";
    public static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    public static final String TOKENS_DIRECTORY_PATH = "tokens";
    public static final String CREDENTIALS_FILE_PATH = "/oauth/credentials.json";
    public static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY);

    private static Credential getCredential(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        InputStream inputStream = GSheetsService.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (inputStream == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(inputStream));
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets,
                SCOPES).setDataStoreFactory(new FileDataStoreFactory(new File(TOKENS_DIRECTORY_PATH))).setAccessType("offline").build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public List<List<Object>> getData() throws GeneralSecurityException, IOException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        final String sheetId = "1tB5CLOlx1vmyIB6pT3IgYe-qvnrIjq0BsQYsdgXkTZ4";
        final String range = "_GGnetwork!A2:E";
        Sheets service = new Sheets.Builder(HTTP_TRANSPORT, GSheetsService.JSON_FACTORY,
                GSheetsService.getCredential(HTTP_TRANSPORT)).setApplicationName(GSheetsService.APPLICATION).build();
        ValueRange response = service.spreadsheets().values().get(sheetId, range).execute();
        List<List<Object>> objectList = response.getValues();
        if (objectList == null || objectList.isEmpty()) {
            System.out.println("Error");
        }
        return objectList;
    }

    public Map<String, String> createMap(List<List<Object>> listObjects) {
        Map<String, String> nicknameMap = new HashMap<>();
        for (List row : listObjects) {
            if (row.size() >= 5) {
                String nickConference = row.get(0).toString();
                Object nickGG = row.get(4);
                if (nickGG != null) {
                    String value = nickGG.toString();
                    nicknameMap.put(nickConference, value);
                }
            }
        }
        return nicknameMap;
    }
}
