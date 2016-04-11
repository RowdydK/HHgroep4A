package edu.avans.hartigehap.domain;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
@Getter
@Setter
public class BillStateCreated extends BillState{
    private static final long serialVersionUID = 1L;

	@Override
	public Date billCreated(Bill context, Collection<Order> orders, Order currentOrder)throws StateException, EmptyBillException{
		boolean allEmpty = true;
		Iterator<Order> orderIterator = orders.iterator();
		while (orderIterator.hasNext()){
			Order order = orderIterator.next();
			if (!order.isEmpty()) {
				allEmpty = false;
				break;
			}
		}
		if (allEmpty){
			throw new EmptyBillException("not allowed to submit an empty bill");
        }
		
		if (!currentOrder.isEmpty()){
			throw new StateException("not allowed to submit an with currentOrder in created state");
        }
		context.setBillState(new BillStateSubmitted());
		context.setBillStateId(Bill.BillStateId.SUBMITTED);

		System.out.println("billCreated");
		return new Date();
	}


	
}
