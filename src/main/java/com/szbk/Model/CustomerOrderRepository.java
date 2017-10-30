package com.szbk.Model;

import com.szbk.Model.Entity.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long> {

    List<CustomerOrder> findCustomerOrdersByCustomerNameAndGroupNameAndCompanyName(String customerName, String groupName, String companyName);

    List<CustomerOrder> findCustomerOrdersByCustomerNameAndGroupNameAndCompanyNameAndStatusEquals(
            String customerName, String groupName, String companyName, String status);

    List<CustomerOrder> findCustomerOrdersByCompanyNameAndGroupNameAndCustomerName(
            String companyName, String groupName, String customerName);

    @Query("select distinct companyName from CustomerOrder")
    List<String> getAllCompanyNames();

    @Query("select distinct groupName from CustomerOrder where companyName = ?")
    List<String> getAllGroupNames(String companyName);

    @Query("select distinct customerName from CustomerOrder where companyName = ?1 and groupName = ?2")
    List<String> getAllCustomerNames(String companyName, String groupName);
}
