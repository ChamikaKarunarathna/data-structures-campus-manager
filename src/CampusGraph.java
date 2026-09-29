public class CampusGraph {
    // First location in the linked list of campus locations.
    private GraphVertex head;

    public CampusGraph() {
        this.head = null;
    }

    public boolean isEmpty() {
        return head == null;
    }

    public void addLocation(String name) {
        if (!isValidName(name)) {
            return;
        }
        name = name.trim();
        if (findLocation(name) != null) {
            System.out.println("Location '" + name + "' already exists.");
            return;
        }

        GraphVertex newVertex = new GraphVertex(name);
        if (isEmpty()) {
            head = newVertex;
        } else {
            GraphVertex current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newVertex;
        }
        System.out.println("Location '" + name + "' added successfully.");
    }

    public void removeLocation(String name) {
        if (!isValidName(name)) {
            return;
        }
        name = name.trim();
        GraphVertex previous = null;
        GraphVertex current = head;
        while (current != null && !current.name.equalsIgnoreCase(name)) {
            previous = current;
            current = current.next;
        }
        if (current == null) {
            System.out.println("Location '" + name + "' not found.");
            return;
        }

        // Remove incoming connections before unlinking the location itself.
        GraphVertex vertex = head;
        while (vertex != null) {
            removeNeighbour(vertex, current);
            vertex = vertex.next;
        }
        if (previous == null) {
            head = current.next;
        } else {
            previous.next = current.next;
        }
        System.out.println("Location '" + current.name + "' removed successfully.");
    }

    public void addConnection(String firstName, String secondName) {
        if (!isValidName(firstName) || !isValidName(secondName)) {
            return;
        }
        GraphVertex first = findExistingLocation(firstName.trim());
        if (first == null) {
            return;
        }
        GraphVertex second = findExistingLocation(secondName.trim());
        if (second == null) {
            return;
        }
        if (first == second) {
            System.out.println("A location cannot be connected to itself.");
            return;
        }
        if (hasNeighbour(first, second)) {
            System.out.println("Connection already exists.");
            return;
        }

        // Store both directions because campus roads are undirected.
        appendNeighbour(first, second);
        appendNeighbour(second, first);
        System.out.println("Connection added successfully:");
        System.out.println(first.name + " <--> " + second.name);
    }

    public void removeConnection(String firstName, String secondName) {
        if (!isValidName(firstName) || !isValidName(secondName)) {
            return;
        }
        GraphVertex first = findExistingLocation(firstName.trim());
        if (first == null) {
            return;
        }
        GraphVertex second = findExistingLocation(secondName.trim());
        if (second == null) {
            return;
        }
        if (!hasNeighbour(first, second)) {
            System.out.println("Connection does not exist.");
            return;
        }

        removeNeighbour(first, second);
        removeNeighbour(second, first);
        System.out.println("Connection between '" + first.name + "' and '"
                + second.name + "' removed successfully.");
    }

    public void displayConnections() {
        if (isEmpty()) {
            System.out.println("Campus network is empty.");
            return;
        }
        System.out.println("===== Campus Network =====");
        GraphVertex current = head;
        while (current != null) {
            System.out.print(current.name + " -> ");
            GraphEdgeNode edge = current.firstNeighbour;
            if (edge == null) {
                System.out.print("No connections");
            }
            while (edge != null) {
                System.out.print(edge.neighbour.name);
                if (edge.next != null) {
                    System.out.print(", ");
                }
                edge = edge.next;
            }
            System.out.println();
            current = current.next;
        }
    }

    public void bfsTraversal(String startName) {
        if (isEmpty()) {
            System.out.println("Campus network is empty.");
            return;
        }
        if (!isValidName(startName)) {
            return;
        }
        GraphVertex start = findLocation(startName.trim());
        if (start == null) {
            System.out.println("Starting location not found.");
            return;
        }

        // Reset visits so every BFS starts fresh, including repeated traversals.
        GraphVertex current = head;
        while (current != null) {
            current.visited = false;
            current = current.next;
        }
        BfsQueue queue = new BfsQueue();
        start.visited = true;
        queue.enqueue(start);
        System.out.println("BFS Traversal:");
        boolean firstOutput = true;
        while (!queue.isEmpty()) {
            // FIFO order visits all nearby locations before more distant ones.
            current = queue.dequeue();
            if (!firstOutput) {
                System.out.print(" -> ");
            }
            System.out.print(current.name);
            firstOutput = false;

            GraphEdgeNode edge = current.firstNeighbour;
            while (edge != null) {
                if (!edge.neighbour.visited) {
                    // Mark before enqueueing to avoid duplicates and cycles.
                    edge.neighbour.visited = true;
                    queue.enqueue(edge.neighbour);
                }
                edge = edge.next;
            }
        }
        System.out.println();
    }

    private boolean isValidName(String name) {
        if (name == null || name.trim().isEmpty()) {
            System.out.println("Location name cannot be empty.");
            return false;
        }
        return true;
    }

    private GraphVertex findLocation(String name) {
        GraphVertex current = head;
        while (current != null) {
            if (current.name.equalsIgnoreCase(name)) {
                return current;
            }
            current = current.next;
        }
        return null;
    }

    private GraphVertex findExistingLocation(String name) {
        GraphVertex vertex = findLocation(name);
        if (vertex == null) {
            System.out.println("Location '" + name + "' does not exist.");
        }
        return vertex;
    }

    private boolean hasNeighbour(GraphVertex vertex, GraphVertex neighbour) {
        GraphEdgeNode edge = vertex.firstNeighbour;
        while (edge != null) {
            if (edge.neighbour == neighbour) {
                return true;
            }
            edge = edge.next;
        }
        return false;
    }

    private void appendNeighbour(GraphVertex vertex, GraphVertex neighbour) {
        GraphEdgeNode newEdge = new GraphEdgeNode(neighbour);
        if (vertex.firstNeighbour == null) {
            vertex.firstNeighbour = newEdge;
        } else {
            GraphEdgeNode edge = vertex.firstNeighbour;
            while (edge.next != null) {
                edge = edge.next;
            }
            edge.next = newEdge;
        }
    }

    private void removeNeighbour(GraphVertex vertex, GraphVertex neighbour) {
        GraphEdgeNode previous = null;
        GraphEdgeNode edge = vertex.firstNeighbour;
        while (edge != null) {
            if (edge.neighbour == neighbour) {
                if (previous == null) {
                    vertex.firstNeighbour = edge.next;
                } else {
                    previous.next = edge.next;
                }
                return;
            }
            previous = edge;
            edge = edge.next;
        }
    }

    // A vertex represents one campus location.
    private static class GraphVertex {
        private final String name;
        private GraphEdgeNode firstNeighbour;
        private GraphVertex next;
        private boolean visited;

        private GraphVertex(String name) {
            this.name = name;
            this.firstNeighbour = null;
            this.next = null;
        }
    }

    // Each vertex has its own linked list of neighbouring vertices.
    // An undirected road will need one entry at each end of the road.
    private static class GraphEdgeNode {
        private final GraphVertex neighbour;
        private GraphEdgeNode next;

        private GraphEdgeNode(GraphVertex neighbour) {
            this.neighbour = neighbour;
            this.next = null;
        }
    }

    // Separate queue: the existing service queue only stores ServiceRequest objects.
    private static class BfsQueue {
        private BfsQueueNode front;
        private BfsQueueNode rear;

        private boolean isEmpty() {
            return front == null;
        }

        private void enqueue(GraphVertex vertex) {
            BfsQueueNode node = new BfsQueueNode(vertex);
            if (isEmpty()) {
                front = node;
            } else {
                rear.next = node;
            }
            rear = node;
        }

        private GraphVertex dequeue() {
            if (isEmpty()) {
                return null;
            }
            GraphVertex vertex = front.vertex;
            front = front.next;
            if (front == null) {
                rear = null;
            }
            return vertex;
        }
    }

    private static class BfsQueueNode {
        private final GraphVertex vertex;
        private BfsQueueNode next;

        private BfsQueueNode(GraphVertex vertex) {
            this.vertex = vertex;
        }
    }
}
