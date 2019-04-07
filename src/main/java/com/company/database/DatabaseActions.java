package com.company.database;

import com.company.model.Contact;
import com.company.model.Customer;
import com.company.utils.PropertyFileResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

public class DatabaseActions {
    private static final Logger log = LoggerFactory.getLogger(DatabaseActions.class);
    private Connection connection;

    public DatabaseActions() {
    }

    public void insert(Customer customer) {
        try {
            this.connect();
            connection.setAutoCommit(false);
            int customerId = this.insertCustomer(customer);
            this.insertContacts(customerId, customer.getContacts());
            connection.commit();
            connection.close();
        } catch (SQLException e) {
            log.error("Unable to insert rows to database", e);
            this.rollbackTransaction();
        }
    }

    private int insertCustomer(Customer customer) throws SQLException {
        final String sql = "INSERT INTO CUSTOMERS(NAME, SURNAME, AGE) VALUES(?, ?, ?)";
        String [] columnNames = new String[]{"id"};
        PreparedStatement preparedStatement = connection.prepareStatement(sql, columnNames);
        preparedStatement.setString(1, customer.getName());
        preparedStatement.setString(2, customer.getSurname());
        this.setAgeOfCustomer(customer, preparedStatement);
        preparedStatement.execute();
        int customerId = this.getIdOfInsertedRow(preparedStatement.getGeneratedKeys());
        preparedStatement.close();
        return customerId;
    }

    private void insertContacts(int customerId, List<Contact> contacts) throws SQLException {
        final String sql = "INSERT INTO CONTACTS(ID_CUSTOMER, TYPE, CONTACT) VALUES(?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        for(Contact contact : contacts) {
            preparedStatement.setInt(1, customerId);
            preparedStatement.setInt(2, contact.getContactType().getNumericValue());
            preparedStatement.setString(3, contact.getContactText());
            preparedStatement.addBatch();
        }
        preparedStatement.executeBatch();
        preparedStatement.close();
    }

    private int getIdOfInsertedRow(ResultSet generatedKeys) throws SQLException {
        if(generatedKeys.next()) {
            int primaryKey = generatedKeys.getInt(1);
            generatedKeys.close();
            return primaryKey;
        }
        log.error("Unable to get primary key of inserted customer");
        throw new SQLException("Unable to get primary key of inserted customer");
    }

    private void setAgeOfCustomer(Customer customer, PreparedStatement preparedStatement) throws SQLException {
        String age = customer.getAge();
        if (age == null || age.replaceAll(" ", "").length() == 0) {
            preparedStatement.setNull(3, Types.INTEGER);
        } else {
            preparedStatement.setString(3, customer.getAge());
        }
    }

    private void connect() {
        if (connection == null || isConnectionClosed()) {
            String url = PropertyFileResolver.getInstance().getPropertyValue(PropertyFileResolver.getDbUrlPropertyName());
            String user = PropertyFileResolver.getInstance().getPropertyValue(PropertyFileResolver.getDbUserPropertyName());
            String password = PropertyFileResolver.getInstance().getPropertyValue(PropertyFileResolver.getDbPasswordPropertyName());
            try {
                connection = DriverManager.getConnection(url, user, password);
            } catch (SQLException e) {
                log.error("Unable to get connection to database", e);
                throw new IllegalStateException("Unable to get connection to database");
            }
        }
    }

    private boolean isConnectionClosed() {
        try {
            return connection.isClosed();
        } catch (SQLException e) {
            log.error("Unable to check if connection is closed", e);
            throw new IllegalStateException("Unable to check if connection is closed", e);
        }
    }

    private void rollbackTransaction() {
        try {
            connection.rollback();
        } catch (SQLException e) {
            log.error("Unable to roll back transaction", e);
            throw new IllegalStateException("Unable to roll back transaction", e);
        }
    }
}
