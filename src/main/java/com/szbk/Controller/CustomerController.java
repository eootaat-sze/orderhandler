package com.szbk.Controller;

import com.szbk.Model.Entity.Customer;
import com.szbk.Model.CustomerRepository;
import com.szbk.Model.Entity.User;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

//The controller, which does the customer things, like login and registration.
@UIScope
@SpringComponent
public class CustomerController {
    @Autowired
    CustomerRepository repo;

    public boolean registration(Customer c) {
        long count = repo.count();

        //Actually, it returns the same customer object I saved, so... why?
        repo.save(c);

        //If we have more items in the repository after the save, than we had before, then the customer was saved (practically).
        return count < repo.count();
    }

    public Customer login(User u) {
        List<Customer> customers = repo.findAll();

        //If, in the database, there is the given email and password, then it is okay, if not, something is wrong.
        for (Customer customer : customers) {
            if (customer.getEmail().equals(u.getEmail())) {
                if (customer.getPassword().equals(u.getPassword())) {
//                    System.out.println("controller - customer: " + customer);
                    return customer;
                }
            }
        }

        return null;
    }

    public List<String> getAllCustomersNameAndEmail() {
        List<String> customerNameAndEmail = new ArrayList<>();
        List<Customer> customers = repo.findAll();
        String value;

        System.out.println("controller: " + customers);
        for (Customer c : customers) {
            value = c.getEmail() + " - " + c.getCustomerName();
//            System.out.println("value: " + value);
            customerNameAndEmail.add(value);
        }

//        System.out.println("controller: " + customers);

        return customerNameAndEmail;
    }

    public Customer findCustomerByEmail(String email) {
        return repo.findCustomerByEmail(email);
    }

    public List<Customer> getAllCustomers() {
        return repo.findAll();
    }
}
