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

	@Override
	public Date billSubmitted(Bill context)throws StateException{

		context.setBillState(new BillStatePaid());
		context.setBillStateId(Bill.BillStateId.PAID);

		return new Date();
	}

}
