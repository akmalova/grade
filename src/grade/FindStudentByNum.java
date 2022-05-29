package grade;

import java.awt.*;
import java.sql.*;
import javax.swing.*;
import static javax.swing.GroupLayout.Alignment.*;
import static javax.swing.GroupLayout.*;
import javax.swing.table.*;


public class FindStudentByNum extends JInternalFrame {
    
    private String recordBook;
    
    private JTextField textField;
    private JTable table;
    
    public FindStudentByNum() {
        
        initComponents();
    }
    
    private void initComponents() {
        
        setClosable(true);
        setResizable(true);
        
        setTitle("Поиск студента по номеру зачетной книжки");
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        
        Font font = new Font("Tahoma", Font.PLAIN, 14);

        JLabel label1 = new JLabel("Номер зачетной книжки");
        label1.setFont(font);
        
        textField = new JTextField(30);
        textField.setToolTipText("Введите номер зачетной книжки...");
        textField.setFont(font);
        
        JButton button = new JButton("ОК");
        button.setFont(font);
        button.addActionListener((event) -> findStudent());
        
        table = new JTable();
        String[] columnNames = {"Фамилия", "Имя", "Отчество",
            "Подразделение", "Семестр", "Степень", "Курс", "Группа"};
        Object[][] data = {};
        table.setModel(new DefaultTableModel(data, 
                columnNames));
        table.getTableHeader().setFont(font);
        table.setFont(font);
        
        table.setRowHeight(table.getRowHeight() + 5);
        
        int[] size = {120, 120, 120, 350, 100, 100, 50, 50};
        for (int i = 0; i < size.length; i++){
            TableColumn column = table.getColumnModel().getColumn(i);
            column.setMinWidth(size[i]);
            column.setMaxWidth(size[i] + 100);
        }
        
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(table);
        
        var pane = getContentPane();
        var gl = new GroupLayout(pane);
        pane.setLayout(gl);
        gl.setAutoCreateGaps(true);
        gl.setAutoCreateContainerGaps(true);

        gl.setHorizontalGroup(gl.createParallelGroup()
                .addGroup(gl.createSequentialGroup()
                        .addComponent(label1)
                        .addComponent(textField, DEFAULT_SIZE,
                                DEFAULT_SIZE, PREFERRED_SIZE)
                        .addComponent(button)
                        )
                .addComponent(scrollPane)
        );
        
        gl.setVerticalGroup(gl.createSequentialGroup()
                .addGroup(gl.createParallelGroup(BASELINE)
                        .addComponent(label1)
                        .addComponent(textField)
                        .addComponent(button)
                )
                .addComponent(scrollPane)
        );
    }
    
    private void findStudent() {
        
        recordBook = textField.getText();
        
        if (recordBook.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Необходимо заполнить поле!", 
                    "Сервис БРС", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String SQL = "SELECT * FROM view_students WHERE recordbooknumber = ?";
        
        DefaultTableModel tableModel = (DefaultTableModel)table.getModel();
        tableModel.setRowCount(0);
        
        try {
            Connection conn = connect();
            PreparedStatement ps = conn.prepareStatement(SQL);
            ps.setString(1, recordBook);
            
            ResultSet res = ps.executeQuery();

            while (res.next()) {
                String lastname = res.getString("lastname");
                String firstname = res.getString("firstname");
                String secondname = res.getString("secondname");
                String faculty = res.getString("facultyname");
                int semesterId = res.getInt("semesterid");
                String semester = (semesterId % 2 == 1) ? "Осень" : "Весна";
                int year = 2014 + ((semesterId % 2 == 1) ? (semesterId - 1) : 
                        semesterId - 2) / 2;
                semester += " " + year;
                String degree = res.getString("degree");
                switch(degree) {
                    case "bachelor":
                        degree = "Баклавриат";
                        break;
                    case "master":
                        degree = "Магистратура";
                        break;
                    case "specialist":
                        degree = "Специалитет";
                        break;
                    case "postgraduate":
                        degree = "Аспирантура";
                        break;
                    default:
                            break;
                }
                String course = res.getString("gradenum");
                String group = res.getString("groupnum");

                String tbData[] = {lastname, firstname, secondname, 
                    faculty, semester, degree, course, group};
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
