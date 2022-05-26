package grade;

import java.awt.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;

public class Grade extends JFrame {
    
    protected static String[] facultiesList;
    protected static String[] yearsList;
    private JDesktopPane desktopPane;
    
    public Grade() {
        
        initUI();
        getFacultiesList();
        getSemestersList();
    }
    
    private void initUI() {
        
        desktopPane = new JDesktopPane();
        setContentPane(desktopPane);
        
        createMenuBar();
        
        setTitle("Сервис БРС");
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setExtendedState(MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    
    private void createMenuBar() {
        
        JMenuBar menuBar = new JMenuBar();
        Font font = new Font("Tahoma", Font.PLAIN, 12);
        
        JMenu studentMenu = new JMenu("Работа со студентами");
        studentMenu.setFont(font);
        
        JMenuItem findStudentByName = new JMenuItem("Поиск студента по ФИО");
        findStudentByName.setFont(font);
        findStudentByName.addActionListener((event) -> findStudentByNamePerformed());
        studentMenu.add(findStudentByName);
        
        JMenuItem findStudentByNum = new JMenuItem("Поиск студента по номеру зачетной книжки");
        findStudentByNum.setFont(font);
        findStudentByNum.addActionListener((event) -> findStudentByNumPerformed());
        studentMenu.add(findStudentByNum);
        
        JMenuItem studentIfoByName = new JMenuItem("Информация о студенте по ФИО");
        studentIfoByName.setFont(font);
        studentIfoByName.addActionListener((event) -> studentInfoByNamePerformed());
        studentMenu.add(studentIfoByName);
        
        JMenuItem studentInfoByNum = new JMenuItem("Информация о студенте по номеру зачетной книжки");
        studentInfoByNum.setFont(font);
        studentInfoByNum.addActionListener((event) -> studentInfoByNumPerformed());
        studentMenu.add(studentInfoByNum);
        
        JMenuItem studentsList = new JMenuItem("Список студентов по курсу и группе");
        studentsList.setFont(font);
        studentsList.addActionListener((event) -> studentsListPerformed());
        studentMenu.add(studentsList);
        
        JMenu teacherMenu = new JMenu("Работа с преподавателями");
        teacherMenu.setFont(font);
        
        JMenuItem findTeacher = new JMenuItem("Поиск преподавателя");
        findTeacher.setFont(font);
        findTeacher.addActionListener((event) -> findTeacherPerformed());
        teacherMenu.add(findTeacher);
        
        JMenuItem teachersList = new JMenuItem("Список сотрудников подразделения");
        teachersList.setFont(font);
        teachersList.addActionListener((event) -> facultyTeachersListPerformed());
        teacherMenu.add(teachersList);
        
        JMenuItem teacherDisciplines = new JMenuItem("Список дисциплин, закрпеленных за преподавателем");
        teacherDisciplines.setFont(font);
        teacherDisciplines.addActionListener((event) -> teacherDisciplinesPerformed());
        teacherMenu.add(teacherDisciplines);
        
        JMenu disciplineMenu = new JMenu("Работа с дисциплинами");
        disciplineMenu.setFont(font);
        JMenuItem unlockDiscipline = new JMenuItem("Очистить и разблокировать дисциплину");
        unlockDiscipline.setFont(font);
        unlockDiscipline.addActionListener((event) -> unlockDisciplinePerformed());
        disciplineMenu.add(unlockDiscipline);
        
        menuBar.add(studentMenu);
        menuBar.add(teacherMenu);
        menuBar.add(disciplineMenu);
        
        setJMenuBar(menuBar);
    }
    
    private void findTeacherPerformed() {
        
        FindTeacher frame = new FindTeacher();
        desktopPane.add(frame);
        frame.show();
    }
    
    private void facultyTeachersListPerformed() {
        
        FacultyTeachersList frame = new FacultyTeachersList();
        desktopPane.add(frame);
        frame.show();
    }
    
    private void teacherDisciplinesPerformed() {
        
        TeacherDisciplines frame = new TeacherDisciplines();
        desktopPane.add(frame);
        frame.show();
    }
    
    private void findStudentByNamePerformed() {
        
        FindStudentByName frame = new FindStudentByName();
        desktopPane.add(frame);
        frame.show();
    }
    
    private void findStudentByNumPerformed() {
        
        FindStudentByNum frame = new FindStudentByNum();
        desktopPane.add(frame);
        frame.show();
    }
    
    private void studentInfoByNamePerformed() {
        
        StudentInfoByName frame = new StudentInfoByName();
        desktopPane.add(frame);
        frame.show();
    }
            
    private void studentInfoByNumPerformed() {
        
        StudentInfoByNum frame = new StudentInfoByNum();
        desktopPane.add(frame);
        frame.show();
    }
    
    private void studentsListPerformed() {
        
        StudentsListByGroup frame = new StudentsListByGroup();
        desktopPane.add(frame);
        frame.show();
    }
    
    private void unlockDisciplinePerformed() {
        
        UnlockDiscipline unlockDiscipline = new UnlockDiscipline();
    }
    
    private void getFacultiesList() {
        
        ArrayList<String> list = new ArrayList<>();
        try {
            Connection conn = connect();
            String SQL = "SELECT * FROM getfaculties()";
            PreparedStatement ps = conn.prepareStatement(SQL);
            
            ResultSet res = ps.executeQuery();
            
            while (res.next()) {
                list.add(res.getString("name"));
            }  
        } 
        
        catch (SQLException e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, 
                    "Не удалось получить список факультетов", 
                    "Сервис БРС", JOptionPane.WARNING_MESSAGE);
        }
        
        facultiesList = list.toArray(new String[list.size()]);
    }
    
    private void getSemestersList() {
        
        ArrayList<String> list = new ArrayList<>();
        
        try {
            Connection conn = connect();
            String SQL = "SELECT * FROM semesters";
            PreparedStatement ps = conn.prepareStatement(SQL);
            
            ResultSet res = ps.executeQuery();
            
            String year;
            while (res.next()) {      
                year = "" + res.getInt("year");
                if (!list.contains(year))
                    list.add("" + year);
            }  
        } 
        
        catch (SQLException e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, 
                    "Не удалось получить список семестров", 
                    "Сервис БРС", JOptionPane.WARNING_MESSAGE);
        }
        
        yearsList = list.toArray(new String[list.size()]);
    }
    
    public Connection connect() throws SQLException {
         
        return DriverManager.getConnection(Login.properties.getProperty("url"),
                Login.properties.getProperty("user"), 
                Login.properties.getProperty("password"));
    }
}
