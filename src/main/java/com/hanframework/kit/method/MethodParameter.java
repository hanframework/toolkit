package com.hanframework.kit.method;


import com.hanframework.kit.asserts.Assert;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author liuxin
 * @version Id: MethodParameter.java, v 0.1 2018/11/19 3:12 PM
 */
public class MethodParameter {

    /**
     * 方法名字
     */
    private final Method method;

    /**
     * 构造方法
     */
    private final Constructor<?> constructor;

    /**
     * 方法索引
     */
    private final int parameterIndex;

    private Class<?> containingClass;

    /**
     * 方法类型
     */
    private Class<?> parameterType;

    private Type genericParameterType;

    /**
     * 方法上注解
     */
    private Annotation[] parameterAnnotations;

    private ParameterNameDiscoverer parameterNameDiscoverer;

    /**
     * 方法名字
     */
    private String parameterName;

    /**
     * 默认为直接参数
     * 当大于1则返回方法参数的泛型
     */
    private int nestingLevel = 1;

    /**
     * Map from Integer level to Integer type index
     */
    Map<Integer, Integer> typeIndexesPerLevel;


    /**
     * 根据方法索引构造方法参数
     *
     * @param method         方法
     * @param parameterIndex 方法参数索引
     */
    public MethodParameter(Method method, int parameterIndex) {
        this(method, parameterIndex, 1);
    }

    /**
     * 根据方法索引构造方法参数
     *
     * @param method         方法
     * @param parameterIndex 方法参数索引
     * @param nestingLevel   等级
     */
    public MethodParameter(Method method, int parameterIndex, int nestingLevel) {
        Assert.notNull(method, "Method must not be null");
        this.method = method;
        this.parameterIndex = parameterIndex;
        this.nestingLevel = nestingLevel;
        this.constructor = null;
    }

    /**
     * 构造参数
     *
     * @param constructor    构造
     * @param parameterIndex 构造参数索引
     */
    public MethodParameter(Constructor<?> constructor, int parameterIndex) {
        this(constructor, parameterIndex, 1);
    }

    /**
     * 构造参数
     *
     * @param constructor    构造
     * @param parameterIndex 构造参数索引
     * @param nestingLevel   等级
     */
    public MethodParameter(Constructor<?> constructor, int parameterIndex, int nestingLevel) {
        Assert.notNull(constructor, "Constructor must not be null");
        this.constructor = constructor;
        this.parameterIndex = parameterIndex;
        this.nestingLevel = nestingLevel;
        this.method = null;
    }

    /**
     * 从原始对象重新构建一个新对象
     *
     * @param original 原始对象
     */
    public MethodParameter(MethodParameter original) {
        Assert.notNull(original, "Original must not be null");
        this.method = original.method;
        this.constructor = original.constructor;
        this.parameterIndex = original.parameterIndex;
        this.containingClass = original.containingClass;
        this.parameterType = original.parameterType;
        this.genericParameterType = original.genericParameterType;
        this.parameterAnnotations = original.parameterAnnotations;
        this.parameterNameDiscoverer = original.parameterNameDiscoverer;
        this.parameterName = original.parameterName;
        this.nestingLevel = original.nestingLevel;
        this.typeIndexesPerLevel = original.typeIndexesPerLevel;
    }


    /**
     * 返回当前方法,当为构造参数时候,会返回null
     *
     * @return method
     */
    public Method getMethod() {
        return this.method;
    }

    /**
     * 返回构造方法,当为普通方法时候,会返回null
     *
     * @return Constructor
     */
    public Constructor<?> getConstructor() {
        return this.constructor;
    }

    /**
     * 用来判断是构造方法,还是普通方法
     * @return Member
     */
    public Member getMember() {
        if (this.method != null) {
            return this.method;
        } else {
            return this.constructor;
        }
    }

    /**
     * 返回两者的注解信息接口
     *
     * @return 两者的注解信息接口
     */
    public AnnotatedElement getAnnotatedElement() {
        if (this.method != null) {
            return this.method;
        } else {
            return this.constructor;
        }
    }

    /**
     * 当前方法所属于的对象类型
     *
     * @return class
     */
    public Class<?> getDeclaringClass() {
        return getMember().getDeclaringClass();
    }

    /**
     * 方法或者构造方法参数的索引位置
     *
     * @return int
     */
    public int getParameterIndex() {
        return this.parameterIndex;
    }

    /**
     * Set a containing class to resolve the parameter type against.
     */
    void setContainingClass(Class<?> containingClass) {
        this.containingClass = containingClass;
    }

    public Class<?> getContainingClass() {
        return (this.containingClass != null ? this.containingClass : getDeclaringClass());
    }

    /**
     * 手动设置参数类型
     */
    void setParameterType(Class<?> parameterType) {
        this.parameterType = parameterType;
    }

    /**
     * 返回参数类型
     *
     * @return the parameter type (never {@code null})
     */
    public Class<?> getParameterType() {
        if (this.parameterType == null) {
            if (this.parameterIndex < 0) {
                this.parameterType = (this.method != null ? this.method.getReturnType() : null);
            } else {
                this.parameterType = (this.method != null ?
                        this.method.getParameterTypes()[this.parameterIndex] :
                        this.constructor.getParameterTypes()[this.parameterIndex]);
            }
        }
        return this.parameterType;
    }

    /**
     * 返回泛型参数类型
     *
     * @return the parameter type (never {@code null})
     */
    public Type getGenericParameterType() {
        if (this.genericParameterType == null) {
            if (this.parameterIndex < 0) {
                this.genericParameterType = (this.method != null ? this.method.getGenericReturnType() : null);
            } else {
                this.genericParameterType = (this.method != null ?
                        this.method.getGenericParameterTypes()[this.parameterIndex] :
                        this.constructor.getGenericParameterTypes()[this.parameterIndex]);
            }
        }
        return this.genericParameterType;
    }


    public Class<?> getNestedParameterType() {
        if (this.nestingLevel > 1) {
            Type type = getGenericParameterType();
            if (type instanceof ParameterizedType) {
                Integer index = getTypeIndexForCurrentLevel();
                Type[] args = ((ParameterizedType) type).getActualTypeArguments();
                Type arg = args[index != null ? index : args.length - 1];
                if (arg instanceof Class) {
                    return (Class<?>) arg;
                } else if (arg instanceof ParameterizedType) {
                    arg = ((ParameterizedType) arg).getRawType();
                    if (arg instanceof Class) {
                        return (Class<?>) arg;
                    }
                }
            }
            return Object.class;
        } else {
            return getParameterType();
        }
    }

    /**
     * 返回方法注解信息
     * @return Annotation[]
     */
    public Annotation[] getMethodAnnotations() {
        return getAnnotatedElement().getAnnotations();
    }

    /**
     * 查询指定的注解信息
     *
     * @param annotationType 注解类型
     * @param <T>            泛型
     * @return 注解信息
     */
    public <T extends Annotation> T getMethodAnnotation(Class<T> annotationType) {
        return getAnnotatedElement().getAnnotation(annotationType);
    }

    /**
     * 获取方法或者是构造方法参数注解
     *
     * @return 注解数组
     */
    public Annotation[] getParameterAnnotations() {
        if (this.parameterAnnotations == null) {
            Annotation[][] annotationArray = (this.method != null ?
                    this.method.getParameterAnnotations() : this.constructor.getParameterAnnotations());
            if (this.parameterIndex >= 0 && this.parameterIndex < annotationArray.length) {
                this.parameterAnnotations = annotationArray[this.parameterIndex];
            } else {
                this.parameterAnnotations = new Annotation[0];
            }
        }
        return this.parameterAnnotations;
    }

    /**
     * 从参数上,获取指定的注解信息
     *
     * @param annotationType 注解类型
     * @param <T>            泛型
     * @return 注解信息
     */
    @SuppressWarnings("unchecked")
    public <T extends Annotation> T getParameterAnnotation(Class<T> annotationType) {
        Annotation[] anns = getParameterAnnotations();
        for (Annotation ann : anns) {
            if (annotationType.isInstance(ann)) {
                return (T) ann;
            }
        }
        return null;
    }

    /**
     * 是否存在注解信息
     *
     * @return boolean
     */
    public boolean hasParameterAnnotations() {
        return (getParameterAnnotations().length != 0);
    }

    /**
     * 是否存在指定的注解信息
     *
     * @param annotationType 注解类型
     * @param <T>            泛型
     * @return 注解信息
     */
    public <T extends Annotation> boolean hasParameterAnnotation(Class<T> annotationType) {
        return (getParameterAnnotation(annotationType) != null);
    }

    /**
     * 设置参数名获取器
     *
     * @param parameterNameDiscoverer 方法名获取
     */
    public void initParameterNameDiscovery(ParameterNameDiscoverer parameterNameDiscoverer) {
        this.parameterNameDiscoverer = parameterNameDiscoverer;
    }

    /**
     * 获取参数变量名
     *
     * @return string
     */
    public String getParameterName() {
        if (this.parameterNameDiscoverer != null) {
            String[] parameterNames = (this.method != null ?
                    this.parameterNameDiscoverer.getParameterNames(this.method) :
                    this.parameterNameDiscoverer.getParameterNames(this.constructor));
            if (parameterNames != null) {
                this.parameterName = parameterNames[this.parameterIndex];
            }
            this.parameterNameDiscoverer = null;
        }
        return this.parameterName;
    }

    /**
     * Increase this parameter's nesting level.
     *
     * @see #getNestingLevel()
     */
    public void increaseNestingLevel() {
        this.nestingLevel++;
    }

    /**
     * Decrease this parameter's nesting level.
     *
     * @see #getNestingLevel()
     */
    public void decreaseNestingLevel() {
        getTypeIndexesPerLevel().remove(this.nestingLevel);
        this.nestingLevel--;
    }

    /**
     * Return the nesting level of the target type
     * (typically 1; e.g. in case of a List of Lists, 1 would indicate the
     * nested List, whereas 2 would indicate the element of the nested List).
     * @return int
     */
    public int getNestingLevel() {
        return this.nestingLevel;
    }

    /**
     * 设置参数类型
     *
     * @param typeIndex 类型索引
     */
    public void setTypeIndexForCurrentLevel(int typeIndex) {
        getTypeIndexesPerLevel().put(this.nestingLevel, typeIndex);
    }

    /**
     * Return the type index for the current nesting level.
     *
     * @return the corresponding type index, or {@code null}
     * if none specified (indicating the default type index)
     * @see #getNestingLevel()
     */
    public Integer getTypeIndexForCurrentLevel() {
        return getTypeIndexForLevel(this.nestingLevel);
    }

    /**
     * Return the type index for the specified nesting level.
     *
     * @param nestingLevel the nesting level to check
     * @return the corresponding type index, or {@code null}
     * if none specified (indicating the default type index)
     */
    public Integer getTypeIndexForLevel(int nestingLevel) {
        return getTypeIndexesPerLevel().get(nestingLevel);
    }

    /**
     * Obtain the (lazily constructed) type-indexes-per-level Map.
     */
    private Map<Integer, Integer> getTypeIndexesPerLevel() {
        if (this.typeIndexesPerLevel == null) {
            this.typeIndexesPerLevel = new HashMap<Integer, Integer>(4);
        }
        return this.typeIndexesPerLevel;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && obj instanceof MethodParameter) {
            MethodParameter other = (MethodParameter) obj;
            return (this.parameterIndex == other.parameterIndex && getMember().equals(other.getMember()));
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (getMember().hashCode() * 31 + this.parameterIndex);
    }


    /**
     * 自动从方法或者构造方法上获取参数细腻
     *
     * @param methodOrConstructor 方法或者构造方法对象
     * @param parameterIndex      方法参数索引
     * @return MethodParameter
     */
    public static MethodParameter forMethodOrConstructor(Object methodOrConstructor, int parameterIndex) {
        if (methodOrConstructor instanceof Method) {
            return new MethodParameter((Method) methodOrConstructor, parameterIndex);
        } else if (methodOrConstructor instanceof Constructor) {
            return new MethodParameter((Constructor<?>) methodOrConstructor, parameterIndex);
        } else {
            throw new IllegalArgumentException(
                    "Given object [" + methodOrConstructor + "] is neither a Method nor a Constructor");
        }
    }


}
