package org.medusa.barrage.client;

import org.medusa.barrage.common.Message;
import org.medusa.barrage.common.UserPool;
import org.medusa.barrage.proxy.MessageProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

/**
 * Created by 一线生 on 2015/11/22.
 * 说明：
 */

@ServerEndpoint("/init")
public class BarrageClient {

    private static Logger LOG = LoggerFactory.getLogger(BarrageClient.class);

    @OnOpen
    public void onOpen(Session session) {
        //加入用户池
        UserPool.add(session);
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        LOG.info("user [" + session.getId() + "] Received: " + message);
        //给所有用户发送消息
        MessageProxy.getInstance().getProxy(Message.class).send(message, session);
    }

    @OnError
    public void onError(Throwable throwable) {
        LOG.error(throwable.getMessage());
    }

    @OnClose
    public void onClose(Session session) {
        //移除用户池
        UserPool.remove(session.getId());

    }
}
