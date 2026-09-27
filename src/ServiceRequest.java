public class ServiceRequest {
    private String studentId;
    private String requestType;
    private String timestamp;

    public ServiceRequest(String studentId, String requestType, String timestamp) {
        this.studentId = studentId;
        this.requestType = requestType;
        this.timestamp = timestamp;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getRequestType() {
        return requestType;
    }

    public String getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "Student ID: " + studentId + " | Request: " + requestType + " | Time: " + timestamp;
    }
}
