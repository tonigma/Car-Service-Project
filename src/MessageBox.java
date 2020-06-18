import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;

//custom Message Dialog to display message
public class MessageBox extends UIManager {

    public MessageBox(String text){
        this.put("OptionPane.background",new ColorUIResource(32,64,81));
        this.put("Panel.background",new ColorUIResource(32,64,81));
        this.put("OptionPane.messageForeground", Color.WHITE);
        JOptionPane.showMessageDialog(null,text);
    }
    public static void Message(String text){
        MessageBoxInit();
        JOptionPane.showMessageDialog(null,text);
    }
    public static int ConfirmationMessage(String text){
        MessageBoxInit();
        int res = JOptionPane.showConfirmDialog(null,text);
        return res;
    }
    private static void MessageBoxInit(){
        put("OptionPane.background",new ColorUIResource(32,64,81));
        put("Panel.background",new ColorUIResource(32,64,81));
        put("OptionPane.messageForeground", Color.WHITE);
    }
}
