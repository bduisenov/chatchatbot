package de.hackday.usecase;

import de.hackday.usecase.gateway.StatementsGateway;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class AskFullNameInteractor implements UseCase<String, String> {

    @Resource
    private StatementsGateway statementsGateway;

    @Override public String execute(String val) {
        return statementsGateway.getFullName();
    }
}
