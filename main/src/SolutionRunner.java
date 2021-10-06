import hackerrank.problemsolving.medium.TheTimeInWords;

import java.io.File;
import java.io.PrintStream;
import java.nio.file.Files;

public class SolutionRunner {

    private static PrintStream CONSOLE_OUT = System.out;

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            throw new IllegalArgumentException("Args should contain the input and output file path!");
        }
        final String inputFile = args[0];
        final String outputFile = args[1];

        System.setIn(Files.newInputStream(new File(inputFile).toPath()));
        System.setOut(new PrintStream(new File(outputFile)));
        TheTimeInWords.main(args);
        CONSOLE_OUT.println(new String(Files.readAllBytes(new File(outputFile).toPath())));
    }

}