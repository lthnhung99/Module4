package com.cg.model;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

@Entity
@Table(name = "customers")
public class Customer extends BaseEntity implements Validator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //    @NotBlank(message = "Họ tên là bắt buộc")
//    @Pattern(regexp = "^(?!\\d)[a-zA-Z\\s]{5,20}$",message = "Tên không được chứa ký tự số, nằm trong khoảng 5-20 ký tự")
    @Column(name = "full_name", nullable = false)
    private String fullName;
//    @NotBlank(message = "Email là bắt buộc")
//    @Pattern(regexp = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$",message = "Email không hợp lệ")

    @Column(nullable = false, unique = true)
    private String email;
    //    @Pattern(regexp ="((84|0)[3|5|7|8|9])+([0-9]{8})\\b", message = "Số điện thoại không hợp lệ")
    private String phone;
    private String address;
    @Column(precision = 10, scale = 0, nullable = false)
    private BigDecimal balance;

    public Customer() {
    }

    public Customer(Long id, String fullName, String email, String phone, String address, BigDecimal balance) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Customer.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        Customer customer = (Customer) target;

        String fullName = customer.fullName;
        String email = customer.email;
        String phone = customer.phone;
        String regexName = "^(?!\\d)[a-zA-Z\\s]{5,20}$";
        String regexEmail = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";
//        String regexPhone = "((84|0)[3|5|7|8|9])+([0-9]{8})\\b";


        if (fullName.length() == 0) {
            errors.rejectValue("fullName", "fullName.empty");
        } else {
            if (!fullName.matches(regexName)) {
                errors.rejectValue("fullName", "fullName.regex");
            }

        }
        if (email.length() == 0) {
            errors.rejectValue("email", "email.empty");
        } else {

            if (!email.matches(regexEmail)) {
                errors.rejectValue("email", "email.regex");
            }

//        if (!phone.matches(regexPhone)) {
//            errors.rejectValue("phone", "Số điện thoại không hợp lệ");
//        }

        }
    }
}
