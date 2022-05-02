package com.example.springreqresclient.model;

import lombok.Data;

@Data
public class UserData {
    private Integer id;
    private String email;
    private String first_name;
    private String last_name;
    private String avatar;
}
