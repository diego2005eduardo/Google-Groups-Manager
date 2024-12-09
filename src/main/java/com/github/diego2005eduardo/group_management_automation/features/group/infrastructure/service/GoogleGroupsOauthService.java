package com.github.diego2005eduardo.group_management_automation.features.group.infrastructure.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.common.collect.ImmutableList;

import java.io.FileInputStream;

public final class GoogleGroupsOauthService {

    private static final ImmutableList<String> API_SCOPES =
            ImmutableList.of(
                    "https://www.googleapis.com/auth/cloud-identity.groups",
                    "https://www.googleapis.com/auth/admin.directory.group",
                    "https://www.googleapis.com/auth/admin.directory.group.member",
                    "https://www.googleapis.com/auth/apps.groups.settings");

    public static void main(final String[] args) throws Exception {
        String accessToken = getTokenFromJsonKey();
    }

    private static String getTokenFromJsonKey() throws Exception {
        FileInputStream serviceAccountStream = new FileInputStream(
                GoogleGroupsOauthService.class.getClassLoader().getResource("credentials.json").getFile());

        GoogleCredential credential = GoogleCredential.fromStream(
                serviceAccountStream,
                new NetHttpTransport(),
                GsonFactory.getDefaultInstance());

        HttpTransport httpTransport = new NetHttpTransport();
        JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
        GoogleCredential.Builder builder = new GoogleCredential.Builder()
                .setServiceAccountPrivateKey(credential.getServiceAccountPrivateKey())
                .setServiceAccountPrivateKeyId(credential.getServiceAccountPrivateKeyId())
                .setServiceAccountId(credential.getServiceAccountId())
                .setTransport(httpTransport)
                .setJsonFactory(jsonFactory)
                .setServiceAccountScopes(API_SCOPES)
                .setClock(credential.getClock());

        credential = builder.build();
        if (!credential.refreshToken()) {
            throw new Exception("Failed to fetch access token.");
        }
        return credential.getAccessToken();
    }
}
