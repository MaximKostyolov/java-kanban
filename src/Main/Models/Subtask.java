package Main.Models;

import java.time.LocalDateTime;

public class Subtask extends Task {

    private int epicId;

    public Subtask(String name, String description, Status status, LocalDateTime startTime, long duration, int epicId) {
        super(name, description, status, startTime, duration);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, Status status, int epicId) {
        super(name, description, status);
        this.epicId = epicId;
    }

    public Subtask(String name, String description) {
        super(name, description);
        setStatus(Status.NEW);
    }

    public Subtask(int id, String name, String description, Status status, int epicId) {
        super(id, name, description, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "id=" + getId() +
                ", name=" + getName() +
                ", description=" + getDescription() +
                ", status=" + getStatus() +
                ", startTime=" + getStartTime() +
                ", duration=" + getDuration() +
                ", endTime=" + getEndTime() +
                ", epicId=" + getEpicId() +
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
        Subtask subtask = (Subtask) o;
        return  ((this.getId() == subtask.getId()) && (this.getName().equals(subtask.getName())) &&
                (this.getDescription().equals(subtask.getDescription())) && (this.getStatus().equals(subtask.getStatus())) && (this.getStartTime() ==
                subtask.getStartTime()) && (this.getDuration() == subtask.getDuration()) && (this.getEndTime() == subtask.getEndTime()) &&
                (this.getEpicId() == subtask.getEpicId()));
    }

}
