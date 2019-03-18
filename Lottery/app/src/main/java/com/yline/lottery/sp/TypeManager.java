package com.yline.lottery.sp;

import java.util.ArrayList;
import java.util.List;

/**
 * Type管理类
 *
 * @author yline 2019/3/18 -- 21:31
 * @version 1.0.0
 */
public class TypeManager {
    /**
     * 获取彩票名称
     *
     * @param lottoId 彩票ID
     * @return 彩票类型，lottoId if not exist
     */
    public static String getTypeNameByLottoId(String lottoId) {
        for (TypeEnum typeEnum : TypeEnum.values()) {
            if (typeEnum.getId().equalsIgnoreCase(lottoId)) {
                return typeEnum.getName();
            }
        }
        return lottoId;
    }

    /**
     * 获取彩票名称
     *
     * @param lottoId 彩票ID
     * @return 彩票类型，DLT if not exist
     */
    public static String getTypeFileNameByLottoId(String lottoId) {
        String fileName = TypeEnum.DLT.getId();
        for (TypeEnum typeEnum : TypeEnum.values()) {
            if (typeEnum.getId().equalsIgnoreCase(lottoId)) {
                fileName = typeEnum.getId();
                break;
            }
        }
        return "file:///android_asset/rule/" + fileName + ".html";
    }

    /**
     * 返回，彩票Id，列表
     *
     * @return id 队列
     */
    public static List<String> getLottoIdList() {
        List<String> idList = new ArrayList<>();
        for (TypeEnum typeEnum : TypeEnum.values()) {
            idList.add(typeEnum.getId());
        }
        return idList;
    }

    /**
     * 返回，彩票长度
     *
     * @return 长度
     */
    public static int getLottoTypeCount() {
        return TypeEnum.values().length;
    }
}
