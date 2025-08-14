-- 创建数据库
CREATE DATABASE IF NOT EXISTS beman DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE beman;

-- 用户表
CREATE TABLE IF NOT EXISTS `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `nickname` varchar(50) DEFAULT NULL COMMENT '昵称',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `status` tinyint(4) DEFAULT '1' COMMENT '状态：0-禁用，1-正常',
  `user_type` tinyint(4) DEFAULT '1' COMMENT '用户类型：1-实名用户，2-匿名用户',
  `privacy_level` tinyint(4) DEFAULT '0' COMMENT '隐私等级：0-公开，1-仅好友，2-完全私密',
  `anonymous_id` varchar(50) DEFAULT NULL COMMENT '匿名ID',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(4) DEFAULT '0' COMMENT '逻辑删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  KEY `idx_user_type` (`user_type`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 帖子表
CREATE TABLE IF NOT EXISTS `post` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `anonymous_id` varchar(50) DEFAULT NULL COMMENT '匿名ID（匿名发帖时使用）',
  `title` varchar(200) NOT NULL COMMENT '标题',
  `content` text NOT NULL COMMENT '内容',
  `content_type` tinyint(4) DEFAULT '1' COMMENT '内容类型：1-文字，2-语音',
  `voice_url` varchar(255) DEFAULT NULL COMMENT '语音文件URL',
  `tags` json DEFAULT NULL COMMENT '标签（JSON格式）',
  `view_count` int(11) DEFAULT '0' COMMENT '浏览次数',
  `like_count` int(11) DEFAULT '0' COMMENT '点赞次数',
  `comment_count` int(11) DEFAULT '0' COMMENT '评论次数',
  `status` tinyint(4) DEFAULT '1' COMMENT '状态：0-审核中，1-正常，2-已删除',
  `is_top` tinyint(4) DEFAULT '0' COMMENT '是否置顶：0-否，1-是',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(4) DEFAULT '0' COMMENT '逻辑删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='帖子表';

-- 分类表
CREATE TABLE IF NOT EXISTS `category` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint(20) DEFAULT '0' COMMENT '用户ID（0表示系统默认分类）',
  `name` varchar(50) NOT NULL COMMENT '分类名称',
  `icon` varchar(100) DEFAULT NULL COMMENT '分类图标',
  `color` varchar(20) DEFAULT NULL COMMENT '分类颜色',
  `type` tinyint(4) NOT NULL COMMENT '分类类型：1-收入分类，2-支出分类',
  `sort_order` int(11) DEFAULT '0' COMMENT '排序',
  `enabled` tinyint(4) DEFAULT '1' COMMENT '是否启用：0-禁用，1-启用',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(4) DEFAULT '0' COMMENT '逻辑删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_type` (`type`),
  KEY `idx_enabled` (`enabled`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='分类表';

-- 交易记录表
CREATE TABLE IF NOT EXISTS `transaction` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `type` tinyint(4) NOT NULL COMMENT '交易类型：1-收入，2-支出',
  `amount` decimal(10,2) NOT NULL COMMENT '金额',
  `category_id` bigint(20) NOT NULL COMMENT '分类ID',
  `category_name` varchar(50) DEFAULT NULL COMMENT '分类名称（冗余字段）',
  `category_icon` varchar(100) DEFAULT NULL COMMENT '分类图标',
  `description` varchar(200) NOT NULL COMMENT '描述',
  `transaction_date` datetime NOT NULL COMMENT '交易日期',
  `payment_method` tinyint(4) DEFAULT '1' COMMENT '支付方式：1-现金，2-支付宝，3-微信，4-银行卡',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `status` tinyint(4) DEFAULT '1' COMMENT '状态：0-待确认，1-已确认，2-已取消',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(4) DEFAULT '0' COMMENT '逻辑删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_type` (`type`),
  KEY `idx_category_id` (`category_id`),
  KEY `idx_transaction_date` (`transaction_date`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='交易记录表';

-- 插入默认分类数据
INSERT INTO `category` (`user_id`, `name`, `icon`, `color`, `type`, `sort_order`, `enabled`) VALUES
-- 收入分类
(0, '工资', '💰', '#4caf50', 1, 1, 1),
(0, '奖金', '🎁', '#ff9800', 1, 2, 1),
(0, '投资收益', '📈', '#2196f3', 1, 3, 1),
(0, '兼职收入', '💼', '#9c27b0', 1, 4, 1),
(0, '其他收入', '📦', '#607d8b', 1, 5, 1),

-- 支出分类
(0, '餐饮', '🍽️', '#f44336', 2, 1, 1),
(0, '交通', '🚗', '#2196f3', 2, 2, 1),
(0, '购物', '🛒', '#ff9800', 2, 3, 1),
(0, '娱乐', '🎮', '#9c27b0', 2, 4, 1),
(0, '医疗', '💊', '#4caf50', 2, 5, 1),
(0, '教育', '📚', '#607d8b', 2, 6, 1),
(0, '住房', '🏠', '#795548', 2, 7, 1),
(0, '通讯', '📱', '#00bcd4', 2, 8, 1),
(0, '服装', '👕', '#e91e63', 2, 9, 1),
(0, '其他支出', '📦', '#9e9e9e', 2, 10, 1);

-- 评论表
CREATE TABLE IF NOT EXISTS `comment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `post_id` bigint(20) NOT NULL COMMENT '帖子ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `anonymous_id` varchar(50) DEFAULT NULL COMMENT '匿名ID',
  `content` text NOT NULL COMMENT '评论内容',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父评论ID（回复时使用）',
  `like_count` int(11) NOT NULL DEFAULT '0' COMMENT '点赞次数',
  `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '状态：0-审核中，1-正常，2-已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '逻辑删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  KEY `idx_post_id` (`post_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评论表';

-- 财务记录表
CREATE TABLE IF NOT EXISTS `finance_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `amount` decimal(10,2) NOT NULL COMMENT '金额',
  `type` tinyint(4) NOT NULL COMMENT '类型：1-收入，2-支出',
  `category` varchar(50) NOT NULL COMMENT '分类',
  `description` varchar(200) DEFAULT NULL COMMENT '描述',
  `is_shared` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否共同支出：0-否，1-是',
  `voice_url` varchar(255) DEFAULT NULL COMMENT '语音记录URL',
  `record_date` date NOT NULL COMMENT '记录日期',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '逻辑删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_type` (`type`),
  KEY `idx_category` (`category`),
  KEY `idx_record_date` (`record_date`),
  KEY `idx_is_shared` (`is_shared`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='财务记录表';

-- 日程表
CREATE TABLE IF NOT EXISTS `schedule` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `title` varchar(200) NOT NULL COMMENT '标题',
  `description` text COMMENT '描述',
  `event_date` date NOT NULL COMMENT '事件日期',
  `event_time` time DEFAULT NULL COMMENT '事件时间',
  `remind_days` varchar(50) DEFAULT NULL COMMENT '提醒天数（JSON格式，如[3,7,30]）',
  `is_important` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否重要：0-否，1-是',
  `gift_suggestion` varchar(500) DEFAULT NULL COMMENT '礼物建议',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '逻辑删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_event_date` (`event_date`),
  KEY `idx_is_important` (`is_important`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='日程表';

-- 情绪分析记录表
CREATE TABLE IF NOT EXISTS `emotion_analysis` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `dialogue` text NOT NULL COMMENT '对话内容',
  `emotion_result` varchar(500) NOT NULL COMMENT '情绪分析结果（JSON格式）',
  `suggestion` text COMMENT '应对建议',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '逻辑删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='情绪分析记录表';

-- 成长计划表
CREATE TABLE IF NOT EXISTS `growth_plan` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `title` varchar(200) NOT NULL COMMENT '计划标题',
  `description` text COMMENT '计划描述',
  `plan_type` tinyint(4) NOT NULL COMMENT '计划类型：1-沟通练习，2-情绪管理，3-关系经营',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '状态：0-进行中，1-已完成，2-已放弃',
  `start_date` date NOT NULL COMMENT '开始日期',
  `end_date` date DEFAULT NULL COMMENT '结束日期',
  `progress` int(11) NOT NULL DEFAULT '0' COMMENT '进度（0-100）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '逻辑删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_plan_type` (`plan_type`),
  KEY `idx_status` (`status`),
  KEY `idx_start_date` (`start_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='成长计划表';

-- 成就勋章表
CREATE TABLE IF NOT EXISTS `achievement` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `achievement_type` varchar(50) NOT NULL COMMENT '成就类型',
  `achievement_name` varchar(100) NOT NULL COMMENT '成就名称',
  `description` varchar(200) COMMENT '成就描述',
  `icon_url` varchar(255) DEFAULT NULL COMMENT '勋章图标URL',
  `unlock_date` datetime DEFAULT NULL COMMENT '解锁时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '逻辑删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_achievement` (`user_id`, `achievement_type`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_achievement_type` (`achievement_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='成就勋章表'; 