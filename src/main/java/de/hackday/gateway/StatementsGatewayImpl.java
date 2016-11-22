package de.hackday.gateway;

import de.hackday.usecase.gateway.StatementsGateway;
import javaslang.control.Try;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Scanner;

@Component class StatementsGatewayImpl implements StatementsGateway {

    @Override public String getGreeting() {
        return getText("greeting.txt");
    }

    @Override public String getTop() {
        return getText("top_offers.txt");
    }

    @Override
    public String getFullName() {
        return "What is your full name?";
    }

    @Override public String getAddress() {
        return "What is your email address?";
    }

    public String getText(String filename) {
        return Try.of(() -> new ClassPathResource("text/" + filename).getFile())
                .flatMapTry(file -> Try.of(() -> new Scanner(file).useDelimiter("\\A").next()))
                .getOrElse("Oops, something went wrong. Sorry");
    }

}
