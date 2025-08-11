package com.beman.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 发帖DTO
 */
@Data
public class PostCreateDTO {

    /**
     * 标题
     */
    @NotBlank(message = "标题不能为空")
    @Size(max = 200, message = "标题长度不能超过200字符")
    private String title;

    /**
     * 内容
     */
    @NotBlank(message = "内容不能为空")
    @Size(max = 5000, message = "内容长度不能超过5000字符")
    private String content;

    /**
     * 标签列表
     */
    private List<String> tags;

    /**
     * 是否匿名发布
     */
    private Boolean isAnonymous;

    /**
     * 内容类型：1-文字，2-语音
     */
    private Integer contentType = 1;

    /**
     * 语音文件URL（当contentType为2时使用）
     */
    private String voiceUrl;
}
