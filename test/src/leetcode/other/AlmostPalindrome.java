package leetcode.other;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AlmostPalindrome {

    class Solution {
        public boolean almostPalindrome(String input) {
            if (input == null || input.length() <= 2) {
                return true;
            }
            String lowerInput = input.toLowerCase();
            int leftIndex = 0;
            int rightIndex = input.length() - 1;
            return recursion(lowerInput, leftIndex, rightIndex, 1);
        }

        private boolean recursion(String lowerInput, int leftIndex, int rightIndex, int removeableCount) {
            while(leftIndex < rightIndex) {
                if (lowerInput.charAt(leftIndex) == lowerInput.charAt(rightIndex)) {
                    leftIndex++;
                    rightIndex--;
                } else if (removeableCount > 0) {
                    return recursion(lowerInput, leftIndex + 1, rightIndex, removeableCount - 1) || recursion(lowerInput, leftIndex , rightIndex - 1, removeableCount - 1);
                } else {
                    return false;
                }
            }
            return true;
        }
    }

    // -------------------------------------------
    // TESTS
    // -------------------------------------------

    @Test
    void case0() {
        shouldBeTrue("RaceXcar");
        shouldBeTrue("");
        shouldBeTrue(" ");
        shouldBeTrue("RZ");
        shouldBeFalse("AAZ");
        shouldBeFalse("ZAZ");
        shouldBeFalse("ZAA");
        shouldBeFalse("ABCDDZCBA");
    }

    private void shouldBeTrue(String input) {
        final boolean result = new Solution().almostPalindrome(input);
        assertTrue(result);
    }

    private void shouldBeFalse(String input) {
        final boolean result = new Solution().almostPalindrome(input);
        assertTrue(result);
    }

}
