package grade;

import java.awt.*;
import java.sql.*;
import static javax.swing.GroupLayout.Alignment.*;
import javax.swing.*;
import static javax.swing.GroupLayout.*;
import javax.swing.table.DefaultTableModel;

public class StudentsListByGroup extends JInternalFrame {
    
    private String faculty;
    private Integer course;
    private Integer group;
    private int semester;
    private int degree;
    private final String[] degreeList = {"bachelor", "master", "specialist", 
        "postgraduate"};
    
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private JComboBox comboBox3;
    private JRadioButton rButton1;
    private JRadioButton rButton2;
    private JTextField textField1;
    private JTextField textField2;
    private JTable table;
    
    public StudentsListByGroup() {
        
        initComponents();
    }
    
    private void initComponents() {
        
        setClosable(true);
        setResizable(true);
        
        setTitle("Список студентов по курсу и группе");
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        
        Font font = new Font("Tahoma", Font.PLAIN, 14);

        JLabel label1 = new JLabel("Подразделение");
        label1.setFont(font);
        
        comboBox1 = new JComboBox(Grade.facultiesList);
        comboBox1.setFont(font);
        
        JLabel label2 = new JLabel("Курс");
        label2.setFont(font);
        
        textField1 = new JTextField(20);
        textField1.setToolTipText("Введите курс...");
        textField1.setFont(font);
        
        JLabel label3 = new JLabel("Группа");
        label3.setFont(font);
        
        textField2 = new JTextField(20);
        textField2.setToolTipText("Введите группу...");
        textField2.setFont(font);
        
        JLabel label4 = new JLabel("Степень");
        label4.setFont(font);
        
        String[] degrees = {"Бакалавриат", "Специалитет", "Магистратура", 
            "Аспирантура"};
        comboBox2 = new JComboBox(degrees);
        comboBox2.setFont(font);
        
        JLabel label5 = new JLabel("Семестр");
        label5.setFont(font);
        
        comboBox3 = new JComboBox(Grade.yearsList);
        comboBox3.setFont(font);
        
        rButton1 = new JRadioButton("Осень", true);
        rButton1.setFont(font);
        rButton2 = new JRadioButton("Весна");
        rButton2.setFont(font);
        
        ButtonGroup bGroup = new ButtonGroup();
        bGroup.add(rButton1);
        bGroup.add(rButton2);
                
        JButton button = new JButton("ОК");
        button.setFont(font);
        button.addActionListener((event) -> studentsList());
        
        table = new JTable();
        String[] columnNames = {"Фамилия", "Имя", "Отчество"};
        Object[][] data = {};
        table.setModel(new DefaultTableModel(data, 
                columnNames));
        table.getTableHeader().setFont(font);
        table.setFont(font);
        
        table.setRowHeight(table.getRowHeight() + 5);
        
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(table);
        
        var pane = getContentPane();
        var gl = new GroupLayout(pane);
        pane.setLayout(gl);
        gl.setAutoCreateGaps(true);
        gl.setAutoCreateContainerGaps(true);

        gl.setHorizontalGroup(gl.createParallelGroup()
                .addGroup(gl.createSequentialGroup()
                        .addGroup(gl.createParallelGroup(TRAILING)
                                .addComponent(label1)
                                .addComponent(label4)
                                .addComponent(label5)
                        )
                        .addGroup(gl.createParallelGroup(LEADING)
                                .addComponent(comboBox1, DEFAULT_SIZE, 
                                        DEFAULT_SIZE, PREFERRED_SIZE)
                                .addGroup(gl.createSequentialGroup()
                                        .addComponent(comboBox2, DEFAULT_SIZE, 
                                        DEFAULT_SIZE, PREFERRED_SIZE)
                                        .addGap(38)
                                        .addComponent(label2)
                                        .addComponent(textField1, DEFAULT_SIZE, 
                                                DEFAULT_SIZE, PREFERRED_SIZE)
                                        .addGap(38)
                                        .addComponent(label3, DEFAULT_SIZE, 
                                                DEFAULT_SIZE, PREFERRED_SIZE)
                                        .addComponent(textField2, DEFAULT_SIZE,
                                                DEFAULT_SIZE, PREFERRED_SIZE)
                                )
                                .addGroup(gl.createSequentialGroup()
                                        .addComponent(comboBox3, DEFAULT_SIZE,
                                                DEFAULT_SIZE, PREFERRED_SIZE)
                                        .addComponent(rButton1)
                                        .addComponent(rButton2)
                                )
                        )
                        .addGap(10)
                        .addComponent(button)
            )
            .addComponent(scrollPane)
        );
        
        gl.setVerticalGroup(gl.createSequentialGroup()
                .addGroup(gl.createParallelGroup(BASELINE)
                        .addComponent(label1)
                        .addComponent(comboBox1)
                )
                .addGroup(gl.createParallelGroup(BASELINE)
                        .addComponent(label4)
                        .addComponent(comboBox2)
                        .addComponent(label2)
                        .addComponent(textField1)
                        .addComponent(label3)
                        .addComponent(textField2)
                        .addComponent(button)
                )
                .addGroup(gl.createParallelGroup(BASELINE)
                        .addComponent(label5)
                        .addComponent(comboBox3)
                        .addComponent(rButton1)
                        .addComponent(rButton2)
                )
                .addComponent(scrollPane)
        );
    }
    
    private void studentsList() {
        
        faculty = comboBox1.getSelectedItem().toString();
        course = Integer.parseInt(textField1.getText());
        group = Integer.parseInt(textField2.getText());
        degree = comboBox2.getSelectedIndex();
        System.out.println(degree);
        
        int num = rButton1.isSelected() ? 0 : 1;
        semester = 2 * comboBox3.getSelectedIndex() + num + 1;
        
        int facultyId, courseId, groupId = 0;
        
        try {
            Connection conn = connect();
            String SQL = "SELECT id FROM faculties WHERE name = ?";
            PreparedStatement ps = conn.prepareStatement(SQL); 
            ps.setString(1, faculty);
            
            ResultSet res = ps.executeQuery();
            res.next();
            facultyId = res.getInt("id");
        }   
        catch (SQLException e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, "Не удалось выполнить поиск", 
                    "Сервис БРС", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            Connection conn = connect();
            // grade_getid - реализованная мной функция в базе данных, 
            // принимающая на вход число и строку
            String SQL = "SELECT * FROM grade_getid(?, ?)";
            PreparedStatement ps = conn.prepareStatement(SQL); 
            ps.setInt(1, course);
            ps.setString(2, degreeList[degree]);
            
            ResultSet res = ps.executeQuery();
            res.next();
            courseId = res.getInt("id");
        }   
        catch (SQLException e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, "Не удалось выполнить поиск", 
                    "Сервис БРС", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            Connection conn = connect();
            String SQL = "SELECT * FROM getgroups(?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(SQL); 
            ps.setInt(1, courseId);
            ps.setInt(2, facultyId);
            ps.setInt(3, semester);
            
            ResultSet res = ps.executeQuery();
            while (res.next()){
                if (group == res.getInt("groupnum"))
                groupId = res.getInt("id");
            }
        }   
        catch (SQLException e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, "Не удалось выполнить поиск", 
                    "Сервис БРС", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        DefaultTableModel tableModel = (DefaultTableModel)table.getModel();
        tableModel.setRowCount(0);
        
        try {
            Connection conn = connect();
            String SQL = "SELECT * FROM getstudents(?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(SQL);
            ps.setInt(1, facultyId);
            ps.setInt(2, courseId);
            ps.setInt(3, groupId);
            ps.setInt(4, semester);
            ps.setString(5, "");
            ps.setString(6, "");
            ps.setString(7, "");
            
            ResultSet res = ps.executeQuery();

            while (res.next()) {
                String lastname = res.getString("lastname");
                String firstname = res.getString("firstname");
                String secondname = res.getString("secondname");

                String tbData[] = {lastname, firstname, secondname};
                tableModel.addRow(tbData);
            }  
        } 
        
        catch (SQLException e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, 
                    "Не удалось выполнить поиск", 
                    "Сервис БРС", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    public Connection connect() throws SQLException {
         
        return DriverManager.getConnection(Login.properties.getProperty("url"),
                Login.properties.getProperty("user"), 
                Login.properties.getProperty("password"));
    }
}