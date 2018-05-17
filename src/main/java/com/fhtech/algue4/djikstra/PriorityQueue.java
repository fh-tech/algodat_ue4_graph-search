package com.fhtech.algue4.djikstra;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class PriorityQueue<E> {

    private ArrayList<E> elements = null;
    private Comparator<E> comparator = null;

    public PriorityQueue(int capacity, Comparator<E> comparator) {
        assert capacity >= 0;
        assert comparator != null;

        this.elements = new ArrayList<>(capacity);
        this.comparator = comparator;
    }

    public PriorityQueue(Comparator<E> comparator) {
        this(10, comparator);
    }

    public PriorityQueue() {
        this(10,
                (E e1, E e2) -> ((Comparable<E>) e1).compareTo(e2));
    }

    public void enqueue(E e) {
        assert e != null;
        elements.add(e);
        siftUp(e, elements.size() - 1);
    }

    public int size(){
        return elements.size();
    }

    public E dequeue() {
        if(elements.size() > 1){
            E ret = elements.get(0);
            elements.set(0, elements.remove(elements.size() - 1));
            siftDown(elements.get(0), 0);
            return ret;
        }else{
            return elements.remove(0);
        }
    }

    private void siftUp(E e, int i) {
        if (i > 0) {
            final int parent = parentIdx(i);
            if (comparator.compare(e, elements.get(parentIdx(i))) <= 0) {
                //e is smaller -> sift up
                Collections.swap(elements, i, parentIdx(i));
                siftUp(e, parentIdx(i));
            }
        }
    }

    private void siftDown(E e, int i) {
        //choose a child node to check sift down
        int child;
        int left = leftChildIdx(i);
        int right = rightChildIdx(i);

        //if parent has children you can swap, if not you are done
        if(left < elements.size()) {

            if (right >= elements.size()) {
                //only left side filled
                child = left;
            } else {
                //both sides filled choose smaller
                if (comparator.compare(elements.get(left), elements.get(right)) <= 0) {
                    child = left;
                } else {
                    child = right;
                }
            }
            //if child is smaller swap and repeat
            if (comparator.compare(e, elements.get(child)) >= 0) {
                Collections.swap(elements, i, child);
                siftDown(e, child);
            }
        }

    }

    private int rightChildIdx(int parent) {
        return (parent * 2) + 2;
    }

    private int leftChildIdx(int parent) {
        return (parent * 2) + 1;
    }

    private int parentIdx(int child) {
        return ((child + 1) / 2) - 1;
    }

}
