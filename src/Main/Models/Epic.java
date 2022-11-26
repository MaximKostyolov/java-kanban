package Main.Models;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Integer> subtaskId;

    public Epic(int id, String name, String description, Status status, ArrayList<Integer> subtaskId) {
        super(id, name, description, status);
        this.subtaskId = subtaskId;
    }

    public Epic(String name, String description, Status status, LocalDateTime startTime, long duration,
                LocalDateTime endTime) {
        super(name, description, status, startTime, duration, endTime);
        this.subtaskId = new ArrayList<>();
    }

    public Epic(String name, String description, ArrayList<Integer> subtaskId) {
        super(name, description);
        this.subtaskId = subtaskId;
    }

    public Epic(String name, String description) {
        super(name, description);
        this.subtaskId = new ArrayList<>();
    }

    public Epic(String name, String description, Status status) {
        super(name, description, status);
        this.subtaskId = new ArrayList<>();
    }

    public ArrayList<Integer> getSubtaskId() {
        return subtaskId;
    }

    public void setSubtaskId(ArrayList<Integer> subtaskId) {
        this.subtaskId = subtaskId;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + getId() +
                ", name=" + getName() +
                ", description=" + getDescription() +
                ", status=" + getStatus() +
                ", startTime=" + getStartTime() +
                ", duration=" + getDuration() +
                ", endTime=" + getEndTime() +
                ", subtaskId=" + getSubtaskId() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || o.getClass() != this.getClass()) {
            return false;
        }
        Epic epic = (Epic) o;
        return  ((this.getId() == epic.getId()) && (this.getName().equals(epic.getName())) &&
                (this.getDescription().equals(epic.getDescription())) && (this.getStatus().equals(epic.getStatus())) && (this.getStartTime() ==
                epic.getStartTime()) && (this.getDuration() == epic.getDuration()) && (this.getEndTime() == epic.getEndTime()) &&
                (this.getSubtaskId().equals(epic.getSubtaskId())));
    }
}