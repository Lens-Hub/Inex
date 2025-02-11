package ge.inex.utils;

import ge.inex.runner.TestRunner;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class Hooks extends TestRunner {

    @BeforeMethod
    public void beforeEachTest() {
        System.out.println("Starting test...");
    }

    @AfterMethod
    public void afterEachTest() {
        System.out.println("Test finished.");
    }
}
