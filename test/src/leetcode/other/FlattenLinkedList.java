package leetcode.other;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Stack;

class FlattenLinkedList {

    static class ListNode {
        public final int value;
        public ListNode next;
        public ListNode prev;
        public ListNode child;

        ListNode(int value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value + Optional.ofNullable(child).map(it -> "(" + child + ")").orElse("") + Optional.ofNullable(next).map(it -> "," + it).orElse("");
        }
    }

    class Solution {
        public ListNode flattenListNodes(ListNode head) {
            ListNode current = head;
            Stack<ListNode> jumpBackStack = new Stack<>();
            while(current != null) {
                if (current.child != null) {
                    ListNode nextJumpBack = current.next;
                    current.next = current.child;
                    current.child.prev = current;
                    current.child = null;
                    if (nextJumpBack != null) {
                        jumpBackStack.add(nextJumpBack);
                    }
                }
                if (current.next == null && !jumpBackStack.isEmpty()) {
                    current.next = jumpBackStack.pop();
                    current.next.prev = current;
                }
                current = current.next;
            }
            return head;
        }
    }

    // -------------------------------------------
    // TESTS
    // -------------------------------------------

    @Test
    void case0() {
        final ListNode r = new Solution().flattenListNodes(toLinkedList(1, 3, List.of(4, 6, List.of(7)), 8, 2));
        System.out.println(r.toString());
    }

    private ListNode toLinkedList(Object... args) {
        List<ListNode> nodes = new ArrayList<>();

        if (args.length == 0 || args[0].getClass().isArray()) {
            throw new IllegalArgumentException();
        }
        ListNode prevNode = null;
        for (int i = 0; i < args.length; i++) {
            if (isCollection(args[i])) {
                if (isCollection(args[i - 1])) {
                    throw new IllegalArgumentException();
                } else {
                    prevNode.child = toLinkedList(((List<Object>)args[i]).toArray());
                }
            } else {
                ListNode current = new ListNode((Integer)(args[i]));
                if (prevNode != null) {
                    prevNode.next = current;
                }
                current.prev = prevNode;
                prevNode = current;
                nodes.add(current);
            }
        }
        return nodes.get(0);
    }

    private boolean isCollection(Object args) {
        return List.class.isAssignableFrom(args.getClass());
    }

}
