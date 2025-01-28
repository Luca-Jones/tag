package utils;

import java.util.concurrent.ConcurrentSkipListSet;

public class Tests {
    public static void main(String[] args) {
        
        ConcurrentSkipListSet<A> set = new ConcurrentSkipListSet<>();
        ConcurrentSortedList<A> list = new ConcurrentSortedList<>();
        A x = new A();
        A y = new A();
        System.out.println(x.equals(y));
        System.out.println(y.equals(x));
        System.out.println(x.compareTo(y) == 0);
        System.out.println(y.compareTo(x) == 0);
        set.add(x);
        System.out.println(set.contains(x));
        System.out.println(set.contains(y));
        list.add(x);
        System.out.println(list.contains(x));
        System.out.println(list.contains(y));
    }
}

class A implements Comparable<A> {
    public A() {}

    @Override
    public int compareTo(A o) {
        return 0;
    }
}
