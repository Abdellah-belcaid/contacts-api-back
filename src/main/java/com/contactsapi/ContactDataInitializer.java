package com.contactsapi;


import com.contactsapi.domain.Contact;
import com.contactsapi.service.ContactService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ContactDataInitializer implements CommandLineRunner {

    private final ContactService contactService;

    public ContactDataInitializer(ContactService contactService) {
        this.contactService = contactService;
    }

    @Override
    public void run(String... args) throws Exception {
        // Call the method to initialize contacts
    //initializeContacts();
    }

    private void initializeContacts() {
        Contact contact1 = new Contact();
        contact1.setId(java.util.UUID.randomUUID().toString());
        contact1.setName("John Doe 1");
        contact1.setEmail("john.doe1@example.com");
        contact1.setTitle("Manager");
        contact1.setPhone("123-456-7891");
        contact1.setAddress("123 Main Street, City 1");
        contact1.setStatus("Active");

        Contact contact2 = new Contact();
        contact2.setId(java.util.UUID.randomUUID().toString());
        contact2.setName("John Doe 2");
        contact2.setEmail("john.doe2@example.com");
        contact2.setTitle("Manager");
        contact2.setPhone("123-456-7892");
        contact2.setAddress("123 Main Street, City 2");
        contact2.setStatus("Active");

        Contact contact3 = new Contact();
        contact3.setId(java.util.UUID.randomUUID().toString());
        contact3.setName("John Doe 3");
        contact3.setEmail("john.doe3@example.com");
        contact3.setTitle("Manager");
        contact3.setPhone("123-456-7893");
        contact3.setAddress("123 Main Street, City 3");
        contact3.setStatus("Active");

        Contact contact4 = new Contact();
        contact4.setId(java.util.UUID.randomUUID().toString());
        contact4.setName("John Doe 4");
        contact4.setEmail("john.doe4@example.com");
        contact4.setTitle("Manager");
        contact4.setPhone("123-456-7894");
        contact4.setAddress("123 Main Street, City 4");
        contact4.setStatus("Active");

        Contact contact5 = new Contact();
        contact5.setId(java.util.UUID.randomUUID().toString());
        contact5.setName("John Doe 5");
        contact5.setEmail("john.doe5@example.com");
        contact5.setTitle("Manager");
        contact5.setPhone("123-456-7895");
        contact5.setAddress("123 Main Street, City 5");
        contact5.setStatus("Active");

        // Save each contact
        contactService.createContact(contact1);
        contactService.createContact(contact2);
        contactService.createContact(contact3);
        contactService.createContact(contact4);
        contactService.createContact(contact5);
    }
}
