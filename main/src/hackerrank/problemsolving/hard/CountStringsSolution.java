package hackerrank.problemsolving.hard;

import java.io.*;
import java.lang.reflect.Array;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

public class CountStringsSolution {

    // *** SOLUTION CONSTANTS ***

    static final long RESULT_CUT_LIMIT = (long) Math.pow(10, 9);

    static class Pair <T, K> {
        private T key;
        private K value;

        public Pair(T key, K value) {
            this.key = key;
            this.value = value;
        }

        public T getKey() {
            return key;
        }

        public K getValue() {
            return value;
        }
    }

    // *** MATH UTILS ***

    static class MatrixUtil {

        static BigInteger[][] squareMatrixPower(BigInteger[][] base, int pow, int digits) {
            int size = base.length;
            BigInteger[][] ans = new BigInteger[size][size];
            // generate identity matrix
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    ans[i][j] = (j == i) ? BigInteger.ONE : BigInteger.ZERO;
                }
            }

            // binary exponentiation
            while (pow != 0) {
                if ((pow & 1) != 0) ans = squareMatrixMultiply(ans, base, digits);

                base = squareMatrixMultiply(base, base, digits);

                pow >>= 1;
            }

            return ans;
        }

        static BigInteger[][] squareMatrixMultiply(BigInteger[][] m, BigInteger[][] m2, int digits) {
            int size = m.length;
            BigInteger[][] ans = new BigInteger[size][size];

            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    ans[i][j] = BigInteger.ZERO;
                    for (int k = 0; k < size; k++) {
                        ans[i][j] = ans[i][j].add(m[i][k].multiply(m2[k][j]));
                    }
                }
            }

            if (digits != 0) {
                BigInteger limit = BigInteger.valueOf(10).pow(digits);
                for (int i = 0; i < size; i++) {
                    for (int j = 0; j < size; j++) {
                        if(ans[i][j].compareTo(limit) > 0){
                            ans[i][j] = ans[i][j].mod(limit);//.add(BigInteger.valueOf(7));
                        }
                    }
                }
            }
            return ans;
        }

        public static <T> T[][] createIdentityMatrix(int stateCount, T identity, Class<T> clazz) {
            Class<?> oneDimArrayClazz = Array.newInstance(clazz, 0).getClass();
            T[][] matrix = (T[][]) Array.newInstance(oneDimArrayClazz, stateCount);
            for (int i = 0; i < stateCount; i++) {
                matrix[i] = (T[]) Array.newInstance(clazz, stateCount);
                for (int j = 0; j < stateCount; j++) {
                    matrix[i][j] = identity;
                }
            }
            return matrix;
        }

    }

    // *** REGEX ****

    static class RegexParser {

        // ** NODE DEFINITIONS ** //

        static abstract class Node {
            Node[] nodes;

            public Node(Node... nodes) {
                this.nodes = nodes;
            }

            public abstract Set<String> getAllPossibleStrings(int length);

            public List<Node> getNodes() {
                return Collections.unmodifiableList(Arrays.asList(nodes));
            }
        }

        static class StaticNode extends Node {
            String exp;

            public StaticNode(String exp) {
                super();
                this.exp = exp;
            }

            public Set<String> getAllPossibleStrings(int length) {
                HashSet<String> set = new HashSet<>();
                set.add(exp);
                return set;
            }

            public String getExp() {
                return exp;
            }
        }

        static class DecisionNode extends Node {
            public DecisionNode(Node... nodes) {
                super(nodes);
            }

            public Set<String> getAllPossibleStrings(int length) {
                HashSet<String> set = new HashSet<>();
                Arrays.stream(nodes)
                        .map(node -> node.getAllPossibleStrings(length))
                        .forEach(set::addAll);
                return set;
            }
        }

        static class RepeatNode extends Node {
            public RepeatNode(Node... nodes) {
                super(nodes);
            }

            public Set<String> getAllPossibleStrings(int length) {
                Node repeatedNode = nodes[0];
                HashSet<String> result = new HashSet<>();
                result.add("");
                generateExtensions(result, repeatedNode.getAllPossibleStrings(length), length);
                return result;
            }

            private void generateExtensions(Set<String> results, Set<String> wordSet, int len) {
                if (wordSet.isEmpty()) {
                    return;
                }

                int prevSize, newSize;
                do {
                    Set<String> extendedResults = wordSet.stream()
                            .flatMap(word -> results.stream().map(r -> r + word))
                            .filter(r -> r.length() <= len)
                            .collect(Collectors.toSet());
                    prevSize = results.size();
                    results.addAll(extendedResults);
                    newSize = results.size();
                } while (prevSize != newSize);
            }
        }

        static class ConcatNode extends Node {
            public ConcatNode(Node... nodes) {
                super(nodes);
            }

            public Set<String> getAllPossibleStrings(int length) {
                List<Set<String>> wordSets = Arrays.stream(nodes)
                        .parallel()
                        .map(node -> node.getAllPossibleStrings(length))
                        .collect(Collectors.toList());
                Set<String> results = new HashSet<>(wordSets.get(0));
                for (int nodeIdx = 1; nodeIdx < nodes.length; nodeIdx++) {
                    Set<String> wordSet = wordSets.get(nodeIdx);
                    results = results.stream()
                            .flatMap(curResult -> wordSet.stream()
                                    .map(word -> curResult + word)
                                    .filter(created -> created.length() <= length))
                            .collect(Collectors.toSet());
                }
                return results;
            }
        }


    // ** OPERATION TREE BUILDING ** //

        public Node buildOperationTree(String exp) {
            int max = getMaxLevel(exp);
            if (max == 0) {
                return new StaticNode(exp);
            }
            Node root = getOperationOnFirstLevel(exp);
            return root;
        }

        private Node getOperationOnFirstLevel(String exp) {
            if (isExpGrouped(exp) && exp.endsWith("*)")) {
                exp = removeParenthesis(exp);
                Node operationOnFirstLevel = getOperationOnFirstLevel(exp.substring(0, exp.length() - 1));
                if (operationOnFirstLevel instanceof RepeatNode) {
                    return operationOnFirstLevel;
                } else {
                    return new RepeatNode(operationOnFirstLevel);
                }
            }
            if (isExpGrouped(exp)) {
                exp = removeParenthesis(exp);
                int index = charIndexOnZeroLevel(exp, '|');
                if (index != -1) {
                    return new DecisionNode(
                            getOperationOnFirstLevel(exp.substring(0, index)),
                            getOperationOnFirstLevel(exp.substring(index + 1))
                    );
                } else {
                    Optional<Pair<Integer, Integer>> groupIndexes = getFirstOuterGroupIndexes(exp);
                    if (groupIndexes.isPresent()) {
                        Pair<Integer, Integer> indexes = groupIndexes.get();
                        List<Node> concatNodes = new LinkedList<>();
                        if (indexes.getKey() != 0) {
                            String subExp = exp.substring(0, indexes.getKey()).trim();
                            if (!subExp.isEmpty()) concatNodes.add(getOperationOnFirstLevel(subExp));
                        }
                        String groupExp = exp.substring(indexes.getKey(), indexes.getValue() + 1);
                        concatNodes.add(getOperationOnFirstLevel(groupExp));
                        if (indexes.getValue() != exp.length() - 1) {
                            String subExp = exp.substring(indexes.getValue() + 1).trim();
                            if (!subExp.isEmpty()) concatNodes.add(getOperationOnFirstLevel(subExp));
                        }
                        return new ConcatNode(concatNodes.toArray(new Node[concatNodes.size()]));
                    } else {
                        return new StaticNode(exp);
                    }
                }
            }
            if (getMaxLevel(exp) != 0) {
                if (isExpGrouped(exp)) {
                    return getOperationOnFirstLevel(exp);
                } else {
                    throw new IllegalStateException("Parse Error 1");
                }
            } else {
                return new StaticNode(exp);
            }
        }

        private int charIndexOnZeroLevel(String exp, char opChar) {
            int level = 0;
            for (int i = 0; i < exp.length(); i++) {
                char cur = exp.charAt(i);
                if (cur == '(') {
                    level++;
                } else if (cur == ')') {
                    level--;
                } else if (cur == opChar && level == 0) {
                    return i;
                }
            }
            return -1;
        }

        private boolean isExpGrouped(String exp) {
            if (exp.startsWith("(") && exp.endsWith(")")) {
                int level = 0;
                for (int i = 1; i < exp.length() - 1; i++) {
                    char cur = exp.charAt(i);
                    if (cur == '(') {
                        level++;
                    } else if (cur == ')') {
                        level--;
                        if (level == -1) {
                            return false;
                        }
                    }
                }
                return true;
            } else {
                return false;
            }
        }

        private Optional<Pair<Integer, Integer>> getFirstOuterGroupIndexes(String exp) {
            int level = 0;
            int start = -1;
            int end = -1;
            for (int i = 0; i < exp.length(); i++) {
                char cur = exp.charAt(i);
                if (cur == '(') {
                    if (level == 0 && start == -1) {
                        start = i;
                    }
                    level++;
                } else if (cur == ')') {
                    level--;
                    if (level == 0 && end == -1) {
                        end = i;
                    }
                }
            }
            return (start == -1 || end == -1) ? Optional.empty() : Optional.of(new Pair<>(start, end));
        }

        private String removeParenthesis(String exp) {
            return exp.substring(1, exp.length() - 1);
        }

        private int getMaxLevel(String exp) {
            int maxLevel = 0;
            int level = 0;
            for (char c : exp.toCharArray()) {
                if (c == '(') {
                    level++;
                    if (maxLevel < level) {
                        maxLevel = level;
                    }
                } else if (c == ')') {
                    level--;
                }
            }
            return maxLevel;
        }

    }

    // *** AUTOMATONS ****

    static abstract class Automaton {

        public static final State UNHANDLED_INPUT_STATE = new State(-1, null);

        static class State {

            private static long NEXT_STATE_ID = 0;

            private final long id;
            private final Object metaData;

            public State() {
                this.id = getNextId();
                this.metaData = null;
            }

            public State(Object metaData) {
                this.metaData = metaData;
                this.id = getNextId();
            }

            State(long id, Object metaData) {
                this.id = id;
                this.metaData = metaData;
            }

            private synchronized static long getNextId() {
                return NEXT_STATE_ID++;
            }

            public long getId() {
                return id;
            }

            public Object getMetaData() {
                return metaData;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o)
                    return true;
                if (o == null || getClass() != o.getClass())
                    return false;
                State state = (State) o;
                return id == state.id;
            }

            @Override
            public int hashCode() {

                return Objects.hash(id);
            }

            @Override
            public String toString() {
                return String.valueOf(id);
            }
        }

        static class Transition {
            private final State from;
            private final State to;
            private final char input;

            public Transition(State from, State to, char input) {
                this.from = from;
                this.to = to;
                this.input = input;
            }

            public State getFrom() {
                return from;
            }

            public State getTo() {
                return to;
            }

            public char getInput() {
                return input;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o)
                    return true;
                if (o == null || getClass() != o.getClass())
                    return false;
                Transition that = (Transition) o;
                return input == that.input && Objects.equals(from, that.from) && Objects.equals(to, that.to);
            }

            @Override
            public int hashCode() {

                return Objects.hash(from, to, input);
            }

            @Override
            public String toString() {
                return "(" + getFrom().toString() + "--" + String.valueOf(getInput()) + "-->" + getTo() + ")";
            }
        }

        static class Config {
            protected State state;
            protected int inputIdx;

            public Config(State state, int inputIdx) {
                this.state = state;
                this.inputIdx = inputIdx;
            }

            public State getState() {
                return state;
            }

            public int getInputIdx() {
                return inputIdx;
            }
        }

        enum PROCESS_RESULT {
            ACCEPTED, NOT_ACCEPTED, UNHANDLED_INPUT
        }

        protected final List<State> states;
        protected final Set<Transition> transitions;
        protected final Set<Character> alphabet;
        protected final State startState;
        protected final Set<State> markedStates;
        protected final Map<State, List<Transition>> transitionMap;

        public Automaton(Collection<State> states, Collection<Transition> transitions, Collection<Character> alphabet, State startState,
                         Collection<State> markedStates) {
            this.states = states.stream().distinct().collect(Collectors.toList());
            this.transitions = new HashSet<>(transitions);
            this.alphabet = new HashSet<>(alphabet);
            this.startState = startState;
            this.markedStates = new HashSet<>(markedStates);
            this.transitionMap = createTransitionMap(transitions);
        }

        public abstract long countAcceptedInputs(int inputLength);

        public abstract PROCESS_RESULT inputAccepted(CharSequence input);

        public List<State> getStates() {
            return Collections.unmodifiableList(states);
        }

        public Set<Transition> getTransitions() {
            return Collections.unmodifiableSet(transitions);
        }

        public Set<Character> getAlphabet() {
            return Collections.unmodifiableSet(alphabet);
        }

        public State getStartState() {
            return startState;
        }

        public Set<State> getMarkedStates() {
            return Collections.unmodifiableSet(markedStates);
        }

        public String dump() {
            return dumpStates(states) + "\n" +
                    dumpTransitions(transitions) + "\n" +
                    dumpStates(Collections.singletonList(startState)) + "\n" +
                    dumpStates(markedStates);
        }

        protected String dumpTransitions(Collection<? extends Transition> transitions) {
            return transitions.stream().map(Transition::toString).collect(Collectors.joining(", "));
        }

        protected String dumpStates(Collection<? extends State> states) {
            return states.stream()
                    .map(State::toString)
                    .collect(Collectors.joining(", "));
        }

        protected Map<State, List<Transition>> getTransitionMap() {
            return transitionMap;
        }

        private Map<State, List<Transition>> createTransitionMap(Collection<Transition> transitions) {
            Map<State, List<Transition>> map = transitions.stream().collect(Collectors.groupingBy(Transition::getFrom, Collectors.toList()));
            states.stream().forEach(state -> map.putIfAbsent(state, Collections.emptyList()));
            return map;
        }
    }

    static class NfaAutomaton extends Automaton {

        public static final State ARBITRARY_START_STATE = new State(-2, null);
        public static final char EMPTY_INPUT = '\u19C9';

        static class MultiConfig {
            private final Set<State> states;
            private final int inputIdx;

            public MultiConfig(Collection<State> state, int inputIdx) {
                this.states = new HashSet<>(state);
                this.inputIdx = inputIdx;
            }

            public Set<State> getStates() {
                return Collections.unmodifiableSet(states);
            }

            public int getInputIdx() {
                return inputIdx;
            }
        }

        public NfaAutomaton(List<State> states, List<Transition> transitions, List<Character> alphabet) {
            super(states, transitions, alphabet, ARBITRARY_START_STATE, Collections.emptyList());
        }

        public NfaAutomaton(Collection<State> states, Collection<Transition> transitions, Collection<Character> alphabet, State startState,
                            Collection<State> markedStates) {
            super(states, transitions, alphabet, startState, markedStates);
        }

        public Set<Automaton.State> getAllEmptyReachable(Automaton.State firstState) {
            Set<Automaton.State> reachableStates = new HashSet<>();
            reachableStates.add(firstState);
            int prevSize;
            Set<Automaton.State> discoveredInLastIteration = new HashSet<>(Collections.singletonList(firstState));
            do {
                prevSize = reachableStates.size();
                Set<Automaton.State> finalDiscoveredInLastIteration = discoveredInLastIteration;
                Set<Automaton.State> currentDiscovered = this.transitions.stream()
                        .filter(t -> t.getInput() == NfaAutomaton.EMPTY_INPUT && finalDiscoveredInLastIteration.contains(t.getFrom())).map(t -> t.getTo())
                        .collect(Collectors.toSet());
                reachableStates.addAll(currentDiscovered);
                discoveredInLastIteration = currentDiscovered;
            } while (prevSize != reachableStates.size());
            return reachableStates;
        }

        @Override
        public long countAcceptedInputs(int inputLength) {
            long result = 0;
            Stack<MultiConfig> configs = new Stack<>();
            configs.push(new MultiConfig(getAllEmptyReachable(startState), 0));
            while (!configs.empty()) {
                MultiConfig config = configs.pop();
                Set<State> curStates = config.getStates();
                List<Transition> availableTransitions = curStates.stream()
                        .flatMap(state -> transitionMap.get(state).stream())
                        .collect(Collectors.toList());

                // Exit condition
                boolean inputHasNext = config.inputIdx < inputLength;
                if (!inputHasNext && config.getStates().stream().anyMatch(markedStates::contains)) {
                    result++;
                }

                // Process input
                if (inputHasNext) {
                    for (char c : alphabet) {
                        Set<State> destStates = availableTransitions.stream().filter(t -> t.getInput() == c)
                                .map(t -> t.getTo())
                                .flatMap(state -> getAllEmptyReachable(state).stream())
                                .collect(Collectors.toSet());
                        if (!destStates.isEmpty()) {
                            configs.push(new MultiConfig(destStates, config.getInputIdx() + 1));
                        }
                    }
                }
            }
            return result;
        }

        public long countAcceptedInputs2(int inputLength) {
            long result = 0;
            Stack<Config> configs = new Stack<>();
            configs.push(new Config(startState, 0));
            while (!configs.empty()) {
                Config config = configs.pop();
                State curState = config.getState();
                List<Transition> availableTransitions = transitionMap.get(curState);

                // Handle EMPTY
                availableTransitions.stream().filter(t -> t.getInput() == EMPTY_INPUT).map(t -> new Config(t.getTo(), config.getInputIdx()))
                        .forEach(configs::push);

                // Exit condition
                boolean inputHasNext = config.inputIdx < inputLength;
                if (!inputHasNext && markedStates.contains(config.state)) {
                    result++;
                }

                // Process input
                if (inputHasNext) {
                    availableTransitions.stream().filter(t -> t.getInput() != EMPTY_INPUT).map(t -> new Config(t.getTo(), config.getInputIdx() + 1))
                            .forEach(configs::push);
                }
            }
            return result;
        }

        public PROCESS_RESULT inputAccepted(CharSequence input) {
            List<State> resultStates = processInput(input, true);
            if (resultStates.isEmpty()) {
                return PROCESS_RESULT.UNHANDLED_INPUT;
            } else {
                return resultStates.stream().anyMatch(markedStates::contains) ? PROCESS_RESULT.ACCEPTED : PROCESS_RESULT.NOT_ACCEPTED;
            }
        }

        public List<State> processInput(CharSequence input, boolean firstMarked) {
            Stack<Config> configs = new Stack<>();
            getAllEmptyReachable(startState).stream().map(state -> new Config(state, 0)).forEach(configs::push);
            List<State> fullyProcessedStates = new ArrayList<>();
            while (!configs.empty()) {
                Config config = configs.pop();
                State curState = config.getState();
                List<Transition> availableTransitions = transitionMap.get(curState);

                // Process input & Checking if input is processed
                boolean inputHasNext = config.inputIdx < input.length();
                if (inputHasNext) {
                    availableTransitions.stream().filter(t -> t.getInput() == input.charAt(config.inputIdx))
                            .flatMap(t -> getAllEmptyReachable(t.getTo()).stream()).map(destState -> new Config(destState, config.getInputIdx() + 1))
                            .forEach(configs::push);
                } else {
                    if (markedStates.contains(curState) && firstMarked) {
                        return Collections.singletonList(curState);
                    }
                    fullyProcessedStates.add(curState);
                }
            }
            return fullyProcessedStates;
        }

    }

    static class DfaAutomaton extends Automaton {

        static class MutableConfig extends Config {

            public MutableConfig(State state, int inputIdx) {
                super(state, inputIdx);
            }

            public void step(Transition transition) {
                state = transition.getTo();
                inputIdx++;
            }

        }

        public DfaAutomaton(Collection<State> states, Collection<Transition> transitions, Collection<Character> alphabet, State startState,
                            Collection<State> markedStates) {
            super(states, transitions, alphabet, startState, markedStates);
        }

        @Override
        public long countAcceptedInputs(int inputLength) {
            long result = 0;
            Stack<MutableConfig> configs = new Stack<>();
            configs.push(new MutableConfig(startState, 0));
            MutableConfig curConfig;
            boolean hasMoreInput;
            while (!configs.empty()) {
                curConfig = configs.peek();
                hasMoreInput = curConfig.inputIdx < inputLength;
                if (!hasMoreInput) {
                    configs.pop();
                    if (markedStates.contains(curConfig.getState())) {
                        result++;
                    }
                } else {
                    List<Transition> availableTransitions = transitionMap.get(curConfig.getState());
                    if (availableTransitions.size() == 1) {
                        curConfig.step(availableTransitions.get(0));
                    } else {
                        configs.pop();
                        int finalInputIdx = curConfig.inputIdx;
                        availableTransitions.stream()
                                .map(Transition::getTo)
                                .filter(state -> state != UNHANDLED_INPUT_STATE)
                                .map(state -> new MutableConfig(state, finalInputIdx + 1))
                                .forEach(configs::push);
                    }
                }
            }
            return result;
        }

        @Override
        public PROCESS_RESULT inputAccepted(CharSequence input) {
            State resultState = processInput(startState, input);
            if (resultState == UNHANDLED_INPUT_STATE) {
                return PROCESS_RESULT.UNHANDLED_INPUT;
            }
            return markedStates.contains(resultState) ? PROCESS_RESULT.ACCEPTED : PROCESS_RESULT.NOT_ACCEPTED;
        }

        public State processInput(State state, CharSequence input) {
            State curState = state;
            for (int inputIdx = 0; inputIdx < input.length(); inputIdx++) {
                final int finalInputIdx = inputIdx;
                List<Transition> availableTransitions = transitionMap.get(curState);
                Optional<Transition> transitionForInput = availableTransitions.stream().filter(t -> t.getInput() == input.charAt(finalInputIdx)).findFirst();
                curState = transitionForInput.map(Transition::getTo).orElse(UNHANDLED_INPUT_STATE);
            }
            return curState;
        }

        public BigInteger[][] transferMatrix() {
            int stateCount = states.size();
            BigInteger identity = BigInteger.ZERO;
            BigInteger[][] matrix = MatrixUtil.createIdentityMatrix(stateCount, identity, BigInteger.class);

            for (int fromIDx = 0; fromIDx < stateCount; fromIDx++) {
                for (Transition transition : transitionMap.get(states.get(fromIDx))) {
                    int toIdx = states.indexOf(transition.getTo());
                    matrix[fromIDx][toIdx] = matrix[fromIDx][toIdx].add(BigInteger.ONE);
                }
            }
            return matrix;
        }
    }

    static class NfaToDfaConverter {

        private final boolean useUnhandledInputState;

        public NfaToDfaConverter(boolean useUnhandledInputState) {
            this.useUnhandledInputState = useUnhandledInputState;
        }

        public DfaAutomaton convert(NfaAutomaton nfa) {
            Map<Set<Automaton.State>, Automaton.State> dfaStates = new HashMap<>();
            List<Automaton.Transition> dfaTransitions = new LinkedList<>();

            Set<Character> nfaAlphabet = nfa.getAlphabet();
            Map<Automaton.State, List<NfaAutomaton.Transition>> nfaTransitionMap =
                    nfa.getTransitionMap();

            dfaStates.put(
                    nfa.getAllEmptyReachable(nfa.getStartState()),
                    new Automaton.State()
            );

            Set<Automaton.State> processedDfaStates = new HashSet<>();
            do {
                Map.Entry<Set<Automaton.State>, Automaton.State> currentStateEntry =
                        dfaStates.entrySet().stream()
                                .filter(stateEntry -> !processedDfaStates.contains(stateEntry.getValue()))
                                .findFirst().orElseThrow(IllegalStateException::new);
                Automaton.State currentState = currentStateEntry.getValue();
                Set<Automaton.State> currentAffectedStates = currentStateEntry.getKey();
                for (char inputChar : nfaAlphabet) {
                    List<Automaton.Transition> nfaTransitionsForInput = currentAffectedStates.stream()
                            .flatMap(nfaState -> nfaTransitionMap.get(nfaState).stream())
                            .filter(t -> t.getInput() == inputChar)
                            .collect(Collectors.toList());
                    if (nfaTransitionsForInput.isEmpty()) {
                        if (useUnhandledInputState) {
                            dfaTransitions.add(new Automaton.Transition(
                                    currentState,
                                    Automaton.UNHANDLED_INPUT_STATE,
                                    inputChar
                            ));
                        }
                    } else {
                        Set<Automaton.State> reachableNfaStates = nfaTransitionsForInput.stream()
                                .map(Automaton.Transition::getTo)
                                .flatMap(nfaState -> nfa.getAllEmptyReachable(nfaState).stream())
                                .collect(Collectors.toSet());
                        Automaton.State targetDfaState = dfaStates.get(reachableNfaStates);
                        if (targetDfaState == null) {
                            targetDfaState = new Automaton.State();
                            dfaStates.put(reachableNfaStates, targetDfaState);
                        }
                        dfaTransitions.add(new Automaton.Transition(currentState, targetDfaState, inputChar));
                    }
                }
                processedDfaStates.add(currentState);
            } while (processedDfaStates.size() != dfaStates.size());

            if (useUnhandledInputState) {
                dfaStates.put(
                        new HashSet<>(Collections.singletonList(Automaton.UNHANDLED_INPUT_STATE)),
                        Automaton.UNHANDLED_INPUT_STATE);
            }

            List<Automaton.State> dfaMarkedStates = dfaStates.entrySet().stream()
                    .filter(entry -> entry.getKey().stream().anyMatch(state -> nfa.getMarkedStates().contains(state)))
                    .map(Map.Entry::getValue)
                    .collect(Collectors.toList());

            return new DfaAutomaton(
                    dfaStates.values(),
                    dfaTransitions,
                    nfaAlphabet,
                    dfaStates.get(nfa.getAllEmptyReachable(nfa.getStartState())),
                    dfaMarkedStates
            );
        }
    }

    static class RegexTreeToNfaConverter {

        public NfaAutomaton convert(RegexParser.Node node, List<Character> alphabet) {
            NfaAutomaton converted = null;

            if (node instanceof RegexParser.StaticNode) {
                RegexParser.StaticNode castedNode = (RegexParser.StaticNode) node;
                converted = convert(castedNode, alphabet);
            }
            if (node instanceof RegexParser.DecisionNode) {
                RegexParser.DecisionNode castedNode = (RegexParser.DecisionNode) node;
                converted = convert(castedNode, alphabet);
            }
            if (node instanceof RegexParser.RepeatNode) {
                RegexParser.RepeatNode castedNode = (RegexParser.RepeatNode) node;
                converted = convert(castedNode, alphabet);
            }
            if (node instanceof RegexParser.ConcatNode) {
                RegexParser.ConcatNode castedNode = (RegexParser.ConcatNode) node;
                converted = convert(castedNode, alphabet);
            }
            if (converted != null) {
                return converted;
            } else {
                throw new IllegalArgumentException(
                        String.format("The given node type is not handled! (%s)", node.getClass().getSimpleName()));
            }
        }

        private NfaAutomaton convert(
                RegexParser.StaticNode node, List<Character> alphabet
        ) {
            List<NfaAutomaton.State> extendedStates = new ArrayList<>();
            List<NfaAutomaton.Transition> extendedTransitions = new ArrayList<>();
            extendedStates.add(new NfaAutomaton.State(node));
            for (char c : node.getExp().toCharArray()) {
                extendedStates.add(new NfaAutomaton.State(node));
                extendedTransitions.add(new NfaAutomaton.Transition(
                        extendedStates.get(extendedStates.size() - 2),
                        extendedStates.get(extendedStates.size() - 1),
                        c
                ));
            }
            return new NfaAutomaton(
                    extendedStates,
                    extendedTransitions,
                    alphabet,
                    extendedStates.get(0),
                    Collections.singletonList(extendedStates.get(extendedStates.size() - 1))
            );
        }

        private NfaAutomaton convert(RegexParser.DecisionNode node, List<Character> alphabet) {
            List<NfaAutomaton> convertedOptionNfas =
                    node.getNodes().stream()
                            .map(optionNode -> convert(optionNode, alphabet))
                            .collect(Collectors.toList());
            // Create new local start and finish node and transitions for decision with EMPTY input
            NfaAutomaton.State decisionStartState = new NfaAutomaton.State(node);
            NfaAutomaton.State decisionFinishState = new NfaAutomaton.State(node);
            List<NfaAutomaton.Transition> optionStartTransitions = convertedOptionNfas.stream()
                    .map(nfa -> nfa.getStartState())
                    .map(optionStartState ->
                            new NfaAutomaton.Transition(
                                    decisionStartState,
                                    optionStartState,
                                    NfaAutomaton.EMPTY_INPUT)
                    ).collect(Collectors.toList());
            List<NfaAutomaton.Transition> optionFinishTransitions = convertedOptionNfas.stream()
                    .flatMap(nfa -> nfa.getMarkedStates().stream())
                    .map(candidateFinishState ->
                            new NfaAutomaton.Transition(
                                    candidateFinishState,
                                    decisionFinishState,
                                    NfaAutomaton.EMPTY_INPUT)
                    ).collect(Collectors.toList());
            // Create a new NFA incl. all states and transitions
            List<NfaAutomaton.State> extendedStates = new ArrayList<>();
            convertedOptionNfas.stream().map(Automaton::getStates).forEach(extendedStates::addAll);
            extendedStates.add(decisionStartState);
            extendedStates.add(decisionFinishState);
            List<NfaAutomaton.Transition> extendedTransitions = new ArrayList<>();
            convertedOptionNfas.stream().map(Automaton::getTransitions).forEach(extendedTransitions::addAll);
            extendedTransitions.addAll(optionStartTransitions);
            extendedTransitions.addAll(optionFinishTransitions);

            return new NfaAutomaton(
                    extendedStates,
                    extendedTransitions,
                    alphabet,
                    decisionStartState,
                    Collections.singletonList(decisionFinishState)
            );
        }

        private NfaAutomaton convert(
                RegexParser.RepeatNode node,
                List<Character> alphabet
        ) {
            NfaAutomaton convertedChildNode = convert(node.getNodes().get(0), alphabet);
            NfaAutomaton.State repetitionStart = new NfaAutomaton.State(node);
            NfaAutomaton.State repetitionFinish = new NfaAutomaton.State(node);
            NfaAutomaton.Transition toChildStartTransition = new NfaAutomaton.Transition(
                    repetitionStart,
                    convertedChildNode.getStartState(),
                    NfaAutomaton.EMPTY_INPUT
            );
            List<NfaAutomaton.Transition> returnFromChildTransitions = convertedChildNode.getMarkedStates()
                    .stream()
                    .map(convertedMarkedState -> new NfaAutomaton.Transition(
                            convertedMarkedState,
                            repetitionStart,
                            NfaAutomaton.EMPTY_INPUT
                    )).collect(Collectors.toList());
            NfaAutomaton.Transition finishRepetitionTransition = new NfaAutomaton.Transition(
                    repetitionStart,
                    repetitionFinish,
                    NfaAutomaton.EMPTY_INPUT
            );
            List<NfaAutomaton.State> extendedStates = new ArrayList<>(convertedChildNode.getStates());
            List<NfaAutomaton.Transition> extendedTransitions = new ArrayList<>(convertedChildNode.getTransitions());
            extendedStates.add(repetitionStart);
            extendedStates.add(repetitionFinish);
            extendedTransitions.add(toChildStartTransition);
            extendedTransitions.addAll(returnFromChildTransitions);
            extendedTransitions.add(finishRepetitionTransition);
            return new NfaAutomaton(
                    extendedStates,
                    extendedTransitions,
                    alphabet,
                    repetitionStart,
                    Collections.singletonList(repetitionFinish));
        }

        private NfaAutomaton convert(
                RegexParser.ConcatNode node,
                List<Character> alphabet
        ) {
            List<NfaAutomaton> convertedNfas =
                    node.getNodes().stream()
                            .map(optionNode -> convert(optionNode, alphabet))
                            .collect(Collectors.toList());
            List<NfaAutomaton.Transition> partConnectorTransitions = new ArrayList<>();
            for (int i = 1; i < convertedNfas.size(); i++) {
                NfaAutomaton currentNfa = convertedNfas.get(i);
                convertedNfas.get(i - 1).getMarkedStates().stream()
                        .map(convertedMarkedState -> new NfaAutomaton.Transition(
                                convertedMarkedState,
                                currentNfa.getStartState(),
                                NfaAutomaton.EMPTY_INPUT
                        )).forEach(partConnectorTransitions::add);
            }
            List<NfaAutomaton.State> extendedStates = convertedNfas.stream()
                    .flatMap(nfa -> nfa.getStates().stream())
                    .collect(Collectors.toList());
            List<NfaAutomaton.Transition> extendedTransitions = convertedNfas.stream()
                    .flatMap(nfa -> nfa.getTransitions().stream())
                    .collect(Collectors.toList());
            extendedTransitions.addAll(partConnectorTransitions);
            return new NfaAutomaton(
                    extendedStates,
                    extendedTransitions,
                    alphabet,
                    convertedNfas.get(0).getStartState(),
                    convertedNfas.get(convertedNfas.size() - 1).getMarkedStates()
            );
        }
    }

    // ** SOLUTION ROOT ** //
    public static BigInteger countStrings(String exp, int stringLen) {
        RegexParser.Node root = new RegexParser().buildOperationTree(exp);
        NfaAutomaton nfa = new RegexTreeToNfaConverter().convert(root, Arrays.asList('a', 'b'));
        DfaAutomaton dfa = new NfaToDfaConverter(false).convert(nfa);
        //System.out.println(dfa.dump());
        BigInteger result;
        result = countStringsByTransitionMatrix(dfa, stringLen);
        return result;
    }

    public static BigInteger countStringsOld(String exp, int stringLen) {
        RegexParser.Node root = new RegexParser().buildOperationTree(exp);
        NfaAutomaton nfa = new RegexTreeToNfaConverter().convert(root, Arrays.asList('a', 'b'));
        DfaAutomaton dfa = new NfaToDfaConverter(false).convert(nfa);
        return BigInteger.valueOf(dfa.countAcceptedInputs(stringLen));
    }

    public static BigInteger countStringsOld2(String exp, int stringLen) {
        RegexParser.Node root = new RegexParser().buildOperationTree(exp);
        NfaAutomaton nfa = new RegexTreeToNfaConverter().convert(root, Arrays.asList('a', 'b'));
        return BigInteger.valueOf(nfa.countAcceptedInputs(stringLen));
    }

    private static BigInteger countStringsByTransitionMatrix(DfaAutomaton dfaAutomaton, int length) {
        BigInteger[][] transferMatrix = dfaAutomaton.transferMatrix();
        BigInteger[][] poweredMatrix = MatrixUtil.squareMatrixPower(transferMatrix, length, 11);
        int startIdx = dfaAutomaton.getStates().indexOf(dfaAutomaton.getStartState());
        BigInteger result = dfaAutomaton.getMarkedStates().stream()
                .map(state -> dfaAutomaton.getStates().indexOf(state))
                .map(stateIdx -> poweredMatrix[startIdx][stateIdx])
                .reduce(BigInteger.ZERO, BigInteger::add);
        return result;
    }

    // ** PROVIDED CODE ** //

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));
        int t = Integer.parseInt(scanner.nextLine().trim());
        List<Pair<String, Integer>> tasks = new ArrayList<>(t);

        for (int tItr = 0; tItr < t; tItr++) {
            String[] rl = scanner.nextLine().split(" ");
            String r = rl[0];
            int l = Integer.parseInt(rl[1].trim());
            tasks.add(new Pair<>(r, l));
        }

        List<Long> results = tasks.stream()
                //.parallel()
                .map(task -> countStrings(task.getKey(), task.getValue()))
                .map(BigInteger::longValue)
                .collect(Collectors.toList());
        for (Long result : results) {
            if (result >= RESULT_CUT_LIMIT) {
                result = (result % RESULT_CUT_LIMIT) + 7;
            }
            bufferedWriter.write(result.toString());
            bufferedWriter.newLine();
        }
        bufferedWriter.close();
    }

    // *** TESTING PURPOSE ***

    private static BigInteger countStringsBenchmark(String expression, int length) {
        long start = System.currentTimeMillis();
        BigInteger result = countStrings(expression, length);
        long elapsedMs = System.currentTimeMillis() - start;
        //System.out.println("RESULT: " + result + " - " + expression + " [" + length + "]");
        //System.out.println("Elapsed Time: " + elapsedMs + "ms\n");
        return result;
    }

}
