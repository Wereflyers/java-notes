package ru.practice.javanotes.manager;


import ru.practice.javanotes.tasksProperties.Task;

public class Node { //Класс для узла списка
    private final Task data;
    private Node next = null;
    private Node prev = null;

    public Node (Task data) {
        this.data = data;
    }

    public Task getData() {
        return data;
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public Node getPrev() {
        return prev;
    }

    public void setPrev(Node prev) {
        this.prev = prev;
    }
}
