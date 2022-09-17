import java.util.ArrayList;

public class Epic extends Task {

    ArrayList<Integer> subtaskId;

    public Epic(String name, String description, ArrayList<Integer> subtaskId) {
        super(name, description);
        this.subtaskId = subtaskId;
    }

    public Epic(String name, String description) {
        super(name, description);
        this.subtaskId = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subtaskId=" + subtaskId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + getId() +
                ", status='" + status + '\'' +
                '}';
    }

}
