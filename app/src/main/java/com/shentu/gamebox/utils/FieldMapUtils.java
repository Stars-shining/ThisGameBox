package com.shentu.gamebox.utils;

import com.alibaba.fastjson.JSON;
import com.shentu.gamebox.bean.GameInfoBean;
import com.shentu.gamebox.bean.ParamBean;
import com.shentu.gamebox.bean.ServiceBean;

import java.util.HashMap;

public class FieldMapUtils {

    /*params   type 游戏类型
     * gameId 游戏id
     * agent_code 代理标识
     * action 请求action 例如：game_box/api/get_games
     * name userName
     * version 子包版本
     * parent_version 母包版本
     *
     * MD5 加密 ParamsBean 字段根据FastJson 进行对象->Json  根据注解顺序进行转换 否则加密MD5与后台不匹配。*/

    public static HashMap<String, Object> getRequestBody(String type, String gameId, String agent_code, String action, String name, String version) {

        ServiceBean serviceBean = new ServiceBean();
        ParamBean paramBean = new ParamBean();
        String t = Constant.getCurrentTime();
//        String t = "2021-04-06 16:45";
        if (null != name && !name.isEmpty()) {
            paramBean.setUsername(name);
        }
        if (null != type && !type.isEmpty()) {
            paramBean.setType(type);
        }

        if (null != gameId && !gameId.isEmpty()) {
            paramBean.setGame_id(gameId);
        }
        if (!agent_code.isEmpty()) {
            paramBean.setAgent_code(agent_code);
        }
        if (!version.isEmpty()) {
            paramBean.setAgent_version(version);
        }

        LogUtils.e(paramBean.toString());
        String param = JSON.toJSONString(paramBean);
        String md5 = MegDigest.Md5(param);
        String slat = action + md5 + t;
        String sign = MegDigest.Md5(slat);
        LogUtils.e(action + param);
        LogUtils.e("t" + t);

        LogUtils.e(sign);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("action", action);
        hashMap.put("param", param);
        hashMap.put("sign", sign);
        hashMap.put("t", t);
        if (action .equals(Constant.DOWN_LOAD)){
            hashMap.put("game_id",gameId);
            hashMap.put("type",type);
            hashMap.put("agent_code",agent_code);
        }

        return hashMap;

    }

    public static HashMap<String, Object> getRequestBody(String type, String gameId, String agent_code, String action, String name) {
        return getRequestBody(type, gameId, agent_code, action, "", "");
    }


    public static HashMap<String, Object> getBoxGameInfoBody( String gameId, String agent_code, String action, String game_action, String uuid) {

        GameInfoBean gameInfoBean = new GameInfoBean();
        String t = Constant.getCurrentTime();

        if (null != gameId && !gameId.isEmpty()) {
            gameInfoBean.setGame_id(gameId);
        }
        if (null != agent_code && !agent_code.isEmpty()) {
            gameInfoBean.setAgent_code(agent_code);
        }
        if (null != game_action && !game_action.isEmpty()) {
            gameInfoBean.setGame_action(game_action);
        }
        if (null != uuid && !uuid.isEmpty()) {
            gameInfoBean.setImei(uuid);
        }

        String param = JSON.toJSONString(gameInfoBean);
        String md5 = MegDigest.Md5(param);
        String slat = action + md5 + t;
        String sign = MegDigest.Md5(slat);
        LogUtils.e(action + param);
        LogUtils.e("t" + t);

        LogUtils.e(sign);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("action", action);
        hashMap.put("param", param);
        hashMap.put("sign", sign);
        hashMap.put("t", t);

        return hashMap;
    }

}
