package hackerrank.problemsolving.medium;

import hackerrank.problemsolving.HackerRankTestExecutor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.PrintStream;

class MaximumPalindromesTest {

    private static final PrintStream CONSOLE_OUT = System.out;
    private static final Class<?> SUT_CLASS = MaximumPalindromes.class;

    @BeforeEach
    void setUp() {
        HackerRankTestExecutor.checkOutputPathEnvVar();
    }

    @Test
    void case0() {
        final String caseName = Thread.currentThread().getStackTrace()[1].getMethodName();
        CONSOLE_OUT.println(new HackerRankTestExecutor(SUT_CLASS).executeWithResources(caseName));
    }

    @Test
    void case1() {
        final String caseName = Thread.currentThread().getStackTrace()[1].getMethodName();
        CONSOLE_OUT.println(new HackerRankTestExecutor(SUT_CLASS).executeWithResources(caseName));
    }

    @Test
    void case13() {
        final String caseName = Thread.currentThread().getStackTrace()[1].getMethodName();
        CONSOLE_OUT.println(new HackerRankTestExecutor(SUT_CLASS).executeWithResources(caseName));
    }

    @Test
    void case17() {
        final String caseName = Thread.currentThread().getStackTrace()[1].getMethodName();
        CONSOLE_OUT.println(new HackerRankTestExecutor(SUT_CLASS).executeWithResources(caseName));
    }

    @Test
    void case22() {
        final String caseName = Thread.currentThread().getStackTrace()[1].getMethodName();
        CONSOLE_OUT.println(new HackerRankTestExecutor(SUT_CLASS).executeWithResources(caseName));
    }

}
