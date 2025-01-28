package utils;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

/**
 * Represents a collection of elements that are sorted. Duplicates are
 * permitted. This collection is sorted in ascending order according to their
 * natural ordering on insertion.
 * WARNING: this collection is not safe from concurrent modification!
 */
public class SortedCollection<T extends Comparable<T>> implements Collection<T> {

    /**
     * Rep Invariant:
     * elements are sorted in ascending order according to their natural ordering.
     */
    private List<T> elements;

    public void checkRep() {
        for (int i = 0; i < elements.size() - 1; i++) {
            assert elements.get(i).compareTo(elements.get(i + 1)) <= 0;
        }
    }

    public SortedCollection() {
        this.elements = Collections.synchronizedList(new ArrayList<>());
    }

    @Override
    public int size() {
        return elements.size();
    }

    @Override
    public boolean isEmpty() {
        return elements.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        @SuppressWarnings("unchecked")
        T e = (T) o;
        return binarySearch(0, size(), e) != -1;
    }

    /**
     * @param start starting index (inclusive)
     * @param end   ending index (inclusive)
     * @param e     element to search for
     * @return the index of e or -1 if not in the collection
     */
    private int binarySearch(int start, int end, T e) {

        if (start == end)
            return (elements.get(start).equals(e)) ? start : -1;

        int mid = (start + end) / 2; // integer overflow not an issue

        if (elements.get(mid).compareTo(e) < 0) {
            return binarySearch(mid + 1, end, e);
        } else if (elements.get(mid).compareTo(e) > 0) {
            return binarySearch(start, mid, e);
        } else {
            return mid;
        }
    }

    /**
     * @param e     element to insert
     * @param start (inclusive)
     * @param end   (exclusive)
     * @return the index of the minimum element greater than e
     */
    private int minBinarySearch(int start, int end, T e) {

        if (start == end)
            return start;

        int mid = (start + end) / 2; // integer overflow not an issue

        if (elements.get(mid).compareTo(e) < 0) {
            return minBinarySearch(mid + 1, end, e);
        } else if (elements.get(mid).compareTo(e) > 0) {
            return minBinarySearch(start, mid, e);
        } else {
            return mid;
        }
    }

    @Override
    public Iterator<T> iterator() {
        return elements.iterator();
    }

    @Override
    public Object[] toArray() {
        return elements.toArray();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object[] toArray(Object[] a) {
        return elements.toArray(a);
    }

    @Override
    public boolean add(T e) {
        int i = minBinarySearch(0, size(), e);
        elements.add(i, e);
        return true;
    }

    @Override
    public boolean remove(Object o) {
        @SuppressWarnings("unchecked")
        int i = binarySearch(0, size(), (T) o);
        return elements.remove(i) != null;
    }

    @Override
    public boolean containsAll(@SuppressWarnings("rawtypes") Collection c) {
        return elements.containsAll(c);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean addAll(@SuppressWarnings("rawtypes") Collection c) {
        return elements.addAll(c);
    }

    @Override
    public boolean removeAll(@SuppressWarnings("rawtypes") Collection c) {
        return elements.removeAll(c);
    }

    @Override
    public boolean retainAll(@SuppressWarnings("rawtypes") Collection c) {
        return elements.retainAll(c);
    }

    @Override
    public void clear() {
        elements.clear();
    }

}
