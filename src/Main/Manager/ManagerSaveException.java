package Main.Manager;

public class ManagerSaveException extends RuntimeException {

    public ManagerSaveException() {
    }

    public ManagerSaveException(final String message) {
        super(message);
    }

    public String getDetailMessage() {
        return super.getMessage();
    }
}
