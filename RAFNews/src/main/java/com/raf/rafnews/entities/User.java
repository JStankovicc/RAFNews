package com.raf.rafnews.entities;

import com.raf.rafnews.entities.enums.Status;
import com.raf.rafnews.entities.enums.Type;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class User {
    private Integer id;
    @NotNull(message = "Title field is required")
    @NotEmpty(message = "Title field is required")
    private String first_name;
    @NotNull(message = "Title field is required")
    @NotEmpty(message = "Title field is required")
    private String last_name;
    @NotNull(message = "Title field is required")
    @NotEmpty(message = "Title field is required")
    private String email;
    @NotNull(message = "Title field is required")
    @NotEmpty(message = "Title field is required")
    private String password;
    private Status status;
    private Type type;

    public User(){

    }

    public User(String first_name, String last_name, String email, String password) {
        this();
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
    }

    public User(Integer id, String first_name, String last_name, String email, Status status, Type type) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.status = status;
        this.type = type;
    }

    public User(String first_name, String last_name, String email, String password, Status status, Type type) {
        this(first_name, last_name, email, password);
        this.status = status;
        this.type = type;
    }

    public User(Integer id, String first_name, String last_name, String email, String password, Status status, Type type) {
        this(first_name, last_name, email, password, status, type);
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
