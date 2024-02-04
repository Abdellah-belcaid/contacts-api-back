package com.contactsapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ContactsApiBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(ContactsApiBackApplication.class, args);
    }

}
