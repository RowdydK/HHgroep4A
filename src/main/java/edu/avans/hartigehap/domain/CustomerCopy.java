package edu.avans.hartigehap.domain;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;


/**
 * 
 * @author Erco
 */
@Entity
@Table(name = "CUSTOMERS2")
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
@Getter
@Setter
@NoArgsConstructor
public abstract class CustomerCopy extends DomainObject {
    private static final long serialVersionUID = 1L;

    @Transient
    public final String error = "Cannot instantiate the selected state";

    // no cascading
    @ManyToMany
    public Collection<Restaurant> restaurants = new ArrayList<Restaurant>();

    @OneToOne(cascade = javax.persistence.CascadeType.ALL)
    public Bill currentBill;

    // no cascading
    // bidirectional one-to-many; mapping on the database happens at the many
    // side
    @OneToMany(mappedBy = "customer")
    public Collection<Bill> bills = new ArrayList<Bill>();

    @OneToOne(cascade = javax.persistence.CascadeType.ALL)
    private CustomerCopy customerState;

    public void setCustomerState(CustomerCopy state){
    	customerState = state;
    }

    public CustomerCopy getCustomerState(){
    	return customerState;
 }
    public enum CustomerStateId {
        NULL, REAL
    }

    @Enumerated(EnumType.ORDINAL)
    private CustomerStateId customerStateId;

    // example of a "derived property". This property can be be easily derived
    // from the property "birthDate", so no need to persist it.
    @Transient
    public  abstract String getBirthDateString();

    // This method only updates user-editable fields
    // id, version, restaurants, bills are considered not user-editable
    public abstract void updateEditableFields(CustomerReal customer);

    // business logic

    public void becomeNull(CustomerCopy context){
        System.out.println(error);
    }

    public void becomeReal(CustomerCopy context){
        System.out.println(error);
    }

}
