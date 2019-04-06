package com.company.model;

public class Contact {
    private int id;
    private Integer idCustomer;
    private byte type;
    private String contact;

    public Contact(Integer idCustomer, byte type, String contact) {
        this.idCustomer = idCustomer;
        this.type = type;
        this.contact = contact;
    }
}
