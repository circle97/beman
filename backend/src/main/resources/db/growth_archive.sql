-- 成长档案功能数据库表结构
-- 创建时间: 2024年12月25日
-- 功能: 关系里程碑记录、成长轨迹分析、目标管理、成就系统

-- 关系里程碑表
CREATE TABLE IF NOT EXISTS `relationship_milestone` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `milestone_type` varchar(50) NOT NULL COMMENT '里程碑类型：first_meet-初次见面, first_date-第一次约会, confession-表白, engagement-订婚, wedding-结婚, anniversary-纪念日',
  `title` varchar(200) NOT NULL COMMENT '里程碑标题',
  `description` text COMMENT '里程碑描述',
  `milestone_date` date NOT NULL COMMENT '里程碑日期',
  `location` varchar(200) DEFAULT NULL COMMENT '地点',
  `emotion_score` int(11) DEFAULT NULL COMMENT '情感评分(1-10)',
  `photos` json DEFAULT NULL COMMENT '照片URL数组',
  `tags` json DEFAULT NULL COMMENT '标签数组',
  `is_public` tinyint(4) DEFAULT '1' COMMENT '是否公开：0-私密，1-公开',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(4) DEFAULT '0' COMMENT '逻辑删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_milestone_type` (`milestone_type`),
  KEY `idx_milestone_date` (`milestone_date`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='关系里程碑表';

-- 成长轨迹记录表
CREATE TABLE IF NOT EXISTS `growth_trajectory` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `record_date` date NOT NULL COMMENT '记录日期',
  `communication_score` int(11) DEFAULT NULL COMMENT '沟通质量评分(1-100)',
  `trust_score` int(11) DEFAULT NULL COMMENT '信任程度评分(1-100)',
  `support_score` int(11) DEFAULT NULL COMMENT '相互支持评分(1-100)',
  `intimacy_score` int(11) DEFAULT NULL COMMENT '亲密度评分(1-100)',
  `overall_score` int(11) DEFAULT NULL COMMENT '综合评分(1-100)',
  `mood_state` varchar(50) DEFAULT NULL COMMENT '情绪状态：happy-开心, sad-难过, angry-生气, calm-平静, excited-兴奋',
  `relationship_notes` text COMMENT '关系笔记',
  `improvement_goals` text COMMENT '改进目标',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(4) DEFAULT '0' COMMENT '逻辑删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_date` (`user_id`, `record_date`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_record_date` (`record_date`),
  KEY `idx_overall_score` (`overall_score`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='成长轨迹记录表';

-- 关系目标表
CREATE TABLE IF NOT EXISTS `relationship_goal` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `goal_type` varchar(50) NOT NULL COMMENT '目标类型：communication-沟通改善, trust-信任建立, intimacy-亲密提升, conflict-冲突解决, support-相互支持',
  `title` varchar(200) NOT NULL COMMENT '目标标题',
  `description` text COMMENT '目标描述',
  `target_date` date NOT NULL COMMENT '目标完成日期',
  `priority` tinyint(4) DEFAULT '2' COMMENT '优先级：1-高，2-中，3-低',
  `status` tinyint(4) DEFAULT '0' COMMENT '状态：0-进行中，1-已完成，2-已延期，3-已放弃',
  `progress` int(11) DEFAULT '0' COMMENT '进度(0-100)',
  `milestones` json DEFAULT NULL COMMENT '子目标里程碑',
  `reward` varchar(200) DEFAULT NULL COMMENT '完成奖励',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(4) DEFAULT '0' COMMENT '逻辑删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_goal_type` (`goal_type`),
  KEY `idx_status` (`status`),
  KEY `idx_target_date` (`target_date`),
  KEY `idx_priority` (`priority`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='关系目标表';

-- 成长档案配置表
CREATE TABLE IF NOT EXISTS `growth_profile_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `config_key` varchar(100) NOT NULL COMMENT '配置键',
  `config_value` text COMMENT '配置值',
  `config_type` varchar(50) DEFAULT 'string' COMMENT '配置类型：string-字符串, number-数字, boolean-布尔值, json-JSON对象',
  `description` varchar(200) DEFAULT NULL COMMENT '配置描述',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(4) DEFAULT '0' COMMENT '逻辑删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_key` (`user_id`, `config_key`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_config_key` (`config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='成长档案配置表';

-- 插入默认配置数据
INSERT INTO `growth_profile_config` (`user_id`, `config_key`, `config_value`, `config_type`, `description`) VALUES
(0, 'milestone_types', '["first_meet","first_date","confession","engagement","wedding","anniversary","travel","gift","conflict_resolution","breakthrough"]', 'json', '里程碑类型配置'),
(0, 'goal_categories', '["communication","trust","intimacy","conflict","support","quality_time","understanding","appreciation"]', 'json', '目标分类配置'),
(0, 'emotion_states', '["happy","excited","content","calm","neutral","worried","sad","angry","frustrated"]', 'json', '情绪状态配置'),
(0, 'score_weights', '{"communication": 0.3, "trust": 0.25, "support": 0.2, "intimacy": 0.15, "conflict_resolution": 0.1}', 'json', '评分权重配置');

-- 成长档案统计视图
CREATE OR REPLACE VIEW `growth_profile_stats` AS
SELECT 
  g.user_id,
  COUNT(DISTINCT rm.id) as milestone_count,
  COUNT(DISTINCT gt.id) as trajectory_count,
  COUNT(DISTINCT rg.id) as goal_count,
  COUNT(DISTINCT CASE WHEN rg.status = 1 THEN rg.id END) as completed_goal_count,
  AVG(gt.overall_score) as avg_overall_score,
  MAX(gt.overall_score) as max_overall_score,
  MIN(gt.overall_score) as min_overall_score,
  MAX(rm.milestone_date) as last_milestone_date,
  MAX(gt.record_date) as last_trajectory_date
FROM `user` u
LEFT JOIN `relationship_milestone` rm ON u.id = rm.user_id AND rm.deleted = 0
LEFT JOIN `growth_trajectory` gt ON u.id = gt.user_id AND gt.deleted = 0
LEFT JOIN `relationship_goal` rg ON u.id = rg.user_id AND rg.deleted = 0
WHERE u.deleted = 0
GROUP BY g.user_id;
