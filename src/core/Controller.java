package core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

public class Controller {
    public ArrayList<Customer> customers;

    public Controller(){
        customers = new ArrayList<>();
    }

    public boolean begin(){
        System.out.println("Welcome to JavaPhone Telecom");
        customerLogin();
        return true;
    }

    private void customerLogin(){
        String afm="";
        String id="";
        String address="";
        String jobStatus="";
        String email="";

        Scanner scanner = new Scanner(System.in);
        while (afm == "") {
            System.out.print("Please write your AFM: ");
            afm = scanner.nextLine();
            for (Customer c : customers) {
                if (afm.equals(c.getAFM())) {
                    afm = "";
                    System.out.println("Error AFM already exist");
                    break;
                }
            }
        }

        while (id == "") {
            System.out.print("Please write your ID: ");
            id = scanner.nextLine();
            for (Customer c : customers) {
                if (id.equals(c.getID())) {
                    id = "";
                    System.out.println("Error ID already exist");
                    break;
                }
            }
        }

        System.out.print("Please write your Address: ");
        address = scanner.nextLine();

        do {
            System.out.print(" Please choose your Job Status among the following choices : \n 1. Private Citizen \n 2. Student \n 3. Professional \n");
            int y = Integer.parseInt(scanner.nextLine());
            switch (y) {
                case 1 -> jobStatus = "Private Citizen";
                case 2 -> jobStatus = "Student";
                case 3 -> jobStatus = "Professional";
                default -> System.out.println("Sorry, please enter valid Option");
            }
        }while (jobStatus == "");


        System.out.print("Please write your Email: ");
        email = scanner.nextLine();
        customers.add(new Customer(afm, id, address, jobStatus, email));
    }

    public boolean verifyListIntegrity(){
        HashSet<String> afms = new HashSet<>();
        HashSet<String> ids = new HashSet<>();
        for (Customer c : customers){
            if( !afms.add(c.getAFM()) || !ids.add(c.getAFM()) )
                return false;
        }
        return true;
    }
}
