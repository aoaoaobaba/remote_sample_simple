package com.example.sbdemo;

import lombok.Data;

@Data
public class Account {
    Integer accountId;
    String email;
    String password;
    String userName;
}