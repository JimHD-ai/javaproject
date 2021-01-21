package core;

import java.time.LocalDate;
import java.util.ArrayList;

import java.util.Scanner;

public class Controller {
    public ArrayList<Customer> customers;

    private Customer currentCustomer;

    public Controller() {
        customers = new ArrayList<>();
    }

    /*
    This method will create customers and contracts so the program can be tested
    */
    public void initializeForTesting() {
        System.out.println("Starting system initialization");
        customers.add(new Customer("010101", "AI141516", "Egnatia 115", "Professional", "itookapillinibiza@gmail.com"));
        customers.add(new Customer("020202", "AK120519", "Adrianoupoleos 14", "Student", "aviciithoughtIwasCool@hotmail.com"));
        customers.add(new Customer("030303", "AI452030", "Skepastou 5", "Private Citizen", "finallygotolder@gmail.com"));
        customers.add(new Customer("040404", "AI214175", "Papazoli 7", "Private Citizen", "waytooold@yahoo.gr"));

        Contract contract;

        //CUSTOMER 1
        currentCustomer = customers.get(0);
        contract = new HomeContract(Contract.getUniqueId(), "2331032543", ContractType.HOME, currentCustomer.getAFM()
                , currentCustomer.getID(), 150, LocalDate.now(), LocalDate.now().plusMonths(12),
                12, true, PaymentType.CREDITCARD, true, "VDSL", currentCustomer);
        currentCustomer.addContract(contract);
        contract = new MobileContract(Contract.getUniqueId(), "6989371422", ContractType.MOBILE, currentCustomer.getAFM(),
                currentCustomer.getID(), 1500, LocalDate.now(), LocalDate.now().plusMonths(24), 24, true,
                true, PaymentType.CREDITCARD, 5, 100, currentCustomer);
        currentCustomer.addContract(contract);

        //CUSTOMER 2
        currentCustomer = customers.get(1);
        contract = new MobileContract(Contract.getUniqueId(), "6989371422", ContractType.MOBILE, currentCustomer.getAFM(),
                currentCustomer.getID(), 400, LocalDate.now().plusDays(5), LocalDate.now().plusMonths(24), 24, false,
                true, PaymentType.CREDITCARD, 5, 100, currentCustomer);
        currentCustomer.addContract(contract);

        //CUSTOMER 3
        currentCustomer = customers.get(2);
        contract = new HomeContract(Contract.getUniqueId(), "2102045350", ContractType.HOME, currentCustomer.getAFM()
                , currentCustomer.getID(), 20000, LocalDate.now(), LocalDate.now().plusMonths(12),
                12, false, PaymentType.CREDITCARD, false, null, currentCustomer);
        currentCustomer.addContract(contract);
        contract = new HomeContract(Contract.getUniqueId(), "2102045350", ContractType.HOME, currentCustomer.getAFM()
                , currentCustomer.getID(), 150, LocalDate.now().plusMonths(24), LocalDate.now().plusMonths(24).plusMonths(24),
                24, false, PaymentType.CREDITCARD, false, "None", currentCustomer);
        currentCustomer.addContract(contract);

        //CUSTOMER 4
        currentCustomer = customers.get(3);
        contract = new MobileContract(Contract.getUniqueId(), "6972152830", ContractType.MOBILE, currentCustomer.getAFM(),
                currentCustomer.getID(), 1000, LocalDate.now(), LocalDate.now().plusMonths(24), 24,
                true, true, PaymentType.STANDING_ORDER, 10, 0, currentCustomer);
        currentCustomer.addContract(contract);
        contract = new MobileContract(Contract.getUniqueId(), "6931405060", ContractType.MOBILE, currentCustomer.getAFM(),
                currentCustomer.getID(), 50, LocalDate.now(), LocalDate.now().plusMonths(12), 12,
                false, false, PaymentType.STANDING_ORDER, 0, 220, currentCustomer);
        currentCustomer.addContract(contract);
    }

    public void begin() {
        initializeForTesting();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to JavaPhone Telecom");

        int choice = -1;
        do {
            System.out.println("""
                    Please enter the number corresponding to the actions below:
                    1) Customer Log In
                    2) New Contract
                    3) Delete Contract
                    4) Show Active Contracts & Statistics
                    5) exit
                    """);
            choice = Integer.parseInt(scanner.nextLine());
            if (choice == 1)
                customerLogin();
            else if (choice == 2)
                newContract();
            else if (choice == 3)
                deleteContract();
            else if (choice == 4)
                showStatistics();

            System.out.println("------------------------------------------------------------");
        }
        while (choice != 5);

        System.out.println("Thank you for using our system");
    }

    private void customerLogin() {
        String afm = "";
        String id = "";
        String address = "";
        String jobStatus = "";
        String email = "";

        Scanner scanner = new Scanner(System.in);
        while (afm.equals("")) {
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
        } while (jobStatus == "");


        System.out.print("Please write your Email: ");
        email = scanner.nextLine();
        if (currentCustomer != null)
            System.out.println("New customer account created, context switched to new customer with afm: " + afm);
        currentCustomer = new Customer(afm, id, address, jobStatus, email);
        customers.add(currentCustomer);
    }

    /*
    @types Home, Mobile
    @constraint No 2 contracts can overlap in date if they are on the same phone number
    @constraint Home phone numbers can only have home contracts and vice versa for mobile (check 2 or 6 for number)
    */
    private void newContract() {
        if (currentCustomer == null) {
            System.err.println("No customer is logged in, exiting action");
            return;
        }
        Scanner scanner = new Scanner(System.in);
        System.out.println("""
                Please enter the number corresponding to contract type you'd like
                1) Home Contract
                2) Mobile Contract
                """);
        int choice = Integer.parseInt(scanner.nextLine());
        Contract contract;
        if (choice == 1)
            contract = new HomeContract();
        else if (choice == 2)
            contract = new MobileContract();
        else {
            System.out.println("Invalid input");
            return;
        }
        contract = contract.build(currentCustomer);

        if (contract == null)
            System.err.println("Action failed, please retry");
        else {
            System.out.println("Contract created successfully, adding to account");
            currentCustomer.addContract(contract);
            contract.calculateDiscount(currentCustomer);
            contract.printContract();
        }
    }

    /*
    @use Deletes a contract
    @constraint a customer can only delete his own contracts
    In order to fully delete a contract, its unique id must be removed
    from the idSet and the customer's contracts arraylist

    The contract to be deleted is selected from the user
    */
    private void deleteContract() {
        if (currentCustomer == null) {
            System.err.println("No customer is logged in, exiting action");
            return;
        }
        if (currentCustomer.getContracts().size() == 0) {
            System.err.println("No contracts associated with this account");
            return;
        }
        int i = 1;
        System.out.println("Contracts associated with this account:");
        for (Contract c : currentCustomer.getContracts()) {
            System.out.print(i + ")");
            c.printCondensed();
            i++;
            System.out.println();
        }

        System.out.print("Select the contract you want to delete: ");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        System.out.println();
        int index = Integer.parseInt(input) - 1;

        /*
        Gets the contract from the user's input, removes its id from the staticSet and the customer's arraylist
        */
        if (index < currentCustomer.getContracts().size() && index >= 0) {
            Contract contractToBeDeleted = currentCustomer.getContracts().get(index);
            Contract.staticContractIds.remove(contractToBeDeleted.contractId);
            currentCustomer.getContracts().remove(contractToBeDeleted);
            System.out.println("Contract deleted successfully");
        } else
            System.err.println("Wrong input, index out of bounds");
    }

    private void showStatistics() {
        if (customers.size() == 0) {
            System.err.println("No customer data available");
            return;
        }
        double minCall, maxCall, meanCall;
        int contracts;

        minCall = Integer.MAX_VALUE;
        maxCall = meanCall = contracts = 0;
        //HOME
        for (Customer c : customers)
            for (Contract con : c.getContracts())
                if (con.type == ContractType.HOME) {
                    minCall = Math.min(minCall, con.freeCallMinutes);
                    maxCall = Math.max(maxCall, con.freeCallMinutes);
                    meanCall += con.freeCallMinutes;
                    contracts++;
                }
        meanCall = contracts > 0 ? meanCall / contracts : 0;
        minCall = minCall == Integer.MAX_VALUE ? 0 : minCall;
        System.out.println("------------------------------------");
        System.out.println("HOME CONTRACTS\n\t MIN CALL\t MAX CALL\t MEAN CALL");
        System.out.printf("\t\t %.2f \t\t %.2f \t\t %.2f\n", minCall, maxCall, meanCall);

        minCall = Integer.MAX_VALUE;
        maxCall = meanCall = contracts = 0;
        for (Customer c : customers)
            for (Contract con : c.getContracts())
                if (con.type == ContractType.MOBILE) {
                    minCall = Math.min(minCall, con.freeCallMinutes);
                    maxCall = Math.max(maxCall, con.freeCallMinutes);
                    meanCall += con.freeCallMinutes;
                    contracts++;
                }
        meanCall = contracts > 0 ? meanCall / contracts : 0;
        minCall = minCall == Integer.MAX_VALUE ? 0 : minCall;

        System.out.println("MOBILE CONTRACTS\n\t MIN CALL\t MAX CALL\t MEAN CALL");
        System.out.printf("\t\t %.2f \t\t %.2f \t\t %.2f\n", minCall, maxCall, meanCall);

        System.out.println("Please enter your AFM to see your active contracts:");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        Customer tempCustomer;
        if (customers.stream().anyMatch(cust -> cust.getAFM().equals(input)))
            tempCustomer = customers.stream()
                    .filter(cust -> cust.getAFM().equals(input)).findFirst().get();
        else {
            System.err.println("No such AFM found in customer database");
            return;
        }

        ArrayList<Contract> activeContracts = tempCustomer.getActiveContracts();

        if (activeContracts.size() == 0) {
            System.err.println("No active contracts found");
            return;
        }
        //Show details for every active contract
        activeContracts.forEach(Contract::printContract);
    }
}
