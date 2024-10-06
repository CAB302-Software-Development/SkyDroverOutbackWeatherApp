import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.parallel.ExecutionMode.SAME_THREAD;

import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Account;
import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Location;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.parallel.Execution;

@Execution(SAME_THREAD)
@Timeout(value = 10000, unit = TimeUnit.MILLISECONDS) // no test should take longer than 10 seconds
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
    List<UUID> seenIds = new ArrayList<>();
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
    addAccounts();

    // Verify the accounts
    assertEquals(accountsTemplate.size(), accountDAO.getAll().size(),
        "There should be " + accountsTemplate.size() + " accounts");
  }

  @Test
  void testGetAccountByID() {
    // Insert the new accounts
    addAccounts();

    for (Account account : accountsTemplate) {

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
    Location relevantLocation = new Location(testAccount, 0.0, 0.0, 9999.0, "Sky Island");
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

    for (Account account : accountsTemplate) {
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

    for (Account account : accountsTemplate) {

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
    for (Account account : accountsTemplate) {

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

    for (Account account : accountsTemplate) {
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

}
