package edu.avans.hartigehap.domain;


import org.joda.time.DateTime;

public class RealCustomer extends Customer {

    public RealCustomer(String firstName, String lastName, DateTime birthDate, int partySize, String description,
                        byte[] photo ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.partySize = partySize;
        this.description = description;
        this.photo = photo;
    }

    public boolean isNil(){
        return false;
    }
    public String firstName(){

        return firstName;
    }

    public String lastName(){

        return lastName;
    }

    public DateTime birthDate(){

        return birthDate;
    }

    public int partySize(){

        return partySize;
    }

    public String description(){

        return description;
    }

    public byte[] photo(){

        return photo;
    }
}
