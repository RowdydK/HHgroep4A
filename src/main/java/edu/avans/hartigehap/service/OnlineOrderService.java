package edu.avans.hartigehap.service;

import edu.avans.hartigehap.domain.*;

public interface OnlineOrderService {
	void addOrderItem(Customer customer, String menuItemName);

    void deleteOrderItem(Customer customer, String menuItemName);

    void submitOrder(Customer customer) throws StateException;

    void submitBill(Customer customer) throws StateException, EmptyBillException;
}
