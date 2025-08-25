-- 用户关注关系表
CREATE TABLE IF NOT EXISTS `user_follow` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `follower_id` bigint(20) NOT NULL COMMENT '关注者ID',
  `following_id` bigint(20) NOT NULL COMMENT '被关注者ID',
  `status` tinyint(4) DEFAULT 0 COMMENT '关注状态：0-已关注，1-已取消，2-已屏蔽',
  `follow_type` tinyint(4) DEFAULT 1 COMMENT '关注类型：1-普通关注，2-特别关注，3-好友关注',
  `remark_name` varchar(100) DEFAULT NULL COMMENT '备注名称',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '关注时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) DEFAULT 0 COMMENT '逻辑删除标识',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_follower_following` (`follower_id`, `following_id`),
  KEY `idx_follower_id` (`follower_id`),
  KEY `idx_following_id` (`following_id`),
  KEY `idx_status` (`status`),
  KEY `idx_follow_type` (`follow_type`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_deleted` (`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户关注关系表';

-- 插入示例数据
INSERT INTO `user_follow` (`follower_id`, `following_id`, `status`, `follow_type`, `remark_name`) VALUES
(1, 2, 0, 1, '好友小王'),
(1, 3, 0, 2, '特别关注'),
(2, 1, 0, 1, '好友小李'),
(3, 1, 0, 1, NULL),
(4, 1, 0, 1, NULL),
(5, 1, 0, 1, NULL);
