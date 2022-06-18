package onlytest;

import common.OutputPathCheckExtension;
import hackerrank.problemsolving.HackerRankTestExecutor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@ExtendWith(OutputPathCheckExtension.class)
class ExampleTest {

    class ExampleSolution {

        public static int nonDivisibleSubset(int k, List<Integer> s) {
            return 1;
        }
    }

    public class ExampleMain {
        public static void main(String[] args) throws IOException {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

            String[] firstMultipleInput = bufferedReader.readLine().replaceAll("\\s+$", "").split(" ");

            int n = Integer.parseInt(firstMultipleInput[0]);

            int k = Integer.parseInt(firstMultipleInput[1]);

            List<Integer> s = Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
                .map(Integer::parseInt)
                .collect(toList());

            int result = ExampleSolution.nonDivisibleSubset(k, s);

            bufferedWriter.write(String.valueOf(result));
            bufferedWriter.newLine();

            bufferedReader.close();
            bufferedWriter.close();
        }
    }

    // -------------------------------------------
    // TESTS
    // -------------------------------------------

    @Test
    void case0() {
        String input =  """
                1 1
                1 1
                """;
        String output =  "1";
        System.out.println(new HackerRankTestExecutor(ExampleMain.class).execute(input, output));
    }

}
