package ru.otus.conf;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.db.handler.GetAllUsersHandler;
import ru.otus.db.handler.InsertUserHandler;
import ru.otus.front.FrontendService;
import ru.otus.front.FrontendServiceImpl;
import ru.otus.front.handler.GetUserResponseHandler;
import ru.otus.messagesystem.HandlersStore;
import ru.otus.messagesystem.HandlersStoreImpl;
import ru.otus.messagesystem.MessageSystem;
import ru.otus.messagesystem.MessageSystemImpl;
import ru.otus.messagesystem.client.CallbackRegistry;
import ru.otus.messagesystem.client.CallbackRegistryImpl;
import ru.otus.messagesystem.client.MsClient;
import ru.otus.messagesystem.client.MsClientImpl;
import ru.otus.messagesystem.message.MessageType;

@Configuration
public class MessageSystemConfig {

    private static final String FRONTEND_SERVICE_CLIENT_NAME = "frontendService";
    private static final String DATABASE_SERVICE_CLIENT_NAME = "databaseService";

    @Bean(destroyMethod = "dispose")
    public MessageSystem messageSystem() {
        return new MessageSystemImpl();
    }

    @Bean
    public CallbackRegistry callbackRegistry() {
        return new CallbackRegistryImpl();
    }

    @Bean("databaseMsClient")
    public MsClient databaseMsClient(MessageSystem messageSystem, CallbackRegistry callbackRegistry, InsertUserHandler insertUserHandler, GetAllUsersHandler getAllUsersHandler) {
        HandlersStore requestHandlerDatabaseStore = new HandlersStoreImpl();
        requestHandlerDatabaseStore.addHandler(MessageType.INSERT_USER, insertUserHandler);
        requestHandlerDatabaseStore.addHandler(MessageType.GET_USERS, getAllUsersHandler);

        MsClient databaseMsClient = new MsClientImpl(DATABASE_SERVICE_CLIENT_NAME,
                messageSystem, requestHandlerDatabaseStore, callbackRegistry);
        messageSystem.addClient(databaseMsClient);

        return databaseMsClient;
    }

    @Bean("frontMsClient")
    public MsClient frontMsClient(MessageSystem messageSystem, CallbackRegistry callbackRegistry, GetUserResponseHandler getUserResponseHandler) {
        HandlersStore frontHandlerStore = new HandlersStoreImpl();
        frontHandlerStore.addHandler(MessageType.GET_USERS, getUserResponseHandler);

        MsClient frontMsClient = new MsClientImpl(FRONTEND_SERVICE_CLIENT_NAME,
                messageSystem, frontHandlerStore, callbackRegistry);
        messageSystem.addClient(frontMsClient);

        return frontMsClient;
    }

    @Bean
    public FrontendService frontendService(@Qualifier("frontMsClient") MsClient frontMsClient) {
        return new FrontendServiceImpl(frontMsClient, DATABASE_SERVICE_CLIENT_NAME);
    }

}
