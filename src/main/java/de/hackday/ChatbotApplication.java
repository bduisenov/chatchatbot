package de.hackday;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import de.hackday.fsm.Conditions;
import de.hackday.fsm.FSM;
import de.hackday.fsm.States;
import de.hackday.rest.TelegramResponse;
import de.hackday.service.InputProcessingService;
import de.hackday.usecase.AskFullNameInteractor;
import de.hackday.usecase.UseCase;
import javaslang.control.Try;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.support.RestGatewaySupport;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static de.hackday.Constants.API_KEY;
import static de.hackday.Constants.API_PATH;
import static de.hackday.fsm.States.*;

@SpringBootApplication
public class ChatbotApplication extends RestGatewaySupport {

    private ScheduledExecutorService pollingService = Executors.newScheduledThreadPool(1);

    private ApplicationContext context;

    private AtomicLong lastPollingOffset = new AtomicLong(0);

    private Cache<Long, States> cache = CacheBuilder.newBuilder().build();

    @Resource
    private InputProcessingService inputProcessingService;

    @Resource
    private Map<States, UseCase<String, String>> mapping;

    @Resource
    private Map<Integer, States> stepMapping;

    @Bean
    public Map<Integer, States> stepMapping() {
        return ImmutableMap.of(
                1, TOP,
                2, CREATE,
                3, ADD_MEMBER
        );
    }

    @Bean
    public Map<States, UseCase<String, String>> mapping(
            UseCase<String, String> greetingsInteractor,
            UseCase<String, String> listTopOptionsInterator,
            UseCase<String, String> askFullNameInteractor,
            UseCase<String, String> askForEmailAddressInteractor
    ) {
        return ImmutableMap.of(
                GREETING, greetingsInteractor,
                TOP, listTopOptionsInterator,
                CREATE, askFullNameInteractor,
                CREATE_FULLNAME, askFullNameInteractor,
                CREATE_ADDRESS, askForEmailAddressInteractor
        );
    }

    public FSM<States, Conditions> fsm(States initialState) {
        return new FSM<>(initialState, ImmutableList.of(
                new FSM.Transition<>(JOINED, Collections.emptySet(), GREETING),
                new FSM.Transition<>(GREETING, Collections.singleton(Conditions.TOP), TOP),
                //new FSM.Transition<>(CREATE, Collections.emptySet(), CREATE_FULLNAME),
                new FSM.Transition<>(CREATE, Collections.emptySet(), CREATE_ADDRESS),
                new FSM.Transition<>(CREATE_FULLNAME, Collections.emptySet(), CREATE_ADDRESS),
                new FSM.Transition<>(CREATE_ADDRESS, Collections.emptySet(), GREETING)
        ));
    }

    @PostConstruct
    private void postConstruct() {
        pollingService.scheduleAtFixedRate(() -> {
            ResponseEntity<TelegramResponse> response = getRestTemplate()
                    .getForEntity(API_PATH + API_KEY + "/getUpdates?offset=" + lastPollingOffset.get(), TelegramResponse.class);
            long lastProcessed = processResponse(response.getBody());
            lastPollingOffset.set(lastProcessed + 1);
        }, 0, 1, TimeUnit.SECONDS);
    }

    long processResponse(TelegramResponse response) {
        long lastProcessed = lastPollingOffset.get();
        for (TelegramResponse.Payload payload : response.getResult()) {
            lastProcessed = payload.getUpdate_id();
            processMessage(payload.getMessage());
        }
        return lastProcessed;
    }

    void processMessage(TelegramResponse.Message message) {
        Long chatId = message.getChat().getId();

        States currentState = Try.of(() -> cache.get(chatId, () -> JOINED)).getOrElse(JOINED);
        String text = message.getText();
        States nextState;
        if (currentState != CREATE) {
            if (text.equals("/start")) {
                nextState = GREETING;
            } else {
                nextState = Try.of(() -> stepMapping.get(Integer.valueOf(text))).getOrElse(cache.getIfPresent(chatId));
            }
        } else {
            nextState = fsm(currentState).apply(Collections.emptySet()).get();
        }
        cache.put(chatId, nextState);


        String response = mapping.get(nextState).execute(text);
        getRestTemplate().getForObject(API_PATH + API_KEY + "/sendMessage?chat_id={0}&text={1}", String.class, chatId, response);
    }

    public static void main(String[] args) {
        SpringApplication.run(ChatbotApplication.class, args);
    }
}
