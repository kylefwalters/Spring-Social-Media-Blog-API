package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.repository.MessageRepository;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Message getMessage(int messageId) {
        Optional<Message> foundMessage = messageRepository.findById(messageId);
        return foundMessage.orElse(null);
    }

    public List<Message> getAllMessagesFromUser(int accountId) {
        return messageRepository.findByPostedBy(accountId);
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Message postMessage(Message message) {
        Message postedMessage = messageRepository.save(message);
        if(postedMessage == null) {
            return null;
        }

        return message;
    }

    public int deleteMessage(int messageId) {
        try {
            messageRepository.deleteById(messageId);
        } catch (EmptyResultDataAccessException e) {
            return 0;
        }
        return 1;
    }

    public int updateMessage(int messageId, String newMessageText) {
        Message foundMessage = getMessage(messageId);
        if(foundMessage == null) {
            return 0;
        }
    
        foundMessage.setMessageText(newMessageText);

        messageRepository.save(foundMessage);
        return 1;
    }
}
