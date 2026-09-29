import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

// No test library needed: compile with src/*.java and run CampusGraphTest.
public class CampusGraphTest {
    private static int checks;

    public static void main(String[] args) {
        testRequiredScenarios();
        testEmptyGraphAndNames();
        testCyclesAndRemoval();
        testMainMenu();
        System.out.println("All " + checks + " checks passed.");
    }

    private static void testRequiredScenarios() {
        CampusGraph graph = new CampusGraph();
        String[] locations = {"Main Gate", "Library", "Canteen", "IT Building", "Auditorium"};
        for (String location : locations) {
            expectOutput("add " + location, "Location '" + location + "' added successfully.\n",
                    () -> graph.addLocation(location));
        }
        expectOutput("duplicate location", "Location 'Library' already exists.\n",
                () -> graph.addLocation("Library"));

        String[][] roads = {{"Main Gate", "Library"}, {"Main Gate", "Canteen"},
                {"Library", "IT Building"}, {"Canteen", "Auditorium"}};
        for (String[] road : roads) {
            expectOutput("add road", "Connection added successfully:\n" + road[0] + " <--> " + road[1] + "\n",
                    () -> graph.addConnection(road[0], road[1]));
        }
        String network = "===== Campus Network =====\n"
                + "Main Gate -> Library, Canteen\n"
                + "Library -> Main Gate, IT Building\n"
                + "Canteen -> Main Gate, Auditorium\n"
                + "IT Building -> Library\n"
                + "Auditorium -> Canteen\n";
        expectOutput("sample adjacency lists", network, graph::displayConnections);
        expectOutput("duplicate road", "Connection already exists.\n",
                () -> graph.addConnection("Main Gate", "Library"));
        expectOutput("reverse duplicate", "Connection already exists.\n",
                () -> graph.addConnection("Library", "Main Gate"));
        expectOutput("missing endpoint", "Location 'Gym' does not exist.\n",
                () -> graph.addConnection("Library", "Gym"));
        expectOutput("self connection", "A location cannot be connected to itself.\n",
                () -> graph.addConnection("Library", "Library"));
        expectOutput("failed operations preserve graph", network, graph::displayConnections);

        String traversal = "BFS Traversal:\nMain Gate -> Library -> Canteen -> IT Building -> Auditorium\n";
        expectOutput("sample BFS", traversal, () -> graph.bfsTraversal("Main Gate"));
        expectOutput("repeated BFS resets visits", traversal, () -> graph.bfsTraversal("Main Gate"));
        expectOutput("remove road", "Connection between 'Main Gate' and 'Canteen' removed successfully.\n",
                () -> graph.removeConnection("Main Gate", "Canteen"));
        expectOutput("both directions removed", "===== Campus Network =====\n"
                + "Main Gate -> Library\nLibrary -> Main Gate, IT Building\n"
                + "Canteen -> Auditorium\nIT Building -> Library\nAuditorium -> Canteen\n",
                graph::displayConnections);
        expectOutput("remove location", "Location 'Library' removed successfully.\n",
                () -> graph.removeLocation("Library"));
        expectOutput("incoming roads removed", "===== Campus Network =====\n"
                + "Main Gate -> No connections\nCanteen -> Auditorium\n"
                + "IT Building -> No connections\nAuditorium -> Canteen\n", graph::displayConnections);
        expectOutput("nonexistent road", "Connection does not exist.\n",
                () -> graph.removeConnection("Main Gate", "Auditorium"));
        expectOutput("invalid BFS start", "Starting location not found.\n",
                () -> graph.bfsTraversal("Gym"));
        expectOutput("isolated start", "BFS Traversal:\nMain Gate\n",
                () -> graph.bfsTraversal("Main Gate"));
        expectOutput("disconnected component", "BFS Traversal:\nCanteen -> Auditorium\n",
                () -> graph.bfsTraversal("Canteen"));
    }

    private static void testEmptyGraphAndNames() {
        CampusGraph graph = new CampusGraph();
        check("new graph empty", graph.isEmpty());
        expectOutput("empty display", "Campus network is empty.\n", graph::displayConnections);
        expectOutput("empty BFS", "Campus network is empty.\n", () -> graph.bfsTraversal("Gym"));
        expectOutput("empty remove", "Location 'Gym' not found.\n", () -> graph.removeLocation("Gym"));
        String invalidName = "Location name cannot be empty.\n";
        expectOutput("blank location", invalidName, () -> graph.addLocation("   "));
        expectOutput("null location", invalidName, () -> graph.addLocation(null));
        expectOutput("empty location", invalidName, () -> graph.addLocation(""));
        expectOutput("trim location", "Location 'Library' added successfully.\n",
                () -> graph.addLocation(" Library "));
        check("graph no longer empty", !graph.isEmpty());
        expectOutput("case insensitive duplicate", "Location 'library' already exists.\n",
                () -> graph.addLocation(" library "));
        capture(() -> graph.addLocation("Canteen"));
        expectOutput("blank removal", invalidName, () -> graph.removeLocation("\t"));
        expectOutput("blank first endpoint", invalidName, () -> graph.addConnection("", "Library"));
        expectOutput("blank second endpoint", invalidName, () -> graph.addConnection("Library", " "));
        expectOutput("blank road removal", invalidName, () -> graph.removeConnection("Library", ""));
        expectOutput("blank BFS start", invalidName, () -> graph.bfsTraversal(" "));
        expectOutput("missing first endpoint", "Location 'Gym' does not exist.\n",
                () -> graph.addConnection("Gym", "Library"));
        expectOutput("missing removal endpoint", "Location 'Gym' does not exist.\n",
                () -> graph.removeConnection("Library", "Gym"));
        expectOutput("case insensitive self road", "A location cannot be connected to itself.\n",
                () -> graph.addConnection(" library ", "LIBRARY"));
        expectOutput("trim and case road", "Connection added successfully:\nLibrary <--> Canteen\n",
                () -> graph.addConnection(" library ", "CANTEEN"));
        expectOutput("trim and case BFS", "BFS Traversal:\nLibrary -> Canteen\n",
                () -> graph.bfsTraversal(" LIBRARY "));
        expectOutput("reverse road removal", "Connection between 'Canteen' and 'Library' removed successfully.\n",
                () -> graph.removeConnection(" canteen ", "LIBRARY"));
        expectOutput("reverse removal symmetry", "===== Campus Network =====\n"
                + "Library -> No connections\nCanteen -> No connections\n", graph::displayConnections);
        expectOutput("repeat removal", "Connection does not exist.\n",
                () -> graph.removeConnection("Library", "Canteen"));
        expectOutput("case insensitive location removal", "Location 'Library' removed successfully.\n",
                () -> graph.removeLocation(" library "));
        expectOutput("last location removal", "Location 'Canteen' removed successfully.\n",
                () -> graph.removeLocation("Canteen"));
        check("all removed", graph.isEmpty());
        capture(() -> graph.addLocation("Library"));
        expectOutput("readd after empty", "BFS Traversal:\nLibrary\n", () -> graph.bfsTraversal("Library"));
    }

    private static void testCyclesAndRemoval() {
        CampusGraph graph = new CampusGraph();
        capture(() -> {
            for (String name : new String[] {"A", "B", "C", "D", "Isolated"}) {
                graph.addLocation(name);
            }
            graph.addConnection("A", "B");
            graph.addConnection("A", "C");
            graph.addConnection("A", "D");
            graph.addConnection("B", "C");
            graph.addConnection("B", "D");
        });
        expectOutput("cycle and shared neighbour", "BFS Traversal:\nA -> B -> C -> D\n",
                () -> graph.bfsTraversal("A"));
        expectOutput("different start after BFS", "BFS Traversal:\nD -> A -> B -> C\n",
                () -> graph.bfsTraversal("D"));
        capture(() -> graph.removeConnection("A", "C"));
        expectOutput("middle neighbour removal", "===== Campus Network =====\n"
                + "A -> B, D\nB -> A, C, D\nC -> B\nD -> A, B\nIsolated -> No connections\n",
                graph::displayConnections);
        capture(() -> graph.removeLocation("A"));
        expectOutput("head location removal", "===== Campus Network =====\n"
                + "B -> C, D\nC -> B\nD -> B\nIsolated -> No connections\n", graph::displayConnections);
        capture(() -> graph.removeLocation("Isolated"));
        capture(() -> graph.removeLocation("D"));
        expectOutput("tail location removal", "===== Campus Network =====\nB -> C\nC -> B\n",
                graph::displayConnections);
        capture(() -> graph.addLocation("A"));
        expectOutput("readded location has no stale roads", "BFS Traversal:\nA\n",
                () -> graph.bfsTraversal("A"));
    }

    private static void testMainMenu() {
        String input = String.join("\n",
                "oops", "99", "14", "15", "Gym", "10", "", "10", "Main Gate", "10", "Library",
                "12", "Main Gate", "Library", "14", "15", "Main Gate",
                "13", "Library", "Main Gate", "14", "11", "Library", "14",
                "1", "S001", "Alice", "Computing", "85", "4", "8", "9", "S001",
                "2", "S001", "Alice Updated", "Computing", "90", "9", "S001",
                "5", "S001", "Certificate", "6", "7", "3", "S001", "4", "8", "9", "S001", "16", "");
        InputStream originalInput = System.in;
        String output;
        try {
            System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
            output = capture(() -> Main.main(new String[0]));
        } finally {
            System.setIn(originalInput);
        }
        for (String expected : new String[] {
                "Invalid input. Please enter a number between 1 and 16.",
                "Invalid choice. Please select a number between 1 and 16.",
                "Campus network is empty.", "Location name cannot be empty.",
                "===== Campus Network =====\nMain Gate -> Library\nLibrary -> Main Gate\n",
                "BFS Traversal:\nMain Gate -> Library\n",
                "Connection between 'Library' and 'Main Gate' removed successfully.",
                "===== Campus Network =====\nMain Gate -> No connections\nLibrary -> No connections\n",
                "Location 'Library' removed successfully.",
                "===== Campus Network =====\nMain Gate -> No connections\n\n--- Main Menu ---",
                "Student added successfully.", "--- Student Records ---",
                "--- Student Records (BST, sorted by ID) ---",
                "Student found: ID: S001 | Name: Alice | Programme: Computing | Marks:",
                "Student record updated successfully.",
                "Student found: ID: S001 | Name: Alice Updated | Programme: Computing | Marks:",
                "Service request added to queue successfully.",
                "Processing next request: Student ID: S001 | Request: Certificate",
                "--- Recent Actions (Most Recent First) ---", "Added new student record: S001",
                "Student record deleted successfully.", "No student records available.",
                "Student with ID S001 not found.", "Exiting the system. Goodbye!"}) {
            check("menu output: " + expected, output.contains(expected));
        }
        check("no graph placeholders", !output.contains("Feature under development"));
    }

    // Capture printed output so tests check the same messages a student sees.
    private static String capture(Runnable action) {
        PrintStream originalOutput = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try (PrintStream output = new PrintStream(buffer)) {
            System.setOut(output);
            action.run();
        } finally {
            System.setOut(originalOutput);
        }
        return buffer.toString().replace("\r\n", "\n");
    }

    private static void expectOutput(String label, String expected, Runnable action) {
        String actual = capture(action);
        check(label + "\nExpected:\n" + expected + "Actual:\n" + actual, expected.equals(actual));
    }

    private static void check(String label, boolean passed) {
        if (!passed) {
            throw new AssertionError(label);
        }
        checks++;
    }
}
