package com.chinaso.toutiao.util;

import com.chinaso.toutiao.app.Constants;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class SignCodeUtil {
    /**
     * MD5加密前的code
     * map直接用TreeMap创建
     * map的value值是key+value
     *
     * @param map
     * @return
     */
    public static String getAsceCode(Map<String, String> map) {
        Set<String> keySet = map.keySet();
        Iterator<String> iter = keySet.iterator();
        StringBuilder buffer = new StringBuilder();
        while (iter.hasNext()) {
            String key = iter.next();
            buffer.append(map.get(key));
        }
        DebugUtil.e("TAG", buffer.toString());
        //// TODO: 2016/12/21 秘钥未确定
        return Constants.SECRET_KEY + buffer.toString();
    }

}
