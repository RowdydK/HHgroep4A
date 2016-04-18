package edu.avans.hartigehap.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import edu.avans.hartigehap.domain.OrderItem;

public interface OrderItemRepository extends PagingAndSortingRepository<OrderItem, Long> {
	
}
