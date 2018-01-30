package com.cn.bent.sports.ibeacon;

import com.minew.beaconset.MinewBeacon;

import java.util.Comparator;

public class UserRssi implements Comparator<MinewBeacon> {

    @Override
    public int compare(MinewBeacon minewDevice, MinewBeacon t1) {
        float floatValue1 = minewDevice.getRssi();
        float floatValue2 = t1.getRssi();
        if (floatValue1 < floatValue2) {
            return 1;
        } else if (floatValue1 == floatValue2) {
            return 0;
        } else {
            return -1;
        }
    }
}
