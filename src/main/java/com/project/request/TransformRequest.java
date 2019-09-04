package com.project.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransformRequest {
    @JsonProperty(value = "ID")
    private String id;//烤箱ID，如果是烤箱想手机发送，可以不传

    @JsonProperty(value = "Mobile_ID")
    private String mobileId;// 手机id ，如果是手机向烤箱发送，可以不传

    @JsonProperty(value = "TO")
    private int to;//传输方向：0 烤箱app向服务器传送 1 手机app向服务器传送

    @JsonProperty(value = "Status")
    private int status;//烤箱状态 0 待机状态  1 加热状太

    @JsonProperty(value = "Model")
    private int model;// 加热模式 1 上烤 2 下烤 3 上下考 4旋转 5 上烤+旋转 6解冻 7 发酵

    @JsonProperty(value = "Uptempture")
    private int uptempture;// 上管温度

    @JsonProperty(value = "Downtempture")
    private int downtempture;//下管温度

    @JsonProperty(value = "Time")
    private int time;//加热时间

}
