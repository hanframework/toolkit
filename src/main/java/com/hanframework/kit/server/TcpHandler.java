package com.hanframework.kit.server;

import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

/**
 * @author liuxin
 * 2020-07-03 00:07
 */
public class TcpHandler implements Runnable {

    private final Selector selector;

    private final ServerSocketChannel ssc;

    public TcpHandler(Selector selector, ServerSocketChannel ssc) {
        this.selector = selector;
        this.ssc = ssc;
    }

    @Override
    public void run() {
        try {

        } catch (Exception e) {

        }
    }
}
