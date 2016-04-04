package edu.avans.hartigehap.service;

import edu.avans.hartigehap.domain.Customer;
import edu.avans.hartigehap.domain.DiningTable;
import edu.avans.hartigehap.domain.EmptyBillException;
import edu.avans.hartigehap.domain.Order;
import edu.avans.hartigehap.domain.StateException;

public interface OnlineOrderService {
	
	Order getOrder(String orderId);
	
	void addOrderItem(Customer customer, String menuItemName);

    void deleteOrderItem(Customer customer, String menuItemName);

    void submitOrder(Customer customer) throws StateException;

    void submitBill(Customer customer) throws StateException, EmptyBillException;
}
