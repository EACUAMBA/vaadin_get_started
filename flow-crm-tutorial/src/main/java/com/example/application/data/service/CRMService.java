package com.example.application.data.service;

import com.example.application.data.entity.Company;
import com.example.application.data.entity.Contact;
import com.example.application.data.entity.Status;
import com.example.application.data.repository.CompanyRepository;
import com.example.application.data.repository.ContactRepository;
import com.example.application.data.repository.StatusRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

//The @Service annotation makes this a Spring-managed service that you can inject into your view.
@Service
public class CRMService {

    private final ContactRepository contactRepository;
    private final CompanyRepository companyRepository;
    private final StatusRepository statusRepository;

    //Use Spring constructor injection to autowire the database repositories.
    public CRMService(ContactRepository contactRepository, CompanyRepository companyRepository, StatusRepository statusRepository) {
        this.contactRepository = contactRepository;
        this.companyRepository = companyRepository;
        this.statusRepository = statusRepository;
    }

    public List<Contact> findAllContacts(String filter){
        //Check if there is an active filter, return either all contacts or use the repository to filter based on the string.
        if(StringUtils.isBlank(filter))
            return this.contactRepository.findAll();
        return contactRepository.search(filter);
    }

    public Long countContacts(){
        return contactRepository.count();
    }

    public void deleteContact(Contact contact){
        contactRepository.delete(contact);
    }

    public void saveContact(Contact contact){
        //Service classes often include validation and other business rules before persisting data. Here, you check that you aren’t trying to save a null object.
        if(Objects.isNull(contact)){
            System.err.println("Contact is null, Are you sure you have connected your form to the application?");
            return;
        }

        contactRepository.save(contact);
    }

    public List<Company> findAllCompanies(){
        return companyRepository.findAll();
    }

    public List<Status> findAllStatuses(){
        return statusRepository.findAll();
    }

}
