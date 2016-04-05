package edu.avans.hartigehap.domain;

import java.util.Date;

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
public class BillStateSubmitted extends BillState{

	BillOriginator originator;
	BillCaretaker caretaker;

	@Override
	public Date billSubmitted(Bill context)throws StateException{

		Bill newContext = new Bill();
		newContext = context;

		newContext.setBillState(new BillStatePaid());
		newContext.setBillStateId(Bill.BillStateId.PAID);

		originator.setBill(context);
		caretaker.add(originator.saveBillToMemento());

		return new Date();
	}

	public Bill getPreviousBillState(int i){
		originator.getBillFromMemento(caretaker.get(i));

		return originator.getBill();
	}
}
