package com.example.application.views.list;

import com.example.application.data.entity.Company;
import com.example.application.data.entity.Contact;
import com.example.application.data.entity.Status;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ContactFormTest {
    private List<Company> companies;
    private List<Status> statuses;
    private Contact marcUsher;
    private Company company1;
    private Company company2;
    private Status status1;
    private Status status2;

    //The @Before annotation adds dummy data that’s used for testing. This method is executed before each @Test method.
    @Before 
    public void setupData() {
        companies = new ArrayList<>();
        company1 = new Company();
        company1.setName("Vaadin Ltd");
        company2 = new Company();
        company2.setName("IT Mill");
        companies.add(company1);
        companies.add(company2);

        statuses = new ArrayList<>();
        status1 = new Status();
        status1.setName("Status 1");
        status2 = new Status();
        status2.setName("Status 2");
        statuses.add(status1);
        statuses.add(status2);

        marcUsher = new Contact();
        marcUsher.setFirstName("Marc");
        marcUsher.setLastName("Usher");
        marcUsher.setEmail("marc@usher.com");
        marcUsher.setStatus(status1);
        marcUsher.setCompany(company2);
    }

    @Test
    public void formFieldsPopulated() {
        ContactForm form = new ContactForm(companies, statuses);
        //Validates that the fields are populated correctly, by first initializing the contact form with some companies, and then setting a contact bean for the form.
        form.setContact(marcUsher);

        //Uses standard JUnit assertEquals() methods to compare the values from the fields available through the ContactForm instance:
        Assert.assertEquals("Marc", form.firstNameTextField.getValue());
        Assert.assertEquals("Usher", form.lastNameTextField.getValue());
        Assert.assertEquals("marc@usher.com", form.emailField.getValue());
        Assert.assertEquals(company2, form.companyComboBox.getValue());
        Assert.assertEquals(status1, form.statusComboBox.getValue());
    }

    @Test
    public void saveEventHasCorrectValues() {
        ContactForm form = new ContactForm(companies, statuses);
        //Initialize the form with an empty Contact.
        Contact contact = new Contact();
        form.setContact(contact);
        //Populate values into the form.
        form.firstNameTextField.setValue("John");
        form.lastNameTextField.setValue("Doe");
        form.companyComboBox.setValue(company1);
        form.emailField.setValue("john@doe.com");
        form.statusComboBox.setValue(status2);

        //Capture the saved contact into an AtomicReference.
        AtomicReference<Contact> savedContactRef = new AtomicReference<>(null);
        form.addListener(ContactForm.SaveEvent.class, e -> {
            savedContactRef.set(e.getContact());
        });
        //Click the save button and read the saved contact.
        form.saveButton.click();
        Contact savedContact = savedContactRef.get();

        //Once the event data is available, verify that the bean contains the expected values.
        Assert.assertEquals("John", savedContact.getFirstName());
        Assert.assertEquals("Doe", savedContact.getLastName());
        Assert.assertEquals("john@doe.com", savedContact.getEmail());
        Assert.assertEquals(company1, savedContact.getCompany());
        Assert.assertEquals(status2, savedContact.getStatus());
    }
}