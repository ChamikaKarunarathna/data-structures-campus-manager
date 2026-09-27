public class RecentActionStack {
    private StackNode top;

    public boolean isEmpty() {
        return top == null;
    }

    public void push(String action) {
        StackNode newNode = new StackNode(action);
        newNode.next = top;
        top = newNode;
    }

    public String pop() {
        if (isEmpty()) {
            System.out.println("No recent actions available.");
            return null;
        }

        String action = top.data;
        top = top.next;
        return action;
    }

    public String peek() {
        if (isEmpty()) {
            return null;
        }
        return top.data;
    }

    public void displayActions() {
        if (isEmpty()) {
            System.out.println("No recent actions recorded.");
            return;
        }

        System.out.println("\n--- Recent Actions (Most Recent First) ---");
        StackNode current = top;
        int index = 1;

        while (current != null) {
            System.out.println(index + ". " + current.data);
            current = current.next;
            index++;
        }
        System.out.println("----------------------------------------");
    }

    private static class StackNode {
        private String data;
        private StackNode next;

        public StackNode(String data) {
            this.data = data;
            this.next = null;
        }
    }
}
