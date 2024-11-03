package DBTests;

import static org.junit.jupiter.api.Assertions.*;

import cab302softwaredevelopment.outbackweathertrackerapplication.database.dao.AccountDAO;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Account;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Location;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.Theme;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.UserModel;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.WidgetInfo;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.dto.CreateUserDTO;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.dto.UserLoginRequestDTO;
import cab302softwaredevelopment.outbackweathertrackerapplication.services.UserApiService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.json.JSONObject;
import org.junit.jupiter.api.*;

@Timeout(value = 100000, unit = TimeUnit.MILLISECONDS) // No test should take longer than 100 seconds
public class AccountDAOTest extends DBTest {

  @Test
  public void testAccountsGetAllEmpty() {
    // Retrieve the accounts
    List<Account> accounts = accountDAO.getAll();

    // Verify the accounts
    assertEquals(0, accounts.size(), "Accounts should be empty");
  }

  @Test
  public void testAddAccounts() {
    // Insert the new accounts
    addAccounts();

    // Retrieve the accounts
    List<Account> accounts = accountDAO.getAll();

    // Verify the hourly forecasts
    assertEquals(accountsTemplate.size(), accounts.size(),
        "There should be " + accountsTemplate.size() + " accounts");

    // Verify that the forecasts got assigned an ID and that they're unique
    List<String> seenIds = new ArrayList<>();
    for (Account account : accounts) {
      assertFalse(seenIds.contains(account.getId()), "Account ID should be unique");
      seenIds.add(account.getId());
    }
  }

  @Test
  void testDeleteAccount() {
    // Insert the new accounts
    addAccounts();

    // Verify the accounts
    assertEquals(accountsTemplate.size(), accountDAO.getAll().size(),
        "There should be " + accountsTemplate.size() + " accounts");

    // Retrieve the accounts
    List<Account> accounts = accountDAO.getAll();

    // Delete the accounts
    for (Account account : accounts) {
      accountDAO.delete(account);
    }

    // Verify the accounts
    assertEquals(0, accountDAO.getAll().size(), "Accounts should be empty");
  }

  @Test
  void testDeleteAccountById() {
    // Insert the new accounts
    addAccounts();

    // Verify the accounts
    assertEquals(accountsTemplate.size(), accountDAO.getAll().size(),
        "There should be " + accountsTemplate.size() + " accounts");

    // Retrieve the accounts
    List<Account> accounts = accountDAO.getAll();

    // Delete the accounts
    for (Account account : accounts) {
      accountDAO.delete(account.getId());
    }

    // Verify the accounts
    assertEquals(0, accountDAO.getAll().size(), "Accounts should be empty");
  }

  @Test
  void testUniqueAccounts() {
    // Insert the new accounts
    addAccounts();

    // Try to insert the same accounts again
    assertThrows(Exception.class, this::addAccounts);

    // Verify the accounts
    assertEquals(accountsTemplate.size(), accountDAO.getAll().size(),
        "There should be " + accountsTemplate.size() + " accounts");
  }

  @Test
  void testGetAccountByID() {
    // Insert the new accounts
    addAccounts();

    for (Account account : accountDAO.getAll()) {

      // Retrieve the account
      Account account_result = accountDAO.getById(account.getId());

      // Verify the account
      assertEquals(account.getId(), account_result.getId());
    }
  }

  @Test
  void testAccountDeleteCascade() {
    // Insert the new accounts
    addAccounts();
    addLocations();

    // Use a test account
    Account testAccount = accountsTemplate.get(1);

    // Add a relevant location
    Location relevantLocation = new Location(testAccount, 0.0, 0.0, "Sky Island");
    locationDAO.insert(relevantLocation);

    // Verify the locations
    assertEquals(locationsTemplate.size() + 1, locationDAO.getAll().size(),
        "There should be " + (locationsTemplate.size() + 1) + " locations");

    // Delete the account
    accountDAO.delete(testAccount);

    // Verify the hourly forecasts
    assertEquals(locationsTemplate.size(), locationDAO.getAll().size(),
        "There should be " + locationsTemplate.size() + " locations");

  }

  @Test
  void testUpdateEmail() {
    // Insert the new accounts
    addAccounts();

    for (Account account : accountDAO.getAll()) {
      String originalEmail = account.getEmail();

      // Update the email
      account.setEmail(account.getId() + "@gmail.com");
      accountDAO.update(account);

      // Retrieve the account
      Account account_result = accountDAO.getById(account.getId());
      assertNotEquals(originalEmail, account_result.getEmail());
      assertEquals(account_result.getEmail(), account.getId() + "@gmail.com");
    }
  }

  @Test
  void testVerifyPassword() {
    // Create a new account
    Account account = Account.builder()
        .username("username")
        .id(UUID.randomUUID().toString())
        .email("email@gmail.com")
        .password("password")
        .build();
    accountDAO.insert(account);

    // Verify the password
    assertTrue(account.verifyPassword("password"));
    assertFalse(account.verifyPassword("wrongpassword"));
  }

  @Test
  void testGetAccountByEmail() {
    // Insert the new accounts
    addAccounts();

    for (Account account : accountDAO.getAll()) {

      // Retrieve the account
      Account account_result = accountDAO.getByEmail(account.getEmail());

      // Verify the account
      assertEquals(account.getEmail(), account_result.getEmail());
    }
  }

  @Test
  void testGetAccountByNonExistentEmail() {
    // Insert the new accounts
    addAccounts();

    // Retrieve the account
    Account account_result = accountDAO.getByEmail("");
    assertNull(account_result);
  }

  @Test
  void testUpdatePassword() {
    // Insert the new accounts
    addAccounts();

    String testPassword = "newpassword123";
    for (Account account : accountDAO.getAll()) {

      assertFalse(account.verifyPassword(testPassword), "Password should not be the same as the test password");

      // Update the password
      account.setPassword(testPassword);
      accountDAO.update(account);

      // Retrieve the account
      Account account_result = accountDAO.getById(account.getId());
      assertTrue(account_result.verifyPassword(testPassword), "Password should be the same as the test password");
    }
  }

  @Test
  void testChangeCelsiusPreference() {
    // Insert the new accounts
    addAccounts();

    for (Account account : accountDAO.getAll()) {
      Boolean originalPreference = account.getPreferCelsius();

      // Update the preference
      account.setPreferCelsius(!originalPreference);
      accountDAO.update(account);

      // Retrieve the account
      Account account_result = accountDAO.getById(account.getId());
      assertNotEquals(originalPreference, account_result.getPreferCelsius());
      assertEquals(account_result.getPreferCelsius(), !originalPreference);
    }
  }

  @Test
  void testAddAccountWithDefaultLayouts() {
    // Create a new account
    Account account = Account.builder()
        .username("test")
        .id(UUID.randomUUID().toString())
        .email("testemail12345@test.com")
        .password("password")
        .build();

    // Insert the account
    accountDAO.insert(account);

    // Retrieve the account
    Account account_result = accountDAO.getByEmail("testemail12345@test.com");

    // Verify the account
    assertEquals(account.getEmail(), account_result.getEmail());
    assertEquals(account.getPreferCelsius(), account_result.getPreferCelsius());
    assertNotNull(account_result.getDashboardLayouts());
  }

  @Nested
  public class AccountQueryTests{
    @Test
    void testGetAccountById() {
      // Insert the new accounts
      addAccounts();

      for (Account account : accountDAO.getAll()) {

        // Retrieve the account
        Account account_result = new AccountDAO.AccountQuery()
            .whereId(account.getId())
            .getSingleResult();

        // Verify the account
        assertEquals(account.getId(), account_result.getId());

        // Confirm that only one account is returned
        List<Account> accounts = new AccountDAO.AccountQuery()
            .whereId(account.getId())
            .getResults();
        assertEquals(1, accounts.size());
        assertEquals(account.getId(), accounts.get(0).getId());
      }
    }

    @Test
    void testGetAccountByEmail() {
      // Insert the new accounts
      addAccounts();

      for (Account account : accountDAO.getAll()) {

        // Retrieve the account
        Account account_result = new AccountDAO.AccountQuery()
            .whereEmail(account.getEmail())
            .getSingleResult();

        // Verify the account
        assertEquals(account.getEmail(), account_result.getEmail());

        // Confirm that only one account is returned
        List<Account> accounts = new AccountDAO.AccountQuery()
            .whereEmail(account.getEmail())
            .getResults();
        assertEquals(1, accounts.size());
        assertEquals(account.getEmail(), accounts.get(0).getEmail());
      }
    }

    @Test
    void testFilterForCelsiusPreference() {
      // Insert the new accounts
      addAccounts();

      // Retrieve the accounts
      List<Account> accounts = new AccountDAO.AccountQuery()
          .wherePreferCelsius(true)
          .getResults();

      // Verify the accounts
      for (Account account : accounts) {
        assertTrue(account.getPreferCelsius());
      }

      // Retrieve the accounts
      accounts = new AccountDAO.AccountQuery()
          .wherePreferCelsius(false)
          .getResults();

      // Verify the accounts
      for (Account account : accounts) {
        assertFalse(account.getPreferCelsius());
      }
    }

    @Test
    void testFilterForCelsiusPreferenceAndEmail() {
      // Insert the new accounts
      addAccounts();

      for (Account account : accountDAO.getAll()) {

        // Retrieve account with the same email and celsius preference
        Account account_result = new AccountDAO.AccountQuery()
            .whereEmail(account.getEmail())
            .wherePreferCelsius(account.getPreferCelsius())
            .getSingleResult();
        // Verify the account
        assertNotNull(account_result);
        assertEquals(account.getEmail(), account_result.getEmail());
        assertEquals(account.getPreferCelsius(), account_result.getPreferCelsius());
      }
    }


  }
}
