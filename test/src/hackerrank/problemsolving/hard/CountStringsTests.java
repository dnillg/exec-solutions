package hackerrank.problemsolving.hard;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CountStringsTests {

    @Test
    public void flowTest() {
        String exp = "((((((ba)*)|b)|(((a|(b(a*)))(a|b))*))((b|(a*))b))|(b(((((aa)b)|a)*)|(a(a*)))))";
        int len = 8;
        CountStringsSolution.RegexParser.Node root = new CountStringsSolution.RegexParser().buildOperationTree(exp);
        CountStringsSolution.NfaAutomaton nfa = new CountStringsSolution.RegexTreeToNfaConverter().convert(root, Arrays.asList('a', 'b'));
        CountStringsSolution.DfaAutomaton dfa = new CountStringsSolution.NfaToDfaConverter(false).convert(nfa);
        BigInteger result = CountStringsSolution.countStrings(exp, len);
        BigInteger result2 = CountStringsSolution.countStringsOld(exp, len);
        BigInteger result3 = CountStringsSolution.countStringsOld2(exp, len);
        Set<String> allPossibleStrings = root.getAllPossibleStrings(len).stream().filter(s->s.length() == len).collect(Collectors.toSet());
        Optional<String> badString = allPossibleStrings.stream()
                .filter(str -> dfa.inputAccepted(str) != CountStringsSolution.Automaton.PROCESS_RESULT.ACCEPTED)
                .findFirst();
        System.out.println(badString);
        int result4 = allPossibleStrings.size();
        System.out.println(result + " " + result2 + " " + result3 + " " + result4);
    }

    @Test
    public void flowTest2() {
        String exp = "((((((ba)*)|b)|(((a|(b(a*)))(a|b))*))((b|(a*))b))|(b(((((aa)b)|a)*)|(a(a*)))))";
        int len = 8;
        List<String> input = getStrings(8);
        CountStringsSolution.RegexParser.Node root = new CountStringsSolution.RegexParser().buildOperationTree(exp);
        CountStringsSolution.NfaAutomaton nfa = new CountStringsSolution.RegexTreeToNfaConverter().convert(root, Arrays.asList('a', 'b'));
        CountStringsSolution.DfaAutomaton dfa = new CountStringsSolution.NfaToDfaConverter(false).convert(nfa);
        Set<String> allPossibleStrings = root.getAllPossibleStrings(len);
        for (String str : input) {
            assertEquals(nfa.inputAccepted(str) == CountStringsSolution.Automaton.PROCESS_RESULT.ACCEPTED, allPossibleStrings.contains(str));
        }

    }

    public List<String> getStrings(int len) {
        List result = new ArrayList<>((int) Math.pow(2, len));
        getStrings(result, "", len, Arrays.asList('a', 'b'));
        return result;
    }

    private void getStrings(List<String> result, String prefix, int len, Collection<Character> alphabet) {
        if (prefix.length() == len - 1) {
            for (Character c : alphabet) {
                result.add(prefix + c);
            }
        } else {
            for (Character c : alphabet) {
                getStrings(result, prefix + c, len, alphabet);
            }
        }
    }

    @Test
    public void testTransferMatrix() {
        CountStringsSolution.NfaAutomaton nfaAutomaton = getSampleNfa();
        CountStringsSolution.DfaAutomaton dfaAutomaton = new CountStringsSolution.NfaToDfaConverter(false).convert(nfaAutomaton);
        BigInteger[][] transferMatrix = dfaAutomaton.transferMatrix();
        System.out.println(transferMatrix[2][2]);
    }

    @Test
    public void testMatrixMultiplication() {
        BigInteger[][] A = {
                {new BigInteger("1"), new BigInteger("2")},
                {new BigInteger("3"), new BigInteger("4")}
        };
        BigInteger[][] B = {
                {new BigInteger("2"), new BigInteger("0")},
                {new BigInteger("1"), new BigInteger("2")}
        };
        BigInteger[][] result1 = CountStringsSolution.MatrixUtil.squareMatrixMultiply(A, B, 0);
        BigInteger[][] result2 = CountStringsSolution.MatrixUtil.squareMatrixPower(A, 20, 0);

        printMatrix(result1);
        printMatrix(result2);
    }

    @Test
    public void testAutomatons() {
        CountStringsSolution.NfaAutomaton nfaAutomaton = getSampleNfa();
        CountStringsSolution.DfaAutomaton dfaAutomaton = new CountStringsSolution.NfaToDfaConverter(false).convert(nfaAutomaton);
        System.out.println(dfaAutomaton.inputAccepted("aba") + " " + nfaAutomaton.inputAccepted("aba"));
        System.out.println(dfaAutomaton.inputAccepted("") + " " + nfaAutomaton.inputAccepted(""));
        System.out.println(dfaAutomaton.inputAccepted("abbbbbba") + " " + nfaAutomaton.inputAccepted("abbbbbba"));
        System.out.println(dfaAutomaton.inputAccepted("aaa") + " " + nfaAutomaton.inputAccepted("aaa"));

        for (int i = 0; i < 6; i++) {
            long start = System.nanoTime();
            long countDfa = dfaAutomaton.countAcceptedInputs(i);
            long elapsedMs = System.nanoTime() - start / 1000;
            System.out.println(i + " -> " + countDfa + "(" + elapsedMs + "ms)");
        }
    }

    private static CountStringsSolution.NfaAutomaton getSampleNfa() {
        CountStringsSolution.Automaton.State q_s = CountStringsSolution.NfaAutomaton.ARBITRARY_START_STATE;
        CountStringsSolution.Automaton.State q_0 = new CountStringsSolution.NfaAutomaton.State();
        CountStringsSolution.Automaton.State q_1 = new CountStringsSolution.NfaAutomaton.State();
        CountStringsSolution.Automaton.State q_2 = new CountStringsSolution.NfaAutomaton.State();
        CountStringsSolution.NfaAutomaton.State[] states = new CountStringsSolution.NfaAutomaton.State[]{q_s, q_0, q_1, q_2};
        CountStringsSolution.NfaAutomaton.Transition transitions[] = new CountStringsSolution.NfaAutomaton.Transition[]{new CountStringsSolution.NfaAutomaton.Transition(q_s, q_0, CountStringsSolution.NfaAutomaton.EMPTY_INPUT),
                new CountStringsSolution.NfaAutomaton.Transition(q_0, q_1, 'a'), new CountStringsSolution.NfaAutomaton.Transition(q_1, q_1, 'b'), new CountStringsSolution.NfaAutomaton.Transition(q_1, q_2, 'b'),
                new CountStringsSolution.NfaAutomaton.Transition(q_1, q_2, 'a'),};
        return new CountStringsSolution.NfaAutomaton(Arrays.asList(states), Arrays.asList(transitions), Arrays.asList('a', 'b'), q_s,
                Arrays.asList(states[states.length - 1], states[1]));
    }

    private static void printMatrix(Object[][] result1) {
        for (int i = 0; i < result1.length; i++) {
            System.out.println(
                    Arrays.stream(result1[i])
                            .map(Object::toString)
                            .collect(Collectors.joining("\t"))
            );
        }
    }
}
