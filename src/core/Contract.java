package core;

import java.time.LocalDate;
import java.util.*;

public abstract class Contract {
    protected static HashSet<String> staticContractIds = new HashSet<>();

    protected String contractId;
    protected String phoneNumber;
    protected ContractType type;
    protected String customerAFM;
    protected String customerID;

    protected int freeCallMinutes;
    protected LocalDate startDate;
    protected LocalDate endDate;
    protected int contractDuration; //12 or 24 months
    protected double monthlyCost;

    protected boolean electronic;
    protected PaymentType paymentType;

    protected boolean internetConnection;

    protected int discountPercentage;

    public abstract double calculateCost();

    public abstract Contract build(Customer customer);

    public boolean buildAbstract(Customer customer) {
        Scanner scanner = new Scanner(System.in);
        contractId = getUniqueId();

        String tempInput;
        System.out.println("Please enter the phone number you want to associate with this contract: ");
        tempInput = scanner.nextLine();
        if (tempInput.length() != 10) {
            System.err.println("Wrong input, number must be 10 characters long");
            return false;
        }
        if (type == ContractType.HOME && tempInput.charAt(0) != '2') {
            System.err.println("Wrong input, home phone number must start with '2'");
            return false;
        } else if (type == ContractType.MOBILE && tempInput.charAt(0) != '6') {
            System.err.println("Wrong input, mobile phone number must start with '6'");
            return false;
        }


        phoneNumber = tempInput;
        customerAFM = customer.getAFM();
        customerID = customer.getID();

        System.out.println("Enter your desired call time in minutes: ");
        tempInput = scanner.nextLine();
        freeCallMinutes = Integer.parseInt(tempInput);
        if (freeCallMinutes < 0) {
            System.out.println("Wrong input, call time must be at least 0");
            return false;
        }
        System.out.println("Enter your desired start date: (DD MM YYYY)");
        tempInput = scanner.nextLine();
        String[] date = tempInput.split(" ");
        if (date.length != 3) {
            System.out.println("Wrong input, make sure it's in the format DD MMM YYYY with spaces");
            return false;
        }
        Integer[] dateNums = Arrays.stream(tempInput.split(" ")).map(Integer::parseInt).toArray(Integer[]::new);
        //Checks if the values are valid
        if (dateNums[2] < 1950
                || dateNums[1] <= 0 || dateNums[1] > 12
                || dateNums[0] <= 0 || dateNums[0] > 31) {
            System.err.println("Wrong input, some value is wrong");
            return false;
        }
        startDate = LocalDate.of(dateNums[2], dateNums[1], dateNums[0]);
        if (!verifyUniquePhone(phoneNumber, customer)) {
            System.err.println("Error, contracts cannot overlap for the same phone number");
            return false;
        }
        System.out.println("""
                Please enter your desired duration in months:
                1) 12
                2) 24
                """);
        tempInput = scanner.nextLine();
        if (tempInput.equals("1")) {
            endDate = startDate.plusMonths(12);
            contractDuration = 12;
        } else if (tempInput.equals("2")) {
            endDate = startDate.plusMonths(24);
            contractDuration = 12;
        } else {
            System.err.println("Wrong input");
            return false;
        }
        System.out.println("""
                Would you like your account to be electronic?:
                1) Yes
                2) No
                """);
        tempInput = scanner.nextLine();
        electronic = tempInput.equals("1");

        System.out.println("""
                Please enter your preferred method of payment:
                1) Cash
                2) Credit Card
                3) Standing Order
                """);
        tempInput = scanner.nextLine();
        if (tempInput.equals("1"))
            paymentType = PaymentType.CASH;
        else if (tempInput.equals("2"))
            paymentType = PaymentType.CREDITCARD;
        else if (tempInput.equals("3"))
            paymentType = PaymentType.STANDING_ORDER;
        else {
            System.out.println("Wrong input");
        }
        System.out.println("""
                Would you like to have internet connectivity?:
                1) Yes
                2) No               
                """);
        tempInput = scanner.nextLine();
        internetConnection = tempInput.equals("1");
        calculateDiscount(customer);
        return true;
    }

    /*
    Generate a unique id for every contract
    @HashSet guarantees no duplicates
    */
    public static String getUniqueId() {
        String uid;
        do
            uid = UUID.randomUUID().toString();
        while (staticContractIds.contains(uid));

        return uid;
    }

    /*
    @returns true if the contract's start date is not before the end date of any other
        contract on the same phone number
    */
    public boolean verifyUniquePhone(String phoneNumber, Customer customer) {
        return customer.getContracts().stream()
                .noneMatch(con -> con.phoneNumber.equals(phoneNumber)
                        && startDate.isBefore(con.endDate));
    }

    public abstract void printContract();

    public void printCondensed() {
        System.out.println("\nPhone number: " + phoneNumber +
                        "\nContract type: " + type +
                        "\nSTART DATE " + startDate.toString() +
                        "\nEND DATE " + endDate.toString());
    }

    public double getMonthlyCost() {
        return monthlyCost;
    }

    public void calculateDiscount(Customer customer) {
        discountPercentage = 0;
        ArrayList<Contract> activeContracts = customer.getActiveContracts();
        //JOB STATUS
        if (customer.getJobStatus().equals("Private Citizen"))
            discountPercentage += 5 * activeContracts.size();
        else if (customer.getJobStatus().equals("Professional"))
            discountPercentage += 10 * activeContracts.size();
        else
            discountPercentage += 15 * activeContracts.size();

        //Check if any contract above 1000 minutes and depending on type
        if (customer.getContracts().stream().anyMatch(c -> c.freeCallMinutes >= 1000))
            if (type == ContractType.HOME)
                discountPercentage += 8;
            else
                discountPercentage += 11;
        if (paymentType == PaymentType.CREDITCARD || paymentType == PaymentType.STANDING_ORDER)
            discountPercentage += 5;

        //If account is electronic
        if (electronic)
            discountPercentage += 2;

        //Limit the max value of discount at 45
        discountPercentage = Math.min(discountPercentage, 45);
    }
}
