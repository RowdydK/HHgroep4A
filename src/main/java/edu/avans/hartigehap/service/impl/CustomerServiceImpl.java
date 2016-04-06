package edu.avans.hartigehap.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.Iterator;
import java.util.Collection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import edu.avans.hartigehap.domain.*;
import edu.avans.hartigehap.repository.*;
import edu.avans.hartigehap.service.*;

import com.google.common.collect.Lists;

@Service("customerService")
@Repository
@Transactional
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Transactional(readOnly = true)
    public List<CopyCustomer> findAll() { return Lists.newArrayList(customerRepository.findAll());
    }

    @Transactional(readOnly = true)
    public CopyCustomer findById(Long id) {
        return customerRepository.findOne(id);
    }

    @Transactional(readOnly = true)
    public CopyCustomer findByFirstNameAndLastName(String firstName, String lastName) {

        CopyCustomer customer = null;

        List<RealCustomer> customers = customerRepository.findByFirstNameAndLastName(firstName, lastName);
        if (!customers.isEmpty()) {
            customer = customers.get(0);
        }
        return customer;
    }

    @Transactional(readOnly = true)
    public List<CopyCustomer> findCustomersForRestaurant(Restaurant restaurant) {

        // a query created using a repository method name
        List<CopyCustomer> customersForRestaurants = customerRepository.findByRestaurants(
                Arrays.asList(new Restaurant[] { restaurant }), new Sort(Sort.Direction.ASC, "lastName"));

        log.info("findCustomersForRestaurant using query created using repository method name");
        ListIterator<CopyCustomer> it = customersForRestaurants.listIterator();
        while (it.hasNext()) {
            CopyCustomer customer = it.next();
            log.info("customer = " + customer);
        }

        return customersForRestaurants;
    }

    @Transactional(readOnly = true)
    public Page<CopyCustomer> findAllByPage(Pageable pageable) {
        return customerRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<CopyCustomer> findCustomersForRestaurantByPage(Restaurant restaurant, Pageable pageable) {
        // a query created using a repository method name
        Page<CopyCustomer> customersForRestaurants = customerRepository
                .findByRestaurants((Collection<Restaurant>) Arrays.asList(new Restaurant[] { restaurant }), pageable);

        log.info("findCustomersForRestaurant using query created using repository method name");
        Iterator<CopyCustomer> it = customersForRestaurants.iterator();
        while (it.hasNext()) {
            CopyCustomer customer = it.next();
            log.info("customer = " + customer);
        }

        return customersForRestaurants;
    }

    public CopyCustomer save(CopyCustomer customer) {
        return customerRepository.save(customer);
    }

    public void delete(Long id) {
        customerRepository.delete(id);
    }

}
