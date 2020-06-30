package com.hanframework.kit.text;


import org.junit.Test;

/**
 * @author liuxin
 * 2020-06-30 22:31
 */
public class ColorTest {
    @Test
    public void test() {
        new UnixColor().blue("blue");
        new UnixColor().yellow("yellow");
        new UnixColor().green("green");
        new UnixColor().magenta("magenta");
        new UnixColor().cyan("cyan");
        new UnixColor().red("red");
    }

}