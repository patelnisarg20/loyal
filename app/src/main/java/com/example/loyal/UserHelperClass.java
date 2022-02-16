package com.example.loyal;

public class UserHelperClass {
    String name, email, bithdate, gender;
    public UserHelperClass(){

    }

    public UserHelperClass(String name, String email, String bithdate, String gender) {
        this.name = name;
        this.email = email;
        this.bithdate = bithdate;
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBithdate() {
        return bithdate;
    }

    public void setBithdate(String bithdate) {
        this.bithdate = bithdate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}

