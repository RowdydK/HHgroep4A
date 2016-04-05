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
public class BillStatePaid extends BillState{
    private static final long serialVersionUID = 1L;

	BillOriginator originator;
	BillCaretaker caretaker;

	@Override
	public void billPaid(Bill context){

		originator.setBill(context);
		caretaker.add(originator.saveBillToMemento());

		System.out.println("Bill has already been paid for");
	}

	public Bill getPreviousBillState(int i){
		originator.getBillFromMemento(caretaker.get(i));

		return originator.getBill();
	}

}
