package hackerrank.problemsolving.medium;

import java.io.*;
import java.util.*;

import static java.util.stream.Collectors.joining;

class Result_States {

    private static char[] copy(char[] o) {
        char[] c = new char[o.length];
        for(int i = 0; i < o.length; i++) {
            c[i] = o[i];
        }
        return c;
    }

    private static boolean isPalindrome(char[] state) {
        int stopPos = state.length / 2;
        for(int i = 0; i < stopPos; i++) {
            if (state[i] != state[state.length - 1 - i]) {
                return false;
            }
        }
        return true;
    }

    static class State {
        char[] chars;
        int pos;
        int left;

        public State(char[] chars, int pos, int left) {
            this.chars = chars;
            this.pos = pos;
            this.left = left;
        }
    }

    public static String highestValuePalindrome(String s, int n, int k) {
        final int stopPos = n / 2 + 1;
        LinkedList<State> states = new LinkedList<>();
        states.addFirst(new State(copy(s.toCharArray()), 0, k));
        State state;
        while((state = states.isEmpty() ? null : states.removeFirst()) != null) {
            ArrayList<State> current = new ArrayList<>();
            if (state.left >= 0)
                System.out.println(new String(state.chars) + "-" + state.pos + " - " + state.left);
            else {
                continue;
                // drop state
            }
            if (state.pos >= stopPos && isPalindrome(state.chars)) {
                return new String(state.chars);
            }
            if (isPalindrome(state.chars) && state.left == 0) {
                return new String(state.chars);
            }
            for (char c = '9'; c >= '0'; c--) {
                int newLeft = state.left;
                if (newLeft == 1) {
                    if (state.chars[state.pos] != c) {
                        char[] newState = copy(state.chars);
                        newState[state.pos] = c;
                        current.add(new State(newState, state.pos + 1, 0));
                    }
                    if (state.chars[n - 1 - state.pos] != c) {
                        char[] newState = copy(state.chars);
                        newState[n - 1 - state.pos] = c;
                        current.add(new State(newState, state.pos + 1, 0));
                    }
                    if (state.chars[state.pos] == state.chars[n - 1 - state.pos]) {
                        current.add(new State(state.chars, state.pos + 1, state.left));
                    }
                } else {
                    char[] newState = copy(state.chars);
                    if (newState[state.pos] != c) {
                        newLeft--;
                        newState[state.pos] = c;
                    }
                    if (state.chars[n - 1 - state.pos] != c) {
                        newLeft--;
                        newState[newState.length - 1 - state.pos] = c;
                    }
                    current.add(new State(newState, state.pos + 1, newLeft));
                }
            }
            for (int i = current.size() - 1; i >= 0; i--) {
                states.addFirst(current.get(i));
            }
        }

        return "-1";
    }

}

public class HighestValuePalindrome_States {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        String[] firstMultipleInput = bufferedReader.readLine().replaceAll("\\s+$", "").split(" ");

        int n = Integer.parseInt(firstMultipleInput[0]);

        int k = Integer.parseInt(firstMultipleInput[1]);

        String s = bufferedReader.readLine();

        String result = Result_States.highestValuePalindrome(s, n, k);

        bufferedWriter.write(result);
        bufferedWriter.newLine();

        bufferedReader.close();
        bufferedWriter.close();
    }
}

