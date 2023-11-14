package com.example.managecustomers.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.example.managecustomers.demo.model.Credential;
import com.example.managecustomers.demo.model.Customer;
import com.example.managecustomers.demo.service.CustomerService;

import org.springframework.ui.Model;

@Controller
@SessionAttributes("token")
public class CustomerController {
    @Autowired
    private CustomerService service;

    @GetMapping("/")
    public String viewIndex(Model model) {
        model.addAttribute("credential", new Credential());
        return "index";
    }

    @PostMapping("/auth")
    public String authenticate(@ModelAttribute("credential") Credential credential, Model model) {
        String bearer = service.authenticate(credential);
        if (bearer != null) {
            model.addAttribute("token", bearer);
            return "redirect:/customers";
        }
        return "redirect:/";
    }


    @GetMapping("/customers") 
    public String viewHome(Model model) {
        if ((String)model.getAttribute("token") == "") {
            return "redirect:/";
        }
        model.addAttribute("listCustomers", service.getCustomers((String)model.getAttribute("token")));
        return "home";
    }

    @GetMapping("/add")
    public String addNewCustomer(Model model) {
        if ((String)model.getAttribute("token") != "") {
            model.addAttribute("customer", new Customer());
            return "add_customer";
        }
        return "redirect:/";
    }

    @PostMapping("/saveCustomer") 
    public String saveCustomer(@ModelAttribute("customer") Customer customer, Model model) {
        if ((String)model.getAttribute("token") != "") {
            service.saveCustomer(customer, (String)model.getAttribute("token"));
            return "redirect:/customers";
        }
        return "redirect:/";
    }

    @GetMapping("/delete/{uuid}")
    public String deleteCustomer(@PathVariable String uuid, Model model) {
        if ((String)model.getAttribute("token") != "") {
            service.delete(uuid, (String)model.getAttribute("token"));
            return "redirect:/customers";
        }
        return "redirect:/";
    }

    @GetMapping("/update/{uuid}")
    public String updateExistingCustomer(@PathVariable String uuid, Model model, 
        @RequestParam("first_name") String firstName,
        @RequestParam("last_name") String lastName,
        @RequestParam("address") String address,
        @RequestParam("city") String city,
        @RequestParam("street") String street,
        @RequestParam("state") String state,
        @RequestParam("email") String email,
        @RequestParam("phone") String phone) {
        if ((String)model.getAttribute("token") != "") {
            Customer customer = new Customer();
            customer.setUuid(uuid);
            customer.setFirst_name(firstName);
            customer.setLast_name(lastName);
            customer.setAddress(address);
            customer.setCity(city);
            customer.setStreet(street);
            customer.setState(state);
            customer.setEmail(email);
            customer.setPhone(phone);

            model.addAttribute("customer", customer);
            model.addAttribute("uuid", customer.getUuid());
            return "update_customer";
        }
        return "redirect:/";
    }

    @PostMapping("/updateCustomer")
    public String updateCustomerCall(@ModelAttribute("customer") Customer customer, Model model) {
        if ((String)model.getAttribute("token") != "") {
            service.updateCustomer(customer, customer.getUuid(), (String)model.getAttribute("token"));
            return "redirect:/customers";
        }
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(Model model) {
        model.addAttribute("token","");
        return "redirect:/";
    }
}
