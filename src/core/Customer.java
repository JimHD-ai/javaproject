package core;

import java.util.ArrayList;

public class Customer {
    private String afm;
    private String id;
    private String address;
    private String jobStatus;
    private String email;

    private ArrayList<Contract> contracts;

    public Customer(){

    }

    public Customer(String afm, String id, String address, String jobStatus, String email) {
        this.afm = afm;
        this.id = id;
        this.address = address;
        this.jobStatus = jobStatus;
        this.email = email;
    }

    public String getAFM(){ return afm ; }
    public String getID() { return id; }
    public String getAddress(){return address;}
    public String getJobStatus(){return jobStatus;}
    public String getEmail(){return email;}
}
