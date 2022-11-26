package Main.History;

import Main.Models.Task;

public class Node <T extends Task> {

    public T data;
    public Node<T> next;
    public Node<T> prev;

    public Node(T data) {
        this.data = data;
        this.next = null;
        this.prev = null;
    }

    public Node(T data, Node<T> prev) {
        this.data = data;
        this.prev = prev;
        this.next = null;
    }

}

