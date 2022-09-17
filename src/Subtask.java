public class Subtask extends Task {

    int epicId;

    public Subtask(String name, String description, Status status, int epicId) {
        super(name, description, status);
        this.epicId = epicId;
    }

    public Subtask(String name, String description) {
        super(name, description);
        this.status = Status.NEW;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "epicId=" + epicId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + getId() +
                ", status='" + status + '\'' +
                '}';
    }

}
