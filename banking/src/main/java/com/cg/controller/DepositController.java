package com.cg.controller;

import com.cg.model.Customer;
import com.cg.model.Deposit;
import com.cg.service.customer.ICustomerService;
import com.cg.service.deposit.IDepositService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/customers")
public class DepositController {
    @Autowired
    private IDepositService depositService;
    @Autowired
    private ICustomerService customerService;

    @GetMapping("/deposit/{id}")
    public String showDepositPage(@PathVariable Long id, Model model) {
        Optional<Customer> customerOptional = customerService.findById(id);
        if (customerOptional.isEmpty()) {
            return "/error/404";
        }
        Deposit deposit = new Deposit();
        Customer customer = customerOptional.get();
        model.addAttribute("customer", customer);
        model.addAttribute("deposit", deposit);
        return "customer/deposit";

    }

    @PostMapping("/deposit/{id}")
    public String doDeposit(@PathVariable Long id,@RequestParam double amount, Model model) {
        Optional<Customer> customerOptional = customerService.findById(id);
        if (customerOptional.isEmpty()) {
            return "/error/404";
        }
        Customer customer = customerOptional.get();
        double balance = customer.getBalance();
        Deposit deposit = new Deposit();
        customer.setBalance(balance + amount);
        customerService.save(customer);
        deposit.setIdCustomer(customer.getId());
        deposit.setAmount(amount);
        depositService.save(deposit);
        model.addAttribute("customer", customer);
        model.addAttribute("deposit", new Deposit());

        return "customer/deposit";
    }
}
