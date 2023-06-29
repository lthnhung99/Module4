package com.cg.controller;

import com.cg.model.Customer;
import com.cg.service.customer.CustomerServiceImpl;
import com.cg.service.customer.ICustomerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/customers")


public class CustomerController {
    @GetMapping
    public ModelAndView showListPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("customer/list");
        ICustomerService customerService=new CustomerServiceImpl();
        List<Customer> customers=customerService.getAll();
        if(customers!=null) {
            for (Customer customer:customers) {
                System.out.println(customer.toString());
            }
        }
        modelAndView.addObject("customers",customers);
        return modelAndView;
    }
    @GetMapping("/{id}")
    public String showEditPage(@PathVariable String id, Model model) {
        ICustomerService customerService = new CustomerServiceImpl();

        try {
            Long customerId = Long.parseLong(id);
            Customer customer = customerService.getById(customerId);

            model.addAttribute("customer", customer);

            return "customer/edit";
        }
        catch (Exception e) {
            return "error/404";
        }
    }

    @GetMapping("/create")
    public String showCreatePage(Model model) {
        Customer customer=new Customer();
        model.addAttribute("customer",customer);
        return "customer/create";
    }

        @GetMapping("/info/{id}")
    public String showInfoPage(@PathVariable Long id, Model model) {
        ICustomerService customerService = new CustomerServiceImpl();

        Customer customer = customerService.getById(id);

        model.addAttribute("customer", customer);

        return "customer/info";
    }
    @PostMapping("/create")
    public String doCreate(@ModelAttribute Customer customer, Model model) {

        ICustomerService customerService = new CustomerServiceImpl();
        customerService.add(customer);

        model.addAttribute("customer", new Customer());

        return "customer/create";
    }

    @PostMapping("/{id}")
    public String doUpdate(@PathVariable Long id, @ModelAttribute Customer customer, Model model) {

        ICustomerService customerService = new CustomerServiceImpl();
        customer.setId(id);
        customerService.update(customer);

        List<Customer> customers = customerService.getAll();

        model.addAttribute("customers", customers);

        return "customer/list";
    }
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable String id, Model model) {
        ICustomerService customerService=new CustomerServiceImpl();
        List<Customer> customers=customerService.getAll();
        try {
            Long customerId=Long.parseLong(id);
            Customer customer=customerService.getById(customerId);
            if(customers!=null) {
                customers.remove(customer);
            }
            model.addAttribute("customers",customers);
            return "customer/list";
        } catch (Exception e) {
            return "error/404";
        }
    }


}
