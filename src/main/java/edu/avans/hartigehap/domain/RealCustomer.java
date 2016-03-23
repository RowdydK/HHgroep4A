package edu.avans.hartigehap.domain;


import org.joda.time.DateTime;

public class RealCustomer extends Customer {
    private static final long serialVersionUID = 1L;

    public RealCustomer(String firstName, String lastName, DateTime birthDate, int partySize, String description,
                        byte[] photo ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.partySize = partySize;
        this.description = description;
        this.photo = photo;
    }
    @Override
    public void becomeNull(Customer context){
        System.out.println("NullCustomer");
        context.setCustomerState(NullCustomer.getInstance());
    }

    @Override
    public String getFirstName(){

        return firstName;
    }

    @Override
    public String getLastName(){

        return lastName;
    }

    @Override
    public DateTime getBirthDate(){

        return birthDate;
    }

    @Override
    public int getPartySize(){

        return partySize;
    }

    @Override
    public String getDescription(){

        return description;
    }

    @Override
    public byte[] getPhoto(){

        return photo;
    }
}
