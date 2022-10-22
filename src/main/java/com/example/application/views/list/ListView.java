package com.example.application.views.list;

import com.example.application.data.entity.Contact;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.Collections;

@PageTitle("Contacts | Vaadin CRM")
@Route(value = "")
//The view extends VerticalLayout, which places all child components vertically.
public class ListView extends VerticalLayout {
    //The Grid component is typed with Contact.
    Grid<Contact> grid = new Grid<>(Contact.class);
    TextField filterTextField = new TextField();
    //Creates a field for the form, so you have access to it from other methods later on.
    ContactForm contactForm;

    public ListView() {
        addClassName("list-view");
        setSizeFull();
        configureGrid();
        configureForm();

        //Add the toolbar and grid to the VerticalLayout.
        add(
                getToolbar(),
                //Change the add() method to call getContent().
                getContent()
        );
    }

    // The method returns a HorizontalLayout that wraps the form and the grid, showing them next to each other.
    public Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, contactForm);
        //Use setFlexGrow() to define that the Grid should get two times the space of the form.
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, contactForm);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    //Create a method for initializing the form.
    public void configureForm() {
        //Initialize the form with empty company and status lists for now; you add these in the next chapter.
        contactForm = new ContactForm(Collections.emptyList(), Collections.emptyList());
        contactForm.setWidth("25rem");
    }

    //The grid configuration is extracted to a separate method to keep the constructor easier to read.
    private void configureGrid() {
        grid.addClassName("contact-grid");
        grid.setSizeFull();
        //Define which properties of Contact the grid should show.
        grid.setColumns("firstName", "lastName", "email");
        //Define custom columns for nested objects.
        grid.addColumn(contact -> contact.getStatus().getName()).setHeader("Status");
        grid.addColumn(contact -> contact.getCompany().getName()).setHeader("Company");
        //Configure the columns to automatically adjust their size to fit their contents.
        grid.getColumns().forEach(contactColumn -> contactColumn.setAutoWidth(true));
    }

    private HorizontalLayout getToolbar() {
        filterTextField.setPlaceholder("Filter by name");
        filterTextField.setClearButtonVisible(true);
        //Configure the search field to fire value-change events only when the user stops typing. This way you avoid unnecessary database calls.
        filterTextField.setValueChangeMode(ValueChangeMode.LAZY);

        Button addContactButton = new Button("Add contact");

        //The toolbar uses a HorizontalLayout to place the TextField and Button next to each other.
        HorizontalLayout toolbar = new HorizontalLayout(filterTextField, addContactButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }
}
