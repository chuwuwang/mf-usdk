package com.morefun.ysdk.sample.utils;

import android.os.Bundle;
import android.text.TextUtils;

import com.morefun.yapi.engine.DeviceInfoConstrants;
import com.morefun.ysdk.sample.device.DeviceHelper;

import java.util.ArrayList;

public class StringUitls {

    private String transformAmount(String amount) {
        try {
            long lAmount = Long.parseLong(amount);
            amount = String.valueOf(lAmount * 100);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return amount;
    }

    public static <T> ArrayList<T> createArrayList(T... elements) {
        ArrayList<T> list = new ArrayList<T>();
        for (T element : elements) {
            list.add(element);
        }
        return list;
    }

    public static String getServiceCodeFromTrack2(String track2) {
        String ServiceCode24 = "";
        if (TextUtils.isEmpty(track2)){
            return "";
        }
        for (int i = 0; i < track2.length(); i++) {
            if (track2.charAt(i) == '=' || track2.charAt(i) == 'd' || track2.charAt(i) == 'D') {
                ServiceCode24 = track2.substring(i + 5, i + 5 + 3);
                break;
            }
        }
        return ServiceCode24;
    }

    public static String getDeviceModel() {
        try {
            Bundle devInfo = DeviceHelper.getDeviceService().getDevInfo();
            return devInfo.getString(DeviceInfoConstrants.COMMOM_MODEL_EX);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
