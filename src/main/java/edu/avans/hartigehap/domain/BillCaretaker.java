package edu.avans.hartigehap.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import java.util.List;
import java.util.ArrayList;
/**
 * Created by Student on 02-04-16.
 */
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
@Getter
@Setter
public class BillCaretaker {

    private List<BillMemento> mementoList = new ArrayList<>();

    public void add(BillMemento bill){
        mementoList.add(bill);
    }

    public BillMemento get(int index) {
        return mementoList.get(index);
    }


}
