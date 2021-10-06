package hackerrank.problemsolving.medium;

import java.io.*;
import java.math.*;
import java.security.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;
import java.util.regex.*;
import java.util.stream.*;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

class TheTimeInWordsResult {

    private static final Map<Integer, String> numbersFrom0To9 = Map.of(
            0, "zero",
            1, "one",
            2, "two",
            3, "three",
            4, "four",
            5, "five",
            6, "six",
            7, "seven",
            8, "eight",
            9, "nine"
    );

    private static final Map<Integer, String> TENS = Map.of(
            2, "twenty",
            3, "thirty"
    );

    private static final Map<Integer, String> numbersFrom10To19 = Map.of(
            0, "ten",
            1, "eleven",
            2, "twelve",
            3, "thirteen",
            4, "fourteen",
            5, "fifteen",
            6, "sixteen",
            7, "seventeen",
            8, "eighteen",
            9, "nineteen"
    );

    private static String convertNumber(int m) {
        int tens = m / 10;
        int ones = m % 10;
        if (m >= 0 && m < 10) {
            return numbersFrom0To9.get(ones);
        } else if (m >= 10 && m < 20) {
            return numbersFrom10To19.get(ones);
        } else if (m >= 20) {
            return TENS.get(tens) + " " + numbersFrom0To9.get(ones);
        } else {
            throw new IllegalArgumentException("Must be between 0 and 30");
        }
    }

    private static String convertMinutes(int m) {
        if (m == 15) {
            return "quarter";
        } else if (m == 30) {
            return "half";
        } else if (m == 1) {
            return convertNumber(m) + " minute";
        } else {
            return convertNumber(m) + " minutes";
        }
    }

    public static String timeInWords(int h, int m) {
        int nextHr = h + 1;
        if (nextHr > 12) {
            nextHr = nextHr - 12;
        }
        if (m == 0) {
            return convertNumber(h) + " o' clock";
        } else if (m > 0 && m <= 30) {
            return convertMinutes(m) + " past " + convertNumber(h);
        } else if (m > 30 && m <= 60) {
            return convertMinutes(60 - m) + " to " + convertNumber(nextHr);
        } else {
            throw new IllegalArgumentException("m should be between 0 and 60");
        }
    }

}

public class TheTimeInWords {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        int h = Integer.parseInt(bufferedReader.readLine().trim());

        int m = Integer.parseInt(bufferedReader.readLine().trim());

        String result = TheTimeInWordsResult.timeInWords(h, m);

        bufferedWriter.write(result);
        bufferedWriter.newLine();

        bufferedReader.close();
        bufferedWriter.close();
    }
}
