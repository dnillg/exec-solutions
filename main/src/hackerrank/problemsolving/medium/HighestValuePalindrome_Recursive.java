package hackerrank.problemsolving.medium;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;

class Result_Recursive {

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

    private static String r(char[] state, int left, int pos, int stopPos) {
        if (left >= 0)
            System.out.println(new String(state) + "-" + pos + " - " + left);
        if (left < 0) {
            return null;
        }
        if (pos >= stopPos) {
            return isPalindrome(state) ? new String(state) : null;
        }
        if (isPalindrome(state) && left == 0) {
            return new String(state);
        }
        for (char c = '9'; c >= '0'; c--) {
            int newLeft = left;
            if (left == 1) {
                if (state[pos] != c) {
                    char[] newState = copy(state);
                    newState[pos] = c;
                    String solution = r(newState, 0, pos+1, stopPos);
                    if (solution != null) {
                        return solution;
                    }
                }
                if (state[state.length - 1 - pos] != c) {
                    char[] newState = copy(state);
                    newState[newState.length - 1 - pos] = c;
                    String solution = r(newState, 0, pos+1, stopPos);
                    if (solution != null) {
                        return solution;
                    }
                }
                if (state[pos] == state[state.length - 1 - pos]) {
                    String solution = r(state, left, pos+1, stopPos);
                    if (solution != null) {
                        return solution;
                    }
                }
                return isPalindrome(state) ? new String(state) : null;
            } else {
                char[] newState = copy(state);
                if (state[pos] != c) {
                    newLeft--;
                    newState[pos] = c;
                }
                if (state[state.length - 1 - pos] != c) {
                    newLeft--;
                    newState[newState.length - 1 - pos] = c;
                }
                String solution = r(newState, newLeft, pos+1, stopPos);
                if (solution != null) {
                    return solution;
                }
            }
        }
        return null;
    }

    public static String highestValuePalindrome(String s, int n, int k) {
        char[] state = copy(s.toCharArray());
        return Optional.ofNullable(r(state, k, 0, n / 2 + 1)).orElse("-1");
    }

}

public class HighestValuePalindrome_Recursive {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        String[] firstMultipleInput = bufferedReader.readLine().replaceAll("\\s+$", "").split(" ");

        int n = Integer.parseInt(firstMultipleInput[0]);

        int k = Integer.parseInt(firstMultipleInput[1]);

        String s = bufferedReader.readLine();

        String result = Result_Recursive.highestValuePalindrome(s, n, k);

        bufferedWriter.write(result);
        bufferedWriter.newLine();

        bufferedReader.close();
        bufferedWriter.close();
    }
}

