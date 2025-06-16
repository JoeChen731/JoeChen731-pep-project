package Service;

import DAO.MessageDAO;
import Model.Message;
import java.util.List;

/**
 * Service layer for Message operations
 * Contains business logic for message-related operations
 */
public class MessageService {
    private MessageDAO messageDAO;
    private AccountService accountService;

    /**
     * Default constructor initializes with new DAOs
     */
    public MessageService() {
        this.messageDAO = new MessageDAO();
        this.accountService = new AccountService();
    }

    /**
     * Constructor with dependency injection for testing
     * @param messageDAO The MessageDAO implementation to use
     * @param accountService The AccountService implementation to use
     */
    public MessageService(MessageDAO messageDAO, AccountService accountService) {
        this.messageDAO = messageDAO;
        this.accountService = accountService;
    }

    /**
     * Creates a new message with validation
     * @param message The message to create
     * @return Created message with ID if successful, null otherwise
     */
    public Message createMessage(Message message) {
        // Validate message requirements
        if(message.getMessage_text().isBlank() || 
           message.getMessage_text().length() > 255 || 
           accountService.getAccountById(message.getPosted_by()) == null) {
            return null;
        }
        
        return messageDAO.insertMessage(message);
    }

    /**
     * Retrieves all messages
     * @return List of all messages
     */
    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    /**
     * Retrieves a message by its ID
     * @param message_id The ID of the message to retrieve
     * @return The Message object if found, null otherwise
     */
    public Message getMessageById(int message_id) {
        return messageDAO.getMessageById(message_id);
    }

    /**
     * Deletes a message by its ID
     * @param message_id The ID of the message to delete
     * @return The deleted Message object if found and deleted, null otherwise
     */
    public Message deleteMessage(int message_id) {
        return messageDAO.deleteMessage(message_id);
    }

    /**
     * Updates a message's text with validation
     * @param message_id The ID of the message to update
     * @param message_text The new message text
     * @return Updated Message object if successful, null otherwise
     */
    public Message updateMessage(int message_id, String message_text) {
        // Validate new message text
        if(message_text.isBlank() || message_text.length() > 255) {
            return null;
        }
        
        return messageDAO.updateMessage(message_id, message_text);
    }

    /**
     * Retrieves all messages posted by a specific user
     * @param account_id The ID of the user whose messages to retrieve
     * @return List of messages posted by the user
     */
    public List<Message> getMessagesByUser(int account_id) {
        return messageDAO.getMessagesByUser(account_id);
    }
}