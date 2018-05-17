package com.fhtech.algue4.djikstra;

import org.junit.jupiter.api.Test;

import java.util.Comparator;
import static org.junit.jupiter.api.Assertions.*;

class PriorityQueueTest {

    @Test
    void QueueEnqueueOrdersCorrectly(){
        PriorityQueue<Integer> queue = new PriorityQueue<>(10, Integer::compareTo);

        queue.enqueue(10);
        queue.enqueue(9);
        queue.enqueue(8);
        queue.enqueue(7);
        queue.enqueue(6);
        queue.enqueue(5);
        queue.enqueue(4);
        queue.enqueue(3);
        queue.enqueue(2);
        queue.enqueue(1);

        assertEquals(1, queue.dequeue().intValue());
        assertEquals(2, queue.dequeue().intValue());
        assertEquals(3, queue.dequeue().intValue());
        assertEquals(4, queue.dequeue().intValue());
        assertEquals(5, queue.dequeue().intValue());
        assertEquals(6, queue.dequeue().intValue());
        assertEquals(7, queue.dequeue().intValue());
        assertEquals(8, queue.dequeue().intValue());
        assertEquals(9, queue.dequeue().intValue());
        assertEquals(10, queue.dequeue().intValue());
    }

    @Test
    void QueueInsertsDuplicates(){
        PriorityQueue<Double> queue = new PriorityQueue<>();

        queue.enqueue(1.0);
        queue.enqueue(3.0);
        queue.enqueue(3.0);
        queue.enqueue(2.0);
        queue.enqueue(2.0);
        queue.enqueue(5.0);
        queue.enqueue(5.0);
        queue.enqueue(4.0);
        queue.enqueue(4.0);

        assertEquals(1.0, queue.dequeue().doubleValue());
        assertEquals(2.0, queue.dequeue().doubleValue());
        assertEquals(2.0, queue.dequeue().doubleValue());
        assertEquals(3.0, queue.dequeue().doubleValue());
        assertEquals(3.0, queue.dequeue().doubleValue());
        assertEquals(4.0, queue.dequeue().doubleValue());
        assertEquals(4.0, queue.dequeue().doubleValue());
        assertEquals(5.0, queue.dequeue().doubleValue());
        assertEquals(5.0, queue.dequeue().doubleValue());

    }

}