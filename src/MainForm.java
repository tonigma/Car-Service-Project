import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainForm extends JFrame{
    private JPanel mainPanel;
    private JButton addProblemButton;
    private JButton addNewClientButton;
    private JButton searchButton;
    private JButton updateButton;

    public MainForm(){
        add(mainPanel);
        this.setTitle("Car service");
        this.setSize(550,320);
        this.setResizable(false);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        addNewClientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                AddClientForm addClient = new AddClientForm();
                dispose();
            }
        });
        addProblemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                AddProblem problem = new AddProblem();
                dispose();
            }
        });
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                UpdateForm addRepair = new UpdateForm();
                dispose();
            }
        });
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                SearchForm search = new SearchForm();
                dispose();
            }
        });
    }
}
