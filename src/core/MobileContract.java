package core;

import java.time.LocalDate;
import java.util.Scanner;

public class MobileContract extends Contract {
    private double freeData;
    private int freeSMS;

    public MobileContract() {
        super();
    }
    public MobileContract(String contractId, String phoneNUmber, ContractType type, String customerAFM
            , String customerID, int freeCallMinutes, LocalDate startDate, LocalDate endDate, int contractDuration
            , boolean electronic, PaymentType paymentType, double freeData, int freeSMS, Customer customer){
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
        this.freeData = freeData;
        this.freeSMS = freeSMS;
    }

    @Override
    public double calculateCost() {
        double cost = 0.54 * freeCallMinutes / 3;
        cost += 0.23 * freeSMS / 2;

        return (cost/100) * 100- discountPercentage;
    }

    @Override
    public MobileContract build(Customer customer) {
        type = ContractType.MOBILE;
        if (!super.buildAbstract(customer))
            return null;
        Scanner scanner = new Scanner(System.in);
        String tempInput;
        if (internetConnection) {
            System.out.println("Please enter your desired amount of data in GB: ");
            tempInput = scanner.nextLine();
            freeData = Double.parseDouble(tempInput);
        } else
            freeData = 0;
        if (freeData < 0) {
            System.out.println("Wrong input, must be > 0");
            return null;
        }
        System.out.println("Please enter your desired amount of SMS: ");
        tempInput = scanner.nextLine();
        freeSMS = Integer.parseInt(tempInput);
        if (freeSMS < 0) {
            System.out.println("Wrong input, must be > 0");
            return null;
        }

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
                        Call minutes: %d
                        Internet Data: %.1f
                        SMS: %d
                        START DATE: %s
                        END DATE: %s
                        Electronic: %b
                        Payment method: %s
                        Monthly Cost: %.2f€
                        Discount: %d%%
                        %n""", contractId, phoneNumber, customerID, customerAFM,
                type, freeCallMinutes, freeData, freeSMS, startDate.toString(), endDate.toString(),
                electronic, paymentType, monthlyCost, discountPercentage);
    }
}
