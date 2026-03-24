ALTER TABLE `spot`
  ADD COLUMN `latitude` DECIMAL(10,6) NULL COMMENT '景点纬度' AFTER `spotDescription`,
  ADD COLUMN `longitude` DECIMAL(10,6) NULL COMMENT '景点经度' AFTER `latitude`,
  ADD COLUMN `visitDurationMinutes` INT NULL DEFAULT 120 COMMENT '建议游玩时长（分钟）' AFTER `longitude`,
  ADD COLUMN `openTime` VARCHAR(16) NULL DEFAULT '09:00' COMMENT '开放时间，格式 HH:mm' AFTER `visitDurationMinutes`,
  ADD COLUMN `closeTime` VARCHAR(16) NULL DEFAULT '18:00' COMMENT '关闭时间，格式 HH:mm' AFTER `openTime`;

UPDATE `spot`
SET
  `visitDurationMinutes` = CASE
    WHEN `spotTags` LIKE '%博物馆%' THEN 180
    WHEN `spotTags` LIKE '%爬山%' OR `spotTags` LIKE '%世界自然遗产%' THEN 240
    WHEN `spotTags` LIKE '%古镇%' OR `spotTags` LIKE '%人文街区%' THEN 150
    WHEN `spotTags` LIKE '%夜游%' THEN 120
    ELSE 150
  END,
  `openTime` = CASE
    WHEN `spotTags` LIKE '%夜游%' THEN '10:00'
    ELSE '09:00'
  END,
  `closeTime` = CASE
    WHEN `spotTags` LIKE '%夜游%' THEN '22:00'
    WHEN `spotTags` LIKE '%古镇%' OR `spotTags` LIKE '%人文街区%' OR `spotTags` LIKE '%美食%' THEN '21:00'
    ELSE '18:00'
  END
WHERE `visitDurationMinutes` IS NULL
   OR `openTime` IS NULL
   OR `closeTime` IS NULL;
