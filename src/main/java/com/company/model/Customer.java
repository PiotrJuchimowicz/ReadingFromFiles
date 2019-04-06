package com.company.model;

public class Customer {
    private int id;
    private Integer age;
    private String name;
    private String surname;

    public Customer(Integer age, String name, String surname) {
        this.age = age;
        this.name = name;
        this.surname = surname;
    }
}
