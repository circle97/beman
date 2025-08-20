-- 礼物推荐表
CREATE TABLE IF NOT EXISTS `gift_recommendation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(255) NOT NULL COMMENT '礼物名称',
  `description` text COMMENT '礼物描述',
  `image_url` varchar(500) DEFAULT NULL COMMENT '礼物图片URL',
  `min_price` decimal(10,2) NOT NULL COMMENT '最低价格',
  `max_price` decimal(10,2) NOT NULL COMMENT '最高价格',
  `min_age` int(11) DEFAULT NULL COMMENT '最低适用年龄',
  `max_age` int(11) DEFAULT NULL COMMENT '最高适用年龄',
  `gender` tinyint(4) NOT NULL DEFAULT '0' COMMENT '适用性别：0-不限，1-男，2-女',
  `occasions` json DEFAULT NULL COMMENT '适用场合：JSON格式',
  `categories` json DEFAULT NULL COMMENT '礼物类型：JSON格式',
  `tags` json DEFAULT NULL COMMENT '标签：JSON格式',
  `rating` tinyint(4) NOT NULL DEFAULT '3' COMMENT '推荐指数：1-5星',
  `reason` text COMMENT '推荐理由',
  `purchase_url` varchar(500) DEFAULT NULL COMMENT '购买链接',
  `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '状态：1-启用，2-禁用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  KEY `idx_status` (`status`),
  KEY `idx_rating` (`rating`),
  KEY `idx_price` (`min_price`, `max_price`),
  KEY `idx_gender` (`gender`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='礼物推荐表';

-- 插入示例数据
INSERT INTO `gift_recommendation` (`name`, `description`, `image_url`, `min_price`, `max_price`, `min_age`, `max_age`, `gender`, `occasions`, `categories`, `tags`, `rating`, `reason`, `purchase_url`, `status`) VALUES
('智能手表', '多功能智能手表，支持健康监测、运动追踪、消息提醒等功能', 'https://example.com/smartwatch.jpg', 299.00, 1299.00, 18, 65, 0, '["生日", "节日", "纪念日"]', '["电子产品", "数码配件"]', '["智能", "健康", "时尚"]', 5, '实用性强，适合各年龄段，是送礼的绝佳选择', 'https://example.com/buy/smartwatch', 1),
('香水套装', '精选多款香水，包含不同香调，适合各种场合使用', 'https://example.com/perfume.jpg', 199.00, 599.00, 20, 50, 2, '["生日", "节日", "约会"]', '["美妆", "香水"]', '["优雅", "浪漫", "精致"]', 4, '香水是女性最爱的礼物之一，能展现品味和关怀', 'https://example.com/buy/perfume', 1),
('乐高积木', '经典乐高积木套装，培养创造力和动手能力', 'https://example.com/lego.jpg', 99.00, 399.00, 6, 16, 0, '["生日", "节日", "儿童节"]', '["玩具", "益智"]', '["创意", "教育", "趣味"]', 5, '适合儿童和青少年，寓教于乐，深受欢迎', 'https://example.com/buy/lego', 1),
('红酒礼盒', '精选优质红酒，包装精美，适合商务送礼和节日庆祝', 'https://example.com/wine.jpg', 299.00, 899.00, 25, 70, 0, '["商务", "节日", "庆祝"]', '["酒水", "礼品"]', '["高端", "商务", "庆祝"]', 4, '红酒是商务送礼和节日庆祝的经典选择', 'https://example.com/buy/wine', 1),
('手工围巾', '纯手工编织围巾，材质柔软，款式多样', 'https://example.com/scarf.jpg', 89.00, 299.00, 18, 80, 0, '["生日", "节日", "冬季"]', '["服装", "配饰"]', '["温暖", "手工", "时尚"]', 4, '手工制品体现用心，围巾实用性强，适合各年龄段', 'https://example.com/buy/scarf', 1),
('书籍套装', '精选经典文学作品，包装精美，适合文学爱好者', 'https://example.com/books.jpg', 129.00, 399.00, 16, 80, 0, '["生日", "节日", "毕业"]', '["图书", "文化"]', '["文学", "经典", "文化"]', 4, '书籍是精神食粮，适合送给爱读书的人', 'https://example.com/buy/books', 1),
('运动鞋', '专业运动鞋，舒适透气，适合运动和日常穿着', 'https://example.com/shoes.jpg', 199.00, 699.00, 12, 60, 0, '["生日", "节日", "运动"]', '["服装", "运动"]', '["运动", "舒适", "时尚"]', 4, '运动鞋实用性强，适合各年龄段，是送礼的好选择', 'https://example.com/buy/shoes', 1),
('咖啡机', '全自动咖啡机，操作简单，能制作各种咖啡', 'https://example.com/coffee.jpg', 599.00, 1999.00, 25, 65, 0, '["生日", "节日", "乔迁"]', '["家电", "厨房"]', '["咖啡", "品质", "生活"]', 5, '咖啡机提升生活品质，适合送给咖啡爱好者', 'https://example.com/buy/coffee', 1),
('手工钱包', '真皮手工钱包，做工精细，款式经典', 'https://example.com/wallet.jpg', 199.00, 599.00, 18, 70, 1, '["生日", "节日", "商务"]', '["配饰", "皮具"]', '["商务", "经典", "品质"]', 4, '钱包是男性日常必需品，手工制作体现品质', 'https://example.com/buy/wallet', 1),
('花束', '精选鲜花，搭配精美包装，适合各种浪漫场合', 'https://example.com/flowers.jpg', 99.00, 399.00, 18, 80, 0, '["生日", "节日", "约会", "求婚"]', '["鲜花", "礼品"]', '["浪漫", "美丽", "祝福"]', 5, '鲜花是表达爱意和祝福的最佳选择', 'https://example.com/buy/flowers', 1);
