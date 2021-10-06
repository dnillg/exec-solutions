package hackerrank.problemsolving.medium;

import hackerrank.problemsolving.HackerRankTestExecutor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.PrintStream;

class BiggerIsGreaterTest {

    private static final PrintStream CONSOLE_OUT = System.out;
    private static final Class<?> SUT_CLASS = BiggerIsGreater.class;

    @BeforeEach
    void setUp() {
        HackerRankTestExecutor.checkOutputPathEnvVar();
    }

    @Test
    void case0() {
        final String caseName = Thread.currentThread().getStackTrace()[1].getMethodName();
        CONSOLE_OUT.println(new HackerRankTestExecutor(SUT_CLASS, caseName).execute());
    }

    @Test
    void case4() {
        final String caseName = Thread.currentThread().getStackTrace()[1].getMethodName();
        CONSOLE_OUT.println(new HackerRankTestExecutor(SUT_CLASS, caseName).execute());
    }

}
