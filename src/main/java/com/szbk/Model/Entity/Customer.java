package com.szbk.Model.Entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Date;

@Entity
public class Customer {
    //Auto generated ID for the customer.
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String customerName;
    private String password;
    private String email;
    private String groupName;
    private String companyName;
    private String innerName;

    public Customer() {}

    public Customer(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Customer(String customerName, String password, String email, String groupName, String companyName) {
        this.customerName = customerName;
        this.password = password;
        this.email = email;
        this.groupName = groupName;
        this.companyName = companyName;
    }

    public String getCustomerName() {
        return this.customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getCompanyName() {
        return this.companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getInnerName() {
        return this.innerName;
    }

    public void setInnerName(String innerName) {
        this.innerName = innerName;
    }

    public long getId() {
        return this.id;
    }

    @Override
    public String toString() {
        return "id: " + id + ", name: " + customerName + "password: " + password + " email: " + email + " groupName: " + groupName + " companyName: " + companyName;
    }
}
