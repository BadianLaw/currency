package com.practice.util;

import io.muserver.MuServer;

public class MuServerUtil {
    private static boolean stop = false;
    private static MuServer muServer;

    public static boolean isStop() {
        return stop;
    }

    public static MuServer getMuServer() {
        return muServer;
    }

    public static void setMuServer(MuServer muServer) {
        MuServerUtil.muServer = muServer;
    }
}
