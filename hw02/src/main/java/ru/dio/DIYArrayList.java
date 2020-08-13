package ru.dio;

import java.util.*;

public class DIYArrayList<T> implements List<T> {

    private final static int DEFAULT_CAPACITY = 10;
    private T[] elements;
    private int size = 0;

    public DIYArrayList() {
        this(DEFAULT_CAPACITY);
    }

    @SuppressWarnings("unchecked")
    public DIYArrayList(int capacity) {
        if (capacity < 0) throw new IllegalArgumentException("Capacity should be zero or greater");
        if (capacity == 0) {
            elements = (T[]) new Object[]{};
        } else {
            elements = (T[]) new Object[capacity];
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(elements, size);
    }

    @Override
    public boolean add(T t) {
        if (arrayIsFull()) {
            increaseCapacity();
        }
        elements[size++] = t;
        return true;
    }

    private boolean arrayIsFull() {
        return size == elements.length;
    }

    @SuppressWarnings("unchecked")
    private void increaseCapacity() {
        int currentCapacity = elements.length;
        int minimumCapacity = 1 + currentCapacity;
        int optimalCapacity = currentCapacity >> 1 + currentCapacity;

        if (currentCapacity > 0) {
            int capacity = Math.max(minimumCapacity, optimalCapacity);
            elements = Arrays.copyOf(elements, capacity);
        } else {
            elements = (T[]) new Object[DEFAULT_CAPACITY];
        }
    }


    @Override
    public T get(int index) {
        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException("Wrong index");
        }
        return elements[index];
    }

    @Override
    public T set(int index, T element) {
        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException("Wrong index");
        }
        return elements[index] = element;
    }

    @Override
    public ListIterator<T> listIterator() {
        return new ListIteratorImpl();
    }

    @Override
    public Iterator<T> iterator() {
        return new IteratorImpl();
    }

    class IteratorImpl implements Iterator<T> {

        int pointer = -1;

        @Override
        public boolean hasNext() {
            return pointer < size -1;
        }

        @Override
        public T next() {
            if (pointer >= size) {
                throw new NoSuchElementException();
            }
            pointer++;
            return elements[pointer];
        }
    }

    class ListIteratorImpl extends IteratorImpl implements ListIterator<T> {

        @Override
        public boolean hasPrevious() {
            throw new UnsupportedOperationException();
        }

        @Override
        public T previous() {
            throw new UnsupportedOperationException();
        }

        @Override
        public int nextIndex() {
            return pointer + 1;
        }

        @Override
        public int previousIndex() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void set(T t) {
            if (pointer < 0)
                throw new IllegalStateException();
            elements[pointer] = t;
        }

        @Override
        public void add(T t) {
            throw new UnsupportedOperationException();
        }
    }


    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(int index, T element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T remove(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int indexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int lastIndexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }


}