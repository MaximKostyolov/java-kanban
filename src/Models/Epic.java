package Models;

import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Integer> subtaskId;

    public ArrayList<Integer> getSubtaskId() {
        return subtaskId;
    }

    public void setSubtaskId(ArrayList<Integer> subtaskId) {
        this.subtaskId = subtaskId;
    }

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
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", status='" + getStatus() + '\'' +
                '}';
    }

}