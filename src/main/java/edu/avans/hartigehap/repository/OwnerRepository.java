package edu.avans.hartigehap.repository;

import edu.avans.hartigehap.domain.Owner;
import edu.avans.hartigehap.domain.Restaurant;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Collection;
import java.util.List;

/**
 * Created by Rowdy on 16-2-2016.
 */
public interface OwnerRepository extends PagingAndSortingRepository<Owner, Long> {
    List<Owner> findByName(String name);

    List<Owner> findByRestaurants(Collection<Restaurant> restaurants, Sort sort);
}
