# 这是一个数据库课设作业 By MySQL & JAVA 
-----
## 课设作业要求：
 * 学生选课管理信息系统
 * 系统功能基本要求
 * 教师信息,包括教师编号、教师姓名、性别、年龄、学历、职称、毕业院校，健康状况等。
 * 学生信息，包括学号、姓名、所属院系、已选课情况等。
 * 教室信息，包括，可容纳人数、空闲时间等。
 * 选课信息，包括课程编号、课程名称、任课教师、选课的学生情况等。
 * 成绩信息，包括课程编号、课程名称、学分、成绩。
 * 按一定条件可以查询，并将结果打印输出。
---

## 图项目展示：
<img width="1217" height="834" alt="image" src="https://github.com/user-attachments/assets/caa4d3e7-f3f5-4dad-8163-e7ab2d9c4d57" />
>>> *图一:查询所有学生信息*


<img width="1220" height="813" alt="image" src="https://github.com/user-attachments/assets/61a0d9b8-6d89-48e6-b0f2-4c248d2b0010" />
>>> *图二：按照学生的学院属性查询*



**右上角添加了night mood丰富了UI配色方案**
<img width="1177" height="765" alt="image" src="https://github.com/user-attachments/assets/74d77342-da85-4364-8f41-d1d9cabe4d55" />
>>> *图三:夜间模式展示*

***其他课设作业任务（如查询教室信息）均满足，这里不做展示***

---
## 注：使用需要链接MySQL，代码直接注入，点击小闪电运行
<img width="1919" height="973" alt="image" src="https://github.com/user-attachments/assets/c0ace528-dba8-4f65-b70a-ec77dc2b140a" />
>>> *图四: Mysql使用教程*

## 原数据库代码 .sql

```
-- ================ 1. 创建数据库（如果不存在） ================
CREATE DATABASE IF NOT EXISTS db_courseSelect
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE db_courseSelect;

-- ================ 2. 删除旧表 ================
DROP TABLE IF EXISTS t_grade;
DROP TABLE IF EXISTS t_selection;
DROP TABLE IF EXISTS t_course;
DROP TABLE IF EXISTS t_classroom;
DROP TABLE IF EXISTS t_teacher;
DROP TABLE IF EXISTS t_sinfo;
DROP TABLE IF EXISTS t_slogon;
DROP TABLE IF EXISTS t_adminlogon;


-- ================ 3. 基础数据表创建 ================

-- 教师信息表（含完整外键关联准备）
CREATE TABLE t_teacher (
                           teacherId INT NOT NULL AUTO_INCREMENT COMMENT '教师编号',
                           teacherName VARCHAR(20) NOT NULL COMMENT '教师姓名',
                           teacherSex ENUM('male', 'female') NOT NULL COMMENT '性别',
                           teacherAge INT NOT NULL COMMENT '年龄',
                           education VARCHAR(20) NOT NULL COMMENT '学历',
                           title VARCHAR(20) NOT NULL COMMENT '职称',
                           gradSchool VARCHAR(50) NOT NULL COMMENT '毕业院校',
                           healthStatus VARCHAR(20) NOT NULL COMMENT '健康状况',
                           PRIMARY KEY (teacherId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 教室信息表
CREATE TABLE t_classroom (
                             classroomId INT NOT NULL AUTO_INCREMENT COMMENT '教室编号',
                             classroomName VARCHAR(20) NOT NULL COMMENT '教室名称',
                             capacity INT NOT NULL COMMENT '容纳人数',
                             freeTime VARCHAR(100) NOT NULL COMMENT '空闲时间（如：周一1-2节,周三3-4节）',
                             PRIMARY KEY (classroomId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 学生登录表（账号密码）
CREATE TABLE t_slogon (
                          Sno INT NOT NULL COMMENT '学号（主键）',
                          Spassword VARCHAR(20) NOT NULL COMMENT '登录密码',
                          PRIMARY KEY (Sno)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 学生信息表（详细信息，性别字段规范为ENUM）
CREATE TABLE t_sinfo (
                         Sno INT NOT NULL COMMENT '学号（关联登录表）',
                         Sname VARCHAR(20) NOT NULL COMMENT '学生姓名',
                         Stele ENUM('male', 'female') NOT NULL COMMENT '性别',
                         department VARCHAR(30) NOT NULL COMMENT '所属院系',
                         phone VARCHAR(20) COMMENT '联系电话',
                         PRIMARY KEY (Sno),
                         CONSTRAINT FK_sinfo_slogon
                             FOREIGN KEY (Sno) REFERENCES t_slogon(Sno)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 管理员登录表
CREATE TABLE t_adminlogon (
                              adminId INT NOT NULL AUTO_INCREMENT COMMENT '管理员编号',
                              password VARCHAR(20) NOT NULL COMMENT '登录密码',
                              PRIMARY KEY (adminId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- ================ 4. 核心业务表创建（含外键直接定义） ================

-- 课程表（外键在表定义中直接添加）
CREATE TABLE t_course (
                          courseId INT NOT NULL AUTO_INCREMENT COMMENT '课程编号',
                          courseName VARCHAR(20) NOT NULL COMMENT '课程名称',
                          courseTime VARCHAR(50) NOT NULL COMMENT '上课时间',
                          courseTeacher VARCHAR(20) NOT NULL COMMENT '授课教师（冗余字段）',
                          capacity INT NOT NULL COMMENT '课程容量',
                          teacherId INT NOT NULL COMMENT '关联教师表',
                          classroomId INT NOT NULL COMMENT '关联教室表',
                          numSelected INT NOT NULL DEFAULT 0 COMMENT '已选人数',
                          PRIMARY KEY (courseId),
                          CONSTRAINT FK_course_teacher
                              FOREIGN KEY (teacherId) REFERENCES t_teacher(teacherId),
                          CONSTRAINT FK_course_classroom
                              FOREIGN KEY (classroomId) REFERENCES t_classroom(classroomId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 选课记录表（关联课程、学生）
CREATE TABLE t_selection (
                             selectionId INT NOT NULL AUTO_INCREMENT COMMENT '选课记录编号',
                             courseId INT NOT NULL COMMENT '关联课程表',
                             Sno INT NOT NULL COMMENT '关联学生表',
                             PRIMARY KEY (selectionId),
                             CONSTRAINT FK_selection_course
                                 FOREIGN KEY (courseId) REFERENCES t_course(courseId),
                             CONSTRAINT FK_selection_student
                                 FOREIGN KEY (Sno) REFERENCES t_sinfo(Sno)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 成绩表（关联课程、学生）
CREATE TABLE t_grade (
                         gradeId INT NOT NULL AUTO_INCREMENT COMMENT '成绩记录编号',
                         courseId INT NOT NULL COMMENT '关联课程表',
                         courseName VARCHAR(20) NOT NULL COMMENT '课程名称（冗余字段）',
                         credit INT NOT NULL COMMENT '课程学分',
                         Sno INT NOT NULL COMMENT '关联学生表',
                         score INT COMMENT '学生成绩',
                         PRIMARY KEY (gradeId),
                         CONSTRAINT FK_grade_course
                             FOREIGN KEY (courseId) REFERENCES t_course(courseId),
                         CONSTRAINT FK_grade_student
                             FOREIGN KEY (Sno) REFERENCES t_sinfo(Sno)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- ================ 5. 插入初始化数据（不指定自增ID） ================
SET FOREIGN_KEY_CHECKS = 0; -- 临时关闭外键检查

-- 插入默认教师（自动生成teacherId）
INSERT INTO t_teacher (teacherName, teacherSex, teacherAge, education, title, gradSchool, healthStatus)
VALUES
('Mr. Bi', 'male', 28, 'Doctor', '院长', 'Harvel University', 'healthy'),
('Wang Ying Jie', 'female', 26, 'Doctor', 'professor', 'Yale University', 'beautiful'),
('Wang Hong Da', 'male', 5, 'kingdergartern', 'cleaner', 'YTU', 'good');

-- 插入默认教室（自动生成classroomId）
INSERT INTO t_classroom (classroomName, capacity, freeTime)
VALUES
('6402', 50, 'From Monday to Saturday_14:00-17:00'),
('4th_205', 40, 'Tuesday 3-4, Thursday3-4'),
('N629_Dorm', 16, '19:00_Everyday');

-- 插入默认课程（关联教师和教室，自动生成courseId）
INSERT INTO t_course (courseName, courseTime, courseTeacher, capacity, teacherId, classroomId, numSelected)
VALUES
('OS', 'Monday 1-2', 'Mr Bi', 50, 1, 1, 0),
('SQL', 'Tuesday 3-4', 'Wang Ying Jie', 40, 2, 2, 0),
('瓦罗兰特', 'Wednesday 5-6', 'Wang Hong Da', 30, 3, 3, 0),
('Sing', 'Friday Singer Show', '马嘉祺', 888, 4, 3, 0);

-- 学生登录数据
INSERT INTO t_slogon (Sno, Spassword) VALUES (5528, '123456');

-- 学生信息数据（性别规范为ENUM）
INSERT INTO t_sinfo (Sno, Sname, Stele, department, phone)
VALUES
(5528, 'Li Zheng', 'male', '计控学院', '123456789'),
(5530, 'Ma Wen Hao', 'male', '计控学院', '110120'),
(2256, 'Talor Swift', 'female', 'Law school', '5201314');


-- 管理员数据
INSERT INTO t_adminlogon (password) VALUES ('admin123');

-- 选课记录（学生5528选了课程3）
INSERT INTO t_selection (courseId, Sno) VALUES (3, 5528);

-- 成绩记录
INSERT INTO t_grade (courseId, courseName, credit, Sno, score)
VALUES
(1, 'OS', 3, 5528, 96),
(4, 'Sing', 2, 2256, 998);

SET FOREIGN_KEY_CHECKS = 1; -- 恢复外键检查


-- ================ 6. 验证外键关联（可选查询） ================
SELECT
    TABLE_NAME,
    COLUMN_NAME,
    CONSTRAINT_NAME,
    REFERENCED_TABLE_NAME,
    REFERENCED_COLUMN_NAME
FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE
WHERE REFERENCED_TABLE_SCHEMA = 'db_courseSelect';


-- ================ 7. 常用查询示例 ================
-- 查询学生未选课程（预编译参数化查询示例）
-- SELECT c.* FROM t_course c LEFT JOIN t_selection s ON c.courseId = s.courseId AND s.Sno = ? WHERE s.Sno IS NULL;

-- 查询某教师授课的课程
-- SELECT c.* FROM t_course c WHERE c.teacherId = 2;

-- 查询学生已选课程
-- SELECT c.* FROM t_selection s JOIN t_course c ON s.courseId = c.courseId WHERE s.Sno = ?;


```
