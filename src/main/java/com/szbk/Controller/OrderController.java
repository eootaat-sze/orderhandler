package com.szbk.Controller;

import com.szbk.Model.Entity.CustomerOrder;
import com.szbk.Model.CustomerOrderRepository;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@UIScope
@SpringComponent
public class OrderController {
    @Autowired
    private CustomerOrderRepository repo;

    //Returns all the orders from the database.
    public List<CustomerOrder> getAllOrdersByCustomer(String customerName, String groupName, String companyName) {
//        return repo.findAllById(id);
        return repo.findCustomerOrdersByCustomerNameAndGroupNameAndCompanyName(customerName, groupName, companyName);
    }

    public boolean saveOrder(CustomerOrder order) {
        long count = repo.count();

        order.setSequence(order.getSequence().toUpperCase());
        repo.save(order);
        System.out.println("orders: " + repo.findAll());

        if (count < repo.count()) {
            return true;
        }

        return false;
    }
}