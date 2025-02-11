package ge.inex.tests;

import ge.inex.utils.ExtentManager;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import com.aventstack.extentreports.ExtentTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ApiTest {

    @Test
    public void testGetUsersStatusCode() {
        ExtentTest test = ExtentManager.createTest("GET Users API Test");

        // API request
        Response response = RestAssured.get("https://reqres.in/api/users?page=2");

        // ვამოწმებთ, რომ სტატუს კოდი 200-ია
        int statusCode = response.getStatusCode();
        test.info("Received status code: " + statusCode);
        Assert.assertEquals(statusCode, 200, "Unexpected status code!");

        // ტესტის შედეგი რეპორტში
        test.pass("Status code is correct: 200");
        ExtentManager.flushReports();
    }
}
