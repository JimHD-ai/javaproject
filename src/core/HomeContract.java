package core;

import java.util.Scanner;

public class HomeContract extends Contract {
    private String connectionSpeed;

    public HomeContract() {
        super();
    }

    @Override
    public double calculateCost() {
        double cost = 0.45 * freeCallMinutes / 3;
        cost += connectionSpeed.equals("ADSL") ? 18.69 : 31.9; //taken straight from COSMOTE
        if (paymentType == PaymentType.CREDITCARD) //convenience fee
            cost++;
        if (contractDuration == 24) //2% reduction for loyal customers
            cost = 0.98 * cost;
        return cost;
    }

    @Override
    public HomeContract build(Customer customer) {
        type = ContractType.HOME;
        if(!super.buildAbstract(customer))
            return null;
        Scanner scanner = new Scanner(System.in);
        String tempInput;
        if (internetConnection)
            System.out.println("""
                    Please select your preferred internet speed:
                    1) ADSL
                    2) VDSL             
                    """);
        tempInput = scanner.nextLine();
        connectionSpeed = tempInput.equals("1") ? "ADSL" : "VDSL";

        monthlyCost = calculateCost();
        return this;
    }

    @Override
    public void printContract() {
        System.out.printf("""
                Contract ID: %s
                Phone Number: %s
                Customer ID: %s
                Customer AFM : %s
                ------------------
                CONTRACT DETAILS
                Contract type: %s
                Call time (minutes): %d
                Internet Connection Type: %s
                START DATE: %s
                END DATE: %s
                Electronic: %b
                Payment method: %s
                Monthly Cost: %.2f€
                %n""",contractId, phoneNumber, customerID, customerAFM,
                type,freeCallMinutes, internetConnection, startDate.toString(), endDate.toString(),
                electronic, paymentType, monthlyCost);
    }
}
