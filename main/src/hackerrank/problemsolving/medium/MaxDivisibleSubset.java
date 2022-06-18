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

class MaxDivisibleSubsetResult {


    public static int nonDivisibleSubset(int k, List<Integer> s) {
        int[] rems = new int[k];
        for (int i = 0; i < s.size(); i++) {
            int actual = s.get(i);
            int rem = actual % k;
            rems[rem]++;
        }

        int max = Math.min(1, rems[0]);
        for (int i = 1; i < k/ 2 +1; i++) {
            if (i != k - i) {
                max += Math.max(rems[i], rems[k - i]);
            } else {
                max++;
            }
        }
        return max;
    }
}

public class MaxDivisibleSubset {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        String[] firstMultipleInput = bufferedReader.readLine().replaceAll("\\s+$", "").split(" ");

        int n = Integer.parseInt(firstMultipleInput[0]);

        int k = Integer.parseInt(firstMultipleInput[1]);

        List<Integer> s = Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
            .map(Integer::parseInt)
            .collect(toList());

        int result = MaxDivisibleSubsetResult.nonDivisibleSubset(k, s);

        bufferedWriter.write(String.valueOf(result));
        bufferedWriter.newLine();

        bufferedReader.close();
        bufferedWriter.close();
    }
}

