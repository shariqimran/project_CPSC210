package ui;

//import com.oracle.javafx.jmx.json.JSONReader;

import model.*;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

// Metaplace Application
public class MetaplaceApp {

    private Account account;
    //private Account account1;
    protected ArrayList<Products> productsList;
    private Scanner sc;
    private static final String JSON_STORE = "./data/account.json";
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;


    // method taken from Teller App, (https://github.students.cs.ubc.ca/CPSC210/TellerApp)
    // EFFECTS: runs the metaplace application
    public MetaplaceApp() {
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        productsList = new ArrayList<>();
        runMetaplace();
    }

    // method taken from Teller App, (https://github.students.cs.ubc.ca/CPSC210/TellerApp)
    // MODIFIES: this
    // EFFECTS: processes user input
    public void runMetaplace() {
//        boolean keepGoing = true;
//        String command;
        init();
//
//        while (keepGoing) {
//            displayMenu();
//            command = sc.next();
//            command = command.toLowerCase();
//            if (command.equals("q")) {
//                saveAccount();
//                keepGoing = false;
//            } else {
//                processMenuCommand(command);
//            }
//        }
//
//        System.out.println("\n---------------------------------");
//        System.out.println("- Thank you for using METAPLACE -");
//        System.out.println("           Goodbye!");
//        System.out.println(("---------------------------------"));
    }

    // method taken from JsonSerializationDemo, (https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git)
    // EFFECTS: saves the workroom to file, prints Events
    protected void saveAccount() {
        try {
            jsonWriter.open();
            jsonWriter.write(account);
            jsonWriter.close();
            System.out.println("Saved");

            for (Event el : EventLog.getInstance()) {
                System.out.println(el.toString() + "\n\n");
            }

            System.exit(0);

        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to" + JSON_STORE);
        }
    }


    // MODIFIES: this
    // EFFECTS: initializes account and product list
    public void init() {

        loadWorkRoom();
//        account = account1;

        if (account.getProducts().isEmpty()) {
            Products product1 = new Products("Sapphire Blue Shirt", 800, "Brand New!");
            Products product2 = new Products("Red Shirt", 500, "Used Like New!");
            Products product3 = new Products("SM1 Art Piece", 3000,
                    "Get Exclusive Product Drops from the SM Website!");
            Products product4 = new Products("SM2 Art Piece", 3500,
                    "Get Exclusive Product Drops from the SM Website!");
            Products product5 = new Products("KERS Collectors item", 6000,
                    "Limited Edition KERS Black & White Figurine!");

            productsList.add(product1);
            productsList.add(product2);
            productsList.add(product3);
            productsList.add(product4);
            productsList.add(product5);

            for (Products p : productsList) {
                account.addToProducts(p);
            }
        } else {
//            productsList = account1.getProducts();
            productsList = account.getProducts();
        }
        sc = new Scanner(System.in);

    }


    // EFFECTS: displays menu of options to user
    public void displayMenu() {
//        System.out.println(account.getProducts());
//        System.out.println(account.getPurchase());
        System.out.println("\n-----------------------");
        System.out.println("       METAPLACE       ");
        System.out.println("-----------------------");
        System.out.println("\nSelect from:");
        System.out.println("\t1 => Browse Products");
        System.out.println("\t2 => Create a Listing");
        System.out.println("\t3 => View your Purchases");
        System.out.println("\t4 => Wallet");
        System.out.println("\tQ => Save and Quit");
        System.out.println("\nUser Input: ");
    }

    // MODIFIES: this
    // EFFECTS: processes user command
    public void processMenuCommand(String command) {
        switch (command) {
            case "1":
                viewProducts();
                break;
            case "2":
                listProducts();
                break;
            case "3":
                viewPurchases();
                break;
            case "4":
                viewWallet();
                break;
            default:
                System.out.println("\nSelection not valid...");
                System.out.println("Please try again!");
                break;
        }
    }

    // EFFECTS: shows user all products in product list
    public void viewProducts() {
        System.out.println("\nMETAPLACE");

        for (Products product : productsList) {
            double num = 0;
            num = productsList.indexOf(product) + 1;

            System.out.println("-----------------------");
            System.out.printf("Number: %.0f%nName: %s%nPrice: $%.0f%nDescription: %s", num,
                    product.getName(), product.getPrice(), product.getDescription());
            System.out.println();
        }
        viewProductsOptions();
    }

    // EFFECTS: displays product menu options, processes user command
    public void viewProductsOptions() {
        if (productsList.isEmpty()) {
            System.out.println("\nNo items to display!");
        } else {
            System.out.println("-----------------------");
            System.out.printf("%nCurrent Balance: $%.0f%n", account.getBalance());
            System.out.println("\n1) Purchase an Item");
            System.out.println("2) Return to Menu");
            System.out.println("\nUser Input: ");
            String selection = sc.next();
            switch (selection) {
                case "1":
                    purchaseItem();
                    break;
                case "2":
                    System.out.println("\nReturning to menu...");
                    break;
                default:
                    System.out.println("\nSelection not valid... \nPlease Try Again!");
                    viewProducts();
                    break;
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: conducts an item transaction
    public void purchaseItem() {
        System.out.println("\nPlease enter the number of the item you wish to purchase: ");

        if (sc.hasNextInt() && !productsList.isEmpty()) {
            int selection = sc.nextInt();
            if (selection > 0 && selection <= productsList.size()) {
                Products yourProduct = productsList.get(selection - 1);
                if (yourProduct.getPrice() <= account.getBalance()) {
                    account.purchase(yourProduct);
                    productsList.remove(yourProduct);
                    //remove(yourProduct);
                    productReceipt(yourProduct);
                } else {
                    System.out.println("\nNot enough funds to purchase '" + yourProduct.getName() + "'!");
                    System.out.println("\nReturning to Product Listing(s)...");
                    viewProducts();
                }
            } else {
                System.out.println("\nInput not valid... \nPlease Try Again");
                purchaseItem();
            }
        } else {
            System.out.println("\nInput is not an Integer! \nPlease Try Again");
            String garbage = sc.next();
            purchaseItem();
        }
    }

    // EFFECTS: returns purchased item receipt
    public void productReceipt(Products yourProduct) {
        System.out.println("\n   PURCHASE RECEIPT");
        System.out.println("-----------------------");
        System.out.println("Congratulations!");
        System.out.println("You Have Successfully Purchased:");
        System.out.printf("%nName: %s%nPrice: $%.0f%n%nYour Remaining Balance: $%.0f",
                yourProduct.getName(), yourProduct.getPrice(), account.getBalance());
        System.out.println("\n\nYou are now returning to the main menu...");
    }

    // EFFECTS: displays list menu options, processes user command
    public void listProducts() {
        System.out.println("\nMETAPLACE LISTING");
        System.out.println("-----------------------");
        System.out.println("1) Enter details of Listing");
        System.out.println("2) Return to Menu");
        System.out.println("\nUser Input: ");

        String selection = sc.next();

        switch (selection) {
            case "1":
                listing();
                break;
            case "2":
                System.out.println("\nReturning to menu...");
                break;
            default:
                System.out.println("\nSelection not valid...");
                System.out.println("Please try again!");
                listProducts();
                break;
        }
    }

    // EFFECTS: processes user command for name in listing menu
    public void listing() {
        System.out.println("\nYour Listing");
        System.out.println("-----------------------");
        System.out.print("Name: ");

        String selectionName = sc.next();
        System.out.print("Price: ");

        checkListingSpecs(selectionName);
    }

    // MODIFIES: this
    // EFFECTS: creates a listing
    public void checkListingSpecs(String selectionName) {
        if (sc.hasNextInt()) {
            double selectionPrice = sc.nextInt();
            if (selectionPrice > 0) {
                System.out.print("Description: ");
                String selectionDesc = sc.next();
                Products newProduct = new Products(selectionName, selectionPrice, selectionDesc);
                productsList.add(newProduct);
                //account.addToProducts(newProduct); // -----
                System.out.println("\nYou have successfully listed your product '" + newProduct.getName()
                        + "' in the METAPLACE");
                System.out.println("\nReturning to menu...");
            } else {
                System.out.println("\nInput not valid...");
                System.out.println("Please enter a positive integer!");
                listing();
            }
        } else {
            String garbage = sc.next();
            System.out.println("Input is not an Integer!");
            System.out.println("Please Try Again");
            listing();
        }
    }

    // EFFECTS: shows purchase history to user, processes user command
    public void viewPurchases() {
        if (account.getPurchase().isEmpty()) {
            System.out.println("\nPURCHASE HISTORY");
            System.out.println("-----------------------");
            System.out.println("No Purchases to Show!");
            System.out.println("Returning to Menu...");
        } else {
            System.out.println("\nPURCHASE HISTORY");
            System.out.println("-----------------------");
            int num = 0;
            for (Products p : account.getPurchase()) {
                num++;
                double price = p.getPrice();
                System.out.println("-" + num + "-" + "\nName: "
                        + p.getName() + "\nPrice: $" + String.format("%.0f", price) + "\n");
            }
            System.out.println("\n1) Return to Menu");
            System.out.println("\nUser Input: ");
            String selection = sc.next();
            returnMenuOnViewPurchases(selection);
            if (selection.equals("1")) {
                System.out.println("\nReturning to menu...");
            }
        }
    }

    // EFFECTS: check if user has not entered the right command to proceed
    public void returnMenuOnViewPurchases(String selection) {
        while (!selection.equals("1")) {
            System.out.println("\nSelection not valid...");
            System.out.println("Please try again!");
            selection = sc.next();
        }
    }

    // EFFECTS: displays wallet menu, processes user commands
    public void viewWallet() {
        System.out.println("\nWALLET");
        System.out.println("-----------------------");
        System.out.printf("Current Balance: $%.0f%n", account.getBalance());
        System.out.println("\n1) Add Funds");
        System.out.println("2) Return to Menu\n");
        System.out.println("User Input: ");

        String selection = sc.next();

        switch (selection) {
            case "1":
                addFunds();
                break;
            case "2":
                System.out.println("\nReturning to menu...");
                break;
            default:
                System.out.println("\nSelection not valid...");
                System.out.println("Please try again!");
                viewWallet();
                break;
        }
    }

    // MODIFIES: this
    // EFFECTS: adds funds in wallet, processes user commands
    public void addFunds() {
        System.out.println("\nPlease enter the amount you wish to add: ");

        if (sc.hasNextInt()) {
            double amount = sc.nextInt();
            if (account.reload(amount)) {
                account.addMoney(amount);
                System.out.printf("%nYou have successfully added $%.0f to your account", amount);
                System.out.printf("%nYour new balance is $%.0f%n", account.getBalance());
            } else {
                System.out.println("\nInput not valid...");
                System.out.println("Please try again!");
                viewWallet();
            }
        } else {
            System.out.println("\nInput not valid...");
            System.out.println("Please try again!");
            String garbage = sc.next();
            viewWallet();
        }
    }

    public Account getAccount() {
        return this.account;
    }

    // method taken from JsonSerializationDemo, (https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git)
    // MODIFIES: this
    // EFFECTS: loads workroom from file
    public void loadWorkRoom() {
        try {
//            account1 = jsonReader.read();
            account = jsonReader.read();
            System.out.println("Loaded from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }
}
