package com.szbk.Model.Entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;
import java.util.Date;

@Entity
public class CustomerOrder {
    //Auto generated ID for the customer.
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String customerName;
    private String groupName;
    private String companyName;
    private String sequence;
    private double scale;
    private String purification;
    private String type;
    private LocalDate orderDate;

    public CustomerOrder() {}

    public CustomerOrder(String customerName, String groupName, String companyName, String sequence, double scale,
                         String purification, String type, LocalDate orderDate) {
        this.customerName = customerName;
        this.groupName = groupName;
        this.companyName = companyName;
        this.sequence = sequence;
        this.scale = scale;
        this.purification = purification;
        this.type = type;
        this.orderDate = orderDate;
    }

    public String getCustomerName() {
        return this.customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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

    public String getSequence() {
        return this.sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public double getScale() {
        return this.scale;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    public String getPurification() {
        return this.purification;
    }

    public void setPurification(String purification) {
        this.purification = purification;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDate getOrderDate() {
        return this.orderDate;
    }

    public void setOrderDate(LocalDate d) {
        this.orderDate = d;
    }

    public long getId() {
        return this.id;
    }

    @Override
    public String toString() {
        return "customerName: " + customerName + ", groupName: " + groupName + ", companyName: " + companyName +
                ", sequence: " + sequence + ", scale: " + scale + ", purification: " + purification + ", type: " + type +
                ", orderDate: " + orderDate;
    }
}
