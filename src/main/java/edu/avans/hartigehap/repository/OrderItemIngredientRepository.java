package edu.avans.hartigehap.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import edu.avans.hartigehap.domain.OrderItemIngredient;

public interface OrderItemIngredientRepository extends PagingAndSortingRepository<OrderItemIngredient, Long> {
	
}
