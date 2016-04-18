package edu.avans.hartigehap.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Getter
@Setter


public class CustomerNull extends CustomerCopy {
    private static final long serialVersionUID = 1L;

    private static CustomerNull nullCustomer = new CustomerNull();

    @NotEmpty(message = "{validation.firsstname.NotEmpty.message}")
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
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    public DateTime birthDate;

    public int partySize;

    public String description;

    @Basic(fetch = FetchType.LAZY)
    @Lob
    @Column(name = "PHOTO")
    public byte[] photo;

    public CustomerNull(){}

    @Override
    public String getBirthDateString() {
        return null;
    }

    public void updateEditableFields(CustomerReal customer) {

    }

    public void becomeReal(CustomerCopy context){
        System.out.println("Real Customer");
        context.setCustomerState(new CustomerReal(firstName, lastName,birthDate, partySize, description, photo));
    }

    public static CustomerNull getInstance(){
        return nullCustomer;
    }

    public String getFirstName(){
        return null;
    }

    public String getLastName(){
        return null;
    }

    public int getPartySize(){
        return 0;
    }

    public String getDescription(){
        return null;
    }

    public byte[] getPhoto(){
        return null;
    }

}
