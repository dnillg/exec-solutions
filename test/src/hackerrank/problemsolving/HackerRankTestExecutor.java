package hackerrank.problemsolving;

import java.io.File;
import java.lang.reflect.Method;
import java.nio.file.Files;

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
            String resourcePath = "/" + sutClass.getPackageName()
                .replace(".", "/") + "/" + sutClass.getSimpleName() + "/" + testCaseName + "_input.txt";
            final String inputFilePath = new File(getClass().getResource(resourcePath).toURI()).toPath().toAbsolutePath().toString();
            System.setIn(getClass().getResourceAsStream(resourcePath));
            final Method mainMethod = sutClass.getMethod("main", String[].class);
            mainMethod.invoke(null, new Object[]{new String[0]});
            return new String(Files.readAllBytes(new File(System.getenv("OUTPUT_PATH")).toPath()));
        } catch (Exception e) {
            throw new RuntimeException("Test case failed!", e);
        }
    }

    public Class<?> getSutClass() {
        return sutClass;
    }

    public String getTestCaseName() {
        return testCaseName;
    }
}
