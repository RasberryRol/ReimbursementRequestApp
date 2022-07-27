
/*
*  editorName: Dief Gerard
*  title: RolBank
*  description: Console banking application allowing users to
*               create and open accounts.
*  date: 07/21/2022
*  version: 1.0
* */

package org.example;

import org.postgresql.Driver;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.sql.*;

import java.io.IOException;
import java.util.*;


/*
 * This class is to:
 * - hold the main() method
 * - establish database connectivity (JDBC)
*/
public class App {
    //hashMap to hold the accounts information
    static private HashMap<String, User> userAccounts = new HashMap<>();

    //variable declarations and initializations
    static private User User;

    //static int primaryKey = 111;
    static private Scanner in = new Scanner(System.in);

    static private boolean exit = false;

    //method to register the driver for database connection
    static {
        try {
            DriverManager.registerDriver(new Driver());
        } catch (SQLException e) {
            System.out.println("Driver not registered!");
        }
    }

    //main method to run the program
    public static void main(String[] args) throws SQLException {

        //connect to the database
        try {
            Connection c = getConnection("postgres", "D!lemma628",
                    "jdbc:postgresql://rolson-dief-6261.cd6e7q9aln6t.us-east-1.rds.amazonaws.com:5432/library");

            //System.out.println("Database connection established!");
        } catch (SQLException e) {
            System.out.println("Database connection failed!");
        }

        ConnectionManager cnn = new ConnectionManager("jdbc:postgresql://rolson-dief-6261.cd6e7q9aln6t.us-east-1.rds.amazonaws.com:5432/library",
                "postgres", "D!lemma628", new Driver());

        Connection connection = cnn.getConnection();

        //SQL query to initialize the app to the accounts available in the database
        String query = "select username, userpassword from customer_accounts";

        //save the database information to a hashMap for later use
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();  //execute the query and save the result to rs

            //save the username and password to the hashMap
            while (rs.next()) {
                String username = rs.getString("username");
                String password = rs.getString("userpassword");
                User user = new User(username, password);
                userAccounts.put(user.getUserName(), user);
                saveHashMap(userAccounts);

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //start of the program run
        try {
            employee();
        } catch (IOException e) {
            System.out.println("Something is wrong (employee)!");
        }


        try {
            menuInput(1);
        } catch (IOException e) {
            System.out.println("Something is wrong (menuInput(1))!");
        }

        //menu to be displayed once an account is accessed
        while (!exit) {
            System.out.println();
            System.out.println("Chose an action of the folowing options:");
            System.out.print("Enter: \n");
            System.out.println("  *A for Account Information.\t*B to Deposit.\n" +
                    "  *C to Withdraw.\t\t\t*D to Check Balance.\n" +
                    "  *E to Delete Account.\t\t*F to Make transfer.\n" +
                    "  *G for Main menu.\t\t*H to Exit");

            try {
                menuInput(2);
            } catch (IOException e) {
                System.out.println("Something is wrong (menuInput(2))!");
            }
        }
        in.close();

    }

    /*
    * This method is to establish connection to the database
    * */
    public static Connection getConnection(String userName, String password, String url) throws SQLException {
        return DriverManager.getConnection(url, userName, password);
    }


    /*
     * Method to take a user input and
     * call the openAccount or
     * the createAccount method based on the input
    * */
    static public Object menuInput(int set) throws IOException {

        //input 1 to open an account
        if (set == 1) {
            int choice;

            try {
                System.out.print("Enter digit: ");

                if (in.hasNextInt()) {

                    choice = in.nextInt();

                    if (choice == 1) {
                        openAccount(); //open existing account
                    }
                    else if (choice == 2) {
                        createAccount(); //create new account
                    }
                    else {
                        System.out.println();
                        System.out.println("Please enter a valid option.");
                        in.nextLine();
                        menuInput(1);
                    }

                }
                else if (in.hasNextLine()) {
                    System.out.println();
                    System.out.println("Please enter a valid option.");
                    in.nextLine();
                    menuInput(1);

                }

            } catch (InputMismatchException | SQLException e) {
                e.printStackTrace();
                System.out.println();
                System.out.println("Please enter a valid option.");
                menuInput(1);
            }

        }
        //Input for the second set of options
        else if (set == 2) {

            String choice;

            try {
                System.out.println();
                System.out.print("Enter letter: ");
                choice = in.next();
                choice = choice.toUpperCase();

                switch (choice) {
                    //display current account information
                    case "A":
                        retrieveInfo();
                        break;

                    //make a deposit
                    case "B":
                        System.out.println();
                        System.out.print("Please enter any amount to deposit: ");
                        int deposit = in.nextInt();

                        //connect to the database
                        ConnectionManager cnn = new ConnectionManager("jdbc:postgresql://rolson-dief-6261.cd6e7q9aln6t.us-east-1.rds.amazonaws.com:5432/library",
                                "postgres", "D!lemma628", new Driver());

                        User user = null;

                        System.out.print("Enter the acoount ID: ");
                        int ID = in.nextInt();

                        AccountRepo repo = new AccountRepo(cnn);
                        user = repo.getById(ID); //select the account from the database by ID

                        //add the deposit to the balance and save it
                        user.setBalance(user.getBalance() + deposit);
                        repo.updateBalance(user, ID);

                        //update the hashMap
                        User.deposit(deposit);
                        saveHashMap(userAccounts);

                        break;

                    //make a withdraw
                    case "C":
                        System.out.println();
                        System.out.print("Please enter any amount to withdraw: ");
                        int withdraw = in.nextInt();

                        ConnectionManager cnn3 = new ConnectionManager("jdbc:postgresql://rolson-dief-6261.cd6e7q9aln6t.us-east-1.rds.amazonaws.com:5432/library",
                                "postgres", "D!lemma628", new Driver());

                        User user3 = null;

                        System.out.print("Enter the acoount ID: ");
                        int id4 = in.nextInt();

                        AccountRepo repo2 = new AccountRepo(cnn3);
                        user3 = repo2.getById(id4);
                        user3.setBalance(user3.getBalance() - withdraw);
                        repo2.updateBalance(user3, id4);


                        User.withdraw(withdraw);
                        saveHashMap(userAccounts);
                        break;

                    //display current account's balance
                    case "D":
                        checkBalance();
                        break;


                    //call the deleAccount method to delete an account
                    case "E":
                        deleteAccount();
                        break;

                    //make a transfer
                    case "F":
                        System.out.println("Enter the current account's ID: ");
                        int id1 = in.nextInt();
                        System.out.println("Enter the id of the account to transfer to: ");
                        int id = in.nextInt();

                        ConnectionManager cnn2 = new ConnectionManager("jdbc:postgresql://rolson-dief-6261.cd6e7q9aln6t.us-east-1.rds.amazonaws.com:5432/library",
                                "postgres", "D!lemma628", new Driver());

                        AccountRepo repo1 = new AccountRepo(cnn2);
                        AccountRepo repo3 = new AccountRepo(cnn2);
                        User user1 = repo1.getById(id);
                        User user2 = repo3.getById(id1);

                        System.out.println("Enter transfer amount: ");
                        int amount = in.nextInt();
                        user1.setBalance(user1.getBalance() + amount);
                        user2.setBalance(user2.getBalance() - amount);

                        repo1.updateBalance(user1, id);
                        repo3.updateBalance(user2, id1);

                        String userName = user1.getUserName();
                        String password = user1.getPassword();

                        User updateReceivingBalance = new User(userName, password);

                        updateReceivingBalance.setBalance((user1.getBalance() + amount));
                        User.withdraw(amount);
                        saveHashMap(userAccounts);
                        break;

                    case "G":
                        mainMenu();
                        menuInput(1);
                        break;

                    //exit the program if I is entered
                    case "H":
                        exit = true;
                        break;

                    //tell the user that an invalid option was typed in
                    default:
                        System.out.println();
                        System.out.println("Plase chose a valid option.");
                        break;
                }
            }
            //catch InputMismatchException if user input is none of the above switch/case options
            catch (InputMismatchException e) {
                System.out.println();
                System.out.println("Please enter a valid option.");
                menuInput(2);

            }
        }

        return null;
    }


    /*
    * check for the initialized accounts and
    * open the specified one if it exist
    * */
    static public void openAccount() {

        System.out.println();
        System.out.print("Please enter the username of the account: ");
        String userName = in.next();

        //check if the account exist
        if (!(userAccounts.get(userName) == null)) {

            boolean correctPassword = validatePassword(userName, 0);

            if (correctPassword) {
                User user = userAccounts.get(userName);

                //Set the new User object to a global object to be ready to take action in the account
                User = user;

            } else {
                User user = userAccounts.get(userName);
                User = user;
            }
        } else {

            System.out.println();
            System.out.println("Please enter an existing account.");
            openAccount();
        }
    }

    //select an account from the database using its ID
    public static int getNextId() throws SQLException {
        ConnectionManager cnn = new ConnectionManager("jdbc:postgresql://rolson-dief-6261.cd6e7q9aln6t.us-east-1.rds.amazonaws.com:5432/library",
                "postgres", "D!lemma628", new Driver());

        Connection connection = cnn.getConnection();
        String query = "select max(account_id) from customer_accounts";

        int nextId = 0;
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                nextId = rs.getInt(1) + 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nextId;
    }

    //Creates a new account-User object and update the database and HashMap with the new account
    static public void createAccount() throws IOException, SQLException {

        System.out.println();
        System.out.print("Enter a new user name for the account: ");

        String userName = in.next();

        //Checks for an existing account
        if (!(userAccounts.get(userName) == null)) {

            System.out.println();
            System.out.println("This user name already exists.");
            createAccount();

        }

        System.out.println();
        System.out.print("Enter a new password for the account: ");
        String password = in.next();

        int id2 = getNextId();

        System.out.println();
        System.out.println("Your account ID is: " + id2);

        //Creates the account-User object and updates the HashMap
        connectSaveInfo(userName, password, id2);
        User user = new User(userName, password);
        userAccounts.put(user.getUserName(), user);

        //Set the new User object to a global object to be ready to take action in the account
        User = user;

        saveHashMap(userAccounts);
    }


    //Deletes an account-User object from the database and HashMap
    static public void deleteAccount() throws IOException {

        System.out.println();
        System.out.println("Please enter password to delete account.");

        in.nextLine();
        boolean correctPassword = validatePassword(User.getUserName(), 0);

        if (correctPassword) {
            //remove the account from the file
            userAccounts.remove(User.getUserName());
            saveHashMap(userAccounts);
            System.out.println();
            System.out.println("You have successfuly deleted this account: " + User.getUserName() + ".");
            System.exit(0);

        }
    }

    /*
    * Method to check whether a password is valid or not
    * @param userName
    * @param tries
    * */
    static public boolean validatePassword(String userName, int tries) {

        User user = userAccounts.get(userName);
        tries++;

        if (tries == 1) {

            System.out.println();
            System.out.print("Please enter the password: ");
            String password = in.next();
            if (password.equals(user.getPassword())) {
                return true;
            } else {
                System.out.println();
                System.out.println("Incorrect password.");
                validatePassword(userName, tries);
            }

        } else if (tries == 2) {

            System.out.println();
            System.out.print("Please enter the correct password: ");
            String password = in.next();
            if (password.equals(user.getPassword())) {
                return true;
            } else {
                System.out.println();
                System.out.println("Incorrect password.");
                validatePassword(userName, tries);
            }

        } else if (tries == 3) {

            System.out.println();
            System.out.print("Please enter the correct password: ");
            String password = in.next();
            if (password.equals(user.getPassword())) {
                return true;
            } else {
                System.out.println();
                System.out.println("Incorrect password.");
                validatePassword(userName, tries);
            }

        } else if (tries == 4) {
            System.out.println();
            System.out.println("You have used your 3 tries the app is going to exit.");
            System.exit(0);
        }

        return false;
    }


    //method to display the main menu
    static public void mainMenu() {
        System.out.println();
        System.out.println("1: To open existing account.\t2: To create new account.");
    }


    /*
    * Employee sign in method
    * */
    static public void employee() throws IOException {
        System.out.println("\t\tWelcome to Rolbank!\n");
        System.out.println("Are you a Rolbank employee?\n" +
                "Reply Y for YES or N for NO: ");

        int choice;
        try {
            String empl;
            empl = in.nextLine();
            empl = empl.toUpperCase();

            switch (empl) {
                //log in from the main menu
                case "N":
                    System.out.println("\t\t\t\t\t\t-Costumer Login-");
                    mainMenu();
                    break;

                //log in as an employee
                case "Y":
                    System.out.println("\t\t\t\t\t\t-Employee Login-");

                    openAccount();

                    System.out.println();
                    System.out.println("Enter: \n" + "*1 to create a customer account\n" +
                            "*2 to approve or deny credit applications\n");

                    //access to create customer account
                    choice = in.nextInt();
                    if (choice == 1) {
                        System.out.println("Create customer account.");
                        createUserAccount();
                    } else if (choice == 2) {
                        System.out.println("Review credit.");
                    } else {
                        System.out.println("Invalid Input!");
                    }
                    break;

                default:
                    System.out.println("Invalid input. Try Again!\n");
                    employee();
                    break;
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid Input. Try Again!");
            employee();
        }
    }

    //methos for an employee to create a user account with temporary credentials
    static public boolean createUserAccount() throws IOException {
        System.out.println("Customer temporary userName: ");
        String custUserName = in.next();

        //checks if the username already exist
        if (!(userAccounts.get(custUserName) == null)) {

            System.out.println();
            System.out.println("This username already exists.");
            createUserAccount();
            return false;
        }

        System.out.println();
        System.out.print("Customer temporary password: ");
        String password = in.next();
        System.out.println();

        //Creates the account-User object and updates the HashMap
        User user = new User(custUserName, password);
        userAccounts.put(user.getUserName(), user);

        //Set the new User object to a global object to be ready to take action in the account
        User = user;

        saveHashMap(userAccounts);
        return true;
    }

    //method to get database information
    public static void connectSaveInfo(String userName, String password, int id2) {
        ConnectionManager cnn = new ConnectionManager("jdbc:postgresql://rolson-dief-6261.cd6e7q9aln6t.us-east-1.rds.amazonaws.com:5432/library",
                "postgres", "D!lemma628", new Driver());

        User user = new User(userName, password);
        user.setAccountId(id2);
        user.setBalance(0);
        AccountRepo repo = new AccountRepo(cnn);
        repo.save(user);
    }


    /*method to retrieve data from the database and
    * display the current account information
     */
    public static User retrieveInfo() {
        ConnectionManager cnn = new ConnectionManager("jdbc:postgresql://rolson-dief-6261.cd6e7q9aln6t.us-east-1.rds.amazonaws.com:5432/library",
                "postgres", "D!lemma628", new Driver());

        User user = null;
        AccountRepo repo = new AccountRepo(cnn);

        System.out.print("Enter the acoount ID: ");
        System.out.println();
        int ID = in.nextInt();

        user = repo.getById(ID);

        try {
            System.out.print("Account Information: \n" +
                    "\tUsername: " + user.getUserName() + "\n" +
                    "\tPassword: " + user.getPassword() + "\n" +
                    "\tID: " + user.getAccountId());
        } catch (NullPointerException e) {
            System.out.println("The account does not exist.");
        }
        return user;
    }


    //method to display account's balance
    public static void checkBalance() throws IOException {

        ConnectionManager cnn = new ConnectionManager("jdbc:postgresql://rolson-dief-6261.cd6e7q9aln6t.us-east-1.rds.amazonaws.com:5432/library",
                "postgres", "D!lemma628", new Driver());

        User user = null;

        System.out.print("Enter the account ID: ");
        int id = in.nextInt();

        AccountRepo repo = new AccountRepo(cnn);
        user = repo.getById(id);
        System.out.println("Your account balance is: " + user.getBalance());
        //repo.updateBalance(user, id);

    }

    //method to create a file and save the account information to the hashMap
    static public void saveHashMap(HashMap<String, User> userAccounts) throws IOException {

        FileOutputStream fos = new FileOutputStream("Accounts.dat");
        ObjectOutputStream oos = new ObjectOutputStream(fos);

        //Saves the HashMap to a file
        oos.writeObject(userAccounts);

        oos.close();
    }
}
