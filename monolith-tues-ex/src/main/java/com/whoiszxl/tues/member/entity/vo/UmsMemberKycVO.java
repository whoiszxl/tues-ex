package com.whoiszxl.tues.member.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.whoiszxl.tues.common.bean.AbstractObject;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户KYC认证表
 * </p>
 *
 * @author whoiszxl
 * @since 2021-03-17
 */
@Data
public class UmsMemberKycVO extends AbstractObject implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "用户ID")
    private Long memberId;

    @ApiModelProperty(value = "身份证ID")
    private String idCard;

    @ApiModelProperty(value = "身份证正面图")
    private String idcardFrontUrl;

    @ApiModelProperty(value = "身份证背面图")
    private String idcardReverseUrl;

    @ApiModelProperty(value = "手持身份证图")
    private String idcardHoldUrl;

    @ApiModelProperty(value = "视频地址")
    private String videoUrl;

    @ApiModelProperty(value = "真实姓名")
    private String realName;

    @ApiModelProperty(value = "审核拒绝理由")
    private String rejectReason;

    @ApiModelProperty(value = "认证类型")
    private Integer type;

    @ApiModelProperty(value = "审核状态: 0审核中 1审核成功 -1审核失败")
    private Integer auditStatus;

    @ApiModelProperty(value = "六位视频认证随机数")
    private String videoRandom;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updatedAt;


}
