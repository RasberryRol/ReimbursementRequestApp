package org.example;

import java.io.Serializable;

public class User implements Serializable {

    private int accountId; //variable to save the account's ID

    //getter method to retrieve the account ID
    public int getAccountId() {
        return accountId;
    }
    //setter method to update the account ID
    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    //constructor for user accounts credentials
    User(int accountId, String userName, String password, int balance){
        this.userName = userName;
        this.password = password;
        this.accountId = accountId;
        this.balance = balance;
    }

    private double lastTransaction; //variable to save the last transaction
    private char transaction;

    //user accounts username and password with their
    //constructor, getter, and setter methods
    private String userName;
    public String password;
    User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName){
        this.userName = userName;
    }


    public String getPassword() {
        return password;
    }
    public void setPassword(String Password){
        this.password = password;
    }

    //variable to save current and updated balance in the accounts
    private int balance;
    public int getBalance() {
        return balance;
    }
    public void setBalance(int balance){
        this.balance = balance;
    }



    /*
    * method to make a transfer
    * @param amount
    * @param id
    * */
    public void transfer(double amount) {
        if (balance < amount) {
            System.out.println("Unable to complete transfer!");
        } else {
            balance -= amount;
            System.out.println("Transfer completed!");
        }
    }

    /*
    * method to make a deposit and update the
    * transferred to and from accounts
    * @param deposit
    * deposit amount is to be added to the account once deposit is complete
     * */
    public void deposit(int deposit) {

        balance += deposit;
        lastTransaction = deposit;
        transaction = 'D';
        //System.out.println("\nBalance: $" + balance);

        //return balance;
    }

    /*
    * method to make a withdrawal
    * @param withdraw
    * withdraw amount is to be subtracted from the account once transfer is complete
    * */
    public void withdraw(double withdraw) {
    //if balance is less than $20, do not execute the transfer
    if ((balance - 20) > withdraw) {
        balance -= withdraw;
        lastTransaction = withdraw;
        transaction = 'W';

    }
    else{
        System.out.println("Insufficient funds!");
    }
    //return balance;
    }


    //if transaction == 'D', display last deposit
    //if transaction == 'W', display last withdrawal
    public void LastTransaction(){
        if ( transaction == 'D' ) {
            System.out.println("Your last transaction was a deposit of: $" + lastTransaction);

        } else if ( transaction == 'W' ) {
            System.out.printf("Your last transaction was a withdraw of: $" + lastTransaction);
        }
        //return lastTransaction;
    }

    //Change the current password to the new user password
/*    public void changePassword(boolean correctPassword, String newPassword) {

        if (correctPassword) {
            password = newPassword;
        }
    }*/

}
