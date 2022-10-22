package com.example.application.views.list;

import com.example.application.data.entity.Contact;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Contacts | Vaadin CRM")
@Route(value = "")
//The view extends VerticalLayout, which places all child components vertically.
public class ListView extends VerticalLayout {
    //The Grid component is typed with Contact.
    Grid<Contact> grid = new Grid<>(Contact.class);
    TextField filterTextField = new TextField();

    public ListView(){
        addClassName("list-view");
        setSizeFull();
        configureGrid();

        //Add the toolbar and grid to the VerticalLayout.
        add(getToolbar(), grid);
    }

    //The grid configuration is extracted to a separate method to keep the constructor easier to read.
    private void configureGrid(){
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

    private HorizontalLayout getToolbar(){
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
