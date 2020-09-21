package com.hanframework.kit.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author liuxin
 * 2020-07-02 23:40
 */
public class TCPReactor {

    private final Selector selector;

    private final ServerSocketChannel ssc;

    public TCPReactor(Integer port) throws IOException {
        selector = Selector.open();
        ssc = ServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(port);
        ssc.socket().bind(inetSocketAddress);
        ssc.configureBlocking(false);
        SelectionKey sk = ssc.register(selector, SelectionKey.OP_ACCEPT);
        sk.attach(new AcceptHandler(selector, ssc));
    }

    public void start() throws Exception {
        while (!Thread.interrupted()) {
            int localPort = ssc.socket().getLocalPort();
            if (selector.select() != 0) {
                System.out.println("等待事件发生在端口:" + localPort);
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    dispatch(iterator.next());
                    iterator.remove();
                }
            }
        }
    }

    private void dispatch(SelectionKey selectionKey) {
        Runnable attachment = (Runnable) selectionKey.attachment();
        if (attachment != null) {
            attachment.run();
        }
    }
}
