package com.example.springreqresclient.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/*
    {
        "name": "morpheus",
        "job": "leader",
        "id": "357",
        "createdAt": "2022-05-01T17:24:39.097Z"
    }
 */

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class NewUser {
    private String id ;
    private String name ;
    private String job ;
    private String createdAt ;
}
