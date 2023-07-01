package ru.practice.javanotes.manager;

import ru.practice.javanotes.tasksProperties.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomLinkedList {
    private static Node head;
    private static final Map<Integer, Node> indexer = new HashMap<>();

    public void linkLast(Task task) {
        Node node = new Node(task);
        remove(task.getIdNumber());
        if (head != null) {
            node.setNext(head);
        }
        head = node;
        indexer.put(task.getIdNumber(), node);
        }

    public List<Task> getTasks() {
        List<Task> list = new ArrayList<>();
        for (Node node : indexer.values()) {
            list.add(node.getData());
        }
        return list;
    }

    public void remove(Integer id) {
        if (indexer.containsKey(id)) {
            Node node = indexer.get(id);
            if (node.getPrev() != null && node.getNext() != null) {
                node.getPrev().setNext(node.getNext());
                node.getNext().setPrev(node.getPrev());
            } else if (node.getNext() != null) {
                head = node.getNext();
            } else if (node.getPrev() != null) {
                node.getPrev().setNext(null);
            }
            node.setPrev(null);
            node.setNext(null);
            indexer.remove(id);
        }
    }

    public void clear() {
        head = null;
        indexer.clear();
    }
}
