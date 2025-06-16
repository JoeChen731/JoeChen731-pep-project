package Service;

import DAO.AccountDAO;
import Model.Account;

/**
 * Service layer for Account operations
 * Contains business logic for account-related operations
 */
public class AccountService {
    private AccountDAO accountDAO;

    /**
     * Default constructor initializes with new AccountDAO
     */
    public AccountService() {
        this.accountDAO = new AccountDAO();
    }

    /**
     * Constructor with dependency injection for testing
     * @param accountDAO The AccountDAO implementation to use
     */
    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    /**
     * Registers a new account with validation
     * @param account The account to register
     * @return Registered account with ID if successful, null otherwise
     */
    public Account register(Account account) {
        // Validate username and password requirements
        if(account.getUsername().isBlank() || account.getPassword().length() < 4) {
            return null;
        }
        
        // Check if username already exists
        if(accountDAO.getAccountByUsername(account.getUsername()) != null) {
            return null;
        }
        
        // Insert the new account into database
        return accountDAO.insertAccount(account);
    }

    /**
     * Authenticates a user's login credentials
     * @param account The account with login credentials
     * @return Authenticated account if successful, null otherwise
     */
    public Account login(Account account) {
        return accountDAO.login(account.getUsername(), account.getPassword());
    }

    /**
     * Retrieves an account by its ID
     * @param account_id The ID of the account to retrieve
     * @return The Account object if found, null otherwise
     */
    public Account getAccountById(int account_id) {
        return accountDAO.getAccountById(account_id);
    }
}