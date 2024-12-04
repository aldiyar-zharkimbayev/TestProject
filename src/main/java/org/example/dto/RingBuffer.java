package org.example.dto;

import org.example.exception.NoDataException;
import org.example.exception.NoFreeSpaceException;

import java.util.Arrays;

public class RingBuffer {

    private final Object[] data;
    private int writeIdx = 0;
    private int readIdx = 0;
    private int contains = 0;

    public RingBuffer(int maxBufferSize) {
        data = new Object[maxBufferSize];
        Arrays.fill(data, new Empty());
    }

    public void put(Object o) {
        if (contains == this.data.length) {
            throw new NoFreeSpaceException();
        }
        this.data[writeIdx] = o;
        contains++;
        writeIdx = (writeIdx + 1) % this.data.length;
    }

    public Object get() {
        if (contains == 0) {
            throw new NoDataException();
        }
        Object result = this.data[readIdx];
        this.data[readIdx] = new Empty();
        contains--;
        readIdx = (readIdx + 1) % this.data.length;
        return result;
    }

    @Override
    public String toString() {
        return Arrays.toString(this.data);
    }

}
