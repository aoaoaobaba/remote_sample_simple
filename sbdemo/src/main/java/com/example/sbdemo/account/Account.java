package com.example.sbdemo.account;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Account {
    Integer accountId;
    String email;
    String password;
    String userName;
}
