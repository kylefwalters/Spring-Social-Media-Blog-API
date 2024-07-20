package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account findByUsername(String username) {
        List<Account> foundUsers = accountRepository.findByUsername(username);
        if(foundUsers.isEmpty()) {
            return null;
        }
        // username is unique, so there should only be one result
        return foundUsers.get(0);
    }

    public Account findByUsernameAndPassword(String username, String password) {
        Account foundAccount = findByUsername(username);
        if(foundAccount == null) {
            return null;
        }

        return foundAccount.getPassword() == password ? foundAccount : null;
    }

    public Account registerAccount(Account account) {
        return accountRepository.save(account);
    }
}
