package com.hanframework.kit.stream;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author liuxin
 */
public class EnhanceStream<T> {


    public static <T, R> List<R> mapToList(Stream<T> dataStream, Function<T, R> mapping) {
        return dataStream.map(mapping).collect(Collectors.toList());
    }

    static <T, R> Set<R> mapToSet(Stream<T> dataStream, Function<T, R> mapping) {
        return dataStream.map(mapping).collect(Collectors.toSet());
    }

    static <T, R> List<R> distinctMapToList(Stream<T> dataStream, Function<T, R> mapping) {
        return dataStream.map(mapping).distinct().collect(Collectors.toList());
    }


    /**
     * 对数据流进行过滤
     *
     * @param dataStream        数据流
     * @param predicate         过滤条件
     * @param exceptionSupplier 异常生成
     * @param <T>               数据泛型
     * @param <X>               异常泛型
     * @return List      过滤后的数据条件
     * @throws X Throwable
     */
    public static <T, X extends Throwable> T filterSingle(Stream<T> dataStream, Predicate<? super T> predicate,
                                                          Supplier<? extends X> exceptionSupplier) throws X {
        return StreamFilter.filterSingle(dataStream, predicate, exceptionSupplier);
    }

    /**
     * 对数据流进行过滤
     *
     * @param dataStream 数据流
     * @param predicate  过滤条件
     * @param <T>        数据泛型
     * @return List      过滤后的数据条件
     */
    public static <T> List<T> filter(Stream<T> dataStream, Predicate<? super T> predicate) {
        return StreamFilter.filter(dataStream, predicate);
    }

    public static <T, X extends Throwable> T findFirst(Stream<T> dataStream, Supplier<? extends X> exceptionSupplier) throws X {
        return dataStream.findFirst().orElseThrow(exceptionSupplier);
    }

    public static <T> Optional<T> findFirst(Stream<T> dataStream) {
        return dataStream.findFirst();
    }

    public static <T> Optional<T> findFirst(List<T> dataList) {
        return dataList.stream().findFirst();
    }

    public static <T> Optional<T> findAny(Stream<T> dataStream) {
        return dataStream.findAny();
    }

    public static <T> Optional<T> findAny(List<T> dataList) {
        return dataList.stream().findAny();
    }

    /**
     * 对数据流进行过滤,并对过滤后的数据进行类型转换
     *
     * @param dataStream   数据流
     * @param predicate    过滤条件
     * @param applyMapping 数据转换器
     * @param <T>          数据泛型
     * @param <V>          转换后的数据类型
     * @return List        过滤后的数据条件
     */
    public static <T, V> List<V> filter(Stream<T> dataStream, Predicate<? super T> predicate,
                                        Function<? super T, ? extends V> applyMapping) {
        return StreamFilter.filter(dataStream, predicate, applyMapping);
    }

    /**
     * 对数据流进行分组
     *
     * @param dataStream 数据流
     * @param keyApply   分组项
     * @param <T>        数据泛型
     * @param <K>        分组项泛型
     * @return Map       分组后数据
     */
    static <T, K> Map<K, List<T>> group(Stream<T> dataStream,
                                        Function<? super T, ? extends K> keyApply) {
        return StreamBinder.group(dataStream, keyApply);
    }

    /**
     * 对数据流进行分组,并对分组后的数据进行类型转换
     *
     * @param dataStream 数据流
     * @param keyApply   分组项
     * @param valueApply 数据转换器
     * @param <T>        数据泛型
     * @param <K>        分组项泛型
     * @param <V>        转换后的数据类型
     * @return Map       分组后数据
     */
    static <T, K, V> Map<K, List<V>> group(Stream<T> dataStream, Function<? super T, ? extends K> keyApply,
                                           Function<? super T, ? extends V> valueApply) {
        return StreamBinder.group(dataStream, keyApply, valueApply);
    }

    /**
     * 数据拆分「注意: 拆分后的数据,如果Key一样会产生异常,请谨慎使用
     *
     * @param dataStream 数据流
     * @param keyApply   Key转换器
     * @param <T>        原始数据泛型
     * @param <K>        Key泛型
     * @return Map       拆分后数据
     */
    public static <T, K> Map<K, T> dismantling(Stream<T> dataStream, Function<? super T, ? extends K> keyApply) {
        return StreamBinder.dismantling(dataStream, keyApply);
    }

    /**
     * 数据拆分「注意: 拆分后的数据,如果Key一样会产生异常,请谨慎使用
     *
     * @param dataStream 数据流
     * @param keyApply   Key转换器
     * @param <T>        原始数据泛型
     * @param <K>        Key泛型
     * @return Map       拆分后数据
     */
    static <T, K, X extends Throwable> Map<K, T> dismantling(Stream<T> dataStream,
                                                             Function<? super T, ? extends K> keyApply,
                                                             Supplier<?
                                                                     extends X> exceptionSupplier) throws X {
        return StreamBinder.dismantling(dataStream, keyApply, exceptionSupplier);
    }

    /**
     * 数据拆分「注意: 拆分后的数据,如果Key一样会产生异常,请谨慎使用
     *
     * @param dataStream 数据流
     * @param keyApply   Key转换器
     * @param valueApply Value转换器
     * @param <T>        原始数据泛型
     * @param <K>        Key泛型
     * @param <V>        Value泛型
     * @return Map       拆分后数据
     */
    static <T, K, V> Map<K, V> dismantling(Stream<T> dataStream, Function<? super T, ? extends K> keyApply,
                                           Function<? super T, ? extends V> valueApply) {
        return StreamBinder.dismantling(dataStream, keyApply, valueApply);
    }

    /**
     * 数据拆分「注意: 拆分后的数据,如果Key一样会产生异常,请谨慎使用
     *
     * @param dataStream 数据流
     * @param keyApply   Key转换器
     * @param valueApply Value转换器
     * @param <T>        原始数据泛型
     * @param <K>        Key泛型
     * @param <V>        Value泛型
     * @param <X>        数据重复产生的异常描述
     * @return Map       拆分后数据
     */
    static <T, K, V, X extends Throwable> Map<K, V> dismantling(Stream<T> dataStream, Function<? super T,
            ? extends K> keyApply,
                                                                Function<? super T, ? extends V> valueApply,
                                                                Supplier<? extends X> exceptionSupplier) throws X {
        return StreamBinder.dismantling(dataStream, keyApply, valueApply, exceptionSupplier);
    }


    private static <T> Stream<T> newStream(List<T> dataSources) {
        if (dataSources == null) {
            return Stream.empty();
        }
        return dataSources.stream();
    }


    /**
     * 对数据流进行过滤,并对过滤后的数据进行类型转换
     *
     * @param dataSources  原始数据流
     * @param predicate    过滤条件
     * @param applyMapping 数据转换器
     * @param <V>          转换后的数据类型
     * @param <T>          原始数据类型
     * @return List        过滤后的数据条件
     */
    public static <T, V> List<V> filter(List<T> dataSources, Predicate<? super T> predicate, Function<? super T, ?
            extends V> applyMapping) {
        return StreamFilter.filter(newStream(dataSources), predicate, applyMapping);
    }

    /**
     * 对数据流进行分组
     *
     * @param dataSources 原始数据流
     * @param keyApply    分组项
     * @param <K>         分组项泛型
     * @return Map       分组后数据
     */
    public <K> Map<K, List<T>> group(List<T> dataSources, Function<? super T, ? extends K> keyApply) {
        return StreamBinder.group(newStream(dataSources), keyApply);
    }

    /**
     * 对数据流进行分组,并对分组后的数据进行类型转换
     *
     * @param dataSources 原始数据流
     * @param keyApply    分组项
     * @param valueApply  数据转换器
     * @param <K>         分组项泛型
     * @param <V>         转换后的数据类型
     * @return Map       分组后数据
     */
    public <K, V> Map<K, List<V>> group(List<T> dataSources, Function<? super T, ? extends K> keyApply,
                                        Function<? super T, ? extends V> valueApply) {
        return StreamBinder.group(newStream(dataSources), keyApply, valueApply);
    }

    /**
     * 数据拆分「注意: 拆分后的数据,如果Key一样会产生异常,请谨慎使用
     *
     * @param dataSources 原始数据
     * @param keyApply    Key转换器
     * @param valueApply  Value转换器
     * @param <K>         Key泛型
     * @param <V>         Value泛型
     * @param <T>         原始数据类型
     * @return Map       拆分后数据
     */
    public static <T, K, V> Map<K, V> dismantling(List<T> dataSources, Function<? super T, ? extends K> keyApply,
                                                  Function<? super T, ? extends V> valueApply) {
        return StreamBinder.dismantling(newStream(dataSources), keyApply, valueApply);
    }


    public static <T> void removeIf(List<T> dataSources, Predicate<T> predicate) {
        if (Objects.nonNull(dataSources) && !dataSources.isEmpty()) {
            dataSources.removeIf(predicate);
        }
    }

    public static <T> void removeNotIf(List<T> dataSources, Predicate<T> predicate) {
        if (Objects.nonNull(dataSources) && !dataSources.isEmpty()) {
            dataSources.removeIf(t -> !predicate.test(t));
        }
    }

    public static void main(String[] args) {
        List<Long> list = new ArrayList<>();
        list.add(1L);
        list.add(2L);

        removeIf(list, (x) -> x.equals(1L));
        System.out.println(list);
        list.clear();
        list.add(1L);
        list.add(2L);
        removeNotIf(list, (x) -> x.equals(1L));
        System.out.println(list);
    }
}
