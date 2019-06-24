package com.yline.xposed.module.wechat;

import android.content.ContentValues;

import com.yline.utils.LogUtil;
import com.yline.xposed.IPlugin;
import com.yline.xposed.config.SPManager;
import com.yline.xposed.utils.VersionUtil;

import java.util.Arrays;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static java.util.Arrays.copyOfRange;

/**
 * 朋友圈，防删除
 *
 * @author yline 2019/6/24 -- 11:18
 */
public class CircleWithDrawPlugin implements IPlugin {

    @Override
    public void hook(XC_LoadPackage.LoadPackageParam lpparam, String versionName) throws Throwable {
        final String dbClassName = VersionUtil.getDatabaseClassName(versionName);

        XposedHelpers.findAndHookMethod(dbClassName, lpparam.classLoader, "updateWithOnConflict",
                String.class, ContentValues.class, String.class, String[].class, int.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);

                        boolean enable = SPManager.getInstance().isWechatCircleWithdraw(true);
                        LogUtil.v("enable = " + enable + ", param = " + Arrays.toString(param.args));

                        String sns = (String) param.args[0];
                        if ("SnsInfo".equalsIgnoreCase(sns)) {
                            ContentValues contentValues = ((ContentValues) param.args[1]);
                            int type = contentValues.getAsInteger("type");
                            int sourceType = contentValues.getAsInteger("sourceType");
                            if ((type == 1 || type == 2 || type == 3 || type == 15) && sourceType == 0) {
                                String label = "[已删除]";
                                contentValues.remove("sourceType");
                                contentValues.put("content", notifyInfoDelete(label, contentValues.getAsByteArray("content")));
                            }
                        }

                        if ("SnsComent".equalsIgnoreCase(sns)) {
                            if (param.args[0].equals("SnsComment")) {
                                ContentValues contentValues = ((ContentValues) param.args[1]);
                                int type = contentValues.getAsInteger("type");
                                int commentflag = contentValues.getAsInteger("commentflag");
                                if (type != 1 && commentflag == 1) {
                                    String label = "[已删除]";
                                    contentValues.remove("commentflag");
                                    contentValues.put("curActionBuf", notifyCommentDelete(label, contentValues.getAsByteArray("curActionBuf")));
                                }
                            }
                        }
                    }
                });
    }


    private byte[] notifyInfoDelete(String head, byte[] msg) {
        if (head == null || msg == null) {
            return null;
        }

        int start = -1;
        for (int i = 0; i < msg.length; i++) {
            if (msg[i] == 0x2A) {
                start = i + 1;
                break;
            }
        }

        int[] res = decodeMsgSize(start, msg);
        int lenSize = res[0];
        int msgSize = res[1];

        byte[] content = copyOfRange(msg, start + lenSize, msg.length);

        if (new String(content).startsWith(head)) {
            return msg;
        }

        byte[] len = encodeMsgSize((head + " ").getBytes().length + msgSize);
        return concatAll(copyOfRange(msg, 0, start), len, (head + " ").getBytes(), content);
    }

    private byte[] notifyCommentDelete(String head, byte[] msg) {
        if (head == null || msg == null) {
            return null;
        }

        int nameStart = -1;
        for (int i = 0; i < msg.length; i++) {
            if (msg[i] == 0x22) {
                nameStart = i;
                break;
            }
        }
        int start = nameStart + msg[nameStart + 1] + 13;

        int[] res = decodeMsgSize(start, msg);
        int lenSize = res[0];
        int msgSize = res[1];

        byte[] content = copyOfRange(msg, start + lenSize, msg.length);

        if (new String(content).startsWith(head)) {
            return msg;
        }

        byte[] len = encodeMsgSize((head + " ").getBytes().length + msgSize);
        return concatAll(copyOfRange(msg, 0, start), len, (head + " ").getBytes(), content);
    }

    private int[] decodeMsgSize(int start, byte[] msg) {
        int lenSize = 1;
        int msgSize = msg[start];
        if (msgSize < 0) {
            lenSize = 2;
            int tmp = msg[start + 1];
            msgSize = (msgSize & 0x7F) + (tmp << 7);
        }
        int[] res = {lenSize, msgSize};
        return res;
    }

    private byte[] encodeMsgSize(int msgSize) {
        if ((msgSize >> 7) > 0) {
            byte[] res = {(byte) (msgSize & 0x7F | 0x80), (byte) (msgSize >> 7)};
            return res;
        } else {
            byte[] res = {(byte) msgSize};
            return res;
        }
    }

    private byte[] concatAll(byte[] first, byte[]... rest) {
        int totalLength = first.length;
        for (byte[] array : rest) {
            totalLength += array.length;
        }
        byte[] result = Arrays.copyOf(first, totalLength);
        int offset = first.length;
        for (byte[] array : rest) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }
        return result;
    }
}
