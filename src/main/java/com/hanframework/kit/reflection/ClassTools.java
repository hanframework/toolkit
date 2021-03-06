package com.hanframework.kit.reflection;


import com.hanframework.kit.asserts.Assert;
import com.hanframework.kit.string.ObjectTools;
import com.hanframework.kit.string.StringTools;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Logger;

/**
 * @author liuxin
 * 2017/11/17 下午10:55
 */
public abstract class ClassTools {

    private static Logger log = Logger.getLogger("ClassTools");

    /**
     * 获取class中带有组件标记的方法
     *
     * @param clazz
     * @param annotationClass
     * @return
     */
    private List<Method> getMethodsWithAnnotation(Class<?> clazz, Class<?> annotationClass) {
        List<Method> res = new LinkedList<>();
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            Annotation[] annotations = method.getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation.annotationType() == annotationClass) {
                    res.add(method);
                    break;
                }
            }
        }
        return res;
    }

    /**
     * 获取原始类型,主要处理从被代理的对象中,获取原始参数
     *
     * @param instance
     * @return
     */
    public static Class<?> getUserClass(Object instance) {
        Assert.notNull(instance, "Instance must not be null");
        return getUserClass(instance.getClass());
    }

    public static Class<?> getUserClass(Class<?> clazz) {
        if (clazz != null && clazz.getName().contains("$$")) {
            Class<?> superclass = clazz.getSuperclass();
            if (superclass != null && Object.class != superclass) {
                return superclass;
            }
        }
        return clazz;
    }

    /**
     * 将 objs,装换targetClass类型的List
     *
     * @param objs
     * @param targetClass
     * @param <T>
     * @return
     */
    public static <T> List<T> cast(Object[] objs, Class<T> targetClass) {
        List<T> methods = new ArrayList<T>();
        Iterator<Object> iterator = Arrays.asList(objs).iterator();
        while (iterator.hasNext()) {
            methods.add(targetClass.cast(iterator.next()));
        }
        return methods;
    }


    public static <T> T cast(Object obj, Class<T> targetClass) {
        return targetClass.cast(obj);
    }

    public static <T> List<T> castByArray(Object[] objs, Class<T> targetClass) {
        List<T> methods = new ArrayList<T>();
        Iterator<Object> iterator = Arrays.asList(objs).iterator();
        while (iterator.hasNext()) {
            methods.add(targetClass.cast(iterator.next()));
        }
        return methods;
    }

    /**
     * ClassUtils.getShortName() //获取短类名，如上例中的：Required
     * ClassUtils.getClassFileName() //获取类文件名，如上例中的：Required.class
     * ClassUtils.getPackageName() //获取包，如上例中的：cps.apm.util.fileprocessor.annotation
     * ClassUtils.getQualifiedName() //获取包名+类名，如上例中的：cps.apm.util.fileprocessor.annotation.Required
     * Assert
     *
     * @param clazz
     * @return
     */
    public static String getShortName(Class<?> clazz) {
        if (ObjectTools.isNotEmpty(clazz)) {
            return clazz.getSimpleName();
        }
        return null;
    }

    /**
     * 获取类文件名，如上例中的：Required.class
     *
     * @param clazz
     * @return
     */
    public static String getClassFileName(Class<?> clazz) {
        if (ObjectTools.isNotEmpty(clazz)) {
            String className = clazz.getName();
            int lastDotIndex = className.lastIndexOf(46);
            return className.substring(lastDotIndex + 1) + ".class";
        }
        return null;
    }

    /**
     * 获取包，如上例中的：org.smileframework.tool.asserts
     *
     * @param clazz 类型
     * @return string
     */
    public static String getPackageName(Class<?> clazz) {
        if (ObjectTools.isNotEmpty(clazz)) {
            String fqClassName = clazz.getName();
            int lastDotIndex = fqClassName.lastIndexOf(46);
            return lastDotIndex != -1 ? fqClassName.substring(0, lastDotIndex) : "";
        }
        return null;
    }

    /**
     * org.smileframework.tool.asserts.Assert
     *
     * @param clazz 类型
     * @return string
     */
    public static String getQualifiedName(Class<?> clazz) {
        return clazz.isArray() ? getQualifiedNameForArray(clazz) : clazz.getName();
    }

    private static String getQualifiedNameForArray(Class<?> clazz) {
        StringBuilder result = new StringBuilder();

        while (clazz.isArray()) {
            clazz = clazz.getComponentType();
            result.append("[]");
        }

        result.insert(0, clazz.getName());
        return result.toString();
    }

    /**
     * 获取文件包名
     *
     * @param clazz 类型
     * @return string
     */
    public static String classPackageAsResourcePath(Class<?> clazz) {
        if (clazz == null) {
            return "";
        } else {
            String className = clazz.getName();
            int packageEndIndex = className.lastIndexOf(46);
            if (packageEndIndex == -1) {
                return "";
            } else {
                String packageName = className.substring(0, packageEndIndex);
                return packageName.replace('.', '/');
            }
        }
    }

    /**
     * 上下文加载器
     *
     * @return 类加载器
     */
    public static ClassLoader getDefaultClassLoader() {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        } catch (Throwable var3) {
            ;
        }
        if (cl == null) {
            cl = ClassTools.class.getClassLoader();
            if (cl == null) {
                try {
                    cl = ClassLoader.getSystemClassLoader();
                } catch (Throwable var2) {
                    ;
                }
            }
        }
        return cl;
    }

    /**
     * 获取指定包名下的所有类
     *
     * @param classLoader 类型加载器
     * @param packageName 扫描包
     * @return Set
     */
    public static Set<Class> getClassesByPackageName(ClassLoader classLoader, String packageName, boolean isInitialized, boolean recursively, boolean isPrintCLass) throws IOException {
        Set<Class> classes = new HashSet<>();
        try {
            Enumeration<URL> urls = classLoader.getResources(packageName.replace(".", "/"));
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                if (url != null) {
                    String protocol = url.getProtocol();
                    if ("file".equals(protocol)) {
                        String packagePath = url.getPath().replaceAll(" ", "");
                        getClassesInPackageUsingFileProtocol(classes, classLoader, packagePath, packageName, isInitialized, recursively, isPrintCLass);
                    } else if ("jar".equals(protocol)) {
                        getClassesInPackageUsingJarProtocol(classes, classLoader, url, packageName, isInitialized, recursively, isPrintCLass);
                    } else {
                        log.info(String.format("protocol[%s] not supported!", protocol));
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return classes;
    }

    private static void getClassesInPackageUsingJarProtocol(Set<Class> classes, ClassLoader classLoader, URL url, String packageName, boolean isInitialized, boolean recursively, boolean isPrintCLass) throws IOException {
        String packagePath = packageName.replace(".", "/");
        if (isPrintCLass) {
            log.info("---------getClassesInPackageUsingJarProtocol----------");
        }
        JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
        if (jarURLConnection != null) {
            JarFile jarFile = jarURLConnection.getJarFile();
            Enumeration<JarEntry> jarEntries = jarFile.entries();
            while (jarEntries.hasMoreElements()) {
                JarEntry jarEntry = jarEntries.nextElement();
                String jarEntryName = jarEntry.getName();
                if (jarEntryName.startsWith(packagePath) && jarEntryName.endsWith(".class")) {
                    if (!recursively && jarEntryName.substring(packagePath.length() + 1).contains("/")) {
                        continue;
                    }
                    if (isPrintCLass) {
                        log.info(jarEntryName);
                    }
                    String className = jarEntryName.substring(0, jarEntryName.lastIndexOf(".")).replaceAll("/", ".");
                    classes.add(loadClass(className, isInitialized, classLoader));
                }
            }
        }
        if (isPrintCLass) {
            log.info("---------getClassesInPackageUsingJarProtocol----------");
        }
    }


    private static void getClassesInPackageUsingFileProtocol(Set<Class> classes, ClassLoader classLoader, String packagePath, String packageName, boolean isInitialized, boolean recursively, boolean isPrintCLass) {
        final File[] files = new File(packagePath).listFiles(
                file ->
                        (file.isFile() && file.getName().endsWith(".class") || file.isDirectory()));
        for (File file : files) {
            String fileName = file.getName();
            if (file.isFile()) {
                String className = fileName.substring(0, fileName.lastIndexOf("."));
                if (!StringTools.isEmpty(packageName)) {
                    className = packageName + "." + className;
                }
                classes.add(loadClass(className, isInitialized, classLoader));
            } else if (recursively) {
                String subPackagePath = fileName;
                if (!StringTools.isEmpty(subPackagePath)) {
                    subPackagePath = packagePath + "/" + subPackagePath;
                }
                String subPackageName = fileName;
                if (!StringTools.isEmpty(packageName)) {
                    subPackageName = packageName + "." + subPackageName;
                }
                getClassesInPackageUsingFileProtocol(classes, classLoader, subPackagePath, subPackageName, isInitialized, recursively, isPrintCLass);
            }
        }
    }

    /**
     * @param className     完整类路径
     * @param isInitialized 是否初始化 第2个boolean参数表示类是否需要初始化，  Class.forName(className)默认是需要初始化。
     *                      一旦初始化，就会触发目标对象的 static块代码执行，static参数也也会被再次初始化
     *                      <p>
     *                      。
     * @param classLoader   类加载器
     * @return
     */
    public static Class<?> loadClass(String className, Boolean isInitialized, ClassLoader classLoader) {
        Class<?> clazz;
        try {
            clazz = Class.forName(className, isInitialized, classLoader);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return clazz;
    }


    /**
     * 判读是否存在
     *
     * @param className 类方法
     * @return boolean
     */
    public static boolean isPresent(String className) {
        return isPresent(className, null);
    }

    /**
     * 判断当前是否存在该类
     *
     * @param className   类全路径
     * @param classLoader 类加载器
     * @return boolean
     */
    public static boolean isPresent(String className, ClassLoader classLoader) {
        Class<?> aClass = null;
        try {
            if (classLoader == null) {
                classLoader = getDefaultClassLoader();
            }
            aClass = Class.forName(className, false, classLoader);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return aClass != null ? true : false;
    }

    /**
     * 判断是否是抽象类
     *
     * @param cls 类型
     * @return boolean
     */
    public static boolean isAbstract(Class cls) {
        return Modifier.isAbstract(cls.getModifiers());
    }

    /**
     * 判断是否是抽象类
     *
     * @param cls 类型
     * @return boolean
     */
    public static boolean isInterface(Class cls) {
        return Modifier.isInterface(cls.getModifiers());
    }


}
