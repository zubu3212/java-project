import java.util.Random;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    static Random rand = new Random();

    public static String generateRandomString(int length) {
        String charset = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder randomString = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = rand.nextInt(charset.length());
            randomString.append(charset.charAt(index));
        }
        return randomString.toString();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        double balance = 1000.00; // Starting balance
        int savedPin = 1234; // Sample PIN
        double sendMoneyFee = 5.0;
        double nonCkashFee = 10.0;
        double cashOutFee = 10.0;

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dateStr = dateFormat.format(new Date());

        while (true) {
            System.out.println("\n=== ckash ===");
            System.out.println("1. Send Money");
            System.out.println("2. Send Money to Non-cKash User");
            System.out.println("3. Mobile Recharge");
            System.out.println("4. Payment");
            System.out.println("5. Cash Out");
            System.out.println("6. Pay Bill");
            System.out.println("7. Add Money");
            System.out.println("8. Download cKash App");
            System.out.println("9. My cKash");
            System.out.println("10. Reset PIN");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");

            int input3;
            try {
                input3 = scanner.nextInt();
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Clear invalid input
                continue;
            }

            switch (input3) {
                case 0:
                    System.out.println("Thank you for using cKash!");
                    System.exit(0);
                    break;

                case 1: // Send Money
                    sendMoney(scanner, balance, sendMoneyFee, savedPin, dateStr);
                    break;

                case 2: // Send Money to Non-cKash User
                    System.out.print("Enter Receiver Phone Number: ");
                    String nonCkashNumber = scanner.next();
                    System.out.print("Enter Amount: ");
                    double nonCkashAmount = scanner.nextDouble();
                    System.out.print("Enter Menu PIN to confirm: ");
                    int nonCkashPin = scanner.nextInt();

                    if (nonCkashPin == savedPin) {
                        if ((nonCkashAmount + nonCkashFee) <= balance) {
                            balance -= (nonCkashAmount + nonCkashFee);
                            System.out.printf("Send Money Tk %.2f to Non-cKash User (%s) Successful. Fee Tk %.2f. Balance Tk %.2f.%n",
                                    nonCkashAmount, nonCkashNumber, nonCkashFee, balance);

                            // Writing transaction to file
                            try (PrintWriter file = new PrintWriter(new FileWriter("statements.txt", true))) {
                                file.printf("%s - Send Money to Non-cKash: Tk %.2f to %s. Fee: Tk %.2f. Balance: Tk %.2f.%n",
                                        dateStr, nonCkashAmount, nonCkashNumber, nonCkashFee, balance);
                            } catch (IOException e) {
                                System.out.println("Error writing to file: " + e.getMessage());
                            }
                        } else {
                            System.out.println("Insufficient balance.");
                        }
                    } else {
                        System.out.println("Incorrect PIN.");
                    }
                    break;

                case 3: // Mobile Recharge
                    System.out.print("Enter Mobile Number: ");
                    String rechargeNumber = scanner.next();
                    System.out.print("Enter Recharge Amount: ");
                    double rechargeAmount = scanner.nextDouble();
                    System.out.print("Enter Menu PIN to confirm: ");
                    int rechargePin = scanner.nextInt();

                    if (rechargePin == savedPin) {
                        if (rechargeAmount <= balance) {
                            balance -= rechargeAmount;
                            System.out.printf("Mobile Recharge Tk %.2f to %s Successful. Balance Tk %.2f.%n",
                                    rechargeAmount, rechargeNumber, balance);

                            // Writing transaction to file
                            try (PrintWriter file = new PrintWriter(new FileWriter("statements.txt", true))) {
                                file.printf("%s - Mobile Recharge: Tk %.2f to %s. Balance: Tk %.2f.%n",
                                        dateStr, rechargeAmount, rechargeNumber, balance);
                            } catch (IOException e) {
                                System.out.println("Error writing to file: " + e.getMessage());
                            }
                        } else {
                            System.out.println("Insufficient balance.");
                        }
                    } else {
                        System.out.println("Incorrect PIN.");
                    }
                    break;

                case 4: // Payment
                    System.out.print("Enter Merchant ID: ");
                    String merchantId = scanner.next();
                    System.out.print("Enter Payment Amount: ");
                    double paymentAmount = scanner.nextDouble();
                    System.out.print("Enter Menu PIN to confirm: ");
                    int paymentPin = scanner.nextInt();

                    if (paymentPin == savedPin) {
                        if (paymentAmount <= balance) {
                            balance -= paymentAmount;
                            System.out.printf("Payment Tk %.2f to Merchant (%s) Successful. Balance Tk %.2f.%n",
                                    paymentAmount, merchantId, balance);

                            // Writing transaction to file
                            try (PrintWriter file = new PrintWriter(new FileWriter("statements.txt", true))) {
                                file.printf("%s - Payment: Tk %.2f to Merchant %s. Balance: Tk %.2f.%n",
                                        dateStr, paymentAmount, merchantId, balance);
                            } catch (IOException e) {
                                System.out.println("Error writing to file: " + e.getMessage());
                            }
                        } else {
                            System.out.println("Insufficient balance.");
                        }
                    } else {
                        System.out.println("Incorrect PIN.");
                    }
                    break;

                case 5: // Cash Out
                    System.out.print("Enter Agent Number: ");
                    int agentNumber = scanner.nextInt();
                    System.out.print("Enter Amount: ");
                    double cashOutAmount = scanner.nextDouble();
                    System.out.print("Enter Menu PIN to confirm: ");
                    int cashOutPin = scanner.nextInt();

                    if (cashOutPin == savedPin) {
                        if ((cashOutAmount + cashOutFee) <= balance) {
                            balance -= (cashOutAmount + cashOutFee);
                            System.out.printf("Cash Out Tk %.2f to Agent (%d) Successful. Fee Tk %.2f. Balance Tk %.2f.%n",
                                    cashOutAmount, agentNumber, cashOutFee, balance);

                            // Writing transaction to file
                            try (PrintWriter file = new PrintWriter(new FileWriter("statements.txt", true))) {
                                file.printf("%s - Cash Out: Tk %.2f from Agent %d. Fee: Tk %.2f. Balance: Tk %.2f.%n",
                                        dateStr, cashOutAmount, agentNumber, cashOutFee, balance);
                            } catch (IOException e) {
                                System.out.println("Error writing to file: " + e.getMessage());
                            }
                        } else {
                            System.out.println("Insufficient balance.");
                        }
                    } else {
                        System.out.println("Incorrect PIN.");
                    }
                    break;

                case 9: // My cKash
                    System.out.printf("Your current balance is Tk %.2f.%n", balance);
                    System.out.println("Recent Transactions:");
                    try {
                        List<String> transactions = Files.readAllLines(Paths.get("statements.txt"));
                        transactions.stream().skip(Math.max(0, transactions.size() - 5)).forEach(System.out::println);
                    } catch (IOException e) {
                        System.out.println("No recent transactions found.");
                    }
                    break;

                case 10: // Reset PIN
                    System.out.print("Enter Current PIN: ");
                    int currentPin = scanner.nextInt();
                    if (currentPin == savedPin) {
                        System.out.print("Enter New PIN: ");
                        int newPin = scanner.nextInt();
                        System.out.print("Confirm New PIN: ");
                        int confirmPin = scanner.nextInt();
                        if (newPin == confirmPin) {
                            savedPin = newPin;
                            System.out.println("PIN successfully reset.");
                        } else {
                            System.out.println("PINs do not match. Try again.");
                        }
                    } else {
                        System.out.println("Incorrect Current PIN.");
                    }
                    break;

                default:
                    System.out.println("Invalid option. Please choose from the menu.");
            }
        }
    }

    private static void sendMoney(Scanner scanner, double balance, double fee, int savedPin, String dateStr) {
        System.out.print("Enter Receiver cKash Account No: ");
        int number = scanner.nextInt();
        System.out.print("Enter Amount: ");
        double amount = scanner.nextDouble();
        System.out.print("Enter Reference: ");
        String reference = scanner.next();
        System.out.print("Enter Menu PIN to confirm: ");
        int pin = scanner.nextInt();

        if (pin == savedPin) {
            if ((amount + fee) <= balance) {
                balance -= (amount + fee);
                System.out.printf("Send Money Tk %.2f to %d Successful. Ref %s. Fee Tk %.2f. Balance Tk %.2f.%n",
                        amount, number, reference, fee, balance);

                // Writing transaction to file
                try (PrintWriter file = new PrintWriter(new FileWriter("statements.txt", true))) {
                    file.printf("%s - Send Money: Tk %.2f to %d. Ref: %s. Fee: Tk %.2f. Balance: Tk %.2f.%n",
                            dateStr, amount, number, reference, fee, balance);
                } catch (IOException e) {
                    System.out.println("Error writing to file: " + e.getMessage());
                }
            } else {
                System.out.println("Insufficient balance.");
            }
        } else {
            System.out.println("Incorrect PIN.");
        }
    }
}