/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 80045
Source Host           : localhost:3306
Source Database       : tourism

Target Server Type    : MYSQL
Target Server Version : 80045
File Encoding         : 65001

Date: 2026-03-24 17:22:22
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `barrage`
-- ----------------------------
DROP TABLE IF EXISTS `barrage`;
CREATE TABLE `barrage` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `message` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '弹幕文本',
  `userAvatar` varchar(1024) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '用户头像',
  `userId` bigint NOT NULL COMMENT '用户id',
  `isSelected` tinyint NOT NULL DEFAULT '0' COMMENT '是否精选（默认0，精选为1）',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3501019 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of barrage
-- ----------------------------
INSERT INTO `barrage` VALUES ('3501001', '北京故宫建议至少预留半天，不然会很赶。', 'https://www.code-nav.cn/logo.png', '6', '1', '2026-03-23 10:00:00', '2026-03-23 10:00:00', '0');
INSERT INTO `barrage` VALUES ('3501002', '西湖傍晚真的很美，湖边风景很治愈。', 'https://www.code-nav.cn/logo.png', '5', '1', '2026-03-23 10:01:00', '2026-03-23 10:01:00', '0');
INSERT INTO `barrage` VALUES ('3501003', '兵马俑讲解很关键，建议拼讲解团。', 'https://www.code-nav.cn/logo.png', '6', '0', '2026-03-23 10:02:00', '2026-03-23 10:02:00', '0');
INSERT INTO `barrage` VALUES ('3501004', '成都熊猫基地上午去最合适，熊猫更活跃。', 'https://www.code-nav.cn/logo.png', '5', '1', '2026-03-23 10:03:00', '2026-03-23 10:03:00', '0');
INSERT INTO `barrage` VALUES ('3501005', '长城风大，带帽子和防风外套很有必要。', 'https://www.code-nav.cn/logo.png', '6', '0', '2026-03-23 10:04:00', '2026-03-23 10:04:00', '0');
INSERT INTO `barrage` VALUES ('3501006', '河坊街的小吃集中，晚上氛围更好。', 'https://www.code-nav.cn/logo.png', '5', '0', '2026-03-23 10:05:00', '2026-03-23 10:05:00', '0');
INSERT INTO `barrage` VALUES ('3501007', '西安城墙骑行体验不错，路线平缓。', 'https://www.code-nav.cn/logo.png', '6', '0', '2026-03-23 10:06:00', '2026-03-23 10:06:00', '0');
INSERT INTO `barrage` VALUES ('3501008', '都江堰和青城山一天略紧，建议早起。', 'https://www.code-nav.cn/logo.png', '5', '0', '2026-03-23 10:07:00', '2026-03-23 10:07:00', '0');
INSERT INTO `barrage` VALUES ('3501009', '南锣鼓巷周末人多，工作日体验更好。', 'https://www.code-nav.cn/logo.png', '6', '0', '2026-03-23 10:08:00', '2026-03-23 10:08:00', '0');
INSERT INTO `barrage` VALUES ('3501010', '灵隐寺建议提前线上购票，排队会少一些。', 'https://www.code-nav.cn/logo.png', '5', '0', '2026-03-23 10:09:00', '2026-03-23 10:09:00', '0');
INSERT INTO `barrage` VALUES ('3501011', '回民街美食很多，建议先看评价再排队。', 'https://www.code-nav.cn/logo.png', '6', '0', '2026-03-23 10:10:00', '2026-03-23 10:10:00', '0');
INSERT INTO `barrage` VALUES ('3501012', '春熙路晚上灯光很好，拍照出片。', 'https://www.code-nav.cn/logo.png', '5', '0', '2026-03-23 10:11:00', '2026-03-23 10:11:00', '0');
INSERT INTO `barrage` VALUES ('3501013', '颐和园适合慢慢逛，别安排太满。', 'https://www.code-nav.cn/logo.png', '6', '0', '2026-03-23 10:12:00', '2026-03-23 10:12:00', '0');
INSERT INTO `barrage` VALUES ('3501014', '西溪湿地乘船体验值得安排。', 'https://www.code-nav.cn/logo.png', '5', '0', '2026-03-23 10:13:00', '2026-03-23 10:13:00', '0');
INSERT INTO `barrage` VALUES ('3501015', '大雁塔夜景和音乐喷泉都很推荐。', 'https://www.code-nav.cn/logo.png', '6', '1', '2026-03-23 10:14:00', '2026-03-23 10:14:00', '0');
INSERT INTO `barrage` VALUES ('3501016', '锦里古街适合边走边吃，不用赶。', 'https://www.code-nav.cn/logo.png', '5', '0', '2026-03-23 10:15:00', '2026-03-23 10:15:00', '0');
INSERT INTO `barrage` VALUES ('3501017', '什刹海夜晚骑行很舒服。', 'https://www.code-nav.cn/logo.png', '6', '0', '2026-03-23 10:16:00', '2026-03-23 10:16:00', '0');
INSERT INTO `barrage` VALUES ('3501018', '千岛湖建议预留整天，游船时间较长。', 'https://www.code-nav.cn/logo.png', '5', '0', '2026-03-23 10:17:00', '2026-03-23 10:17:00', '0');

-- ----------------------------
-- Table structure for `barrage_bak_20260323`
-- ----------------------------
DROP TABLE IF EXISTS `barrage_bak_20260323`;
CREATE TABLE `barrage_bak_20260323` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `message` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '弹幕文本',
  `userAvatar` varchar(1024) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '用户头像',
  `userId` bigint NOT NULL COMMENT '用户id',
  `isSelected` tinyint NOT NULL DEFAULT '0' COMMENT '是否精选（默认0，精选为1）',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3501019 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of barrage_bak_20260323
-- ----------------------------
INSERT INTO `barrage_bak_20260323` VALUES ('3501001', '北京故宫建议至少预留半天，不然会很赶。', 'https://www.code-nav.cn/logo.png', '6', '1', '2026-03-23 10:00:00', '2026-03-23 10:00:00', '0');
INSERT INTO `barrage_bak_20260323` VALUES ('3501002', '西湖傍晚真的很美，湖边风景很治愈。', 'https://www.code-nav.cn/logo.png', '5', '1', '2026-03-23 10:01:00', '2026-03-23 10:01:00', '0');
INSERT INTO `barrage_bak_20260323` VALUES ('3501003', '兵马俑讲解很关键，建议拼讲解团。', 'https://www.code-nav.cn/logo.png', '6', '0', '2026-03-23 10:02:00', '2026-03-23 10:02:00', '0');
INSERT INTO `barrage_bak_20260323` VALUES ('3501004', '成都熊猫基地上午去最合适，熊猫更活跃。', 'https://www.code-nav.cn/logo.png', '5', '1', '2026-03-23 10:03:00', '2026-03-23 10:03:00', '0');
INSERT INTO `barrage_bak_20260323` VALUES ('3501005', '长城风大，带帽子和防风外套很有必要。', 'https://www.code-nav.cn/logo.png', '6', '0', '2026-03-23 10:04:00', '2026-03-23 10:04:00', '0');
INSERT INTO `barrage_bak_20260323` VALUES ('3501006', '河坊街的小吃集中，晚上氛围更好。', 'https://www.code-nav.cn/logo.png', '5', '0', '2026-03-23 10:05:00', '2026-03-23 10:05:00', '0');
INSERT INTO `barrage_bak_20260323` VALUES ('3501007', '西安城墙骑行体验不错，路线平缓。', 'https://www.code-nav.cn/logo.png', '6', '0', '2026-03-23 10:06:00', '2026-03-23 10:06:00', '0');
INSERT INTO `barrage_bak_20260323` VALUES ('3501008', '都江堰和青城山一天略紧，建议早起。', 'https://www.code-nav.cn/logo.png', '5', '0', '2026-03-23 10:07:00', '2026-03-23 10:07:00', '0');
INSERT INTO `barrage_bak_20260323` VALUES ('3501009', '南锣鼓巷周末人多，工作日体验更好。', 'https://www.code-nav.cn/logo.png', '6', '0', '2026-03-23 10:08:00', '2026-03-23 10:08:00', '0');
INSERT INTO `barrage_bak_20260323` VALUES ('3501010', '灵隐寺建议提前线上购票，排队会少一些。', 'https://www.code-nav.cn/logo.png', '5', '0', '2026-03-23 10:09:00', '2026-03-23 10:09:00', '0');
INSERT INTO `barrage_bak_20260323` VALUES ('3501011', '回民街美食很多，建议先看评价再排队。', 'https://www.code-nav.cn/logo.png', '6', '0', '2026-03-23 10:10:00', '2026-03-23 10:10:00', '0');
INSERT INTO `barrage_bak_20260323` VALUES ('3501012', '春熙路晚上灯光很好，拍照出片。', 'https://www.code-nav.cn/logo.png', '5', '0', '2026-03-23 10:11:00', '2026-03-23 10:11:00', '0');
INSERT INTO `barrage_bak_20260323` VALUES ('3501013', '颐和园适合慢慢逛，别安排太满。', 'https://www.code-nav.cn/logo.png', '6', '0', '2026-03-23 10:12:00', '2026-03-23 10:12:00', '0');
INSERT INTO `barrage_bak_20260323` VALUES ('3501014', '西溪湿地乘船体验值得安排。', 'https://www.code-nav.cn/logo.png', '5', '0', '2026-03-23 10:13:00', '2026-03-23 10:13:00', '0');
INSERT INTO `barrage_bak_20260323` VALUES ('3501015', '大雁塔夜景和音乐喷泉都很推荐。', 'https://www.code-nav.cn/logo.png', '6', '1', '2026-03-23 10:14:00', '2026-03-23 10:14:00', '0');
INSERT INTO `barrage_bak_20260323` VALUES ('3501016', '锦里古街适合边走边吃，不用赶。', 'https://www.code-nav.cn/logo.png', '5', '0', '2026-03-23 10:15:00', '2026-03-23 10:15:00', '0');
INSERT INTO `barrage_bak_20260323` VALUES ('3501017', '什刹海夜晚骑行很舒服。', 'https://www.code-nav.cn/logo.png', '6', '0', '2026-03-23 10:16:00', '2026-03-23 10:16:00', '0');
INSERT INTO `barrage_bak_20260323` VALUES ('3501018', '千岛湖建议预留整天，游船时间较长。', 'https://www.code-nav.cn/logo.png', '5', '0', '2026-03-23 10:17:00', '2026-03-23 10:17:00', '0');

-- ----------------------------
-- Table structure for `comment`
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '评论 ID',
  `postId` bigint NOT NULL COMMENT '面经帖子 ID',
  `userId` bigint NOT NULL COMMENT '用户 ID',
  `content` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '评论内容',
  `parentId` bigint DEFAULT NULL COMMENT '父评论 ID，支持多级嵌套回复',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
  `ancestorId` bigint DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `comment_questionId` (`postId`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3601017 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of comment
-- ----------------------------
INSERT INTO `comment` VALUES ('3601001', '3401001', '6', '这条北京2天路线很实用，我照着走了一遍。', null, '2026-03-23 10:20:00', '2026-03-23 10:20:00', '0', null);
INSERT INTO `comment` VALUES ('3601002', '3401001', '5', '建议补充一下故宫预约时间段信息。', null, '2026-03-23 10:21:00', '2026-03-23 10:21:00', '0', null);
INSERT INTO `comment` VALUES ('3601003', '3401002', '6', '胡同夜游那段很有帮助，感谢分享。', null, '2026-03-23 10:22:00', '2026-03-23 10:22:00', '0', null);
INSERT INTO `comment` VALUES ('3601004', '3401003', '5', '西湖一日安排节奏不错，不会太累。', null, '2026-03-23 10:23:00', '2026-03-23 10:23:00', '0', null);
INSERT INTO `comment` VALUES ('3601005', '3401004', '6', '灵隐寺和西溪湿地组合很适合亲子。', null, '2026-03-23 10:24:00', '2026-03-23 10:24:00', '0', null);
INSERT INTO `comment` VALUES ('3601006', '3401005', '5', '西安3天这篇很完整，路线清晰。', null, '2026-03-23 10:25:00', '2026-03-23 10:25:00', '0', null);
INSERT INTO `comment` VALUES ('3601007', '3401006', '6', '回民街避坑建议很实在。', null, '2026-03-23 10:26:00', '2026-03-23 10:26:00', '0', null);
INSERT INTO `comment` VALUES ('3601008', '3401007', '5', '熊猫基地建议提前预约，补充一下会更好。', null, '2026-03-23 10:27:00', '2026-03-23 10:27:00', '0', null);
INSERT INTO `comment` VALUES ('3601009', '3401008', '6', '都江堰和青城山一天确实偏紧。', null, '2026-03-23 10:28:00', '2026-03-23 10:28:00', '0', null);
INSERT INTO `comment` VALUES ('3601010', '3401009', '5', '预算部分写得很细，收藏了。', null, '2026-03-23 10:29:00', '2026-03-23 10:29:00', '0', null);
INSERT INTO `comment` VALUES ('3601011', '3401010', '6', '杭州预算方案挺适合周末短途。', null, '2026-03-23 10:30:00', '2026-03-23 10:30:00', '0', null);
INSERT INTO `comment` VALUES ('3601012', '3401011', '5', '夜景机位建议实测有效。', null, '2026-03-23 10:31:00', '2026-03-23 10:31:00', '0', null);
INSERT INTO `comment` VALUES ('3601013', '3401012', '6', '春熙路夜游路线安排合理。', null, '2026-03-23 10:32:00', '2026-03-23 10:32:00', '0', null);
INSERT INTO `comment` VALUES ('3601014', '3401001', '5', '同意，长城当天尽量别再排太多市内景点。', '3601001', '2026-03-23 10:33:00', '2026-03-23 10:33:00', '0', '3601001');
INSERT INTO `comment` VALUES ('3601015', '3401005', '6', '补充一点：兵马俑建议工作日去，人少体验更好。', '3601006', '2026-03-23 10:34:00', '2026-03-23 10:34:00', '0', '3601006');
INSERT INTO `comment` VALUES ('3601016', '3401007', '5', '亲子出行记得带轻便推车，园区步行较多。', '3601008', '2026-03-23 10:35:00', '2026-03-23 10:35:00', '0', '3601008');

-- ----------------------------
-- Table structure for `comment_bak_20260323`
-- ----------------------------
DROP TABLE IF EXISTS `comment_bak_20260323`;
CREATE TABLE `comment_bak_20260323` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '评论 ID',
  `postId` bigint NOT NULL COMMENT '面经帖子 ID',
  `userId` bigint NOT NULL COMMENT '用户 ID',
  `content` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '评论内容',
  `parentId` bigint DEFAULT NULL COMMENT '父评论 ID，支持多级嵌套回复',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
  `ancestorId` bigint DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `comment_questionId` (`postId`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3601017 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of comment_bak_20260323
-- ----------------------------
INSERT INTO `comment_bak_20260323` VALUES ('3601001', '3401001', '6', '这条北京2天路线很实用，我照着走了一遍。', null, '2026-03-23 10:20:00', '2026-03-23 10:20:00', '0', null);
INSERT INTO `comment_bak_20260323` VALUES ('3601002', '3401001', '5', '建议补充一下故宫预约时间段信息。', null, '2026-03-23 10:21:00', '2026-03-23 10:21:00', '0', null);
INSERT INTO `comment_bak_20260323` VALUES ('3601003', '3401002', '6', '胡同夜游那段很有帮助，感谢分享。', null, '2026-03-23 10:22:00', '2026-03-23 10:22:00', '0', null);
INSERT INTO `comment_bak_20260323` VALUES ('3601004', '3401003', '5', '西湖一日安排节奏不错，不会太累。', null, '2026-03-23 10:23:00', '2026-03-23 10:23:00', '0', null);
INSERT INTO `comment_bak_20260323` VALUES ('3601005', '3401004', '6', '灵隐寺和西溪湿地组合很适合亲子。', null, '2026-03-23 10:24:00', '2026-03-23 10:24:00', '0', null);
INSERT INTO `comment_bak_20260323` VALUES ('3601006', '3401005', '5', '西安3天这篇很完整，路线清晰。', null, '2026-03-23 10:25:00', '2026-03-23 10:25:00', '0', null);
INSERT INTO `comment_bak_20260323` VALUES ('3601007', '3401006', '6', '回民街避坑建议很实在。', null, '2026-03-23 10:26:00', '2026-03-23 10:26:00', '0', null);
INSERT INTO `comment_bak_20260323` VALUES ('3601008', '3401007', '5', '熊猫基地建议提前预约，补充一下会更好。', null, '2026-03-23 10:27:00', '2026-03-23 10:27:00', '0', null);
INSERT INTO `comment_bak_20260323` VALUES ('3601009', '3401008', '6', '都江堰和青城山一天确实偏紧。', null, '2026-03-23 10:28:00', '2026-03-23 10:28:00', '0', null);
INSERT INTO `comment_bak_20260323` VALUES ('3601010', '3401009', '5', '预算部分写得很细，收藏了。', null, '2026-03-23 10:29:00', '2026-03-23 10:29:00', '0', null);
INSERT INTO `comment_bak_20260323` VALUES ('3601011', '3401010', '6', '杭州预算方案挺适合周末短途。', null, '2026-03-23 10:30:00', '2026-03-23 10:30:00', '0', null);
INSERT INTO `comment_bak_20260323` VALUES ('3601012', '3401011', '5', '夜景机位建议实测有效。', null, '2026-03-23 10:31:00', '2026-03-23 10:31:00', '0', null);
INSERT INTO `comment_bak_20260323` VALUES ('3601013', '3401012', '6', '春熙路夜游路线安排合理。', null, '2026-03-23 10:32:00', '2026-03-23 10:32:00', '0', null);
INSERT INTO `comment_bak_20260323` VALUES ('3601014', '3401001', '5', '同意，长城当天尽量别再排太多市内景点。', '3601001', '2026-03-23 10:33:00', '2026-03-23 10:33:00', '0', '3601001');
INSERT INTO `comment_bak_20260323` VALUES ('3601015', '3401005', '6', '补充一点：兵马俑建议工作日去，人少体验更好。', '3601006', '2026-03-23 10:34:00', '2026-03-23 10:34:00', '0', '3601006');
INSERT INTO `comment_bak_20260323` VALUES ('3601016', '3401007', '5', '亲子出行记得带轻便推车，园区步行较多。', '3601008', '2026-03-23 10:35:00', '2026-03-23 10:35:00', '0', '3601008');

-- ----------------------------
-- Table structure for `notice`
-- ----------------------------
DROP TABLE IF EXISTS `notice`;
CREATE TABLE `notice` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `noticeTitle` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '公告标题',
  `noticeContent` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '公告内容',
  `noticeAdminId` bigint NOT NULL COMMENT '创建人id（管理员）',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3301009 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of notice
-- ----------------------------
INSERT INTO `notice` VALUES ('3301001', '清明假期景区预约提示', '清明期间热门景区客流较大，请提前在景区官方渠道预约，避免现场排队。', '5', '2026-03-23 09:30:00', '2026-03-23 09:30:00', '0');
INSERT INTO `notice` VALUES ('3301002', '西安部分景区夜场开放', '本周五至周日，大雁塔周边夜间演出延长至21:30，建议错峰出行。', '5', '2026-03-23 09:31:00', '2026-03-23 09:31:00', '0');
INSERT INTO `notice` VALUES ('3301003', '北京长城风大注意保暖', '近期八达岭区域风力较强，建议携带防风外套和舒适防滑鞋。', '5', '2026-03-23 09:32:00', '2026-03-23 09:32:00', '0');
INSERT INTO `notice` VALUES ('3301004', '杭州西湖周末交通提醒', '周末西湖周边停车紧张，建议优先地铁或公交出行。', '5', '2026-03-23 09:33:00', '2026-03-23 09:33:00', '0');
INSERT INTO `notice` VALUES ('3301005', '成都熊猫基地早高峰提示', '熊猫基地上午9:00-11:00客流密集，建议提前入园并规划参观路线。', '5', '2026-03-23 09:34:00', '2026-03-23 09:34:00', '0');
INSERT INTO `notice` VALUES ('3301006', '平台新增多城市路线模板', '平台已上线北京、杭州、西安、成都多主题路线，可在景点路线页查看。', '5', '2026-03-23 09:35:00', '2026-03-23 09:35:00', '0');
INSERT INTO `notice` VALUES ('3301007', '旅游旺季消费防骗提醒', '请在正规平台预订门票与酒店，谨防低价陷阱和虚假导游服务。', '5', '2026-03-23 09:36:00', '2026-03-23 09:36:00', '0');
INSERT INTO `notice` VALUES ('3301008', '景区文明旅游倡议', '请遵守景区秩序，爱护文物古迹，不乱刻画、不乱丢垃圾。', '5', '2026-03-23 09:37:00', '2026-03-23 09:37:00', '0');

-- ----------------------------
-- Table structure for `notice_bak_20260323`
-- ----------------------------
DROP TABLE IF EXISTS `notice_bak_20260323`;
CREATE TABLE `notice_bak_20260323` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `noticeTitle` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '公告标题',
  `noticeContent` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '公告内容',
  `noticeAdminId` bigint NOT NULL COMMENT '创建人id（管理员）',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3301009 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of notice_bak_20260323
-- ----------------------------
INSERT INTO `notice_bak_20260323` VALUES ('3301001', '清明假期景区预约提示', '清明期间热门景区客流较大，请提前在景区官方渠道预约，避免现场排队。', '5', '2026-03-23 09:30:00', '2026-03-23 09:30:00', '0');
INSERT INTO `notice_bak_20260323` VALUES ('3301002', '西安部分景区夜场开放', '本周五至周日，大雁塔周边夜间演出延长至21:30，建议错峰出行。', '5', '2026-03-23 09:31:00', '2026-03-23 09:31:00', '0');
INSERT INTO `notice_bak_20260323` VALUES ('3301003', '北京长城风大注意保暖', '近期八达岭区域风力较强，建议携带防风外套和舒适防滑鞋。', '5', '2026-03-23 09:32:00', '2026-03-23 09:32:00', '0');
INSERT INTO `notice_bak_20260323` VALUES ('3301004', '杭州西湖周末交通提醒', '周末西湖周边停车紧张，建议优先地铁或公交出行。', '5', '2026-03-23 09:33:00', '2026-03-23 09:33:00', '0');
INSERT INTO `notice_bak_20260323` VALUES ('3301005', '成都熊猫基地早高峰提示', '熊猫基地上午9:00-11:00客流密集，建议提前入园并规划参观路线。', '5', '2026-03-23 09:34:00', '2026-03-23 09:34:00', '0');
INSERT INTO `notice_bak_20260323` VALUES ('3301006', '平台新增多城市路线模板', '平台已上线北京、杭州、西安、成都多主题路线，可在景点路线页查看。', '5', '2026-03-23 09:35:00', '2026-03-23 09:35:00', '0');
INSERT INTO `notice_bak_20260323` VALUES ('3301007', '旅游旺季消费防骗提醒', '请在正规平台预订门票与酒店，谨防低价陷阱和虚假导游服务。', '5', '2026-03-23 09:36:00', '2026-03-23 09:36:00', '0');
INSERT INTO `notice_bak_20260323` VALUES ('3301008', '景区文明旅游倡议', '请遵守景区秩序，爱护文物古迹，不乱刻画、不乱丢垃圾。', '5', '2026-03-23 09:37:00', '2026-03-23 09:37:00', '0');

-- ----------------------------
-- Table structure for `post`
-- ----------------------------
DROP TABLE IF EXISTS `post`;
CREATE TABLE `post` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `title` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '内容',
  `tags` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '标签列表（json 数组）',
  `thumbNum` int NOT NULL DEFAULT '0' COMMENT '点赞数',
  `favourNum` int NOT NULL DEFAULT '0' COMMENT '收藏数',
  `userId` bigint NOT NULL COMMENT '创建用户 id',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_userId` (`userId`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3401013 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='帖子';

-- ----------------------------
-- Records of post
-- ----------------------------
INSERT INTO `post` VALUES ('3401001', '北京2天经典路线（故宫+长城）', '第1天建议故宫+景山，第2天建议八达岭长城，市内交通以地铁+景区专线为主。', '[\"北京\",\"历史文化\",\"2天\"]', '12', '29', '5', '2026-03-23 09:40:00', '2026-03-23 22:29:22', '0');
INSERT INTO `post` VALUES ('3401002', '北京胡同夜游怎么安排', '南锣鼓巷到什刹海步行线路适合夜游，建议18点后开始，拍照与美食体验兼顾。', '[\"北京\",\"夜游\",\"美食\"]', '7', '15', '6', '2026-03-23 09:41:00', '2026-03-23 09:41:00', '0');
INSERT INTO `post` VALUES ('3401003', '杭州西湖一日慢游攻略', '上午苏堤骑行，下午雷峰塔和湖滨散步，晚间河坊街小吃打卡。', '[\"杭州\",\"自然风光\",\"1天\"]', '11', '22', '5', '2026-03-23 09:42:00', '2026-03-23 09:42:00', '0');
INSERT INTO `post` VALUES ('3401004', '杭州灵隐寺+西溪湿地双线推荐', '灵隐寺适合早去避人流，西溪湿地建议下午乘船体验。', '[\"杭州\",\"亲子\",\"慢游\"]', '6', '13', '6', '2026-03-23 09:43:00', '2026-03-23 09:43:00', '0');
INSERT INTO `post` VALUES ('3401005', '西安3天深度文化游', '兵马俑+华清宫建议同日，城墙与大雁塔安排在城市日，回民街留给晚上。', '[\"西安\",\"历史文化\",\"3天\"]', '16', '33', '5', '2026-03-23 09:44:00', '2026-03-23 09:44:00', '0');
INSERT INTO `post` VALUES ('3401006', '西安美食避坑与点单建议', '回民街建议避开景区主干道高峰，优先本地口碑老店。', '[\"西安\",\"美食\",\"避坑\"]', '8', '19', '6', '2026-03-23 09:45:00', '2026-03-23 09:45:00', '0');
INSERT INTO `post` VALUES ('3401007', '成都亲子游路线（熊猫基地优先）', '熊猫基地尽量上午入园，下午锦里和宽窄巷子轻松逛。', '[\"成都\",\"亲子\",\"2天\"]', '10', '24', '5', '2026-03-23 09:46:00', '2026-03-23 09:46:00', '0');
INSERT INTO `post` VALUES ('3401008', '成都山水线：都江堰+青城山', '建议早出发，先都江堰后青城山，步行强度中等。', '[\"成都\",\"自然风光\",\"1天\"]', '9', '18', '6', '2026-03-23 09:47:00', '2026-03-23 09:47:00', '0');
INSERT INTO `post` VALUES ('3401009', '预算1000元如何玩北京2天', '交通优先地铁，门票选择核心景点，住宿可选二环外地铁沿线。', '[\"北京\",\"预算\",\"2天\"]', '13', '21', '5', '2026-03-23 09:48:00', '2026-03-23 09:48:00', '0');
INSERT INTO `post` VALUES ('3401010', '预算1500元杭州+周边轻松游', '西湖+灵隐寺+千岛湖组合，住宿建议选地铁沿线。', '[\"杭州\",\"预算\",\"2-3天\"]', '7', '14', '6', '2026-03-23 09:49:00', '2026-03-23 09:49:00', '0');
INSERT INTO `post` VALUES ('3401011', '西安夜景拍照机位推荐', '城墙南门段和大雁塔广场适合夜景，建议携带轻便三脚架。', '[\"西安\",\"夜游\",\"拍照\"]', '5', '12', '5', '2026-03-23 09:50:00', '2026-03-23 09:50:00', '0');
INSERT INTO `post` VALUES ('3401012', '成都春熙路夜生活攻略', '春熙路晚餐后可步行至太古里，适合年轻人夜游路线。', '[\"成都\",\"夜游\",\"城市\"]', '6', '11', '6', '2026-03-23 09:51:00', '2026-03-23 09:51:00', '0');

-- ----------------------------
-- Table structure for `post_bak_20260323`
-- ----------------------------
DROP TABLE IF EXISTS `post_bak_20260323`;
CREATE TABLE `post_bak_20260323` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `title` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '内容',
  `tags` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '标签列表（json 数组）',
  `thumbNum` int NOT NULL DEFAULT '0' COMMENT '点赞数',
  `favourNum` int NOT NULL DEFAULT '0' COMMENT '收藏数',
  `userId` bigint NOT NULL COMMENT '创建用户 id',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_userId` (`userId`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3401013 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='帖子';

-- ----------------------------
-- Records of post_bak_20260323
-- ----------------------------
INSERT INTO `post_bak_20260323` VALUES ('3401001', '北京2天经典路线（故宫+长城）', '第1天建议故宫+景山，第2天建议八达岭长城，市内交通以地铁+景区专线为主。', '[\"北京\",\"历史文化\",\"2天\"]', '12', '29', '5', '2026-03-23 09:40:00', '2026-03-23 09:40:00', '0');
INSERT INTO `post_bak_20260323` VALUES ('3401002', '北京胡同夜游怎么安排', '南锣鼓巷到什刹海步行线路适合夜游，建议18点后开始，拍照与美食体验兼顾。', '[\"北京\",\"夜游\",\"美食\"]', '7', '15', '6', '2026-03-23 09:41:00', '2026-03-23 09:41:00', '0');
INSERT INTO `post_bak_20260323` VALUES ('3401003', '杭州西湖一日慢游攻略', '上午苏堤骑行，下午雷峰塔和湖滨散步，晚间河坊街小吃打卡。', '[\"杭州\",\"自然风光\",\"1天\"]', '11', '22', '5', '2026-03-23 09:42:00', '2026-03-23 09:42:00', '0');
INSERT INTO `post_bak_20260323` VALUES ('3401004', '杭州灵隐寺+西溪湿地双线推荐', '灵隐寺适合早去避人流，西溪湿地建议下午乘船体验。', '[\"杭州\",\"亲子\",\"慢游\"]', '6', '13', '6', '2026-03-23 09:43:00', '2026-03-23 09:43:00', '0');
INSERT INTO `post_bak_20260323` VALUES ('3401005', '西安3天深度文化游', '兵马俑+华清宫建议同日，城墙与大雁塔安排在城市日，回民街留给晚上。', '[\"西安\",\"历史文化\",\"3天\"]', '16', '33', '5', '2026-03-23 09:44:00', '2026-03-23 09:44:00', '0');
INSERT INTO `post_bak_20260323` VALUES ('3401006', '西安美食避坑与点单建议', '回民街建议避开景区主干道高峰，优先本地口碑老店。', '[\"西安\",\"美食\",\"避坑\"]', '8', '19', '6', '2026-03-23 09:45:00', '2026-03-23 09:45:00', '0');
INSERT INTO `post_bak_20260323` VALUES ('3401007', '成都亲子游路线（熊猫基地优先）', '熊猫基地尽量上午入园，下午锦里和宽窄巷子轻松逛。', '[\"成都\",\"亲子\",\"2天\"]', '10', '24', '5', '2026-03-23 09:46:00', '2026-03-23 09:46:00', '0');
INSERT INTO `post_bak_20260323` VALUES ('3401008', '成都山水线：都江堰+青城山', '建议早出发，先都江堰后青城山，步行强度中等。', '[\"成都\",\"自然风光\",\"1天\"]', '9', '18', '6', '2026-03-23 09:47:00', '2026-03-23 09:47:00', '0');
INSERT INTO `post_bak_20260323` VALUES ('3401009', '预算1000元如何玩北京2天', '交通优先地铁，门票选择核心景点，住宿可选二环外地铁沿线。', '[\"北京\",\"预算\",\"2天\"]', '13', '21', '5', '2026-03-23 09:48:00', '2026-03-23 09:48:00', '0');
INSERT INTO `post_bak_20260323` VALUES ('3401010', '预算1500元杭州+周边轻松游', '西湖+灵隐寺+千岛湖组合，住宿建议选地铁沿线。', '[\"杭州\",\"预算\",\"2-3天\"]', '7', '14', '6', '2026-03-23 09:49:00', '2026-03-23 09:49:00', '0');
INSERT INTO `post_bak_20260323` VALUES ('3401011', '西安夜景拍照机位推荐', '城墙南门段和大雁塔广场适合夜景，建议携带轻便三脚架。', '[\"西安\",\"夜游\",\"拍照\"]', '5', '12', '5', '2026-03-23 09:50:00', '2026-03-23 09:50:00', '0');
INSERT INTO `post_bak_20260323` VALUES ('3401012', '成都春熙路夜生活攻略', '春熙路晚餐后可步行至太古里，适合年轻人夜游路线。', '[\"成都\",\"夜游\",\"城市\"]', '6', '11', '6', '2026-03-23 09:51:00', '2026-03-23 09:51:00', '0');

-- ----------------------------
-- Table structure for `post_favour`
-- ----------------------------
DROP TABLE IF EXISTS `post_favour`;
CREATE TABLE `post_favour` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `postId` bigint NOT NULL COMMENT '帖子 id',
  `userId` bigint NOT NULL COMMENT '创建用户 id',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_postId` (`postId`) USING BTREE,
  KEY `idx_userId` (`userId`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2036087914954342402 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='帖子收藏';

-- ----------------------------
-- Records of post_favour
-- ----------------------------

-- ----------------------------
-- Table structure for `post_favour_bak_20260323`
-- ----------------------------
DROP TABLE IF EXISTS `post_favour_bak_20260323`;
CREATE TABLE `post_favour_bak_20260323` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `postId` bigint NOT NULL COMMENT '帖子 id',
  `userId` bigint NOT NULL COMMENT '创建用户 id',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_postId` (`postId`) USING BTREE,
  KEY `idx_userId` (`userId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='帖子收藏';

-- ----------------------------
-- Records of post_favour_bak_20260323
-- ----------------------------

-- ----------------------------
-- Table structure for `post_thumb`
-- ----------------------------
DROP TABLE IF EXISTS `post_thumb`;
CREATE TABLE `post_thumb` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `postId` bigint NOT NULL COMMENT '帖子 id',
  `userId` bigint NOT NULL COMMENT '创建用户 id',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_postId` (`postId`) USING BTREE,
  KEY `idx_userId` (`userId`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2036087914698489859 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='帖子点赞';

-- ----------------------------
-- Records of post_thumb
-- ----------------------------

-- ----------------------------
-- Table structure for `post_thumb_bak_20260323`
-- ----------------------------
DROP TABLE IF EXISTS `post_thumb_bak_20260323`;
CREATE TABLE `post_thumb_bak_20260323` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `postId` bigint NOT NULL COMMENT '帖子 id',
  `userId` bigint NOT NULL COMMENT '创建用户 id',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_postId` (`postId`) USING BTREE,
  KEY `idx_userId` (`userId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='帖子点赞';

-- ----------------------------
-- Records of post_thumb_bak_20260323
-- ----------------------------

-- ----------------------------
-- Table structure for `spot`
-- ----------------------------
DROP TABLE IF EXISTS `spot`;
CREATE TABLE `spot` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `adminId` bigint NOT NULL COMMENT '管理员 ID（某人创建该景点）',
  `spotName` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '景点名',
  `spotAvatar` varchar(1024) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '景点封面图',
  `spotLocation` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '景点所在地',
  `spotDescription` varchar(4096) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '景点介绍',
  `latitude` decimal(10,6) DEFAULT NULL COMMENT '景点纬度',
  `longitude` decimal(10,6) DEFAULT NULL COMMENT '景点经度',
  `visitDurationMinutes` int DEFAULT '120' COMMENT '建议游玩时长（分钟）',
  `openTime` varchar(16) DEFAULT '09:00' COMMENT '开放时间，格式 HH:mm',
  `closeTime` varchar(16) DEFAULT '18:00' COMMENT '关闭时间，格式 HH:mm',
  `spotTags` varchar(2048) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '景点标签（JSON字符串数组）',
  `favourNum` int NOT NULL DEFAULT '0' COMMENT '收藏量',
  `viewNum` int NOT NULL DEFAULT '0' COMMENT '浏览量',
  `spotStatus` tinyint NOT NULL DEFAULT '0' COMMENT '景点状态（1开放，0关闭，默认关闭）',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3009006 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of spot
-- ----------------------------
INSERT INTO `spot` VALUES ('3001001', '5', '故宫', 'https://upload.wikimedia.org/wikipedia/commons/a/a7/Forbidden_City_Beijing_Shenwumen_Gate.JPG', '北京', '明清皇家宫殿群，适合历史文化深度游。', '39.916345', '116.397155', '180', '08:00', '18:00', '[\"历史文化\",\"宫殿\",\"北京\"]', '186', '920', '1', '2026-03-23 09:00:00', '2026-03-24 01:11:08', '0');
INSERT INTO `spot` VALUES ('3001002', '5', '颐和园', 'https://upload.wikimedia.org/wikipedia/commons/0/06/%E9%A2%90%E5%92%8C%E5%9B%AD%E4%B8%87%E5%AF%BF%E5%B1%B1%E4%BD%9B%E9%A6%99%E9%98%81.jpg', '北京', '皇家园林代表，适合慢游与拍照。', '39.999629', '116.275451', '180', '08:00', '18:00', '[\"园林\",\"休闲\",\"北京\"]', '142', '760', '1', '2026-03-23 09:00:00', '2026-03-24 01:11:08', '0');
INSERT INTO `spot` VALUES ('3001003', '5', '八达岭长城', 'https://upload.wikimedia.org/wikipedia/commons/c/c4/GreatWall_Badaling.jpg', '北京', '长城经典段，交通便利，适合首次打卡。', '40.356389', '116.020556', '240', '07:30', '18:00', '[\"历史文化\",\"世界遗产\",\"北京\"]', '265', '1180', '1', '2026-03-23 09:00:00', '2026-03-24 01:11:08', '0');
INSERT INTO `spot` VALUES ('3001004', '5', '天坛公园', 'https://upload.wikimedia.org/wikipedia/commons/5/5f/Temple_of_Heaven_20160323_01.jpg', '北京', '祭天建筑群与古树园林结合，文化氛围浓。', '39.882200', '116.406600', '180', '08:00', '18:00', '[\"历史文化\",\"公园\",\"北京\"]', '98', '620', '1', '2026-03-23 09:00:00', '2026-03-24 01:11:08', '0');
INSERT INTO `spot` VALUES ('3001005', '5', '南锣鼓巷', 'https://upload.wikimedia.org/wikipedia/commons/7/73/View_down_Nanluoguxiang_2011.jpg', '北京', '胡同街区与小吃文化体验地。', '39.941000', '116.403700', '120', '09:00', '18:00', '[\"人文街区\",\"美食\",\"北京\"]', '173', '840', '1', '2026-03-23 09:00:00', '2026-03-24 01:11:08', '0');
INSERT INTO `spot` VALUES ('3001006', '5', '什刹海', 'https://upload.wikimedia.org/wikipedia/commons/9/95/Shishahai.jpg', '北京', '湖区夜景与酒吧街，适合傍晚游玩。', '39.942500', '116.385300', '150', '10:00', '22:00', '[\"夜游\",\"休闲\",\"北京\"]', '132', '690', '1', '2026-03-23 09:00:00', '2026-03-24 01:11:08', '0');
INSERT INTO `spot` VALUES ('3002001', '5', '西湖', 'https://upload.wikimedia.org/wikipedia/commons/1/17/West_Lake%2C_Hangzhou_2025.jpg', '杭州', '杭州核心景区，四季皆可游。', '30.247100', '120.155100', '120', '09:00', '18:00', '[\"自然风光\",\"湖泊\",\"杭州\"]', '281', '1300', '1', '2026-03-23 09:00:00', '2026-03-24 01:11:08', '0');
INSERT INTO `spot` VALUES ('3002002', '5', '灵隐寺', 'https://upload.wikimedia.org/wikipedia/commons/c/c2/Hangzhou_Lingyin-Temple_20161003.jpg', '杭州', '千年古刹，文化底蕴深厚。', '30.242800', '120.101500', '120', '09:00', '18:00', '[\"历史文化\",\"寺庙\",\"杭州\"]', '167', '860', '1', '2026-03-23 09:00:00', '2026-03-24 01:11:08', '0');
INSERT INTO `spot` VALUES ('3002003', '5', '西溪湿地', 'https://upload.wikimedia.org/wikipedia/commons/2/20/Xixi_Wetland_Park%2C_Hangzhou%2C_China.jpg', '杭州', '湿地生态景观，适合亲子与慢游。', '30.272100', '120.063900', '120', '09:00', '18:00', '[\"自然风光\",\"亲子\",\"杭州\"]', '121', '610', '1', '2026-03-23 09:00:00', '2026-03-24 01:11:08', '0');
INSERT INTO `spot` VALUES ('3002004', '5', '河坊街', 'https://upload.wikimedia.org/wikipedia/commons/9/99/Hefang_Street.jpg', '杭州', '古街步行区，适合美食与夜游。', '30.242600', '120.171700', '150', '10:00', '22:00', '[\"人文街区\",\"美食\",\"杭州\"]', '149', '735', '1', '2026-03-23 09:00:00', '2026-03-24 01:11:08', '0');
INSERT INTO `spot` VALUES ('3002005', '5', '千岛湖', 'https://upload.wikimedia.org/wikipedia/commons/6/6f/Thousand_Island_Lake.JPG', '杭州', '湖岛景观丰富，适合1日外圈线路。', '29.594600', '118.993100', '120', '09:00', '18:00', '[\"自然风光\",\"湖泊\",\"杭州\"]', '138', '700', '1', '2026-03-23 09:00:00', '2026-03-24 01:11:08', '0');
INSERT INTO `spot` VALUES ('3003001', '5', '秦始皇兵马俑', 'https://upload.wikimedia.org/wikipedia/commons/8/88/51714-Terracota-Army.jpg', '西安', '世界级考古遗址，历史价值极高。', '34.384900', '109.273200', '180', '08:00', '18:00', '[\"历史文化\",\"博物馆\",\"西安\"]', '294', '1410', '1', '2026-03-23 09:00:00', '2026-03-24 01:11:08', '0');
INSERT INTO `spot` VALUES ('3003002', '5', '西安城墙', 'https://upload.wikimedia.org/wikipedia/commons/3/32/City_wall_of_Xi%27an_51550-Xian_%2827959363326%29.jpg', '西安', '可骑行环城，视角独特。', '34.266300', '108.943400', '120', '09:00', '18:00', '[\"历史文化\",\"骑行\",\"西安\"]', '183', '910', '1', '2026-03-23 09:00:00', '2026-03-24 01:11:08', '0');
INSERT INTO `spot` VALUES ('3003003', '5', '大雁塔', 'https://upload.wikimedia.org/wikipedia/commons/1/13/Giant_Wild_Goose_Pagoda.jpg', '西安', '唐文化地标，夜景与喷泉演出热门。', '34.218600', '108.962100', '150', '10:00', '22:00', '[\"历史文化\",\"夜游\",\"西安\"]', '176', '845', '1', '2026-03-23 09:00:00', '2026-03-24 01:11:08', '0');
INSERT INTO `spot` VALUES ('3003004', '5', '回民街', 'https://upload.wikimedia.org/wikipedia/commons/d/d9/Bei_Yuan_Men.JPG', '西安', '本地小吃集中地，适合晚间逛吃。', '34.263700', '108.947100', '150', '10:00', '22:00', '[\"美食\",\"人文街区\",\"西安\"]', '155', '790', '1', '2026-03-23 09:00:00', '2026-03-24 01:11:08', '0');
INSERT INTO `spot` VALUES ('3003005', '5', '华清宫', 'https://upload.wikimedia.org/wikipedia/commons/8/8e/%E5%8D%8E%E6%B8%85%E5%AE%AB.jpg', '西安', '温泉宫苑与历史故事结合。', '34.361900', '109.214500', '180', '08:00', '18:00', '[\"历史文化\",\"园林\",\"西安\"]', '126', '640', '1', '2026-03-23 09:00:00', '2026-03-24 01:11:08', '0');
INSERT INTO `spot` VALUES ('3004001', '5', '宽窄巷子', 'https://upload.wikimedia.org/wikipedia/commons/d/d4/Chengdu_travel_045_%2836150300546%29.jpg', '成都', '成都慢生活代表街区。', '30.667200', '104.055500', '150', '10:00', '22:00', '[\"人文街区\",\"休闲\",\"成都\"]', '163', '820', '1', '2026-03-23 09:00:00', '2026-03-24 01:11:08', '0');
INSERT INTO `spot` VALUES ('3004002', '5', '锦里古街', 'https://upload.wikimedia.org/wikipedia/commons/e/e3/Jinli_Street_35201-Chengdu_%2849068150581%29.jpg', '成都', '川味小吃与民俗表演结合。', '30.650600', '104.043800', '150', '10:00', '22:00', '[\"美食\",\"人文街区\",\"成都\"]', '151', '780', '1', '2026-03-23 09:00:00', '2026-03-24 01:11:08', '0');
INSERT INTO `spot` VALUES ('3004003', '5', '成都大熊猫繁育研究基地', 'https://upload.wikimedia.org/wikipedia/commons/4/4c/Chengdu_Research_Base_Eingang.jpg', '成都', '亲子和萌宠主题首选景点。', '30.739900', '104.152500', '120', '09:00', '18:00', '[\"亲子\",\"自然\",\"成都\"]', '322', '1581', '1', '2026-03-23 09:00:00', '2026-03-24 01:11:08', '0');
INSERT INTO `spot` VALUES ('3004004', '5', '青城山', 'https://upload.wikimedia.org/wikipedia/commons/b/be/%E5%9B%9B%E5%B7%9D%E9%9D%92%E5%9F%8E%E5%B1%B1_-_panoramio.jpg', '成都', '登山与道教文化体验线路。', '30.904600', '103.567800', '240', '07:30', '18:00', '[\"自然风光\",\"爬山\",\"成都\"]', '134', '690', '1', '2026-03-23 09:00:00', '2026-03-24 01:11:08', '0');
INSERT INTO `spot` VALUES ('3004005', '5', '都江堰', 'https://upload.wikimedia.org/wikipedia/commons/d/d2/36661-Dujiangyan_%2844634340644%29.jpg', '成都', '古代水利工程，文化科普价值高。', '30.998400', '103.619900', '240', '07:30', '18:00', '[\"历史文化\",\"世界遗产\",\"成都\"]', '172', '860', '1', '2026-03-23 09:00:00', '2026-03-24 01:11:08', '0');
INSERT INTO `spot` VALUES ('3004006', '5', '春熙路', 'https://upload.wikimedia.org/wikipedia/commons/7/73/Chunxi_Road_seen_from_viaduct%2C_Chengdu.jpg', '成都', '商业步行街，适合夜游与购物。', '30.658700', '104.081700', '150', '10:00', '22:00', '[\"夜游\",\"休闲\",\"成都\"]', '145', '740', '1', '2026-03-23 09:00:00', '2026-03-24 01:11:08', '0');
INSERT INTO `spot` VALUES ('3005001', '5', '外滩', '/spot-images/3005001.jpg', '上海', '黄浦江畔经典天际线与万国建筑群。', '31.240000', '121.490000', '150', '10:00', '22:00', '[\"城市地标\",\"夜景\",\"上海\"]', '188', '980', '1', '2026-03-23 22:20:00', '2026-03-24 01:11:08', '0');
INSERT INTO `spot` VALUES ('3005002', '5', '东方明珠广播电视塔', '/spot-images/3005002.jpg', '上海', '上海核心城市地标，适合俯瞰浦江。', '31.239700', '121.499800', '120', '09:00', '18:00', '[\"城市地标\",\"观景\",\"上海\"]', '172', '910', '1', '2026-03-23 22:20:00', '2026-03-24 01:11:08', '0');
INSERT INTO `spot` VALUES ('3005003', '5', '豫园', '/spot-images/3005003.jpg', '上海', '江南古典园林与老城厢文化代表。', '31.227200', '121.492000', '120', '09:00', '18:00', '[\"园林\",\"历史文化\",\"上海\"]', '136', '760', '1', '2026-03-23 22:20:00', '2026-03-24 01:11:08', '0');
INSERT INTO `spot` VALUES ('3005004', '5', '南京路步行街', '/spot-images/3005004.jpg', '上海', '都市商圈与夜间步行街体验。', '31.234500', '121.475300', '150', '10:00', '22:00', '[\"商业街区\",\"夜游\",\"上海\"]', '149', '821', '1', '2026-03-23 22:20:00', '2026-03-24 01:11:08', '0');
INSERT INTO `spot` VALUES ('3005005', '5', '上海迪士尼度假区', '/spot-images/3005005.jpg', '上海', '亲子及主题乐园出行首选。', '31.144300', '121.657200', '480', '08:30', '21:30', '[\"亲子\",\"主题乐园\",\"上海\"]', '215', '1200', '1', '2026-03-23 22:20:00', '2026-03-24 01:11:08', '0');
INSERT INTO `spot` VALUES ('3006001', '5', '夫子庙', '/spot-images/3006001.jpg', '南京', '秦淮文化核心区域，夜景与美食丰富。', '32.023800', '118.792700', '150', '10:00', '22:00', '[\"历史文化\",\"美食\",\"南京\"]', '168', '860', '1', '2026-03-23 22:20:00', '2026-03-24 01:11:08', '0');
INSERT INTO `spot` VALUES ('3006002', '5', '中山陵', '/spot-images/3006002.jpg', '南京', '近代历史纪念地，台阶登临视野开阔。', '32.060300', '118.850700', '180', '08:00', '18:00', '[\"历史文化\",\"纪念建筑\",\"南京\"]', '159', '830', '1', '2026-03-23 22:20:00', '2026-03-24 01:11:08', '0');
INSERT INTO `spot` VALUES ('3006003', '5', '玄武湖', '/spot-images/3006003.jpg', '南京', '城市湖景公园，适合慢跑与骑行。', '32.075400', '118.792300', '120', '09:00', '18:00', '[\"自然风光\",\"休闲\",\"南京\"]', '125', '700', '1', '2026-03-23 22:20:00', '2026-03-24 01:11:08', '0');
INSERT INTO `spot` VALUES ('3006004', '5', '南京博物院', '/spot-images/3006004.jpg', '南京', '综合性博物馆，文物馆藏丰富。', '32.040100', '118.839400', '180', '08:00', '18:00', '[\"博物馆\",\"历史文化\",\"南京\"]', '142', '771', '1', '2026-03-23 22:20:00', '2026-03-24 01:11:08', '0');
INSERT INTO `spot` VALUES ('3006005', '5', '老门东', '/spot-images/3006005.jpg', '南京', '老城街区更新样本，适合夜游与拍照。', '32.015400', '118.798700', '150', '10:00', '22:00', '[\"人文街区\",\"夜游\",\"南京\"]', '133', '740', '1', '2026-03-23 22:20:00', '2026-03-24 01:11:08', '0');
INSERT INTO `spot` VALUES ('3006011', '5', '明孝陵', '/spot-images/3006002.jpg', '南京', '明代皇家陵寝群核心景点，石象路和神道极具历史氛围。', '32.061900', '118.847300', '180', '07:30', '18:00', '[\"历史文化\",\"陵寝\",\"南京\"]', '158', '845', '1', '2026-03-24 20:30:00', '2026-03-24 20:30:00', '0');
INSERT INTO `spot` VALUES ('3006012', '5', '南京总统府', '/spot-images/3006004.jpg', '南京', '兼具近代历史与民国建筑特色，是南京城市历史叙事的重要节点。', '32.043400', '118.792600', '150', '08:30', '18:00', '[\"历史文化\",\"民国建筑\",\"南京\"]', '172', '920', '1', '2026-03-24 20:30:00', '2026-03-24 20:30:00', '0');
INSERT INTO `spot` VALUES ('3006013', '5', '鸡鸣寺', '/spot-images/3006001.jpg', '南京', '南京古寺代表，春季樱花和登高视野体验突出。', '32.064500', '118.792900', '120', '07:30', '17:30', '[\"寺庙\",\"历史文化\",\"南京\"]', '136', '760', '1', '2026-03-24 20:30:00', '2026-03-24 20:30:00', '0');
INSERT INTO `spot` VALUES ('3006014', '5', '牛首山文化旅游区', '/spot-images/3006002.jpg', '南京', '集佛教文化、山地景观和大型建筑群于一体，适合深度游。', '31.949800', '118.721000', '240', '08:30', '17:30', '[\"自然风光\",\"佛教文化\",\"南京\"]', '169', '890', '1', '2026-03-24 20:30:00', '2026-03-24 20:30:00', '0');
INSERT INTO `spot` VALUES ('3006015', '5', '雨花台风景区', '/spot-images/3006002.jpg', '南京', '兼具纪念意义与山地公园属性，适合半日游。', '31.995600', '118.785600', '150', '08:00', '17:30', '[\"纪念地\",\"自然风光\",\"南京\"]', '124', '680', '1', '2026-03-24 20:30:00', '2026-03-24 20:30:00', '0');
INSERT INTO `spot` VALUES ('3006016', '5', '侵华日军南京大屠杀遇难同胞纪念馆', '/spot-images/3006004.jpg', '南京', '重要的爱国主义教育与历史纪念场馆，适合沉浸式参观。', '32.035100', '118.748800', '180', '08:30', '16:30', '[\"纪念馆\",\"历史文化\",\"南京\"]', '181', '970', '1', '2026-03-24 20:30:00', '2026-03-24 20:30:00', '0');
INSERT INTO `spot` VALUES ('3006017', '5', '南京城墙中华门段', '/spot-images/3006005.jpg', '南京', '明城墙代表段落，适合俯瞰古城格局与傍晚散步。', '32.008300', '118.781800', '120', '08:30', '21:00', '[\"历史文化\",\"城墙\",\"南京\"]', '143', '780', '1', '2026-03-24 20:30:00', '2026-03-24 20:30:00', '0');
INSERT INTO `spot` VALUES ('3006018', '5', '红山森林动物园', '/spot-images/3006003.jpg', '南京', '亲子出行热门景区，步行动线丰富，适合放慢节奏游玩。', '32.094900', '118.807600', '210', '08:30', '17:00', '[\"亲子\",\"动物园\",\"南京\"]', '165', '910', '1', '2026-03-24 20:30:00', '2026-03-24 20:30:00', '0');
INSERT INTO `spot` VALUES ('3006019', '5', '紫峰大厦观景台', '/spot-images/3006005.jpg', '南京', '城市高空观景点，适合傍晚和夜景时段安排。', '32.061200', '118.778900', '90', '10:00', '22:00', '[\"观景\",\"夜游\",\"南京\"]', '151', '835', '1', '2026-03-24 20:30:00', '2026-03-24 20:30:00', '0');
INSERT INTO `spot` VALUES ('3006020', '5', '南京眼步行桥', '/spot-images/3006005.jpg', '南京', '滨江夜景和城市地标式步行桥，适合夜游收尾。', '31.988900', '118.682800', '90', '10:00', '22:30', '[\"夜游\",\"城市地标\",\"南京\"]', '139', '805', '1', '2026-03-24 20:30:00', '2026-03-24 20:30:00', '0');
INSERT INTO `spot` VALUES ('3007001', '5', '洪崖洞', '/spot-images/3007001.jpg', '重庆', '山城吊脚楼夜景地标，灯光效果突出。', '29.563000', '106.580000', '150', '10:00', '22:00', '[\"夜景\",\"城市地标\",\"重庆\"]', '221', '1250', '1', '2026-03-23 22:20:00', '2026-03-24 01:11:08', '0');
INSERT INTO `spot` VALUES ('3007002', '5', '解放碑', '/spot-images/3007002.jpg', '重庆', '重庆商圈核心，交通便捷。', '29.558300', '106.577100', '150', '10:00', '22:00', '[\"城市地标\",\"商业街区\",\"重庆\"]', '145', '780', '1', '2026-03-23 22:20:00', '2026-03-24 01:11:08', '0');
INSERT INTO `spot` VALUES ('3007003', '5', '长江索道', '/spot-images/3007003.jpg', '重庆', '跨江交通体验项目，山城特色鲜明。', '29.554100', '106.570300', '150', '10:00', '22:00', '[\"特色交通\",\"观景\",\"重庆\"]', '171', '890', '1', '2026-03-23 22:20:00', '2026-03-24 01:11:08', '0');
INSERT INTO `spot` VALUES ('3007004', '5', '磁器口古镇', '/spot-images/3007004.jpg', '重庆', '传统古镇街巷与本地小吃体验。', '29.581400', '106.446700', '150', '10:00', '22:00', '[\"古镇\",\"美食\",\"重庆\"]', '152', '810', '1', '2026-03-23 22:20:00', '2026-03-24 01:11:08', '0');
INSERT INTO `spot` VALUES ('3007005', '5', '武隆天生三桥', '/spot-images/3007005.jpg', '重庆', '喀斯特地貌奇观，适合自然风光游。', '29.401300', '107.756700', '240', '07:30', '18:00', '[\"自然风光\",\"世界自然遗产\",\"重庆\"]', '187', '960', '1', '2026-03-23 22:20:00', '2026-03-24 01:11:08', '0');
INSERT INTO `spot` VALUES ('3008001', '5', '拙政园', '/spot-images/3008001.jpg', '苏州', '苏州园林代表，江南文化底蕴浓厚。', '31.326800', '120.631000', '180', '08:00', '18:00', '[\"园林\",\"历史文化\",\"苏州\"]', '176', '920', '1', '2026-03-23 22:20:00', '2026-03-24 01:11:08', '0');
INSERT INTO `spot` VALUES ('3008002', '5', '平江路', '/spot-images/3008002.jpg', '苏州', '古城水巷步行街，适合慢游。', '31.317300', '120.632500', '150', '10:00', '22:00', '[\"古街\",\"休闲\",\"苏州\"]', '141', '760', '1', '2026-03-23 22:20:00', '2026-03-24 01:11:08', '0');
INSERT INTO `spot` VALUES ('3008003', '5', '寒山寺', '/spot-images/3008003.jpg', '苏州', '江南名寺，文化典故知名度高。', '31.302100', '120.554500', '120', '09:00', '18:00', '[\"寺庙\",\"历史文化\",\"苏州\"]', '128', '700', '1', '2026-03-23 22:20:00', '2026-03-24 01:11:08', '0');
INSERT INTO `spot` VALUES ('3008004', '5', '周庄古镇', '/spot-images/3008004.jpg', '苏州', '经典水乡古镇，游船体验丰富。', '31.117800', '120.844100', '150', '10:00', '22:00', '[\"古镇\",\"水乡\",\"苏州\"]', '163', '840', '1', '2026-03-23 22:20:00', '2026-03-24 01:11:08', '0');
INSERT INTO `spot` VALUES ('3008005', '5', '金鸡湖', '/spot-images/3008005.jpg', '苏州', '现代城市湖区，夜跑休闲热门。', '31.321600', '120.734600', '150', '10:00', '22:00', '[\"湖泊\",\"城市休闲\",\"苏州\"]', '119', '650', '1', '2026-03-23 22:20:00', '2026-03-24 01:11:08', '0');
INSERT INTO `spot` VALUES ('3009001', '5', '漓江', '/spot-images/3009001.jpg', '桂林', '山水甲天下代表景观，游船体验经典。', '25.290300', '110.295000', '120', '09:00', '18:00', '[\"自然风光\",\"山水\",\"桂林\"]', '224', '1260', '1', '2026-03-23 22:20:00', '2026-03-24 01:11:08', '0');
INSERT INTO `spot` VALUES ('3009002', '5', '象鼻山', '/spot-images/3009002.jpg', '桂林', '桂林城市名片，适合半日游。', '25.273600', '110.292400', '120', '09:00', '18:00', '[\"自然地标\",\"山水\",\"桂林\"]', '157', '820', '1', '2026-03-23 22:20:00', '2026-03-24 01:11:08', '0');
INSERT INTO `spot` VALUES ('3009003', '5', '阳朔西街', '/spot-images/3009003.jpg', '桂林', '中外游客聚集的休闲街区。', '24.778300', '110.496600', '150', '10:00', '22:00', '[\"人文街区\",\"夜游\",\"桂林\"]', '147', '790', '1', '2026-03-23 22:20:00', '2026-03-24 01:11:08', '0');
INSERT INTO `spot` VALUES ('3009004', '5', '龙脊梯田', '/spot-images/3009004.jpg', '桂林', '梯田景观层次丰富，摄影热门。', '25.754000', '110.011000', '240', '07:30', '18:00', '[\"自然风光\",\"摄影\",\"桂林\"]', '169', '880', '1', '2026-03-23 22:20:00', '2026-03-24 01:11:08', '0');
INSERT INTO `spot` VALUES ('3009005', '5', '两江四湖', '/spot-images/3009005.jpg', '桂林', '城市水系夜游项目，适合晚间安排。', '25.279000', '110.290500', '150', '10:00', '22:00', '[\"夜游\",\"水系景观\",\"桂林\"]', '134', '720', '1', '2026-03-23 22:20:00', '2026-03-24 01:11:08', '0');

-- ----------------------------
-- Table structure for `spot_bak_20260323`
-- ----------------------------
DROP TABLE IF EXISTS `spot_bak_20260323`;
CREATE TABLE `spot_bak_20260323` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `adminId` bigint NOT NULL COMMENT '管理员 ID（某人创建该景点）',
  `spotName` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '景点名',
  `spotAvatar` varchar(1024) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '景点封面图',
  `spotLocation` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '景点所在地',
  `spotDescription` varchar(4096) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '景点介绍',
  `spotTags` varchar(2048) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '景点标签（JSON字符串数组）',
  `favourNum` int NOT NULL DEFAULT '0' COMMENT '收藏量',
  `viewNum` int NOT NULL DEFAULT '0' COMMENT '浏览量',
  `spotStatus` tinyint NOT NULL DEFAULT '0' COMMENT '景点状态（1开放，0关闭，默认关闭）',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3004007 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of spot_bak_20260323
-- ----------------------------
INSERT INTO `spot_bak_20260323` VALUES ('3001001', '5', '故宫', 'https://upload.wikimedia.org/wikipedia/commons/a/a7/Forbidden_City_Beijing_Shenwumen_Gate.JPG', '北京', '明清皇家宫殿群，适合历史文化深度游。', '[\"历史文化\",\"宫殿\",\"北京\"]', '186', '920', '1', '2026-03-23 09:00:00', '2026-03-23 09:00:00', '0');
INSERT INTO `spot_bak_20260323` VALUES ('3001002', '5', '颐和园', 'https://upload.wikimedia.org/wikipedia/commons/0/06/%E9%A2%90%E5%92%8C%E5%9B%AD%E4%B8%87%E5%AF%BF%E5%B1%B1%E4%BD%9B%E9%A6%99%E9%98%81.jpg', '北京', '皇家园林代表，适合慢游与拍照。', '[\"园林\",\"休闲\",\"北京\"]', '142', '760', '1', '2026-03-23 09:00:00', '2026-03-23 09:00:00', '0');
INSERT INTO `spot_bak_20260323` VALUES ('3001003', '5', '八达岭长城', 'https://upload.wikimedia.org/wikipedia/commons/c/c4/GreatWall_Badaling.jpg', '北京', '长城经典段，交通便利，适合首次打卡。', '[\"历史文化\",\"世界遗产\",\"北京\"]', '265', '1180', '1', '2026-03-23 09:00:00', '2026-03-23 09:00:00', '0');
INSERT INTO `spot_bak_20260323` VALUES ('3001004', '5', '天坛公园', 'https://upload.wikimedia.org/wikipedia/commons/5/5f/Temple_of_Heaven_20160323_01.jpg', '北京', '祭天建筑群与古树园林结合，文化氛围浓。', '[\"历史文化\",\"公园\",\"北京\"]', '98', '620', '1', '2026-03-23 09:00:00', '2026-03-23 09:00:00', '0');
INSERT INTO `spot_bak_20260323` VALUES ('3001005', '5', '南锣鼓巷', 'https://upload.wikimedia.org/wikipedia/commons/7/73/View_down_Nanluoguxiang_2011.jpg', '北京', '胡同街区与小吃文化体验地。', '[\"人文街区\",\"美食\",\"北京\"]', '173', '840', '1', '2026-03-23 09:00:00', '2026-03-23 09:00:00', '0');
INSERT INTO `spot_bak_20260323` VALUES ('3001006', '5', '什刹海', 'https://upload.wikimedia.org/wikipedia/commons/9/95/Shishahai.jpg', '北京', '湖区夜景与酒吧街，适合傍晚游玩。', '[\"夜游\",\"休闲\",\"北京\"]', '132', '690', '1', '2026-03-23 09:00:00', '2026-03-23 09:00:00', '0');
INSERT INTO `spot_bak_20260323` VALUES ('3002001', '5', '西湖', 'https://upload.wikimedia.org/wikipedia/commons/1/17/West_Lake%2C_Hangzhou_2025.jpg', '杭州', '杭州核心景区，四季皆可游。', '[\"自然风光\",\"湖泊\",\"杭州\"]', '281', '1300', '1', '2026-03-23 09:00:00', '2026-03-23 09:00:00', '0');
INSERT INTO `spot_bak_20260323` VALUES ('3002002', '5', '灵隐寺', 'https://upload.wikimedia.org/wikipedia/commons/c/c2/Hangzhou_Lingyin-Temple_20161003.jpg', '杭州', '千年古刹，文化底蕴深厚。', '[\"历史文化\",\"寺庙\",\"杭州\"]', '167', '860', '1', '2026-03-23 09:00:00', '2026-03-23 09:00:00', '0');
INSERT INTO `spot_bak_20260323` VALUES ('3002003', '5', '西溪湿地', 'https://upload.wikimedia.org/wikipedia/commons/2/20/Xixi_Wetland_Park%2C_Hangzhou%2C_China.jpg', '杭州', '湿地生态景观，适合亲子与慢游。', '[\"自然风光\",\"亲子\",\"杭州\"]', '121', '610', '1', '2026-03-23 09:00:00', '2026-03-23 09:00:00', '0');
INSERT INTO `spot_bak_20260323` VALUES ('3002004', '5', '河坊街', 'https://upload.wikimedia.org/wikipedia/commons/9/99/Hefang_Street.jpg', '杭州', '古街步行区，适合美食与夜游。', '[\"人文街区\",\"美食\",\"杭州\"]', '149', '735', '1', '2026-03-23 09:00:00', '2026-03-23 09:00:00', '0');
INSERT INTO `spot_bak_20260323` VALUES ('3002005', '5', '千岛湖', 'https://upload.wikimedia.org/wikipedia/commons/6/6f/Thousand_Island_Lake.JPG', '杭州', '湖岛景观丰富，适合1日外圈线路。', '[\"自然风光\",\"湖泊\",\"杭州\"]', '138', '700', '1', '2026-03-23 09:00:00', '2026-03-23 09:00:00', '0');
INSERT INTO `spot_bak_20260323` VALUES ('3003001', '5', '秦始皇兵马俑', 'https://upload.wikimedia.org/wikipedia/commons/8/88/51714-Terracota-Army.jpg', '西安', '世界级考古遗址，历史价值极高。', '[\"历史文化\",\"博物馆\",\"西安\"]', '294', '1410', '1', '2026-03-23 09:00:00', '2026-03-23 09:00:00', '0');
INSERT INTO `spot_bak_20260323` VALUES ('3003002', '5', '西安城墙', 'https://upload.wikimedia.org/wikipedia/commons/3/32/City_wall_of_Xi%27an_51550-Xian_%2827959363326%29.jpg', '西安', '可骑行环城，视角独特。', '[\"历史文化\",\"骑行\",\"西安\"]', '183', '910', '1', '2026-03-23 09:00:00', '2026-03-23 09:00:00', '0');
INSERT INTO `spot_bak_20260323` VALUES ('3003003', '5', '大雁塔', 'https://upload.wikimedia.org/wikipedia/commons/1/13/Giant_Wild_Goose_Pagoda.jpg', '西安', '唐文化地标，夜景与喷泉演出热门。', '[\"历史文化\",\"夜游\",\"西安\"]', '176', '845', '1', '2026-03-23 09:00:00', '2026-03-23 09:00:00', '0');
INSERT INTO `spot_bak_20260323` VALUES ('3003004', '5', '回民街', 'https://upload.wikimedia.org/wikipedia/commons/d/d9/Bei_Yuan_Men.JPG', '西安', '本地小吃集中地，适合晚间逛吃。', '[\"美食\",\"人文街区\",\"西安\"]', '155', '790', '1', '2026-03-23 09:00:00', '2026-03-23 09:00:00', '0');
INSERT INTO `spot_bak_20260323` VALUES ('3003005', '5', '华清宫', 'https://upload.wikimedia.org/wikipedia/commons/8/8e/%E5%8D%8E%E6%B8%85%E5%AE%AB.jpg', '西安', '温泉宫苑与历史故事结合。', '[\"历史文化\",\"园林\",\"西安\"]', '126', '640', '1', '2026-03-23 09:00:00', '2026-03-23 09:00:00', '0');
INSERT INTO `spot_bak_20260323` VALUES ('3004001', '5', '宽窄巷子', 'https://upload.wikimedia.org/wikipedia/commons/d/d4/Chengdu_travel_045_%2836150300546%29.jpg', '成都', '成都慢生活代表街区。', '[\"人文街区\",\"休闲\",\"成都\"]', '163', '820', '1', '2026-03-23 09:00:00', '2026-03-23 09:00:00', '0');
INSERT INTO `spot_bak_20260323` VALUES ('3004002', '5', '锦里古街', 'https://upload.wikimedia.org/wikipedia/commons/e/e3/Jinli_Street_35201-Chengdu_%2849068150581%29.jpg', '成都', '川味小吃与民俗表演结合。', '[\"美食\",\"人文街区\",\"成都\"]', '151', '780', '1', '2026-03-23 09:00:00', '2026-03-23 09:00:00', '0');
INSERT INTO `spot_bak_20260323` VALUES ('3004003', '5', '成都大熊猫繁育研究基地', 'https://upload.wikimedia.org/wikipedia/commons/4/4c/Chengdu_Research_Base_Eingang.jpg', '成都', '亲子和萌宠主题首选景点。', '[\"亲子\",\"自然\",\"成都\"]', '322', '1580', '1', '2026-03-23 09:00:00', '2026-03-23 09:00:00', '0');
INSERT INTO `spot_bak_20260323` VALUES ('3004004', '5', '青城山', 'https://upload.wikimedia.org/wikipedia/commons/b/be/%E5%9B%9B%E5%B7%9D%E9%9D%92%E5%9F%8E%E5%B1%B1_-_panoramio.jpg', '成都', '登山与道教文化体验线路。', '[\"自然风光\",\"爬山\",\"成都\"]', '134', '690', '1', '2026-03-23 09:00:00', '2026-03-23 09:00:00', '0');
INSERT INTO `spot_bak_20260323` VALUES ('3004005', '5', '都江堰', 'https://upload.wikimedia.org/wikipedia/commons/d/d2/36661-Dujiangyan_%2844634340644%29.jpg', '成都', '古代水利工程，文化科普价值高。', '[\"历史文化\",\"世界遗产\",\"成都\"]', '172', '860', '1', '2026-03-23 09:00:00', '2026-03-23 09:00:00', '0');
INSERT INTO `spot_bak_20260323` VALUES ('3004006', '5', '春熙路', 'https://upload.wikimedia.org/wikipedia/commons/7/73/Chunxi_Road_seen_from_viaduct%2C_Chengdu.jpg', '成都', '商业步行街，适合夜游与购物。', '[\"夜游\",\"休闲\",\"成都\"]', '145', '740', '1', '2026-03-23 09:00:00', '2026-03-23 09:00:00', '0');

-- ----------------------------
-- Table structure for `spot_fee`
-- ----------------------------
DROP TABLE IF EXISTS `spot_fee`;
CREATE TABLE `spot_fee` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `spotId` bigint NOT NULL COMMENT '景点id',
  `adminId` bigint NOT NULL COMMENT '管理员ID',
  `spotFeeDescription` varchar(1024) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '门票描述',
  `spotFeePrice` decimal(10,2) NOT NULL COMMENT '门票价格',
  `spotFeeNumber` int DEFAULT '0' COMMENT '景点门票数量',
  `spotFeeStatus` tinyint NOT NULL DEFAULT '0' COMMENT '门票可用状态 1可用 0不可用 默认 0',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `spotIdIndex` (`spotId`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3109006 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of spot_fee
-- ----------------------------
INSERT INTO `spot_fee` VALUES ('3101001', '3001001', '5', '故宫标准门票', '60.00', '5000', '1', '2026-03-23 09:10:00', '2026-03-23 09:10:00', '0');
INSERT INTO `spot_fee` VALUES ('3101002', '3001002', '5', '颐和园标准门票', '30.00', '5000', '1', '2026-03-23 09:10:00', '2026-03-23 09:10:00', '0');
INSERT INTO `spot_fee` VALUES ('3101003', '3001003', '5', '八达岭长城门票', '45.00', '5000', '1', '2026-03-23 09:10:00', '2026-03-23 09:10:00', '0');
INSERT INTO `spot_fee` VALUES ('3101004', '3001004', '5', '天坛公园门票', '35.00', '5000', '1', '2026-03-23 09:10:00', '2026-03-23 09:10:00', '0');
INSERT INTO `spot_fee` VALUES ('3101005', '3001005', '5', '南锣鼓巷游览票', '20.00', '5000', '1', '2026-03-23 09:10:00', '2026-03-23 09:10:00', '0');
INSERT INTO `spot_fee` VALUES ('3101006', '3001006', '5', '什刹海游船票', '50.00', '5000', '1', '2026-03-23 09:10:00', '2026-03-23 09:10:00', '0');
INSERT INTO `spot_fee` VALUES ('3102001', '3002001', '5', '西湖景区联票', '55.00', '5000', '1', '2026-03-23 09:10:00', '2026-03-23 09:10:00', '0');
INSERT INTO `spot_fee` VALUES ('3102002', '3002002', '5', '灵隐寺门票', '45.00', '5000', '1', '2026-03-23 09:10:00', '2026-03-23 09:10:00', '0');
INSERT INTO `spot_fee` VALUES ('3102003', '3002003', '5', '西溪湿地门票', '80.00', '5000', '1', '2026-03-23 09:10:00', '2026-03-23 09:10:00', '0');
INSERT INTO `spot_fee` VALUES ('3102004', '3002004', '5', '河坊街体验票', '25.00', '5000', '1', '2026-03-23 09:10:00', '2026-03-23 09:10:00', '0');
INSERT INTO `spot_fee` VALUES ('3102005', '3002005', '5', '千岛湖游船票', '120.00', '5000', '1', '2026-03-23 09:10:00', '2026-03-23 09:10:00', '0');
INSERT INTO `spot_fee` VALUES ('3103001', '3003001', '5', '兵马俑门票', '120.00', '5000', '1', '2026-03-23 09:10:00', '2026-03-23 09:10:00', '0');
INSERT INTO `spot_fee` VALUES ('3103002', '3003002', '5', '西安城墙门票', '54.00', '5000', '1', '2026-03-23 09:10:00', '2026-03-23 09:10:00', '0');
INSERT INTO `spot_fee` VALUES ('3103003', '3003003', '5', '大雁塔门票', '40.00', '5000', '1', '2026-03-23 09:10:00', '2026-03-23 09:10:00', '0');
INSERT INTO `spot_fee` VALUES ('3103004', '3003004', '5', '回民街美食通票', '30.00', '5000', '1', '2026-03-23 09:10:00', '2026-03-23 09:10:00', '0');
INSERT INTO `spot_fee` VALUES ('3103005', '3003005', '5', '华清宫门票', '110.00', '5000', '1', '2026-03-23 09:10:00', '2026-03-23 09:10:00', '0');
INSERT INTO `spot_fee` VALUES ('3104001', '3004001', '5', '宽窄巷子城市体验票', '20.00', '5000', '1', '2026-03-23 09:10:00', '2026-03-23 09:10:00', '0');
INSERT INTO `spot_fee` VALUES ('3104002', '3004002', '5', '锦里古街文化票', '20.00', '5000', '1', '2026-03-23 09:10:00', '2026-03-23 09:10:00', '0');
INSERT INTO `spot_fee` VALUES ('3104003', '3004003', '5', '熊猫基地门票', '58.00', '5000', '1', '2026-03-23 09:10:00', '2026-03-23 09:10:00', '0');
INSERT INTO `spot_fee` VALUES ('3104004', '3004004', '5', '青城山门票', '80.00', '5000', '1', '2026-03-23 09:10:00', '2026-03-23 09:10:00', '0');
INSERT INTO `spot_fee` VALUES ('3104005', '3004005', '5', '都江堰门票', '80.00', '5000', '1', '2026-03-23 09:10:00', '2026-03-23 09:10:00', '0');
INSERT INTO `spot_fee` VALUES ('3104006', '3004006', '5', '春熙路夜游券', '15.00', '5000', '1', '2026-03-23 09:10:00', '2026-03-23 09:10:00', '0');
INSERT INTO `spot_fee` VALUES ('3105001', '3005001', '5', '外滩城市观光票', '20.00', '5000', '1', '2026-03-23 22:25:00', '2026-03-23 22:25:00', '0');
INSERT INTO `spot_fee` VALUES ('3105002', '3005002', '5', '东方明珠观景票', '199.00', '5000', '1', '2026-03-23 22:25:00', '2026-03-23 22:25:00', '0');
INSERT INTO `spot_fee` VALUES ('3105003', '3005003', '5', '豫园门票', '40.00', '5000', '1', '2026-03-23 22:25:00', '2026-03-23 22:25:00', '0');
INSERT INTO `spot_fee` VALUES ('3105004', '3005004', '5', '南京路城市体验票', '15.00', '5000', '1', '2026-03-23 22:25:00', '2026-03-23 22:25:00', '0');
INSERT INTO `spot_fee` VALUES ('3105005', '3005005', '5', '上海迪士尼一日票', '475.00', '5000', '1', '2026-03-23 22:25:00', '2026-03-23 22:25:00', '0');
INSERT INTO `spot_fee` VALUES ('3106001', '3006001', '5', '夫子庙秦淮游览票', '30.00', '5000', '1', '2026-03-23 22:25:00', '2026-03-23 22:25:00', '0');
INSERT INTO `spot_fee` VALUES ('3106002', '3006002', '5', '中山陵景区门票', '60.00', '5000', '1', '2026-03-23 22:25:00', '2026-03-23 22:25:00', '0');
INSERT INTO `spot_fee` VALUES ('3106003', '3006003', '5', '玄武湖环湖票', '25.00', '5000', '1', '2026-03-23 22:25:00', '2026-03-23 22:25:00', '0');
INSERT INTO `spot_fee` VALUES ('3106004', '3006004', '5', '南京博物院展馆票', '50.00', '5000', '1', '2026-03-23 22:25:00', '2026-03-23 22:25:00', '0');
INSERT INTO `spot_fee` VALUES ('3106005', '3006005', '5', '老门东街区体验票', '20.00', '5000', '1', '2026-03-23 22:25:00', '2026-03-23 22:25:00', '0');
INSERT INTO `spot_fee` VALUES ('3106011', '3006011', '5', '明孝陵门票', '70.00', '5000', '1', '2026-03-24 20:35:00', '2026-03-24 20:35:00', '0');
INSERT INTO `spot_fee` VALUES ('3106012', '3006012', '5', '南京总统府门票', '40.00', '5000', '1', '2026-03-24 20:35:00', '2026-03-24 20:35:00', '0');
INSERT INTO `spot_fee` VALUES ('3106013', '3006013', '5', '鸡鸣寺门票', '15.00', '5000', '1', '2026-03-24 20:35:00', '2026-03-24 20:35:00', '0');
INSERT INTO `spot_fee` VALUES ('3106014', '3006014', '5', '牛首山文化旅游区门票', '98.00', '5000', '1', '2026-03-24 20:35:00', '2026-03-24 20:35:00', '0');
INSERT INTO `spot_fee` VALUES ('3106015', '3006015', '5', '雨花台风景区门票', '20.00', '5000', '1', '2026-03-24 20:35:00', '2026-03-24 20:35:00', '0');
INSERT INTO `spot_fee` VALUES ('3106016', '3006016', '5', '南京大屠杀纪念馆预约服务票', '0.00', '5000', '1', '2026-03-24 20:35:00', '2026-03-24 20:35:00', '0');
INSERT INTO `spot_fee` VALUES ('3106017', '3006017', '5', '中华门城堡门票', '50.00', '5000', '1', '2026-03-24 20:35:00', '2026-03-24 20:35:00', '0');
INSERT INTO `spot_fee` VALUES ('3106018', '3006018', '5', '红山森林动物园门票', '40.00', '5000', '1', '2026-03-24 20:35:00', '2026-03-24 20:35:00', '0');
INSERT INTO `spot_fee` VALUES ('3106019', '3006019', '5', '紫峰大厦观景票', '88.00', '5000', '1', '2026-03-24 20:35:00', '2026-03-24 20:35:00', '0');
INSERT INTO `spot_fee` VALUES ('3106020', '3006020', '5', '南京眼步行桥体验票', '0.00', '5000', '1', '2026-03-24 20:35:00', '2026-03-24 20:35:00', '0');
INSERT INTO `spot_fee` VALUES ('3107001', '3007001', '5', '洪崖洞夜景观光票', '35.00', '5000', '1', '2026-03-23 22:25:00', '2026-03-23 22:25:00', '0');
INSERT INTO `spot_fee` VALUES ('3107002', '3007002', '5', '解放碑城市体验票', '15.00', '5000', '1', '2026-03-23 22:25:00', '2026-03-23 22:25:00', '0');
INSERT INTO `spot_fee` VALUES ('3107003', '3007003', '5', '长江索道往返票', '30.00', '5000', '1', '2026-03-23 22:25:00', '2026-03-23 22:25:00', '0');
INSERT INTO `spot_fee` VALUES ('3107004', '3007004', '5', '磁器口古镇游览票', '20.00', '5000', '1', '2026-03-23 22:25:00', '2026-03-23 22:25:00', '0');
INSERT INTO `spot_fee` VALUES ('3107005', '3007005', '5', '武隆天生三桥门票', '125.00', '5000', '1', '2026-03-23 22:25:00', '2026-03-23 22:25:00', '0');
INSERT INTO `spot_fee` VALUES ('3108001', '3008001', '5', '拙政园门票', '80.00', '5000', '1', '2026-03-23 22:25:00', '2026-03-23 22:25:00', '0');
INSERT INTO `spot_fee` VALUES ('3108002', '3008002', '5', '平江路古街体验票', '20.00', '5000', '1', '2026-03-23 22:25:00', '2026-03-23 22:25:00', '0');
INSERT INTO `spot_fee` VALUES ('3108003', '3008003', '5', '寒山寺门票', '30.00', '5000', '1', '2026-03-23 22:25:00', '2026-03-23 22:25:00', '0');
INSERT INTO `spot_fee` VALUES ('3108004', '3008004', '5', '周庄古镇联票', '100.00', '5000', '1', '2026-03-23 22:25:00', '2026-03-23 22:25:00', '0');
INSERT INTO `spot_fee` VALUES ('3108005', '3008005', '5', '金鸡湖游船票', '40.00', '5000', '1', '2026-03-23 22:25:00', '2026-03-23 22:25:00', '0');
INSERT INTO `spot_fee` VALUES ('3109001', '3009001', '5', '漓江游船票', '210.00', '5000', '1', '2026-03-23 22:25:00', '2026-03-23 22:25:00', '0');
INSERT INTO `spot_fee` VALUES ('3109002', '3009002', '5', '象鼻山门票', '70.00', '5000', '1', '2026-03-23 22:25:00', '2026-03-23 22:25:00', '0');
INSERT INTO `spot_fee` VALUES ('3109003', '3009003', '5', '阳朔西街体验票', '20.00', '5000', '1', '2026-03-23 22:25:00', '2026-03-23 22:25:00', '0');
INSERT INTO `spot_fee` VALUES ('3109004', '3009004', '5', '龙脊梯田门票', '80.00', '5000', '1', '2026-03-23 22:25:00', '2026-03-23 22:25:00', '0');
INSERT INTO `spot_fee` VALUES ('3109005', '3009005', '5', '两江四湖夜游票', '120.00', '5000', '1', '2026-03-23 22:25:00', '2026-03-23 22:25:00', '0');

-- ----------------------------
-- Table structure for `spot_fee_bak_20260323`
-- ----------------------------
DROP TABLE IF EXISTS `spot_fee_bak_20260323`;
CREATE TABLE `spot_fee_bak_20260323` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `spotId` bigint NOT NULL COMMENT '景点id',
  `adminId` bigint NOT NULL COMMENT '管理员ID',
  `spotFeeDescription` varchar(1024) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '门票描述',
  `spotFeePrice` decimal(10,2) NOT NULL COMMENT '门票价格',
  `spotFeeNumber` int DEFAULT '0' COMMENT '景点门票数量',
  `spotFeeStatus` tinyint NOT NULL DEFAULT '0' COMMENT '门票可用状态 1可用 0不可用 默认 0',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `spotIdIndex` (`spotId`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3104007 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of spot_fee_bak_20260323
-- ----------------------------
INSERT INTO `spot_fee_bak_20260323` VALUES ('3101001', '3001001', '5', '故宫标准门票', '60.00', '5000', '1', '2026-03-23 09:10:00', '2026-03-23 09:10:00', '0');
INSERT INTO `spot_fee_bak_20260323` VALUES ('3101002', '3001002', '5', '颐和园标准门票', '30.00', '5000', '1', '2026-03-23 09:10:00', '2026-03-23 09:10:00', '0');
INSERT INTO `spot_fee_bak_20260323` VALUES ('3101003', '3001003', '5', '八达岭长城门票', '45.00', '5000', '1', '2026-03-23 09:10:00', '2026-03-23 09:10:00', '0');
INSERT INTO `spot_fee_bak_20260323` VALUES ('3101004', '3001004', '5', '天坛公园门票', '35.00', '5000', '1', '2026-03-23 09:10:00', '2026-03-23 09:10:00', '0');
INSERT INTO `spot_fee_bak_20260323` VALUES ('3101005', '3001005', '5', '南锣鼓巷游览票', '20.00', '5000', '1', '2026-03-23 09:10:00', '2026-03-23 09:10:00', '0');
INSERT INTO `spot_fee_bak_20260323` VALUES ('3101006', '3001006', '5', '什刹海游船票', '50.00', '5000', '1', '2026-03-23 09:10:00', '2026-03-23 09:10:00', '0');
INSERT INTO `spot_fee_bak_20260323` VALUES ('3102001', '3002001', '5', '西湖景区联票', '55.00', '5000', '1', '2026-03-23 09:10:00', '2026-03-23 09:10:00', '0');
INSERT INTO `spot_fee_bak_20260323` VALUES ('3102002', '3002002', '5', '灵隐寺门票', '45.00', '5000', '1', '2026-03-23 09:10:00', '2026-03-23 09:10:00', '0');
INSERT INTO `spot_fee_bak_20260323` VALUES ('3102003', '3002003', '5', '西溪湿地门票', '80.00', '5000', '1', '2026-03-23 09:10:00', '2026-03-23 09:10:00', '0');
INSERT INTO `spot_fee_bak_20260323` VALUES ('3102004', '3002004', '5', '河坊街体验票', '25.00', '5000', '1', '2026-03-23 09:10:00', '2026-03-23 09:10:00', '0');
INSERT INTO `spot_fee_bak_20260323` VALUES ('3102005', '3002005', '5', '千岛湖游船票', '120.00', '5000', '1', '2026-03-23 09:10:00', '2026-03-23 09:10:00', '0');
INSERT INTO `spot_fee_bak_20260323` VALUES ('3103001', '3003001', '5', '兵马俑门票', '120.00', '5000', '1', '2026-03-23 09:10:00', '2026-03-23 09:10:00', '0');
INSERT INTO `spot_fee_bak_20260323` VALUES ('3103002', '3003002', '5', '西安城墙门票', '54.00', '5000', '1', '2026-03-23 09:10:00', '2026-03-23 09:10:00', '0');
INSERT INTO `spot_fee_bak_20260323` VALUES ('3103003', '3003003', '5', '大雁塔门票', '40.00', '5000', '1', '2026-03-23 09:10:00', '2026-03-23 09:10:00', '0');
INSERT INTO `spot_fee_bak_20260323` VALUES ('3103004', '3003004', '5', '回民街美食通票', '30.00', '5000', '1', '2026-03-23 09:10:00', '2026-03-23 09:10:00', '0');
INSERT INTO `spot_fee_bak_20260323` VALUES ('3103005', '3003005', '5', '华清宫门票', '110.00', '5000', '1', '2026-03-23 09:10:00', '2026-03-23 09:10:00', '0');
INSERT INTO `spot_fee_bak_20260323` VALUES ('3104001', '3004001', '5', '宽窄巷子城市体验票', '20.00', '5000', '1', '2026-03-23 09:10:00', '2026-03-23 09:10:00', '0');
INSERT INTO `spot_fee_bak_20260323` VALUES ('3104002', '3004002', '5', '锦里古街文化票', '20.00', '5000', '1', '2026-03-23 09:10:00', '2026-03-23 09:10:00', '0');
INSERT INTO `spot_fee_bak_20260323` VALUES ('3104003', '3004003', '5', '熊猫基地门票', '58.00', '5000', '1', '2026-03-23 09:10:00', '2026-03-23 09:10:00', '0');
INSERT INTO `spot_fee_bak_20260323` VALUES ('3104004', '3004004', '5', '青城山门票', '80.00', '5000', '1', '2026-03-23 09:10:00', '2026-03-23 09:10:00', '0');
INSERT INTO `spot_fee_bak_20260323` VALUES ('3104005', '3004005', '5', '都江堰门票', '80.00', '5000', '1', '2026-03-23 09:10:00', '2026-03-23 09:10:00', '0');
INSERT INTO `spot_fee_bak_20260323` VALUES ('3104006', '3004006', '5', '春熙路夜游券', '15.00', '5000', '1', '2026-03-23 09:10:00', '2026-03-23 09:10:00', '0');

-- ----------------------------
-- Table structure for `spot_order`
-- ----------------------------
DROP TABLE IF EXISTS `spot_order`;
CREATE TABLE `spot_order` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `userId` bigint NOT NULL COMMENT '用户 id',
  `spotFeeId` bigint NOT NULL COMMENT '门票id',
  `userName` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '用户名',
  `userPhone` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '联系电话',
  `paymentAmount` decimal(10,2) DEFAULT NULL COMMENT '支付金额',
  `payStatus` tinyint NOT NULL DEFAULT '0' COMMENT '支付状态（1已支付，0未支付）默认0',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `payId` (`userId`,`spotFeeId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of spot_order
-- ----------------------------

-- ----------------------------
-- Table structure for `spot_order_bak_20260323`
-- ----------------------------
DROP TABLE IF EXISTS `spot_order_bak_20260323`;
CREATE TABLE `spot_order_bak_20260323` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `userId` bigint NOT NULL COMMENT '用户 id',
  `spotFeeId` bigint NOT NULL COMMENT '门票id',
  `userName` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '用户名',
  `userPhone` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '联系电话',
  `paymentAmount` decimal(10,2) DEFAULT NULL COMMENT '支付金额',
  `payStatus` tinyint NOT NULL DEFAULT '0' COMMENT '支付状态（1已支付，0未支付）默认0',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `payId` (`userId`,`spotFeeId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of spot_order_bak_20260323
-- ----------------------------

-- ----------------------------
-- Table structure for `spot_route`
-- ----------------------------
DROP TABLE IF EXISTS `spot_route`;
CREATE TABLE `spot_route` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `spotIds` varchar(2048) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '景点 id（JSON字符串,顺序从前往后)',
  `adminId` bigint NOT NULL COMMENT '管理员 ID',
  `spotRouteAvatar` varchar(2048) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '路线封面图',
  `spotRouteDescription` varchar(1024) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '路线描述',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3209004 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of spot_route
-- ----------------------------
INSERT INTO `spot_route` VALUES ('3201001', '[\"3001001\",\"3001004\",\"3001005\"]', '5', 'https://upload.wikimedia.org/wikipedia/commons/a/a7/Forbidden_City_Beijing_Shenwumen_Gate.JPG', '北京历史文化一日线：故宫 -> 天坛 -> 南锣鼓巷', '2026-03-23 09:20:00', '2026-03-23 09:20:00', '0');
INSERT INTO `spot_route` VALUES ('3201002', '[\"3001002\",\"3001006\",\"3001005\"]', '5', 'https://upload.wikimedia.org/wikipedia/commons/0/06/%E9%A2%90%E5%92%8C%E5%9B%AD%E4%B8%87%E5%AF%BF%E5%B1%B1%E4%BD%9B%E9%A6%99%E9%98%81.jpg', '北京慢游二日线（第1天）：颐和园 -> 什刹海 -> 南锣鼓巷', '2026-03-23 09:20:00', '2026-03-23 09:20:00', '0');
INSERT INTO `spot_route` VALUES ('3201003', '[\"3001003\",\"3001001\"]', '5', 'https://upload.wikimedia.org/wikipedia/commons/c/c4/GreatWall_Badaling.jpg', '北京经典打卡线：八达岭长城 -> 故宫', '2026-03-23 09:20:00', '2026-03-23 09:20:00', '0');
INSERT INTO `spot_route` VALUES ('3201004', '[\"3001004\",\"3001002\",\"3001006\"]', '5', 'https://upload.wikimedia.org/wikipedia/commons/5/5f/Temple_of_Heaven_20160323_01.jpg', '北京亲子休闲线：天坛 -> 颐和园 -> 什刹海', '2026-03-23 09:20:00', '2026-03-23 09:20:00', '0');
INSERT INTO `spot_route` VALUES ('3202001', '[\"3002001\",\"3002002\",\"3002004\"]', '5', 'https://upload.wikimedia.org/wikipedia/commons/1/17/West_Lake%2C_Hangzhou_2025.jpg', '杭州经典城市线：西湖 -> 灵隐寺 -> 河坊街', '2026-03-23 09:20:00', '2026-03-23 09:20:00', '0');
INSERT INTO `spot_route` VALUES ('3202002', '[\"3002001\",\"3002003\",\"3002005\"]', '5', 'https://upload.wikimedia.org/wikipedia/commons/2/20/Xixi_Wetland_Park%2C_Hangzhou%2C_China.jpg', '杭州自然风光线：西湖 -> 西溪湿地 -> 千岛湖', '2026-03-23 09:20:00', '2026-03-23 09:20:00', '0');
INSERT INTO `spot_route` VALUES ('3202003', '[\"3002004\",\"3002001\",\"3002002\"]', '5', 'https://upload.wikimedia.org/wikipedia/commons/9/99/Hefang_Street.jpg', '杭州慢生活线：河坊街 -> 西湖 -> 灵隐寺', '2026-03-23 09:20:00', '2026-03-23 09:20:00', '0');
INSERT INTO `spot_route` VALUES ('3203001', '[\"3003001\",\"3003005\",\"3003003\"]', '5', 'https://upload.wikimedia.org/wikipedia/commons/8/88/51714-Terracota-Army.jpg', '西安深度文化线：兵马俑 -> 华清宫 -> 大雁塔', '2026-03-23 09:20:00', '2026-03-23 09:20:00', '0');
INSERT INTO `spot_route` VALUES ('3203002', '[\"3003002\",\"3003003\",\"3003004\"]', '5', 'https://upload.wikimedia.org/wikipedia/commons/3/32/City_wall_of_Xi%27an_51550-Xian_%2827959363326%29.jpg', '西安城内线：城墙 -> 大雁塔 -> 回民街', '2026-03-23 09:20:00', '2026-03-23 09:20:00', '0');
INSERT INTO `spot_route` VALUES ('3203003', '[\"3003003\",\"3003002\",\"3003004\"]', '5', 'https://upload.wikimedia.org/wikipedia/commons/1/13/Giant_Wild_Goose_Pagoda.jpg', '西安夜景美食线：大雁塔 -> 城墙 -> 回民街', '2026-03-23 09:20:00', '2026-03-23 09:20:00', '0');
INSERT INTO `spot_route` VALUES ('3204001', '[\"3004003\",\"3004002\",\"3004001\",\"3004006\"]', '5', 'https://upload.wikimedia.org/wikipedia/commons/4/4c/Chengdu_Research_Base_Eingang.jpg', '成都亲子美食线：熊猫基地 -> 锦里 -> 宽窄巷子 -> 春熙路', '2026-03-23 09:20:00', '2026-03-23 09:20:00', '0');
INSERT INTO `spot_route` VALUES ('3204002', '[\"3004005\",\"3004004\"]', '5', 'https://upload.wikimedia.org/wikipedia/commons/d/d2/36661-Dujiangyan_%2844634340644%29.jpg', '成都山水文化线：都江堰 -> 青城山', '2026-03-23 09:20:00', '2026-03-23 09:20:00', '0');
INSERT INTO `spot_route` VALUES ('3205001', '[\"3005001\",\"3005002\",\"3005004\"]', '5', '/spot-images/3005001.jpg', '上海都市地标一日线：外滩 -> 东方明珠 -> 南京路步行街', '2026-03-23 22:30:00', '2026-03-24 00:05:46', '0');
INSERT INTO `spot_route` VALUES ('3205002', '[\"3005003\",\"3005001\",\"3005004\"]', '5', '/spot-images/3005003.jpg', '上海老城风情线：豫园 -> 外滩 -> 南京路步行街', '2026-03-23 22:30:00', '2026-03-24 00:05:46', '0');
INSERT INTO `spot_route` VALUES ('3205003', '[\"3005005\",\"3005001\"]', '5', '/spot-images/3005005.jpg', '上海亲子休闲线：迪士尼 -> 外滩夜景', '2026-03-23 22:30:00', '2026-03-24 00:05:46', '0');
INSERT INTO `spot_route` VALUES ('3206001', '[\"3006002\",\"3006003\",\"3006001\"]', '5', '/spot-images/3006002.jpg', '南京历史文化线：中山陵 -> 玄武湖 -> 夫子庙', '2026-03-23 22:30:00', '2026-03-24 00:17:32', '0');
INSERT INTO `spot_route` VALUES ('3206002', '[\"3006004\",\"3006005\",\"3006001\"]', '5', '/spot-images/3006004.jpg', '南京博物与街区线：南京博物院 -> 老门东 -> 夫子庙', '2026-03-23 22:30:00', '2026-03-24 00:17:32', '0');
INSERT INTO `spot_route` VALUES ('3206003', '[\"3006003\",\"3006002\"]', '5', '/spot-images/3006003.jpg', '南京慢游半日线：玄武湖 -> 中山陵', '2026-03-23 22:30:00', '2026-03-24 00:17:32', '0');
INSERT INTO `spot_route` VALUES ('3206011', '[\"3006002\",\"3006011\",\"3006004\"]', '5', '/spot-images/3006002.jpg', '南京紫金山历史线：中山陵 -> 明孝陵 -> 南京博物院', '2026-03-24 20:40:00', '2026-03-24 20:40:00', '0');
INSERT INTO `spot_route` VALUES ('3206012', '[\"3006012\",\"3006013\",\"3006003\"]', '5', '/spot-images/3006004.jpg', '南京民国人文线：总统府 -> 鸡鸣寺 -> 玄武湖', '2026-03-24 20:40:00', '2026-03-24 20:40:00', '0');
INSERT INTO `spot_route` VALUES ('3206013', '[\"3006001\",\"3006005\",\"3006017\",\"3006019\"]', '5', '/spot-images/3006005.jpg', '南京秦淮夜游线：夫子庙 -> 老门东 -> 中华门城墙 -> 紫峰大厦观景台', '2026-03-24 20:40:00', '2026-03-24 20:40:00', '0');
INSERT INTO `spot_route` VALUES ('3206014', '[\"3006016\",\"3006015\",\"3006012\"]', '5', '/spot-images/3006004.jpg', '南京纪念与城市线：纪念馆 -> 雨花台 -> 总统府', '2026-03-24 20:40:00', '2026-03-24 20:40:00', '0');
INSERT INTO `spot_route` VALUES ('3206015', '[\"3006018\",\"3006003\",\"3006013\"]', '5', '/spot-images/3006003.jpg', '南京亲子休闲线：红山森林动物园 -> 玄武湖 -> 鸡鸣寺', '2026-03-24 20:40:00', '2026-03-24 20:40:00', '0');
INSERT INTO `spot_route` VALUES ('3206016', '[\"3006014\",\"3006001\",\"3006020\"]', '5', '/spot-images/3006002.jpg', '南京禅意夜景线：牛首山 -> 夫子庙 -> 南京眼步行桥', '2026-03-24 20:40:00', '2026-03-24 20:40:00', '0');
INSERT INTO `spot_route` VALUES ('3206017', '[\"3006011\",\"3006002\",\"3006012\",\"3006001\"]', '5', '/spot-images/3006002.jpg', '南京紧凑经典线：明孝陵 -> 中山陵 -> 总统府 -> 夫子庙', '2026-03-24 20:40:00', '2026-03-24 20:40:00', '0');
INSERT INTO `spot_route` VALUES ('3207001', '[\"3007001\",\"3007002\",\"3007003\"]', '5', '/spot-images/3007001.jpg', '重庆夜景地标线：洪崖洞 -> 解放碑 -> 长江索道', '2026-03-23 22:30:00', '2026-03-24 00:05:46', '0');
INSERT INTO `spot_route` VALUES ('3207002', '[\"3007004\",\"3007001\",\"3007002\"]', '5', '/spot-images/3007004.jpg', '重庆山城人文线：磁器口 -> 洪崖洞 -> 解放碑', '2026-03-23 22:30:00', '2026-03-24 00:05:46', '0');
INSERT INTO `spot_route` VALUES ('3207003', '[\"3007005\",\"3007001\"]', '5', '/spot-images/3007005.jpg', '重庆自然奇观线：武隆天生三桥 -> 洪崖洞夜游', '2026-03-23 22:30:00', '2026-03-24 00:05:46', '0');
INSERT INTO `spot_route` VALUES ('3208001', '[\"3008001\",\"3008002\",\"3008003\"]', '5', '/spot-images/3008001.jpg', '苏州园林古街线：拙政园 -> 平江路 -> 寒山寺', '2026-03-23 22:30:00', '2026-03-24 00:05:46', '0');
INSERT INTO `spot_route` VALUES ('3208002', '[\"3008004\",\"3008001\"]', '5', '/spot-images/3008004.jpg', '苏州水乡经典线：周庄古镇 -> 拙政园', '2026-03-23 22:30:00', '2026-03-24 00:05:46', '0');
INSERT INTO `spot_route` VALUES ('3208003', '[\"3008005\",\"3008002\"]', '5', '/spot-images/3008005.jpg', '苏州城市休闲线：金鸡湖 -> 平江路', '2026-03-23 22:30:00', '2026-03-24 00:05:46', '0');
INSERT INTO `spot_route` VALUES ('3209001', '[\"3009001\",\"3009002\",\"3009005\"]', '5', '/spot-images/3009001.jpg', '桂林山水经典线：漓江 -> 象鼻山 -> 两江四湖', '2026-03-23 22:30:00', '2026-03-24 00:05:46', '0');
INSERT INTO `spot_route` VALUES ('3209002', '[\"3009004\",\"3009003\"]', '5', '/spot-images/3009004.jpg', '桂林梯田人文线：龙脊梯田 -> 阳朔西街', '2026-03-23 22:30:00', '2026-03-24 00:05:46', '0');
INSERT INTO `spot_route` VALUES ('3209003', '[\"3009001\",\"3009003\",\"3009002\"]', '5', '/spot-images/3009001.jpg', '桂林阳朔轻松线：漓江 -> 阳朔西街 -> 象鼻山', '2026-03-23 22:30:00', '2026-03-24 00:05:46', '0');

-- ----------------------------
-- Table structure for `spot_route_bak_20260323`
-- ----------------------------
DROP TABLE IF EXISTS `spot_route_bak_20260323`;
CREATE TABLE `spot_route_bak_20260323` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `spotIds` varchar(2048) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '景点 id（JSON字符串,顺序从前往后)',
  `adminId` bigint NOT NULL COMMENT '管理员 ID',
  `spotRouteAvatar` varchar(2048) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '路线封面图',
  `spotRouteDescription` varchar(1024) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '路线描述',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3204003 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of spot_route_bak_20260323
-- ----------------------------
INSERT INTO `spot_route_bak_20260323` VALUES ('3201001', '[\"3001001\",\"3001004\",\"3001005\"]', '5', 'https://upload.wikimedia.org/wikipedia/commons/a/a7/Forbidden_City_Beijing_Shenwumen_Gate.JPG', '北京历史文化一日线：故宫 -> 天坛 -> 南锣鼓巷', '2026-03-23 09:20:00', '2026-03-23 09:20:00', '0');
INSERT INTO `spot_route_bak_20260323` VALUES ('3201002', '[\"3001002\",\"3001006\",\"3001005\"]', '5', 'https://upload.wikimedia.org/wikipedia/commons/0/06/%E9%A2%90%E5%92%8C%E5%9B%AD%E4%B8%87%E5%AF%BF%E5%B1%B1%E4%BD%9B%E9%A6%99%E9%98%81.jpg', '北京慢游二日线（第1天）：颐和园 -> 什刹海 -> 南锣鼓巷', '2026-03-23 09:20:00', '2026-03-23 09:20:00', '0');
INSERT INTO `spot_route_bak_20260323` VALUES ('3201003', '[\"3001003\",\"3001001\"]', '5', 'https://upload.wikimedia.org/wikipedia/commons/c/c4/GreatWall_Badaling.jpg', '北京经典打卡线：八达岭长城 -> 故宫', '2026-03-23 09:20:00', '2026-03-23 09:20:00', '0');
INSERT INTO `spot_route_bak_20260323` VALUES ('3201004', '[\"3001004\",\"3001002\",\"3001006\"]', '5', 'https://upload.wikimedia.org/wikipedia/commons/5/5f/Temple_of_Heaven_20160323_01.jpg', '北京亲子休闲线：天坛 -> 颐和园 -> 什刹海', '2026-03-23 09:20:00', '2026-03-23 09:20:00', '0');
INSERT INTO `spot_route_bak_20260323` VALUES ('3202001', '[\"3002001\",\"3002002\",\"3002004\"]', '5', 'https://upload.wikimedia.org/wikipedia/commons/1/17/West_Lake%2C_Hangzhou_2025.jpg', '杭州经典城市线：西湖 -> 灵隐寺 -> 河坊街', '2026-03-23 09:20:00', '2026-03-23 09:20:00', '0');
INSERT INTO `spot_route_bak_20260323` VALUES ('3202002', '[\"3002001\",\"3002003\",\"3002005\"]', '5', 'https://upload.wikimedia.org/wikipedia/commons/2/20/Xixi_Wetland_Park%2C_Hangzhou%2C_China.jpg', '杭州自然风光线：西湖 -> 西溪湿地 -> 千岛湖', '2026-03-23 09:20:00', '2026-03-23 09:20:00', '0');
INSERT INTO `spot_route_bak_20260323` VALUES ('3202003', '[\"3002004\",\"3002001\",\"3002002\"]', '5', 'https://upload.wikimedia.org/wikipedia/commons/9/99/Hefang_Street.jpg', '杭州慢生活线：河坊街 -> 西湖 -> 灵隐寺', '2026-03-23 09:20:00', '2026-03-23 09:20:00', '0');
INSERT INTO `spot_route_bak_20260323` VALUES ('3203001', '[\"3003001\",\"3003005\",\"3003003\"]', '5', 'https://upload.wikimedia.org/wikipedia/commons/8/88/51714-Terracota-Army.jpg', '西安深度文化线：兵马俑 -> 华清宫 -> 大雁塔', '2026-03-23 09:20:00', '2026-03-23 09:20:00', '0');
INSERT INTO `spot_route_bak_20260323` VALUES ('3203002', '[\"3003002\",\"3003003\",\"3003004\"]', '5', 'https://upload.wikimedia.org/wikipedia/commons/3/32/City_wall_of_Xi%27an_51550-Xian_%2827959363326%29.jpg', '西安城内线：城墙 -> 大雁塔 -> 回民街', '2026-03-23 09:20:00', '2026-03-23 09:20:00', '0');
INSERT INTO `spot_route_bak_20260323` VALUES ('3203003', '[\"3003003\",\"3003002\",\"3003004\"]', '5', 'https://upload.wikimedia.org/wikipedia/commons/1/13/Giant_Wild_Goose_Pagoda.jpg', '西安夜景美食线：大雁塔 -> 城墙 -> 回民街', '2026-03-23 09:20:00', '2026-03-23 09:20:00', '0');
INSERT INTO `spot_route_bak_20260323` VALUES ('3204001', '[\"3004003\",\"3004002\",\"3004001\",\"3004006\"]', '5', 'https://upload.wikimedia.org/wikipedia/commons/4/4c/Chengdu_Research_Base_Eingang.jpg', '成都亲子美食线：熊猫基地 -> 锦里 -> 宽窄巷子 -> 春熙路', '2026-03-23 09:20:00', '2026-03-23 09:20:00', '0');
INSERT INTO `spot_route_bak_20260323` VALUES ('3204002', '[\"3004005\",\"3004004\"]', '5', 'https://upload.wikimedia.org/wikipedia/commons/d/d2/36661-Dujiangyan_%2844634340644%29.jpg', '成都山水文化线：都江堰 -> 青城山', '2026-03-23 09:20:00', '2026-03-23 09:20:00', '0');

-- ----------------------------
-- Table structure for `spot_score`
-- ----------------------------
DROP TABLE IF EXISTS `spot_score`;
CREATE TABLE `spot_score` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `spotId` bigint NOT NULL COMMENT '景点 id',
  `userId` bigint NOT NULL COMMENT '用户 id',
  `score` int NOT NULL COMMENT '评分（满分5）',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `scoreId` (`spotId`,`userId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of spot_score
-- ----------------------------

-- ----------------------------
-- Table structure for `spot_score_bak_20260323`
-- ----------------------------
DROP TABLE IF EXISTS `spot_score_bak_20260323`;
CREATE TABLE `spot_score_bak_20260323` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `spotId` bigint NOT NULL COMMENT '景点 id',
  `userId` bigint NOT NULL COMMENT '用户 id',
  `score` int NOT NULL COMMENT '评分（满分5）',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `scoreId` (`spotId`,`userId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of spot_score_bak_20260323
-- ----------------------------

-- ----------------------------
-- Table structure for `user`
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `userAccount` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '账号',
  `userPassword` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
  `unionId` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '微信开放平台id',
  `mpOpenId` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '公众号openId',
  `userName` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户昵称',
  `userAvatar` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户头像',
  `userProfile` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户简介',
  `userRole` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'user' COMMENT '用户角色：user/admin/ban',
  `userPhone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '联系电话',
  `aiRemainNumber` int DEFAULT '0' COMMENT '用户 AI 剩余可使用次数',
  `balance` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '用户余额（仅AI接口调用）',
  `editTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '编辑时间',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_unionId` (`unionId`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2019407186799722499 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='用户';

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', 'user1', '01059c753223cd004dd34f0e5e140ba2', 'unionId1', 'mpOpenId1', 'user1', 'https://www.code-nav.cn/logo.png', '喜欢编程的小白', 'user', null, '0', '0.00', '2025-01-28 18:31:53', '2025-01-28 18:31:53', '2025-02-08 15:41:28', '0');
INSERT INTO `user` VALUES ('2', 'user2', '01059c753223cd004dd34f0e5e140ba2', 'unionId2', 'mpOpenId2', 'user2', 'https://www.code-nav.cn/logo.png', '全栈开发工程师', 'user', null, '0', '0.00', '2025-01-28 18:31:53', '2025-01-28 18:31:53', '2025-01-28 18:31:53', '0');
INSERT INTO `user` VALUES ('3', 'user3', '01059c753223cd004dd34f0e5e140ba2', 'unionId3', 'mpOpenId3', 'user3', 'https://www.code-nav.cn/logo.png', '前端爱好者', 'user', null, '0', '0.00', '2025-01-28 18:31:53', '2025-01-28 18:31:53', '2025-01-28 18:31:53', '0');
INSERT INTO `user` VALUES ('4', 'user4', '01059c753223cd004dd34f0e5e140ba2', 'unionId4', 'mpOpenId4', 'user4', 'https://www.code-nav.cn/logo.png', '后端开发工程师', 'user', null, '0', '0.00', '2025-01-28 18:31:53', '2025-01-28 18:31:53', '2025-01-28 18:31:53', '0');
INSERT INTO `user` VALUES ('5', 'admin', '01059c753223cd004dd34f0e5e140ba2', null, null, '程序员小白', 'https://www.code-nav.cn/logo.png', '系统管理员', 'admin', '13812345678', '0', '0.00', '2025-01-28 18:31:53', '2025-01-28 18:31:53', '2026-02-05 22:08:28', '0');
INSERT INTO `user` VALUES ('6', 'xiaobaitiao', '01059c753223cd004dd34f0e5e140ba2', null, null, '程序员小白条1', 'https://img1.baidu.com/it/u=3598104138,3632108415&fm=253&fmt=auto&app=120&f=JPEG?w=800&h=800', 'Java开发工程师2', 'user', '13812345678', '0', '860.20', '2025-01-28 18:31:53', '2025-01-28 18:31:53', '2025-02-28 14:34:31', '0');
INSERT INTO `user` VALUES ('1888851998200721410', 'xiaobaitiao123', '01059c753223cd004dd34f0e5e140ba2', null, null, '小白条的小助手', 'https://www.code-nav.cn/logo.png', '我是小助手', 'user', '13812345678', '0', '0.00', '2025-02-10 15:26:24', '2025-02-10 15:26:24', '2025-02-21 16:25:52', '0');
INSERT INTO `user` VALUES ('1890695308817182722', 'xiaobaitiao1234', '01059c753223cd004dd34f0e5e140ba2', null, null, null, null, null, 'user', null, '0', '0.00', '2025-02-15 17:31:03', '2025-02-15 17:31:03', '2025-02-15 17:42:52', '1');
INSERT INTO `user` VALUES ('2019407186799722498', 'toxixixixi', '01059c753223cd004dd34f0e5e140ba2', null, null, null, null, null, 'user', null, '0', '0.00', '2026-02-05 21:46:06', '2026-02-05 21:46:06', '2026-02-05 21:46:06', '0');

-- ----------------------------
-- Table structure for `user_ai_message`
-- ----------------------------
DROP TABLE IF EXISTS `user_ai_message`;
CREATE TABLE `user_ai_message` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `userInputText` varchar(4096) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '用户输入',
  `aiGenerateText` varchar(4096) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT 'AI 生成结果',
  `userId` bigint NOT NULL COMMENT '用户ID',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2036339609529569283 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of user_ai_message
-- ----------------------------
INSERT INTO `user_ai_message` VALUES ('1887795958008999937', 'Java 怎么学习', '2323133', '5', '2025-02-07 17:30:04', '2025-02-08 15:58:48', '0');
INSERT INTO `user_ai_message` VALUES ('1887806705665454082', 'Java 怎么学习，简短介绍即可', '', '6', '2025-02-07 18:12:47', '2025-02-07 18:12:47', '0');
INSERT INTO `user_ai_message` VALUES ('1887806865468436481', 'Java 怎么学习，简短介绍即可', '', '6', '2025-02-07 18:13:25', '2025-02-07 18:13:25', '0');
INSERT INTO `user_ai_message` VALUES ('1887808007292317697', 'Java 怎么学习，简短介绍即可', '学习', '6', '2025-02-07 18:17:57', '2025-02-07 18:17:57', '0');
INSERT INTO `user_ai_message` VALUES ('1887808105250287618', 'Java 怎么学习', '学习', '6', '2025-02-07 18:18:20', '2025-02-07 18:18:20', '0');
INSERT INTO `user_ai_message` VALUES ('1888114368764936193', 'Java 怎么学习', '学习Java是一个循序渐进的过程，需要掌握基础知识、进行实践练习、深入学习高级特性，并参与项目开发以积累经验。以下是一些建议：\n\n### 一、扎实基础\n\n1. **选择优质学习资源**：可以选择权威的教材，如《Java核心技术卷I》等；也可以观看在线课程，像尚硅谷宋红康老师的Java零基础入门教程，讲解细致，适合初学者。\n\n2. **掌握基本语法**：包括数据类型、变量、运算符、控制流语句（如if-else、for循环、while循环等）、数组和字符串等。\n\n3. **理解面向对象编程**：这是Java的核心概念，需深入理解类和对象的关系，掌握封装、继承和多态三大特性，以及接口、抽象类等相关知识。\n\n4. **熟悉常用类库**：如集合框架（List、Set、Map等）、输入输出流（用于文件操作和网络通信）、日期时间处理类等。\n\n### 二、实践练习\n\n1. **完成基础练习题**：巩固所学的语法知识，加深对知识点的理解和记忆。\n\n2. **进行小项目实践**：从简单的项目入手，如图书管理系统、学生成绩管理系统等，将所学知识综合运用，提高解决实际问题的能力。\n\n3. **参与开源项目**：在技术平台上搜索感兴趣的开源项目参与其中，学习他人的优秀代码和设计思路，提升自己的编程水平。\n\n### 三、深入学习\n\n1. **学习数据库知识**：掌握至少一种关系型数据库，如MySQL，了解数据库的设计、创建表、增删改查操作，以及如何使用JDBC与Java程序进行交互。\n\n2. **研究Java高级特性**：如泛型、反射、注解、动态代理、并发编程等，这些特性在大型项目中经常使用，能够提升程序的性能和可扩展性。\n\n3. **学习框架**：如Spring、Spring MVC、MyBatis等，这些框架可以帮助开发者快速构建企业级应用，提高开发效率。\n\n### 四、持续提升\n\n1. **阅读源码**：阅读优秀的Java源码，如开源框架的源代码，学习其设计模式、代码结构和编程技巧，提升自己的编程能力。\n\n2. **关注行业动态**：关注Java的最新发展动态和技术趋势，学习新的技术和工具，不断更新自己的知识体系。\n\n3. **参加技术交流活动**：加入Java技术社区，参加线上线下的技术交流活动、研讨会等，与其他开发者交流经验，分享心得，拓宽视野。\n\n总之，通过以上步骤和方法，你可以系统地学习Java编程，逐步提升自己的编程能力。记住，持续学习和实践是成为优秀Java开发者的关键。', '6', '2025-02-08 14:35:19', '2025-02-08 14:35:19', '0');
INSERT INTO `user_ai_message` VALUES ('1888119440571863041', '力扣的两数之和该怎么做？给我 Java 代码', '力扣（LeetCode）上的“两数之和”问题要求你在一个整数数组中找到两个数，使它们的和等于一个特定的目标值，并返回这两个数的索引。\n\n下面是一个用 Java 实现的解决方案：\n\n```java\nimport java.util.HashMap;\nimport java.util.Map;\n\npublic class TwoSum {\n    public int[] twoSum(int[] nums, int target) {\n        // 创建一个哈希表来存储数组中的数值及其对应的索引\n        Map<Integer, Integer> map = new HashMap<>();\n        \n        // 遍历数组\n        for (int i = 0; i < nums.length; i++) {\n            // 计算当前数值与目标值之间的差值\n            int complement = target - nums[i];\n            \n            // 如果哈希表中存在这个差值，说明找到了两个数\n            if (map.containsKey(complement)) {\n                return new int[] { map.get(complement), i };\n            }\n            \n            // 将当前数值及其索引存入哈希表\n            map.put(nums[i], i);\n        }\n        \n        // 如果没有找到符合条件的两个数，抛出异常\n        throw new IllegalArgumentException(\"No two sum solution\");\n    }\n\n    public static void main(String[] args) {\n        TwoSum solution = new TwoSum();\n        int[] nums = {2, 7, 11, 15};\n        int target = 9;\n        int[] result = solution.twoSum(nums, target);\n        System.out.println(\"Indices: \" + result[0] + \", \" + result[1]);\n    }\n}\n```\n\n### 代码解释：\n1. **导入必要的包**：`import java.util.HashMap; import java.util.Map;`\n   - `HashMap` 用于存储数组中的数值及其对应的索引。\n   - `Map` 是 `HashMap` 的接口。\n\n2. **定义类和方法**：`public class TwoSum` 和 `public int[] twoSum(int[] nums, int target)`\n   - `TwoSum` 类包含解决问题的方法。\n   - `twoSum` 方法接受一个整数数组 `nums` 和一个目标值 `target`，返回两个整数的索引。\n\n3. **创建哈希表**：`Map<Integer, Integer> map = new HashMap<>();`\n   - 用于存储数组中的数值及其对应的索引。\n\n4. **遍历数组**：`for (int i = 0; i < nums.length; i++)`\n   - 使用循环遍历数组中的每一个元素。\n\n5. **计算差值**：`int complement = target - nums[i];`\n   - 计算当前数值与目标值之间的差值。\n\n6. **检查哈希表**：`if (map.containsKey(complement))`\n   - 如果哈希表中存在这个差值，说明找到了两个数，返回它们的索引。\n\n7. **更新哈希表**：`map.put(nums[i], i);`\n   - 将当前数值及其索引存入哈希表。\n\n8. **处理未找到的情况**：`throw new IllegalArgumentException(\"No two sum solution\");`\n   - 如果遍历完数组后没有找到符合条件的两个数，抛出异常。\n\n9. **主方法测试**：`public static void main(String[] args)`\n   - 创建一个 `TwoSum` 对象并调用 `twoSum` 方法进行测试。\n\n希望这段代码能帮助你理解如何解决这个问题！', '6', '2025-02-08 14:55:28', '2025-02-08 14:55:28', '0');
INSERT INTO `user_ai_message` VALUES ('1888121302951890945', '1+1=？', '$1+1$ $=2$', '6', '2025-02-08 15:02:52', '2025-02-08 15:02:52', '0');
INSERT INTO `user_ai_message` VALUES ('1888122068517228546', '请简要描述线程与进程的关系,区别及优缺点？', '线程与进程是操作系统中两个重要的概念，它们之间既有联系又有区别。以下是对线程与进程的关系、区别及优缺点的简要描述：\n\n### 关系\n\n1. **包含关系**：一个进程可以包含多个线程，这些线程共享进程的资源。\n\n2. **执行环境**：线程在进程内执行任务并共享进程的上下文和资源。\n\n### 区别\n\n1. **资源占用**：进程拥有独立的内存空间和系统资源，而线程则共享其所属进程的资源。\n\n2. **执行方式**：进程在操作系统中独立执行，而线程则是在进程内部由操作系统统一调度。\n\n3. **开销**：创建和销毁进程的开销较大，因为需要分配独立的内存空间和系统资源；而线程的创建和销毁开销相对较小，因为它们共享进程的资源。\n\n4. **并发性**：进程是独立的执行单元，不受其他进程影响，安全性较高；线程共享进程的资源，因此在并发执行时需要考虑同步和互斥问题，安全性较低。\n\n### 优点\n\n1. **进程的优点**：\n   - 独立性：进程之间相互隔离，一个进程的崩溃通常不会影响其他进程的正常运行。\n   - 资源管理：进程提供了更高级别的隔离和资源管理，使得不同进程可以独立运行，互不干扰。\n\n2. **线程的优点**：\n   - 轻量级：线程是轻量级的执行单位，创建和销毁线程的开销相对较小。\n   - 并发性：通过多线程可以实现任务的并发处理，提高系统的效率和响应速度。\n   - 资源共享：线程共享进程的内存空间和资源，方便数据共享和通信。\n\n### 缺点\n\n1. **进程的缺点**：\n   - 资源占用：每个进程都有自己的独立内存空间和系统资源，因此资源占用相对较大。\n   - 切换开销：进程切换时需要保存和恢复较多的上下文信息，开销较大。\n\n2. **线程的缺点**：\n   - 同步与互斥：由于线程共享进程的资源，因此在并发执行时需要考虑同步和互斥问题，以避免数据竞争和死锁。\n   - 编程复杂性：多线程编程相对复杂，需要考虑线程间的协作和同步问题。\n   - 健壮性降低：一个线程的异常可能会导致整个进程的崩溃。\n\n综上所述，线程与进程各有其优缺点，适用于不同的应用场景。在选择使用线程还是进程时，需要根据具体需求进行权衡和选择。', '6', '2025-02-08 15:05:55', '2025-02-08 15:05:55', '0');
INSERT INTO `user_ai_message` VALUES ('1888124716830134273', '给我两行诗句\n', '当然可以，以下是两行诗句：\n\n月下独酌花影碎，风中浅唱梦初回。', '6', '2025-02-08 15:16:26', '2025-02-08 15:16:26', '0');
INSERT INTO `user_ai_message` VALUES ('1888124905250852866', '给我李白的三首诗歌\n', '李白的诗歌风格豪放、浪漫，充满了丰富的想象力和深刻的情感表达。以下为你推荐三首李白的著名诗歌：\n\n1. **《将进酒》**\n   - **全文**：君不见，黄河之水天上来，奔流到海不复回。君不见，高堂明镜悲白发，朝如青丝暮成雪。人生得意须尽欢，莫使金樽空对月。天生我材必有用，千金散尽还复来。烹羊宰牛且为乐，会须一饮三百杯。岑夫子，丹丘生，将进酒，杯莫停。与君歌一曲，请君为我倾耳听。钟鼓馔玉不足贵，但愿长醉不愿醒。古来圣贤皆寂寞，惟有饮者留其名。陈王昔时宴平乐，斗酒十千恣欢谑。主人何为言少钱，径须沽取对君酌。五花马，千金裘，呼儿将出换美酒，与尔同销万古愁。\n   - **赏析**：这首诗是李白怀才不遇，与友人岑勋、元丹丘相会时所作。诗中表达了诗人对时光流逝、人生短暂的感慨，同时也流露出他豪放不羁的性格和对自由生活的向往。全诗情感饱满，语言豪放，充分展现了李白的浪漫主义风格。\n\n2. **《望庐山瀑布》**\n   - **全文**：日照香炉生紫烟，遥看瀑布挂前川。飞流直下三千尺，疑是银河落九天。\n   - **赏析**：这首诗形象地描绘了庐山瀑布雄奇壮丽的景色，反映了诗人对祖国大好河山的无限热爱。诗中运用了丰富的想象力和夸张的手法，将瀑布比作银河从天而降，生动地表现了瀑布的雄伟壮观。\n\n3. **《早发白帝城》**\n   - **全文**：朝辞白帝彩云间，千里江陵一日还。两岸猿声啼不住，轻舟已过万重山。\n   - **赏析**：此诗创作于乾元二年（759）诗人流放途中遇赦返回之时。全诗给人一种锋棱挺拔、空灵飞动之感，洋溢着诗人经过艰难岁月之后突然迸发的一种激情，故雄峻迅疾中又带有豪情欢悦。\n\n总的来说，这些诗歌都展现了李白独特的艺术风格和卓越的才华，是中国古典文学中的瑰宝。', '6', '2025-02-08 15:17:11', '2025-02-08 15:17:11', '0');
INSERT INTO `user_ai_message` VALUES ('1888125326132482050', '唐宋八大家分别是谁？\n', '唐宋八大家分别是**韩愈、柳宗元、欧阳修、苏洵、苏轼、苏辙、王安石、曾巩**。以下是他们的具体介绍：\n\n1. **韩愈**：字退之，河南河阳人。唐代文学家、思想家、哲学家、政治家。他是唐宋古文运动的倡导者，主张学习先秦两汉的散文语言，破骈为散，扩大文言文的表达功能。其文章众多，现存的诗文有1200余篇，其中散文近400篇，类型涵盖赋、诗、赞、书、说、传、记、序、哀辞、祭文、碑志、表、杂文等多种体裁。\n\n2. **柳宗元**：字子厚，河东（今山西运城永济一带）人。唐代文学家、哲学家、散文家和思想家，与韩愈并称为“韩柳”，共同倡导了唐代的古文运动。\n\n3. **欧阳修**：字永叔，号醉翁，晚号六一居士，吉州永丰（今江西永丰）人。北宋政治家、文学家，是宋代古文运动的领袖之一，对后世文学影响深远。\n\n4. **苏洵**：字明允，自号老泉，眉州眉山（今四川眉山）人。北宋文学家，与其子苏轼、苏辙并以文学著称于世，世称“三苏”。\n\n5. **苏轼**：字子瞻，又字和仲，号东坡居士，世称苏东坡、苏仙。汉族，北宋著名文学家、书法家、画家。他是豪放派词人的代表，与黄庭坚并称“苏黄”，与辛弃疾并称“苏辛”。\n\n6. **苏辙**：字子由，一字同叔，晚号颍滨遗老。眉州眉山（今属四川省眉山市）人。北宋文学家、宰相，与父亲苏洵、兄长苏轼齐名，合称“三苏”。\n\n7. **王安石**：字介甫，号半山，临江（今江西樟树）人。北宋著名的思想家、政治家、文学家、改革家，唐宋八大家之一。\n\n8. **曾巩**：字子固，世称“南丰先生”，建昌南丰（今属江西）人。北宋文学家、史学家、政治家，为“唐宋八大家”之一。', '6', '2025-02-08 15:18:52', '2025-02-08 15:18:52', '0');
INSERT INTO `user_ai_message` VALUES ('1888131116046139393', '程序员小白条是谁？\n', '程序员小白条是一位活跃在技术分享领域的博主，以下是关于他的一些信息：\n\n1. **技术分享内容**：他会分享面试必备的八股文知识，涵盖Java基础、Java框架、Git命令、JVM、多线程、消息队列、微服务、Linux、数据库、Redis缓存、算法等众多技术领域。还会介绍一些技术概念和原理，如渐进式rehash、缓存击穿、缓存穿透、缓存雪崩、MVCC、回表、最左匹配原则、循环依赖、IOC、DI以及用户态和内核态等。\n\n2. **项目经验分享**：他曾分享过自己使用SpringBoot2+Vue2开发的智能GPT图书管理系统，包括全局异常拦截器、登录校验拦截器、Echarts展示借阅量等功能，以及可添加爬虫等扩展功能。\n\n总的来说，程序员小白条通过其丰富的技术分享和实战经验展示，为广大程序员提供了宝贵的学习资源和参考。', '6', '2025-02-08 15:41:52', '2025-02-08 15:41:52', '0');
INSERT INTO `user_ai_message` VALUES ('1888131623997325314', '答案说明所有是谁？\n', '您的问题似乎缺乏具体的背景信息和上下文，无法明确指出“答案说明所有”指的是哪些人或事物。能否请您提供更多的详细内容或者情境描述？这样我才能准确地回答您的问题，比如：\n\n1. 您是在询问某个特定问题的答案，还是想了解某类问题的解答方式？\n2. “说明所有”是指解释清楚所有相关细节、涉及的所有方面，还是指提供所有可能的答案选项？\n3. 是否有特定的主题、领域或情境，比如科学、历史、文化、技术等，与您希望了解的答案相关？\n4. 是否有特定的人物、群体、机构等，他们的观点、行为或决策与您关心的答案有关？\n\n请补充相关信息，我会很乐意帮助您解答关于“答案说明所有”的具体所指。', '6', '2025-02-08 15:43:53', '2025-02-08 15:43:53', '0');
INSERT INTO `user_ai_message` VALUES ('1888138562139553794', '面向对象和面向过程的区别面向过程编程（Procedural-Oriented Programming，POP）和面向对象编程（Object-Oriented Programming，OOP）是两种常见的编程范式，两者的主要区别在于解决问题的方式不同：面向过程编程（POP）：面向过程把解决问题的过程拆成一个个方法，通过一个个方法的执行解决问题。面向对象编程（OOP）：面向对象会先抽象出对象，然后用对象执行方法的方式解决问题。', '### 分析问题\n\n用户提出了一个关于面向过程编程（POP）和面向对象编程（OOP）区别的问题。这个问题是一个基础的编程范式问题，通常在面试中用于评估候选人对基本编程概念的理解。\n\n### 深度推理问题及可能的场景和解决方案\n\n1. **场景一：软件开发项目选择**\n   - **问题**：在一个大型软件开发项目中，如何决定是使用POP还是OOP？\n   - **解决方案**：\n     - 考虑项目的复杂性和可维护性。如果项目需求简单且变化不大，POP可能更合适。\n     - 如果项目需要高度的模块化、可扩展性和重用性，OOP可能是更好的选择。\n\n2. **场景二：团队技术栈统一**\n   - **问题**：如何在团队中统一技术栈，确保所有成员都能高效协作？\n   - **解决方案**：\n     - 进行技术调研，了解团队成员的技术背景和偏好。\n     - 根据项目需求和团队情况，选择适合的编程范式，并制定统一的编码规范和最佳实践。\n\n3. **场景三：性能优化**\n   - **问题**：在性能敏感的应用中，如何选择编程范式以优化性能？\n   - **解决方案**：\n     - 对于性能要求极高的应用，可能需要深入分析两种范式的性能表现。\n     - 在某些情况下，结合使用POP和OOP，或者根据性能瓶颈选择合适的范式，可能是最佳策略。\n\n### 用户回答打分及不足之处分析\n\n用户的回答简洁明了，正确地指出了POP和OOP的主要区别。然而，这个回答相对基础，没有深入探讨两种范式在不同场景下的应用和优缺点。为了更全面地展示对编程范式的理解，用户可以考虑添加以下内容：\n\n- **具体示例**：给出一个简单的代码示例，分别展示POP和OOP如何解决同一个问题。\n- **优缺点分析**：详细描述POP和OOP各自的优点和缺点，以及它们在不同类型项目中的应用。\n- **实际应用场景**：讨论在实际开发中，如何根据项目需求和团队情况选择合适的编程范式。\n\n综上所述，虽然用户的回答是正确的，但为了在面试中获得更高的评价，建议提供更详细、更具体的信息来展示对编程范式的深入理解。', '6', '2025-02-08 16:11:27', '2025-02-08 16:11:27', '0');
INSERT INTO `user_ai_message` VALUES ('1890697165966913537', '有了进程为什么还需要线程?进程切换是一个开销很大的操作，线程切换的成本较低。线程更轻量，一个进程可以创建多个线程。多个线程可以并发处理不同的任务，更有效地利用了多处理器和多核计算机。而进程只能在一个时间干一件事，如果在执行过程中遇到阻塞问题比如 IO 阻塞就会挂起直到结果返回。同一进程内的线程共享内存和文件，因此它们之间相互通信无须调用内核。为什么要使用多线程?', '以下是对“有了进程为什么还需要线程”这一问题的分析、推理以及相关场景和解决方案的阐述，并包含对可能回答的打分及不足分析：\n\n### 问题分析\n- **核心关注点**：该问题主要聚焦于进程与线程存在的必要性，尤其是在已经有进程概念的情况下为何还要引入线程，重点在于探究二者的区别以及线程相较于进程的优势所在。\n\n### 深度推理及场景、解决方案\n#### 1. 资源利用角度\n - **场景**：在多核计算机系统中，如果只使用进程来执行任务，当一个进程因为I/O操作（如等待用户输入、等待文件读取完成等）而阻塞时，整个CPU核就会处于闲置状态，即使有其他可以执行的任务也无法进行，这就导致了计算资源的浪费。例如在一个服务器应用中，某个进程在等待客户端通过网络发送数据时，CPU就只能干等着，无法去处理其他可以即时处理的任务。\n - **解决方案**：引入线程后，当一个线程因为I/O阻塞时，同一进程内的其他线程可以利用CPU继续执行其他任务，使得CPU资源能够被充分利用起来，提高了系统的整体吞吐量。比如上述服务器应用中，可以在等待网络数据的进程中再创建几个线程，有的线程去处理其他已经接收到的数据，有的线程去处理一些本地计算任务等。\n\n#### 2. 并发性角度\n - **场景**：对于一些需要同时处理多个相似或不同任务的应用程序来说，如果仅靠进程来实现并发，会面临诸多限制。比如一个图形界面应用程序，既要响应用户的鼠标点击操作（这是一个独立的交互任务），又要不断更新界面显示内容（这是另一个独立的绘制任务），若只用进程，很难做到高效的同时处理这两个任务。\n - **解决方案**：使用线程可以让多个任务在同一个进程内并发执行。不同的线程可以专注于不同的功能模块，它们共享进程的资源（如内存空间等），通过合理的调度机制，能够快速地切换执行，实现真正意义上的并发处理，提高程序的响应速度和用户体验。例如上述图形界面程序中，可以创建一个专门处理鼠标事件的线程和一个专门负责界面绘制的线程，让它们并发运行。\n\n#### 3. 通信效率角度\n - **场景**：在复杂的应用程序中，不同部分之间往往需要进行数据传递和通信。如果使用多个进程来实现不同功能模块，那么进程间通信（IPC）通常需要借助操作系统提供的特定机制（如管道、消息队列等），这些机制相对复杂且开销较大。例如在一个企业级应用中，负责数据处理的进程要和负责数据存储的进程频繁交换数据，每次通信都要经过复杂的IPC流程。\n - **解决方案**：同一进程内的线程共享内存和文件等资源，它们之间的通信可以直接通过共享内存变量等方式进行，无需像进程间通信那样调用内核接口，极大地简化了通信流程，降低了通信成本，提高了通信效率。比如在上述企业级应用中，如果把数据处理和数据存储放在同一个进程的不同线程中，它们可以通过共享内存中的缓冲区来快速传递数据。\n\n### 对可能回答的打分及不足分析\n- **假设回答示例**：“有了进程还需要线程是因为线程更轻量，一个进程能创建多个线程，多个线程可以并发处理任务，比进程只能一个时间干一件事要好。”\n    - **打分**：5分（满分10分）\n    - **不足分析**：这个回答只是简单陈述了线程相比进程的部分优势，没有深入解释为什么会有这些优势以及在实际场景中如何体现这些优势。缺乏对资源利用、并发性、通信效率等具体方面的详细阐述，没有结合实际的应用案例来说明线程存在的必要性，整体回答比较笼统和表面，没有深入挖掘问题背后的原理和实际意义。\n \n\n综上所述，进程和线程在计算机系统中都有其不可或缺的地位，线程的出现是为了弥补进程在某些方面的不足，更好地满足现代复杂应用程序对资源利用、并发处理和高效通信等方面的需求。', '6', '2025-02-15 17:38:26', '2025-02-15 17:38:26', '0');
INSERT INTO `user_ai_message` VALUES ('1891408151659982850', 'HTTP/1.0 和 HTTP/1.1 有什么区别？连接方式 : HTTP/1.0 为短连接，HTTP/1.1 支持长连接。HTTP 协议的长连接和短连接，实质上是 TCP 协议的长连接和短连接。', 'HTTP/1.0 和 HTTP/1.1 是两个版本的超文本传输协议（HTTP），它们在连接方式、缓存处理、管线化支持等方面存在一些关键区别。以下是对这些区别的详细分析：\n\n### 1. 连接方式\n- **HTTP/1.0**: 默认使用短连接，即每个请求都会建立一个新的TCP连接，并在请求处理完毕后立即关闭连接。这种方式在处理大量请求时效率较低，因为每次请求都需要进行三次握手和四次挥手的过程。\n- **HTTP/1.1**: 引入了持久连接（也称为长连接）的概念，允许多个请求复用同一个TCP连接。这减少了连接建立和关闭的开销，提高了网络传输的效率。\n\n### 2. 缓存处理\n- **HTTP/1.0**: 对缓存的支持较为有限，主要依赖于Expires头字段来控制缓存行为。\n- **HTTP/1.1**: 增强了对缓存的支持，引入了Cache-Control头字段，提供了更灵活的缓存控制机制，如max-age、no-cache等。\n\n### 3. 管线化支持\n- **HTTP/1.0**: 不支持管线化，即客户端必须等待服务器响应前一个请求后才能发送下一个请求。\n- **HTTP/1.1**: 支持管线化，允许客户端在一个TCP连接上发送多个请求而无需等待响应，这进一步提高了网络传输的效率。\n\n### 4. 主机头支持\n- **HTTP/1.0**: 不支持主机头（Host header），这意味着每个IP地址只能托管一个网站。\n- **HTTP/1.1**: 引入了主机头，使得同一个IP地址可以托管多个不同的网站，这是虚拟主机技术的基础。\n\n### 5. 状态码和原因短语\n- **HTTP/1.0**: 定义了一些基本的状态码和原因短语，但数量较少。\n- **HTTP/1.1**: 扩展了状态码和原因短语的数量，提供了更多的错误信息和成功响应的细分。\n\n### 6. 分块传输编码\n- **HTTP/1.0**: 不支持分块传输编码，这意味着服务器在发送响应之前需要知道整个响应体的大小。\n- **HTTP/1.1**: 引入了分块传输编码，允许服务器以流的方式发送响应体，这对于动态生成内容或大文件传输非常有用。\n\n### 7. 身份验证改进\n- **HTTP/1.0**: 身份验证方法相对简单，通常只支持基本的身份验证。\n- **HTTP/1.1**: 提供了更多种类的身份验证方案，包括摘要身份验证和Bearer token等。\n\n### 8. 安全性增强\n- **HTTP/1.0**: 安全性较低，没有内置的安全特性。\n- **HTTP/1.1**: 虽然本身并不安全，但它为后续的安全协议如HTTPS提供了基础。\n\n### 总结\nHTTP/1.1 在HTTP/1.0的基础上进行了多项改进，包括持久连接、缓存控制、管线化支持、主机头支持等，这些改进使得HTTP/1.1更加高效、灵活和安全。然而，需要注意的是，HTTP/1.1仍然是一个无状态协议，这意味着它不保留任何客户端到服务器的通信状态。为了解决这个问题，后来发展出了HTTP/2和HTTP/3等版本，它们进一步优化了性能和安全性。\n\n如果你有具体的代码或问题需要我点评打分，请提供相关详细信息，我会尽力为你进行分析和评价。', '6', '2025-02-17 16:43:38', '2025-02-17 16:43:38', '0');
INSERT INTO `user_ai_message` VALUES ('2035373851739803649', '北京', '### 推荐景点：故宫、长城  \n### 推荐理由  \n1. **故宫**：北京核心文化地标，明清两代皇家宫殿，红墙黄瓦间藏着千年宫廷历史与建筑艺术精华，是感受中国古代皇家气象的必访之地。  \n2. **长城**：世界奇迹之一，北京段（八达岭/慕田峪）气势磅礴，是中华民族精神的象征，登高可俯瞰山峦间的雄伟壮阔，体验历史厚重感。  \n\n### 路线建议  \n**Day1：故宫深度游**  \n上午从午门进入，沿中轴线游览太和殿、乾清宫等核心宫殿；下午逛东六宫（如延禧宫）或西六宫，结束后登景山公园，俯瞰故宫全景。  \n\n**Day2：长城一日游**  \n选择八达岭（交通便利）或慕田峪（人少景美），早上乘景区专线车出发；上午徒步游览长城（建议走精华段），下午返回市区，可顺路参观明十三陵（若时间充裕）。  \n\n（注：景点库中北京相关景点仅故宫、长城，故推荐这两个核心景点）', '6', '2026-03-21 23:11:56', '2026-03-21 23:11:56', '0');
INSERT INTO `user_ai_message` VALUES ('2035379515656593409', '北京', '### 推荐景点及理由  \n1. **故宫**：明清两代皇家宫殿，中国古代建筑艺术巅峰之作，漫步其中可沉浸式感受皇家文化与历史厚重感。  \n2. **长城（八达岭段）**：世界文化遗产，“不到长城非好汉”的必打卡地，雄伟壮观的城墙见证古代防御工程智慧。  \n\n### 路线建议  \n**Day1：故宫深度游**  \n- 上午：午门进，沿中轴线游览太和殿、中和殿、保和殿，参观珍宝馆/钟表馆（需额外购票）；  \n- 下午：逛东六宫/西六宫，神武门出后步行至景山公园，登高俯瞰故宫全景。  \n\n**Day2：长城+明十三陵**  \n- 上午：乘八达岭高铁或旅游专线至八达岭长城，选择北坡攀登（景点集中，视野佳）；  \n- 下午：前往明十三陵（推荐定陵，可参观地下宫殿），傍晚返回市区。  \n\n（注：若仅安排一天，建议优先故宫，长城可单独安排一日，避免行程过赶。）', '6', '2026-03-21 23:34:26', '2026-03-21 23:34:26', '0');
INSERT INTO `user_ai_message` VALUES ('2035383296456744962', '北京', '推荐景点：故宫、长城  \n\n推荐理由：  \n1. 故宫：明清皇家宫殿，红墙黄瓦承载千年历史，是感受中国古代宫廷文化的核心地标。  \n2. 长城（八达岭）：世界奇迹，山脊上的巨龙气势磅礴，见证古代军事工程的智慧与震撼。  \n\n路线建议：  \nDay1：故宫深度游——上午走中轴线（太和殿→中和殿→保和殿），下午逛东六宫/西六宫，傍晚登景山俯瞰故宫全景。  \nDay2：长城一日行——早乘专线车/高铁到八达岭，优先走北坡（景点密集、视野佳），下午返程可顺路参观明十三陵（可选）。  \n\n（注：北京景点库内仅故宫、长城符合需求，故推荐这两个核心景点）', '6', '2026-03-21 23:49:28', '2026-03-21 23:49:28', '0');
INSERT INTO `user_ai_message` VALUES ('2036129954316496897', '南京2天，历史文化+夜游，预算1200', '### 推荐景点及理由  \n1. **中山陵**：中国近代史上重要地标，以宏伟的建筑和深厚的革命历史底蕴著称，是感受南京历史文化的必访之地。  \n2. **南京博物院**：中国三大博物馆之一，馆藏文物超43万件，涵盖从旧石器时代到明清的历史遗存，能深度了解江南文化脉络。  \n3. **夫子庙秦淮河风光带**：明清建筑与秦淮夜景完美融合，既有“十里秦淮”的历史韵味，又有热闹的夜游体验（乘船赏灯、尝特色小吃）。  \n\n\n### 2天路线建议  \n**Day1**  \n- 上午：中山陵（免费，需提前预约，游览2.5小时）→ 步行至音乐台（可选，门票10元）  \n- 下午：南京博物院（免费，需预约，重点看“历史馆”“特展馆”，游览3小时）  \n- 晚上：夫子庙秦淮河夜游（乘船游秦淮，费用约80-100元，品尝鸭血粉丝汤、盐水鸭等小吃）  \n\n**Day2**  \n- 上午：夫子庙区域逛江南贡院（科举文化，门票30元）+ 乌衣巷（免费）  \n- 下午：玄武湖公园（免费，漫步湖边，远眺明城墙，游览2小时）  \n- 晚上：老门东历史街区（免费，逛文创店、吃蒋有记锅贴，看夜景灯光）  \n\n**预算参考**：门票（约150元）+ 餐饮（约300元）+ 交通（约50元）+ 游船（100元）= 总计约600元，远低于1200元预算，可灵活增加特色体验（如明城墙登城）。\n\n\n此路线兼顾历史文化深度与夜游趣味，节奏宽松，适合2天沉浸式感受南京魅力。', '6', '2026-03-24 01:16:25', '2026-03-24 01:16:25', '0');
INSERT INTO `user_ai_message` VALUES ('2036339609529569282', '南京2天，历史文化+夜游，预算1200\n\n', '### 推荐景点及理由  \n1. **夫子庙**：南京历史文化核心地标，秦淮河夜游是“六朝金粉地”的经典体验，可逛古街、品秦淮小吃、乘画舫赏两岸灯火，兼具历史底蕴与夜游乐趣。  \n2. **中山陵**：中国近代民主革命的象征，建筑庄严肃穆，承载孙中山先生的革命精神，是了解民国历史的必访之地。  \n3. **南京博物院**：全国三大博物院之一，馆藏文物超43万件，从史前到明清全面展现江苏及南京的历史脉络，文化价值极高。  \n\n\n### 2天路线建议  \n**Day1**：  \n- 上午：中山陵（提前预约，免费）→ 漫步陵园路，感受民国建筑风情；  \n- 中午：中山陵附近用餐（推荐民国主题餐厅）；  \n- 下午：南京博物院（提前预约，免费）→ 重点参观“历史馆”“特展馆”；  \n- 傍晚：前往夫子庙→ 夜游秦淮河（乘画舫约100元），逛老门东古街，品鸭血粉丝汤、盐水鸭等小吃。  \n- 住宿：夫子庙周边酒店（中等价位约400元/晚）。  \n\n**Day2**：  \n- 上午：夫子庙核心区（大成殿、江南贡院，门票合计约60元）→ 了解科举文化；  \n- 中午：夫子庙小吃街用餐；  \n- 下午：可选补充参观总统府（35元，民国历史缩影）或自由漫步玄武湖（免费），随后返程。  \n\n\n### 预算估算  \n住宿400元 + 秦淮河游船100元 + 夫子庙门票60元 + 总统府35元 + 餐饮300元 + 交通50元 = **945元**，远低于1200元预算，剩余可灵活用于特色体验（如手作纪念品）。  \n\n此路线兼顾历史文化深度与夜游趣味，符合南京2天行程需求。', '6', '2026-03-24 15:09:30', '2026-03-24 15:09:30', '0');

-- ----------------------------
-- Table structure for `user_spot_favorites`
-- ----------------------------
DROP TABLE IF EXISTS `user_spot_favorites`;
CREATE TABLE `user_spot_favorites` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `userId` bigint NOT NULL COMMENT '用户 ID',
  `spotId` bigint NOT NULL COMMENT '景点 ID',
  `status` tinyint DEFAULT '1' COMMENT '1-正常收藏 0-取消收藏',
  `remark` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '用户备注',
  `isDelete` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `unique_favorite` (`userId`,`spotId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of user_spot_favorites
-- ----------------------------

-- ----------------------------
-- Table structure for `user_spot_favorites_bak_20260323`
-- ----------------------------
DROP TABLE IF EXISTS `user_spot_favorites_bak_20260323`;
CREATE TABLE `user_spot_favorites_bak_20260323` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `userId` bigint NOT NULL COMMENT '用户 ID',
  `spotId` bigint NOT NULL COMMENT '景点 ID',
  `status` tinyint DEFAULT '1' COMMENT '1-正常收藏 0-取消收藏',
  `remark` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '用户备注',
  `isDelete` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `unique_favorite` (`userId`,`spotId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of user_spot_favorites_bak_20260323
-- ----------------------------
