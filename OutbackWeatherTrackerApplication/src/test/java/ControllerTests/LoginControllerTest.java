package ControllerTests;

import static org.junit.jupiter.api.Assertions.*;

import cab302softwaredevelopment.outbackweathertrackerapplication.controllers.windows.LoginController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

public class LoginControllerTest extends ApplicationTest  {
    private LoginController loginController;

    @BeforeEach
    public void setUp() {
        loginController = new LoginController();
    }

    @Test
    public void testLoginSuccess() {
        boolean result = loginController.handleLogin("test1@gmail.com", "SecurePass1!");
        assertTrue(result, "Login should succeed with valid credentials.");
    }

    @Test
    public void testLoginFailure1() {
        boolean result = loginController.handleLogin("12224g", "aaaaaaa");
        assertFalse(result, "Login should fail with invalid credentials.");
    }

    @Test
    public void testSignupFailure1() {
        boolean result = loginController.handleSignUp("email@@example.com", "SecurePass1!");
        assertFalse(result, "Login should fail with invalid credentials.");
    }

    @Test
    public void testSignupFailure2() {
        boolean result = loginController.handleSignUp("email@example.com", "insecurepass1");
        assertFalse(result, "Login should fail with invalid credentials.");
    }
}
