import java.util.*;

class DFA {

    static class DFAState {
        public Set<State> nfa_states;     // kis nfa se bani hai
        public boolean isAccept;         // kya accepting nfa hai
        public Map<Character, DFAState> transitions;  // symbol -> next DFA state

        public DFAState(Set<State> nfaStates) {
            this.nfa_states = nfaStates;
            this.isAccept = false;
            this.transitions = new HashMap<>();
        }

        @Override
        public String toString() {
            return "DFAState(" + nfa_states.toString() + ")" + (isAccept ? "*" : "");
        }
    }

    private List<DFAState> states;  // is dfa ki states

    public Set<DFAState> getAcceptStates() {
        return acceptStates;
    }

    public DFAState getStartState() {
        return startState;
    }

    private DFAState startState;    // start state
    private Set<DFAState> acceptStates; // accept state

    public DFA(NFA nfa) {
        this.states = new ArrayList<>();
        this.acceptStates = new HashSet<>();
        convertFromNFA(nfa);
    }
    
    private void convertFromNFA(NFA nfa) {
        Queue<DFAState> queue = new LinkedList<>();

        Set<State> startSet = new HashSet<>();
        startSet.add(nfa.start);
        Set<State> startClosure = lambdaClosureOfSet(startSet);

        this.startState = new DFAState(startClosure);
        if (startClosure.contains(nfa.accept)) {
            this.startState.isAccept = true;
            this.acceptStates.add(startState);
        }
        states.add(startState);
        queue.add(startState);

        Set<Character> symbols = getAllSymbols(nfa.start);

        while (!queue.isEmpty()) {
            DFAState current = queue.poll();

            for (char c : symbols) {
                Set<State> targetNFAStates = move(current.nfa_states, c);
                if (!targetNFAStates.isEmpty()) {
                    Set<State> closure = lambdaClosureOfSet(targetNFAStates);

                    DFAState existing = findDFAState(states, closure);
                    if (existing == null) {
                        DFAState newDFA = new DFAState(closure);
                        if (closure.contains(nfa.accept)) {
                            newDFA.isAccept = true;
                            acceptStates.add(newDFA);
                        }
                        states.add(newDFA);
                        queue.add(newDFA);
                        current.transitions.put(c, newDFA);
                    } else {
                        current.transitions.put(c, existing);
                    }
                }
            }
        }
    }

    public List<DFAState> getStates() {
        return states;
    }

    // print dfa
    public void printDFA() {
        if (states.isEmpty()) {
            System.out.println("DFA is empty!");
            return;
        }

        // assign state numbers takay pata ho kon kon
        Map<DFAState, Integer> stateIds = new HashMap<>();
        int idCounter = 0;
        for (DFAState state : states) {
            stateIds.put(state, idCounter++);
        }

        // sigma nikalo
        Set<Character> symbols = new TreeSet<>();
        for (DFAState state : states) {
            symbols.addAll(state.transitions.keySet());
        }

        // header row ---> sab sigma print
        System.out.println("\nDFA Transition Table:");
        System.out.printf("%-10s", "State");
        for (char c : symbols) {
            System.out.printf("%-10s", c);
        }
        System.out.println();

        // har state ki row print
        for (DFAState state : states) {
            String stateLabel = (state.isAccept ? "*" : "") + "q" + stateIds.get(state);
            System.out.printf("%-10s", stateLabel);

            for (char c : symbols) {
                DFAState nextState = state.transitions.get(c);
                String nextLabel = (nextState != null) ? "q" + stateIds.get(nextState) : "-";
                System.out.printf("%-10s", nextLabel);
            }
            System.out.println();
        }
    }

    private static Set<Character> getAllSymbols(State nfaStart) {
        Set<Character> symbols = new HashSet<>();
        Queue<State> queue = new LinkedList<>();
        Set<State> visited = new HashSet<>();

        queue.add(nfaStart);
        while (!queue.isEmpty()) {
            State state = queue.poll();
            if (!visited.contains(state)) {
                visited.add(state);
                if (state.transitionState != null && state.symbol != 0) {
                    symbols.add(state.symbol);
                }
                queue.addAll(state.lambdaTransition);
                if (state.transitionState != null) {
                    queue.add(state.transitionState);
                }
            }
        }
        return symbols;
    }

    private static Set<State> lambdaClosureOfSet(Set<State> inputSet) {
        Set<State> result = new HashSet<>();
        for (State s : inputSet) {
            lambdaClosure(result, s);
        }
        return result;
    }

    //  lambda closure ITNA MUSHKIL
    private static void lambdaClosure(Set<State> states, State s) {
        if (s == null || states.contains(s)) return;
        states.add(s);
        for (State next : s.lambdaTransition) {
            lambdaClosure(states, next);
        }
    }

    //  from a set of states on symbol c, collect all next states
    private static Set<State> move(Set<State> states, char c) {
        Set<State> result = new HashSet<>();
        for (State s : states) {
            if (s.symbol == c && s.transitionState != null) {
                result.add(s.transitionState);
            }
        }
        return result;
    }

    //  find dfa state
    private static DFAState findDFAState(List<DFAState> list, Set<State> st) {
        for (DFAState d : list) {
            if (d.nfa_states.equals(st)) {
                return d;
            }
        }
        return null;
    }

    public boolean accepts(String input) {
        DFAState currentState = startState;
        for (char c : input.toCharArray()) {
            if (!currentState.transitions.containsKey(c)) {
                return false;
            }
            currentState = currentState.transitions.get(c);
        }
        return currentState.isAccept;
    }

}
