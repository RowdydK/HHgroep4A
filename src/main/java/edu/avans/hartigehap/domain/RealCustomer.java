package edu.avans.hartigehap.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;

import javax.persistence.*;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;


@Entity
@Getter
@Setter
@Table(name = "CUSTOMERS")
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
@ToString(callSuper = true, includeFieldNames = true, of = { "firstName", "lastName", "bills" })
@NoArgsConstructor
public class RealCustomer extends CopyCustomer {
    private static final long serialVersionUID = 1L;

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

    public RealCustomer(String firstName, String lastName, DateTime birthDate, int partySize, String description,
                        byte[] photo ) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.partySize = partySize;
        this.description = description;
        this.photo = photo.clone();
    }

    @Override
    public String getBirthDateString() {
        String birthDateString = "";
        if (birthDate != null) {
            birthDateString = org.joda.time.format.DateTimeFormat.forPattern("yyyy-MM-dd").print(birthDate);
        }
        return birthDateString;
    }

    @Override
    public void updateEditableFields(RealCustomer customer) {
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

    @Override
    public void becomeNull(CopyCustomer context){
        System.out.println("NullCustomer");
        context.setCustomerState(NullCustomer.getInstance());
    }

    public String getFirstName(){
        return firstName;
    }

    public String getLastName(){
        return lastName;
    }

    public int getPartySize(){
        return partySize;
    }

    public String getDescription(){
        return description;
    }

    public byte[] getPhoto(){
        return photo;
    }
}
