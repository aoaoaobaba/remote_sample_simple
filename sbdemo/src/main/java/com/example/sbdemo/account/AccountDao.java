package com.example.sbdemo;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AccountDao {

    @Select("select * from account where account_id=#{accountId}")
    Account findById(Account account);

    @Select("select * from account")
    List<Account> findAll();
}