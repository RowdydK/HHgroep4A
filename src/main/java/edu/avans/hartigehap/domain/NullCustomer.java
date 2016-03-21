package edu.avans.hartigehap.domain;


import org.exolab.castor.types.DateTime;

public class NullCustomer extends Customer {
    private static final long serialVersionUID = 1L;

    private static NullCustomer nullCustomer = new NullCustomer();

    public NullCustomer(){}

    public static NullCustomer getInstance(){
        return nullCustomer;
    }

    public boolean isNil(){
        return true;
    }

    public String firstName(){
        return "";
    }

    public String lastName(){
        return "";
    }

    public DateTime birthDate(){
        return null;
    }

    public int partySize(){
        return 0;
    }

    public String description(){
        return "";
    }

    public byte[] photo(){
        return null;
    }

    public void becomeReal(Customer context){
        System.out.println("Real Customer");
        context.setCustomerState(new RealCustomer(firstName, lastName,birthDate, partySize, description, photo));
    }
}
