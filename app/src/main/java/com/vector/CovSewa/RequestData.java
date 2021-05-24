package com.vector.CovSewa;

import java.io.Serializable;

public class RequestData implements Serializable {
    String requestId, donorId, doneeId, topic, description, contact, category, add, name,productId;
    boolean saveStatus , status;
    long time;

    public RequestData(String requestId, String donorId, String doneeId, String topic, String description, String contact, String category, String add, String name, boolean saveStatus, boolean status, long time) {
        this.requestId = requestId;
        this.donorId = donorId;
        this.doneeId = doneeId;
        this.topic = topic;
        this.description = description;
        this.contact = contact;
        this.category = category;
        this.add = add;
        this.name = name;
        this.saveStatus = saveStatus;
        this.status = status;
        this.time = time;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public RequestData() {
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAdd() {
        return add;
    }

    public void setAdd(String add) {
        this.add = add;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSaveStatus() {
        return saveStatus;
    }

    public void setSaveStatus(boolean saveStatus) {
        this.saveStatus = saveStatus;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getDonorId() {
        return donorId;
    }

    public void setDonorId(String donorId) {
        this.donorId = donorId;
    }

    public String getDoneeId() {
        return doneeId;
    }

    public void setDoneeId(String doneeId) {
        this.doneeId = doneeId;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
