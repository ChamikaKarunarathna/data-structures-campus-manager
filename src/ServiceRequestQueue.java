public class ServiceRequestQueue {
    private QueueNode front;
    private QueueNode rear;

    public boolean isEmpty() {
        return front == null;
    }

    public void enqueue(ServiceRequest request) {
        QueueNode newNode = new QueueNode(request);

        if (isEmpty()) {
            front = newNode;
            rear = newNode;
        } else {
            rear.next = newNode;
            rear = newNode;
        }
    }

    public ServiceRequest dequeue() {
        if (isEmpty()) {
            System.out.println("No service requests in the queue.");
            return null;
        }

        ServiceRequest removedRequest = front.data;
        front = front.next;

        if (front == null) {
            rear = null;
        }

        return removedRequest;
    }

    public ServiceRequest peek() {
        if (isEmpty()) {
            return null;
        }
        return front.data;
    }

    public void displayQueue() {
        if (isEmpty()) {
            System.out.println("No service requests waiting in the queue.");
            return;
        }

        System.out.println("\n--- Service Request Queue ---");
        QueueNode current = front;
        int index = 1;

        while (current != null) {
            System.out.println(index + ". " + current.data.toString());
            current = current.next;
            index++;
        }
        System.out.println("-----------------------------");
    }

    private static class QueueNode {
        private ServiceRequest data;
        private QueueNode next;

        public QueueNode(ServiceRequest data) {
            this.data = data;
            this.next = null;
        }
    }
}
