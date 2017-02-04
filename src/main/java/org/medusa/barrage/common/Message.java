package org.medusa.barrage.common;

import org.medusa.barrage.entity.Text;
import org.medusa.barrage.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.Session;
import java.io.IOException;
import java.util.Set;

/**
 * Created by 一线生 on 2015/11/22.
 * 说明：
 */
public class Message {

    private static Logger LOG = LoggerFactory.getLogger(Message.class);

    /**
     * 给除自己外的所有用户发送消息
     * @param message
     * @param filterSession
     */
    public void send(String message, Session filterSession) {
        //从昵称池拿出用户昵称
        Text text = JsonUtil.getBean(message, Text.class);
        text.setUserName(NickPool.get(filterSession.getId()));

        //判断是否是需要执行的脚本，是则return，由代理处理脚本
        if(CMD.isCMD(text.getMessage())) {
            return;
        }

        //给所有用户发送消息
        Set<String> keys = UserPool.getUserPool().keySet();
        for(String key : keys) {
            Session session = (Session) UserPool.get(key);
            //屏蔽状态关闭的用户
            if(!session.isOpen()) {
                UserPool.remove(session.getId());
                continue;
            }
            //排除自己
            if(session.equals(filterSession)) {
                continue;
            }
            try {
                //发送消息
                session.getBasicRemote().sendText(JsonUtil.getString(text));
            } catch (IOException e) {
                LOG.error("给用户 [" + session.getId() + "] 发送消息失败", e);
            }
        }
    }
}
