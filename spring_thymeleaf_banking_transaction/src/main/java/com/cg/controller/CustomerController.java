package com.cg.controller;

import com.cg.model.Customer;
import com.cg.model.Deposit;
import com.cg.model.Transfer;
import com.cg.model.Withdraw;
import com.cg.service.customer.ICustomerService;
import com.cg.service.deposit.IDepositService;
import com.cg.service.transfer.ITransferService;
import com.cg.service.withdraw.IWithdrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/customers")
public class CustomerController {
    @Autowired
    private ICustomerService customerService;
    @Autowired
    private IDepositService depositService;
    @Autowired
    private IWithdrawService withdrawService;
    @Autowired
    private ITransferService transferService;


    @GetMapping
    public String showListPage(Model model) {
        List<Customer> customers = customerService.findAll();
        model.addAttribute("customers", customers);
        return "customer/list";
    }

    @GetMapping("/create")
    public String showCreatePage(Model model) {
        model.addAttribute("customer", new Customer());


        return "/customer/create";
    }

    @PostMapping("/create")
    public String doCreate(Model model, @ModelAttribute Customer customer, BindingResult bindingResult) {

       customer.validate(customer, bindingResult);

        if (bindingResult.hasFieldErrors()) {
            model.addAttribute("hasError", true);
            return "customer/create";
        }

        String email = customer.getEmail();

        Boolean existsEmail = customerService.existsByEmail(email);

        if (existsEmail) {
            model.addAttribute("notValid", true);
            model.addAttribute("message", "Email đã tồn tại");
            return "customer/create";
        }

        customer.setId(null);
        customer.setBalance(BigDecimal.ZERO);
        customerService.save(customer);

        return "/customer/create";
    }

    @GetMapping("/edit/{id}")
    public String showEditPage(@PathVariable Long id, Model model) {

        Optional<Customer> customerOptional = customerService.findById(id);

        if (customerOptional.isEmpty()) {
            model.addAttribute("error", true);
        } else {
            Customer customer = customerOptional.get();
            model.addAttribute("customer", customer);
        }
        return "customer/edit";
    }


    @PostMapping("/edit/{id}")
    public String doEdit(@PathVariable Long id, @Validated @ModelAttribute Customer customer, Model model, BindingResult bindingResult) {
        customer.validate(customer, bindingResult);
        if (bindingResult.hasFieldErrors()) {
            model.addAttribute("hasError", true);
            return "customer/create";
        }
        String email = customer.getEmail();

        Boolean existsEmail = customerService.existsByEmail(email);

        if (existsEmail) {
            model.addAttribute("notValid", true);
            model.addAttribute("message", "Email đã tồn tại");
            return "customer/edit";
        }

        Optional<Customer> customerOptional = customerService.findById(id);

        if (customerOptional.isEmpty()) {
            model.addAttribute("error", true);
        } else {
            Customer customer1 = customerOptional.get();
            customer.setId(id);
            customer.setBalance(customer1.getBalance());
            //customer.setBalance(BigDecimal.ZERO);
            customerService.save(customer);
            model.addAttribute("customer", customer);
            model.addAttribute("success", true);
            model.addAttribute("message", "Cập nhật thành công");
        }
        return "customer/edit";
    }

    @GetMapping("/delete/{id}")
    public String doDelete(@PathVariable String id, RedirectAttributes redirectAttributes) {

        try {
            Long customerId = Long.parseLong(id);
            customerService.deleteById(customerId);

            redirectAttributes.addFlashAttribute("success", true);
            redirectAttributes.addFlashAttribute("message", "Xoá thành công");
            return "redirect:/customers";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", true);
            redirectAttributes.addFlashAttribute("message", "Không thể xoá");
            return "redirect:/customers";
        }

    }


    @GetMapping("/deposit/{customerId}")
    public String showDepositForm(@PathVariable Long customerId, Model model) {

        Optional<Customer> customerOptional = customerService.findById(customerId);

        if (customerOptional.isEmpty()) {
            model.addAttribute("error", true);
            model.addAttribute("message", "Không tìm thấy customer");

        } else {

            Customer customer = customerOptional.get();
            Deposit deposit = new Deposit();
            deposit.setCustomer(customer);

            model.addAttribute("deposit", deposit);

        }
        return "/customer/deposit";
    }

    @PostMapping("/deposit/{customerId}")
    public String doDeposit(@PathVariable Long customerId, Model model, @Validated @ModelAttribute Deposit deposit, BindingResult bindingResult) {
        deposit.validate(deposit, bindingResult);

        if (bindingResult.hasFieldErrors()) {
            model.addAttribute("hasError", true);
            return "customer/deposit";
        }


        Optional<Customer> customerOptional = customerService.findById(customerId);
        if (customerOptional.isEmpty()) {
            model.addAttribute("error", true);
            model.addAttribute("message", "ID không tồn tại");
        } else {
            Customer customer = customerOptional.get();
            BigDecimal currentBalance = customer.getBalance();
            BigDecimal newBalance = currentBalance.add(deposit.getTransactionAmount());
            customer.setBalance(newBalance);
            customerService.save(customer);

            deposit.setId(null);
            deposit.setCustomer(customer);
            depositService.save(deposit);

            model.addAttribute("deposit", deposit);

            model.addAttribute("success", true);
            model.addAttribute("message", "Gửi thành công");

        }
        return "/customer/deposit";
    }

    @GetMapping("/withdraw/{customerId}")
    public String showWithdrawForm(@PathVariable Long customerId, Model model) {
        try {
            Optional<Customer> customerOptional = customerService.findById(customerId);

            if (customerOptional.isEmpty()) {
                model.addAttribute("error", true);
                model.addAttribute("message", "ID không tồn tại");

            } else {
                Customer customer = customerOptional.get();
                Withdraw withdraw = new Withdraw();
                withdraw.setCustomer(customer);
                model.addAttribute("withdraw", withdraw);
            }
        } catch (Exception e) {
            return "/error/500";
        }

        return "/customer/withdraw";
    }

    @PostMapping("/withdraw/{customerId}")
    public String doWithdraw(@PathVariable Long customerId, @ModelAttribute Withdraw withdraw, Model model, BindingResult bindingResult) {
        withdraw.validate(withdraw, bindingResult);
        if (bindingResult.hasFieldErrors()) {
            model.addAttribute("hasError", true);
            return "customer/withdraw";
        }
        Optional<Customer> customerOptional = customerService.findById(customerId);

        if (customerOptional.isEmpty()) {
            model.addAttribute("error", true);
            model.addAttribute("message", "ID không tồn tại");
        } else {
            Customer customer = customerOptional.get();
            BigDecimal currentBalance = customer.getBalance();
            if (currentBalance.compareTo(withdraw.getTransactionAmount()) >= 0) {

                BigDecimal newBalance = currentBalance.subtract(withdraw.getTransactionAmount());
                customer.setBalance(newBalance);
                customerService.save(customer);

                withdraw.setId(null);
                withdraw.setCustomer(customer);
                withdrawService.save(withdraw);

                model.addAttribute("success", true);
                model.addAttribute("message", "Rút thành công");

            } else {
                model.addAttribute("error", true);
                model.addAttribute("message", "Số dư không đủ");
                withdraw.setCustomer(customer);
            }
            model.addAttribute("withdraw", withdraw);
        }
        return "/customer/withdraw";
    }

    @GetMapping("/transfer/{senderId}")
    public String showTransferForm(@PathVariable Long senderId, Model model) {

        Optional<Customer> senderOptional = customerService.findById(senderId);

        if (senderOptional.isEmpty()) {
            model.addAttribute("error", true);
            model.addAttribute("message", "ID không tồn tại");
            return "/customer/transfer";
        } else {
            Customer sender = senderOptional.get();

            Transfer transfer = new Transfer();
            transfer.setSender(sender);
            model.addAttribute("transfer", transfer);

            List<Customer> recipients = customerService.findAll();
            model.addAttribute("recipients", recipients);
        }


        return "/customer/transfer";
    }

    @PostMapping("/transfer/{senderId}")
    public String doTransfer(Model model, @PathVariable Long senderId, @RequestParam("recipient.id") Long recipientId, @ModelAttribute Transfer transfer, BindingResult bindingResult) {
        transfer.validate(transfer, bindingResult);
        if (bindingResult.hasFieldErrors()) {
            model.addAttribute("hasError", true);
            return "customer/transfer";
        }

        Optional<Customer> senderOptional = customerService.findById(senderId);
        List<Customer> recipients = customerService.findAll();
        model.addAttribute("recipients", recipients);
        model.addAttribute("transfer", transfer);

        if (senderOptional.isEmpty()) {
            model.addAttribute("error", true);
            model.addAttribute("message", "Không tìm thấy sender");
            return "/customer/transfer";
        }

        Optional<Customer> recipientOptional = customerService.findById(transfer.getRecipient().getId());

        if (recipientOptional.isEmpty()) {
            model.addAttribute("error", true);
            model.addAttribute("message", "Không tìm thấy recipient");
            return "/customer/transfer";
        }

        Customer sender = senderOptional.get();
        Customer recipient = recipientOptional.get();

        if (sender.getId() == recipient.getId()) {
            model.addAttribute("error", true);
            model.addAttribute("message", "Recipient phải khác Sender");
            return "/customer/transfer";
        }

        BigDecimal currentBalance = sender.getBalance();
        BigDecimal transferAmount = transfer.getTransferAmount();
        Long fees = transfer.getFees();
        BigDecimal feesAmount = transferAmount.multiply(BigDecimal.valueOf(fees)).divide(BigDecimal.valueOf(100));
        BigDecimal transactionAmount = transferAmount.add(feesAmount);

        if (currentBalance.compareTo(transactionAmount) < 0) {
            model.addAttribute("currentRecipient", recipientId);
            model.addAttribute("error", true);
            model.addAttribute("message", "Số dư không đủ");
            return "/customer/transfer";

        }
        BigDecimal newBalance = currentBalance.subtract(transactionAmount);
        sender.setBalance(newBalance);
        customerService.save(sender);

        BigDecimal recipientCurrentBalance = recipient.getBalance();
        BigDecimal recipientNewBalance = recipientCurrentBalance.add(transfer.getTransferAmount());
        recipient.setBalance(recipientNewBalance);
        customerService.save(recipient);

        transfer.setId(null);
        transfer.setSender(sender);
        transfer.setRecipient(recipient);
        transfer.setTransferAmount(transferAmount);
        transfer.setFeesAmount(feesAmount);
        transfer.setTransactionAmount(transactionAmount);
        transferService.save(transfer);
        model.addAttribute("transfer", transfer);

        model.addAttribute("currentRecipient", recipientId);
        model.addAttribute("success", true);
        model.addAttribute("message", "Chuyển thành công");

        return "/customer/transfer";
    }

    @GetMapping("/history")
    public String showHistoryTransferPage(Model model) {
        List<Transfer> transfers = transferService.findAll();

        model.addAttribute("transfers", transfers);

        return "customer/history";
    }

}
