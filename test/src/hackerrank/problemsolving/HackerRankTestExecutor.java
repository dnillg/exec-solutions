package hackerrank.problemsolving;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HackerRankTestExecutor {

    private final Class<?> sutClass;
    private final String testCaseName;

    public HackerRankTestExecutor(Class<?> sutClass, String testCaseName) {
        this.sutClass = sutClass;
        this.testCaseName = testCaseName;
    }

    public static void checkOutputPathEnvVar() {
        if (System.getenv("OUTPUT_PATH") == null) {
            throw new IllegalArgumentException("OUTPUT_PATH env var should be set!");
        }
    }

    public String execute() {
        try {
            final String resourceFolderPath = sutClass.getPackageName()
                .replace(".", "/") + "/" + sutClass.getSimpleName() + "/";
            String inputResourcePath = "/" + resourceFolderPath + testCaseName + "_input.txt";
            String expectedOutputResourcePath = "/" + resourceFolderPath + testCaseName + "_output.txt";
            final InputStream isStdIn = getClass().getResourceAsStream(inputResourcePath);
            System.setIn(isStdIn);
            final Method mainMethod = sutClass.getMethod("main", String[].class);
            mainMethod.invoke(null, new Object[]{new String[0]});
            final String actualOutputStr = trim(new String(Files.readAllBytes(new File(System.getenv("OUTPUT_PATH")).toPath())));
            final String expectedOutput = trim(new String(Files.readAllBytes(new File(getClass().getResource(expectedOutputResourcePath).toURI()).toPath())));
            assertEquals(expectedOutput, actualOutputStr);
            return actualOutputStr;
        } catch (Exception e) {
            throw new RuntimeException("Test case failed!", e);
        }
    }

    private String trim(String s) {
        return s.trim();
    }

    public Class<?> getSutClass() {
        return sutClass;
    }

    public String getTestCaseName() {
        return testCaseName;
    }
}
