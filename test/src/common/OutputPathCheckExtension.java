package common;

import hackerrank.problemsolving.HackerRankTestExecutor;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;

public class OutputPathCheckExtension implements TestInstancePostProcessor {

        @Override
        public void postProcessTestInstance(Object testInstance, ExtensionContext context) throws Exception {
            HackerRankTestExecutor.checkOutputPathEnvVar();
        }

}
