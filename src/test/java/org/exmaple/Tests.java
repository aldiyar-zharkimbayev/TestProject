package org.exmaple;

import org.example.dto.RingBuffer;
import org.example.exception.NoDataException;
import org.example.exception.NoFreeSpaceException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class Tests {

    /**
     * 2) нельзя менять сигнатуру
     */
    /**
     * 1. FIFO
     * 2. Ring buffer, use all space in the buffer. Throw exception in corner cases:  no free space (on put) or data (on get)e.g.
     * created empty buffer with size = 3   _,_,_
     * get -> no data, buffer is empty (exception: non thread-save)
     * put 1 ->     1,_,_
     * put 2 ->     1,2,_
     * put 3 ->     1,2,3
     * put 4 -> buffer is full  (exception: non thread-save)
     * get -> 1     _,2,3
     * put 4 ->    4,2,3
     * get -> 2     4,_,3
     */

    // no data
    @Test
    public void test_no_data() {
        RingBuffer ringBuffer = new RingBuffer(3);
        assertThrows(NoDataException.class, ringBuffer::get);
    }

    // no free space
    // put 4 -> buffer is full
    @Test
    public void test_free_space() {
        RingBuffer ringBuffer = new RingBuffer(3);
        ringBuffer.put(1);
        ringBuffer.put(2);
        ringBuffer.put(3);
        assertThrows(NoFreeSpaceException.class, () -> ringBuffer.put(4));
    }

    @Test
    public void tests() {
        RingBuffer ringBuffer = new RingBuffer(3);

        assertThrows(NoDataException.class, ringBuffer::get);

        ringBuffer.put(1);
        assertEquals("[1, _, _]", ringBuffer.toString());

        ringBuffer.put(2);
        assertEquals("[1, 2, _]", ringBuffer.toString());

        ringBuffer.put(3);
        assertEquals("[1, 2, 3]", ringBuffer.toString());

        assertThrows(NoFreeSpaceException.class, () -> ringBuffer.put(4));

        assertEquals(1, ringBuffer.get());
        assertEquals("[_, 2, 3]", ringBuffer.toString());

        ringBuffer.put(4);
        assertEquals("[4, 2, 3]", ringBuffer.toString());

        assertEquals(2, ringBuffer.get());
        assertEquals("[4, _, 3]", ringBuffer.toString());
    }

    // put 1 ->     1,_,_
    @Test
    public void test_put_1() {
        RingBuffer ringBuffer = new RingBuffer(3);
        ringBuffer.put(1);
        Object o = ringBuffer.get();
        assertEquals(1, o);
        assertThrows(NoDataException.class, ringBuffer::get);
    }

    // put 2 ->     1,2,_
    @Test
    public void test_put_2() {
        RingBuffer ringBuffer = new RingBuffer(3);
        ringBuffer.put(1);
        ringBuffer.put(2);
        Object o = ringBuffer.get();
        assertEquals(1, o);
        o = ringBuffer.get();
        assertEquals(2, o);
        assertThrows(NoDataException.class, ringBuffer::get);
    }

    // put 3 ->     1,2,3
    @Test
    public void test_put_3() {
        RingBuffer ringBuffer = new RingBuffer(3);
        ringBuffer.put(1);
        ringBuffer.put(2);
        ringBuffer.put(3);
        Object o = ringBuffer.get();
        assertEquals(1, o);
        o = ringBuffer.get();
        assertEquals(2, o);
        o = ringBuffer.get();
        assertEquals(3, o);
        assertThrows(NoDataException.class, ringBuffer::get);
    }

    // get -> 1     _,2,3
    @Test
    public void test_get_1() {
        RingBuffer ringBuffer = new RingBuffer(3);
        ringBuffer.put(1);
        ringBuffer.put(2);
        ringBuffer.put(3);
        Object o = ringBuffer.get();
        assertEquals(1, o);
        o = ringBuffer.get();
        assertEquals(2, o);
    }

    // put 4 ->    4,2,3
    @Test
    public void test_put_4() {
        RingBuffer ringBuffer = new RingBuffer(3);
        ringBuffer.put(1);
        ringBuffer.put(2);
        ringBuffer.put(3);
        Object o = ringBuffer.get();
        assertEquals(1, o);
        ringBuffer.put(4);
        o = ringBuffer.get();
        assertEquals(2, o);
        o = ringBuffer.get();
        assertEquals(3, o);
        o = ringBuffer.get();
        assertEquals(4, o);
    }

    // get -> 2     4,_,3
    @Test
    public void test_get_2() {
        RingBuffer ringBuffer = new RingBuffer(3);
        ringBuffer.put(1);
        ringBuffer.put(2);
        ringBuffer.put(3);
        Object o = ringBuffer.get();
        assertEquals(1, o);
        ringBuffer.put(4);
        o = ringBuffer.get();
        assertEquals(2, o);
    }

}
