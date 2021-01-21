package core;

import java.time.LocalDate;
import java.util.Scanner;

public class HomeContract extends Contract {
    private String connectionSpeed;

    public HomeContract() {
        super();
    }

    public HomeContract(String contractId, String phoneNUmber, ContractType type, String customerAFM
    , String customerID, int freeCallMinutes, LocalDate startDate, LocalDate endDate, int contractDuration
    , boolean electronic, PaymentType paymentType,boolean internetConnection, String connectionSpeed, Customer customer){
        this.contractId = contractId;
        this.phoneNumber = phoneNUmber;
        this.type = type;
        this.customerAFM = customerAFM;
        this.customerID = customerID;
        this.freeCallMinutes = freeCallMinutes;
        this.startDate = startDate;
        this.endDate = endDate;
        this.contractDuration = contractDuration;
        this.electronic = electronic;
        this.paymentType = paymentType;
        this.connectionSpeed = connectionSpeed;
        this.internetConnection = internetConnection;
        calculateDiscount(customer);
        calculateCost();
    }

    @Override
    public double calculateCost() {
        double cost = 0.45 * freeCallMinutes / 3;
        cost += connectionSpeed.equals("ADSL") ? 18.69 : 31.9; //taken straight from COSMOTE

        return (cost/100) * 100- discountPercentage;
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
                Discount: %d%%
                %n""",contractId, phoneNumber, customerID, customerAFM,
                type,freeCallMinutes, internetConnection, startDate.toString(), endDate.toString(),
                electronic, paymentType, monthlyCost, discountPercentage);
    }
}
