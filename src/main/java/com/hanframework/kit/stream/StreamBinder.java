package com.hanframework.kit.stream;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author liuxin
 * 数据分组或绑定操作
 * 使用场景:
 * 1. 分组
 * 2. 分组并对数据结构调整
 * 3. 数据拆分绑定(注意key相同情况会覆盖)
 */
public class StreamBinder {

    /**
     * one to one
     * 拆解,将数据拆解成一一对应的关系
     *
     * @param dataSourceStream 数据流
     * @param keyApply         key生成器
     * @param <K>              key
     * @param <T>              数据源
     * @return Map
     */
    public static <K, T> Map<K, T> dismantling(Stream<T> dataSourceStream,
                                               Function<? super T, ? extends K> keyApply) {
        return dismantling(dataSourceStream, keyApply, Function.identity());
    }


    /**
     * one to one
     * 拆解,将数据拆解成一一对应的关系
     *
     * @param dataSourceStream  数据流
     * @param keyApply          key生成器
     * @param exceptionSupplier 异常生成
     * @param <K>               key
     * @param <T>               数据源
     * @param <X>               异常泛型
     * @return Map
     * @throws X Throwable
     */
    public static <K, T, X extends Throwable> Map<K, T> dismantling(Stream<T> dataSourceStream,
                                                                    Function<? super T, ? extends K> keyApply,
                                                                    Supplier<? extends X> exceptionSupplier) throws X {
        return dismantling(dataSourceStream, keyApply, t -> t, exceptionSupplier);
    }

    /**
     * one to one
     * 拆解,将数据拆解成一一对应的关系,如果出现一对多，只去第一个
     *
     * @param dataSourceStream 数据流
     * @param keyApply         key生成器
     * @param valueApply       value生成器
     * @param <K>              key
     * @param <V>              value
     * @param <T>              数据源
     * @return Map
     */
    public static <K, V, T> Map<K, V> dismantlingFirst(Stream<T> dataSourceStream, Function<? super T, ? extends K> keyApply,
                                                       Function<? super T, ? extends V> valueApply) {
        Map<? extends K, List<T>> group = group(dataSourceStream, keyApply);
        Map<K, V> result = new HashMap<>(group.size());
        for (Map.Entry<? extends K, List<T>> entry : group.entrySet()) {
            K key = entry.getKey();
            List<T> oldValues = entry.getValue();
            if (oldValues == null || oldValues.isEmpty()) {
                continue;
            }
            V newValue = valueApply.apply(oldValues.get(0));
            result.put(key, newValue);
        }
        return result;
    }

    /**
     * one to one
     * 拆解,将数据拆解成一一对应的关系
     *
     * @param dataSourceStream 数据流
     * @param keyApply         key生成器
     * @param valueApply       value生成器
     * @param <K>              key
     * @param <V>              value
     * @param <T>              数据源
     * @return Map
     */
    public static <K, V, T> Map<K, V> dismantling(Stream<T> dataSourceStream, Function<? super T, ? extends K> keyApply,
                                                  Function<? super T, ? extends V> valueApply) {
        Map<? extends K, List<T>> group = group(dataSourceStream, keyApply);
        Map<K, V> result = new HashMap<>(group.size());
        for (Map.Entry<? extends K, List<T>> entry : group.entrySet()) {
            K key = entry.getKey();
            List<T> oldValues = entry.getValue();
            if (oldValues == null || oldValues.isEmpty()) {
                continue;
            }
            if (oldValues.size() > 1) {
                throw new DuplicateFormatFlagsException(String.format("数据重复,请检查绑定key=%s,value=[%s]", key, oldValues));
            }
            V newValue = valueApply.apply(oldValues.get(0));
            result.put(key, newValue);
        }
        return result;
    }

    /**
     * one to one
     * 拆解,将数据拆解成一一对应的关系
     *
     * @param dataSourceStream  数据流
     * @param keyApply          key生成器
     * @param valueApply        value生成器
     * @param exceptionSupplier 异常生成
     * @param <K>               key
     * @param <V>               value
     * @param <T>               数据源
     * @param <X>               异常泛型
     * @return Map
     * @throws X Throwable
     */
    public static <K, V, T, X extends Throwable> Map<K, V> dismantling(Stream<T> dataSourceStream,
                                                                       Function<? super T, ? extends K> keyApply,
                                                                       Function<? super T, ? extends V> valueApply,
                                                                       Supplier<? extends X> exceptionSupplier) throws X {
        Map<? extends K, List<T>> group = group(dataSourceStream, keyApply);
        Map<K, V> result = new HashMap<>(group.size());
        for (Map.Entry<? extends K, List<T>> entry : group.entrySet()) {
            K key = entry.getKey();
            List<T> oldValues = entry.getValue();
            if (oldValues == null || oldValues.isEmpty()) {
                continue;
            }
            if (oldValues.size() > 1) {
                throw exceptionSupplier.get();
            }
            V newValue = valueApply.apply(oldValues.get(0));
            result.put(key, newValue);
        }
        return result;
    }


    /**
     * one to many
     * 分组并对分组后的数据进行结构处理,注意出现相同的KEY值,会覆盖
     *
     * @param dataSourceStream 数据流
     * @param keyApply         key生成器
     * @param valueApply       value生成器
     * @param <K>              key
     * @param <V>              value
     * @param <T>              数据源
     * @return Map
     */
    public static <K, V, T> Map<K, List<V>> group(Stream<T> dataSourceStream, Function<? super T, ? extends K> keyApply,
                                                  Function<? super T, ? extends V> valueApply) {
        Map<K, List<T>> beforeGroup = group(dataSourceStream, keyApply);
        Map<K, List<V>> afterGroup = new HashMap<>(beforeGroup.size());
        for (Map.Entry<K, List<T>> kListEntry : beforeGroup.entrySet()) {
            K groupKey = kListEntry.getKey();
            List<T> groupValues = kListEntry.getValue();
            List<V> collect = groupValues.stream().map(valueApply).collect(Collectors.toList());
            afterGroup.put(groupKey, collect);
        }
        return afterGroup;
    }

    /**
     * one to many
     * 分组根据相同条件分组
     *
     * @param dataSourceStream 数据流
     * @param keyApply key转换器
     * @param <K>              key
     * @param <T>              value
     * @return Map
     */
    public static <K, T> Map<K, List<T>> group(Stream<T> dataSourceStream,
                                               Function<? super T, ? extends K> keyApply) {
        List<T> dataSources = dataSourceStream.collect(Collectors.toList());
        Map<K, List<T>> result = new HashMap<>(dataSources.size());
        for (T dataSource : dataSources) {
            K key = keyApply.apply(dataSource);
            List<T> ts = result.get(key);
            if (ts == null || ts.isEmpty()) {
                ArrayList<T> values = new ArrayList<>();
                values.add(dataSource);
                result.put(key, values);
            } else {
                ts.add(dataSource);
            }
        }
        return result;
    }


    public static void main(String[] args) {
//        List<Person> people = Arrays.asList(new Person(1), new Person(1), new Person(2), new Person(3), new Person(4),
//                new Person(5));
//
//        //1. 数据分组
//        Map<Integer, List<Person>> group0 = group(people.stream(), Person::getAge);
//        //{1=[KuCun{name=1}, KuCun{name=1}], 2=[KuCun{name=2}], 3=[KuCun{name=3}], 4=[KuCun{name=4}], 5=[KuCun{name=5}]}
//        System.out.println(group0);
//
//        //2. 数据分组并对分组后的数据结构进行处理
//        Map<Integer, List<String>> group1 = group(people.stream(), Person::getAge, Person::getName);
//        //{1=[张1, 张1], 2=[张2], 3=[张3], 4=[张4], 5=[张5]}
//        System.out.println(group1);
//
//        //3. 对数据拆分绑定,将age 与 name绑定到一起
//        //注意出现相同的key值会覆盖处理
//        Map<Integer, String> dismantling = dismantling(people.stream(), Person::getAge, Person::getName);
//        //{1=张1, 2=张2, 3=张3, 4=张4, 5=张5}
//        System.out.println(dismantling);

    }
}
