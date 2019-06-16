package com.example.myapplication12.bean;

import java.io.Serializable;

public class User implements Serializable {//

    private String account;

    private String username;

    private String password;

    private String gender;///

    private String email;

    private String telephone;///

    public User(String account, String username, String password, String gender, String email, String telephone) {
        this.account = account;
        this.username = username;
        this.password = password;
        this.gender = gender;
        this.email = email;
        this.telephone = telephone;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

}
