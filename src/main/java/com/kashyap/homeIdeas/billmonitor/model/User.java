package com.kashyap.homeIdeas.billmonitor.model;

import org.elasticsearch.common.Nullable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "user")
public class User {

    @Id
    @ReadOnlyProperty
    private String id;

    @Field(type = FieldType.Text)
    private String firstname;

    @Field(type = FieldType.Text)
    private String lastname;

    @Field(type = FieldType.Text)
    @Nullable
    private String username;

    @Field(type = FieldType.Text)
    @Nullable
    private String password;

    @Field(type = FieldType.Text)
    private String email;

    @Field(type = FieldType.Long)
    private long mobileNo;

    private UserRole role;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(long mobileNo) {
        this.mobileNo = mobileNo;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}
