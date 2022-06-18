package hackerrank.problemsolving.medium;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Result {

    private static final int NOT_FOUND = Integer.MIN_VALUE;
    private static final int MOD = (int)Math.pow(10, 9) + 7;
    private static final List<Integer> FACTORIALS = new ArrayList<>(List.of(1, 1));


    private static String str;
    private static List<List<Integer>> charPositions;


    public static int[] modDivide(int a, int b, int m) {
        if (m == 0)
            return new int[] { NOT_FOUND, 0, 0 };
        a %= m;
        // Extended GCD for b and m.
        int x = 0;
        int u = 1;
        int gcd = m;
        while (b != 0) {
            int q = gcd / b;
            int r = gcd - q * b;
            int n = x - u * q;
            gcd = b;
            b = r;
            x = u;
            u = n;
        }
        if (gcd < 0 && gcd > Integer.MIN_VALUE) {
            gcd = -gcd;
            x = -x;
        }
        // gcd = 0 if and only if b = 0 and m = 0.
        // m != 0 => gcd != 0.
        u = a / gcd;
        if (u * gcd == a) {
            m /= gcd;
            x = modMultiply(u, x, m);
            // x is the first solution,
            // m is the increment,
            // gcd is the quantity of solutions.
            if (m < 0)
                m = -m;
            return new int[] { x, m, gcd };
        }
        return new int[] { NOT_FOUND, 0, 0 };
    }

    public static long[] modDivide(long a, long b, long m) {
        if (Integer.MIN_VALUE < m && m <= Integer.MAX_VALUE) {
            if (m == 0L)
                return new long[] { NOT_FOUND, 0L, 0L };
            int ret[] = modDivide((int) (a % m), (int) (b % m), (int) m);
            return new long[] { ret[0], ret[1], ret[2] };
        }
        a %= m;
        // Extended GCD for b and m.
        long x = 0L;
        long u = 1L;
        long gcd = m;
        while (b != 0L) {
            long q = gcd / b;
            long r = gcd - q * b;
            long n = x - u * q;
            gcd = b;
            b = r;
            x = u;
            u = n;
        }
        if (gcd < 0L && gcd > Long.MIN_VALUE) {
            gcd = -gcd;
            x = -x;
        }
        // gcd = 0 if and only if b = 0 and m = 0.
        // m != 0 => gcd != 0.
        u = a / gcd;
        if (u * gcd == a) {
            m /= gcd;
            x = modMultiply(u, x, m);
            // x is the first solution,
            // m is the increment,
            // gcd is the quantity of solutions.
            if (m < 0L)
                m = -m;
            return new long[] { x, m, gcd };
        }
        return new long[] { NOT_FOUND, 0L, 0L };
    }

    public static int modMultiply(long a, long b, int m) {
        if (m <= 0) {
            if (m == 0)
                return NOT_FOUND;
            m = -m;
        }
        a %= m;
        b %= m;
        a = (a * b) % m;
        if (a < 0L)
            a += m;
        return (int) a;
    }

    public static long modMultiply(long a, long b, long m) {
        if (m <= 0L) {
            if (m == 0L)
                return NOT_FOUND;
            if (m == Long.MIN_VALUE) {
                a *= b;
                if (a < 0L)
                    a += m;
                return a;
            }
            m = -m;
        }
        a %= m;
        b %= m;
        if (m <= Integer.MAX_VALUE) {
            // Safe simple multiplication available.
            a = (a * b) % m;
            if (a < 0L)
                a += m;
            return a;
        }
        if (a < 0L)
            a += m;
        if (b < 0L)
            b += m;
        // a = min( a, b ), b = max( a, b )
        if (a > b) {
            long number = a;
            a = b;
            b = number;
        }
        // Corner cases of Schrage's method.
        if (a < 2L)
            return a * b;
        if (b == m - 1L)
            return m - a;
        // Safe simple multiplication available.
        if (Long.numberOfLeadingZeros(a) + Long.numberOfLeadingZeros(b) > 64)
            return (a * b) % m;
        // Schrage's method.
        // http://home.earthlink.net/~pfenerty/pi/schrages_method.html
        // http://objectmix.com/java/312426-extending-schrage-multiplication.html
        long quot = m / a;
        long rem = m - quot * a;
        if (rem < quot) {
            long number = b / quot;
            number = a * (b - quot * number) - rem * number;
            return number < 0L ? number + m : number;
        }
        // Bitwise multiplication.
        long leftTillOverflow;
        long number = 0L;
        while (a > 0L) {
            if ((a & 1L) == 1L) {
                leftTillOverflow = m - number;
                if (leftTillOverflow > b)
                    number += b;
                else
                    number = b - leftTillOverflow;
            }
            a >>= 1;
            leftTillOverflow = m - b;
            if (leftTillOverflow > b)
                b <<= 1;
            else
                b -= leftTillOverflow;
        }
        return number;
    }


    /*
     * Complete the 'initialize' function below.
     *
     * The function accepts STRING s as parameter.
     */

    public static void initialize(String s) {
        str = s;
        charPositions = new ArrayList<>();
        for (int i = 0; i <= 'z' - 'a'; i++) {
            charPositions.add(new ArrayList<>());
        }

        final char[] chars = s.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            charPositions.get(chars[i] - 'a').add(i);
        }
    }

    private static int factorial(int n){
        if (FACTORIALS.size() < n + 1) {
            int result = FACTORIALS.get(FACTORIALS.size() - 1);
            for (int i = FACTORIALS.size(); i <= n; i++) {
                result = modMultiply(result, i, MOD);
                FACTORIALS.add(result);
            }
        }
        return FACTORIALS.get(n);
    }

    public static int answerQuery(int l, int r) {
        List<Integer> doubles = new ArrayList<>();
        int possibleMiddleChars = 0;
        for (char c = 'a'; c <= 'z'; c++) {
            long count = charPositions.get(c - 'a').stream().parallel().filter(p -> p + 1 >= l && p + 1 <= r).count();
            if (count % 2 == 1) {
                possibleMiddleChars++;
            }
            if (count >= 2) {
                doubles.add((int) count / 2);
            }
        }
        final Integer length = doubles.stream().reduce(0, Integer::sum);
        final int numberator = factorial(length);
        final Integer denominator = doubles.stream()
            .map(v -> factorial(v))
            .reduce(1, (a, b) -> modMultiply(a, b, MOD));
        int res = modDivide(numberator, denominator, MOD)[0];
        if (possibleMiddleChars != 0) {
            res = modMultiply(res, possibleMiddleChars, MOD);
        }
        return res;
    }

}

public class MaximumPalindromes {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        String s = bufferedReader.readLine();

        Result.initialize(s);

        int q = Integer.parseInt(bufferedReader.readLine().trim());

        IntStream.range(0, q).forEach(qItr -> {
            try {
                String[] firstMultipleInput = bufferedReader.readLine().replaceAll("\\s+$", "").split(" ");

                int l = Integer.parseInt(firstMultipleInput[0]);

                int r = Integer.parseInt(firstMultipleInput[1]);

                int result = Result.answerQuery(l, r);

                bufferedWriter.write(String.valueOf(result));
                bufferedWriter.newLine();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        bufferedReader.close();
        bufferedWriter.close();
    }
}


