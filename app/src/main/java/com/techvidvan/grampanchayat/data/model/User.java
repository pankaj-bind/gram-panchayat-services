package com.techvidvan.grampanchayat.data.model;

public class User {
    private String email;
    private String name;
    private String aadhar;
    private String address;
    private String contact;
    private String pincode;
    private String dateOfBirth;
    private String gender;

    public User(String email, String name, String aadhar, String address, String contact, String pincode, String dateOfBirth, String gender) {
        this.email = email;
        this.name = name;
        this.aadhar = aadhar;
        this.address = address;
        this.contact = contact;
        this.pincode = pincode;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", aadhar='" + aadhar + '\'' +
                ", address='" + address + '\'' +
                ", contact='" + contact + '\'' +
                ", pincode='" + pincode + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }
}
