package org.example;

import org.junit.Test;


import java.io.IOException;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class AppTester {

    @Test
    public void MenuInput1() throws IOException {
        App tester = new App();
        assertEquals(1, tester.menuInput(1));

    }


    //method to test the openAccount method
    @Test
    public void testOpenAccount() {
        App tester = new App();
        assertEquals(1, 2);
    }


    @Test
    public void createAccount() throws SQLException, IOException {

    }


    @Test
    public void createAccount2() throws SQLException, IOException {

    }

    @Test
    public void validatePassword1() {
        App tester = new App();
        assertTrue("Should return true", tester.validatePassword("user", 1));
}

    @Test
    public void validatePassword2() {
        App tester = new App();
        assertEquals(true, tester.validatePassword("user", 2));
}

    @Test
    public void validatePassword3() {
        App tester = new App();
        assertEquals(true, tester.validatePassword("user", 3));
    }

    @Test
    public void validatePassword4() {
        App tester = new App();
        assertEquals(false, tester.validatePassword("user", 4));
    }
}

