package com.beman.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 举报VO
 */
@Data
public class ReportVO {

    /**
     * 举报ID
     */
    private Long id;

    /**
     * 举报者ID
     */
    private Long reporterId;

    /**
     * 举报者名称
     */
    private String reporterName;

    /**
     * 被举报内容类型：1-帖子，2-评论，3-用户，4-其他
     */
    private Integer contentType;

    /**
     * 被举报内容ID
     */
    private Long contentId;

    /**
     * 被举报内容摘要
     */
    private String contentSummary;

    /**
     * 举报原因类型：1-垃圾信息，2-不当内容，3-恶意行为，4-版权问题，5-其他
     */
    private Integer reasonType;

    /**
     * 举报原因描述
     */
    private String reasonDescription;

    /**
     * 举报详细描述
     */
    private String description;

    /**
     * 举报证据（图片URL等）
     */
    private String evidence;

    /**
     * 举报状态：0-待处理，1-处理中，2-已处理，3-已驳回，4-已关闭
     */
    private Integer status;

    /**
     * 状态描述
     */
    private String statusDescription;

    /**
     * 处理结果
     */
    private String result;

    /**
     * 处理人ID
     */
    private Long handlerId;

    /**
     * 处理人名称
     */
    private String handlerName;

    /**
     * 处理时间
     */
    private LocalDateTime handleTime;

    /**
     * 优先级：1-低，2-中，3-高，4-紧急
     */
    private Integer priority;

    /**
     * 优先级描述
     */
    private String priorityDescription;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
