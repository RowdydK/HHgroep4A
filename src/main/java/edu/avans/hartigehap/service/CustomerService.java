package edu.avans.hartigehap.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import edu.avans.hartigehap.domain.*;

public interface CustomerService {
    List<CopyCustomer> findAll();

    CopyCustomer findById(Long id);

    CopyCustomer findByFirstNameAndLastName(String firstName, String lastName);

    List<CopyCustomer> findCustomersForRestaurant(Restaurant restaurant);

    Page<CopyCustomer> findAllByPage(Pageable pageable);

    Page<CopyCustomer> findCustomersForRestaurantByPage(Restaurant restaurant, Pageable pageable);

    CopyCustomer save(CopyCustomer customer);

    void delete(Long id);
}
