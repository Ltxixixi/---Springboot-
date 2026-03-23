/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50716
 Source Host           : localhost:3306
 Source Schema         : tourism

 Target Server Type    : MySQL
 Target Server Version : 50716
 File Encoding         : 65001

 Date: 28/02/2025 14:38:11
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for barrage
-- ----------------------------
DROP TABLE IF EXISTS `barrage`;
CREATE TABLE `barrage`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `message` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '弹幕文本',
  `userAvatar` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户头像',
  `userId` bigint(20) NOT NULL COMMENT '用户id',
  `isSelected` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否精选（默认0，精选为1）',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1890696292486328323 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of barrage
-- ----------------------------
INSERT INTO `barrage` VALUES (1884833990411280386, '愿你在蛇年里，事业顺利，家庭幸福，心想事成！', 'https://www.code-nav.cn/logo.png', 6, 0, '2025-01-30 13:20:16', '2025-01-30 13:20:16', 0);
INSERT INTO `barrage` VALUES (1884834015627436033, '愿蛇年为你带来无尽的财富和好运，生活如蛇般灵动顺遂！', 'https://www.code-nav.cn/logo.png', 6, 0, '2025-01-30 13:20:22', '2025-01-30 13:20:22', 0);
INSERT INTO `barrage` VALUES (1884834041141387266, '愿你在新的一年里，财运亨通，事业蒸蒸日上！', 'https://www.code-nav.cn/logo.png', 6, 0, '2025-01-30 13:20:28', '2025-01-30 13:20:28', 0);
INSERT INTO `barrage` VALUES (1884834078550384641, '愿你在新的一年里，事业有成，生活美满，步步高升！', 'https://www.code-nav.cn/logo.png', 6, 0, '2025-01-30 13:20:37', '2025-01-30 13:20:37', 0);
INSERT INTO `barrage` VALUES (1884834107914706945, '愿幸福如蛇般紧紧缠绕你，生活充满甜蜜与温馨！', 'https://www.code-nav.cn/logo.png', 6, 0, '2025-01-30 13:20:44', '2025-01-30 13:20:44', 0);
INSERT INTO `barrage` VALUES (1884834137237086210, '愿你在新的一年里，生活充满灵动与美好，幸福常伴左右！', 'https://www.code-nav.cn/logo.png', 6, 0, '2025-01-30 13:20:51', '2025-01-30 13:20:51', 0);
INSERT INTO `barrage` VALUES (1890696292486328322, '愿你在新的一年里，生活充满灵动与美好，', 'https://www.code-nav.cn/logo.png', 6, 0, '2025-02-15 17:34:58', '2025-02-15 17:34:58', 0);

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '评论 ID',
  `postId` bigint(20) NOT NULL COMMENT '面经帖子 ID',
  `userId` bigint(20) NOT NULL COMMENT '用户 ID',
  `content` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '评论内容',
  `parentId` bigint(20) NULL DEFAULT NULL COMMENT '父评论 ID，支持多级嵌套回复',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否删除',
  `ancestorId` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `comment_questionId`(`postId`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1890696721316163587 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of comment
-- ----------------------------
INSERT INTO `comment` VALUES (1887035269837451265, 1885946066089156610, 6, '你好', NULL, '2025-02-05 15:07:22', '2025-02-05 17:03:22', 1, NULL);
INSERT INTO `comment` VALUES (1887035498150195202, 1885946066089156610, 6, '我是程序员小白条', NULL, '2025-02-05 15:08:16', '2025-02-05 15:33:10', 0, NULL);
INSERT INTO `comment` VALUES (1887039525038551041, 1885946066089156610, 6, '我是', NULL, '2025-02-05 15:24:16', '2025-02-05 15:33:12', 0, NULL);
INSERT INTO `comment` VALUES (1887049977609023490, 1885946066089156610, 6, '的', 1887039525038551041, '2025-02-05 16:05:49', '2025-02-05 16:05:49', 0, 1887039525038551041);
INSERT INTO `comment` VALUES (1887050056956866561, 1885946066089156610, 6, '你好', 1887039525038551041, '2025-02-05 16:06:07', '2025-02-05 16:06:07', 0, 1887039525038551041);
INSERT INTO `comment` VALUES (1887050896794939394, 1885946066089156610, 6, '狗屎', 1887039525038551041, '2025-02-05 16:09:28', '2025-02-05 16:09:28', 0, 1887039525038551041);
INSERT INTO `comment` VALUES (1887052235272843265, 1885946066089156610, 6, '23', 1887039525038551041, '2025-02-05 16:14:47', '2025-02-05 16:14:47', 0, 1887039525038551041);
INSERT INTO `comment` VALUES (1887063382281646081, 1885946066089156610, 6, '阿斯加德按季度就阿斯加德按季度就阿斯加德按季度就阿斯加德按季度就阿斯加德按季度就阿斯加德按季度就阿斯加德按季度就阿斯加德按季度就阿斯加德按季度就阿斯加德按季度就阿斯加德按季度就阿斯加德按季度就阿斯加德按季度就阿斯加德按季度就阿斯加德按季度就阿斯加德按季度就阿斯加德按季度就阿斯加德按季度就阿斯加德按季度就阿斯加德按季度就阿斯加德按季度就', NULL, '2025-02-05 16:59:04', '2025-02-05 16:59:04', 0, NULL);
INSERT INTO `comment` VALUES (1887064425367302146, 1885946066089156610, 6, '我是测试', 1887035269837451265, '2025-02-05 17:03:13', '2025-02-05 17:03:13', 0, 1887035269837451265);
INSERT INTO `comment` VALUES (1887064859033169921, 1885946066089156610, 6, ' 是我是', NULL, '2025-02-05 17:04:57', '2025-02-05 17:04:57', 0, NULL);
INSERT INTO `comment` VALUES (1887064863223279617, 1885946066089156610, 6, '我是', NULL, '2025-02-05 17:04:58', '2025-02-05 17:05:19', 1, NULL);
INSERT INTO `comment` VALUES (1887064866536779777, 1885946066089156610, 6, '我是', NULL, '2025-02-05 17:04:58', '2025-02-05 17:05:20', 1, NULL);
INSERT INTO `comment` VALUES (1887064875789414402, 1885946066089156610, 6, '我', NULL, '2025-02-05 17:05:01', '2025-02-05 17:05:17', 1, NULL);
INSERT INTO `comment` VALUES (1887064879367155714, 1885946066089156610, 6, '我', NULL, '2025-02-05 17:05:01', '2025-02-05 17:05:18', 1, NULL);
INSERT INTO `comment` VALUES (1887064882496106498, 1885946066089156610, 6, '我', NULL, '2025-02-05 17:05:02', '2025-02-05 17:05:15', 1, NULL);
INSERT INTO `comment` VALUES (1887066944608890881, 1885946066089156610, 1, '你好，小白条', 1887064859033169921, '2025-02-05 17:13:14', '2025-02-05 17:13:14', 0, 1887064859033169921);
INSERT INTO `comment` VALUES (1887068408165134337, 1885946066089156610, 6, '32', 1887064859033169921, '2025-02-05 17:19:03', '2025-02-05 17:19:03', 0, 1887064859033169921);
INSERT INTO `comment` VALUES (1887070915905941506, 1885946066089156610, 1, '我是测试用户1', NULL, '2025-02-05 17:29:01', '2025-02-05 17:29:01', 0, NULL);
INSERT INTO `comment` VALUES (1890696721316163586, 1885946066089156610, 6, '测试', NULL, '2025-02-15 17:36:40', '2025-02-15 17:37:04', 1, NULL);

-- ----------------------------
-- Table structure for notice
-- ----------------------------
DROP TABLE IF EXISTS `notice`;
CREATE TABLE `notice`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `noticeTitle` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '公告标题',
  `noticeContent` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '公告内容',
  `noticeAdminId` bigint(20) NOT NULL COMMENT '创建人id（管理员）',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of notice
-- ----------------------------
INSERT INTO `notice` VALUES (1, '23232322323', '233232323', 5, '2025-01-28 18:35:43', '2025-02-15 17:43:13', 0);
INSERT INTO `notice` VALUES (2, '测试内容23', '测试内容2323', 5, '2025-01-28 18:35:52', '2025-01-28 18:35:52', 0);
INSERT INTO `notice` VALUES (3, '23', '232', 5, '2025-02-15 17:43:07', '2025-02-15 17:43:16', 1);

-- ----------------------------
-- Table structure for post
-- ----------------------------
DROP TABLE IF EXISTS `post`;
CREATE TABLE `post`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `title` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '内容',
  `tags` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '标签列表（json 数组）',
  `thumbNum` int(11) NOT NULL DEFAULT 0 COMMENT '点赞数',
  `favourNum` int(11) NOT NULL DEFAULT 0 COMMENT '收藏数',
  `userId` bigint(20) NOT NULL COMMENT '创建用户 id',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_userId`(`userId`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1890699195636756483 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '帖子' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of post
-- ----------------------------
INSERT INTO `post` VALUES (1885946066089156610, '个啊', '测试赛23测试赛23测试赛23测试赛23测试赛23测试赛23测试赛23测试赛23测试赛23测试赛23测试赛23测试赛23测试赛23123', '[\"Python\",\"Java\",\"Go\"]', 1, 1, 6, '2025-02-02 14:59:16', '2025-02-15 17:46:37', 0);
INSERT INTO `post` VALUES (1885961164446900226, '测试内容1', '2323', '[\"Python\",\"Java\"]', 0, 1, 6, '2025-02-02 15:59:15', '2025-02-15 16:55:13', 0);
INSERT INTO `post` VALUES (1885965652691918849, '测', '啊大阿达', '[\"Go\"]', 0, 1, 5, '2025-02-02 16:17:05', '2025-02-06 15:32:47', 0);
INSERT INTO `post` VALUES (1885965697017323521, '23', '123213', '[\"213213\"]', 0, 1, 5, '2025-02-02 16:17:16', '2025-02-06 15:32:49', 0);
INSERT INTO `post` VALUES (1885965717217091586, '21312', '32132132132', '[\"312312312321\"]', 0, 0, 5, '2025-02-02 16:17:21', '2025-02-02 16:37:45', 1);
INSERT INTO `post` VALUES (1885965741871210498, '测定啊', '瓦瓦', '[\"啊问饿啊\"]', 0, 1, 5, '2025-02-02 16:17:27', '2025-02-06 15:32:52', 0);
INSERT INTO `post` VALUES (1887404118676049921, '你好', '23 ', '[\"测试\"]', 0, 1, 6, '2025-02-06 15:33:02', '2025-02-15 17:36:10', 0);
INSERT INTO `post` VALUES (1890696577141157890, '测试内容', '## 1213\n``13``', '[\"测试内容\"]', 0, 0, 6, '2025-02-15 17:36:06', '2025-02-15 17:36:06', 0);
INSERT INTO `post` VALUES (1890699195636756482, '231', '213213', '[\"213\"]', 0, 0, 5, '2025-02-15 17:46:30', '2025-02-15 17:46:34', 1);

-- ----------------------------
-- Table structure for post_favour
-- ----------------------------
DROP TABLE IF EXISTS `post_favour`;
CREATE TABLE `post_favour`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `postId` bigint(20) NOT NULL COMMENT '帖子 id',
  `userId` bigint(20) NOT NULL COMMENT '创建用户 id',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_postId`(`postId`) USING BTREE,
  INDEX `idx_userId`(`userId`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 184 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '帖子收藏' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of post_favour
-- ----------------------------
INSERT INTO `post_favour` VALUES (166, 1885965652691918849, 6, '2025-02-06 15:32:47', '2025-02-06 15:32:47');
INSERT INTO `post_favour` VALUES (168, 1885965697017323521, 6, '2025-02-06 15:32:49', '2025-02-06 15:32:49');
INSERT INTO `post_favour` VALUES (170, 1885965741871210498, 6, '2025-02-06 15:32:52', '2025-02-06 15:32:52');
INSERT INTO `post_favour` VALUES (179, 1885961164446900226, 6, '2025-02-15 16:55:13', '2025-02-15 16:55:13');
INSERT INTO `post_favour` VALUES (180, 1887404118676049921, 6, '2025-02-15 17:36:10', '2025-02-15 17:36:10');
INSERT INTO `post_favour` VALUES (183, 1885946066089156610, 6, '2025-02-15 17:42:06', '2025-02-15 17:42:06');

-- ----------------------------
-- Table structure for post_thumb
-- ----------------------------
DROP TABLE IF EXISTS `post_thumb`;
CREATE TABLE `post_thumb`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `postId` bigint(20) NOT NULL COMMENT '帖子 id',
  `userId` bigint(20) NOT NULL COMMENT '创建用户 id',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_postId`(`postId`) USING BTREE,
  INDEX `idx_userId`(`userId`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 178 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '帖子点赞' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of post_thumb
-- ----------------------------
INSERT INTO `post_thumb` VALUES (177, 1885946066089156610, 6, '2025-02-15 17:42:06', '2025-02-15 17:42:06');

-- ----------------------------
-- Table structure for spot
-- ----------------------------
DROP TABLE IF EXISTS `spot`;
CREATE TABLE `spot`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `adminId` bigint(20) NOT NULL COMMENT '管理员 ID（某人创建该景点）',
  `spotName` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '景点名',
  `spotAvatar` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '景点封面图',
  `spotLocation` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '景点所在地',
  `spotDescription` varchar(4096) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '景点介绍',
  `spotTags` varchar(2048) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '景点标签（JSON字符串数组）',
  `favourNum` int(11) NOT NULL DEFAULT 0 COMMENT '收藏量',
  `viewNum` int(11) NOT NULL DEFAULT 0 COMMENT '浏览量',
  `spotStatus` tinyint(4) NOT NULL DEFAULT 0 COMMENT '景点状态（1开放，0关闭，默认关闭）',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1893908474128637955 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of spot
-- ----------------------------
INSERT INTO `spot` VALUES (1892862072971821057, 5, '故宫', 'https://www.code-nav.cn/logo.png', '北京', NULL, NULL, 0, 0, 0, '2025-02-21 17:01:00', '2025-02-21 17:04:15', 1);
INSERT INTO `spot` VALUES (1892862826109427714, 5, '故宫', 'https://pic.yupi.icu/5563/202502251310084.webp', '39.9169° N, 116.3907° E', '故宫位于北京市中心，是中国明清两代的皇宫，也是世界上最大、最完整的古代宫殿建筑群。始建于公元1420年，占地约72万平方米，拥有近1000间建筑，分为外朝和内廷两部分。故宫不仅是中国封建社会的政治中心，也是皇帝的居住地。其建筑风格融合了中国传统的宫廷建筑艺术，精雕细琢，气势恢宏。宫内有大量珍贵文物，包括书画、陶瓷、金器、玉器等，展示了中国古代辉煌的文化成就。如今的故宫是国家博物馆，吸引着成千上万的游客，是中国历史与文化的象征。', '[\"历史\",\"文化\",\"北京\"]', 0, 57, 1, '2025-02-21 17:04:00', '2025-02-28 13:51:19', 0);
INSERT INTO `spot` VALUES (1893905556574969858, 5, '颐和园', 'https://www.code-nav.cn/logo.png', '浙江', '颐和园位于北京市西北郊，是中国古代皇家园林的代表之一。它始建于清朝乾隆年间，最初作为皇家园林，后来成为清朝的避暑胜地。颐和园的主要景点包括昆明湖、万寿山、长廊、佛香阁等，融合了山水景观和古建筑，体现了中国园林艺术的精髓。昆明湖的水面宽广，周围绿树成荫，是人们游船、散步的好地方。万寿山上有众多寺庙建筑，如佛香阁是园内的最高建筑，俯瞰整个园区。颐和园不只是一个皇家园林，也是文化遗产，展示了中国古代园林建筑和自然景观的和谐美。', '[\"清代\"]', 0, 0, 0, '2025-02-24 14:07:26', '2025-02-25 14:01:29', 1);
INSERT INTO `spot` VALUES (1893907615818850305, 5, '长城', 'https://pic.yupi.icu/5563/202502251312570.webp', '40.4319° N, 116.5704° E', '长城是中国古代的军事防御工程，也是世界文化遗产之一。它起源于公元前7世纪的春秋战国时期，经过历代不断修建，最终形成了横跨中国北部的庞大体系。长城全长约2万公里，穿越了北京、河北、山西、陕西、甘肃等多个省份。最著名的部分是位于北京附近的八达岭长城和慕田峪长城。长城的建筑结构巧妙，既能有效防御外敌的入侵，又具备一定的观赏价值。它不仅是中国历史的见证，更象征着中国人民的智慧和坚韧精神，是世界上最雄伟的建筑之一。', '[\"世界遗产\",\"北京\",\"历史\"]', 0, 5, 0, '2025-02-24 14:15:37', '2025-02-26 13:39:50', 0);
INSERT INTO `spot` VALUES (1893908028072796161, 5, '西湖', 'https://pic.yupi.icu/5563/202502251312572.webp', '30.2500° N, 120.1675° E', '西湖位于浙江省杭州市，是中国最著名的淡水湖之一，以其山水景色、历史文化和人文遗迹而著称。西湖以三潭印月、雷峰塔、苏堤春晓等景点闻名，每一处景点都与中国古代的诗词、画作、传说紧密相连。湖面如镜，四季变幻，春天的柳垂绿意盎然，夏天的荷花盛开，秋天的芦苇荡漾，冬天的雪景更是别具一格。西湖的美景吸引了无数文人墨客留下诗篇，成为中国传统文化的象征之一。它不仅是自然景观的宝库，也是深厚文化的体现。', '[\"自然\",\"湖泊\",\"杭州\"]', 0, 1, 0, '2025-02-24 14:17:15', '2025-02-25 14:15:05', 0);
INSERT INTO `spot` VALUES (1893908268003762178, 5, '黄山', 'https://pic.yupi.icu/5563/202502251312573.webp', '30.1670° N, 118.0500° E', '黄山位于安徽省，是中国最著名的山脉之一，因奇松、怪石、云海和温泉而闻名。黄山以“奇松、怪石、云海、温泉”四大奇观闻名于世，其中最具代表性的是黄山的云海。每当云雾弥漫，山峰若隐若现，宛如仙境。黄山的奇松生长在险峻的岩石上，形态各异，极具观赏性。黄山的石头也因其奇特的造型吸引了大量游客和摄影爱好者。此外，黄山还是许多古代文人流连忘返的地方，许多古诗、画作都以黄山为题材，是中国山水画的发源地之一。', '[\"自然\",\"山脉\",\"安徽\"]', 0, 1, 0, '2025-02-24 14:18:12', '2025-02-25 14:15:01', 0);
INSERT INTO `spot` VALUES (1893908368084049922, 5, '九寨沟', 'https://pic.yupi.icu/5563/202502251312571.webp', '33.2000° N, 103.9000° E', '九寨沟位于四川省阿坝藏族羌族自治州，是中国最著名的自然景区之一，拥有丰富的自然景观和生物多样性。九寨沟的名字源自沟内的九个藏族村寨，山水之间，景色如画。九寨沟最为人称道的是其五彩斑斓的湖泊和瀑布，湖水呈现出不同的颜色，从湛蓝到翠绿，再到透明清澈，令人陶醉。除了湖泊，九寨沟的森林、雪山、瀑布、温泉等自然景观都让人叹为观止。这里是摄影爱好者的天堂，也是徒步旅行者理想的目的地。九寨沟被联合国教科文组织列为世界自然遗产，是中国最重要的生态保护区之一。', '[\"自然\",\"湖泊\",\"四川\"]', 0, 1, 0, '2025-02-24 14:18:36', '2025-02-25 14:14:58', 0);
INSERT INTO `spot` VALUES (1893908474128637954, 5, '布达拉宫', 'https://pic.yupi.icu/5563/202502251312574.webp', '29.6578° N, 91.1169° E', '布达拉宫位于西藏拉萨，是藏传佛教的圣地，也是世界上最高的宫殿之一。它坐落在海拔3700米的玛布日山上，建于公元7世纪，由唐朝文成公主和吐蕃王松赞干布共同修建。布达拉宫由红宫和白宫两部分组成，红宫主要是宗教活动场所，白宫则是历代达赖喇嘛的住所。宫内建筑精美，藏传佛教文化气息浓厚，拥有丰富的历史遗迹、壁画和文物。布达拉宫不仅是西藏文化的象征，也是藏族人民的精神家园。作为世界文化遗产，它吸引着成千上万的朝圣者和游客前来参观，体验其独特的历史和宗教魅力。', '[\"历史\",\"文化\",\"西藏\"]', 0, 1, 0, '2025-02-24 14:19:02', '2025-02-25 14:15:02', 0);

-- ----------------------------
-- Table structure for spot_fee
-- ----------------------------
DROP TABLE IF EXISTS `spot_fee`;
CREATE TABLE `spot_fee`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `spotId` bigint(20) NOT NULL COMMENT '景点id',
  `adminId` bigint(20) NOT NULL COMMENT '管理员ID',
  `spotFeeDescription` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '门票描述',
  `spotFeePrice` decimal(10, 2) NOT NULL COMMENT '门票价格',
  `spotFeeNumber` int(11) NULL DEFAULT 0 COMMENT '景点门票数量',
  `spotFeeStatus` tinyint(4) NOT NULL DEFAULT 0 COMMENT '门票可用状态 1可用 0不可用 默认 0',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `spotIdIndex`(`spotId`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1894999998342070275 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of spot_fee
-- ----------------------------
INSERT INTO `spot_fee` VALUES (1894650334900097025, 1892862826109427714, 5, '数量800张 9折231', 59.90, 79932, 0, '2025-02-26 15:26:55', '2025-02-27 14:35:29', 0);
INSERT INTO `spot_fee` VALUES (1894990853429784577, 1892862826109427714, 5, '数量 800 张 8折', 39.90, 798, 1, '2025-02-27 14:00:01', '2025-02-27 14:00:01', 0);
INSERT INTO `spot_fee` VALUES (1894991418918432770, 1893907615818850305, 5, '数量 900 张 7折', 60.00, 900, 1, '2025-02-27 14:02:16', '2025-02-27 14:03:04', 0);
INSERT INTO `spot_fee` VALUES (1894999998342070274, 1893908028072796161, 5, '23', 59.90, 23, 1, '2025-02-27 14:36:21', '2025-02-27 14:36:49', 1);

-- ----------------------------
-- Table structure for spot_order
-- ----------------------------
DROP TABLE IF EXISTS `spot_order`;
CREATE TABLE `spot_order`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `userId` bigint(20) NOT NULL COMMENT '用户 id',
  `spotFeeId` bigint(20) NOT NULL COMMENT '门票id',
  `userName` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `userPhone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系电话',
  `paymentAmount` decimal(10, 2) NULL DEFAULT NULL COMMENT '支付金额',
  `payStatus` tinyint(4) NOT NULL DEFAULT 0 COMMENT '支付状态（1已支付，0未支付）默认0',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `payId`(`userId`, `spotFeeId`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1895362211523321858 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of spot_order
-- ----------------------------
INSERT INTO `spot_order` VALUES (1894999860227833857, 6, 1894650334900097025, '程序员小白条2', '13898765432', 59.90, 0, '2025-02-27 14:35:48', '2025-02-27 15:06:30', 0);
INSERT INTO `spot_order` VALUES (1895008011639554049, 5, 1894990853429784577, '程序员小白', '13812345678', 39.90, 1, '2025-02-27 15:08:12', '2025-02-27 15:18:56', 0);
INSERT INTO `spot_order` VALUES (1895362211523321857, 6, 1894990853429784577, '程序员小白条1', '13812345678', 79.80, 1, '2025-02-28 14:35:40', '2025-02-28 14:35:40', 0);

-- ----------------------------
-- Table structure for spot_route
-- ----------------------------
DROP TABLE IF EXISTS `spot_route`;
CREATE TABLE `spot_route`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `spotIds` varchar(2048) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '景点 id（JSON字符串,顺序从前往后)',
  `adminId` bigint(20) NOT NULL COMMENT '管理员 ID',
  `spotRouteAvatar` varchar(2048) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '路线封面图',
  `spotRouteDescription` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '路线描述',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1894241082674937858 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of spot_route
-- ----------------------------
INSERT INTO `spot_route` VALUES (1893988822237827074, '[\"1892862826109427714\",\"1893907615818850305\"]', 5, 'https://www.code-nav.cn/logo.png', '故宫-颐和园', '2025-02-24 19:38:18', '2025-02-25 12:20:10', 0);
INSERT INTO `spot_route` VALUES (1894241082674937857, '[\"1893907615818850305\",\"1893908028072796161\"]', 5, 'https://www.code-nav.cn/logo.png', '长城-西湖', '2025-02-25 12:20:42', '2025-02-25 12:20:42', 0);

-- ----------------------------
-- Table structure for spot_score
-- ----------------------------
DROP TABLE IF EXISTS `spot_score`;
CREATE TABLE `spot_score`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `spotId` bigint(20) NOT NULL COMMENT '景点 id',
  `userId` bigint(20) NOT NULL COMMENT '用户 id',
  `score` int(10) NOT NULL COMMENT '评分（满分5）',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `scoreId`(`spotId`, `userId`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1894632636900048899 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of spot_score
-- ----------------------------
INSERT INTO `spot_score` VALUES (1894629439041130498, 1892862826109427714, 6, 3, '2025-02-26 14:03:53', '2025-02-26 14:04:26', 0);
INSERT INTO `spot_score` VALUES (1894631963966025729, 1892862826109427714, 5, 5, '2025-02-26 14:13:55', '2025-02-26 14:14:03', 0);
INSERT INTO `spot_score` VALUES (1894632636900048898, 1892862826109427714, 1888851998200721410, 2, '2025-02-26 14:16:35', '2025-02-26 14:16:35', 0);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `userAccount` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '账号',
  `userPassword` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
  `unionId` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '微信开放平台id',
  `mpOpenId` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '公众号openId',
  `userName` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户昵称',
  `userAvatar` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户头像',
  `userProfile` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户简介',
  `userRole` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'user' COMMENT '用户角色：user/admin/ban',
  `userPhone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '联系电话',
  `aiRemainNumber` int(11) NULL DEFAULT 0 COMMENT '用户 AI 剩余可使用次数',
  `balance` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '用户余额（仅AI接口调用）',
  `editTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '编辑时间',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_unionId`(`unionId`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1890695308817182723 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'user1', '01059c753223cd004dd34f0e5e140ba2', 'unionId1', 'mpOpenId1', 'user1', 'https://www.code-nav.cn/logo.png', '喜欢编程的小白', 'user', NULL, 0, 0.00, '2025-01-28 18:31:53', '2025-01-28 18:31:53', '2025-02-08 15:41:28', 0);
INSERT INTO `user` VALUES (2, 'user2', '01059c753223cd004dd34f0e5e140ba2', 'unionId2', 'mpOpenId2', 'user2', 'https://www.code-nav.cn/logo.png', '全栈开发工程师', 'user', NULL, 0, 0.00, '2025-01-28 18:31:53', '2025-01-28 18:31:53', '2025-01-28 18:31:53', 0);
INSERT INTO `user` VALUES (3, 'user3', '01059c753223cd004dd34f0e5e140ba2', 'unionId3', 'mpOpenId3', 'user3', 'https://www.code-nav.cn/logo.png', '前端爱好者', 'user', NULL, 0, 0.00, '2025-01-28 18:31:53', '2025-01-28 18:31:53', '2025-01-28 18:31:53', 0);
INSERT INTO `user` VALUES (4, 'user4', '01059c753223cd004dd34f0e5e140ba2', 'unionId4', 'mpOpenId4', 'user4', 'https://www.code-nav.cn/logo.png', '后端开发工程师', 'user', NULL, 0, 0.00, '2025-01-28 18:31:53', '2025-01-28 18:31:53', '2025-01-28 18:31:53', 0);
INSERT INTO `user` VALUES (5, 'xiaobai', '01059c753223cd004dd34f0e5e140ba2', NULL, NULL, '程序员小白', 'https://www.code-nav.cn/logo.png', '系统管理员', 'admin', '13812345678', 0, 0.00, '2025-01-28 18:31:53', '2025-01-28 18:31:53', '2025-02-27 15:07:53', 0);
INSERT INTO `user` VALUES (6, 'xiaobaitiao', '01059c753223cd004dd34f0e5e140ba2', NULL, NULL, '程序员小白条1', 'https://img1.baidu.com/it/u=3598104138,3632108415&fm=253&fmt=auto&app=120&f=JPEG?w=800&h=800', 'Java开发工程师2', 'user', '13812345678', 0, 920.20, '2025-01-28 18:31:53', '2025-01-28 18:31:53', '2025-02-28 14:34:31', 0);
INSERT INTO `user` VALUES (1888851998200721410, 'xiaobaitiao123', '01059c753223cd004dd34f0e5e140ba2', NULL, NULL, '小白条的小助手', 'https://www.code-nav.cn/logo.png', '我是小助手', 'user', '13812345678', 0, 0.00, '2025-02-10 15:26:24', '2025-02-10 15:26:24', '2025-02-21 16:25:52', 0);
INSERT INTO `user` VALUES (1890695308817182722, 'xiaobaitiao1234', '01059c753223cd004dd34f0e5e140ba2', NULL, NULL, NULL, NULL, NULL, 'user', NULL, 0, 0.00, '2025-02-15 17:31:03', '2025-02-15 17:31:03', '2025-02-15 17:42:52', 1);

-- ----------------------------
-- Table structure for user_ai_message
-- ----------------------------
DROP TABLE IF EXISTS `user_ai_message`;
CREATE TABLE `user_ai_message`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `userInputText` varchar(4096) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户输入',
  `aiGenerateText` varchar(4096) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'AI 生成结果',
  `userId` bigint(20) NOT NULL COMMENT '用户ID',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint(4) NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1891408151659982851 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_ai_message
-- ----------------------------
INSERT INTO `user_ai_message` VALUES (1887795958008999937, 'Java 怎么学习', '2323133', 5, '2025-02-07 17:30:04', '2025-02-08 15:58:48', 0);
INSERT INTO `user_ai_message` VALUES (1887806705665454082, 'Java 怎么学习，简短介绍即可', '', 6, '2025-02-07 18:12:47', '2025-02-07 18:12:47', 0);
INSERT INTO `user_ai_message` VALUES (1887806865468436481, 'Java 怎么学习，简短介绍即可', '', 6, '2025-02-07 18:13:25', '2025-02-07 18:13:25', 0);
INSERT INTO `user_ai_message` VALUES (1887808007292317697, 'Java 怎么学习，简短介绍即可', '学习', 6, '2025-02-07 18:17:57', '2025-02-07 18:17:57', 0);
INSERT INTO `user_ai_message` VALUES (1887808105250287618, 'Java 怎么学习', '学习', 6, '2025-02-07 18:18:20', '2025-02-07 18:18:20', 0);
INSERT INTO `user_ai_message` VALUES (1888114368764936193, 'Java 怎么学习', '学习Java是一个循序渐进的过程，需要掌握基础知识、进行实践练习、深入学习高级特性，并参与项目开发以积累经验。以下是一些建议：\n\n### 一、扎实基础\n\n1. **选择优质学习资源**：可以选择权威的教材，如《Java核心技术卷I》等；也可以观看在线课程，像尚硅谷宋红康老师的Java零基础入门教程，讲解细致，适合初学者。\n\n2. **掌握基本语法**：包括数据类型、变量、运算符、控制流语句（如if-else、for循环、while循环等）、数组和字符串等。\n\n3. **理解面向对象编程**：这是Java的核心概念，需深入理解类和对象的关系，掌握封装、继承和多态三大特性，以及接口、抽象类等相关知识。\n\n4. **熟悉常用类库**：如集合框架（List、Set、Map等）、输入输出流（用于文件操作和网络通信）、日期时间处理类等。\n\n### 二、实践练习\n\n1. **完成基础练习题**：巩固所学的语法知识，加深对知识点的理解和记忆。\n\n2. **进行小项目实践**：从简单的项目入手，如图书管理系统、学生成绩管理系统等，将所学知识综合运用，提高解决实际问题的能力。\n\n3. **参与开源项目**：在技术平台上搜索感兴趣的开源项目参与其中，学习他人的优秀代码和设计思路，提升自己的编程水平。\n\n### 三、深入学习\n\n1. **学习数据库知识**：掌握至少一种关系型数据库，如MySQL，了解数据库的设计、创建表、增删改查操作，以及如何使用JDBC与Java程序进行交互。\n\n2. **研究Java高级特性**：如泛型、反射、注解、动态代理、并发编程等，这些特性在大型项目中经常使用，能够提升程序的性能和可扩展性。\n\n3. **学习框架**：如Spring、Spring MVC、MyBatis等，这些框架可以帮助开发者快速构建企业级应用，提高开发效率。\n\n### 四、持续提升\n\n1. **阅读源码**：阅读优秀的Java源码，如开源框架的源代码，学习其设计模式、代码结构和编程技巧，提升自己的编程能力。\n\n2. **关注行业动态**：关注Java的最新发展动态和技术趋势，学习新的技术和工具，不断更新自己的知识体系。\n\n3. **参加技术交流活动**：加入Java技术社区，参加线上线下的技术交流活动、研讨会等，与其他开发者交流经验，分享心得，拓宽视野。\n\n总之，通过以上步骤和方法，你可以系统地学习Java编程，逐步提升自己的编程能力。记住，持续学习和实践是成为优秀Java开发者的关键。', 6, '2025-02-08 14:35:19', '2025-02-08 14:35:19', 0);
INSERT INTO `user_ai_message` VALUES (1888119440571863041, '力扣的两数之和该怎么做？给我 Java 代码', '力扣（LeetCode）上的“两数之和”问题要求你在一个整数数组中找到两个数，使它们的和等于一个特定的目标值，并返回这两个数的索引。\n\n下面是一个用 Java 实现的解决方案：\n\n```java\nimport java.util.HashMap;\nimport java.util.Map;\n\npublic class TwoSum {\n    public int[] twoSum(int[] nums, int target) {\n        // 创建一个哈希表来存储数组中的数值及其对应的索引\n        Map<Integer, Integer> map = new HashMap<>();\n        \n        // 遍历数组\n        for (int i = 0; i < nums.length; i++) {\n            // 计算当前数值与目标值之间的差值\n            int complement = target - nums[i];\n            \n            // 如果哈希表中存在这个差值，说明找到了两个数\n            if (map.containsKey(complement)) {\n                return new int[] { map.get(complement), i };\n            }\n            \n            // 将当前数值及其索引存入哈希表\n            map.put(nums[i], i);\n        }\n        \n        // 如果没有找到符合条件的两个数，抛出异常\n        throw new IllegalArgumentException(\"No two sum solution\");\n    }\n\n    public static void main(String[] args) {\n        TwoSum solution = new TwoSum();\n        int[] nums = {2, 7, 11, 15};\n        int target = 9;\n        int[] result = solution.twoSum(nums, target);\n        System.out.println(\"Indices: \" + result[0] + \", \" + result[1]);\n    }\n}\n```\n\n### 代码解释：\n1. **导入必要的包**：`import java.util.HashMap; import java.util.Map;`\n   - `HashMap` 用于存储数组中的数值及其对应的索引。\n   - `Map` 是 `HashMap` 的接口。\n\n2. **定义类和方法**：`public class TwoSum` 和 `public int[] twoSum(int[] nums, int target)`\n   - `TwoSum` 类包含解决问题的方法。\n   - `twoSum` 方法接受一个整数数组 `nums` 和一个目标值 `target`，返回两个整数的索引。\n\n3. **创建哈希表**：`Map<Integer, Integer> map = new HashMap<>();`\n   - 用于存储数组中的数值及其对应的索引。\n\n4. **遍历数组**：`for (int i = 0; i < nums.length; i++)`\n   - 使用循环遍历数组中的每一个元素。\n\n5. **计算差值**：`int complement = target - nums[i];`\n   - 计算当前数值与目标值之间的差值。\n\n6. **检查哈希表**：`if (map.containsKey(complement))`\n   - 如果哈希表中存在这个差值，说明找到了两个数，返回它们的索引。\n\n7. **更新哈希表**：`map.put(nums[i], i);`\n   - 将当前数值及其索引存入哈希表。\n\n8. **处理未找到的情况**：`throw new IllegalArgumentException(\"No two sum solution\");`\n   - 如果遍历完数组后没有找到符合条件的两个数，抛出异常。\n\n9. **主方法测试**：`public static void main(String[] args)`\n   - 创建一个 `TwoSum` 对象并调用 `twoSum` 方法进行测试。\n\n希望这段代码能帮助你理解如何解决这个问题！', 6, '2025-02-08 14:55:28', '2025-02-08 14:55:28', 0);
INSERT INTO `user_ai_message` VALUES (1888121302951890945, '1+1=？', '$1+1$ $=2$', 6, '2025-02-08 15:02:52', '2025-02-08 15:02:52', 0);
INSERT INTO `user_ai_message` VALUES (1888122068517228546, '请简要描述线程与进程的关系,区别及优缺点？', '线程与进程是操作系统中两个重要的概念，它们之间既有联系又有区别。以下是对线程与进程的关系、区别及优缺点的简要描述：\n\n### 关系\n\n1. **包含关系**：一个进程可以包含多个线程，这些线程共享进程的资源。\n\n2. **执行环境**：线程在进程内执行任务并共享进程的上下文和资源。\n\n### 区别\n\n1. **资源占用**：进程拥有独立的内存空间和系统资源，而线程则共享其所属进程的资源。\n\n2. **执行方式**：进程在操作系统中独立执行，而线程则是在进程内部由操作系统统一调度。\n\n3. **开销**：创建和销毁进程的开销较大，因为需要分配独立的内存空间和系统资源；而线程的创建和销毁开销相对较小，因为它们共享进程的资源。\n\n4. **并发性**：进程是独立的执行单元，不受其他进程影响，安全性较高；线程共享进程的资源，因此在并发执行时需要考虑同步和互斥问题，安全性较低。\n\n### 优点\n\n1. **进程的优点**：\n   - 独立性：进程之间相互隔离，一个进程的崩溃通常不会影响其他进程的正常运行。\n   - 资源管理：进程提供了更高级别的隔离和资源管理，使得不同进程可以独立运行，互不干扰。\n\n2. **线程的优点**：\n   - 轻量级：线程是轻量级的执行单位，创建和销毁线程的开销相对较小。\n   - 并发性：通过多线程可以实现任务的并发处理，提高系统的效率和响应速度。\n   - 资源共享：线程共享进程的内存空间和资源，方便数据共享和通信。\n\n### 缺点\n\n1. **进程的缺点**：\n   - 资源占用：每个进程都有自己的独立内存空间和系统资源，因此资源占用相对较大。\n   - 切换开销：进程切换时需要保存和恢复较多的上下文信息，开销较大。\n\n2. **线程的缺点**：\n   - 同步与互斥：由于线程共享进程的资源，因此在并发执行时需要考虑同步和互斥问题，以避免数据竞争和死锁。\n   - 编程复杂性：多线程编程相对复杂，需要考虑线程间的协作和同步问题。\n   - 健壮性降低：一个线程的异常可能会导致整个进程的崩溃。\n\n综上所述，线程与进程各有其优缺点，适用于不同的应用场景。在选择使用线程还是进程时，需要根据具体需求进行权衡和选择。', 6, '2025-02-08 15:05:55', '2025-02-08 15:05:55', 0);
INSERT INTO `user_ai_message` VALUES (1888124716830134273, '给我两行诗句\n', '当然可以，以下是两行诗句：\n\n月下独酌花影碎，风中浅唱梦初回。', 6, '2025-02-08 15:16:26', '2025-02-08 15:16:26', 0);
INSERT INTO `user_ai_message` VALUES (1888124905250852866, '给我李白的三首诗歌\n', '李白的诗歌风格豪放、浪漫，充满了丰富的想象力和深刻的情感表达。以下为你推荐三首李白的著名诗歌：\n\n1. **《将进酒》**\n   - **全文**：君不见，黄河之水天上来，奔流到海不复回。君不见，高堂明镜悲白发，朝如青丝暮成雪。人生得意须尽欢，莫使金樽空对月。天生我材必有用，千金散尽还复来。烹羊宰牛且为乐，会须一饮三百杯。岑夫子，丹丘生，将进酒，杯莫停。与君歌一曲，请君为我倾耳听。钟鼓馔玉不足贵，但愿长醉不愿醒。古来圣贤皆寂寞，惟有饮者留其名。陈王昔时宴平乐，斗酒十千恣欢谑。主人何为言少钱，径须沽取对君酌。五花马，千金裘，呼儿将出换美酒，与尔同销万古愁。\n   - **赏析**：这首诗是李白怀才不遇，与友人岑勋、元丹丘相会时所作。诗中表达了诗人对时光流逝、人生短暂的感慨，同时也流露出他豪放不羁的性格和对自由生活的向往。全诗情感饱满，语言豪放，充分展现了李白的浪漫主义风格。\n\n2. **《望庐山瀑布》**\n   - **全文**：日照香炉生紫烟，遥看瀑布挂前川。飞流直下三千尺，疑是银河落九天。\n   - **赏析**：这首诗形象地描绘了庐山瀑布雄奇壮丽的景色，反映了诗人对祖国大好河山的无限热爱。诗中运用了丰富的想象力和夸张的手法，将瀑布比作银河从天而降，生动地表现了瀑布的雄伟壮观。\n\n3. **《早发白帝城》**\n   - **全文**：朝辞白帝彩云间，千里江陵一日还。两岸猿声啼不住，轻舟已过万重山。\n   - **赏析**：此诗创作于乾元二年（759）诗人流放途中遇赦返回之时。全诗给人一种锋棱挺拔、空灵飞动之感，洋溢着诗人经过艰难岁月之后突然迸发的一种激情，故雄峻迅疾中又带有豪情欢悦。\n\n总的来说，这些诗歌都展现了李白独特的艺术风格和卓越的才华，是中国古典文学中的瑰宝。', 6, '2025-02-08 15:17:11', '2025-02-08 15:17:11', 0);
INSERT INTO `user_ai_message` VALUES (1888125326132482050, '唐宋八大家分别是谁？\n', '唐宋八大家分别是**韩愈、柳宗元、欧阳修、苏洵、苏轼、苏辙、王安石、曾巩**。以下是他们的具体介绍：\n\n1. **韩愈**：字退之，河南河阳人。唐代文学家、思想家、哲学家、政治家。他是唐宋古文运动的倡导者，主张学习先秦两汉的散文语言，破骈为散，扩大文言文的表达功能。其文章众多，现存的诗文有1200余篇，其中散文近400篇，类型涵盖赋、诗、赞、书、说、传、记、序、哀辞、祭文、碑志、表、杂文等多种体裁。\n\n2. **柳宗元**：字子厚，河东（今山西运城永济一带）人。唐代文学家、哲学家、散文家和思想家，与韩愈并称为“韩柳”，共同倡导了唐代的古文运动。\n\n3. **欧阳修**：字永叔，号醉翁，晚号六一居士，吉州永丰（今江西永丰）人。北宋政治家、文学家，是宋代古文运动的领袖之一，对后世文学影响深远。\n\n4. **苏洵**：字明允，自号老泉，眉州眉山（今四川眉山）人。北宋文学家，与其子苏轼、苏辙并以文学著称于世，世称“三苏”。\n\n5. **苏轼**：字子瞻，又字和仲，号东坡居士，世称苏东坡、苏仙。汉族，北宋著名文学家、书法家、画家。他是豪放派词人的代表，与黄庭坚并称“苏黄”，与辛弃疾并称“苏辛”。\n\n6. **苏辙**：字子由，一字同叔，晚号颍滨遗老。眉州眉山（今属四川省眉山市）人。北宋文学家、宰相，与父亲苏洵、兄长苏轼齐名，合称“三苏”。\n\n7. **王安石**：字介甫，号半山，临江（今江西樟树）人。北宋著名的思想家、政治家、文学家、改革家，唐宋八大家之一。\n\n8. **曾巩**：字子固，世称“南丰先生”，建昌南丰（今属江西）人。北宋文学家、史学家、政治家，为“唐宋八大家”之一。', 6, '2025-02-08 15:18:52', '2025-02-08 15:18:52', 0);
INSERT INTO `user_ai_message` VALUES (1888131116046139393, '程序员小白条是谁？\n', '程序员小白条是一位活跃在技术分享领域的博主，以下是关于他的一些信息：\n\n1. **技术分享内容**：他会分享面试必备的八股文知识，涵盖Java基础、Java框架、Git命令、JVM、多线程、消息队列、微服务、Linux、数据库、Redis缓存、算法等众多技术领域。还会介绍一些技术概念和原理，如渐进式rehash、缓存击穿、缓存穿透、缓存雪崩、MVCC、回表、最左匹配原则、循环依赖、IOC、DI以及用户态和内核态等。\n\n2. **项目经验分享**：他曾分享过自己使用SpringBoot2+Vue2开发的智能GPT图书管理系统，包括全局异常拦截器、登录校验拦截器、Echarts展示借阅量等功能，以及可添加爬虫等扩展功能。\n\n总的来说，程序员小白条通过其丰富的技术分享和实战经验展示，为广大程序员提供了宝贵的学习资源和参考。', 6, '2025-02-08 15:41:52', '2025-02-08 15:41:52', 0);
INSERT INTO `user_ai_message` VALUES (1888131623997325314, '答案说明所有是谁？\n', '您的问题似乎缺乏具体的背景信息和上下文，无法明确指出“答案说明所有”指的是哪些人或事物。能否请您提供更多的详细内容或者情境描述？这样我才能准确地回答您的问题，比如：\n\n1. 您是在询问某个特定问题的答案，还是想了解某类问题的解答方式？\n2. “说明所有”是指解释清楚所有相关细节、涉及的所有方面，还是指提供所有可能的答案选项？\n3. 是否有特定的主题、领域或情境，比如科学、历史、文化、技术等，与您希望了解的答案相关？\n4. 是否有特定的人物、群体、机构等，他们的观点、行为或决策与您关心的答案有关？\n\n请补充相关信息，我会很乐意帮助您解答关于“答案说明所有”的具体所指。', 6, '2025-02-08 15:43:53', '2025-02-08 15:43:53', 0);
INSERT INTO `user_ai_message` VALUES (1888138562139553794, '面向对象和面向过程的区别面向过程编程（Procedural-Oriented Programming，POP）和面向对象编程（Object-Oriented Programming，OOP）是两种常见的编程范式，两者的主要区别在于解决问题的方式不同：面向过程编程（POP）：面向过程把解决问题的过程拆成一个个方法，通过一个个方法的执行解决问题。面向对象编程（OOP）：面向对象会先抽象出对象，然后用对象执行方法的方式解决问题。', '### 分析问题\n\n用户提出了一个关于面向过程编程（POP）和面向对象编程（OOP）区别的问题。这个问题是一个基础的编程范式问题，通常在面试中用于评估候选人对基本编程概念的理解。\n\n### 深度推理问题及可能的场景和解决方案\n\n1. **场景一：软件开发项目选择**\n   - **问题**：在一个大型软件开发项目中，如何决定是使用POP还是OOP？\n   - **解决方案**：\n     - 考虑项目的复杂性和可维护性。如果项目需求简单且变化不大，POP可能更合适。\n     - 如果项目需要高度的模块化、可扩展性和重用性，OOP可能是更好的选择。\n\n2. **场景二：团队技术栈统一**\n   - **问题**：如何在团队中统一技术栈，确保所有成员都能高效协作？\n   - **解决方案**：\n     - 进行技术调研，了解团队成员的技术背景和偏好。\n     - 根据项目需求和团队情况，选择适合的编程范式，并制定统一的编码规范和最佳实践。\n\n3. **场景三：性能优化**\n   - **问题**：在性能敏感的应用中，如何选择编程范式以优化性能？\n   - **解决方案**：\n     - 对于性能要求极高的应用，可能需要深入分析两种范式的性能表现。\n     - 在某些情况下，结合使用POP和OOP，或者根据性能瓶颈选择合适的范式，可能是最佳策略。\n\n### 用户回答打分及不足之处分析\n\n用户的回答简洁明了，正确地指出了POP和OOP的主要区别。然而，这个回答相对基础，没有深入探讨两种范式在不同场景下的应用和优缺点。为了更全面地展示对编程范式的理解，用户可以考虑添加以下内容：\n\n- **具体示例**：给出一个简单的代码示例，分别展示POP和OOP如何解决同一个问题。\n- **优缺点分析**：详细描述POP和OOP各自的优点和缺点，以及它们在不同类型项目中的应用。\n- **实际应用场景**：讨论在实际开发中，如何根据项目需求和团队情况选择合适的编程范式。\n\n综上所述，虽然用户的回答是正确的，但为了在面试中获得更高的评价，建议提供更详细、更具体的信息来展示对编程范式的深入理解。', 6, '2025-02-08 16:11:27', '2025-02-08 16:11:27', 0);
INSERT INTO `user_ai_message` VALUES (1890697165966913537, '有了进程为什么还需要线程?进程切换是一个开销很大的操作，线程切换的成本较低。线程更轻量，一个进程可以创建多个线程。多个线程可以并发处理不同的任务，更有效地利用了多处理器和多核计算机。而进程只能在一个时间干一件事，如果在执行过程中遇到阻塞问题比如 IO 阻塞就会挂起直到结果返回。同一进程内的线程共享内存和文件，因此它们之间相互通信无须调用内核。为什么要使用多线程?', '以下是对“有了进程为什么还需要线程”这一问题的分析、推理以及相关场景和解决方案的阐述，并包含对可能回答的打分及不足分析：\n\n### 问题分析\n- **核心关注点**：该问题主要聚焦于进程与线程存在的必要性，尤其是在已经有进程概念的情况下为何还要引入线程，重点在于探究二者的区别以及线程相较于进程的优势所在。\n\n### 深度推理及场景、解决方案\n#### 1. 资源利用角度\n - **场景**：在多核计算机系统中，如果只使用进程来执行任务，当一个进程因为I/O操作（如等待用户输入、等待文件读取完成等）而阻塞时，整个CPU核就会处于闲置状态，即使有其他可以执行的任务也无法进行，这就导致了计算资源的浪费。例如在一个服务器应用中，某个进程在等待客户端通过网络发送数据时，CPU就只能干等着，无法去处理其他可以即时处理的任务。\n - **解决方案**：引入线程后，当一个线程因为I/O阻塞时，同一进程内的其他线程可以利用CPU继续执行其他任务，使得CPU资源能够被充分利用起来，提高了系统的整体吞吐量。比如上述服务器应用中，可以在等待网络数据的进程中再创建几个线程，有的线程去处理其他已经接收到的数据，有的线程去处理一些本地计算任务等。\n\n#### 2. 并发性角度\n - **场景**：对于一些需要同时处理多个相似或不同任务的应用程序来说，如果仅靠进程来实现并发，会面临诸多限制。比如一个图形界面应用程序，既要响应用户的鼠标点击操作（这是一个独立的交互任务），又要不断更新界面显示内容（这是另一个独立的绘制任务），若只用进程，很难做到高效的同时处理这两个任务。\n - **解决方案**：使用线程可以让多个任务在同一个进程内并发执行。不同的线程可以专注于不同的功能模块，它们共享进程的资源（如内存空间等），通过合理的调度机制，能够快速地切换执行，实现真正意义上的并发处理，提高程序的响应速度和用户体验。例如上述图形界面程序中，可以创建一个专门处理鼠标事件的线程和一个专门负责界面绘制的线程，让它们并发运行。\n\n#### 3. 通信效率角度\n - **场景**：在复杂的应用程序中，不同部分之间往往需要进行数据传递和通信。如果使用多个进程来实现不同功能模块，那么进程间通信（IPC）通常需要借助操作系统提供的特定机制（如管道、消息队列等），这些机制相对复杂且开销较大。例如在一个企业级应用中，负责数据处理的进程要和负责数据存储的进程频繁交换数据，每次通信都要经过复杂的IPC流程。\n - **解决方案**：同一进程内的线程共享内存和文件等资源，它们之间的通信可以直接通过共享内存变量等方式进行，无需像进程间通信那样调用内核接口，极大地简化了通信流程，降低了通信成本，提高了通信效率。比如在上述企业级应用中，如果把数据处理和数据存储放在同一个进程的不同线程中，它们可以通过共享内存中的缓冲区来快速传递数据。\n\n### 对可能回答的打分及不足分析\n- **假设回答示例**：“有了进程还需要线程是因为线程更轻量，一个进程能创建多个线程，多个线程可以并发处理任务，比进程只能一个时间干一件事要好。”\n    - **打分**：5分（满分10分）\n    - **不足分析**：这个回答只是简单陈述了线程相比进程的部分优势，没有深入解释为什么会有这些优势以及在实际场景中如何体现这些优势。缺乏对资源利用、并发性、通信效率等具体方面的详细阐述，没有结合实际的应用案例来说明线程存在的必要性，整体回答比较笼统和表面，没有深入挖掘问题背后的原理和实际意义。\n \n\n综上所述，进程和线程在计算机系统中都有其不可或缺的地位，线程的出现是为了弥补进程在某些方面的不足，更好地满足现代复杂应用程序对资源利用、并发处理和高效通信等方面的需求。', 6, '2025-02-15 17:38:26', '2025-02-15 17:38:26', 0);
INSERT INTO `user_ai_message` VALUES (1891408151659982850, 'HTTP/1.0 和 HTTP/1.1 有什么区别？连接方式 : HTTP/1.0 为短连接，HTTP/1.1 支持长连接。HTTP 协议的长连接和短连接，实质上是 TCP 协议的长连接和短连接。', 'HTTP/1.0 和 HTTP/1.1 是两个版本的超文本传输协议（HTTP），它们在连接方式、缓存处理、管线化支持等方面存在一些关键区别。以下是对这些区别的详细分析：\n\n### 1. 连接方式\n- **HTTP/1.0**: 默认使用短连接，即每个请求都会建立一个新的TCP连接，并在请求处理完毕后立即关闭连接。这种方式在处理大量请求时效率较低，因为每次请求都需要进行三次握手和四次挥手的过程。\n- **HTTP/1.1**: 引入了持久连接（也称为长连接）的概念，允许多个请求复用同一个TCP连接。这减少了连接建立和关闭的开销，提高了网络传输的效率。\n\n### 2. 缓存处理\n- **HTTP/1.0**: 对缓存的支持较为有限，主要依赖于Expires头字段来控制缓存行为。\n- **HTTP/1.1**: 增强了对缓存的支持，引入了Cache-Control头字段，提供了更灵活的缓存控制机制，如max-age、no-cache等。\n\n### 3. 管线化支持\n- **HTTP/1.0**: 不支持管线化，即客户端必须等待服务器响应前一个请求后才能发送下一个请求。\n- **HTTP/1.1**: 支持管线化，允许客户端在一个TCP连接上发送多个请求而无需等待响应，这进一步提高了网络传输的效率。\n\n### 4. 主机头支持\n- **HTTP/1.0**: 不支持主机头（Host header），这意味着每个IP地址只能托管一个网站。\n- **HTTP/1.1**: 引入了主机头，使得同一个IP地址可以托管多个不同的网站，这是虚拟主机技术的基础。\n\n### 5. 状态码和原因短语\n- **HTTP/1.0**: 定义了一些基本的状态码和原因短语，但数量较少。\n- **HTTP/1.1**: 扩展了状态码和原因短语的数量，提供了更多的错误信息和成功响应的细分。\n\n### 6. 分块传输编码\n- **HTTP/1.0**: 不支持分块传输编码，这意味着服务器在发送响应之前需要知道整个响应体的大小。\n- **HTTP/1.1**: 引入了分块传输编码，允许服务器以流的方式发送响应体，这对于动态生成内容或大文件传输非常有用。\n\n### 7. 身份验证改进\n- **HTTP/1.0**: 身份验证方法相对简单，通常只支持基本的身份验证。\n- **HTTP/1.1**: 提供了更多种类的身份验证方案，包括摘要身份验证和Bearer token等。\n\n### 8. 安全性增强\n- **HTTP/1.0**: 安全性较低，没有内置的安全特性。\n- **HTTP/1.1**: 虽然本身并不安全，但它为后续的安全协议如HTTPS提供了基础。\n\n### 总结\nHTTP/1.1 在HTTP/1.0的基础上进行了多项改进，包括持久连接、缓存控制、管线化支持、主机头支持等，这些改进使得HTTP/1.1更加高效、灵活和安全。然而，需要注意的是，HTTP/1.1仍然是一个无状态协议，这意味着它不保留任何客户端到服务器的通信状态。为了解决这个问题，后来发展出了HTTP/2和HTTP/3等版本，它们进一步优化了性能和安全性。\n\n如果你有具体的代码或问题需要我点评打分，请提供相关详细信息，我会尽力为你进行分析和评价。', 6, '2025-02-17 16:43:38', '2025-02-17 16:43:38', 0);

-- ----------------------------
-- Table structure for user_spot_favorites
-- ----------------------------
DROP TABLE IF EXISTS `user_spot_favorites`;
CREATE TABLE `user_spot_favorites`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `userId` bigint(20) NOT NULL COMMENT '用户 ID',
  `spotId` bigint(20) NOT NULL COMMENT '景点 ID',
  `status` tinyint(4) NULL DEFAULT 1 COMMENT '1-正常收藏 0-取消收藏',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户备注',
  `isDelete` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否删除',
  `createTime` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unique_favorite`(`userId`, `spotId`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1894623385456693250 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_spot_favorites
-- ----------------------------
INSERT INTO `user_spot_favorites` VALUES (1894622168080920577, 6, 1892862826109427714, 0, NULL, 0, '2025-02-26 13:34:59', '2025-02-26 13:39:44');
INSERT INTO `user_spot_favorites` VALUES (1894623385456693249, 6, 1893907615818850305, 0, NULL, 0, '2025-02-26 13:39:50', '2025-02-26 13:39:50');

SET FOREIGN_KEY_CHECKS = 1;
