package com.example.sbdemo.account;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AccountService {

  private final AccountDao accountDao;

  public Account findById(Account user) {
    return this.accountDao.findById(user);
  }

  public List<Account> getAccountList() {
    return this.accountDao.findAll();
  }
}
