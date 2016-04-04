package edu.avans.hartigehap.domain;


import java.util.Collection;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
@Getter
@Setter
public abstract class BillState {
    private static final long serialVersionUID = 1L;
    
    public enum BillStatusId {
    	CREATED, SUBMITTED, PAID
    }
    
    public BillState(Bill bill){
    	this.bill = bill;
    }
    
    public BillState(){
    	//empty constructor for states
    }
    
    protected BillStatusId billStatusId;
    
    @OneToOne(cascade = javax.persistence.CascadeType.ALL, mappedBy="billState")
    Bill bill;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Transient
	public final String error = "Cannot instantiate the selected state";
	
	public Date billCreated(Bill context, Collection<Order> orders, Order currentOrder)throws StateException, EmptyBillException{
		System.out.println(error);
		return null;
	}
	
	public Date billSubmitted(Bill context)throws StateException{
		System.out.println(error);
		return null;
		
	}
	
	public void billPaid(Bill context){
		System.out.println(error);
	}


	
	
}
