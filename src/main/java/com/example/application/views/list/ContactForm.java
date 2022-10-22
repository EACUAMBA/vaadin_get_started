package com.example.application.views.list;

import com.example.application.data.entity.Company;
import com.example.application.data.entity.Contact;
import com.example.application.data.entity.Status;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.PropertyId;
import com.vaadin.flow.shared.Registration;

import java.util.List;

//ContactForm extends FormLayout: a responsive layout that shows form fields in 1 or 2 columns depending on viewport width.
public class ContactForm extends FormLayout {
    //Creates all the UI components as fields in the component.
    @PropertyId("firstName")
    TextField firstNameTextField = new TextField("First name");
    @PropertyId("lastName")
    TextField lastNameTextField = new TextField("Last name");
    @PropertyId("email")
    EmailField emailField = new EmailField("Email");
    @PropertyId("status")
    ComboBox<Status> statusComboBox = new ComboBox<>("Status");
    @PropertyId("company")
    ComboBox<Company> companyComboBox = new ComboBox<>("Company");

    Button saveButton = new Button("Save");
    Button deleteButton = new Button("Delete");
    Button cancelButton = new Button("Cancel");

    //BeanValidationBinder is a Binder that’s aware of bean validation annotations. By passing it in the Contact.class, you define the type of object you are binding to.
    Binder<Contact> contactBinder = new BeanValidationBinder<>(Contact.class);
    private Contact contact;

    public ContactForm(
            List<Company> companyList,
            List<Status> statusList
    ) {
        //Gives the component a CSS class name, so you can style it later.
        addClassName("contact-form");

        //bindInstanceFields() matches fields in Contact and ContactForm based on their names.
        contactBinder.bindInstanceFields(this);

        companyComboBox.setItems(companyList);
        companyComboBox.setItemLabelGenerator(Company::getName);
        statusComboBox.setItems(statusList);
        statusComboBox.setItemLabelGenerator(Status::getName);

        //Adds all the UI components to the layout.
        add(
                firstNameTextField,
                lastNameTextField,
                emailField,
                companyComboBox,
                statusComboBox,
               // The buttons require a bit of extra configuration. Create and call a new method, createButtonsLayout().
                this.createButtonsLayout()
        );
    }

    private HorizontalLayout createButtonsLayout() {
        //Makes the buttons visually distinct from each other using built-in theme variants.
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        //Defines keyboard shortcuts: Enter to save and Escape to close the editor.
        saveButton.addClickShortcut(Key.ENTER);
        cancelButton.addClickShortcut(Key.ESCAPE);

        //Returns a HorizontalLayout containing the buttons to place them next to each other.
        return new HorizontalLayout(
                saveButton,
                deleteButton,
                cancelButton
        );
    }

    public void setContact(Contact contact){
        //Save a reference to the contact, so you can save the form values back into it later.
        this.contact = contact;
        //Calls binder.readBean() to bind the values from the contact to the UI fields. readBean() copies the values from the contact to an internal model; that way you don’t overwrite values if you cancel editing.
        contactBinder.readBean(contact);
    }

    //ContactFormEvent is a common superclass for all the events. It contains the contact that was edited or deleted.
    public static abstract class ContactFormEvent extends ComponentEvent<ContactForm>{
        private Contact contact;
        protected ContactFormEvent (ContactForm source, Contact contact){
            super(source, false);
            this.contact = contact;
        }

        public Contact getContact(){
            return contact;
        }
    }

    public static class SaveEvent extends ContactFormEvent{
        SaveEvent(ContactForm source, Contact contact){
            super(source, contact);
        }
    }

    public static class DeleteEvent extends ContactFormEvent{
        DeleteEvent(ContactForm source, Contact contact){
            super(source, contact);
        }
    }

    public static class CloseEvent extends ContactFormEvent{
        CloseEvent(ContactForm source, Contact contact){
            super(source, null);
        }
    }

    //The addListener() method uses Vaadin’s event bus to register the custom event types. Select the com.vaadin import for Registration if IntelliJ asks.
    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener){
        return getEventBus().addListener(eventType, listener);
    }
}
