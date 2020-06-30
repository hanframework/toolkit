package com.hanframework.kit.system;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author liuxin
 * 2020-06-30 22:15
 */
public final class SystemInfo {
    public static void main(String[] args) {
        SystemInfo.print();
    }
    public static void print(){
        Map<String, String> map = new LinkedHashMap<>();
        map.put("Java 运行时环境版本", System.getProperty("java.version"));
        map.put("Java 运行时环境供应商", System.getProperty("java.vendor"));
        map.put("Java 供应商的 URL", System.getProperty("java.vendor.url"));
        map.put("Java 安装目录", System.getProperty("java.home"));
        map.put("Java 虚拟机规范版本", System.getProperty("java.vm.specification.version"));
        map.put("Java 虚拟机规范供应商", System.getProperty("java.vm.specification.vendor"));
        map.put("Java 虚拟机规范名称", System.getProperty("java.vm.specification.name"));
        map.put("Java 虚拟机实现版本", System.getProperty("java.vm.version"));
        map.put("Java 虚拟机实现供应商", System.getProperty("java.vm.vendor"));
        map.put("Java 虚拟机实现名称", System.getProperty("java.vm.name"));
        map.put("Java 运行时环境规范版本", System.getProperty("java.specification.version"));
        map.put("Java 运行时环境规范供应商", System.getProperty("java.specification.vendor"));
        map.put("Java 运行时环境规范名称", System.getProperty("java.specification.name"));
        map.put("Java 类格式版本号", System.getProperty("java.class.version"));
        map.put("Java 类路径", System.getProperty("java.class.path"));
        map.put("加载库时搜索的路径列表", System.getProperty("java.library.path"));
        map.put("默认的临时文件路径", System.getProperty("java.io.tmpdir"));
        map.put("要使用的 JIT 编译器的名称", System.getProperty("java.compiler"));
        map.put("一个或多个扩展目录的路径", System.getProperty("java.ext.dirs"));
        map.put("操作系统的名称", System.getProperty("os.name"));
        map.put("操作系统的架构", System.getProperty("os.arch"));
        map.put("操作系统的版本", System.getProperty("os.version"));
        map.put("文件分隔符（在 UNIX 系统中是“/”）", System.getProperty("file.separator"));
        map.put("路径分隔符（在 UNIX 系统中是“:”）", System.getProperty("path.separator"));
        map.put("行分隔符（在 UNIX 系统中是“/n”）", System.getProperty("line.separator"));
        map.put("用户的账户名称", System.getProperty("user.name"));
        map.put("用户的主目录", System.getProperty("user.home"));
        map.put("用户的当前工作目录", System.getProperty("user.dir"));
        for (Map.Entry<String, String> en :map.entrySet()) {
            System.out.println(en.getKey() + "\t" + en.getValue());
        }
    }
}
