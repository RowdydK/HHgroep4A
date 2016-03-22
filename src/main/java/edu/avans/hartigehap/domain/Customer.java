package edu.avans.hartigehap.domain;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.*;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;


/**
 * 
 * @author Erco
 */
@Entity
@Table(name = "CUSTOMERS")
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
@Getter
@Setter
@ToString(callSuper = true, includeFieldNames = true, of = { "firstName", "lastName", "bills" })
@NoArgsConstructor
public abstract class Customer extends DomainObject {
    private static final long serialVersionUID = 1L;

    @Transient
    public final String error = "Cannot instantiate the selected state";

    @NotEmpty(message = "{validation.firstname.NotEmpty.message}")
    @Size(min = 3, max = 60, message = "{validation.firstname.Size.message}")
    public String firstName;

    @NotEmpty(message = "{validation.lastname.NotEmpty.message}")
    @Size(min = 1, max = 40, message = "{validation.lastname.Size.message}")
    public String lastName;
    
//    @NotEmpty(message = "{validation.streetName.NotEmpty.message}")
//    @Size(min = 1, max = 40, message = "{validation.streetName.Size.message}")
    public String streetName;
    
//    @NotEmpty(message = "{validation.number.NotEmpty.message}")
//    @Size(min = 1, max = 10, message = "{validation.number.Size.message}")
    public String number;
    
//    @NotEmpty(message = "{validation.zipCode.NotEmpty.message}")
//    @Size(min = 1, max = 10, message = "{validation.zipCode.Size.message}")
    public String zipCode;
    
    //@NotEmpty(message = "{validation.cityName.NotEmpty.message}")
    //@Size(min = 1, max = 40, message = "{validation.cityName.Size.message}")
    public String cityName;
    
    // works with hibernate 3.x
    // @Type(type="org.joda.time.contrib.hibernate.PersistentDateTime")
    // to allow using Joda's DateTime with hibernate 4.x use:
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    // needed to allow changing a date in the GUI
    @DateTimeFormat(iso = ISO.DATE)
    public DateTime birthDate;

    public int partySize;

    public String description;

    @Basic(fetch = FetchType.LAZY)
    @Lob
    @Column(name = "PHOTO")
    public byte[] photo;

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
    private Customer customerState;

    public void setCustomerState(Customer state){
    	customerState = state;
    }

    public Customer getCustomerState(){
    	return customerState;
 }
    public enum CustomerStateId {
        NULL, REAL
    }

    @Enumerated(EnumType.ORDINAL)
    private CustomerStateId customerStateId;

    // TODO not complete (bills)
    public Customer(String firstName, String lastName, DateTime birthDate, int partySize, String description,
                    byte[] photo) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.partySize = partySize;
        this.description = description;
        this.photo = photo.clone();


    }

    // This method only updates user-editable fields
    // id, version, restaurants, bills are considered not user-editable
    public void updateEditableFields(Customer customer) {
        firstName = customer.firstName;
        lastName = customer.lastName;
        birthDate = customer.birthDate;
        description = customer.description;
        // hack
        // the "if" is a hack
        // when you change a customer without changing the photo, the customer
        // object passed to the server by editcustomer has the non-changed fields
        // filled in, except for the photo.
        // result is that changing only one field of a customer effectively deletes the photo
        // hack: only update the photo when a new photo is passed
        // downside of this hack: it is not possible any more to delete the photo
        if(customer.photo.length != 0) {
            photo = customer.photo;
        }
        partySize = customer.partySize;
    }

    // example of a "derived property". This property can be be easily derived
    // from the property "birthDate", so no need to persist it.
    @Transient
    public String getBirthDateString() {
        String birthDateString = "";
        if (birthDate != null) {
            birthDateString = org.joda.time.format.DateTimeFormat.forPattern("yyyy-MM-dd").print(birthDate);
        }
        return birthDateString;
    }

    // business logic

    public void becomeNull(Customer context){
        System.out.println(error);
    }

    public void becomeReal(Customer context){
        System.out.println(error);
    }

}
