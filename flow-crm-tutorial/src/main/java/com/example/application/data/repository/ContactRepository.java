package com.example.application.data.repository;

import com.example.application.data.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ContactRepository extends JpaRepository<Contact, UUID> {

    //Uses the @Query annotation to define a custom query. In this case, it checks if the string matches the first or the last name, and ignores the case. The query uses Java Persistence Query Language (JPQL) which is an SQL-like language for querying JPA-managed databases.
    @Query(
            "select  c from Contact c " +
                    "where lower(c.firstName) like lower(concat('%', :searchTerm, '%')) " +
                    "or lower(c.lastName) like lower(concat('%', :searchTerm, '%'))"
    )
    List<Contact> search(@Param("searchTerm") String filter);
}
