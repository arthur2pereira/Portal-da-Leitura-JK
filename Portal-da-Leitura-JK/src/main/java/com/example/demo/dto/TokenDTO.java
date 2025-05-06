package com.example.demo.dto;

public class TokenDTO {
    private String token;
    private String role;
    private Object user;

    public TokenDTO(String token, String role, Object user) {
        this.token = token;
        this.role = role;
        this.user = user;
    }

    public Object getUser() {
        return user;
    }

    public void setUser(Object user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
