package edu.avans.hartigehap.domain;


import org.exolab.castor.types.DateTime;

public class NullCustomer extends Customer {

    private static NullCustomer nullCustomer = new NullCustomer();

    private NullCustomer(){

    }

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

}
