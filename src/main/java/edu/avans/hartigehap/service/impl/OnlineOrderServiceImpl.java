package edu.avans.hartigehap.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.avans.hartigehap.domain.Bill;
import edu.avans.hartigehap.domain.Customer;
import edu.avans.hartigehap.domain.EmptyBillException;
import edu.avans.hartigehap.domain.Ingredient;
import edu.avans.hartigehap.domain.MenuItem;
import edu.avans.hartigehap.domain.Order;
import edu.avans.hartigehap.domain.OrderItem;
import edu.avans.hartigehap.domain.OrderItemIngredient;
import edu.avans.hartigehap.domain.StateException;
import edu.avans.hartigehap.repository.BillRepository;
import edu.avans.hartigehap.repository.CustomerRepository;
import edu.avans.hartigehap.repository.IngredientRepository;
import edu.avans.hartigehap.repository.MenuItemRepository;
import edu.avans.hartigehap.repository.OrderItemIngredientRepository;
import edu.avans.hartigehap.repository.OrderItemRepository;
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
    private OrderItemRepository orderItemRepository;
	@Autowired
    private OrderItemIngredientRepository orderItemIngredientRepository;
	@Autowired
    private IngredientRepository ingredientRepository;
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
	public void addOrderItemIngredient(Long OrderItemId, Long ingredientId) {
		OrderItem orderItem = orderItemRepository.findOne(OrderItemId);
		Ingredient ingredient = ingredientRepository.findOne(ingredientId);
		
		orderItem = new OrderItem.OrderItemBuilder(orderItem).ingredient(ingredient).build();
    	
		orderItemRepository.save(orderItem);
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
	public void submitOrder(Bill bill) throws StateException {
		bill.submitOrder();
		
	}
	@Override
	public void submitBill(Customer customer) throws StateException, EmptyBillException {
		// TODO Auto-generated method stub
		
	}
	
}
