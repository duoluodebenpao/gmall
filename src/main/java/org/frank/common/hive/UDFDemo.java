package org.frank.common.hive;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hive.ql.exec.UDF;

public class UDFDemo extends UDF {


    /**
     * @param line           被解析的字符串
     * @param jsonkeysString 要提取的字段
     * @return
     */
    public String evaluate(String line, String jsonkeysString) {

        // 1.切割jsonkeys
        String[] jsonkeys = jsonkeysString.split(",");

        // 2.处理line
        String[] logContents = line.split("\\|");

        // 3.合法性校验
        if (logContents.length != 2 || StringUtils.isBlank(logContents[1])) {
            return "";
        }

        // 4. 准备容器
        StringBuilder sb = new StringBuilder();
        String splitWord = "*";

        try {
            JSONObject jsonObject = JSON.parseObject(logContents[1]);
            if (jsonObject == null) {
                return "";
            } else {
                // 获取cm里面的对象
                JSONObject base = jsonObject.getJSONObject("cm");
                if (base == null) {
                    return "";
                } else {
                    // 循环遍历取值
                    for (int i = 0; i < jsonkeys.length; i++) {
                        String filedName = jsonkeys[i].trim();

                        if (base.containsKey(filedName)) {
                            sb.append(base.getString(filedName)).append(splitWord);
                        } else {
                            sb.append("").append(splitWord);
                        }
                    }
                }
            }
            sb.append(jsonObject.getString("et")).append(splitWord);
            sb.append(logContents[0]).append(splitWord);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    public static void main(String[] args){
        String line = "1541217850324|{\"cm\":{\"mid\":\"my-mid\",\"uid\":\"my-uid\",\"app\":\"58tongcheng\"},\"et\":\"this's et\"}";
        String jsonkeysStr = "mid,uid,app,test";

        String result = new UDFDemo().evaluate(line, jsonkeysStr);
        System.out.println(result);
    }
}
