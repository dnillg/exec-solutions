package leetcode.other;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static org.junit.jupiter.api.Assertions.assertTrue;

class MinimumBracketsToRemove {

    class Solution {
        public String removeInvalidBrackets(String input) {
            Stack<Integer> openPositions = new Stack<>();
            List<Integer> positionsToRemove = new ArrayList<>();

            for (int i = 0; i < input.length(); i++) {
                char c = input.charAt(i);
                if (c == '(') {
                    openPositions.add(i);
                } else if(c == ')') {
                    if (openPositions.isEmpty()) {
                        positionsToRemove.add(i);
                    } else {
                        openPositions.pop();
                    }
                }
            }
            positionsToRemove.addAll(openPositions);

            StringBuilder sb = new StringBuilder();
            int last = -1;
            for(int i = 0; i < positionsToRemove.size(); i++) {
                sb.append(input, last + 1, positionsToRemove.get(i));
                last = positionsToRemove.get(i);
            }
            if (last != input.length() -1) {
                sb.append(input, last + 1, input.length());
            }
            return sb.toString();
        }
    }

    // -------------------------------------------
    // TESTS
    // -------------------------------------------

    @Test
    void case0() {
        System.out.println(new Solution().removeInvalidBrackets("a)bc(d)"));
        System.out.println(new Solution().removeInvalidBrackets(")ab(c)d"));
        System.out.println(new Solution().removeInvalidBrackets("))(("));
        System.out.println(new Solution().removeInvalidBrackets("z))((x"));
    }


}
