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
        return repo.findCustomerOrdersByCustomerNameAndGroupNameAndCompanyName(customerName, groupName, companyName);
    }

    public List<CustomerOrder> getAllOrders() {
        return repo.findAll();
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

    public boolean saveManyOrder(CustomerOrder... orders) {
        long count = repo.count();
        boolean success = false;

        for (CustomerOrder order : orders) {
            success = saveOrder(order);

            if (!success) {
                System.out.println("Problem with the order: " + order);
                break;
            }
        }

        return success;
    }
}
