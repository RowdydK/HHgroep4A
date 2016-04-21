package edu.avans.hartigehap.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author Erco
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "Order.findSubmittedOrders", query = "SELECT o FROM Order o "
                + "WHERE o.orderStatus = edu.avans.hartigehap.domain.Order$OrderStatus.SUBMITTED "
                + "AND o.bill.diningTable.restaurant = :restaurant " + "ORDER BY o.submittedTime"),
        @NamedQuery(name = "Order.findPlannedOrders", query = "SELECT o FROM Order o "
                + "WHERE o.orderStatus = edu.avans.hartigehap.domain.Order$OrderStatus.PLANNED "
                + "AND o.bill.diningTable.restaurant = :restaurant " + "ORDER BY o.plannedTime")
})
// to prevent collision with MySql reserved keyword
@Table(name = "ORDERS")
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
@Getter
@Setter
@ToString(callSuper = true, includeFieldNames = true, of = { "orderStatus", "orderItems" })
public class Order extends DomainObject implements Cloneable {
    private static final long serialVersionUID = 1L;

    public enum OrderStatus {
        CREATED, SUBMITTED, PLANNED, PREPARED, SERVED
    }

    @Enumerated(EnumType.ORDINAL)
    // represented in database as integer
    private OrderStatus orderStatus;

    @Temporal(TemporalType.TIMESTAMP)
    private Date submittedTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date plannedTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date preparedTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date servedTime;

    // unidirectional one-to-many relationship.
    @OneToMany(cascade = javax.persistence.CascadeType.ALL)
    private Collection<OrderItem> orderItems = new ArrayList<OrderItem>();

    @ManyToOne()
    private Bill bill;

    public Order() {
        orderStatus = OrderStatus.CREATED;
    }

    /* business logic */

    @Transient
    public boolean isSubmittedOrSuccessiveState() {

        return orderStatus != OrderStatus.CREATED;
    }

    // transient annotation, because methods starting with are recognized by JPA
    // as properties
    @Transient
    public boolean isEmpty() {

        return orderItems.isEmpty();
    }

    public void addOrderItem(MenuItem menuItem) {
    	addOrderItem(menuItem, null);
    }

    public void addOrderItem(MenuItem menuItem, ArrayList<OrderItemIngredient> ingredients){
    	IteratorRepository itRep = new IteratorRepository(new ArrayList<Object>(orderItems));
        boolean found = false;
    	for (IteratorPattern itPat = itRep.getIterator(); itPat.hasNext();){
            OrderItem orderItem = (OrderItem)itPat.next();
            if (orderItem.getMenuItem().equals(menuItem)) {
            	if(ingredients != null){
            		for(OrderItemIngredient i : ingredients){
            			boolean containsIngredient = false;
            			for(OrderItemIngredient oii : orderItem.getIngredients()){
            				if(oii.getIngredient().equals(i.getIngredient())){
            					containsIngredient = true;
            				}
            			}
            			if(!containsIngredient){
            				found = false;
            				break;
            			}
            		}
            	}
                orderItem.incrementQuantity();
                found = true;
                break;
            }
        }
        if (!found) {
        	OrderItem orderItem = new OrderItem.OrderItemBuilder(menuItem, 1).ingredients(ingredients).build();
            orderItems.add(orderItem);
        }
    }
    
    public void deleteOrderItem(MenuItem menuItem) {
    	IteratorRepository itRep = new IteratorRepository(new ArrayList<Object>(orderItems));
    	boolean found = false;
    	for (IteratorPattern itPat = itRep.getIterator(); itPat.hasNext();){
    		OrderItem orderItem = (OrderItem) itPat.next();
            if (orderItem.getMenuItem().equals(menuItem)) {
            	found = true;
            	if (orderItem.getQuantity() > 1){
            		orderItem.decrementQuantity();
            	}else{
            		orderItems.remove(orderItem);
            	}
            	break;
            }
    	}
    }

    public void submit() throws StateException {
        if (isEmpty()) {
            throw new StateException("not allowed to submit an empty order");
        }

        // this can only happen by directly invoking HTTP requests, so not via
        // GUI
        if (orderStatus != OrderStatus.CREATED) {
            throw new StateException("not allowed to submit an already submitted order");
        }
        submittedTime = new Date();
        orderStatus = OrderStatus.SUBMITTED;
    }

    public void plan() throws StateException {

        // this can only happen by directly invoking HTTP requests, so not via
        // GUI
        if (orderStatus != OrderStatus.SUBMITTED) {
            throw new StateException("not allowed to plan an order that is not in the submitted state");
        }

        plannedTime = new Date();
        orderStatus = OrderStatus.PLANNED;
    }

    public void prepared() throws StateException {

        // this can only happen by directly invoking HTTP requests, so not via
        // GUI
        if (orderStatus != OrderStatus.PLANNED) {
            throw new StateException(
                    "not allowed to change order state to prepared, if it is not in the planned state");
        }

        preparedTime = new Date();
        orderStatus = OrderStatus.PREPARED;
    }

    public void served() throws StateException {

        // this can only happen by directly invoking HTTP requests, so not via
        // GUI
        if (orderStatus != OrderStatus.PREPARED) {
            throw new StateException("not allowed to change order state to served, if it is not in the prepared state");
        }

        servedTime = new Date();
        orderStatus = OrderStatus.SERVED;
    }

    @Transient
    public int getPrice() {
        int price = 0;
        Iterator<OrderItem> orderItemIterator = orderItems.iterator();
        while (orderItemIterator.hasNext()) {
            price += orderItemIterator.next().getPrice();
        }
        return price;
    }
    
    @Override
    public Order clone() {
		try {
			Order clone = (Order) super.clone();
			Collection<OrderItem> thisOrderItems = orderItems;
			ArrayList<OrderItem> cloneOrderItems = new ArrayList<>();
			for(OrderItem orderItem : thisOrderItems){
				cloneOrderItems.add(orderItem.clone());
			}
			clone.setOrderItems(cloneOrderItems);
			return clone;
		} catch (CloneNotSupportedException e) {		
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

}
