package grade;

import java.awt.*;
import java.sql.*;
import javax.swing.*;
import static javax.swing.GroupLayout.Alignment.*;
import static javax.swing.GroupLayout.*;
import javax.swing.table.DefaultTableModel;


public class FindTeacher extends JInternalFrame {
    
    private String lastName;
    private String firstName;
    private String secondName;
    
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTable table;
    
    public FindTeacher() {
        
        initComponents();
    }
    
    private void initComponents() {
        
        setClosable(true);
        setResizable(true);
        
        setTitle("Поиск преподавателя");
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
        
        JButton button = new JButton("ОК");
        button.setFont(font);
        button.addActionListener((event) -> findTeacher());
        
        table = new JTable();
        String[] columnNames = {"Фамилия", "Имя", "Отчество", "Подразделение",
            "Должность"};
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
                        )
                        .addGroup(gl.createParallelGroup()
                                .addComponent(textField1, DEFAULT_SIZE, 
                                        DEFAULT_SIZE, PREFERRED_SIZE)
                                .addComponent(textField2, DEFAULT_SIZE,
                                        DEFAULT_SIZE, PREFERRED_SIZE)
                                .addComponent(textField3, DEFAULT_SIZE,
                                        DEFAULT_SIZE, PREFERRED_SIZE)
                        )
                        .addGroup(gl.createParallelGroup()
                                .addComponent(button)
                        )
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
                .addComponent(scrollPane)
        );
    }
    
    private void findTeacher() {
        
        lastName = textField1.getText();
        firstName = textField2.getText();
        secondName = textField3.getText();
        
        if (lastName.isEmpty() && firstName.isEmpty() && secondName.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Необходимо заполнить хотя бы одно поле!", 
                    "Сервис БРС", JOptionPane.WARNING_MESSAGE);
        return;
        }
        
        String SQL = "SELECT lastname, firstname, secondname, facultyname, "
                    + "jobpositionname FROM view_teachers WHERE ";
          
        if(!lastName.isEmpty() && !firstName.isEmpty() && !secondName.isEmpty()){
            SQL += "lastname = ? AND firstname = ? AND secondname = ? ORDER BY lastname";
        }
        else if(lastName.isEmpty() && !firstName.isEmpty() && !secondName.isEmpty()){
            SQL += "firstname = ? AND secondname = ? ORDER BY lastname";
        }
        else if(!lastName.isEmpty() && firstName.isEmpty() && !secondName.isEmpty()){
            SQL += "lastname = ? AND secondname = ? ORDER BY lastname";
        }
        else if (!lastName.isEmpty() && !firstName.isEmpty() && secondName.isEmpty()){
            SQL += "lastname = ? AND firstname = ? ORDER BY lastname";
        }
        else if(!lastName.isEmpty() && firstName.isEmpty() && secondName.isEmpty()){
            SQL += "lastname = ? ORDER BY lastname";
        }
        else if(lastName.isEmpty() && !firstName.isEmpty() && secondName.isEmpty()){
            SQL += "firstname = ? ORDER BY lastname";
        }
        else if(lastName.isEmpty() && firstName.isEmpty() && !secondName.isEmpty()){
            SQL += "secondname = ? ORDER BY lastname";
        }

        DefaultTableModel tableModel = (DefaultTableModel)table.getModel();
        tableModel.setRowCount(0);
        
        try {
            Connection conn = connect();
            PreparedStatement ps = conn.prepareStatement(SQL);
            
            if(!lastName.isEmpty() && !firstName.isEmpty() && !secondName.isEmpty()){
                ps.setString(1, lastName);
                ps.setString(2, firstName);
                ps.setString(3, secondName);
            }
            else if(lastName.isEmpty() && !firstName.isEmpty() && !secondName.isEmpty()){
                ps.setString(1, firstName);
                ps.setString(2, secondName);
            }
            else if(!lastName.isEmpty() && firstName.isEmpty() && !secondName.isEmpty()){
                ps.setString(1, lastName);
                ps.setString(2, secondName);
            }
            else if(!lastName.isEmpty() && !firstName.isEmpty() && secondName.isEmpty()){
                ps.setString(1, lastName);
                ps.setString(2, firstName);
            }
            else if(!lastName.isEmpty() && firstName.isEmpty() && secondName.isEmpty()){
                ps.setString(1, lastName);
            }
            else if(lastName.isEmpty() && !firstName.isEmpty() && secondName.isEmpty()){
                ps.setString(1, firstName);
            }
            else if(lastName.isEmpty() && firstName.isEmpty() && !secondName.isEmpty()){
                ps.setString(1, secondName);
            }
            
            ResultSet res = ps.executeQuery();

            while (res.next()) {
                String lastname = res.getString("lastname");
                String firstname = res.getString("firstname");
                String secondname = res.getString("secondname");
                String faculty = res.getString("facultyname");
                String job = res.getString("jobpositionname");

                String tbData[] = {lastname, firstname, secondname, faculty, job};
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
