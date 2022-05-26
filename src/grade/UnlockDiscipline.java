package grade;

import java.sql.*;
import javax.swing.*;

public class UnlockDiscipline {
    
    private final String disciplineId;
    
    public UnlockDiscipline() {
        
        disciplineId = JOptionPane.showInputDialog(null, "Введите идентификатор дисциплины",
                "Очистить и разблокировать дисциплину", JOptionPane.INFORMATION_MESSAGE);
        if (disciplineId != null) {
            if (disciplineId.equals(""))
                 JOptionPane.showMessageDialog(null, "Необходимо заполнить поле!", 
                    "Сервис БРС", JOptionPane.WARNING_MESSAGE);
            else
                unlockDisciplinePerformed();
        }
    }
    
    private void unlockDisciplinePerformed() {
        // discipline_clear - реализованная мной функция в базе данных,
        // принимающая на вход число
        String SQL = "SELECT * FROM discipline_clear(?)";
        
        try (var conn = connect()) {
            PreparedStatement ps = conn.prepareStatement(SQL);
            ps.setInt(1, Integer.valueOf(disciplineId));
            ResultSet res = ps.executeQuery();    
            res.next();
            int result = res.getInt(1);
            if (result == -1)
                JOptionPane.showMessageDialog(null, "Дисциплина уже очищена и разблокирована", 
                        "Сервис БРС", JOptionPane.WARNING_MESSAGE); 
            else 
                JOptionPane.showMessageDialog(null, "Дисциплина успешно очищена и разблокирована."
                        + "Количество удаленных записей: " + result + 1, 
                    "Сервис БРС", JOptionPane.INFORMATION_MESSAGE);
        } 
        catch (SQLException e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Не удалось очистить и разблокировать дисциплину", 
                    "Сервис БРС", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    public Connection connect() throws SQLException {
         
        return DriverManager.getConnection(Login.properties.getProperty("url"),
                Login.properties.getProperty("user"), 
                Login.properties.getProperty("password"));
    }
}
