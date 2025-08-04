package com.beman.community.dto;

import lombok.Data;
import com.beman.community.entity.CommunityPost.PostType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class PostRequest {
    @NotBlank(message = "标题不能为空")
    private String title;

    @NotBlank(message = "内容不能为空")
    private String content;

    @NotNull(message = "帖子类型不能为空")
    private PostType postType;

    private List<String> tags;

    private boolean isAnonymous = true;
} 