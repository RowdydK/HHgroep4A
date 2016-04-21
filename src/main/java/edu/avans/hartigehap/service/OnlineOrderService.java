package edu.avans.hartigehap.service;

import edu.avans.hartigehap.domain.*;

import java.util.List;

public interface OnlineOrderService {

	Order getOrder(String orderId);
	
	void addOrderItem(Bill bill, String menuItemName);

    void deleteOrderItem(Bill bill, String menuItemName);

    void submitOrder(Order order) throws StateException;
    
	void addOrderItemIngredient(Long OrderItemId, Long ingredientId);

    void submitBill(Bill bill) throws StateException, EmptyBillException;

    List<Order> findPlannedOrders(Restaurant restaurant);
    
}
