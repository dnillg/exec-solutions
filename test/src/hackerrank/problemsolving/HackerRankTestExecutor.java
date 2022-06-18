package hackerrank.problemsolving;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HackerRankTestExecutor {

    private final Class<?> sutClass;

    public HackerRankTestExecutor(Class<?> sutClass) {
        this.sutClass = sutClass;
    }

    public static void checkOutputPathEnvVar() {
        if (System.getenv("OUTPUT_PATH") == null) {
            throw new IllegalArgumentException("OUTPUT_PATH env var should be set!");
        }
    }

    public String executeWithResources(String testCaseName) {
        try {
            final String resourceFolderPath = sutClass.getPackageName()
                .replace(".", "/") + "/" + sutClass.getSimpleName() + "/";
            String inputResourcePath = "/" + resourceFolderPath + testCaseName + "_input.txt";
            String expectedOutputResourcePath = "/" + resourceFolderPath + testCaseName + "_output.txt";
            final InputStream isStdIn = getClass().getResourceAsStream(inputResourcePath);
            if (isStdIn == null) {
                throw new IllegalArgumentException("Input file can't be loaded: " + inputResourcePath);
            }
            System.setIn(isStdIn);
            final Method mainMethod = sutClass.getMethod("main", String[].class);
            mainMethod.invoke(null, new Object[]{new String[0]});
            final String actualOutputStr = getCaseOutput();
            final String expectedOutput = readFile(getResourceFile(expectedOutputResourcePath));
            assertEquals(expectedOutput, actualOutputStr);
            return actualOutputStr;
        } catch (Exception e) {
            throw new RuntimeException("Test case failed!", e);
        }
    }

    public String execute(String input, String expectedOutput) {
        try {
            System.setIn(new ByteArrayInputStream(input.getBytes()));
            final Method mainMethod = sutClass.getMethod("main", String[].class);
            mainMethod.invoke(null, new Object[]{new String[0]});
            final String actualOutputStr = getCaseOutput();
            assertEquals(expectedOutput, actualOutputStr);
            return actualOutputStr;
        } catch (Exception e) {
            throw new RuntimeException("Test case failed!", e);
        }
    }

    private String getCaseOutput() throws IOException {
        return readFile(new File(System.getenv("OUTPUT_PATH")));
    }

    private File getResourceFile(String expectedOutputResourcePath) throws URISyntaxException {
        return new File(getClass().getResource(expectedOutputResourcePath).toURI());
    }

    private String readFile(File OUTPUT_PATH) throws IOException {
        return trim(new String(Files.readAllBytes(OUTPUT_PATH.toPath())));
    }

    private String trim(String s) {
        return s.trim();
    }

}
