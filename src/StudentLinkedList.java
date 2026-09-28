public class StudentLinkedList {
    private StudentNode head;
    private final StudentSearchIndex searchIndex;

    public StudentLinkedList() {
        this.head = null;
        this.searchIndex = new StudentSearchIndex();
    }

    public boolean isStudentExists(String studentId) {
        StudentNode current = head;
        while (current != null) {
            if (current.data.getStudentId().equalsIgnoreCase(studentId)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    // Add Student Record
    public void addStudent(Student student) {
        StudentNode newNode = new StudentNode(student);
        if (head == null) {
            head = newNode;
        } else {
            StudentNode current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
        searchIndex.add(student);
        System.out.println("Student added successfully.");
    }

    // Update Student Record
    public boolean updateStudent(String studentId, String newName, String newProgramme, double newMarks) {
        StudentNode current = head;
        while (current != null) {
            if (current.data.getStudentId().equalsIgnoreCase(studentId)) {
                current.data.setName(newName);
                current.data.setProgramme(newProgramme);
                current.data.setMarks(newMarks);
                System.out.println("Student record updated successfully.");
                return true;
            }
            current = current.next;
        }
        System.out.println("Student with ID " + studentId + " not found.");
        return false;
    }

    // Delete Student Record
    public boolean deleteStudent(String studentId) {
        if (head == null) {
            System.out.println("No records to delete.");
            return false;
        }

        if (head.data.getStudentId().equalsIgnoreCase(studentId)) {
            searchIndex.remove(studentId);
            head = head.next;
            System.out.println("Student record deleted successfully.");
            return true;
        }

        StudentNode current = head;
        while (current.next != null && !current.next.data.getStudentId().equalsIgnoreCase(studentId)) {
            current = current.next;
        }

        if (current.next != null) {
            searchIndex.remove(studentId);
            current.next = current.next.next;
            System.out.println("Student record deleted successfully.");
            return true;
        }

        System.out.println("Student with ID " + studentId + " not found.");
        return false;
    }

    // Display All Records
    public void displayAllStudents() {
        if (head == null) {
            System.out.println("No student records available.");
            return;
        }

        System.out.println("\n--- Student Records ---");
        StudentNode current = head;
        while (current != null) {
            System.out.println(current.data.toString());
            current = current.next;
        }
        System.out.println("-----------------------");
    }

    public void displayStudentsUsingTree() {
        searchIndex.displayInOrder();
    }

    public Student findStudentUsingHash(String studentId) {
        return searchIndex.find(studentId);
    }
}
