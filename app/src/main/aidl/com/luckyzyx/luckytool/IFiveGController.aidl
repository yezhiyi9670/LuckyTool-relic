package com.luckyzyx.luckytool;

interface IFiveGController {
    boolean checkCompatibility(int subId);
    boolean getFiveGStatus(int subId);
    void setFiveGStatus(int subId,boolean enabled);
}