package com.revature.servlets.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.revature.models.*;

import java.util.Date;
import java.util.Map;

@JsonIgnoreProperties
public class ReimbursementDto  extends Reimbursement {
    private int id;
    private User author;
    private User resolver;
    private String description;
    private ReimbursementType type;
    private Date creationDate;
    private Date resolutionDate;
    private Status status;
    private double amount;

    public ReimbursementDto() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ReimbursementType getType() {
        return type;
    }

    public void setType(ReimbursementType type) {
        this.type = type;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getResolutionDate() {
        return resolutionDate;
    }

    public void setResolutionDate(Date resolutionDate) {
        this.resolutionDate = resolutionDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public User getResolver() {
        return resolver;
    }

    public void setResolver(User resolver) {
        this.resolver = resolver;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @SuppressWarnings("unchecked")
    @JsonProperty("author")
    private void unpackAuthor(Map<String, String> author){
        this.author = unpackUser(author);
    }

    @SuppressWarnings("unchecked")
    @JsonProperty("resolver")
    private void unpackResolver(Map<String, String> resolver){
        if(resolver == null)
            return;
        this.resolver = unpackUser(resolver);
    }

    private User unpackUser(Map<String, String> userMap){
        User user = new User();
        user.setId(Integer.parseInt(userMap.get("id")));
        user.setUsername(userMap.get("username"));
        user.setPassword(userMap.get("password"));
        user.setRole(Role.valueOf(userMap.get("role")));
        user.setFirstName(userMap.get("firstName"));
        user.setLastName(userMap.get("lastName"));
        user.setEmail(userMap.get("email"));
        user.setPhoneNumber(userMap.get("phoneNumber"));
        user.setAddress(userMap.get("address"));
        return user;
    }
}
