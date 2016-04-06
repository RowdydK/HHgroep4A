package edu.avans.hartigehap.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Collection;
import java.util.List;
import edu.avans.hartigehap.domain.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

public interface CustomerRepository extends PagingAndSortingRepository<CopyCustomer, Long> {

    List<RealCustomer> findByFirstNameAndLastName(String firstName, String lastName);

    List<CopyCustomer> findByRestaurants(Collection<Restaurant> restaurants, Sort sort);

    Page<CopyCustomer> findByRestaurants(Collection<Restaurant> restaurants, Pageable pageable);
}
