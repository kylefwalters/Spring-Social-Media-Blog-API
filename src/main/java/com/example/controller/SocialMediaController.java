package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.service.AccountService;
import com.example.service.MessageService;
import com.utility.AccountUtilities;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private MessageService messageService;

    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    @PostMapping("register")
    public ResponseEntity registerAccount(@RequestBody Account account) {
        if(accountService.findByUsername(account.getUsername()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Account with username already exists");
        }

        if(!AccountUtilities.isValidPassword(account.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password is too short");
        }

        Account registeredAccount = accountService.registerAccount(account);
        if(registeredAccount != null) {
            return ResponseEntity.status(HttpStatus.OK).body(registeredAccount);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Request");
    }

    @PostMapping("login")
    public ResponseEntity login(@RequestBody Account account) {
        Account foundAccount = accountService.findByUsernameAndPassword(account.getUsername(), account.getPassword());
        if(foundAccount != null) {
            return ResponseEntity.status(HttpStatus.OK).body(foundAccount);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized request");
    }
}
