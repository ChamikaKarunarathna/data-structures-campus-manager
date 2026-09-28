public class StudentSearchIndex {
    private static class TreeNode {
        private Student student;
        private TreeNode left;
        private TreeNode right;

        private TreeNode(Student student) {
            this.student = student;
        }
    }

    private static class HashEntry {
        private final String studentId;
        private Student student;
        private HashEntry next;

        private HashEntry(String studentId, Student student, HashEntry next) {
            this.studentId = studentId;
            this.student = student;
            this.next = next;
        }
    }

    private TreeNode root;
    private HashEntry[] buckets = new HashEntry[16];
    private int hashSize;

    public void add(Student student) {
        root = insert(root, student);
        putInHash(student);
    }

    public void remove(String studentId) {
        root = removeFromTree(root, studentId);
        removeFromHash(studentId);
    }

    public Student find(String studentId) {
        int index = bucketIndex(studentId, buckets.length);
        HashEntry entry = buckets[index];
        while (entry != null) {
            if (entry.studentId.equalsIgnoreCase(studentId)) {
                return entry.student;
            }
            entry = entry.next;
        }
        return null;
    }

    public void displayInOrder() {
        if (root == null) {
            System.out.println("No student records available.");
            return;
        }

        System.out.println("\n--- Student Records (BST, sorted by ID) ---");
        displayInOrder(root);
        System.out.println("--------------------------------------------");
    }

    private TreeNode insert(TreeNode node, Student student) {
        if (node == null) {
            return new TreeNode(student);
        }

        int comparison = student.getStudentId().compareToIgnoreCase(node.student.getStudentId());
        if (comparison < 0) {
            node.left = insert(node.left, student);
        } else if (comparison > 0) {
            node.right = insert(node.right, student);
        } else {
            node.student = student;
        }
        return node;
    }

    private TreeNode removeFromTree(TreeNode node, String studentId) {
        if (node == null) {
            return null;
        }

        int comparison = studentId.compareToIgnoreCase(node.student.getStudentId());
        if (comparison < 0) {
            node.left = removeFromTree(node.left, studentId);
        } else if (comparison > 0) {
            node.right = removeFromTree(node.right, studentId);
        } else if (node.left == null) {
            return node.right;
        } else if (node.right == null) {
            return node.left;
        } else {
            TreeNode successor = minimum(node.right);
            node.student = successor.student;
            node.right = removeFromTree(node.right, successor.student.getStudentId());
        }
        return node;
    }

    private TreeNode minimum(TreeNode node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    private void displayInOrder(TreeNode node) {
        if (node != null) {
            displayInOrder(node.left);
            System.out.println(node.student);
            displayInOrder(node.right);
        }
    }

    private void putInHash(Student student) {
        if ((hashSize + 1) * 4 > buckets.length * 3) {
            resizeHashTable();
        }

        int index = bucketIndex(student.getStudentId(), buckets.length);
        HashEntry entry = buckets[index];
        while (entry != null) {
            if (entry.studentId.equalsIgnoreCase(student.getStudentId())) {
                entry.student = student;
                return;
            }
            entry = entry.next;
        }
        buckets[index] = new HashEntry(student.getStudentId(), student, buckets[index]);
        hashSize++;
    }

    private void removeFromHash(String studentId) {
        int index = bucketIndex(studentId, buckets.length);
        HashEntry previous = null;
        HashEntry entry = buckets[index];
        while (entry != null) {
            if (entry.studentId.equalsIgnoreCase(studentId)) {
                if (previous == null) {
                    buckets[index] = entry.next;
                } else {
                    previous.next = entry.next;
                }
                hashSize--;
                return;
            }
            previous = entry;
            entry = entry.next;
        }
    }

    private void resizeHashTable() {
        HashEntry[] oldBuckets = buckets;
        buckets = new HashEntry[oldBuckets.length * 2];
        hashSize = 0;
        for (HashEntry bucket : oldBuckets) {
            HashEntry entry = bucket;
            while (entry != null) {
                HashEntry next = entry.next;
                int index = bucketIndex(entry.studentId, buckets.length);
                entry.next = buckets[index];
                buckets[index] = entry;
                hashSize++;
                entry = next;
            }
        }
    }

    private int bucketIndex(String studentId, int capacity) {
        int hash = 0;
        for (int index = 0; index < studentId.length(); index++) {
            hash = 31 * hash + Character.toLowerCase(studentId.charAt(index));
        }
        return (hash & 0x7fffffff) % capacity;
    }
}