package core;

import java.util.Scanner;

public class MobileContract extends Contract {
    private double freeData;
    private int freeSMS;

    public MobileContract() {
        super();
    }

    @Override
    public double calculateCost() {
        double cost = 0.54 * freeCallMinutes / 3;
        cost += 0.23 * freeSMS / 2;

        return (cost/100) * 100-discount;
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
                electronic, paymentType, monthlyCost, discount);
    }
}
