package com.company.model;

public class Contact {
    private ContactType contactType;
    private String contactText;

    public Contact() {
    }

    public Contact(ContactType contactType, String contactText) {
        this.contactType = contactType;
        this.contactText = contactText;
    }

    public ContactType getContactType() {
        return contactType;
    }

    public String getContactText() {
        return contactText;
    }

    public enum ContactType {
        UNKNOWN(0), EMAIL(1), PHONE(2), JABBER(3);
        private int numericValue;

        ContactType(int numericValue) {
            this.numericValue = numericValue;
        }

        public int getNumericValue() {
            return numericValue;
        }
    }
}
