package core;

import java.util.ArrayList;
import java.util.Scanner;

public class Controller {
    private ArrayList<Customer> customers;

    public Controller(){
        customers = new ArrayList<>();
    }

    public void begin(){
        System.out.println("Welcome to JavaPhone Telecom");

        customerLogin();
    }
    //TODO cache user input, use control to not lose data, only current invalid input
    private void customerLogin(){
        boolean loginStatus = true;

        String afm;
        String id="";
        String address="";
        String jobStatus="";
        String email="";

        Scanner scanner = new Scanner(System.in);
        System.out.println("Please write your AFM: "); //BRO EDW PERA MH GAMHSW #TODO
        afm = scanner.nextLine();
        for (Customer c : customers)
            if (afm.equals(c.getAFM()))
                loginStatus = false;

        if (loginStatus)
            customers.add(new Customer(afm, id, address, jobStatus, email));
    }
}
