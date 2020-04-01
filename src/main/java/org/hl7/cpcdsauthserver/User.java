package org.hl7.cpcdsauthserver;

import java.util.HashMap;
import java.util.Map;

public class User {

    private String username;
    private String password;
    private String patientId;
    private String createdDate;
    private String refreshToken;

    public User(String username, String password, String patientId) {
        this(username, password, patientId, null, null);
    }

    public User(String username, String password, String patientId, String createdDate, String refreshToken) {
        this.password = password;
        this.patientId = patientId;
        this.username = username;
        this.createdDate = createdDate;
        this.refreshToken = refreshToken;
    }

    public String getPatientId() {
        return this.patientId;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public String getCreatedDate() {
        return this.createdDate;
    }

    public String getRefreshToken() {
        return this.refreshToken;
    }

    public static User getUser(String username) {
        return App.getDB().readUser(username);
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("username", this.username);
        map.put("password", this.password);
        map.put("patient_id", this.patientId);
        return map;
    }

    @Override
    public String toString() {
        return "User " + this.username + "(" + this.patientId + "): password(" + this.password + ")";
    }
}