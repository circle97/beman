-- BeMan 数据库初始化脚本
-- 创建数据库
CREATE DATABASE IF NOT EXISTS beman DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE beman;

-- 用户表
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码',
    phone VARCHAR(20) NOT NULL UNIQUE COMMENT '手机号',
    email VARCHAR(100) COMMENT '邮箱',
    nickname VARCHAR(50) COMMENT '昵称',
    avatar VARCHAR(255) COMMENT '头像URL',
    relationship_status ENUM('SINGLE', 'IN_RELATIONSHIP', 'MARRIED', 'DIVORCED') COMMENT '感情状态',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    last_login_at TIMESTAMP NULL COMMENT '最后登录时间',
    is_active BOOLEAN DEFAULT TRUE COMMENT '是否激活',
    INDEX idx_username (username),
    INDEX idx_phone (phone),
    INDEX idx_email (email),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 账单表
CREATE TABLE IF NOT EXISTS bills (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '账单ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    amount DECIMAL(10,2) NOT NULL COMMENT '金额',
    type ENUM('INCOME', 'EXPENSE') NOT NULL COMMENT '账单类型：收入/支出',
    date DATE NOT NULL COMMENT '账单日期',
    category VARCHAR(50) NOT NULL COMMENT '分类',
    description TEXT COMMENT '描述',
    is_shared BOOLEAN DEFAULT FALSE COMMENT '是否共享',
    shared_percentage INT DEFAULT 50 COMMENT '共享百分比',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_user_id (user_id),
    INDEX idx_date (date),
    INDEX idx_type (type),
    INDEX idx_category (category),
    INDEX idx_user_date (user_id, date),
    INDEX idx_created_at (created_at),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='账单表';

-- 社区帖子表
CREATE TABLE IF NOT EXISTS community_posts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '帖子ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    title VARCHAR(200) NOT NULL COMMENT '标题',
    content TEXT NOT NULL COMMENT '内容',
    post_type ENUM('HELP', 'EXPERIENCE', 'SHARE') NOT NULL COMMENT '帖子类型：求助/经验/分享',
    tags JSON COMMENT '标签数组',
    is_anonymous BOOLEAN DEFAULT TRUE COMMENT '是否匿名',
    view_count INT DEFAULT 0 COMMENT '浏览次数',
    like_count INT DEFAULT 0 COMMENT '点赞次数',
    comment_count INT DEFAULT 0 COMMENT '评论次数',
    status ENUM('PENDING', 'APPROVED', 'REJECTED') DEFAULT 'PENDING' COMMENT '审核状态',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_user_id (user_id),
    INDEX idx_post_type (post_type),
    INDEX idx_status (status),
    INDEX idx_created_at (created_at),
    FULLTEXT idx_content (title, content),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='社区帖子表';

-- 社区评论表
CREATE TABLE IF NOT EXISTS community_comments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '评论ID',
    post_id BIGINT NOT NULL COMMENT '帖子ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    parent_id BIGINT NULL COMMENT '父评论ID',
    content TEXT NOT NULL COMMENT '评论内容',
    is_anonymous BOOLEAN DEFAULT TRUE COMMENT '是否匿名',
    like_count INT DEFAULT 0 COMMENT '点赞次数',
    status ENUM('PENDING', 'APPROVED', 'REJECTED') DEFAULT 'PENDING' COMMENT '审核状态',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_post_id (post_id),
    INDEX idx_user_id (user_id),
    INDEX idx_parent_id (parent_id),
    INDEX idx_status (status),
    INDEX idx_created_at (created_at),
    FOREIGN KEY (post_id) REFERENCES community_posts(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (parent_id) REFERENCES community_comments(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='社区评论表';

-- 日程提醒表
CREATE TABLE IF NOT EXISTS schedules (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '日程ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    title VARCHAR(200) NOT NULL COMMENT '标题',
    description TEXT COMMENT '描述',
    event_date DATE NOT NULL COMMENT '事件日期',
    event_type ENUM('BIRTHDAY', 'ANNIVERSARY', 'HOLIDAY', 'CUSTOM') NOT NULL COMMENT '事件类型',
    reminder_days JSON COMMENT '提醒天数数组[3,7,30]',
    gift_suggestion TEXT COMMENT '礼物建议',
    is_repeated BOOLEAN DEFAULT FALSE COMMENT '是否重复',
    repeat_type ENUM('YEARLY', 'MONTHLY', 'WEEKLY') COMMENT '重复类型',
    status ENUM('ACTIVE', 'COMPLETED', 'CANCELLED') DEFAULT 'ACTIVE' COMMENT '状态',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_user_id (user_id),
    INDEX idx_event_date (event_date),
    INDEX idx_event_type (event_type),
    INDEX idx_status (status),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='日程提醒表';

-- 情绪记录表
CREATE TABLE IF NOT EXISTS emotion_records (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '记录ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    partner_text TEXT NOT NULL COMMENT '伴侣话语',
    emotion_analysis JSON COMMENT '情绪分析结果',
    suggested_response TEXT COMMENT '建议回应',
    avoid_words JSON COMMENT '避免使用的词汇',
    action_suggestions JSON COMMENT '行动建议',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_user_id (user_id),
    INDEX idx_created_at (created_at),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='情绪记录表';

-- 沟通练习表
CREATE TABLE IF NOT EXISTS communication_practices (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '练习ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    scenario_title VARCHAR(200) NOT NULL COMMENT '场景标题',
    scenario_description TEXT NOT NULL COMMENT '场景描述',
    user_response TEXT COMMENT '用户回应',
    ai_feedback JSON COMMENT 'AI反馈',
    score INT COMMENT '评分(1-10)',
    practice_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '练习时间',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_user_id (user_id),
    INDEX idx_practice_date (practice_date),
    INDEX idx_score (score),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='沟通练习表';

-- 用户成就表
CREATE TABLE IF NOT EXISTS user_achievements (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '成就ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    achievement_type ENUM('FINANCE', 'SCHEDULE', 'EMOTION', 'COMMUNICATION', 'COMMUNITY') NOT NULL COMMENT '成就类型',
    achievement_name VARCHAR(100) NOT NULL COMMENT '成就名称',
    achievement_description TEXT COMMENT '成就描述',
    progress INT DEFAULT 0 COMMENT '进度',
    target INT NOT NULL COMMENT '目标值',
    is_completed BOOLEAN DEFAULT FALSE COMMENT '是否完成',
    completed_at TIMESTAMP NULL COMMENT '完成时间',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_user_id (user_id),
    INDEX idx_achievement_type (achievement_type),
    INDEX idx_is_completed (is_completed),
    UNIQUE KEY uk_user_achievement (user_id, achievement_name),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户成就表';

-- 插入初始数据（可选）
-- 插入测试用户
INSERT INTO users (username, password, phone, email, nickname, relationship_status) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', '13800138000', 'admin@beman.com', '管理员', 'SINGLE'),
('test_user', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', '13800138001', 'test@beman.com', '测试用户', 'IN_RELATIONSHIP')
ON DUPLICATE KEY UPDATE updated_at = CURRENT_TIMESTAMP;

-- 插入测试账单数据
INSERT INTO bills (user_id, amount, type, date, category, description) VALUES
(1, 5000.00, 'INCOME', CURDATE(), '工资', '月薪'),
(1, 100.00, 'EXPENSE', CURDATE(), '餐饮', '午餐'),
(1, 200.00, 'EXPENSE', CURDATE(), '交通', '打车'),
(2, 3000.00, 'INCOME', CURDATE(), '工资', '月薪'),
(2, 150.00, 'EXPENSE', CURDATE(), '购物', '买衣服')
ON DUPLICATE KEY UPDATE updated_at = CURRENT_TIMESTAMP;

-- 社区帖子表
CREATE TABLE IF NOT EXISTS community_posts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '帖子ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    title VARCHAR(200) NOT NULL COMMENT '标题',
    content TEXT NOT NULL COMMENT '内容',
    post_type ENUM('HELP', 'EXPERIENCE', 'SHARE') NOT NULL COMMENT '帖子类型：求助/经验/分享',
    tags JSON COMMENT '标签数组',
    is_anonymous BOOLEAN DEFAULT TRUE COMMENT '是否匿名',
    view_count INT DEFAULT 0 COMMENT '浏览次数',
    like_count INT DEFAULT 0 COMMENT '点赞次数',
    comment_count INT DEFAULT 0 COMMENT '评论次数',
    status ENUM('PENDING', 'APPROVED', 'REJECTED') DEFAULT 'PENDING' COMMENT '审核状态',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_user_id (user_id),
    INDEX idx_post_type (post_type),
    INDEX idx_status (status),
    INDEX idx_created_at (created_at),
    FULLTEXT idx_content (title, content),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='社区帖子表';

-- 社区评论表
CREATE TABLE IF NOT EXISTS community_comments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '评论ID',
    post_id BIGINT NOT NULL COMMENT '帖子ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    parent_id BIGINT NULL COMMENT '父评论ID',
    content TEXT NOT NULL COMMENT '评论内容',
    is_anonymous BOOLEAN DEFAULT TRUE COMMENT '是否匿名',
    like_count INT DEFAULT 0 COMMENT '点赞次数',
    status ENUM('PENDING', 'APPROVED', 'REJECTED') DEFAULT 'PENDING' COMMENT '审核状态',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_post_id (post_id),
    INDEX idx_user_id (user_id),
    INDEX idx_parent_id (parent_id),
    INDEX idx_status (status),
    INDEX idx_created_at (created_at),
    FOREIGN KEY (post_id) REFERENCES community_posts(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (parent_id) REFERENCES community_comments(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='社区评论表';

-- 日程提醒表
CREATE TABLE IF NOT EXISTS schedules (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '日程ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    title VARCHAR(200) NOT NULL COMMENT '标题',
    description TEXT COMMENT '描述',
    event_date DATE NOT NULL COMMENT '事件日期',
    event_type ENUM('BIRTHDAY', 'ANNIVERSARY', 'HOLIDAY', 'CUSTOM') NOT NULL COMMENT '事件类型',
    reminder_days JSON COMMENT '提醒天数数组[3,7,30]',
    gift_suggestion TEXT COMMENT '礼物建议',
    is_repeated BOOLEAN DEFAULT FALSE COMMENT '是否重复',
    repeat_type ENUM('YEARLY', 'MONTHLY', 'WEEKLY') COMMENT '重复类型',
    status ENUM('ACTIVE', 'COMPLETED', 'CANCELLED') DEFAULT 'ACTIVE' COMMENT '状态',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_user_id (user_id),
    INDEX idx_event_date (event_date),
    INDEX idx_event_type (event_type),
    INDEX idx_status (status),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='日程提醒表';

-- 情绪记录表
CREATE TABLE IF NOT EXISTS emotion_records (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '记录ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    partner_text TEXT NOT NULL COMMENT '伴侣话语',
    emotion_analysis JSON COMMENT '情绪分析结果',
    suggested_response TEXT COMMENT '建议回应',
    avoid_words JSON COMMENT '避免使用的词汇',
    action_suggestions JSON COMMENT '行动建议',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_user_id (user_id),
    INDEX idx_created_at (created_at),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='情绪记录表';

-- 沟通练习表
CREATE TABLE IF NOT EXISTS communication_practices (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '练习ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    scenario_title VARCHAR(200) NOT NULL COMMENT '场景标题',
    scenario_description TEXT NOT NULL COMMENT '场景描述',
    user_response TEXT COMMENT '用户回应',
    ai_feedback JSON COMMENT 'AI反馈',
    score INT COMMENT '评分(1-10)',
    practice_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '练习时间',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_user_id (user_id),
    INDEX idx_practice_date (practice_date),
    INDEX idx_score (score),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='沟通练习表';

-- 用户成就表
CREATE TABLE IF NOT EXISTS user_achievements (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '成就ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    achievement_type ENUM('FINANCE', 'SCHEDULE', 'EMOTION', 'COMMUNICATION', 'COMMUNITY') NOT NULL COMMENT '成就类型',
    achievement_name VARCHAR(100) NOT NULL COMMENT '成就名称',
    achievement_description TEXT COMMENT '成就描述',
    progress INT DEFAULT 0 COMMENT '进度',
    target INT NOT NULL COMMENT '目标值',
    is_completed BOOLEAN DEFAULT FALSE COMMENT '是否完成',
    completed_at TIMESTAMP NULL COMMENT '完成时间',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_user_id (user_id),
    INDEX idx_achievement_type (achievement_type),
    INDEX idx_is_completed (is_completed),
    UNIQUE KEY uk_user_achievement (user_id, achievement_name),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户成就表';

-- 插入测试社区帖子
INSERT INTO community_posts (user_id, title, content, post_type, tags, is_anonymous) VALUES
(1, '如何化解冷战？', '最近和女朋友冷战了，想请教大家有什么好的化解方法？', 'HELP', '["冷战化解", "关系修复"]', TRUE),
(2, '我的挽回经验分享', '经过3个月的努力，终于和女朋友和好了，分享一下我的经验...', 'EXPERIENCE', '["挽回经验", "关系经营"]', TRUE)
ON DUPLICATE KEY UPDATE updated_at = CURRENT_TIMESTAMP;

-- 插入测试日程
INSERT INTO schedules (user_id, title, description, event_date, event_type, reminder_days, gift_suggestion) VALUES
(2, '女朋友生日', '女朋友25岁生日', DATE_ADD(CURDATE(), INTERVAL 7 DAY), 'BIRTHDAY', '[3, 7]', '考虑买她喜欢的化妆品'),
(2, '恋爱纪念日', '在一起2周年纪念日', DATE_ADD(CURDATE(), INTERVAL 30 DAY), 'ANNIVERSARY', '[7, 30]', '准备浪漫的晚餐')
ON DUPLICATE KEY UPDATE updated_at = CURRENT_TIMESTAMP;

-- 插入测试情绪记录
INSERT INTO emotion_records (user_id, partner_text, emotion_analysis, suggested_response, avoid_words, action_suggestions) VALUES
(2, '我今天好累啊', '{"emotion": "疲惫", "need": "关心", "confidence": 0.85}', '辛苦了，我给你揉揉肩吧？', '["我也很累", "那你休息吧"]', '["主动分担家务", "准备热茶", "给予拥抱"]'),
(2, '你都不关心我', '{"emotion": "失望", "need": "关注", "confidence": 0.92}', '对不起，我最近确实忽略你了，我们聊聊吧', '["我哪里不关心你了", "你太敏感了"]', '["安排约会", "增加陪伴时间", "表达爱意"]')
ON DUPLICATE KEY UPDATE updated_at = CURRENT_TIMESTAMP;

-- 插入测试社区帖子
INSERT INTO community_posts (user_id, title, content, post_type, tags, is_anonymous) VALUES
(1, '如何化解冷战？', '最近和女朋友冷战了，想请教大家有什么好的化解方法？', 'HELP', '["冷战化解", "关系修复"]', TRUE),
(2, '我的挽回经验分享', '经过3个月的努力，终于和女朋友和好了，分享一下我的经验...', 'EXPERIENCE', '["挽回经验", "关系经营"]', TRUE)
ON DUPLICATE KEY UPDATE updated_at = CURRENT_TIMESTAMP;

-- 插入测试日程
INSERT INTO schedules (user_id, title, description, event_date, event_type, reminder_days, gift_suggestion) VALUES
(2, '女朋友生日', '女朋友25岁生日', DATE_ADD(CURDATE(), INTERVAL 7 DAY), 'BIRTHDAY', '[3, 7]', '考虑买她喜欢的化妆品'),
(2, '恋爱纪念日', '在一起2周年纪念日', DATE_ADD(CURDATE(), INTERVAL 30 DAY), 'ANNIVERSARY', '[7, 30]', '准备浪漫的晚餐')
ON DUPLICATE KEY UPDATE updated_at = CURRENT_TIMESTAMP;

-- 插入测试情绪记录
INSERT INTO emotion_records (user_id, partner_text, emotion_analysis, suggested_response, avoid_words, action_suggestions) VALUES
(2, '我今天好累啊', '{"emotion": "疲惫", "need": "关心", "confidence": 0.85}', '辛苦了，我给你揉揉肩吧？', '["我也很累", "那你休息吧"]', '["主动分担家务", "准备热茶", "给予拥抱"]'),
(2, '你都不关心我', '{"emotion": "失望", "need": "关注", "confidence": 0.92}', '对不起，我最近确实忽略你了，我们聊聊吧', '["我哪里不关心你了", "你太敏感了"]', '["安排约会", "增加陪伴时间", "表达爱意"]')
ON DUPLICATE KEY UPDATE updated_at = CURRENT_TIMESTAMP; 