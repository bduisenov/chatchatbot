package de.hackday.usecase;

import de.hackday.usecase.gateway.StatementsGateway;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
class ListTopOptionsInterator implements UseCase<String, String> {

    @Resource
    private StatementsGateway statementsGateway;

    @Override public String execute(String val) {
        return statementsGateway.getTop();
    }
}
