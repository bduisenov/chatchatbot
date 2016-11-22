package de.hackday.fsm;

import java.util.List;
import java.util.Set;

public class FSM<S extends Enum<S>, C extends Enum<C>> {

    public static class Transition<S, C> {

        private S from;

        private Set<C> conditions;

        private S to;

        public Transition(S from, Set<C> conditions, S to) {
            this.from = from;
            this.conditions = conditions;
            this.to = to;
        }
    }

    private List<Transition<S, C>> transitions;

    private S initialState;

    private S current;

    public FSM(S start, List<Transition<S, C>> transitions) {
        this.initialState = start;
        this.current = start;
        this.transitions = transitions;
    }

    public FSM<S, C> reset() {
        current = initialState;
        return this;
    }

    public S get() {
        return current;
    }

    public FSM<S, C> apply(Set<C> conditions) {
        S nextState = getNextState(conditions);
        if (nextState != null) {
            current = nextState;
        }
        return this;
    }

    private S getNextState(Set<C> conditions) {
        for (Transition<S, C> transition : transitions) {
            boolean currentStateMatches = transition.from.equals(current);
            boolean conditionsMatch = transition.conditions.equals(conditions);
            if (currentStateMatches && conditionsMatch) {
                return transition.to;
            }
        }
        return null;
    }

}
