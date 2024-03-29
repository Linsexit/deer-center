-- 创建库
create database if not exists lu;

-- 切换库
use lu;

-- auto-generated definition
create table user
(
    id           bigint     not null auto_increment comment 'id'
        primary key,
    username     varchar(256)                       null comment '用户昵称',
    userAccount  varchar(256)                       null comment '账户',
    avatarUrl    varchar(1024)                      null comment '用户头像',
    gender       tinyint                            null comment '性别',
    userPassword varchar(512)                       not null comment '密码',
    phone        varchar(128)                       null comment '电话',
    email        varchar(512)                       null comment '邮箱',
    userStatus   int      default 0                 not null comment '状态 0-正常',
    createTime   datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime   datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint  default 0                 not null comment '逻辑删除 0-正常',
    userRole     int      default 0                 not null comment '用户角色 0 - 普通用户 1 - 管理员',
    deerCode     varchar(512)                       null comment '小鹿编号'
)
    comment '用户';

-- 队伍表
create table team
(
    id           bigint         not null auto_increment comment 'id' primary key,
    name     varchar(256)                       null comment '队伍名称',
    description    varchar(1024)                      null comment '描述',
    maxNum   int      default 1                 not null comment '最大人数',
    expireTime   datetime  null comment '过期时间',
    userId           bigint   not null comment '用户id',
    status   int      default 0         not null comment '0 - 公开，1 - 私有 2 - 加密',
    password varchar(512)                       not null comment '密码',

    createTime   datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime   datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint  default 0                 not null comment '是否删除 0-正常'
)
    comment '队伍';

-- 用户队伍关系
create table user_team
(
    id           bigint       not null auto_increment comment 'id' primary key,
    userId           bigint   not null comment '用户id',
    teamId           bigint   not null comment '队伍id',
    joinTime   datetime  null comment '加入时间',
    createTime   datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime   datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint  default 0                 not null comment '是否删除 0-正常'
)
    comment '用户队伍关系';

create table chat
(
    id         bigint auto_increment comment 'id'
        primary key,
    userid     bigint                             not null comment '用户id',
    content    varchar(2050)                      not null comment '内容',
    createTime datetime default CURRENT_TIMESTAMP null comment '创建时间'
)
    comment '用户';

alter table user add COLUMN tags varchar(1024) null comment '标签列表';

-- auto-generated definition
create table chat
(
    id         bigint                             not null comment 'id'
        primary key,
    userid     bigint                             not null comment '用户id',
    content    varchar(2050)                      not null comment '内容',
    createTime datetime default CURRENT_TIMESTAMP null comment '创建时间'
)
    comment '用户';

