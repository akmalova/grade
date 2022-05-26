package grade;

import java.awt.*;
import java.sql.*;
import static javax.swing.GroupLayout.Alignment.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class FacultyTeachersList extends JInternalFrame {
    
    private String faculty;
    
    private JComboBox comboBox;
    private JTable table;
    
    public FacultyTeachersList() {
        
        initComponents();
    }
    
    private void initComponents() {
        
        setClosable(true);
        setResizable(true);
        
        setTitle("Список сотрудников подразделения");
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        
        Font font = new Font("Tahoma", Font.PLAIN, 14);

        JLabel label1 = new JLabel("Подразделение");
        label1.setFont(font);
  
        comboBox = new JComboBox(Grade.facultiesList);
        comboBox.setFont(font);
                
        JButton button = new JButton("ОК");
        button.setFont(font);
        button.addActionListener((event) -> teachersList());
        
        table = new JTable();
        String[] columnNames = {"Фамилия", "Имя", "Отчество", "Должность"};
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
                        .addComponent(label1)
                        .addComponent(comboBox, GroupLayout.DEFAULT_SIZE, 
                                GroupLayout.DEFAULT_SIZE,
                                GroupLayout.PREFERRED_SIZE)
                        .addComponent(button)
                )
                .addComponent(scrollPane)
        );
        
        gl.setVerticalGroup(gl.createSequentialGroup()
                .addGroup(gl.createParallelGroup(BASELINE)
                        .addComponent(label1)
                        .addComponent(comboBox)
                        .addComponent(button))
                .addComponent(scrollPane)
        );
    }
    
    private void teachersList() {
        
        faculty = comboBox.getSelectedItem().toString();

        DefaultTableModel tableModel = (DefaultTableModel)table.getModel();
        tableModel.setRowCount(0);
        
        try {
            Connection conn = connect();
            String SQL = "SELECT distinct lastname, firstname, secondname, "
                    + "jobpositionname FROM view_teachers WHERE facultyname = ?"
                    + " ORDER BY lastname";
            PreparedStatement ps = conn.prepareStatement(SQL);
            ps.setString(1, faculty);
            
            ResultSet res = ps.executeQuery();

            while (res.next()) {
                String lastname = res.getString("lastname");
                String firstname = res.getString("firstname");
                String secondname = res.getString("secondname");
                String job = res.getString("jobpositionname");

                String tbData[] = {lastname, firstname, secondname, job};
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