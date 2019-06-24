package com.yline.xposed.utils;

/**
 * 版本，帮助类
 *
 * @author yline 2019/6/24 -- 14:43
 */
public class VersionUtil {
    public static String getDatabaseClassName(String versionNum) {
        if (compare(versionNum, "6.5.8") >= 0) {
            return "com.tencent.wcdb.database.SQLiteDatabase";
        } else {
            return "com.tencent.mmdb.database.SQLiteDatabase";
        }
    }

    public static boolean redPacketHasTimingIdentifier(String versionNum) {
        return compare(versionNum, "6.5.4") >= 0;
    }

    public static String redPacketReceiveUIClassName(String versionNum) {
        if (compare(versionNum, "7.0.0") >= 0) {
            return "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyNotHookReceiveUI";
        } else if (compare(versionNum, "6.5.23") > 0) {
            return "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyReceiveUI";
        } else if (compare(versionNum, "6.5.6") >= 0) {
            return "com.tencent.mm.plugin.luckymoney.ui.En_fba4b94f";
        } else {
            return "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyReceiveUI";
        }
    }

    public static String redPacketChatroomInfoUIClassName(String versionNum) {
        if (compare(versionNum, "7.0.0") >= 0) {
            return "com.tencent.mm.chatroom.ui.ChatroomInfoUI";
        } else {
            return "com.tencent.mm.plugin.chatroom.ui.ChatroomInfoUI";
        }
    }

    /**
     * 规则，以点，分开；从头往下比
     *
     * @param versionA 版本号
     * @param versionB 版本号
     * @return 大于0；相等；小于0
     */
    public static int compare(String versionA, String versionB) {
        String[] arrayA = versionA.split("\\.");
        String[] arrayB = versionB.split("\\.");

        int aIndex = 0, bIndex = 0;
        while (aIndex < arrayA.length && bIndex < arrayB.length) {
            try {
                int aValue = Integer.parseInt(arrayA[aIndex]);
                int bValue = Integer.parseInt(arrayB[bIndex]);

                // 逐个对比
                if (aValue == bValue) {
                    aIndex++;
                    bIndex++;
                } else {
                    return aValue - bValue;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                return 0;
            }
        }
        return arrayA.length - arrayB.length;
    }
}
