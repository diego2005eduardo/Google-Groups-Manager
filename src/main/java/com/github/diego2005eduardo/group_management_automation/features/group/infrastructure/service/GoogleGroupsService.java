package com.github.diego2005eduardo.group_management_automation.features.group.infrastructure.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.admin.directory.Directory;
import com.google.api.services.admin.directory.model.Member;
import com.google.api.services.admin.directory.model.Members;
import com.google.common.collect.ImmutableList;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

@Component
public class GoogleGroupsService {

    private static final ImmutableList<String> API_SCOPES = ImmutableList.of(
            "https://www.googleapis.com/auth/admin.directory.group",
            "https://www.googleapis.com/auth/admin.directory.group.member"
    );

    @Value("${googlegroup.service_account_id}")
    private String SERVICE_ACCOUNT_ID;
    @Value("${googlegroup.filepath.p12}")
    private String P12_FILE_PATH;
    @Value("${googlegroup.service_account_user}")
    private String SERVICE_ACCOUNT_USER;

    private final HttpTransport httpTransport = new NetHttpTransport();
    private final JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

    private Directory getDirectoryService() throws GeneralSecurityException, IOException {
        GoogleCredential credential = getCredential();
        return new Directory.Builder(httpTransport, jsonFactory, credential)
                .setApplicationName("Group Management Automation")
                .build();
    }

    public GoogleCredential getCredential() throws GeneralSecurityException, IOException {
        GoogleCredential credential = new GoogleCredential.Builder()
                .setTransport(httpTransport)
                .setJsonFactory(jsonFactory)
                .setServiceAccountId(SERVICE_ACCOUNT_ID)
                .setServiceAccountPrivateKeyFromP12File(new File(P12_FILE_PATH))
                .setServiceAccountScopes(API_SCOPES)
                .setServiceAccountUser(SERVICE_ACCOUNT_USER)
                .build();

        if (!credential.refreshToken()) {
            throw new RuntimeException("Failed to obtain access token.");
        }
        return credential;
    }

    public String getAccessToken() {
        try {
            return getCredential().getAccessToken();
        } catch (GeneralSecurityException | IOException exception) {
            throw new RuntimeException("Failed to obtain access token: " + exception.getMessage(), exception);
        }
    }

    public Member createMember(String email, String role) {
        Member member = new Member();
        member.setEmail(email);
        member.setRole(role);
        return member;
    }

    public void addMemberToGroup(String groupKey, Member member) {
        try {
            Directory directory = getDirectoryService();
            directory.members()
                    .insert(groupKey, member)
                    .execute();
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException("Error when adding member to group: " + e.getMessage(), e);
        }
    }

    public void removeMemberFromGroup(String groupKey, String memberEmail) {
        try {
            GoogleCredential credential = getCredential();
            Directory directory = getDirectoryService();
            directory.members()
                    .delete(groupKey, memberEmail)
                    .execute();

        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException("Error when removing group member: " + e.getMessage(), e);
        }
    }

    public List<Member> listGroupMembers(String groupKey) {
        try {
            Directory directory = getDirectoryService();
            List<Member> members = new ArrayList<>();
            String pageToken = null;

            do {
                Members result = directory.members()
                        .list(groupKey)
                        .setPageToken(pageToken)
                        .execute();

                if (result.getMembers() != null) {
                    members.addAll(result.getMembers());
                }
                pageToken = result.getNextPageToken();
            } while (pageToken != null);

            return members;
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException("Error when list group members: " + e.getMessage(), e);
        }
    }
}