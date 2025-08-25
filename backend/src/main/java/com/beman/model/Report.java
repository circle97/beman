package com.beman.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 举报实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("report")
public class Report {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 举报者ID
     */
    @TableField("reporter_id")
    private Long reporterId;

    /**
     * 举报者名称
     */
    @TableField("reporter_name")
    private String reporterName;

    /**
     * 被举报内容类型：1-帖子，2-评论，3-用户，4-其他
     */
    @TableField("content_type")
    private Integer contentType;

    /**
     * 被举报内容ID
     */
    @TableField("content_id")
    private Long contentId;

    /**
     * 举报原因类型：1-垃圾信息，2-不当内容，3-恶意行为，4-版权问题，5-其他
     */
    @TableField("reason_type")
    private Integer reasonType;

    /**
     * 举报详细描述
     */
    @TableField("description")
    private String description;

    /**
     * 举报证据（图片URL等）
     */
    @TableField("evidence")
    private String evidence;

    /**
     * 举报状态：0-待处理，1-处理中，2-已处理，3-已驳回，4-已关闭
     */
    @TableField("status")
    private Integer status;

    /**
     * 处理结果
     */
    @TableField("result")
    private String result;

    /**
     * 处理人ID
     */
    @TableField("handler_id")
    private Long handlerId;

    /**
     * 处理人名称
     */
    @TableField("handler_name")
    private String handlerName;

    /**
     * 处理时间
     */
    @TableField("handle_time")
    private LocalDateTime handleTime;

    /**
     * 优先级：1-低，2-中，3-高，4-紧急
     */
    @TableField("priority")
    private Integer priority;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 逻辑删除标识
     */
    @TableLogic
    @TableField("deleted")
    private Integer deleted;
}
