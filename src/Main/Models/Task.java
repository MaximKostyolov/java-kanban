package Main.Models;

import java.time.LocalDateTime;

public class Task {

    private int id;
    private String name;
    private String description;
    private Status status;
    private LocalDateTime startTime;
    private long duration;
    private LocalDateTime endTime;

    public Task(int id, String name, String description, Status status, LocalDateTime startTime, long duration, LocalDateTime endTime) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
        this.endTime = endTime;
    }

    public Task(String name, String description, Status status, LocalDateTime startTime, long duration, LocalDateTime endTime) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
        this.endTime = endTime;
    }

    public Task(String name, String description, Status status, LocalDateTime startTime, long duration) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
        this.endTime = startTime.plusMinutes(duration);
    }

    public Task(String name, String description, Status status) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = null;
        this.duration = 0;
        this.endTime = null;
    }

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
        this.startTime = null;
        this.duration = 0;
        this.endTime = null;
    }

    public Task(int id, String name, String description, Status status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = null;
        this.duration = 0;
        this.endTime = null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
        if ((duration > 0 ) && (!this.getStartTime().equals(null))) {
            this.setEndTime(this.getStartTime().plusMinutes(duration));
        }
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + getId() +
                ", name=" + getName() +
                ", description=" + getDescription() +
                ", status=" + getStatus() +
                ", startTime=" + getStartTime() +
                ", duration=" + getDuration() +
                ", endTime=" + getEndTime() +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || o.getClass() != this.getClass()) {
            return false;
        }
        Task task = (Task) o;
        return  ((this.getId() == task.getId()) && (this.getName().equals(task.getName())) &&
                (this.getDescription().equals(task.getDescription())) && (this.getStatus().equals(task.getStatus())) && (this.getStartTime() ==
                task.getStartTime()) && (this.getDuration() == task.getDuration()) && (this.getEndTime() == task.getEndTime()));
    }

    @Override
    public int hashCode() {
        return getId();
    }
}
