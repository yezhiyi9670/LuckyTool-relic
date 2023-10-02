package com.luckyzyx.luckytool;

interface IAdbDebugController {
    int getAdbPort();
    void setAdbPort(int port);
    String getWifiIP();
    void restartAdb();
}