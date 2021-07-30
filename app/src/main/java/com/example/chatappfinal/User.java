package com.example.chatappfinal;

public class User {
    String Username,pass,email,id,phoneno;
    public User() {
    }
    public User(String username, String pass, String email, String id,String phoneno) {
        Username = username;
        this.pass = pass;
        this.email = email;
        this.id = id;
        this.phoneno = phoneno;
    }
    public User(String username, String pass, String email) {
        Username = username;
        this.pass = pass;
        this.email = email;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }
    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String   Phoneno) {
            phoneno = Phoneno;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
