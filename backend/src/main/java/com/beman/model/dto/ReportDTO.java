package com.beman.model.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 举报创建DTO
 */
@Data
public class ReportDTO {

    /**
     * 被举报内容类型：1-帖子，2-评论，3-用户，4-其他
     */
    @NotNull(message = "内容类型不能为空")
    private Integer contentType;

    /**
     * 被举报内容ID
     */
    @NotNull(message = "内容ID不能为空")
    private Long contentId;

    /**
     * 举报原因类型：1-垃圾信息，2-不当内容，3-恶意行为，4-版权问题，5-其他
     */
    @NotNull(message = "举报原因不能为空")
    private Integer reasonType;

    /**
     * 举报详细描述
     */
    private String description;

    /**
     * 举报证据（图片URL等）
     */
    private String evidence;
}


