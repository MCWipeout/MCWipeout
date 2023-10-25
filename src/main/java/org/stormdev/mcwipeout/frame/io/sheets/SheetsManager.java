package org.stormdev.mcwipeout.frame.io.sheets;
/*
  Created by Stormbits at 10/25/2023
*/

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
import org.bukkit.Bukkit;
import org.stormdev.mcwipeout.Wipeout;
import org.stormdev.mcwipeout.frame.io.impl.WipeoutDatabase;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

public class SheetsManager {

    private static final String APPLICATION_NAME = "MCWipeout MC Plugin";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    private static final List<String> SCOPES =
            Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY);
    private static final String CREDENTIALS_FILE_PATH = "credentials.json";

    public SheetsManager() {
        Bukkit.getServer().getScheduler().runTaskAsynchronously(Wipeout.get(), () -> {
            try {
                final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

                final String spreadsheetId = "10oEuhGCOPmjdPG1uu3JTpSkxqBZac5AKlXhS1si3XlY";

                final String range = "Teams!A";

                Sheets service =
                        new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                                .setApplicationName(APPLICATION_NAME)
                                .build();
                ValueRange response = service.spreadsheets().values()
                        .get(spreadsheetId, range)
                        .execute();
                List<List<Object>> values = response.getValues();
                if (values == null || values.isEmpty()) {
                    System.out.println("No data found.");
                } else {
                    System.out.println("Team Name");
                    for (List row : values) {
                        System.out.println(row.get(0));
                    }

                }
            } catch (GeneralSecurityException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)
            throws IOException {
        // Load client secrets.
        File file = new File(Wipeout.get().getDataFolder() + "/credentials.json");
        InputStream in = new BufferedInputStream(new FileInputStream(file));
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(Wipeout.get().getDataFolder() + "/token")))
                .setAccessType("offline")
                .build();

        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    }
}
