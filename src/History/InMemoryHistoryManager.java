package History;

import Models.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    CustomLinkedList linkedHistory = new CustomLinkedList<>();

    @Override
    public void add(Task task) {
        linkedHistory.linkLast(task);
    }

    public void add(Subtask subtask) {
        linkedHistory.linkLast(subtask);
    }

    public void add(Epic epic) {
        linkedHistory.linkLast(epic);
    }

    @Override
    public void remove(int id) {
        HashMap<Integer, Node> historyMap = linkedHistory.getHistoryMap();
        if (historyMap.containsKey(id)) {
            Node node = linkedHistory.getNode(id);
            linkedHistory.removeNode(node);
            historyMap.remove(id);
            linkedHistory.setHistoryMap(historyMap);
        }
    }

    @Override
    public List<Task> getHistory() {
        return linkedHistory.getTasks();
    }

    private class CustomLinkedList<T extends Task> {
        public Node<T> head;
        public Node<T> tail;
        private HashMap<Integer, Node<T>> historyMap = new HashMap<>();
        public void linkLast(T task) {
            if (historyMap.containsKey(task.getId())) {
                removeNode(getNode(task.getId()));
                historyMap.remove(task.getId());
                addLast(task);
            } else {
                addLast(task);
            }
        }

        public List<Task> getTasks() {
            List<Task> history = new ArrayList<>();
            Node newTail = new Node<>(tail.data, tail.prev);
            while (newTail.data != head.data) {
                history.add(newTail.data);
                newTail = newTail.prev;
            }
            history.add(head.data);
            return history;
        }

        public void addLast(T task) {
            Node<T> oldTail = tail;
            Node<T> newNode = new Node(task, oldTail);
            tail = newNode;
            if (oldTail == null) {
                head = newNode;
            } else {
                oldTail.next = newNode;
            }
            historyMap.put(task.getId(), newNode);
        }

        public void removeNode(Node node) {
            if (node.prev == null) {
                node.next.prev = null;
                head = node.next;
            } else if (node.next == null) {
                node.prev.next = null;
                tail = node.prev;
            } else {
                node.next.prev = node.prev;
                node.prev.next = node.next;
            }
        }

        public Node getNode(int id) {
            Node node = historyMap.get(id);
            return node;
        }

        public HashMap<Integer, Node<T>> getHistoryMap() {
            return historyMap;
        }

        public void setHistoryMap(HashMap<Integer, Node<T>> historyMap) {
            this.historyMap = historyMap;
        }
    }

}