-- ============================================
-- 旅游平台推荐算法增量脚本
-- 基于 tourism_20260324_1722.sql 数据库结构
-- MySQL 8.0 版本
-- ============================================

-- ============================================
-- 1. 为 Spot 表新增支持推荐的字段
-- ============================================

-- 添加 crowd_tags 字段
SET @column_exists = (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
    WHERE TABLE_SCHEMA = DATABASE() 
    AND TABLE_NAME = 'spot' 
    AND COLUMN_NAME = 'crowd_tags'
);
SET @sql = IF(@column_exists = 0, 
    'ALTER TABLE `spot` ADD COLUMN `crowd_tags` VARCHAR(255) NULL COMMENT ''适用人群标签:family,elder,couple,business,solo'' AFTER `spotTags`',
    'SELECT ''Column crowd_tags already exists''');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 添加 time_slot 字段
SET @column_exists = (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
    WHERE TABLE_SCHEMA = DATABASE() 
    AND TABLE_NAME = 'spot' 
    AND COLUMN_NAME = 'time_slot'
);
SET @sql = IF(@column_exists = 0, 
    'ALTER TABLE `spot` ADD COLUMN `time_slot` VARCHAR(50) NULL COMMENT ''最佳游览时段:morning,afternoon,evening,allday'' AFTER `crowd_tags`',
    'SELECT ''Column time_slot already exists''');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 添加 entrance_fee 字段
SET @column_exists = (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
    WHERE TABLE_SCHEMA = DATABASE() 
    AND TABLE_NAME = 'spot' 
    AND COLUMN_NAME = 'entrance_fee'
);
SET @sql = IF(@column_exists = 0, 
    'ALTER TABLE `spot` ADD COLUMN `entrance_fee` DECIMAL(10,2) NULL COMMENT ''成人门票价格(元)'' AFTER `time_slot`',
    'SELECT ''Column entrance_fee already exists''');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 添加 spot_region 字段
SET @column_exists = (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
    WHERE TABLE_SCHEMA = DATABASE() 
    AND TABLE_NAME = 'spot' 
    AND COLUMN_NAME = 'spot_region'
);
SET @sql = IF(@column_exists = 0, 
    'ALTER TABLE `spot` ADD COLUMN `spot_region` VARCHAR(100) NULL COMMENT ''景点所属区域'' AFTER `entrance_fee`',
    'SELECT ''Column spot_region already exists''');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- ============================================
-- 2. 为现有景点补充人群标签和最佳时段数据
-- ============================================

-- 北京景点
UPDATE `spot` SET `crowd_tags` = 'family,elder,couple', `time_slot` = 'morning', `entrance_fee` = 60.00, `spot_region` = '东城区' WHERE `spotName` = '故宫';
UPDATE `spot` SET `crowd_tags` = 'family,elder', `time_slot` = 'morning', `entrance_fee` = 30.00, `spot_region` = '海淀区' WHERE `spotName` = '颐和园';
UPDATE `spot` SET `crowd_tags` = 'couple,solo', `time_slot` = 'morning', `entrance_fee` = 40.00, `spot_region` = '延庆区' WHERE `spotName` = '八达岭长城';
UPDATE `spot` SET `crowd_tags` = 'family,elder', `time_slot` = 'morning', `entrance_fee` = 34.00, `spot_region` = '东城区' WHERE `spotName` = '天坛公园';
UPDATE `spot` SET `crowd_tags` = 'couple,solo', `time_slot` = 'allday', `entrance_fee` = 0.00, `spot_region` = '东城区' WHERE `spotName` = '南锣鼓巷';
UPDATE `spot` SET `crowd_tags` = 'couple', `time_slot` = 'evening', `entrance_fee` = 0.00, `spot_region` = '西城区' WHERE `spotName` = '什刹海';

-- 杭州景点
UPDATE `spot` SET `crowd_tags` = 'family,elder,couple', `time_slot` = 'morning', `entrance_fee` = 0.00, `spot_region` = '西湖区' WHERE `spotName` = '西湖';
UPDATE `spot` SET `crowd_tags` = 'elder,couple', `time_slot` = 'morning', `entrance_fee` = 75.00, `spot_region` = '西湖区' WHERE `spotName` = '灵隐寺';
UPDATE `spot` SET `crowd_tags` = 'family', `time_slot` = 'afternoon', `entrance_fee` = 70.00, `spot_region` = '西湖区' WHERE `spotName` = '西溪湿地';
UPDATE `spot` SET `crowd_tags` = 'couple,solo', `time_slot` = 'evening', `entrance_fee` = 0.00, `spot_region` = '上城区' WHERE `spotName` = '河坊街';
UPDATE `spot` SET `crowd_tags` = 'family,couple', `time_slot` = 'morning', `entrance_fee` = 130.00, `spot_region` = '淳安县' WHERE `spotName` = '千岛湖';

-- 西安景点
UPDATE `spot` SET `crowd_tags` = 'family,elder', `time_slot` = 'morning', `entrance_fee` = 120.00, `spot_region` = '临潼区' WHERE `spotName` = '秦始皇兵马俑';
UPDATE `spot` SET `crowd_tags` = 'couple,solo', `time_slot` = 'allday', `entrance_fee` = 54.00, `spot_region` = '碑林区' WHERE `spotName` = '西安城墙';
UPDATE `spot` SET `crowd_tags` = 'couple,family', `time_slot` = 'evening', `entrance_fee` = 50.00, `spot_region` = '雁塔区' WHERE `spotName` = '大雁塔';
UPDATE `spot` SET `crowd_tags` = 'couple,solo', `time_slot` = 'evening', `entrance_fee` = 0.00, `spot_region` = '莲湖区' WHERE `spotName` = '回民街';
UPDATE `spot` SET `crowd_tags` = 'family,elder', `time_slot` = 'afternoon', `entrance_fee` = 120.00, `spot_region` = '临潼区' WHERE `spotName` = '华清宫';

-- 成都景点
UPDATE `spot` SET `crowd_tags` = 'family,solo', `time_slot` = 'allday', `entrance_fee` = 0.00, `spot_region` = '青羊区' WHERE `spotName` = '宽窄巷子';
UPDATE `spot` SET `crowd_tags` = 'couple,solo', `time_slot` = 'evening', `entrance_fee` = 0.00, `spot_region` = '武侯区' WHERE `spotName` = '锦里古街';
UPDATE `spot` SET `crowd_tags` = 'family', `time_slot` = 'morning', `entrance_fee` = 55.00, `spot_region` = '成华区' WHERE `spotName` = '成都大熊猫繁育研究基地';
UPDATE `spot` SET `crowd_tags` = 'couple,solo', `time_slot` = 'morning', `entrance_fee` = 60.00, `spot_region` = '都江堰市' WHERE `spotName` = '青城山';
UPDATE `spot` SET `crowd_tags` = 'family,elder', `time_slot` = 'morning', `entrance_fee` = 90.00, `spot_region` = '都江堰市' WHERE `spotName` = '都江堰';
UPDATE `spot` SET `crowd_tags` = 'couple,solo', `time_slot` = 'evening', `entrance_fee` = 0.00, `spot_region` = '锦江区' WHERE `spotName` = '春熙路';

-- 上海景点
UPDATE `spot` SET `crowd_tags` = 'couple', `time_slot` = 'evening', `entrance_fee` = 0.00, `spot_region` = '黄浦区' WHERE `spotName` = '外滩';
UPDATE `spot` SET `crowd_tags` = 'family,couple', `time_slot` = 'morning', `entrance_fee` = 199.00, `spot_region` = '浦东新区' WHERE `spotName` = '东方明珠广播电视塔';
UPDATE `spot` SET `crowd_tags` = 'family,elder', `time_slot` = 'morning', `entrance_fee` = 30.00, `spot_region` = '黄浦区' WHERE `spotName` = '豫园';
UPDATE `spot` SET `crowd_tags` = 'couple,solo', `time_slot` = 'evening', `entrance_fee` = 0.00, `spot_region` = '黄浦区' WHERE `spotName` = '南京路步行街';

-- ============================================
-- 3. 创建用户景点交互记录表
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='用户景点交互记录表（用于协同过滤）';

-- ============================================
-- 4. 创建景点相似度表
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
    KEY `idx_spot_id_b` (`spot_id_b`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='景点相似度缓存表';

-- ============================================
-- 5. 创建推荐日志表
-- ============================================
CREATE TABLE IF NOT EXISTS `recommend_log` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT COMMENT '用户ID（可为空表示未登录）',
    `city` VARCHAR(100) COMMENT '目标城市',
    `preferred_tags` VARCHAR(255) COMMENT '偏好标签(JSON)',
    `budget` DECIMAL(10,2) COMMENT '预算',
    `crowd_type` VARCHAR(20) COMMENT '人群类型',
    `result_count` INT NOT NULL DEFAULT 0 COMMENT '推荐结果数量',
    `algorithm_version` VARCHAR(20) NOT NULL DEFAULT 'v1.0' COMMENT '算法版本',
    `response_time_ms` INT COMMENT '响应耗时（毫秒）',
    `success` TINYINT NOT NULL DEFAULT 1 COMMENT '是否成功:1成功,0失败',
    `error_message` VARCHAR(500) COMMENT '错误信息',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_city` (`city`),
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
    `algorithm_type` VARCHAR(30) NOT NULL DEFAULT 'greedy' COMMENT '算法类型:greedy,genetic',
    `spot_count` INT COMMENT '规划景点数',
    `total_distance` DECIMAL(10,2) COMMENT '总距离（公里）',
    `optimization_score` DECIMAL(10,4) COMMENT '优化得分',
    `iterations` INT COMMENT '遗传算法迭代次数',
    `response_time_ms` INT COMMENT '响应耗时（毫秒）',
    `success` TINYINT NOT NULL DEFAULT 1 COMMENT '是否成功',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_city` (`city`),
    KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='路线规划日志表';

-- ============================================
-- 7. 创建 AI 调用日志表
-- ============================================
CREATE TABLE IF NOT EXISTS `ai_call_log` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT COMMENT '用户ID',
    `call_type` VARCHAR(30) NOT NULL COMMENT '调用类型:parse_intent,explain,route_gen,chat',
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
-- 用户1 (id=5) 的交互
(5, 3001001, 'view', 1.00),
(5, 3001001, 'favorite', 5.00),
(5, 3001001, 'score', 5.00),
(5, 3001003, 'view', 1.00),
(5, 3001003, 'score', 4.00),
(5, 3002001, 'view', 1.00),
(5, 3002001, 'favorite', 5.00),
(5, 3002001, 'score', 5.00),
-- 用户2 (id=6) 的交互
(6, 3001002, 'view', 1.00),
(6, 3001002, 'favorite', 5.00),
(6, 3001002, 'score', 4.00),
(6, 3004003, 'view', 1.00),
(6, 3004003, 'favorite', 5.00),
(6, 3004003, 'score', 5.00),
(6, 3004004, 'view', 1.00),
(6, 3004004, 'score', 4.00),
-- 用户3 (id=7) 的交互
(7, 3003001, 'view', 1.00),
(7, 3003001, 'favorite', 5.00),
(7, 3003001, 'score', 5.00),
(7, 3003002, 'view', 1.00),
(7, 3003002, 'score', 4.00),
(7, 3003003, 'view', 1.00),
(7, 3003003, 'favorite', 5.00);
