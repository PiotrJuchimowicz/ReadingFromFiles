package com.company.model;

import java.util.LinkedList;
import java.util.List;

public class Customer {

    private String age;
    private String name;
    private String surname;
    private List<Contact> contacts;

    public Customer() {
    }

    public Customer(String age, String name, String surname) {
        this.age = age;
        this.name = name;
        this.surname = surname;
        contacts = null;
    }

    public void addContact(Contact contact) {
        if(contacts == null) {
            contacts = new LinkedList<>();
        }
        contacts.add(contact);
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getAge() {
        return age;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public List<Contact> getContacts() {
        return contacts;
    }
}
