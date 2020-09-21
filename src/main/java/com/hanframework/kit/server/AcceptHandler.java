package com.hanframework.kit.server;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @author liuxin
 * 2020-07-03 00:04
 */
public class AcceptHandler implements Runnable {
    private final Selector selector;

    private final ServerSocketChannel ssc;

    public AcceptHandler(Selector selector, ServerSocketChannel ssc) {
        this.selector = selector;
        this.ssc = ssc;
    }

    @Override
    public void run() {
        try {
            SocketChannel sc = ssc.accept();
            sc.configureBlocking(false);
            SelectionKey sk = sc.register(selector, SelectionKey.OP_READ);
            sk.attach(new TcpHandler(selector, ssc));
        } catch (Exception e) {

        }
    }
}
