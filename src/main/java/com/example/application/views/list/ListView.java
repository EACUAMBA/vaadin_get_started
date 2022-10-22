package com.example.application.views.list;

import com.example.application.data.entity.Contact;
import com.example.application.data.service.CRMService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.PermitAll;
import java.util.Objects;

@PermitAll
@PageTitle("Contacts | Vaadin CRM")
//ListView still matches the empty path, but now uses MainLayout as its parent.
@Route(value = "", layout = MainLayout.class)
//The view extends VerticalLayout, which places all child components vertically.
public class ListView extends VerticalLayout {
    //The Grid component is typed with Contact.
    Grid<Contact> grid = new Grid<>(Contact.class);
    TextField filterTextField = new TextField();
    //Creates a field for the form, so you have access to it from other methods later on.
    ContactForm contactForm;
    CRMService crmService;

    //Autowire CrmService through the constructor. Save it in a field, so you can access it from other methods.
    public ListView(CRMService crmService) {
        this.crmService = crmService;
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
        //Call updateList() once you have constructed the view.
        updateList();
        closeEditor();
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
        //Use the service to fetch companies and statuses.
        contactForm = new ContactForm(crmService.findAllCompanies(), crmService.findAllStatuses());
        contactForm.setWidth("25rem");

        //The save event listener calls saveContact()
        contactForm.addListener(ContactForm.SaveEvent.class, this::saveContact);
        //The delete event listener calls deleteContact().
        contactForm.addListener(ContactForm.DeleteEvent.class, this::deleteContact);
        //The close event listener closes the editor.
        contactForm.addListener(ContactForm.CloseEvent.class, event -> closeEditor());
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

        //addValueChangeListener() adds a listener to the grid. The Grid component supports multi- and single-selection modes. You only need to select a single Contact, so you can use the asSingleSelect() method. The getValue() method returns the Contact in the selected row, or null if there is no selection.
        grid.asSingleSelect()
                .addValueChangeListener(event -> {
                   editContact(event.getValue());
                });
    }

    private HorizontalLayout getToolbar() {
        filterTextField.setPlaceholder("Filter by name");
        filterTextField.setClearButtonVisible(true);
        //Configure the search field to fire value-change events only when the user stops typing. This way you avoid unnecessary database calls.
        filterTextField.setValueChangeMode(ValueChangeMode.LAZY);
        //Call updateList() any time the filter changes.
        filterTextField.addValueChangeListener(e -> updateList());

        Button addContactButton = new Button("Add contact");
        //Call addContact() when the user clicks on the "Add contact" button.
        addContactButton.addClickListener(event -> addContact());

        //The toolbar uses a HorizontalLayout to place the TextField and Button next to each other.
        HorizontalLayout toolbar = new HorizontalLayout(filterTextField, addContactButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    //updateList() sets the grid items by calling the service with the value from the filter text field.
    public void updateList(){
        grid.setItems(crmService.findAllContacts(filterTextField.getValue()));
    }

    //editContact() sets the selected contact in the ContactForm and hides or shows the form, depending on the selection. It also sets the "editing" CSS class name when editing.
    public void editContact(Contact contact){
        if(Objects.isNull(contact))
            closeEditor();
        else{
            contactForm.setContact(contact);
            contactForm.setVisible(true);
            addClassName("editing");
        }
    }

    /*
    The closeEditor() call at the end of the constructor:
        - sets the form contact to null, clearing out old values;
        - hides the form;
        - removes the "editing" CSS class from the view.
     */
    private void closeEditor(){
        contactForm.setContact(null);
        contactForm.setVisible(false);
        removeClassName("editing");
    }

    //addContact() clears the grid selection and creates a new Contact.
    private void addContact(){
        grid.asSingleSelect().clear();
        editContact(new Contact());
    }

    /*
    The save event listener calls saveContact(). It:
        * Uses contactService to save the contact in the event to the database.
        * Updates the list.
        * Closes the editor.
    */
    private void saveContact(ContactForm.SaveEvent contact){
        crmService.saveContact(contact.getContact());
        updateList();
        closeEditor();
    }

    /*
    The delete event listener calls deleteContact(). It:
        * uses contactService to delete the contact from the database;
        * updates the list;
        * closes the editor.
     */
    private void deleteContact(ContactForm.DeleteEvent contact){
        crmService.deleteContact(contact.getContact());
        updateList();
        closeEditor();
    }
}
