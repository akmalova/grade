package grade;

import java.awt.*;
import java.sql.*;
import static javax.swing.GroupLayout.Alignment.*;
import javax.swing.*;
import static javax.swing.GroupLayout.*;
import javax.swing.table.DefaultTableModel;

public class TeacherDisciplines extends JInternalFrame {
    
    private String lastName;
    private String firstName;
    private String secondName;
    private Integer semester;
    
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JComboBox comboBox;
    private JRadioButton rButton1;
    private JRadioButton rButton2;
    private JTable table;
    
    public TeacherDisciplines() {
        
        initComponents();
    }
    
    private void initComponents() {
        
        setClosable(true);
        setResizable(true);
        
        setTitle("Поиск дисциплин, закрепленных за преподавателем");
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        
        Font font = new Font("Tahoma", Font.PLAIN, 14);

        JLabel label1 = new JLabel("Фамилия");
        label1.setFont(font);
        
        textField1 = new JTextField(30);
        textField1.setToolTipText("Введите фамилию...");
        textField1.setFont(font);
        
        JLabel label2 = new JLabel("Имя");
        label2.setFont(font);
        
        textField2 = new JTextField(30);
        textField2.setToolTipText("Введите имя...");
        textField2.setFont(font);
        
        JLabel label3 = new JLabel("Отчество");
        label3.setFont(font);
        
        textField3 = new JTextField(30);
        textField3.setToolTipText("Введите отчество...");
        textField3.setFont(font);
        
        JLabel label4 = new JLabel("Семестр");
        label4.setFont(font);
        
        comboBox = new JComboBox(Grade.yearsList);
        comboBox.setFont(font);
        
        rButton1 = new JRadioButton("Осень", true);
        rButton1.setFont(font);
        rButton2 = new JRadioButton("Весна");
        rButton2.setFont(font);
        
        ButtonGroup group = new ButtonGroup();
        group.add(rButton1);
        group.add(rButton2);
        
        JButton button = new JButton("ОК");
        button.setFont(font);
        button.addActionListener((event) -> findTeacher());
        
        table = new JTable();
        String[] columnNames = {"Название дисциплины"};
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
                                .addComponent(label2)
                                .addComponent(label3)
                                .addComponent(label4)
                        )
                        .addGroup(gl.createParallelGroup()
                                .addComponent(textField1, DEFAULT_SIZE, 
                                        DEFAULT_SIZE, PREFERRED_SIZE)
                                .addComponent(textField2,  DEFAULT_SIZE, 
                                        DEFAULT_SIZE, PREFERRED_SIZE)
                                .addComponent(textField3, DEFAULT_SIZE, 
                                        DEFAULT_SIZE, PREFERRED_SIZE)
                                .addGroup(gl.createSequentialGroup()
                                        .addComponent(comboBox, DEFAULT_SIZE, 
                                                DEFAULT_SIZE, PREFERRED_SIZE)
                                        .addComponent(rButton1)
                                        .addComponent(rButton2)
                                )
                        )
                        .addComponent(button)
                )
                .addComponent(scrollPane)
        );
        
        gl.setVerticalGroup(gl.createSequentialGroup()
                .addGroup(gl.createParallelGroup(BASELINE)
                        .addComponent(label1)
                        .addComponent(textField1)
                )
                .addGroup(gl.createParallelGroup(BASELINE)
                        .addComponent(label2)
                        .addComponent(textField2)
                        .addComponent(button)
                )
                .addGroup(gl.createParallelGroup(BASELINE)
                        .addComponent(label3)
                        .addComponent(textField3)
                )
                .addGroup(gl.createParallelGroup(BASELINE)
                        .addComponent(label4)
                        .addComponent(comboBox)
                        .addComponent(rButton1)
                        .addComponent(rButton2)
                )
                .addComponent(scrollPane)
        );
    }
    
    private void findTeacher() {
        
        lastName = textField1.getText();
        firstName = textField2.getText();
        secondName = textField3.getText();
        int num = rButton1.isSelected() ? 0 : 1;
        semester = 2 * comboBox.getSelectedIndex() + num + 1;
        
        if (lastName.isEmpty() || firstName.isEmpty() || secondName.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Необходимо заполнить все поля!", 
                    "Сервис БРС", JOptionPane.WARNING_MESSAGE);
        return;
        }
        
        String SQL = "SELECT * FROM getdisciplinesforteacher ((SELECT distinct "
                + "teacherid FROM view_teachers WHERE lastname = ? AND "
                + "firstname = ? AND secondname = ?), ?)";
        
        DefaultTableModel tableModel = (DefaultTableModel)table.getModel();
        tableModel.setRowCount(0);
        
        try {
            Connection conn = connect();
            PreparedStatement ps = conn.prepareStatement(SQL);
            ps.setString(1,lastName);
            ps.setString(2,firstName);
            ps.setString(3,secondName);
            ps.setInt(4,semester);         
            
            ResultSet res = ps.executeQuery();

            while (res.next()) {
                String discipline = res.getString("SubjectName");
                String tbData[] = {discipline};
                tableModel.addRow(tbData);
            }
        } 
        
        catch (SQLException e) {
            System.out.println(e.getMessage());
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
