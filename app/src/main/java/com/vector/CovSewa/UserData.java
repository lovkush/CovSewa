package com.vector.CovSewa;

public class UserData {
    private String Name, addLine1, addLine2, zipcode, email, contact, category ;
    private String[] receivedRequestId, generatedRequestId, productId;

    public UserData(String name, String addLine1, String addLine2, String zipcode, String email, String contact, String category, String[] receivedRequestId, String[] generatedRequestId, String[] productId) {
        Name = name;
        this.addLine1 = addLine1;
        this.addLine2 = addLine2;
        this.zipcode = zipcode;
        this.email = email;
        this.contact = contact;
        this.category = category;
        this.receivedRequestId = receivedRequestId;
        this.generatedRequestId = generatedRequestId;
        this.productId = productId;
    }


    public UserData() {
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String[] getReceivedRequestId() {
        return receivedRequestId;
    }

    public void setReceivedRequestId(String[] receivedRequestId) {
        this.receivedRequestId = receivedRequestId;
    }

    public String[] getGeneratedRequestId() {
        return generatedRequestId;
    }

    public void setGeneratedRequestId(String[] generatedRequestId) {
        this.generatedRequestId = generatedRequestId;
    }

    public String[] getProductId() {
        return productId;
    }

    public void setProductId(String[] productId) {
        this.productId = productId;
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
