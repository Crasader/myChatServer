package com.tao.logic.core.handler;

import com.google.protobuf.Message;
import com.tao.logic.core.global.HandlerManager;
import com.tao.protobuf.analysis.ParseMap;
import com.tao.protobuf.message.internal.Internal;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by tao on 2017/7/6.
 */

/**
 * LogicServer的handler.
 * 负责处理发来LogicServer的消息.
 */
public final class LogicServerHandler extends SimpleChannelInboundHandler<Message> {

    private static final Logger logger = LoggerFactory.getLogger(LogicServerHandler.class);

    private static ChannelHandlerContext gateConnCtx;   //gate连接的handler
    private static ChannelHandlerContext authConnCtx;   //auth连接的handler




    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {

        Internal.GTransfer gt = (Internal.GTransfer) msg;
        //获得ptoNum
        int ptoNum = gt.getPtoNum();
        //获得消息对象
        Message message = ParseMap.getMessage(ptoNum, gt.getMsg().toByteArray());

        //判断消息类型
        MsgHandler msgHandler = null;
        if(message instanceof Internal.Greet) {
            //如果是Greet消息
            msgHandler = HandlerManager.getMsgHandler(ptoNum, gt.getUserId(), gt.getNetId(),
                    message, ctx);
        } else {
            //如果是其他消息(聊天消息)
            msgHandler = HandlerManager.getMsgHandler(ptoNum, gt.getUserId(), gt.getNetId(),
                    message, getGateConnCtx());
        }




    }



    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }



    public static ChannelHandlerContext getGateConnCtx() {
        return gateConnCtx;
    }

    public static void setGateConnCtx(ChannelHandlerContext gateConnCtx) {
        LogicServerHandler.gateConnCtx = gateConnCtx;
    }

    public static ChannelHandlerContext getAuthConnCtx() {
        return authConnCtx;
    }

    public static void setAuthConnCtx(ChannelHandlerContext authConnCtx) {
        LogicServerHandler.authConnCtx = authConnCtx;
    }
}








