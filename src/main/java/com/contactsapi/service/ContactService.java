package com.contactsapi.service;


import com.contactsapi.domain.Contact;
import com.contactsapi.repository.ContactRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

import static com.contactsapi.constant.Constant.FILE_STORAGE_LOCATION;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(rollbackOn = Exception.class)
public class ContactService {

    private final ContactRepository contactRepository;

    @Cacheable("contacts")
    public Page<Contact> getAllContact(int page, int size) {
        return contactRepository.findAll(PageRequest.of(page, size, Sort.by("name")));
    }

    public Contact getContactById(String id) {
        log.info("Fetching contact with id: {}", id);
        return contactRepository.findById(id).orElseThrow(() -> new RuntimeException("Contact could not be found "));
    }

    @CacheEvict(cacheNames = "contacts", allEntries = true)

    public Contact createContact(Contact contact) {
        return contactRepository.save(contact);
    }

    @CacheEvict(cacheNames = "contacts", allEntries = true)

    public void deleteContact(Contact contact) {
        contactRepository.delete(contact);
    }

    @CacheEvict(cacheNames = "contacts", allEntries = true)
    public Contact updateContact(String id, Contact updatedContact) {
        Contact existingContact = getContactById(id);

        // Update existing contact properties
        existingContact.setName(updatedContact.getName());
        existingContact.setEmail(updatedContact.getEmail());
        existingContact.setTitle(updatedContact.getTitle());
        existingContact.setPhone(updatedContact.getPhone());
        existingContact.setAddress(updatedContact.getAddress());
        existingContact.setStatus(updatedContact.getStatus());

        // Save the updated contact
        return contactRepository.save(existingContact);
    }

    @CacheEvict(cacheNames = "contacts", allEntries = true)
    public String uploadPhoto(String id, MultipartFile file) {
        Contact contact = getContactById(id);

        String photoUrl = savePhotoAndGenerateUrl(id, file);
        contact.setPhotoUrl(photoUrl);
        contactRepository.save(contact);
        return photoUrl;
    }

    @Cacheable(value = "photos", key = "#filename")
    public byte[] getPhotoBytes(String filename) throws IOException {
        Path filePath = Paths.get(FILE_STORAGE_LOCATION, filename);
        return Files.readAllBytes(filePath);
    }

    private final String savePhotoAndGenerateUrl(String id, MultipartFile image) {
        String filename = id + extractFileExtension(image.getOriginalFilename());

        try {
            Path fileStorageLocation = Paths.get(FILE_STORAGE_LOCATION).toAbsolutePath().normalize();
            if (!Files.exists(fileStorageLocation)) {
                Files.createDirectories(fileStorageLocation);
            }

            // Save the photo
            Files.copy(image.getInputStream(),
                    fileStorageLocation.resolve(filename), StandardCopyOption.REPLACE_EXISTING);

            // Generate and return the photo URL
            return ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/contacts/image/" + filename).toUriString();
        } catch (Exception e) {
            log.error("Unable to save photo for id: {}, filename: {}", id, filename, e);
            throw new RuntimeException("Unable to save photo");

        }

    }

    private final String extractFileExtension(String filename) {
        return Optional.of(filename).filter(name -> name.contains("."))
                .map(name -> "." + name.substring(filename.lastIndexOf(".") + 1)).orElse(".png");
    }


}
