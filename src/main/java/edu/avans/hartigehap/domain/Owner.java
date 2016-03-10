package edu.avans.hartigehap.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Setter;
import lombok.ToString;
import lombok.Getter;
import org.hibernate.annotations.Columns;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "OWNERS")
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
@Getter
@Setter
@ToString(callSuper=true, includeFieldNames=true, of= {"name"})

public class Owner extends DomainObject {
	private static final long serialVersionUID = 1L;
	
    @Column(name = "name")
    private String name;

    @ManyToMany (cascade=javax.persistence.CascadeType.ALL)
    private List<Restaurant> restaurants;


}
