-- 举报表
CREATE TABLE IF NOT EXISTS `report` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `reporter_id` bigint(20) NOT NULL COMMENT '举报者ID',
  `reporter_name` varchar(100) NOT NULL COMMENT '举报者名称',
  `content_type` tinyint(4) NOT NULL COMMENT '被举报内容类型：1-帖子，2-评论，3-用户，4-其他',
  `content_id` bigint(20) NOT NULL COMMENT '被举报内容ID',
  `reason_type` tinyint(4) NOT NULL COMMENT '举报原因类型：1-垃圾信息，2-不当内容，3-恶意行为，4-版权问题，5-其他',
  `description` text COMMENT '举报详细描述',
  `evidence` text COMMENT '举报证据（图片URL等）',
  `status` tinyint(4) DEFAULT 0 COMMENT '举报状态：0-待处理，1-处理中，2-已处理，3-已驳回，4-已关闭',
  `result` text COMMENT '处理结果',
  `handler_id` bigint(20) DEFAULT NULL COMMENT '处理人ID',
  `handler_name` varchar(100) DEFAULT NULL COMMENT '处理人名称',
  `handle_time` datetime DEFAULT NULL COMMENT '处理时间',
  `priority` tinyint(4) DEFAULT 1 COMMENT '优先级：1-低，2-中，3-高，4-紧急',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) DEFAULT 0 COMMENT '逻辑删除标识',
  PRIMARY KEY (`id`),
  KEY `idx_reporter_id` (`reporter_id`),
  KEY `idx_content_type` (`content_type`),
  KEY `idx_content_id` (`content_id`),
  KEY `idx_reason_type` (`reason_type`),
  KEY `idx_status` (`status`),
  KEY `idx_priority` (`priority`),
  KEY `idx_handler_id` (`handler_id`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_deleted` (`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='举报表';

-- 插入示例数据
INSERT INTO `report` (`reporter_id`, `reporter_name`, `content_type`, `content_id`, `reason_type`, `description`, `status`, `priority`) VALUES
(1, '用户001', 1, 1, 2, '这个帖子包含不当内容，违反了社区规定', 0, 3),
(2, '用户002', 2, 1, 1, '这条评论是垃圾信息，没有实际意义', 0, 2),
(3, '用户003', 3, 5, 3, '该用户恶意刷屏，影响其他用户正常使用', 1, 4),
(4, '用户004', 1, 3, 4, '这个帖子涉嫌抄袭，侵犯了原创作者的版权', 0, 3),
(5, '用户005', 2, 2, 5, '其他原因，需要管理员进一步核实', 0, 1);
