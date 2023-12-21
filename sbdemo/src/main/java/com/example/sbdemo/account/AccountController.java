package com.example.sbdemo.account;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class AccountController {

    private final AccountService accountService;

    private static final Logger log = LoggerFactory.getLogger(AccountController.class);

    @GetMapping("/account/{id}")
    public Account getAccount(@PathVariable Integer id) {
        log.info("getAccount");
        Account account = Account.builder().accountId(id).build();
        return accountService.findById(account);
    }

    @GetMapping("/accounts")
    public List<Account> getAccountList() {
        log.info("getAccountList");
        return accountService.getAccountList();
    }
}
