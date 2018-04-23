-- MySQL dump 10.10
--
-- Host: localhost    Database: edulive
-- ------------------------------------------------------
-- Server version	5.5-nt

drop database IF EXISTS edulive;
create database edulive;
use edulive;

show TABLES ;

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`(
  `phoneNum` varchar(13) PRIMARY KEY NOT NULL, -- 电话号
  `password` varchar(20) NOT NULL , -- 密码
  `name` varchar(20) NOT NULL, -- 用户姓名
  `nickname` varchar(20), -- 用户昵称
  `registerTime` timestamp NOT NULL DEFAULT NOW(), -- 用户注册时间
  `isStudent` char(1) NOT NULL DEFAULT 1 -- 1代表是学生
)DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `liveRoom`;
CREATE TABLE `liveRoom`(
  `liveRoomNum`  varchar(11) PRIMARY KEY NOT NULL, -- 直播间编号
  `roomName` varchar(40) NOT NULL, -- 直播间房间名
  `teacherId` varchar(13) NOT NULL, -- 发起直播老师的phoneNum
  `startTime` timestamp NOT NULL DEFAULT NOW(), -- 发起直播时间
  `sourcePath` VARCHAR(255), -- 直播教学资源地址
  `imgPath` varchar(255) NOT NULL, -- 直播间封面的图片地址
  `isRecording` char(1) DEFAULT 0, -- 1表示正在录像
  `gagList` TEXT, -- 禁言列表,存储学生学号,每个学生以","隔开
  `blackList` TEXT, -- 黑名单列表,存储学生学号,每个学生以","隔开
  FOREIGN KEY(teacherId) REFERENCES user(phoneNum)
)DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;

DROP TABLE IF EXISTS `video`;
CREATE TABLE `video`(
  `videoNum`  varchar(11) PRIMARY KEY NOT NULL,  -- 视频编号
  `viedoName` VARCHAR(40) NOT NULL ,-- 视频名称
  `watchCount` MEDIUMINT NOT NULL DEFAULT 0,  -- 观看人数
  `releaseTime` TIMESTAMP NOT NULL DEFAULT NOW(), -- 视频发布时间
  `teacherId` varchar(13) NOT NULL, -- 发布视频教师phoneNum
  `videoPath` VARCHAR(255) NOT NULL , -- 视频地址
  `sourcePath` VARCHAR(255), -- 视频教学资源的地址,
  `imgPath` VARCHAR(255) NOT NULL -- 视频封面图片地址
)DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;

INSERT INTO user(phoneNum, password, name, nickname) VALUE ("13112345678","root","小明","jack");
INSERT INTO user(phoneNum, password, name, nickname, isStudent) VALUE ("12112345678","admin","李老师","teacherLee",0);
INSERT INTO user(phoneNum, password, name, nickname, isStudent) VALUE ("10086","admin","王老师","teacherWang",0);
SELECT * FROM user;

INSERT INTO liveroom(liveRoomNum, roomName, teacherId,imgPath,blackList) VALUE ("10086","Java","10086","hhh","13112345678,10086");
