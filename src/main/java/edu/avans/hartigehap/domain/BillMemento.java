package edu.avans.hartigehap.domain;

import java.util.ArrayList;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;

import edu.avans.hartigehap.domain.Bill.BillStateId;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class BillMemento extends DomainObject {
    private BillCaretaker caretaker;

	//Make sure the settings of the max_allowed_packet size in the my.ini file of your mySQL config in XAMP/MAMP/AMPPS
	// must be set higher for this to be able to run succesfully. This file can be found in your XAMP/MAMP/AMPPS installation folder.

	@Column(columnDefinition="blob")
    private BillState billState;
    
    
    @Enumerated(EnumType.ORDINAL)
    private BillStateId billStateId;
    
    private Date submittedTime;
    private Date paidTime;
	@Column(columnDefinition="longblob")
    private Order currentOrder;
	@Column(columnDefinition="longblob")
    private ArrayList<Order> orders = new ArrayList<Order>();
	@Column(columnDefinition="blob")
    private DiningTable diningTable;
	@Column(columnDefinition="blob")
    private DiscountStrategy strategy;
	
	public BillMemento(Bill stateToSave){
		Bill bill = stateToSave.clone();
		this.billState = bill.getBillState();
    	this.billStateId = bill.getBillStateId();
    	this.currentOrder = bill.getCurrentOrder();
    	this.diningTable = bill.getDiningTable();
    	this.orders.addAll(bill.getOrders());
    	this.paidTime = bill.getPaidTime();
    	this.strategy = bill.getStrategy();
    	this.submittedTime = bill.getSubmittedTime();
    	this.version = bill.getVersion();
	}
}
