package com.hanframework.kit.stream;
import java.util.DuplicateFormatFlagsException;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
/**
 * @author liuxin
 */
public class StreamFilter {

    /**
     * 根据条件生成List集合
     *
     * @param dataStream 数据流
     * @param predicate  过滤条件
     * @param <T>        数据泛型
     * @return List
     */
    public static <T> List<T> filter(Stream<T> dataStream, Predicate<? super T> predicate) {
        return dataStream.filter(predicate).collect(Collectors.toList());
    }

    /**
     * @param dataStream   数据流
     * @param predicate    过滤条件
     * @param applyMapping 数据类型转换函数
     * @param <T>
     * @param <V>
     * @return
     */
    public static <T, V> List<V> filter(Stream<T> dataStream, Predicate<? super T> predicate,
                                        Function<? super T, ? extends V> applyMapping) {
        return dataStream.filter(predicate).map(applyMapping).collect(Collectors.toList());
    }

    /**
     * 查询唯一数据
     *
     * @param dataStream 数据流
     * @param predicate  过滤条件
     * @param <T>        数据泛型
     * @return List
     */
    public static <T> Optional<T> filterSingle(Stream<T> dataStream, Predicate<? super T> predicate) {
        List<T> filter = filter(dataStream, predicate);
        if (filter == null || filter.isEmpty()) {
            return Optional.empty();
        }
        if (filter.size() > 1) {
            throw new DuplicateFormatFlagsException(filter.toString());
        }
        return Optional.ofNullable(filter.get(0));
    }

    /**
     * 查询唯一数据
     *
     * @param dataStream 数据流
     * @param predicate  过滤条件
     * @param <T>        数据泛型
     * @return List
     */
    public static <T, E extends Throwable> T filterSingle(Stream<T> dataStream,
                                                          Predicate<? super T> predicate,
                                                          Supplier<? extends E> exceptionSupplier) throws E {
        List<T> filter = filter(dataStream, predicate);
        if (filter == null || filter.isEmpty()) {
            throw exceptionSupplier.get();
        }
        if (filter.size() > 1) {
            throw exceptionSupplier.get();
        }
        return filter.get(0);
    }

}

