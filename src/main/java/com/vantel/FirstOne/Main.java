package com.vantel.FirstOne;

import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.persistence.EntityNotFoundException;

@SpringBootApplication
@RestController
@RequestMapping("/api/v1/customers")
public class Main {

    private final CustomerRepository customerRepository;

    public Main(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @GetMapping
    public List<Customer> getCustomers() {
        return customerRepository.findAll();
    }

    
    @GetMapping("{customerId}")
    public Customer getOneCustomer(@PathVariable("customerId") Integer id) {
        return customerRepository.findById(id).orElse(null);
    }
 

    record NewCustomerRequest (
        String name,
        String email,
        Integer age
    ) {}

    @PostMapping
    public void addCustomer(@RequestBody NewCustomerRequest request) {
        Customer customer = new Customer();
        customer.setName(request.name());
        customer.setEmail(request.email());
        customer.setAge(request.age());
    
        customerRepository.save(customer);        
    }

    @DeleteMapping("{customerId}")
    public void deleteCustomer(@PathVariable("customerId") Integer id) {
        customerRepository.deleteById(id);
    }

    @PutMapping("{customerId}")
    public String editCustomer(
                            @PathVariable("customerId") Integer id, 
                            @RequestBody NewCustomerRequest request) {
        try {
            Customer customer = customerRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Customer not found"));
            customer.setName(request.name());
            customer.setEmail(request.email());
            customer.setAge(request.age());
            customerRepository.save(customer);
            return "Succesfully updated!";
        } catch (EntityNotFoundException e) {
            return e.getMessage();
        }
    }
}
