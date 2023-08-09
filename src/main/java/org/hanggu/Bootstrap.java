package org.hanggu;

import org.hanggu.server.NettyServer;

/**
 * @author wuzhenhong
 */

public class Bootstrap {

    public static void main(String[] args) {
        NettyServer.start(8089, null);
    }
}