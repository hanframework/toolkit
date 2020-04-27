package com.hanframework.kit.futrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;


/**
 * @author liuxin
 * 2020-04-20 21:54
 */
public final class ThrowableUtil {
    private ThrowableUtil() {
    }

    public static <T extends Throwable> T unknownStackTrace(T cause, Class<?> clazz, String method) {
        cause.setStackTrace(new StackTraceElement[]{new StackTraceElement(clazz.getName(), method, (String)null, -1)});
        return cause;
    }

    public static String stackTraceToString(Throwable cause) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream pout = new PrintStream(out);
        cause.printStackTrace(pout);
        pout.flush();

        String var3;
        try {
            var3 = new String(out.toByteArray());
        } finally {
            try {
                out.close();
            } catch (IOException var10) {
            }

        }

        return var3;
    }
}
