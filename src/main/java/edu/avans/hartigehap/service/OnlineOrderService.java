package edu.avans.hartigehap.service;

import edu.avans.hartigehap.domain.Bill;
import edu.avans.hartigehap.domain.Customer;
import edu.avans.hartigehap.domain.EmptyBillException;
import edu.avans.hartigehap.domain.Ingredient;
import edu.avans.hartigehap.domain.Order;
import edu.avans.hartigehap.domain.StateException;

public interface OnlineOrderService {
	
	Order getOrder(String orderId);
	
	void addOrderItem(Bill bill, String menuItemName);

    void deleteOrderItem(Bill bill, String menuItemName);

    void submitOrder(Order order) throws StateException;
    
	void addOrderItemIngredient(Long OrderItemId, Long ingredientId);

    void submitBill(Bill bill) throws StateException, EmptyBillException;
    
}
