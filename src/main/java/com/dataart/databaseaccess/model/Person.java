package com.dataart.databaseaccess.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "People")
@Embeddable
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String firstName;

    @NotNull
    private String surname;

    public Person() {
    }

    public Person(@NotNull Long id, @NotNull String firstName, @NotNull String surname) {
        this.id = id;
        this.firstName = firstName;
        this.surname = surname;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    @Override
    public String toString() {
        return new StringBuilder("Person { ")
                .append("id: ").append(id)
                .append(", name: ").append(firstName)
                .append(", surname: ").append(surname)
                .append(" }")
                .toString();
    }
}
