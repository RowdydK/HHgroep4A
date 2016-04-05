package edu.avans.hartigehap.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

/**
 * Created by Student on 02-04-16.
 */

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")

public class BillOriginator {
    private Bill bill;

    public void setBill(Bill bill){
        this.bill = bill;
    }

    public Bill getBill(){
        return bill;
    }

    public BillMemento saveBillToMemento(){
        return new BillMemento(bill);
    }

    public void getBillFromMemento(BillMemento memento){

        bill = memento.getBill();
    }
}
