-- ============================================
-- FlowStack 完整数据库初始化脚本
-- 包含所有 DDL（表结构）和 DML（初始数据）
-- 
-- 使用方法:
--   mysql -u root -p < init.sql
--   或者
--   mysql -u root -p
--   source /path/to/init.sql
-- ============================================

-- ============================================
-- 1. 创建所有数据库
-- ============================================

CREATE DATABASE IF NOT EXISTS `flowstack_auth` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
CREATE DATABASE IF NOT EXISTS `flowstack_project` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
CREATE DATABASE IF NOT EXISTS `flowstack_task` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
CREATE DATABASE IF NOT EXISTS `flowstack_notification` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
CREATE DATABASE IF NOT EXISTS `flowstack_file` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
CREATE DATABASE IF NOT EXISTS `flowstack_search` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
CREATE DATABASE IF NOT EXISTS `flowstack_analytics` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
CREATE DATABASE IF NOT EXISTS `flowstack_user` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

-- ============================================
-- 2. User Service 数据库初始化
-- ============================================

USE `flowstack_user`;

-- 创建teams表
CREATE TABLE IF NOT EXISTS `teams` (
    `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(255) NOT NULL COMMENT '团队名称',
    `description` TEXT COMMENT '团队描述',
    `owner_id` BIGINT NOT NULL COMMENT '团队所有者ID',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_owner_id` (`owner_id`),
    INDEX `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='团队表';

-- 创建users表
CREATE TABLE IF NOT EXISTS `users` (
    `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(255) NOT NULL COMMENT '用户姓名',
    `email` VARCHAR(255) NOT NULL COMMENT '用户邮箱',
    `avatar` VARCHAR(500) COMMENT '用户头像URL',
    `team_id` BIGINT COMMENT '所属团队ID',
    `role` VARCHAR(50) NOT NULL DEFAULT 'member' COMMENT '角色: owner, admin, member',
    `status` VARCHAR(50) NOT NULL DEFAULT 'offline' COMMENT '状态: online, away, offline',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY `uk_email` (`email`),
    INDEX `idx_team_id` (`team_id`),
    INDEX `idx_email` (`email`),
    INDEX `idx_status` (`status`),
    INDEX `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户表';

-- 初始化团队数据
INSERT INTO `teams` (`id`, `name`, `description`, `owner_id`, `created_at`, `updated_at`) VALUES
(1, '开发团队', '负责产品开发和维护', 1, NOW(), NOW()),
(2, '设计团队', '负责UI/UX设计', 1, NOW(), NOW()),
(3, '产品团队', '负责产品规划和需求管理', 1, NOW(), NOW()),
(4, '测试团队', '负责质量保证和测试', 1, NOW(), NOW()),
(5, '运营团队', '负责产品运营和推广', 1, NOW(), NOW())
ON DUPLICATE KEY UPDATE `name`=VALUES(`name`), `description`=VALUES(`description`), `owner_id`=VALUES(`owner_id`), `updated_at`=VALUES(`updated_at`);

-- 初始化用户数据
INSERT INTO `users` (`id`, `name`, `email`, `avatar`, `team_id`, `role`, `status`, `created_at`, `updated_at`) VALUES
(1, 'Kyle Wong', 'kyle@example.com', 'https://api.dicebear.com/7.x/avataaars/svg?seed=Kyle', 1, 'owner', 'online', NOW(), NOW()),
(2, 'Alex Chen', 'alex@example.com', 'https://api.dicebear.com/7.x/avataaars/svg?seed=Alex', 1, 'admin', 'online', NOW(), NOW()),
(3, 'Sarah Johnson', 'sarah@example.com', 'https://api.dicebear.com/7.x/avataaars/svg?seed=Sarah', 1, 'member', 'away', NOW(), NOW()),
(4, 'Jordan Lee', 'jordan@example.com', 'https://api.dicebear.com/7.x/avataaars/svg?seed=Jordan', 2, 'admin', 'offline', NOW(), NOW()),
(5, 'Michael Brown', 'michael@example.com', 'https://api.dicebear.com/7.x/avataaars/svg?seed=Michael', 1, 'member', 'online', NOW(), NOW()),
(6, 'Emma Wilson', 'emma@example.com', 'https://api.dicebear.com/7.x/avataaars/svg?seed=Emma', 3, 'admin', 'online', NOW(), NOW()),
(7, 'David Kim', 'david@example.com', 'https://api.dicebear.com/7.x/avataaars/svg?seed=David', 2, 'member', 'offline', NOW(), NOW()),
(8, 'Lisa Zhang', 'lisa@example.com', 'https://api.dicebear.com/7.x/avataaars/svg?seed=Lisa', 3, 'member', 'online', NOW(), NOW()),
(9, 'Tom Wilson', 'tom@example.com', 'https://api.dicebear.com/7.x/avataaars/svg?seed=Tom', 4, 'admin', 'away', NOW(), NOW()),
(10, 'Sophia Martinez', 'sophia@example.com', 'https://api.dicebear.com/7.x/avataaars/svg?seed=Sophia', 4, 'member', 'online', NOW(), NOW())
ON DUPLICATE KEY UPDATE `name`=VALUES(`name`), `email`=VALUES(`email`), `avatar`=VALUES(`avatar`), `team_id`=VALUES(`team_id`), `role`=VALUES(`role`), `status`=VALUES(`status`), `updated_at`=VALUES(`updated_at`);

-- ============================================
-- 3. Project Service 数据库初始化
-- ============================================

USE `flowstack_project`;

-- 创建projects表
CREATE TABLE IF NOT EXISTS `projects` (
    `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(255) NOT NULL COMMENT '项目名称',
    `description` TEXT COMMENT '项目描述',
    `owner_id` BIGINT NOT NULL COMMENT '项目所有者ID',
    `status` VARCHAR(50) NOT NULL DEFAULT 'planning' COMMENT '项目状态: planning, active, completed',
    `deadline` DATE COMMENT '截止日期',
    `start_date` DATE COMMENT '开始日期',
    `members` INT NOT NULL DEFAULT 0 COMMENT '成员数量',
    `progress` INT NOT NULL DEFAULT 0 COMMENT '进度百分比',
    `priority` VARCHAR(20) NOT NULL DEFAULT 'MEDIUM' COMMENT '优先级: HIGH, MEDIUM, LOW',
    `starred` BOOLEAN NOT NULL DEFAULT FALSE COMMENT '是否收藏',
    `archived` BOOLEAN NOT NULL DEFAULT FALSE COMMENT '是否归档',
    `cover_image` VARCHAR(500) COMMENT '封面图片URL',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_owner_id` (`owner_id`),
    INDEX `idx_status` (`status`),
    INDEX `idx_priority` (`priority`),
    INDEX `idx_starred` (`starred`),
    INDEX `idx_archived` (`archived`),
    INDEX `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='项目表';

-- 创建项目-团队关联表
CREATE TABLE IF NOT EXISTS `project_teams` (
    `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `project_id` BIGINT NOT NULL COMMENT '项目ID',
    `team_id` BIGINT NOT NULL COMMENT '团队ID',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY `uk_project_team` (`project_id`, `team_id`),
    INDEX `idx_project_id` (`project_id`),
    INDEX `idx_team_id` (`team_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='项目-团队关联表';

-- 创建项目-成员关联表
CREATE TABLE IF NOT EXISTS `project_members` (
    `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `project_id` BIGINT NOT NULL COMMENT '项目ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `role` VARCHAR(50) NOT NULL DEFAULT 'member' COMMENT '角色: owner, admin, member',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY `uk_project_user` (`project_id`, `user_id`),
    INDEX `idx_project_id` (`project_id`),
    INDEX `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='项目-成员关联表';

-- 创建项目标签表
CREATE TABLE IF NOT EXISTS `project_tags` (
    `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `project_id` BIGINT NOT NULL COMMENT '项目ID',
    `tag` VARCHAR(50) NOT NULL COMMENT '标签名称',
    `color` VARCHAR(20) DEFAULT '#3B82F6' COMMENT '标签颜色',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY `uk_project_tag` (`project_id`, `tag`),
    INDEX `idx_project_id` (`project_id`),
    INDEX `idx_tag` (`tag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='项目标签表';

-- 初始化项目数据
INSERT INTO `projects` (`id`, `name`, `description`, `owner_id`, `status`, `deadline`, `start_date`, `members`, `progress`, `priority`, `starred`, `archived`, `cover_image`, `created_at`, `updated_at`) VALUES
(1, 'FlowBoard 前端项目', 'FlowBoard 看板应用的前端开发', 1, 'active', '2026-03-15', '2025-12-01', 5, 60, 'HIGH', FALSE, FALSE, NULL, NOW(), NOW()),
(2, 'FlowStack 后端服务', 'FlowStack 微服务架构的后端开发', 1, 'planning', '2026-06-01', '2026-01-01', 8, 20, 'MEDIUM', FALSE, FALSE, NULL, NOW(), NOW())
ON DUPLICATE KEY UPDATE 
    `name`=VALUES(`name`), 
    `description`=VALUES(`description`), 
    `owner_id`=VALUES(`owner_id`), 
    `status`=VALUES(`status`), 
    `deadline`=VALUES(`deadline`), 
    `start_date`=VALUES(`start_date`), 
    `members`=VALUES(`members`), 
    `progress`=VALUES(`progress`), 
    `priority`=VALUES(`priority`), 
    `starred`=VALUES(`starred`), 
    `archived`=VALUES(`archived`), 
    `cover_image`=VALUES(`cover_image`), 
    `updated_at`=VALUES(`updated_at`);

-- 初始化项目-团队关联数据
INSERT INTO `project_teams` (`project_id`, `team_id`, `created_at`) VALUES
(1, 1, NOW()), -- FlowBoard 前端项目由开发团队负责
(1, 2, NOW()), -- FlowBoard 前端项目由设计团队协助
(2, 1, NOW())  -- FlowStack 后端服务由开发团队负责
ON DUPLICATE KEY UPDATE `project_id`=VALUES(`project_id`), `team_id`=VALUES(`team_id`);

-- 初始化项目-成员关联数据
INSERT INTO `project_members` (`project_id`, `user_id`, `role`, `created_at`, `updated_at`) VALUES
(1, 1, 'owner', NOW(), NOW()),   -- Kyle Wong 是 FlowBoard 前端项目的 Owner
(1, 2, 'admin', NOW(), NOW()),   -- Alex Chen 是 FlowBoard 前端项目的 Admin
(1, 3, 'member', NOW(), NOW()),  -- Sarah Johnson 是 FlowBoard 前端项目的 Member
(2, 1, 'owner', NOW(), NOW()),   -- Kyle Wong 是 FlowStack 后端服务的 Owner
(2, 5, 'member', NOW(), NOW())   -- Michael Brown 是 FlowStack 后端服务的 Member
ON DUPLICATE KEY UPDATE 
    `project_id`=VALUES(`project_id`), 
    `user_id`=VALUES(`user_id`), 
    `role`=VALUES(`role`), 
    `updated_at`=VALUES(`updated_at`);

-- ============================================
-- 脚本执行完成
-- ============================================

