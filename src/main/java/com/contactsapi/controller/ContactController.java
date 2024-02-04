package com.contactsapi.controller;


import com.contactsapi.domain.Contact;
import com.contactsapi.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;

import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/contacts")
public class ContactController {

    private final ContactService contactService;

    @GetMapping
    public ResponseEntity<Page<Contact>> getAllContacts(@RequestParam(value = "page", defaultValue = "0") int page,
                                                        @RequestParam(value = "size", defaultValue = "10") int size) {
        Page<Contact> contacts = contactService.getAllContact(page, size);
        return ResponseEntity.ok().body(contacts);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Contact> getContactById(@PathVariable String id) {
        Contact contact = contactService.getContactById(id);
        return ResponseEntity.ok().body(contact);
    }

    @PostMapping
    public ResponseEntity<Contact> createContact(@RequestBody Contact contact) {
        Contact createdContact = contactService.createContact(contact);
        return ResponseEntity.created(URI.create("/api/contacts/" + createdContact.getId())).body(createdContact);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Contact> updateContact(@PathVariable String id, @RequestBody Contact contact) {
        contact.setId(id);
        Contact updatedContact = contactService.updateContact(id, contact);
        return ResponseEntity.ok().body(updatedContact);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContact(@PathVariable String id) {
        Contact contact = contactService.getContactById(id);
        contactService.deleteContact(contact);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/upload-photo")
    public ResponseEntity<String> uploadPhoto(@RequestParam("id") String id,
                                              @RequestParam("file") MultipartFile file) {
        String photoUrl = contactService.uploadPhoto(id, file);
        return ResponseEntity.ok().body(photoUrl);
    }

    @GetMapping(path = "/image/{filename}", produces = {IMAGE_PNG_VALUE, IMAGE_JPEG_VALUE})
    public ResponseEntity<byte[]> getPhoto(@PathVariable("filename") String filename) {
        try {
            byte[] photoBytes = contactService.getPhotoBytes(filename);
            return new ResponseEntity<>(photoBytes, HttpStatus.OK);
        } catch (IOException e) {
            // Handle IOException appropriately, e.g., log the error or return a 404 response
            return ResponseEntity.notFound().build();
        }
    }
}
