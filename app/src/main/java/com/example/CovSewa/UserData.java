package com.example.CovSewa;

public class UserData {
    private String Name, addLine1, addLine2, zipcode, email, contact;

    public UserData(String name, String addLine1, String addLine2, String zipcode, String email, String contact) {
        Name = name;
        this.addLine1 = addLine1;
        this.addLine2 = addLine2;
        this.zipcode = zipcode;
        this.email = email;
        this.contact = contact;
    }

    public UserData() {
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAddLine1() {
        return addLine1;
    }

    public void setAddLine1(String addLine1) {
        this.addLine1 = addLine1;
    }

    public String getAddLine2() {
        return addLine2;
    }

    public void setAddLine2(String addLine2) {
        this.addLine2 = addLine2;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
