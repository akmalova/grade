package grade;

import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import static javax.swing.GroupLayout.Alignment.*;
import static javax.swing.GroupLayout.*;

public class Login extends JFrame {
    
    protected static Properties properties = new Properties();
    
    private JTextField field1;
    private JTextField field2;
    
    public static void main(String[] args) {
        
        EventQueue.invokeLater(() -> {
            UIManager.put("OptionPane.messageFont", new Font("Tahoma", Font.PLAIN, 12));
            UIManager.put("OptionPane.buttonFont", new Font("Tahoma", Font.PLAIN, 12));
            var grade = new Login();
            grade.setVisible(true);
        });
    }
    
    public Login() {
        
        initUI();
        readUrl();
    }
    
    private void initUI() {
        
        setTitle("Авторизация");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(400, 150);
        setVisible(true);
        setResizable(false);
        setLocationRelativeTo(null);
        Font font = new Font("Tahoma", Font.PLAIN, 14);
        
        JLabel label1 = new JLabel("Имя пользователя");
        label1.setFont(font);
        JLabel label2 = new JLabel("Пароль");
        label2.setFont(font);
        
        field1 = new JTextField(30);
        field1.setFont(font);
        field2 = new JTextField(30);
        field2.setFont(font);
        
        JButton button = new JButton("ОК");
        button.addActionListener((event) -> loginPerformed());
        button.setFont(font);
        
        GroupLayout gl = new GroupLayout(getContentPane());
        setLayout(gl);
        gl.setAutoCreateGaps(true);
        gl.setAutoCreateContainerGaps(true);
        
        gl.setHorizontalGroup(gl.createParallelGroup()
                .addGroup(gl.createSequentialGroup()
                    .addGroup(gl.createParallelGroup(TRAILING)
                        .addComponent(label1)
                        .addComponent(label2)
                    )
                    .addGroup(gl.createParallelGroup()
                        .addComponent(field1, DEFAULT_SIZE, 
                                DEFAULT_SIZE, PREFERRED_SIZE)
                        .addComponent(field2, DEFAULT_SIZE, 
                                DEFAULT_SIZE, PREFERRED_SIZE)
                        .addComponent(button)
                    )
                )
        );
        
        gl.setVerticalGroup(gl.createSequentialGroup()
                .addGap(15)
                .addGroup(gl.createParallelGroup(BASELINE)
                        .addComponent(label1)
                        .addComponent(field1)
                )
                .addGroup(gl.createParallelGroup(BASELINE)
                        .addComponent(label2)
                        .addComponent(field2)
                )   
                .addComponent(button)
        );
    }
    
    private void loginPerformed() {
        
        properties.setProperty("user", field1.getText());
        properties.setProperty("password", field2.getText());
        Grade grade = new Grade();
        setVisible(false);
        grade.setVisible(true);
    }
    
    private void readUrl() {
        
        try {
            FileInputStream fis = new FileInputStream("nbproject/private/config.properties");
            properties.load(fis);
            properties.getProperty("url");
        }
        catch (IOException e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, "Не удалось получить URL-адрес", 
                    "Сервис БРС", JOptionPane.WARNING_MESSAGE);
            String url = JOptionPane.showInputDialog(null, "Введите URL-адрес",
                "Сервис БРС", JOptionPane.INFORMATION_MESSAGE);
            properties.setProperty("url", url);
        }
    }
}
