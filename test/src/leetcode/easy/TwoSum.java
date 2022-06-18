package leetcode.easy;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class TwoSum {

    class Solution {
        public int[] twoSum(int[] nums, int target) {
            Map<Integer, Set<Integer>> indexByValue = new HashMap<>();
            for (int i = 0; i < nums.length; i++) {
                indexByValue.computeIfAbsent(nums[i], (ign) -> new HashSet<Integer>()).add(i);
            }
            for (int i = 0; i < nums.length; i++) {
                var complementer = target - nums[i];
                var cPositions = indexByValue.get(complementer);
                if (cPositions != null) {
                    if (nums[i] == complementer) {
                        var current = i;
                        var secondIndex = cPositions.stream().filter(it -> it != current).findFirst();
                        if (secondIndex.isPresent()) {
                            return new int[] {i, secondIndex.get()};
                        }
                    } else {
                        return new int[] {i, cPositions.iterator().next()};
                    }
                }
            }
            return null;
        }
    }

    // -------------------------------------------
    // TESTS
    // -------------------------------------------

    @Test
    void case0() {
        new Solution().twoSum(new int[]{ 2,7,11,15 }, 9);
    }

}
