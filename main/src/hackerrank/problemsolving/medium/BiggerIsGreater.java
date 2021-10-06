package hackerrank.problemsolving.medium;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class BiggerIsGreaterResult {

    private static String concat(List<Character> options) {
        return options.stream()
            .map(String::valueOf)
            .collect(Collectors.joining());
    }

    private static String tryLargerAndEqualOptions(String prefix, List<Character> options, String original) {
        if (options.isEmpty()) {
            return prefix.compareTo(original) > 0 ? prefix : null;
        }
        System.out.println("AO: " + options.toString());
        Character actualFromOriginal = original.charAt(prefix.length());
        List<Character> biggerOptions = options.stream()
            .filter(c -> c.compareTo(actualFromOriginal) >= 0)
            .collect(Collectors.toList());
        System.out.println("BO: " + biggerOptions.toString());
        return biggerOptions.stream().map(c -> {
            List<Character> reducedOptions = new ArrayList<>(options);
            reducedOptions.remove(c);
            if (c == actualFromOriginal) {
                return tryLargerAndEqualOptions(prefix + c, reducedOptions, original);
            } else {
                return Optional.of(prefix + c + concat(reducedOptions))
                    .filter( f  -> f.compareTo(original) > 0)
                    .orElse(null);
            }
        }).filter(r -> r != null).findFirst().orElse(null);
    }




    public static String biggerIsGreater(String w) {
        List<Character> chars = new ArrayList<>();
        final char[] wChars = w.toCharArray();
        for (char ch : wChars) {
            chars.add(ch);
        }
        chars.sort(Character::compareTo);

        final int wl = w.length();
        Integer idx = null;
        for (int i = wl - 2; i >= 0; i--) {
            if (w.charAt(i) < w.charAt(i + 1)) {
                idx = i;
                break;
            }
        }
        if (idx == null) {
            return "no answer";
        } else {
            final ArrayList<Character> remaining = new ArrayList<>(chars);
            String prefix = w.substring(0, idx);
            for (char c : prefix.toCharArray()) {
                remaining.remove((Character)c);
            }
            final Character nextGreaterChar = remaining.get(remaining.lastIndexOf(w.charAt(idx)) + 1);
            remaining.remove(nextGreaterChar);
            return prefix + nextGreaterChar + concat(remaining);
        }
    }

}

public class BiggerIsGreater {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        int T = Integer.parseInt(bufferedReader.readLine().trim());

        IntStream.range(0, T).forEach(TItr -> {
            try {
                String w = bufferedReader.readLine();

                String result = BiggerIsGreaterResult.biggerIsGreater(w);

                bufferedWriter.write(result);
                bufferedWriter.newLine();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        bufferedReader.close();
        bufferedWriter.close();
    }
}
