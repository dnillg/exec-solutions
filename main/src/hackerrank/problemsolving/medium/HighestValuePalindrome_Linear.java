package hackerrank.problemsolving.medium;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Result_Linear {

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

    public static String highestValuePalindrome(String s, int n, int k) {
        int stopPos = n / 2;
        int rem = k;
        final char[] chars = s.toCharArray();
        char[] firstHalf = Arrays.copyOfRange(chars, 0,n / 2);
        char[] secondHalf = Arrays.copyOfRange(chars, n / 2 + n % 2, n);
        char[] middle = n % 2 == 0 ? new char[0] : new char[] {chars[stopPos]};
        reverse(secondHalf); // reverse secondHalf;
        int diffCount = 0;
        for (int i = 0; i < stopPos; i++) {
            if (firstHalf[i] != secondHalf[i]) {
                diffCount++;
            }
        }
        for (int i = 0; i < stopPos; i++) {
            //System.out.println(buildResult(firstHalf, secondHalf, middle));
            if (firstHalf[i] == secondHalf[i] && firstHalf[i] == '9') {
                continue;
            }
            final int isDifferent = firstHalf[i] != secondHalf[i] ? 1 : 0;
            if (firstHalf[i] != '9' && secondHalf[i] != '9' && rem - 2 >= diffCount - isDifferent) {
                firstHalf[i] = '9';
                secondHalf[i] = '9';
                rem -= 2;
                diffCount -= isDifferent;
            } else if (firstHalf[i] != secondHalf[i] && rem >= diffCount) {
                char larger = (char)Math.max((int)firstHalf[i], (int)secondHalf[i]);
                firstHalf[i] = larger;
                secondHalf[i] = larger;
                rem -= 1;
                diffCount--;
            } else if (firstHalf[i] == secondHalf[i]) {
                continue;
            } else {
                break;
            }
        }
        if (rem > 0 && middle.length > 0) {
            middle[0] = '9';
        }
        if (diffCount == 0) {
            return buildResult(firstHalf, secondHalf, middle);
        } else {
            return "-1";
        }
    }

    private static void reverse(char[] array) {
        for (int i = 0; i < array.length / 2; i++) {
            char temp = array[i];
            array[i] = array[array.length - 1 - i];
            array[array.length - 1 - i] = temp;
        }
    }

    private static String buildResult(char[] firstHalf, char[] secondHalf, char[] middle) {
        char[] rev = copy(secondHalf);
        reverse(rev);
        return new String(firstHalf) + new String(middle) + new String(rev);
    }

}

public class HighestValuePalindrome_Linear {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        String[] firstMultipleInput = bufferedReader.readLine().replaceAll("\\s+$", "").split(" ");

        int n = Integer.parseInt(firstMultipleInput[0]);

        int k = Integer.parseInt(firstMultipleInput[1]);

        String s = bufferedReader.readLine();

        String result = Result_Linear.highestValuePalindrome(s, n, k);

        bufferedWriter.write(result);
        bufferedWriter.newLine();

        bufferedReader.close();
        bufferedWriter.close();
    }
}

