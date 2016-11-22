package de.hackday.usecase;

public interface UseCase <T, R> {

    R execute(T val);

}
