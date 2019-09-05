package com.project.controller;

import com.project.common.AuthToken;
import com.project.request.TransformRequest;
import com.project.response.ServerResponse;
import com.project.service.OvenMobileRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@EnableScheduling
@EnableAsync
@RestController
public class OvenController {

    private static final String MODULE_NAME = "oven";

    private static final String CONTROLLER_NAME = "process";

    @Autowired
    private OvenMobileRelationService ovenMobileRelationService;

    @PostMapping("/oven/process/upload_picture.do")
    public ServerResponse collectPicture(@RequestHeader("oven_id") String ovenId,
                                         @RequestHeader("task_id") String taskId,
                                         @RequestHeader("is_need_send")int isNeedSend,
                                         @RequestHeader("token") String token,
                                         @RequestBody MultipartFile file){
        String sourceToken = AuthToken.getAuthToken(MODULE_NAME,CONTROLLER_NAME,"upload_picture.do");
        if (!AuthToken.checkToken(sourceToken,token)){
            return ServerResponse.createByErrorMessage("鉴权失败");
        }
        return ovenMobileRelationService.collectPicture(ovenId,file,taskId, isNeedSend);
    }

    @PostMapping("/oven/process/transform_data.do")
    public ServerResponse transformData(@RequestHeader String ovenId,
                                        @RequestHeader("token") String token,
                                        @RequestBody TransformRequest transformRequest){
        String sourceToken = AuthToken.getAuthToken(MODULE_NAME,CONTROLLER_NAME,"transform_data.do");
        if (!AuthToken.checkToken(sourceToken,token)){
            return ServerResponse.createByErrorMessage("鉴权失败");
        }
        return ovenMobileRelationService.transformData(ovenId,transformRequest);
    }

    @PostMapping("/oven/process/upload_online.do")
    public ServerResponse checkOnline(@RequestHeader String ovenId,@RequestHeader("token") String token){
        String sourceToken = AuthToken.getAuthToken(MODULE_NAME,CONTROLLER_NAME,"upload_online.do");
        if (!AuthToken.checkToken(sourceToken,token)){
            return ServerResponse.createByErrorMessage("鉴权失败");
        }
        ovenMobileRelationService.checkOnline(ovenId);
        return ServerResponse.createBySuccess();
    }

    /**
     * 每分钟执行一次扫描
     */
    @Async
    @Scheduled(cron = "0 0/1 * * * ?")
    public void scanOvenIfHasOffLine() {
        ovenMobileRelationService.scanOvenIfHasOffline();
    }
}
