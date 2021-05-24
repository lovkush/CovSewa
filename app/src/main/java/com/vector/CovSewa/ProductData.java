package com.vector.CovSewa;

public class ProductData {

    String productTitle, addLine1,addLine2,zipCode,description,productId,category,contact,donorId;
    boolean status;
    int imageCount;
    long timestamp;

    public ProductData() {
    }

    public ProductData(String donorId,String contact,int imageCount,String category, String productTitle, String addLine1, String addLine2, String zipCode, String description, String productId, boolean status, long timestamp) {
        this.productTitle = productTitle;
        this.addLine1 = addLine1;
        this.addLine2 = addLine2;
        this.zipCode = zipCode;
        this.description = description;
        this.productId = productId;
        this.status = status;
        this.timestamp = timestamp;
        this.category = category;
        this.imageCount = imageCount;
        this.contact = contact;
        this.donorId = donorId;
    }

    public String getDonorId() {
        return donorId;
    }

    public void setDonorId(String donorId) {
        this.donorId = donorId;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public int getImageCount() {
        return imageCount;
    }

    public void setImageCount(int imageCount) {
        this.imageCount = imageCount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
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

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
