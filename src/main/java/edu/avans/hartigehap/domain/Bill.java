package edu.avans.hartigehap.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * 
 * @author Erco
 */
@Entity
// optional
@Table(name = "BILLS")
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
@Getter
@Setter
@ToString(callSuper = true, includeFieldNames = true, of = { "billStateId", "currentOrder", "orders" })
public class Bill extends DomainObject implements Cloneable {
    private static final long serialVersionUID = 1L;

    @OneToOne(cascade = javax.persistence.CascadeType.ALL)
    private BillCaretaker caretaker;
    
    public enum BillStateId {
        CREATED, SUBMITTED, PAID
    }
    
    @OneToOne(cascade = javax.persistence.CascadeType.ALL)
    private BillState billState;
    
//    public void setBillState(BillState state){
//    	billState = state;
//    }
//    
//    public BillState getBillState(){
//    	return billState;
//    }
    
    // represented in database as integer
    @Enumerated(EnumType.ORDINAL)
    private BillStateId billStateId;

    @Temporal(TemporalType.TIMESTAMP)
    private Date submittedTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date paidTime;

    // unidirectional one-to-one relationship
    @OneToOne(cascade = javax.persistence.CascadeType.ALL) 
    private Order currentOrder;

    @OneToMany(cascade = javax.persistence.CascadeType.ALL, mappedBy = "bill")
    private Collection<Order> orders = new ArrayList<Order>();

    // bidirectional one-to-many relationship
    @ManyToOne()
    private DiningTable diningTable;

    // bidirectional one-to-many relationship
    @ManyToOne(cascade = javax.persistence.CascadeType.ALL)
    private Customer customer;

    private DiscountStrategy strategy;

    //New BillState
    public Bill(){
    	billState = new BillStateCreated();
    	billStateId = billStateId.CREATED;
    	currentOrder = new Order();
    	currentOrder.setBill(this);
    	orders.add(currentOrder);
    	strategy = DiscountSingleton.getInstance().getDiscountStrategy();
    	caretaker = new BillCaretaker();
    }
    

//    public Bill() {
//        billStatus = BillStatus.CREATED;
//        currentOrder = new Order();
//        currentOrder.setBill(this);
//        orders.add(currentOrder);
//    }

    /* business logic */

    @Transient
    public Collection<Order> getSubmittedOrders() {
        Collection<Order> submittedOrders = new ArrayList<Order>();
        Iterator<Order> orderIterator = orders.iterator();
        while (orderIterator.hasNext()) {
            Order tmp = orderIterator.next();
            if (tmp.isSubmittedOrSuccessiveState()) {
                submittedOrders.add(tmp);
            }
        }
        return submittedOrders;
    }
    
    public Collection<Order> getAllOrders() {
        Collection<Order> submittedOrders = new ArrayList<Order>();
        Iterator<Order> orderIterator = orders.iterator();
        while (orderIterator.hasNext()) {
            Order tmp = orderIterator.next();
                submittedOrders.add(tmp);
        }
        return submittedOrders;
    }


    /**
     * price of *all* orders, so submitted orders and current (not yet
     * submitted) order
     * 
     * @return
     */
    @Transient
    public int getPriceAllOrders() {
        int price = 0;
        Iterator<Order> orderIterator = orders.iterator();
        while (orderIterator.hasNext()) {
            price += orderIterator.next().getPrice();
        }
        return price;
    }

    /**
     * price of the *submitted or successive state* orders only
     * 
     * @return
     */
    @Transient
    //public int getPriceSubmittedOrSuccessiveStateOrders(Bill bill, int discountValue) {
    public int getPriceSubmittedOrSuccessiveStateOrders() {
        int price = 0;
        Iterator<Order> orderIterator = orders.iterator();
        while (orderIterator.hasNext()) {
            Order tmp = orderIterator.next();
            if (tmp.isSubmittedOrSuccessiveState()) {
                price += tmp.getPrice();
            }
        }

        //DiscountStrategy disc = new DiscountStrategy();
        //price = disc.getDiscountPrice(bill, discountValue);

        return price;
    }

    public int getDiscPrice() {
        int price = 0;
        Iterator<Order> orderIterator = orders.iterator();
        while (orderIterator.hasNext()) {
            Order tmp = orderIterator.next();
            if (tmp.isSubmittedOrSuccessiveState()) {
                price += tmp.getPrice();
            }
        }

        try{
        	price = strategy.CalculateDiscount(this);
        }catch(Exception e){
        	System.out.println(e);
        }

        return price;
    }
    
    public void submitOrder() throws StateException {
        currentOrder.submit();
        currentOrder = new Order();
        currentOrder.setBill(this);
        orders.add(currentOrder);
    }
    
    public void submitOnlineOrder() throws StateException {  	
        currentOrder.submit();
    }

    /*
     * as the table gets a new bill, there is no risk that a customer keeps
     * ordering on the submitted or paid bill
     */
    
    public void submit() throws StateException, EmptyBillException{
    	caretaker.addSavedState(saveToMemento());
    	submittedTime = billState.billCreated(this, orders, currentOrder);
    }
    
    @Override
    public Bill clone() {
		try {
			Bill clone = (Bill) super.clone();
			// 	clone.setBillState(this.billState.clone());
			clone.setCurrentOrder(this.currentOrder.clone());
			Collection<Order> thisOrders = orders;
			ArrayList<Order> cloneOrders = new ArrayList<>();
			for(Order order : thisOrders){
				Order orderClone = order.clone();
				cloneOrders.add(orderClone);
				if(order.equals(currentOrder)){
					clone.setCurrentOrder(orderClone);
				}
			}
			clone.setOrders(cloneOrders);
			return clone;
		} catch (CloneNotSupportedException e) {		
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
    
    public BillMemento saveToMemento(){
    	return new BillMemento(this);
    }
    
    public void restoreFromMemento(BillMemento bill){
    	//this.billState = bill.getBillState();
    	this.billStateId = bill.getBillStateId();
    	this.currentOrder = bill.getCurrentOrder();
    	this.diningTable = bill.getDiningTable();
    	this.orders = bill.getOrders();
    	this.paidTime = bill.getPaidTime();
    	this.strategy = bill.getStrategy();
    	this.submittedTime = bill.getSubmittedTime();
    	this.version = bill.getVersion();
    }

    
    
    public void paid() throws StateException {
    	caretaker.addSavedState(saveToMemento());
    	paidTime = billState.billSubmitted(this);
    }



}
