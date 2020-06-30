package com.hanframework.kit.platform;


import org.junit.Test;

/**
 * @author liuxin
 * 2020-06-30 22:34
 */
public class OSinfoTest {

    @Test
    public void test(){
        System.out.println(OSinfo.getOSname().getSystemName());
        System.out.println(OSinfo.isLinux());
        System.out.println(OSinfo.isWindows());
        System.out.println(OSinfo.isAix());
        System.out.println(OSinfo.isMacOS());
        System.out.println(OSinfo.isMacOSX());
    }
}