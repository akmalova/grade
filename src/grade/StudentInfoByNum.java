package grade;

import java.awt.*;
import java.sql.*;
import javax.swing.*;
import static javax.swing.GroupLayout.Alignment.*;
import javax.swing.table.DefaultTableModel;

public class StudentInfoByNum extends JInternalFrame {
    
    private String recordBook;
    private Integer semester;
    
    private JTextField textField;
    private JComboBox comboBox;
    private JRadioButton rButton1;
    private JRadioButton rButton2;
    private JTable table;
    
    public StudentInfoByNum() {
        
        initComponents();
    }
    
    private void initComponents() {
        
        setTitle("Информация о студенте по номеру зачетной книжки");
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setClosable(true);
        setResizable(true);
        Font font = new Font("Tahoma", Font.PLAIN, 14);

        JLabel label1 = new JLabel("Номер зачетной книжки");
        label1.setFont(font);
        
        textField = new JTextField(30);
        textField.setToolTipText("Введите номер зачетной книжки...");
        textField.setFont(font);
        
        JLabel label2 = new JLabel("Семестр");
        label2.setFont(font);
        
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
        button.addActionListener((event) -> findStudentPerformed());
        
        table = new JTable();
        String[] columnNames = {"Дисциплина", "Отчётность", "Баллы"};
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
                        .addComponent(label2))
                    .addGroup(gl.createParallelGroup()
                        .addComponent(textField, GroupLayout.DEFAULT_SIZE, 
                                GroupLayout.DEFAULT_SIZE,
                                GroupLayout.PREFERRED_SIZE)
                        .addGroup(gl.createSequentialGroup()
                        .addComponent(comboBox,GroupLayout.DEFAULT_SIZE, 
                                GroupLayout.DEFAULT_SIZE,
                                GroupLayout.PREFERRED_SIZE) 
                        .addComponent(rButton1)
                        .addComponent(rButton2)
                        .addGap(120)
                        .addComponent(button)
                        )
                    )
                )
                .addComponent(scrollPane)
        );
        
        gl.setVerticalGroup(gl.createSequentialGroup()
                .addGroup(gl.createParallelGroup(BASELINE)
                        .addComponent(label1)
                        .addComponent(textField))
                .addGroup(gl.createParallelGroup(BASELINE)
                        .addComponent(label2)
                        .addComponent(comboBox)
                        .addComponent(rButton1)
                        .addComponent(rButton2)
                        .addComponent(button))
                .addComponent(scrollPane)
        );
    }
    
    private void findStudentPerformed() {
        
       recordBook = textField.getText();
        
        if (recordBook.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Необходимо заполнить поле!", 
                    "Сервис БРС", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int num = rButton1.isSelected() ? 0 : 1;
        semester = 2 * comboBox.getSelectedIndex() + num + 1;
        
        DefaultTableModel tableModel = (DefaultTableModel)table.getModel();
        tableModel.setRowCount(0);
        
        String SQL = "SELECT * FROM student_getdisciplines ((SELECT distinct "
                + "recordbookid FROM view_students WHERE recordbooknumber = ? "
                + "AND semesterid = ?), ?)";
        
        try {
            Connection conn = connect();
            PreparedStatement ps = conn.prepareStatement(SQL);
            ps.setString(1, recordBook);
            ps.setInt(2,semester);
            ps.setInt(3,semester);
            ResultSet res = ps.executeQuery();

            while (res.next()) {
                if (res.getString("Rate") != null) {
                    String discipline = res.getString("SubjectName");
                    String credit = "credit".equals(res.getString("Type"))? 
                            "Зачет" : "Экзамен";
                    String rate = res.getString("Rate");
                    String tbData[] = {discipline, credit, rate};
                    tableModel.addRow(tbData);
                }
            }
        } catch (SQLException e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, "Не удалось выполнить поиск", 
                    "Сервис БРС", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    public Connection connect() throws SQLException {
         
        return DriverManager.getConnection(Login.properties.getProperty("url"),
                Login.properties.getProperty("user"), 
                Login.properties.getProperty("password"));
    }
}
