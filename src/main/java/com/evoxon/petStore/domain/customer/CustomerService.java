package com.evoxon.petStore.domain.customer;

import com.evoxon.petStore.dto.CustomerDto;
import com.evoxon.petStore.persistence.CustomerEntity;
import com.evoxon.petStore.persistence.CustomerRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Service
public class CustomerService implements UserDetailsService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<CustomerEntity> optionalCustomerEntity = customerRepository.findByUsername(username);
        if(optionalCustomerEntity.isEmpty()){
            throw new UsernameNotFoundException("User not found");
        }
        else{
            Customer customer = CustomerDto.fromEntityToDomain(optionalCustomerEntity.get());
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(customer.getCustomerRole().name()));
            return new CustomUserDetails(customer.getUsername(), customer.getPassword(), authorities, customer.getId());
        }
    }

    public Customer getCustomerByName(String username) {
        Optional<CustomerEntity> optionalCustomerEntity = customerRepository.findByUsername(username);
        if (optionalCustomerEntity.isPresent()){
            return CustomerDto.fromEntityToDomain(optionalCustomerEntity.get());
        }
        else{
            return null;
        }


    }

}
