package com.example.application.views.list;

import com.example.application.data.entity.Company;
import com.example.application.data.entity.Status;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;

import java.util.List;

//ContactForm extends FormLayout: a responsive layout that shows form fields in 1 or 2 columns depending on viewport width.
public class ContactForm extends FormLayout {
    //Creates all the UI components as fields in the component.
    TextField firstNameTextField = new TextField("First name");
    TextField lastNameTextField = new TextField("Last name");
    EmailField emailField = new EmailField("Email");
    ComboBox<Status> statusComboBox = new ComboBox<>("Status");
    ComboBox<Company> companyComboBox = new ComboBox<>("Company");

    Button saveButton = new Button("Save");
    Button deleteButton = new Button("Delete");
    Button cancelButton = new Button("Cancel");

    public ContactForm(
            List<Company> companyList,
            List<Status> statusList
    ) {
        //Gives the component a CSS class name, so you can style it later.
        addClassName("contact-form");

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
}
