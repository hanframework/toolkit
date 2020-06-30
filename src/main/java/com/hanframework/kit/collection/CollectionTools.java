package com.hanframework.kit.collection;

import java.util.*;

/**
 * @author liuxin
 * 2019-07-21 19:42
 */
public final class CollectionTools {

    public static <T> List<T> asList(Collection<T> collection) {
        return new ArrayList<>(collection);
    }

    public static <T> Set<T> asSet(Collection<T> collection) {
        return new LinkedHashSet<>(collection);
    }

    public static Integer min(List<Integer> list) {
        list.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.intValue() - o2.intValue();
            }
        });
        return list.get(0);
    }



    public static Integer  max(List<Integer> list) {
        list.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2.intValue() - o1.intValue();
            }
        });
        return list.get(0);
    }

    public static void main(String[] args) {
        List<Integer> s = Arrays.asList(1, 2, 3, 4);
        System.out.println(max(s));
        System.out.println(min(s));
    }

}
