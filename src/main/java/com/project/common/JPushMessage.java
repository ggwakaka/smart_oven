package com.project.common;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;
import com.project.response.ServerResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JPushMessage {

    @Value("${jiguang.master_secret}")
    private String masterSecret = "fd9785be7e05b9dc14569f63";

    @Value("${jiguang.app_key}")
    private String appKey = "3b1b3049c6ec58542a7f8a5b";

    public static void main(String[] args){
        new JPushMessage().jPushMessage("test","666666");
    }

    public ServerResponse jPushMessage(String message, String tagId){
        ClientConfig clientConfig = ClientConfig.getInstance();
        JPushClient jpushClient = new JPushClient(masterSecret, appKey, null, clientConfig);
        PushPayload payload = buildPushObjectAllAliasAlert(message,tagId);
        try {
            log.info(payload.toString());
            PushResult result = jpushClient.sendPush(payload);
            log.info("Got result - " + result);
            return ServerResponse.createBySuccessMessage("推送成功");
        } catch (APIConnectionException e) {
            log.error("Connection error. Should retry later. ", e);
            log.error("Sendno: " + payload.getSendno());
            return ServerResponse.createByErrorMessage("极光推送连接失败，请稍后重试");

        } catch (APIRequestException e) {
            log.error("Error response from JPush server. Should review and fix it. ", e);
            log.info("HTTP Status: " + e.getStatus());
            log.info("Error Code: " + e.getErrorCode());
            log.info("Error Message: " + e.getErrorMessage());
            log.info("Msg ID: " + e.getMsgId());
            log.error("Sendno: " + payload.getSendno());
            return ServerResponse.createByErrorMessage(e.getErrorMessage());
        }
    }

    public PushPayload buildPushObjectAllAliasAlert(String alert, String tagId) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.tag(tagId))
                .setNotification(Notification.alert(alert))
                .setOptions(Options.newBuilder().setApnsProduction(false).setTimeToLive(86000).build())
                .build();
    }
}
