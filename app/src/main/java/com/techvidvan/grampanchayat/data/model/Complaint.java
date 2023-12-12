package com.techvidvan.grampanchayat.data.model;

public class Complaint {

    private String complaintId;
    private String date;
    private String subject;
    private String description;
    private String status;
    private String email;
    private String name;
    private String aadhar;
    private String contact;
    private String feedback;
    private String image;

    public Complaint() {
    }

    public Complaint(String complaintId, String date, String subject, String description, String status, String email, String name, String aadhar, String contact, String feedback, String image) {
        this.complaintId = complaintId;
        this.date = date;
        this.subject = subject;
        this.description = description;
        this.status = status;
        this.email = email;
        this.name = name;
        this.aadhar = aadhar;
        this.contact = contact;
        this.feedback = feedback;
        this.image = image;
    }

    public String getComplaintId() {
        return complaintId;
    }

    public void setComplaintId(String complaintId) {
        this.complaintId = complaintId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAadhar() {
        return aadhar;
    }

    public void setAadhar(String aadhar) {
        this.aadhar = aadhar;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
