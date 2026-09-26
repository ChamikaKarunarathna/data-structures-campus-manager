import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StudentLinkedList studentList = new StudentLinkedList();
        int choice;

        System.out.println("==================================================================");
        System.out.println("  University Student Record and Campus Route Management System    ");
        System.out.println("==================================================================");

        do {
            System.out.println("\n--- Main Menu ---");
            System.out.println("1. Add Student Record");
            System.out.println("2. Update Student Record");
            System.out.println("3. Delete Student Record");
            System.out.println("4. Display All Records using Linked List");
            System.out.println("5. Add Service Request to Queue");
            System.out.println("6. Process Next Service Request");
            System.out.println("7. Display Recent Actions using Stack");
            System.out.println("8. Display Students using BST/AVL");
            System.out.println("9. Search Student using Hashing");
            System.out.println("10. Add Campus Location");
            System.out.println("11. Remove Campus Location");
            System.out.println("12. Add Campus Connection/Road");
            System.out.println("13. Remove Campus Connection/Road");
            System.out.println("14. Display Campus Connections");
            System.out.println("15. Traverse Campus Locations using BFS or DFS");
            System.out.println("16. Exit");
            System.out.print("Enter your choice (1-16): ");

            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number between 1 and 16.");
                System.out.print("Enter your choice (1-16): ");
                scanner.next();
            }
            choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1:
                    System.out.print("Enter Student ID: ");
                    String id = scanner.nextLine();
                    if (studentList.isStudentExists(id)) {
                        System.out.println("Error: Student with ID " + id + " already exists.");
                        break;
                    }
                    System.out.print("Enter Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter Programme: ");
                    String prog = scanner.nextLine();
                    
                    double marks = -1;
                    while (marks < 0 || marks > 100) {
                        System.out.print("Enter Marks (0-100): ");
                        if (scanner.hasNextDouble()) {
                            marks = scanner.nextDouble();
                            if (marks < 0 || marks > 100) {
                                System.out.println("Invalid marks. Must be between 0 and 100.");
                            }
                        } else {
                            System.out.println("Invalid input. Please enter a valid number.");
                            scanner.next();
                        }
                    }
                    scanner.nextLine(); // Consume newline
                    
                    studentList.addStudent(new Student(id, name, prog, marks));
                    break;
                case 2:
                    System.out.print("Enter Student ID to update: ");
                    String updateId = scanner.nextLine();
                    if (!studentList.isStudentExists(updateId)) {
                        System.out.println("Error: Student with ID " + updateId + " not found.");
                        break;
                    }
                    System.out.print("Enter New Name: ");
                    String newName = scanner.nextLine();
                    System.out.print("Enter New Programme: ");
                    String newProg = scanner.nextLine();
                    
                    double newMarks = -1;
                    while (newMarks < 0 || newMarks > 100) {
                        System.out.print("Enter New Marks (0-100): ");
                        if (scanner.hasNextDouble()) {
                            newMarks = scanner.nextDouble();
                            if (newMarks < 0 || newMarks > 100) {
                                System.out.println("Invalid marks. Must be between 0 and 100.");
                            }
                        } else {
                            System.out.println("Invalid input. Please enter a valid number.");
                            scanner.next();
                        }
                    }
                    scanner.nextLine();
                    
                    studentList.updateStudent(updateId, newName, newProg, newMarks);
                    break;
                case 3:
                    System.out.print("Enter Student ID to delete: ");
                    String deleteId = scanner.nextLine();
                    studentList.deleteStudent(deleteId);
                    break;
                case 4:
                    studentList.displayAllStudents();
                    break;
                case 5:
                    System.out.println("[Feature under development] - Add Service Request to Queue");
                    break;
                case 6:
                    System.out.println("[Feature under development] - Process Next Service Request");
                    break;
                case 7:
                    System.out.println("[Feature under development] - Display Recent Actions using Stack");
                    break;
                case 8:
                    System.out.println("[Feature under development] - Display Students using BST/AVL");
                    break;
                case 9:
                    System.out.println("[Feature under development] - Search Student using Hashing");
                    break;
                case 10:
                    System.out.println("[Feature under development] - Add Campus Location");
                    break;
                case 11:
                    System.out.println("[Feature under development] - Remove Campus Location");
                    break;
                case 12:
                    System.out.println("[Feature under development] - Add Campus Connection/Road");
                    break;
                case 13:
                    System.out.println("[Feature under development] - Remove Campus Connection/Road");
                    break;
                case 14:
                    System.out.println("[Feature under development] - Display Campus Connections");
                    break;
                case 15:
                    System.out.println("[Feature under development] - Traverse Campus Locations using BFS or DFS");
                    break;
                case 16:
                    System.out.println("Exiting the system. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please select a number between 1 and 16.");
            }
        } while (choice != 16);

        scanner.close();
    }
}
