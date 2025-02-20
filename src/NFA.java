import java.util.*;

class NFA {
    private int stateCounter = 0; // state number hold karta
    public State start;
    public State accept;

    public NFA(State start, State accept) {
        this.start = start;
        this.accept = accept;
    }

    public NFA(String RE) {
        String postfix = REHelper.infixToPostfix(RE);
        NFA nfa = build(postfix);
        this.start = nfa.start;
        this.accept = nfa.accept;
    }

    private NFA createBasicNFA(char c) {
        State start = new State(stateCounter++);
        State accept = new State(stateCounter++);
        accept.isAccept = true;

        start.symbol = c;
        start.transitionState = accept;

        return new NFA(start, accept);
    }

    public NFA build(String reg_exp) {
        Stack<NFA> stack = new Stack<>();

        for (int i = 0; i < reg_exp.length(); i++) {
            char c = reg_exp.charAt(i);
            switch (c) {

                case '*': {
                    // kleene closure ---> 4 lambda add karte
                    NFA nfa1 = stack.pop();
                    State start = new State(stateCounter++);
                    State accept = new State(stateCounter++);
                    accept.isAccept = true;

                    start.addLambdaTransition(nfa1.start);
                    start.addLambdaTransition(accept);
                    nfa1.accept.addLambdaTransition(nfa1.start);
                    nfa1.accept.addLambdaTransition(accept);
                    nfa1.accept.isAccept = false;

                    stack.push(new NFA(start, accept));
                }
                break;

                case '+': {
                    // add wala ---> 4 lambda add
                    NFA nfa1 = stack.pop();
                    State start = new State(stateCounter++);
                    State accept = new State(stateCounter++);
                    accept.isAccept = true;

                    start.addLambdaTransition(nfa1.start);
                    nfa1.accept.addLambdaTransition(nfa1.start);
                    nfa1.accept.addLambdaTransition(accept);
                    nfa1.accept.isAccept = false;

                    stack.push(new NFA(start, accept));
                }
                break;

                case '?': { // ew symbol
                    // sadness
                    NFA nfa1 = stack.pop();
                    State start = new State(stateCounter++);
                    State accept = new State(stateCounter++);
                    accept.isAccept = true;

                    start.addLambdaTransition(nfa1.start);
                    start.addLambdaTransition(accept);
                    nfa1.accept.addLambdaTransition(accept);
                    nfa1.accept.isAccept = false;

                    stack.push(new NFA(start, accept));
                }
                break;

                case '~': {
                    // concatenation ---> 1 lambda trans
                    NFA nfa2 = stack.pop();
                    NFA nfa1 = stack.pop();

                    nfa1.accept.isAccept = false;
                    nfa1.accept.addLambdaTransition(nfa2.start);

                    stack.push(new NFA(nfa1.start, nfa2.accept));
                }
                break;

                case '|': {
                    // ye + wala symbol (union)
                    NFA nfa2 = stack.pop();
                    NFA nfa1 = stack.pop();
                    State start = new State(stateCounter++);
                    State accept = new State(stateCounter++);
                    accept.isAccept = true;

                    start.addLambdaTransition(nfa1.start);
                    start.addLambdaTransition(nfa2.start);

                    nfa1.accept.isAccept = false;
                    nfa2.accept.isAccept = false;
                    nfa1.accept.addLambdaTransition(accept);
                    nfa2.accept.addLambdaTransition(accept);

                    stack.push(new NFA(start, accept));
                }
                break;

                default:
                    // character hai koi
                    stack.push(createBasicNFA(c));
                    break;
            }
        }
        return stack.pop();
    }

    public void printNFA() {
        if (start == null) {
            System.out.println("NFA is empty!");
            return;
        }

        // traverse the NFA - sab states ko queue main dalo
        Set<State> visited = new HashSet<>();
        Queue<State> queue = new LinkedList<>();
        Map<State, Integer> stateIds = new HashMap<>();

        queue.add(start);
        while (!queue.isEmpty()) {
            State current = queue.poll();
            if (visited.contains(current)) continue;

            // beautiful id for sab (Agar kisi ki nahi)
            stateIds.putIfAbsent(current, stateIds.size());
            visited.add(current);

            // agar agay states hai
            if (current.transitionState != null)
                queue.add(current.transitionState);
            queue.addAll(current.lambdaTransition);
        }

        // collect sabke transition symbols
        Set<Character> symbols = new TreeSet<>();
        for (State state : visited) {
            if (state.symbol != 0) symbols.add(state.symbol);
        }
        symbols.add('ε'); // Include epsilon transitions

        // Print header row
        System.out.println("\nNFA Transition Table:");
        System.out.printf("%-10s", "State");
        for (char c : symbols) {
            System.out.printf("%-12s", c);
        }
        System.out.println();

        // Print transitions for each state
        for (State state : visited) {
            String stateLabel = (state == accept ? "*" : "") + "q" + stateIds.get(state);
            System.out.printf("%-10s", stateLabel);

            for (char c : symbols) {
                List<String> nextStates = new ArrayList<>();

                if (c == 'ε') {
                    for (State epsilonState : state.lambdaTransition) {
                        nextStates.add("q" + stateIds.get(epsilonState));
                    }
                } else if (state.symbol == c && state.transitionState != null) {
                    nextStates.add("q" + stateIds.get(state.transitionState));
                }

                String nextStateString = nextStates.isEmpty() ? "-" : String.join(",", nextStates);
                System.out.printf("%-12s", nextStateString);
            }
            System.out.println();
        }
    }
}
