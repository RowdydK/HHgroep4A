package edu.avans.hartigehap.domain;


public class NullCustomer extends Customer {
    private static final long serialVersionUID = 1L;

    private static NullCustomer nullCustomer = new NullCustomer();

    private NullCustomer(){}

    public void becomeReal(Customer context){
        System.out.println("Real Customer");
        context.setCustomerState(new RealCustomer(firstName, lastName,birthDate, partySize, description, photo));
    }

    public static NullCustomer getInstance(){
        return nullCustomer;
    }

    @Override
    public String getFirstName(){
        return "";
    }

    @Override
    public String getLastName(){
        return "";
    }

    @Override
    public org.joda.time.DateTime getBirthDate(){
        return null;
    }

    @Override
    public int getPartySize(){
        return 0;
    }

    @Override
    public String getDescription(){
        return "";
    }

    @Override
    public byte[] getPhoto(){
        return null;
    }


}
