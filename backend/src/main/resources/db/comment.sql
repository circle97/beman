-- 评论表
CREATE TABLE IF NOT EXISTS `comment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `post_id` bigint(20) NOT NULL COMMENT '帖子ID',
  `content` text NOT NULL COMMENT '评论内容',
  `author_id` bigint(20) NOT NULL COMMENT '作者ID',
  `author_name` varchar(100) NOT NULL COMMENT '作者名称',
  `author_avatar` varchar(500) DEFAULT NULL COMMENT '作者头像',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父评论ID（用于回复功能）',
  `reply_user_id` bigint(20) DEFAULT NULL COMMENT '回复用户ID',
  `reply_user_name` varchar(100) DEFAULT NULL COMMENT '回复用户名称',
  `like_count` int(11) DEFAULT 0 COMMENT '点赞数',
  `is_anonymous` tinyint(1) DEFAULT 0 COMMENT '是否匿名',
  `status` tinyint(4) DEFAULT 0 COMMENT '状态：0-正常，1-已删除，2-已隐藏',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) DEFAULT 0 COMMENT '逻辑删除标识',
  PRIMARY KEY (`id`),
  KEY `idx_post_id` (`post_id`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_author_id` (`author_id`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_status` (`status`),
  KEY `idx_deleted` (`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评论表';

-- 评论点赞记录表
CREATE TABLE IF NOT EXISTS `comment_like` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `comment_id` bigint(20) NOT NULL COMMENT '评论ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `deleted` tinyint(1) DEFAULT 0 COMMENT '逻辑删除标识',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_comment_user` (`comment_id`, `user_id`),
  KEY `idx_comment_id` (`comment_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_deleted` (`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评论点赞记录表';

-- 插入示例数据
INSERT INTO `comment` (`post_id`, `content`, `author_id`, `author_name`, `author_avatar`, `like_count`, `is_anonymous`, `status`) VALUES
(1, '这是一个很好的分享，感谢楼主的经验分享！', 1, '用户001', 'https://example.com/avatar1.jpg', 5, 0, 0),
(1, '我也遇到过类似的情况，楼主的建议很有帮助', 2, '用户002', 'https://example.com/avatar2.jpg', 3, 0, 0),
(1, '学习了，谢谢分享', 3, '用户003', 'https://example.com/avatar3.jpg', 1, 0, 0);

-- 插入示例回复评论
INSERT INTO `comment` (`post_id`, `content`, `author_id`, `author_name`, `author_avatar`, `parent_id`, `reply_user_id`, `reply_user_name`, `like_count`, `is_anonymous`, `status`) VALUES
(1, '谢谢支持！', 1, '用户001', 'https://example.com/avatar1.jpg', 1, 2, '用户002', 2, 0, 0),
(1, '不客气，大家一起学习进步', 2, '用户002', 'https://example.com/avatar2.jpg', 1, 1, '用户001', 1, 0, 0);
