package com.bunjlabs.medicineapp.db;

import org.sql2o.Connection;

public class User {

    private long id;
    private String role;
    private String login;
    private String password;

    public User() {
    }

    public User(long id, String role, String login, String password) {
        this.id = id;
        this.role = role;
        this.login = login;
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public String getRole() {
        return role;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static User getByLoginPass(String login, String password) {
        String query = "SELECT * FROM users WHERE login=:login AND password=:password";
        try (Connection con = Database.getInstance().getDB().open()) {
            return con.createQuery(query).addParameter("login", login).addParameter("password", password).executeAndFetchFirst(User.class);
        }
    }

    public void insertIgnore() {
        String sql
                = "INSERT OR IGNORE INTO users VALUES (:id, :role, :login, :password);";
        try (Connection con = Database.getInstance().getDB().open()) {
            con.createQuery(sql).bind(this).executeUpdate();
        }
    }
}
