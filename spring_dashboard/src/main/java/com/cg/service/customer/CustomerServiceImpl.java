package com.cg.service.customer;

import com.cg.model.Customer;

import java.util.ArrayList;
import java.util.List;

public class CustomerServiceImpl implements ICustomerService {

    public static List<Customer> customers = new ArrayList<Customer>();
    public static Long maxId = 1L;
    static {
        customers.add(new Customer(maxId++,"Tashya Ochoa","toterar@mailinator.com","+1 (793) 841-7639","Huế"));
        customers.add(new Customer(maxId++," Ochoa","ochiar@mailinator.com","+1 (793) 841-7639","Hà nội"));
        customers.add(new Customer(maxId++,"John","john@mailinator.com","+1 (793) 841-7639","Đà nẵng"));
    }

    @Override
    public List<Customer> getAll() {
        return customers;
    }

    @Override
    public Customer getById(Long id) {
        for (Customer customer : customers) {
            if (customer.getId().equals(id)) {
                return customer;
            }
        }
        return null;
    }

    @Override
    public void add(Customer customer) {
        customer.setId(maxId);
        customers.add(customer);
        maxId++;

    }

    @Override
    public void update(Customer customer) {
        int index = -1;
        for (int i = 0; i < customers.size(); i++) {
            if (customers.get(i).getId().equals(customer.getId())) {
                index = i;
            }
        }

        if (index > -1) {
            customers.set(index, customer);
        }

    }
}
