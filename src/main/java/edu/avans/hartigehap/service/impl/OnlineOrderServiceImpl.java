package edu.avans.hartigehap.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.avans.hartigehap.domain.Bill;
import edu.avans.hartigehap.domain.Customer;
import edu.avans.hartigehap.domain.DiningTable;
import edu.avans.hartigehap.domain.EmptyBillException;
import edu.avans.hartigehap.domain.MenuItem;
import edu.avans.hartigehap.domain.Order;
import edu.avans.hartigehap.domain.StateException;
import edu.avans.hartigehap.repository.BillRepository;
import edu.avans.hartigehap.repository.CustomerRepository;
import edu.avans.hartigehap.repository.MenuItemRepository;
import edu.avans.hartigehap.repository.OrderRepository;
import edu.avans.hartigehap.service.OnlineOrderService;
import lombok.extern.slf4j.Slf4j;

@Service("onlineOrderService")
@Repository
@Transactional(rollbackFor = StateException.class)
@Slf4j
public class OnlineOrderServiceImpl implements OnlineOrderService {
	@Autowired
    private MenuItemRepository menuItemRepository;
	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private BillRepository billRepository;
	
	@Transactional(readOnly = true)
	public Order getOrder(String orderId){
		return orderRepository.findOne(Long.valueOf(orderId));
	}
	
	@Override
	public void addOrderItem(Bill bill, String menuItemName) {
		if (bill == null){
			bill = new Bill();
		}
		MenuItem menuItem = menuItemRepository.findOne(menuItemName);
		bill.getCurrentOrder().addOrderItem(menuItem);
		billRepository.save(bill);
	}
	
	@Override
	public void deleteOrderItem(Bill bill, String menuItemName) {
		if (bill == null){
			bill = new Bill();
		}
		MenuItem menuItem = menuItemRepository.findOne(menuItemName);
		bill.getCurrentOrder().deleteOrderItem(menuItem);
	}
	@Override
	public void submitOrder(Order order) throws StateException {
		order.submit();
		
	}
	@Override
	public void submitBill(Bill bill) throws StateException, EmptyBillException {
		bill.submitOrder();
		
	}
	
}
