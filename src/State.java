import java.util.*;

class State {
    public int id; // state number
    public boolean isAccept = false;

    // insaan wali transition
    public char symbol;
    public State transitionState;

    // this is for NFA lambda transitions
    public List<State> lambdaTransition = new ArrayList<>();

    public State(int id) {
        this.id = id;
    }

    public void addLambdaTransition(State state) {
        lambdaTransition.add(state);
    }

    public String toString() {
        return "State " + id + (isAccept ? " (Accept)" : "");
    }
}
