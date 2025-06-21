package Controller;
import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    private AccountService accountService;
    private MessageService messageService;
    private ObjectMapper objectMapper;

    /**
     * Default constructor initializes services and object mapper
     */
    public SocialMediaController() {
        this.accountService = new AccountService();
        this.messageService = new MessageService();
        this.objectMapper = new ObjectMapper();
    }
    /**
     * Starts the Javalin application and configures all endpoints
     * @return The configured Javalin app
     */

    public Javalin startAPI() {
         Javalin app = Javalin.create();
        // Account endpoints
        app.post("/register", this::registerHandler);
        app.post("/login", this::loginHandler);

        // Message endpoints
        app.post("/messages", this::createMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageHandler);
        app.patch("/messages/{message_id}", this::updateMessageHandler);
        app.get("/accounts/{account_id}/messages", this::getMessagesByUserHandler);

        return app;
    }

    /**
     * Handles account registration requests
     * @param ctx The Javalin context containing request and response information
     */
    private void registerHandler(Context ctx) throws JsonProcessingException {
        Account account = objectMapper.readValue(ctx.body(), Account.class);
        Account registeredAccount = accountService.register(account);
        
        if(registeredAccount != null) {
            ctx.json(objectMapper.writeValueAsString(registeredAccount));
        } else {
            ctx.status(400); // Bad request if registration fails
        }
    }

    /**
     * Handles login requests
     * @param ctx The Javalin context containing request and response information
     */
    private void loginHandler(Context ctx) throws JsonProcessingException {
        Account account = objectMapper.readValue(ctx.body(), Account.class);
        Account loggedInAccount = accountService.login(account);
        
        if(loggedInAccount != null) {
            ctx.json(objectMapper.writeValueAsString(loggedInAccount));
        } else {
            ctx.status(401); // Unauthorized if login fails
        }
    }

    /**
     * Handles message creation requests
     * @param ctx The Javalin context containing request and response information
     */
    private void createMessageHandler(Context ctx) throws JsonProcessingException {
        Message message = objectMapper.readValue(ctx.body(), Message.class);
        Message createdMessage = messageService.createMessage(message);
        
        if(createdMessage != null) {
            ctx.json(objectMapper.writeValueAsString(createdMessage));
        } else {
            ctx.status(400); // Bad request if message creation fails
        }
    }

    /**
     * Handles requests to get all messages
     * @param ctx The Javalin context containing request and response information
     */
    private void getAllMessagesHandler(Context ctx) {
        ctx.json(messageService.getAllMessages());
    }

    /**
     * Handles requests to get a specific message by ID
     * @param ctx The Javalin context containing request and response information
     */
    private void getMessageByIdHandler(Context ctx) {
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.getMessageById(message_id);
        
        if(message != null) {
            ctx.json(message);
        } else {
            ctx.result(""); // Empty response if message not found
        }
    }

    /**
     * Handles message deletion requests
     * @param ctx The Javalin context containing request and response information
     */
    private void deleteMessageHandler(Context ctx) throws JsonProcessingException {
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message deletedMessage = messageService.deleteMessage(message_id);
        
        if(deletedMessage != null) {
            ctx.json(objectMapper.writeValueAsString(deletedMessage));
        } else {
            ctx.result(""); // Empty response if message not found
        }
    }

    /**
     * Handles message update requests
     * @param ctx The Javalin context containing request and response information
     */
    private void updateMessageHandler(Context ctx) throws JsonProcessingException {
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message messageUpdate = objectMapper.readValue(ctx.body(), Message.class);
        String message_text = messageUpdate.getMessage_text();
        
        Message updatedMessage = messageService.updateMessage(message_id, message_text);
        if(updatedMessage != null) {
            ctx.json(objectMapper.writeValueAsString(updatedMessage));
        } else {
            ctx.status(400); // Bad request if update fails
        }
    }

    /**
     * Handles requests to get all messages by a specific user
     * @param ctx The Javalin context containing request and response information
     * * Responses:
     * - 200 OK with JSON array of messages (empty array if no messages exist for user)
     */
    private void getMessagesByUserHandler(Context ctx) {
        int account_id = Integer.parseInt(ctx.pathParam("account_id"));
        ctx.json(messageService.getMessagesByUser(account_id));
    }

    /**
     * Example handler (not used in production)
     * @param ctx The Javalin context containing request and response information
     */
    private void exampleHandler(Context ctx) {
        ctx.json("sample text");
    }
}