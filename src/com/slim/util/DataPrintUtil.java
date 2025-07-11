package com.slim.util;

import java.sql.*;
import java.util.Scanner;

/**
 * 数据打印工具类 - 支持查询并打印教师、学生、教室、选课和成绩信息
 */
public class DataPrintUtil {
    // 数据库连接信息
    private static final String DB_URL = "jdbc:mysql://localhost:3306/db_courseSelect?serverTimezone=GMT%2B8";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "123456";

    // 数据库连接
    public Connection con;
    private Scanner scanner;

    /**
     * 构造函数
     */
    public DataPrintUtil() {
        try {
            // 加载数据库驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
            // 连接数据库
            con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("数据库连接成功！");
            scanner = new Scanner(System.in);
        } catch (Exception e) {
            System.err.println("数据库连接失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 打印教师信息
     */
    public void printTeacherInfo() {
        try {
            System.out.println("\n=== 教师信息查询 ===");
            System.out.println("请choose查询方式:");
            System.out.println("1. All the teachers");
            System.out.println("2. 按职称查询teachers");
            System.out.print("Please choose 1 or 2: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // 消耗换行符

            if (choice == 1) {
                // 查看所有教师
                String sql = "SELECT * FROM t_teacher";
                try (PreparedStatement pstmt = con.prepareStatement(sql);
                     ResultSet rs = pstmt.executeQuery()) {
                    printTeacherTable(rs);
                }
            } else if (choice == 2) {
                // 按职称查询
                System.out.print("请输入职称关键词: ");
                String title = scanner.nextLine();
                String sql = "SELECT * FROM t_teacher WHERE title LIKE ?";
                try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                    pstmt.setString(1, "%" + title + "%");
                    try (ResultSet rs = pstmt.executeQuery()) {
                        printTeacherTable(rs);
                    }
                }
            } else {
                System.out.println("无效选择！");
            }
        } catch (Exception e) {
            System.err.println("查询教师信息失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 打印学生信息
     */
    public void printStudentInfo() {
        try {
            System.out.println("\n=== 学生信息查询 ===");
            System.out.println("Please choose:");
            System.out.println("1. All the students");
            System.out.println("2. 按院系查询students");
            System.out.print("choose 1 or 2: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // 消耗换行符

            if (choice == 1) {
                // 查看所有学生
                String sql = "SELECT s.*, sub_sc.courseName, sub_sc.courseId " +
                        "FROM t_sinfo s " +
                        "LEFT JOIN (SELECT sc.Sno, c.courseName, c.courseId " +
                        "FROM t_selection sc " +
                        "INNER JOIN t_course c ON sc.courseId = c.courseId) sub_sc " +
                        "ON s.Sno = sub_sc.Sno";
                try (PreparedStatement pstmt = con.prepareStatement(sql);
                     ResultSet rs = pstmt.executeQuery()) {
                    printStudentTable(rs);
                }
            } else if (choice == 2) {
                // 按院系查询
                System.out.print("请输入院系名称: ");
                String department = scanner.nextLine();
                String sql = "SELECT s.*, sub_sc.courseName, sub_sc.courseId " +
                        "FROM t_sinfo s " +
                        "LEFT JOIN (SELECT sc.Sno, c.courseName, c.courseId " +
                        "FROM t_selection sc " +
                        "INNER JOIN t_course c ON sc.courseId = c.courseId) sub_sc " +
                        "ON s.Sno = sub_sc.Sno " +
                        "WHERE s.department = ?";
                try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                    pstmt.setString(1, department);
                    try (ResultSet rs = pstmt.executeQuery()) {
                        printStudentTable(rs);
                    }
                }
            } else {
                System.out.println("无效选择！");
            }
        } catch (Exception e) {
            System.err.println("查询学生信息失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 打印教室信息
     */
    public void printClassroomInfo() {
        try {
            System.out.println("\n=== 教室信息查询 ===");
            System.out.println("Please choose:");
            System.out.println("1. All the rooms");
            System.out.println("2. Rooms at capacity:");
            System.out.print("choose 1 or 2: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // 消耗换行符

            if (choice == 1) {
                // 查看所有教室
                String sql = "SELECT * FROM t_classroom";
                try (PreparedStatement pstmt = con.prepareStatement(sql);
                     ResultSet rs = pstmt.executeQuery()) {
                    printClassroomTable(rs);
                }
            } else if (choice == 2) {
                // 按容量查询
                System.out.print("请输入最小容量: ");
                int capacity = scanner.nextInt();
                scanner.nextLine(); // 消耗换行符
                String sql = "SELECT * FROM t_classroom WHERE capacity >= ?";
                try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                    pstmt.setInt(1, capacity);
                    try (ResultSet rs = pstmt.executeQuery()) {
                        printClassroomTable(rs);
                    }
                }
            } else {
                System.out.println("无效选择！");
            }
        } catch (Exception e) {
            System.err.println("查询教室信息失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 打印选课信息
     */
    public void printSelectionInfo() {
        try {
            System.out.println("\n=== 选课信息查询 ===");
            System.out.println("Please choose:");
            System.out.println("1. All the infos");
            System.out.println("2. students at the courses");
            System.out.println("3. courses at the students");
            System.out.print("Please choose ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // 消耗换行符

            if (choice == 1) {
                // 查看所有选课记录
                String sql = "SELECT sc.*, c.courseName, s.Sname, s.department " +
                        "FROM t_selection sc " +
                        "INNER JOIN t_course c ON sc.courseId = c.courseId " +
                        "INNER JOIN t_sinfo s ON sc.Sno = s.Sno";
                try (PreparedStatement pstmt = con.prepareStatement(sql);
                     ResultSet rs = pstmt.executeQuery()) {
                    printSelectionTable(rs);
                }
            } else if (choice == 2) {
                // 按课程查询
                System.out.print("请输入课程编号: ");
                int courseId = scanner.nextInt();
                scanner.nextLine(); // 消耗换行符
                String sql = "SELECT sc.*, c.courseName, s.Sname, s.department " +
                        "FROM t_selection sc " +
                        "INNER JOIN t_course c ON sc.courseId = c.courseId " +
                        "INNER JOIN t_sinfo s ON sc.Sno = s.Sno " +
                        "WHERE sc.courseId = ?";
                try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                    pstmt.setInt(1, courseId);
                    try (ResultSet rs = pstmt.executeQuery()) {
                        printSelectionTable(rs);
                    }
                }
            } else if (choice == 3) {
                // 按学生查询（修复：添加Sname列）
                System.out.print("请输入学生学号: ");
                int sno = scanner.nextInt();
                scanner.nextLine(); // 消耗换行符
                String sql = "SELECT sc.*, c.courseName, c.courseTeacher, s.Sname, s.department " +
                        "FROM t_selection sc " +
                        "INNER JOIN t_course c ON sc.courseId = c.courseId " +
                        "INNER JOIN t_sinfo s ON sc.Sno = s.Sno " + // 添加学生表关联
                        "WHERE sc.Sno = ?";
                try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                    pstmt.setInt(1, sno);
                    try (ResultSet rs = pstmt.executeQuery()) {
                        printSelectionTable(rs);
                    }
                }
            } else {
                System.out.println("无效选择！");
            }
        } catch (Exception e) {
            System.err.println("查询选课信息失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 打印成绩信息
     */
    public void printGradeInfo() {
        try {
            System.out.println("\n=== 成绩信息查询 ===");
            System.out.println("Please choose:");
            System.out.println("1.All the records:");
            System.out.println("2. the courses");
            System.out.println("3. the students");
            System.out.print("please choose: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // 消耗换行符

            if (choice == 1) {
                // 查看所有成绩记录
                String sql = "SELECT g.*, c.courseName, s.Sname, s.department " +
                        "FROM t_grade g " +
                        "INNER JOIN t_course c ON g.courseId = c.courseId " +
                        "INNER JOIN t_sinfo s ON g.Sno = s.Sno";
                try (PreparedStatement pstmt = con.prepareStatement(sql);
                     ResultSet rs = pstmt.executeQuery()) {
                    printGradeTable(rs);
                }
            } else if (choice == 2) {
                // 按课程查询
                System.out.print("请输入课程编号: ");
                int courseId = scanner.nextInt();
                scanner.nextLine(); // 消耗换行符
                String sql = "SELECT g.*, c.courseName, s.Sname, s.department " +
                        "FROM t_grade g " +
                        "INNER JOIN t_course c ON g.courseId = c.courseId " +
                        "INNER JOIN t_sinfo s ON g.Sno = s.Sno " +
                        "WHERE g.courseId = ?";
                try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                    pstmt.setInt(1, courseId);
                    try (ResultSet rs = pstmt.executeQuery()) {
                        printGradeTable(rs);
                    }
                }
            } else if (choice == 3) {
                // 按学生查询
                System.out.print("请输入学生学号: ");
                int sno = scanner.nextInt();
                scanner.nextLine(); // 消耗换行符
                String sql = "SELECT g.*, c.courseName, s.Sname, s.department " +
                        "FROM t_grade g " +
                        "INNER JOIN t_course c ON g.courseId = c.courseId " +
                        "INNER JOIN t_sinfo s ON g.Sno = s.Sno " + // 添加学生表关联
                        "WHERE g.Sno = ?";
                try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                    pstmt.setInt(1, sno);
                    try (ResultSet rs = pstmt.executeQuery()) {
                        printGradeTable(rs);
                    }
                }
            } else {
                System.out.println("无效选择！");
            }
        } catch (Exception e) {
            System.err.println("查询成绩信息失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 打印教师信息表
     */
    private void printTeacherTable(ResultSet rs) throws SQLException {
        System.out.println("============ 教师信息 ============");
        System.out.printf("%-6s\t%-12s\t%-4s\t%-4s\t%-12s\t%-8s\t%-16s\t%-8s\n",
                "教师编号", "姓名", "性别", "年龄", "学历", "职称", "毕业院校", "健康状况");
        while (rs.next()) {
            System.out.printf("%-6d\t%-12s\t%-4s\t%-4d\t%-12s\t%-8s\t%-16s\t%-8s\n",
                    rs.getInt("teacherId"),
                    rs.getString("teacherName"),
                    rs.getString("teacherSex"),
                    rs.getInt("teacherAge"),
                    rs.getString("education"),
                    rs.getString("title"),
                    rs.getString("gradSchool"),
                    rs.getString("healthStatus"));
        }
        System.out.println("==================================");
    }

    /**
     * 打印学生信息表（含已选课情况）
     */
    private void printStudentTable(ResultSet rs) throws SQLException {
        System.out.println("============ 学生信息表 ============");
        System.out.printf("%-8s\t%-8s\t%-8s\t%-8s\t%-12s\t%-12s\t%-12s\n",
                "学号", "姓名", "性别", "院系", "电话", "已选课程编号", "已选课程名称");
        while (rs.next()) {
            System.out.printf("%-8d\t%-8s\t%-8s\t%-8s\t%-12s\t%-12s\t%-12s\n",
                    rs.getInt("Sno"),
                    rs.getString("Sname"),
                    rs.getString("Stele"),
                    rs.getString("department"),
                    rs.getString("phone"),
                    rs.getString("courseId"),
                    rs.getString("courseName"));
        }
        System.out.println("==================================");
    }

    /**
     * 打印教室信息表
     */
    private void printClassroomTable(ResultSet rs) throws SQLException {
        System.out.println("============ 教室信息表 ============");
        System.out.printf("%-8s\t%-8s\t%-6s\t%-8s\n",
                "教室编号", "教室名称", "容量", "空闲时间");
        while (rs.next()) {
            System.out.printf("%-8d\t%-8s\t%-6d\t%-8s\n",
                    rs.getInt("classroomId"),
                    rs.getString("classroomName"),
                    rs.getInt("capacity"),
                    rs.getString("freeTime"));
        }
        System.out.println("==================================");
    }

    /**
     * 打印选课信息表
     */
    private void printSelectionTable(ResultSet rs) throws SQLException {
        System.out.println("============ 选课信息表 ============");
        System.out.printf("%-8s\t%-8s\t%-8s\t%-8s\t%-8s\t%-8s\n",
                "选课编号", "课程编号", "课程名称", "学号", "学生姓名", "学生院系");
        while (rs.next()) {
            System.out.printf("%-8d\t%-8d\t%-8s\t%-8d\t%-8s\t%-8s\n",
                    rs.getInt("selectionId"),
                    rs.getInt("courseId"),
                    rs.getString("courseName"),
                    rs.getInt("Sno"),
                    rs.getString("Sname"),
                    rs.getString("department"));
        }
        System.out.println("==================================");
    }

    /**
     * 打印成绩信息表
     */
    private void printGradeTable(ResultSet rs) throws SQLException {
        System.out.println("============ 成绩信息表 ============");
        System.out.printf("%-8s\t%-8s\t%-8s\t%-6s\t%-8s\t%-8s\t%-8s\t%-8s\n",
                "成绩编号", "课程编号", "课程名称", "学分", "学号", "学生姓名", "学生院系", "成绩");
        while (rs.next()) {
            System.out.printf("%-8d\t%-8d\t%-8s\t%-6d\t%-8d\t%-8s\t%-8s\t%-8d\n",
                    rs.getInt("gradeId"),
                    rs.getInt("courseId"),
                    rs.getString("courseName"),
                    rs.getInt("credit"),
                    rs.getInt("Sno"),
                    rs.getString("Sname"),
                    rs.getString("department"),
                    rs.getInt("score"));
        }
        System.out.println("==================================");
    }

    /**
     * 关闭资源
     */
    public void close() {
        try {
            if (con != null && !con.isClosed()) con.close();
            if (scanner != null) scanner.close();
            System.out.println("资源已关闭");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 主函数
     */
    public static void main(String[] args) {
        DataPrintUtil printUtil = new DataPrintUtil();
        boolean running = true;

        try {
            while (running) {
                System.out.println("\n=== 数据查询与打印System ===");
                System.out.println("1. 打印教师信息");
                System.out.println("2. 打印学生信息");
                System.out.println("3. 打印教室信息");
                System.out.println("4. 打印选课信息");
                System.out.println("5. 打印成绩信息");
                System.out.println("0. Quit");
                System.out.print("Please choose between 0 ~ 5: ");

                int choice = printUtil.scanner.nextInt();
                printUtil.scanner.nextLine(); // 消耗换行符
                switch (choice) {
                    case 1:
                        printUtil.printTeacherInfo();
                        break;
                    case 2:
                        printUtil.printStudentInfo();
                        break;
                    case 3:
                        printUtil.printClassroomInfo();
                        break;
                    case 4:
                        printUtil.printSelectionInfo();
                        break;
                    case 5:
                        printUtil.printGradeInfo();
                        break;
                    case 0:
                        running = false;
                        System.out.println("感谢使用，再见！");
                        break;
                    default:
                        System.out.println("无效选择，请重新输入！");
                }
            }
        } catch (Exception e) {
            System.err.println("程序运行异常: " + e.getMessage());
            e.printStackTrace();
        } finally {
            printUtil.close();
        }
    }
}