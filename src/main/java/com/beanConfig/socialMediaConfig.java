package com.beanConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.example.service.AccountService;
import com.example.service.MessageService;
import com.example.controller.SocialMediaController;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

@Configuration
public class socialMediaConfig {
    @Bean
    public SocialMediaController SocialMediaController(AccountService accountService, MessageService messageService) {
        return new SocialMediaController(accountService, messageService);
    }

    @Bean
    public AccountService AccountService(AccountRepository accountRepository) {
        return new AccountService(accountRepository);
    }

    @Bean
    public MessageService MessageService(MessageRepository messageRepository) {
        return new MessageService(messageRepository);
    }
}
