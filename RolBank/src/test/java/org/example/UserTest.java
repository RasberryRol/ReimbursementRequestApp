package org.example;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/*
*  This class is to test the User class
* */
public class UserTest {
    //variable declaration
    private User tester;
    private User tester2;

    //set up the input username and password for the User constructor
    @Before
    public void SetUp(){
        tester = new User("dief", "pass");
        tester2 = new User("rolson", "rol");
        tester.deposit(1000);
    }

    //test to make sure that the right amount is transferred to the account
/*
    @Test
    public void transfer() {
        assertEquals(500, tester.transfer(500, tester2.getAccountId()), 0.0);
    }
*/

    //test to make sure that the right amount is deposited the account
/*    @Test
    public void deposit() {
        assertEquals(500, tester.deposit(500));
    }*/

    //test to make sure that the right amount is withdrawn from the account
/*    @Test
    public void withdraw() {
        assertEquals(700, tester.withdraw(300),0.0);
    }*/

    //test the if condition in the withdraw method
  /*  @Test
    public void withdrawIf() {
        assertTrue(tester.withdraw(300 - 20) > 500);
    }
*/

}