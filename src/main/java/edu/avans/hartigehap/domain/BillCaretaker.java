package edu.avans.hartigehap.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class BillCaretaker extends DomainObject {
	@OneToMany(cascade = javax.persistence.CascadeType.ALL)
	List<BillMemento> savedStates = new ArrayList<>();
	
	public void addSavedState(BillMemento savedState){
		savedStates.add(savedState);
	}
}
