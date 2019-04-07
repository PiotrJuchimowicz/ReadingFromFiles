package com.company.reader;

import com.company.database.DatabaseActions;
import com.company.model.Contact;
import com.company.model.Customer;
import com.company.validator.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class MyFileReaderCSV extends MyFileReader {
    private static final Logger log = LoggerFactory.getLogger(MyFileReaderCSV.class);
    private static final String CSV_DELIMITER = ",";

    public void saveFromFile(String filePath) {
        final DatabaseActions databaseActions = new DatabaseActions();
        try (Scanner sc = new Scanner(new FileInputStream(filePath), getCHARSET())) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] splitedLine = line.split(CSV_DELIMITER);
                Customer customer = this.convertToModel(splitedLine);
                databaseActions.insert(customer);
            }
        } catch (FileNotFoundException e) {
            log.error("Unable to get data from file: " + filePath);
        }
    }

    private Customer convertToModel(String[] splitedLine) {
        String name = splitedLine[0];
        String surname = splitedLine[1];
        String age = splitedLine[2];
        Customer customer = new Customer(age, name, surname);
        for (int i = 4; i < splitedLine.length; i++) {
            String contactText = splitedLine[i];
            Contact.ContactType contactType = this.getContactType(contactText);
            Contact contact = new Contact(contactType, contactText);
            customer.addContact(contact);
        }
        return customer;
    }

    private Contact.ContactType getContactType(String contactText) {
        if (Validator.isEmail(contactText)) {
            return Contact.ContactType.EMAIL;
        } else if (Validator.isPhone(contactText)) {
            return Contact.ContactType.PHONE;
        } else if (Validator.isJabber(contactText)) {
            return Contact.ContactType.JABBER;
        } else {
            return Contact.ContactType.UNKNOWN;
        }
    }
}
