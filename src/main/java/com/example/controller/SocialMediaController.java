package com.example.controller;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;
import com.utility.AccountUtilities;
import com.utility.MessageUtilities;

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
        if(registeredAccount == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Request");
        }
        
        return ResponseEntity.status(HttpStatus.OK).body(registeredAccount);
    }

    @PostMapping("login")
    public ResponseEntity login(@RequestBody Account account) {
        Account foundAccount = accountService.findByUsernameAndPassword(account.getUsername(), account.getPassword());
        if(foundAccount == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized request");
        }
        return ResponseEntity.status(HttpStatus.OK).body(foundAccount);
    }

    @GetMapping("messages")
    public ResponseEntity getAllMessages() {
        List<Message> messages = messageService.getAllMessages();
        return ResponseEntity.status(HttpStatus.OK).body(messages);
    }

    @GetMapping("messages/{messageId}")
    public ResponseEntity getMessage(@PathVariable Integer messageId) {
        Message foundMessage = messageService.getMessage(messageId);
        return ResponseEntity.status(HttpStatus.OK).body(foundMessage);
    }

    @GetMapping("accounts/{accountId}/messages")
    public ResponseEntity getAllMessagesFromUser(@PathVariable Integer accountId) {
        if(accountService.findByAccountId(accountId) == null) {
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }

        List<Message> foundMessages = messageService.getAllMessagesFromUser(accountId);
        return ResponseEntity.status(HttpStatus.OK).body(foundMessages);
    }

    @PostMapping("messages")
    public ResponseEntity postMessage(@RequestBody Message message) {
        if(!MessageUtilities.isValidMessage(message.getMessageText())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Message format invalid");
        }

        if(accountService.findByAccountId(message.getPostedBy()) == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid User");
        }

        Message postedMessage = messageService.postMessage(message);
        if(postedMessage == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Request");
        }
        
        return ResponseEntity.status(HttpStatus.OK).body(postedMessage);
    }

    @DeleteMapping("messages/{messageId}")
    public ResponseEntity deleteMessage(@PathVariable Integer messageId) {
        int updatedRows = messageService.deleteMessage(messageId);

        return ResponseEntity.status(HttpStatus.OK).body(updatedRows != 0 ? updatedRows : null);
    }

    @PatchMapping("messages/{messageId}")
    public ResponseEntity updateMessage(@PathVariable Integer messageId, @RequestBody Message updatedMessage) {
        String newMessageText = updatedMessage.getMessageText();
        if(!MessageUtilities.isValidMessage(newMessageText)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Message format invalid");
        }
        int updatedRows = messageService.updateMessage(messageId, newMessageText);

        if(updatedRows == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Message not found");
        }

        return ResponseEntity.status(HttpStatus.OK).body(updatedRows);
    }
}
