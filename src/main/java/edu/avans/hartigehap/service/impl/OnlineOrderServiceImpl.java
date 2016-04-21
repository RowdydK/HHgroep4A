package edu.avans.hartigehap.service.impl;

import edu.avans.hartigehap.domain.*;
import edu.avans.hartigehap.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.avans.hartigehap.service.OnlineOrderService;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

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
	public void submitOrder(Order order) throws StateException {
		order.submit();
		
	}
	@Override
	public void submitBill(Bill bill) throws StateException, EmptyBillException {
		bill.submitOnlineOrder();
	}

	@Override
	@Transactional(readOnly = true)
	public List<Order> findPlannedOrders(Restaurant restaurant) {
		List<Order> allPlannedOrdersOnline = new ArrayList<>();
		List<Order> allPlannedOrders = orderRepository.findPlannedOrdersForRestaurant(restaurant);
		ListIterator<Order> it = allPlannedOrders.listIterator();
				while(it.hasNext()){
					Order order = it.next();
					if (order.getBill().getDiningTable() == null){
						allPlannedOrdersOnline.add(order);
					}

				}
		return allPlannedOrdersOnline;
	}

}
