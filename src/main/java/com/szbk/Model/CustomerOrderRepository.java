package com.szbk.Model;

import com.szbk.Model.Entity.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long> {

    List<CustomerOrder> findCustomerOrdersByCustomerNameAndGroupNameAndCompanyName(String customerName, String groupName, String companyName);

    List<CustomerOrder> findCustomerOrdersByCustomerNameAndGroupNameAndCompanyNameAndStatusEquals(
            String customerName, String groupName, String companyName, String status);
}
