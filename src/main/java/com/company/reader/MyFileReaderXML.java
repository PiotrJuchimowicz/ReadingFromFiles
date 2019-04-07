package com.company.reader;

import com.company.database.DatabaseActions;
import com.company.model.Contact;
import com.company.model.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MyFileReaderXML extends MyFileReader {
    private static final Logger log = LoggerFactory.getLogger(MyFileReaderXML.class);
    private static final String PERSON_XML_ELEMENT_NAME = "person";
    private static final String NAME_XML_ELEMENT_NAME = "name";
    private static final String SURNAME_XML_ELEMENT_NAME = "surname";
    private static final String AGE_XML_ELEMENT_NAME = "age";
    private static final String CONTACTS_XML_ELEMENT_NAME = "contacts";
    private static final String EMAIL_XML_ELEMENT_NAME = "email";
    private static final String PHONE_XML_ELEMENT_NAME = "phone";
    private static final String JABBER_XML_ELEMENT_NAME = "jabber";

    @Override
    public void saveFromFile(String filePath) {
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        boolean ifStartedReadingContacts = false;
        try {
            XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(new FileInputStream(filePath), getCHARSET());
            Customer customer = null;
            while (xmlEventReader.hasNext()) {
                XMLEvent event = xmlEventReader.nextEvent();
                if (event.isStartElement()) {
                    StartElement startElement = event.asStartElement();
                    if (PERSON_XML_ELEMENT_NAME.equalsIgnoreCase(startElement.getName().getLocalPart())) {
                        customer = new Customer();
                    }
                    if (customer != null) {
                        this.assignCustomerFields(startElement, xmlEventReader, customer);
                    }
                    if (CONTACTS_XML_ELEMENT_NAME.equalsIgnoreCase(startElement.getName().getLocalPart())) {
                        ifStartedReadingContacts = true;
                        continue;
                    }

                    if (customer != null && ifStartedReadingContacts ) {
                        this.assignContactToCustomer(startElement, xmlEventReader, customer);
                    }
                }
                if (event.isEndElement()) {
                    EndElement endElement = event.asEndElement();
                    if (PERSON_XML_ELEMENT_NAME.equalsIgnoreCase(endElement.getName().getLocalPart())) {
                        final DatabaseActions databaseActions = new DatabaseActions();
                        databaseActions.insert(customer);
                        customer = null;
                    }
                    if (CONTACTS_XML_ELEMENT_NAME.equalsIgnoreCase(endElement.getName().getLocalPart())) {
                        ifStartedReadingContacts = false;
                    }
                }
            }
        } catch (XMLStreamException e) {
           log.error("Problem with reading from xml file: " + filePath, e);
        } catch (FileNotFoundException e) {
            log.error("Xml file not found: " + filePath, e);
        }
    }

    private void assignCustomerFields(StartElement startElement, XMLEventReader xmlEventReader, Customer customer) throws XMLStreamException {
        switch (startElement.getName().getLocalPart()) {
            case NAME_XML_ELEMENT_NAME: {
                customer.setName(this.getDataFromEvent(xmlEventReader));
                break;
            }
            case SURNAME_XML_ELEMENT_NAME: {
                customer.setSurname(this.getDataFromEvent(xmlEventReader));
                break;
            }
            case AGE_XML_ELEMENT_NAME: {
                customer.setAge(this.getDataFromEvent(xmlEventReader));
                break;
            }
        }
    }

    private void assignContactToCustomer(StartElement startElement, XMLEventReader xmlEventReader, Customer customer) throws XMLStreamException{
        switch (startElement.getName().getLocalPart()) {
            case EMAIL_XML_ELEMENT_NAME: {
                Contact contact = new Contact(Contact.ContactType.EMAIL, this.getDataFromEvent(xmlEventReader));
                customer.addContact(contact);
                break;
            }
            case PHONE_XML_ELEMENT_NAME: {
                Contact contact = new Contact(Contact.ContactType.PHONE, this.getDataFromEvent(xmlEventReader));
                customer.addContact(contact);
                break;
            }
            case JABBER_XML_ELEMENT_NAME: {
                Contact contact = new Contact(Contact.ContactType.JABBER, this.getDataFromEvent(xmlEventReader));
                customer.addContact(contact);
                break;
            }
            default: {
                Contact contact = new Contact(Contact.ContactType.UNKNOWN, this.getDataFromEvent(xmlEventReader));
                customer.addContact(contact);
            }
        }
    }

    private String getDataFromEvent(XMLEventReader xmlEventReader) throws XMLStreamException {
        Characters nameDataEvent = (Characters) xmlEventReader.nextEvent();
        return nameDataEvent.getData();
    }
}
