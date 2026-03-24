-- ============================================
-- 旅游平台推荐算法增强脚本
-- ============================================

-- ============================================
-- 1. 更新 Spot 表 - 新增支持推荐的字段
-- ============================================
ALTER TABLE `spot` 
ADD COLUMN IF NOT EXISTS `crowd_tags` VARCHAR(255) NULL COMMENT '适用人群标签:family,elder,couple,business,solo' AFTER `spotTags`,
ADD COLUMN IF NOT EXISTS `time_slot` VARCHAR(50) NULL COMMENT '最佳游览时段:morning,afternoon,evening,allday' AFTER `crowd_tags`,
ADD COLUMN IF NOT EXISTS `recommended_duration` INT NULL COMMENT '建议游玩时长（分钟）' AFTER `time_slot`,
ADD COLUMN IF NOT EXISTS `entrance_fee` DECIMAL(10,2) NULL COMMENT '成人门票价格' AFTER `recommended_duration`,
ADD COLUMN IF NOT EXISTS `spot_region` VARCHAR(100) NULL COMMENT '景点所属区域' AFTER `entrance_fee`;

-- ============================================
-- 2. 为现有景点补充数据
-- ============================================

-- 更新北京景点
UPDATE `spot` SET 
    crowd_tags = 'family,elder,couple',
    time_slot = 'morning',
    recommended_duration = 180,
    entrance_fee = 60.00,
    spot_region = '东城区'
WHERE `spotLocation` LIKE '%北京%' OR `spotName` IN ('故宫', '长城');

-- 更新西湖
UPDATE `spot` SET 
    crowd_tags = 'family,elder,couple',
    time_slot = 'morning',
    recommended_duration = 240,
    entrance_fee = 0.00,
    spot_region = '西湖区'
WHERE `spotName` LIKE '%西湖%';

-- 更新黄山
UPDATE `spot` SET 
    crowd_tags = 'couple,solo',
    time_slot = 'morning',
    recommended_duration = 360,
    entrance_fee = 150.00,
    spot_region = '黄山区'
WHERE `spotName` LIKE '%黄山%';

-- 更新九寨沟
UPDATE `spot` SET 
    crowd_tags = 'family,couple,solo',
    time_slot = 'morning',
    recommended_duration = 480,
    entrance_fee = 169.00,
    spot_region = '阿坝藏族羌族自治州'
WHERE `spotName` LIKE '%九寨沟%';

-- 更新布达拉宫
UPDATE `spot` SET 
    crowd_tags = 'elder,couple',
    time_slot = 'morning',
    recommended_duration = 180,
    entrance_fee = 200.00,
    spot_region = '拉萨市'
WHERE `spotName` LIKE '%布达拉宫%';

-- ============================================
-- 3. 创建用户交互记录表（用于协同过滤）
-- ============================================
CREATE TABLE IF NOT EXISTS `spot_interaction` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `spot_id` BIGINT NOT NULL COMMENT '景点ID',
    `interaction_type` VARCHAR(20) NOT NULL COMMENT '交互类型:view,favorite,score,order',
    `interaction_score` DECIMAL(5,2) NOT NULL DEFAULT 1.00 COMMENT '交互得分',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_spot_type` (`user_id`, `spot_id`, `interaction_type`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_spot_id` (`spot_id`),
    KEY `idx_interaction_type` (`interaction_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='用户景点交互记录表';

-- ============================================
-- 4. 创建景点相似度表（缓存协同过滤结果）
-- ============================================
CREATE TABLE IF NOT EXISTS `spot_similarity` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `spot_id_a` BIGINT NOT NULL COMMENT '景点A的ID',
    `spot_id_b` BIGINT NOT NULL COMMENT '景点B的ID',
    `similarity_score` DECIMAL(5,4) NOT NULL COMMENT '相似度得分(0-1)',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_spot_pair` (`spot_id_a`, `spot_id_b`),
    KEY `idx_spot_id_a` (`spot_id_a`),
    KEY `idx_spot_id_b` (`spot_id_b`),
    KEY `idx_similarity_score` (`similarity_score`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='景点相似度表';

-- ============================================
-- 5. 创建推荐日志表
-- ============================================
CREATE TABLE IF NOT EXISTS `recommend_log` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT COMMENT '用户ID（可为空表示未登录）',
    `request_params` TEXT COMMENT '请求参数JSON',
    `recommend_strategy` VARCHAR(50) NOT NULL COMMENT '推荐策略:cf,tag,hot,hybrid',
    `result_count` INT NOT NULL DEFAULT 0 COMMENT '推荐结果数量',
    `algorithm_version` VARCHAR(20) NOT NULL COMMENT '算法版本',
    `response_time_ms` INT COMMENT '响应耗时（毫秒）',
    `success` TINYINT NOT NULL DEFAULT 1 COMMENT '是否成功:1成功,0失败',
    `error_message` VARCHAR(500) COMMENT '错误信息',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_strategy` (`recommend_strategy`),
    KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='推荐日志表';

-- ============================================
-- 6. 创建路线规划日志表
-- ============================================
CREATE TABLE IF NOT EXISTS `route_plan_log` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT COMMENT '用户ID',
    `city` VARCHAR(100) COMMENT '目标城市',
    `day_count` INT COMMENT '天数',
    `budget` DECIMAL(10,2) COMMENT '预算',
    `pace_type` VARCHAR(20) COMMENT '节奏类型:RELAXED,NORMAL,COMPACT',
    `algorithm_type` VARCHAR(30) NOT NULL COMMENT '算法类型:greedy,genetic',
    `spot_count` INT COMMENT '规划景点数',
    `total_distance` DECIMAL(10,2) COMMENT '总距离（公里）',
    `optimization_score` DECIMAL(10,4) COMMENT '优化得分',
    `iterations` INT COMMENT '遗传算法迭代次数',
    `response_time_ms` INT COMMENT '响应耗时（毫秒）',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_city` (`city`),
    KEY `idx_algorithm_type` (`algorithm_type`),
    KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='路线规划日志表';

-- ============================================
-- 7. 创建 AI 调用日志表
-- ============================================
CREATE TABLE IF NOT EXISTS `ai_call_log` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT COMMENT '用户ID',
    `call_type` VARCHAR(30) NOT NULL COMMENT '调用类型:parse_intent,explain,route_gen',
    `request_prompt` TEXT COMMENT '请求提示词',
    `response_content` TEXT COMMENT '响应内容',
    `token_used` INT COMMENT '使用的Token数',
    `response_time_ms` INT COMMENT '响应耗时（毫秒）',
    `success` TINYINT NOT NULL DEFAULT 1 COMMENT '是否成功',
    `error_type` VARCHAR(50) COMMENT '错误类型:timeout,rate_limit,circuit_break,other',
    `error_message` VARCHAR(500) COMMENT '错误信息',
    `degraded` TINYINT NOT NULL DEFAULT 0 COMMENT '是否降级:1降级,0正常',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_call_type` (`call_type`),
    KEY `idx_success` (`success`),
    KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='AI调用日志表';

-- ============================================
-- 8. 插入示例交互数据（用于冷启动）
-- ============================================
INSERT INTO `spot_interaction` (`user_id`, `spot_id`, `interaction_type`, `interaction_score`) VALUES
-- 用户1 对故宫的交互
(1, 1892862826109427714, 'view', 1.00),
(1, 1892862826109427714, 'favorite', 5.00),
(1, 1892862826109427714, 'score', 5.00),
-- 用户1 对长城的交互
(1, 1893907615818850305, 'view', 1.00),
(1, 1893907615818850305, 'score', 4.00),
-- 用户1 对西湖的交互
(1, 1893908028072796161, 'view', 1.00),
(1, 1893908028072796161, 'favorite', 5.00),
-- 用户2 对颐和园的交互
(2, 1893905556574969858, 'view', 1.00),
(2, 1893905556574969858, 'favorite', 5.00),
-- 用户2 对故宫的交互
(2, 1892862826109427714, 'view', 1.00),
(2, 1892862826109427714, 'score', 4.00),
-- 用户3 对九寨沟的交互
(3, 1893908368084049922, 'view', 1.00),
(3, 1893908368084049922, 'favorite', 5.00),
(3, 1893908368084049922, 'score', 5.00);

-- ============================================
-- 9. 创建索引优化查询性能
-- ============================================
-- 推荐相关查询优化索引已在建表语句中包含

-- ============================================
-- 10. 更新 Spot 表的经纬度数据（如果还没有）
-- ============================================
UPDATE `spot` SET 
    latitude = 39.9169,
    longitude = 116.3907
WHERE `spotName` = '故宫' AND (latitude IS NULL OR latitude = 0);

UPDATE `spot` SET 
    latitude = 40.4319,
    longitude = 116.5704
WHERE `spotName` LIKE '%长城%' AND (latitude IS NULL OR latitude = 0);

UPDATE `spot` SET 
    latitude = 30.2500,
    longitude = 120.1675
WHERE `spotName` LIKE '%西湖%' AND (latitude IS NULL OR latitude = 0);

UPDATE `spot` SET 
    latitude = 29.6578,
    longitude = 91.1169
WHERE `spotName` LIKE '%布达拉宫%' AND (latitude IS NULL OR latitude = 0);

COMMIT;
