package com.slim.ui;

import com.slim.util.DataPrintUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 课设作业要求：
 * 学生选课管理信息系统
 * 系统功能基本要求
 * 教师信息,包括教师编号、教师姓名、性别、年龄、学历、职称、毕业院校，健康状况等。
 * 学生信息，包括学号、姓名、所属院系、已选课情况等。
 * 教室信息，包括，可容纳人数、空闲时间等。
 * 选课信息，包括课程编号、课程名称、任课教师、选课的学生情况等。
 * 成绩信息，包括课程编号、课程名称、学分、成绩。
 * 按一定条件可以查询，并将结果打印输出。
 */

public class DataPrintUI extends JFrame {
    // 主题颜色定义 - 改为成员变量以便切换
    private Color PRIMARY_COLOR = new Color(42, 130, 218);
    private Color SECONDARY_COLOR = new Color(65, 158, 233);
    private Color BACKGROUND_COLOR = new Color(245, 247, 250);
    private Color PANEL_BACKGROUND = new Color(255, 255, 255);
    private Color TABLE_HEAD_COLOR = new Color(230, 238, 247);
    private Color BUTTON_HOVER = new Color(32, 109, 189);
    private Color ERROR_COLOR = new Color(220, 53, 69);
    private Color TEXT_COLOR = Color.BLACK;

    // 黑夜模式颜色定义
    private final Color DARK_PRIMARY_COLOR = new Color(70, 130, 180);
    private final Color DARK_SECONDARY_COLOR = new Color(100, 149, 237);
    private final Color DARK_BACKGROUND_COLOR = new Color(45, 45, 48);
    private final Color DARK_PANEL_BACKGROUND = new Color(60, 63, 65);
    private final Color DARK_TABLE_HEAD_COLOR = new Color(70, 70, 80);
    private final Color DARK_BUTTON_HOVER = new Color(50, 120, 180);
    private final Color DARK_TEXT_COLOR = new Color(220, 220, 220);

    // 字体定义
    private static final Font MAIN_FONT = new Font("Microsoft YaHei", Font.PLAIN, 14);
    private static final Font TITLE_FONT = new Font("Microsoft YaHei", Font.BOLD, 16);
    private static final Font BUTTON_FONT = new Font("Microsoft YaHei", Font.BOLD, 14);
    private static final Font TABLE_FONT = new Font("Microsoft YaHei", Font.PLAIN, 13);

    private DataPrintUtil dataPrintUtil;
    private JTabbedPane tabbedPane;
    private boolean darkMode = false;

    // 各标签页组件
    private JComboBox<String> teacherQueryType;
    private JTextField teacherTitleField;
    private JTable teacherTable;
    private DefaultTableModel teacherTableModel;

    private JComboBox<String> studentQueryType;
    private JTextField studentDepartmentField;
    private JTable studentTable;
    private DefaultTableModel studentTableModel;

    private JComboBox<String> classroomQueryType;
    private JTextField classroomCapacityField;
    private JTable classroomTable;
    private DefaultTableModel classroomTableModel;

    private JComboBox<String> selectionQueryType;
    private JTextField courseIdField;
    private JTextField studentIdField;
    private JTable selectionTable;
    private DefaultTableModel selectionTableModel;

    private JComboBox<String> gradeQueryType;
    private JTextField gradeCourseIdField;
    private JTextField gradeStudentIdField;
    private JTable gradeTable;
    private DefaultTableModel gradeTableModel;

    public DataPrintUI() {
        // 初始化数据库工具
        dataPrintUtil = new DataPrintUtil();

        // 设置窗口属性
        setTitle("235 Group_6    Data Query and Display System");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setBackground(BACKGROUND_COLOR);

        // 配置全局字体
        UIManager.put("Label.font", MAIN_FONT);
        UIManager.put("TextField.font", MAIN_FONT);
        UIManager.put("ComboBox.font", MAIN_FONT);
        UIManager.put("Button.font", BUTTON_FONT);
        UIManager.put("Table.font", TABLE_FONT);
        UIManager.put("TableHeader.font", new Font("Microsoft YaHei", Font.BOLD, 14));

        // 创建标签页
        tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(BACKGROUND_COLOR);
        tabbedPane.setForeground(PRIMARY_COLOR);
        tabbedPane.setFont(MAIN_FONT);

        // 添加功能标签页
        addTeacherTab();
        addStudentTab();
        addClassroomTab();
        addSelectionTab();
        addGradeTab();

        // 创建模式切换按钮
        JButton modeToggleButton = new JButton("If you love your eyes --> Night Mode  --> ");
        modeToggleButton.setFont(BUTTON_FONT);
        modeToggleButton.setBackground(PRIMARY_COLOR);
        modeToggleButton.setForeground(Color.WHITE);
        modeToggleButton.setBorderPainted(false);
        modeToggleButton.setFocusPainted(false);
        modeToggleButton.addActionListener(e -> toggleDarkMode());

        // 添加按钮到顶部面板
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.setBackground(BACKGROUND_COLOR);
        topPanel.add(modeToggleButton);

        // 添加组件到窗口
        getContentPane().add(topPanel, BorderLayout.NORTH);
        getContentPane().add(tabbedPane, BorderLayout.CENTER);

        // 显示窗口
        setVisible(true);
    }

    /**
     * 切换白天/黑夜模式
     */
    private void toggleDarkMode() {
        darkMode = !darkMode;
        updateColors();
        repaint();
    }

    /**
     * 更新所有组件的颜色
     */
    private void updateColors() {
        if (darkMode) {
            PRIMARY_COLOR = DARK_PRIMARY_COLOR;
            SECONDARY_COLOR = DARK_SECONDARY_COLOR;
            BACKGROUND_COLOR = DARK_BACKGROUND_COLOR;
            PANEL_BACKGROUND = DARK_PANEL_BACKGROUND;
            TABLE_HEAD_COLOR = DARK_TABLE_HEAD_COLOR;
            BUTTON_HOVER = DARK_BUTTON_HOVER;
            TEXT_COLOR = DARK_TEXT_COLOR;
        } else {
            PRIMARY_COLOR = new Color(42, 130, 218);
            SECONDARY_COLOR = new Color(65, 158, 233);
            BACKGROUND_COLOR = new Color(245, 247, 250);
            PANEL_BACKGROUND = new Color(255, 255, 255);
            TABLE_HEAD_COLOR = new Color(230, 238, 247);
            BUTTON_HOVER = new Color(32, 109, 189);
            TEXT_COLOR = Color.BLACK;
        }

        // 更新主窗口背景
        getContentPane().setBackground(BACKGROUND_COLOR);

        // 更新标签页
        tabbedPane.setBackground(BACKGROUND_COLOR);
        tabbedPane.setForeground(PRIMARY_COLOR);

        // 更新所有组件
        updateComponentColors(getContentPane());
    }

    /**
     * 递归更新组件颜色
     */
    private void updateComponentColors(Component component) {
        if (component instanceof Container) {
            for (Component child : ((Container) component).getComponents()) {
                updateComponentColors(child);
            }
        }

        // 设置背景和前景色
        if (component instanceof JPanel) {
            component.setBackground(PANEL_BACKGROUND);
            component.setForeground(TEXT_COLOR);
        } else if (component instanceof JLabel) {
            component.setForeground(TEXT_COLOR);
        } else if (component instanceof JTextField) {
            component.setBackground(PANEL_BACKGROUND);
            component.setForeground(TEXT_COLOR);
        } else if (component instanceof JComboBox) {
            component.setBackground(PANEL_BACKGROUND);
            component.setForeground(TEXT_COLOR);
        } else if (component instanceof JButton) {
            JButton button = (JButton) component;
            if (!button.getText().equals("🌙 Night Mode") && !button.getText().equals("☀️ Day Mode")) {
                button.setBackground(PRIMARY_COLOR);
                button.setForeground(Color.WHITE);
            }
        } else if (component instanceof JTabbedPane) {
            component.setBackground(BACKGROUND_COLOR);
        } else if (component instanceof JTable) {
            JTable table = (JTable) component;
            table.setBackground(PANEL_BACKGROUND);
            table.setForeground(TEXT_COLOR);
            table.setGridColor(darkMode ? new Color(100, 100, 100) : new Color(220, 220, 220));

            // 更新表头
            JTableHeader header = table.getTableHeader();
            header.setBackground(TABLE_HEAD_COLOR);
            header.setForeground(darkMode ? DARK_TEXT_COLOR : PRIMARY_COLOR);
        } else if (component instanceof JScrollPane) {
            component.setBackground(BACKGROUND_COLOR);
        }
    }

    /**
     * 添加教师信息查询标签页
     */
    private void addTeacherTab() {
        JPanel panel = createStyledPanel();

        // 创建查询条件面板
        JPanel queryPanel = createQueryPanel();
        teacherQueryType = createQueryComboBox(new String[]{"Query All Teachers", "Query by Title"});
        teacherTitleField = createTextField(15);
        teacherTitleField.setEnabled(false);

        JButton queryButton = createPrimaryButton("Query");
        queryButton.addActionListener(e -> queryTeacherInfo());

        teacherQueryType.addActionListener(e -> teacherTitleField.setEnabled(teacherQueryType.getSelectedIndex() == 1));

        queryPanel.add(teacherQueryType);
        queryPanel.add(createLabel("Title Keyword:"));
        queryPanel.add(teacherTitleField);
        queryPanel.add(queryButton);

        // 创建表格模型和表格
        String[] teacherColumns = {"Teacher ID", "Name", "Gender", "Age", "Education", "Title", "Graduate School", "Health Status"};
        teacherTableModel = new DefaultTableModel(teacherColumns, 0);
        teacherTable = createStyledTable(teacherTableModel);
        JScrollPane scrollPane = createScrollPane(teacherTable);

        // 添加组件到面板
        panel.add(queryPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        // 添加标签页
        tabbedPane.addTab("Teacher Info", null, panel, "Query and display teacher information");
    }

    /**
     * 查询教师信息
     */
    private void queryTeacherInfo() {
        clearTable(teacherTableModel);

        try {
            if (teacherQueryType.getSelectedIndex() == 0) {
                // 查询所有教师
                String sql = "SELECT * FROM t_teacher";
                ResultSet rs = dataPrintUtil.con.prepareStatement(sql).executeQuery();
                populateTeacherTable(rs);
            } else {
                // 按职称查询
                String title = teacherTitleField.getText();
                String sql = "SELECT * FROM t_teacher WHERE title LIKE ?";
                java.sql.PreparedStatement pstmt = dataPrintUtil.con.prepareStatement(sql);
                pstmt.setString(1, "%" + title + "%");
                ResultSet rs = pstmt.executeQuery();
                populateTeacherTable(rs);
            }
        } catch (SQLException ex) {
            showErrorDialog("Failed to query teacher information: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * 填充教师表格数据
     */
    private void populateTeacherTable(ResultSet rs) throws SQLException {
        while (rs.next()) {
            Object[] row = {
                    rs.getInt("teacherId"),
                    rs.getString("teacherName"),
                    rs.getString("teacherSex"),
                    rs.getInt("teacherAge"),
                    rs.getString("education"),
                    rs.getString("title"),
                    rs.getString("gradSchool"),
                    rs.getString("healthStatus")
            };
            teacherTableModel.addRow(row);
        }
    }

    /**
     * 添加学生信息查询标签页
     */
    private void addStudentTab() {
        JPanel panel = createStyledPanel();

        // 创建查询条件面板
        JPanel queryPanel = createQueryPanel();
        studentQueryType = createQueryComboBox(new String[]{"Query All Students", "Query by Department"});
        studentDepartmentField = createTextField(15);
        studentDepartmentField.setEnabled(false);

        JButton queryButton = createPrimaryButton("Query");
        queryButton.addActionListener(e -> queryStudentInfo());

        studentQueryType.addActionListener(e -> studentDepartmentField.setEnabled(studentQueryType.getSelectedIndex() == 1));

        queryPanel.add(studentQueryType);
        queryPanel.add(createLabel("Department:"));
        queryPanel.add(studentDepartmentField);
        queryPanel.add(queryButton);

        // 创建表格模型和表格
        String[] studentColumns = {"Student ID", "Name", "Gender", "Department", "Phone", "Course ID", "Course Name"};
        studentTableModel = new DefaultTableModel(studentColumns, 0);
        studentTable = createStyledTable(studentTableModel);
        JScrollPane scrollPane = createScrollPane(studentTable);

        // 添加组件到面板
        panel.add(queryPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        // 添加标签页
        tabbedPane.addTab("Student Info", null, panel, "Query and display student information");
    }

    /**
     * 查询学生信息
     */
    private void queryStudentInfo() {
        clearTable(studentTableModel);

        try {
            if (studentQueryType.getSelectedIndex() == 0) {
                // 查询所有学生
                String sql = "SELECT s.*, sub_sc.courseName, sub_sc.courseId " +
                        "FROM t_sinfo s " +
                        "LEFT JOIN (SELECT sc.Sno, c.courseName, c.courseId " +
                        "FROM t_selection sc " +
                        "INNER JOIN t_course c ON sc.courseId = c.courseId) sub_sc " +
                        "ON s.Sno = sub_sc.Sno";
                ResultSet rs = dataPrintUtil.con.prepareStatement(sql).executeQuery();
                populateStudentTable(rs);
            } else {
                // 按院系查询
                String department = studentDepartmentField.getText();
                String sql = "SELECT s.*, sub_sc.courseName, sub_sc.courseId " +
                        "FROM t_sinfo s " +
                        "LEFT JOIN (SELECT sc.Sno, c.courseName, c.courseId " +
                        "FROM t_selection sc " +
                        "INNER JOIN t_course c ON sc.courseId = c.courseId) sub_sc " +
                        "ON s.Sno = sub_sc.Sno " +
                        "WHERE s.department = ?";
                java.sql.PreparedStatement pstmt = dataPrintUtil.con.prepareStatement(sql);
                pstmt.setString(1, department);
                ResultSet rs = pstmt.executeQuery();
                populateStudentTable(rs);
            }
        } catch (SQLException ex) {
            showErrorDialog("Failed to query student information: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * 填充学生表格数据
     */
    private void populateStudentTable(ResultSet rs) throws SQLException {
        while (rs.next()) {
            Object[] row = {
                    rs.getInt("Sno"),
                    rs.getString("Sname"),
                    rs.getString("Stele"),
                    rs.getString("department"),
                    rs.getString("phone"),
                    rs.getString("courseId"),
                    rs.getString("courseName")
            };
            studentTableModel.addRow(row);
        }
    }

    /**
     * 添加教室信息查询标签页
     */
    private void addClassroomTab() {
        JPanel panel = createStyledPanel();

        // 创建查询条件面板
        JPanel queryPanel = createQueryPanel();
        classroomQueryType = createQueryComboBox(new String[]{"Query All Classrooms", "Query by Capacity"});
        classroomCapacityField = createTextField(15);
        classroomCapacityField.setEnabled(false);

        JButton queryButton = createPrimaryButton("Query");
        queryButton.addActionListener(e -> queryClassroomInfo());

        classroomQueryType.addActionListener(e -> classroomCapacityField.setEnabled(classroomQueryType.getSelectedIndex() == 1));

        queryPanel.add(classroomQueryType);
        queryPanel.add(createLabel("Minimum Capacity:"));
        queryPanel.add(classroomCapacityField);
        queryPanel.add(queryButton);

        // 创建表格模型和表格
        String[] classroomColumns = {"Classroom ID", "Classroom Name", "Capacity", "Free Time"};
        classroomTableModel = new DefaultTableModel(classroomColumns, 0);
        classroomTable = createStyledTable(classroomTableModel);
        JScrollPane scrollPane = createScrollPane(classroomTable);

        // 添加组件到面板
        panel.add(queryPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        // 添加标签页
        tabbedPane.addTab("Classroom Info", null, panel, "Query and display classroom information");
    }

    /**
     * 查询教室信息
     */
    private void queryClassroomInfo() {
        clearTable(classroomTableModel);

        try {
            if (classroomQueryType.getSelectedIndex() == 0) {
                // 查询所有教室
                String sql = "SELECT * FROM t_classroom";
                ResultSet rs = dataPrintUtil.con.prepareStatement(sql).executeQuery();
                populateClassroomTable(rs);
            } else {
                // 按容量查询
                int capacity = Integer.parseInt(classroomCapacityField.getText());
                String sql = "SELECT * FROM t_classroom WHERE capacity >= ?";
                java.sql.PreparedStatement pstmt = dataPrintUtil.con.prepareStatement(sql);
                pstmt.setInt(1, capacity);
                ResultSet rs = pstmt.executeQuery();
                populateClassroomTable(rs);
            }
        } catch (SQLException ex) {
            showErrorDialog("Failed to query classroom information: " + ex.getMessage());
            ex.printStackTrace();
        } catch (NumberFormatException ex) {
            showErrorDialog("Please enter a valid number");
        }
    }

    /**
     * 填充教室表格数据
     */
    private void populateClassroomTable(ResultSet rs) throws SQLException {
        while (rs.next()) {
            Object[] row = {
                    rs.getInt("classroomId"),
                    rs.getString("classroomName"),
                    rs.getInt("capacity"),
                    rs.getString("freeTime")
            };
            classroomTableModel.addRow(row);
        }
    }

    /**
     * 添加选课信息查询标签页
     */
    private void addSelectionTab() {
        JPanel panel = createStyledPanel();

        // 创建查询条件面板
        JPanel queryPanel = createQueryPanel();
        selectionQueryType = createQueryComboBox(new String[]{"Query All Selections", "Query by Course", "Query by Student"});
        courseIdField = createTextField(10);
        studentIdField = createTextField(10);
        courseIdField.setEnabled(false);
        studentIdField.setEnabled(false);

        JButton queryButton = createPrimaryButton("Query");
        queryButton.addActionListener(e -> querySelectionInfo());

        selectionQueryType.addActionListener(e -> {
            int index = selectionQueryType.getSelectedIndex();
            courseIdField.setEnabled(index == 1);
            studentIdField.setEnabled(index == 2);
            courseIdField.setText("");
            studentIdField.setText("");
        });

        queryPanel.add(selectionQueryType);
        queryPanel.add(createLabel("Course ID:"));
        queryPanel.add(courseIdField);
        queryPanel.add(createLabel("Student ID:"));
        queryPanel.add(studentIdField);
        queryPanel.add(queryButton);

        // 创建表格模型和表格
        String[] selectionColumns = {"Selection ID", "Course ID", "Course Name", "Student ID", "Student Name", "Department"};
        selectionTableModel = new DefaultTableModel(selectionColumns, 0);
        selectionTable = createStyledTable(selectionTableModel);
        JScrollPane scrollPane = createScrollPane(selectionTable);

        // 添加组件到面板
        panel.add(queryPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        // 添加标签页
        tabbedPane.addTab("Selection Info", null, panel, "Query and display course selection information");
    }

    /**
     * 查询选课信息
     */
    private void querySelectionInfo() {
        clearTable(selectionTableModel);

        try {
            int index = selectionQueryType.getSelectedIndex();
            if (index == 0) {
                // 查询所有选课
                String sql = "SELECT sc.*, c.courseName, s.Sname, s.department " +
                        "FROM t_selection sc " +
                        "INNER JOIN t_course c ON sc.courseId = c.courseId " +
                        "INNER JOIN t_sinfo s ON sc.Sno = s.Sno";
                ResultSet rs = dataPrintUtil.con.prepareStatement(sql).executeQuery();
                populateSelectionTable(rs);
            } else if (index == 1) {
                // 按课程查询
                int courseId = Integer.parseInt(courseIdField.getText());
                String sql = "SELECT sc.*, c.courseName, s.Sname, s.department " +
                        "FROM t_selection sc " +
                        "INNER JOIN t_course c ON sc.courseId = c.courseId " +
                        "INNER JOIN t_sinfo s ON sc.Sno = s.Sno " +
                        "WHERE sc.courseId = ?";
                java.sql.PreparedStatement pstmt = dataPrintUtil.con.prepareStatement(sql);
                pstmt.setInt(1, courseId);
                ResultSet rs = pstmt.executeQuery();
                populateSelectionTable(rs);
            } else if (index == 2) {
                // 按学生查询
                int sno = Integer.parseInt(studentIdField.getText());
                String sql = "SELECT sc.*, c.courseName, c.courseTeacher, s.Sname, s.department " +
                        "FROM t_selection sc " +
                        "INNER JOIN t_course c ON sc.courseId = c.courseId " +
                        "INNER JOIN t_sinfo s ON sc.Sno = s.Sno " +
                        "WHERE sc.Sno = ?";
                java.sql.PreparedStatement pstmt = dataPrintUtil.con.prepareStatement(sql);
                pstmt.setInt(1, sno);
                ResultSet rs = pstmt.executeQuery();
                populateSelectionTable(rs);
            }
        } catch (SQLException ex) {
            showErrorDialog("Failed to query selection information: " + ex.getMessage());
            ex.printStackTrace();
        } catch (NumberFormatException ex) {
            showErrorDialog("Please enter a valid number");
        }
    }

    /**
     * 填充选课表格数据
     */
    private void populateSelectionTable(ResultSet rs) throws SQLException {
        while (rs.next()) {
            Object[] row = {
                    rs.getInt("selectionId"),
                    rs.getInt("courseId"),
                    rs.getString("courseName"),
                    rs.getInt("Sno"),
                    rs.getString("Sname"),
                    rs.getString("department")
            };
            selectionTableModel.addRow(row);
        }
    }

    /**
     * 添加成绩信息查询标签页
     */
    private void addGradeTab() {
        JPanel panel = createStyledPanel();

        // 创建查询条件面板
        JPanel queryPanel = createQueryPanel();
        gradeQueryType = createQueryComboBox(new String[]{"Query All Grades", "Query by Course", "Query by Student"});
        gradeCourseIdField = createTextField(10);
        gradeStudentIdField = createTextField(10);
        gradeCourseIdField.setEnabled(false);
        gradeStudentIdField.setEnabled(false);

        JButton queryButton = createPrimaryButton("Query");
        queryButton.addActionListener(e -> queryGradeInfo());

        gradeQueryType.addActionListener(e -> {
            int index = gradeQueryType.getSelectedIndex();
            gradeCourseIdField.setEnabled(index == 1);
            gradeStudentIdField.setEnabled(index == 2);
            gradeCourseIdField.setText("");
            gradeStudentIdField.setText("");
        });

        queryPanel.add(gradeQueryType);
        queryPanel.add(createLabel("Course ID:"));
        queryPanel.add(gradeCourseIdField);
        queryPanel.add(createLabel("Student ID:"));
        queryPanel.add(gradeStudentIdField);
        queryPanel.add(queryButton);

        // 创建表格模型和表格
        String[] gradeColumns = {"Grade ID", "Course ID", "Course Name", "Credits", "Student ID", "Student Name", "Department", "Score"};
        gradeTableModel = new DefaultTableModel(gradeColumns, 0);
        gradeTable = createStyledTable(gradeTableModel);
        JScrollPane scrollPane = createScrollPane(gradeTable);

        // 添加组件到面板
        panel.add(queryPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        // 添加标签页
        tabbedPane.addTab("Grade Info", null, panel, "Query and display grade information");
    }

    /**
     * 查询成绩信息
     */
    private void queryGradeInfo() {
        clearTable(gradeTableModel);

        try {
            int index = gradeQueryType.getSelectedIndex();
            if (index == 0) {
                // 查询所有成绩
                String sql = "SELECT g.*, c.courseName, s.Sname, s.department " +
                        "FROM t_grade g " +
                        "INNER JOIN t_course c ON g.courseId = c.courseId " +
                        "INNER JOIN t_sinfo s ON g.Sno = s.Sno";
                ResultSet rs = dataPrintUtil.con.prepareStatement(sql).executeQuery();
                populateGradeTable(rs);
            } else if (index == 1) {
                // 按课程查询
                int courseId = Integer.parseInt(gradeCourseIdField.getText());
                String sql = "SELECT g.*, c.courseName, s.Sname, s.department " +
                        "FROM t_grade g " +
                        "INNER JOIN t_course c ON g.courseId = c.courseId " +
                        "INNER JOIN t_sinfo s ON g.Sno = s.Sno " +
                        "WHERE g.courseId = ?";
                java.sql.PreparedStatement pstmt = dataPrintUtil.con.prepareStatement(sql);
                pstmt.setInt(1, courseId);
                ResultSet rs = pstmt.executeQuery();
                populateGradeTable(rs);
            } else if (index == 2) {
                // 按学生查询
                int sno = Integer.parseInt(gradeStudentIdField.getText());
                String sql = "SELECT g.*, c.courseName, s.Sname, s.department " +
                        "FROM t_grade g " +
                        "INNER JOIN t_course c ON g.courseId = c.courseId " +
                        "INNER JOIN t_sinfo s ON g.Sno = s.Sno " +
                        "WHERE g.Sno = ?";
                java.sql.PreparedStatement pstmt = dataPrintUtil.con.prepareStatement(sql);
                pstmt.setInt(1, sno);
                ResultSet rs = pstmt.executeQuery();
                populateGradeTable(rs);
            }
        } catch (SQLException ex) {
            showErrorDialog("Failed to query grade information: " + ex.getMessage());
            ex.printStackTrace();
        } catch (NumberFormatException ex) {
            showErrorDialog("Please enter a valid number");
        }
    }

    /**
     * 填充成绩表格数据
     */
    private void populateGradeTable(ResultSet rs) throws SQLException {
        while (rs.next()) {
            Object[] row = {
                    rs.getInt("gradeId"),
                    rs.getInt("courseId"),
                    rs.getString("courseName"),
                    rs.getInt("credit"),
                    rs.getInt("Sno"),
                    rs.getString("Sname"),
                    rs.getString("department"),
                    rs.getInt("score")
            };
            gradeTableModel.addRow(row);
        }
    }

    /**
     * 清空表格数据
     */
    private void clearTable(DefaultTableModel model) {
        model.setRowCount(0);
    }

    /**
     * 显示错误对话框
     */
    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(this,
                message,
                "Error",
                JOptionPane.ERROR_MESSAGE,
                createErrorIcon());
    }

    /**
     * 创建错误图标
     */
    private Icon createErrorIcon() {
        ImageIcon originalIcon = (ImageIcon) UIManager.getIcon("OptionPane.errorIcon");
        if (originalIcon != null) {
            Image image = originalIcon.getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            return new ImageIcon(image);
        }
        return null;
    }

    /**
     * 创建样式化面板
     */
    private JPanel createStyledPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PANEL_BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return panel;
    }

    /**
     * 创建查询面板
     */
    private JPanel createQueryPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(PANEL_BACKGROUND);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(SECONDARY_COLOR),
                        "Query Conditions",
                        javax.swing.border.TitledBorder.LEFT,
                        javax.swing.border.TitledBorder.TOP,
                        TITLE_FONT,
                        PRIMARY_COLOR),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        return panel;
    }

    /**
     * 创建查询下拉框
     */
    private JComboBox<String> createQueryComboBox(String[] items) {
        JComboBox<String> comboBox = new JComboBox<>(items);
        comboBox.setPreferredSize(new Dimension(180, 30));
        comboBox.setBackground(PANEL_BACKGROUND);
        comboBox.setForeground(TEXT_COLOR);
        comboBox.setBorder(BorderFactory.createLineBorder(new Color(173, 216, 230)));
        return comboBox;
    }

    /**
     * 创建文本字段
     */
    private JTextField createTextField(int columns) {
        JTextField textField = new JTextField(columns);
        textField.setPreferredSize(new Dimension(120, 30));
        textField.setBackground(PANEL_BACKGROUND);
        textField.setForeground(TEXT_COLOR);
        textField.setBorder(BorderFactory.createLineBorder(new Color(173, 216, 230)));
        return textField;
    }

    /**
     * 创建标签
     */
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setPreferredSize(new Dimension(80, 30));
        label.setForeground(TEXT_COLOR);
        return label;
    }

    /**
     * 创建主按钮
     */
    private JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(100, 35));
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(Color.WHITE);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PRIMARY_COLOR, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        button.addActionListener(e -> {
            JButton btn = (JButton) e.getSource();
            btn.setBackground(BUTTON_HOVER);
            Timer timer = new Timer(200, ae -> btn.setBackground(PRIMARY_COLOR));
            timer.setRepeats(false);
            timer.start();
        });
        return button;
    }

    /**
     * 创建样式化表格
     */
    private JTable createStyledTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setBackground(PANEL_BACKGROUND);
        table.setForeground(TEXT_COLOR);
        table.setGridColor(darkMode ? new Color(100, 100, 100) : new Color(220, 220, 220));
        table.setRowHeight(25);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);

        // 美化表头
        JTableHeader header = table.getTableHeader();
        header.setBackground(TABLE_HEAD_COLOR);
        header.setForeground(darkMode ? DARK_TEXT_COLOR : PRIMARY_COLOR);
        header.setReorderingAllowed(false);

        // 设置交替行颜色
        table.setDefaultRenderer(Object.class, new StyledTableCellRenderer());
        return table;
    }

    /**
     * 创建滚动面板
     */
    private JScrollPane createScrollPane(JTable table) {
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBackground(BACKGROUND_COLOR);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setBackground(Color.WHITE);
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(10, 0));
        return scrollPane;
    }

    /**
     * 主方法
     */
    public static void main(String[] args) {
        // 使用SwingUtilities确保UI在事件调度线程上创建
        SwingUtilities.invokeLater(() -> {
            // 设置系统外观
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new DataPrintUI();
        });
    }

    /**
     * 样式化表格单元格渲染器
     */
    private class StyledTableCellRenderer extends javax.swing.table.DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            Component component = super.getTableCellRendererComponent(table, value,
                    isSelected, hasFocus, row, column);
            // 交替行颜色
            if (!isSelected) {
                if (darkMode) {
                    component.setBackground(row % 2 == 0 ? DARK_PANEL_BACKGROUND : new Color(70, 70, 70));
                } else {
                    component.setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 248, 250));
                }
            } else {
                component.setBackground(new Color(217, 237, 251));
                component.setForeground(PRIMARY_COLOR);
            }
            component.setForeground(TEXT_COLOR);
            setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
            return component;
        }
    }
}