import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddProblem extends JFrame{
    //main panel
    private JPanel addProblemPanel;
    //Text fields
    private JTextField problemNameTF;
    private JTextField problemDurationTF;
    private JTextField problemPriceTF;
    //buttons
    private JButton addButton;
    private JButton backButton;
    //table
    private MyTable Problems;
    public AddProblem(){
        init();
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                dispose();
                MainForm form = new MainForm();
            }
        });
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                CheckTextFields();
                ClearTextFields();
            }
        });
    }

    //initial form data
    private void init() {
        //Adding the panel to the form
        this.add(addProblemPanel);
        //Setting the properties for the form
        this.setTitle("Car service-update");
        this.setSize(530, 350);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        SetTable();

    }

    //clear the fields after adding a problem
    private void ClearTextFields() {
        problemNameTF.setText("");
        problemPriceTF.setText("");
        problemDurationTF.setText("");
    }


    //Checking if all fields are filled
    private void CheckTextFields() {
        if (problemNameTF.getText().equals("")||problemPriceTF.getText().equals("")||problemDurationTF.getText().equals("")){
            MessageBox.Message("All fields need to be filled");
            return;
        }
        InsertQuery();
    }


    //Before doing the insert query check if PriceTF and ProblemTF can be parsed
    private void InsertQuery() {
            try{
                Integer.parseInt(problemDurationTF.getText());
                Double.parseDouble(problemPriceTF.getText());
                DoInsertQuery(Problems);
            }catch (Exception e){
                MessageBox.Message("Wrong input in Duration/Price");
            }
    }

    //FOR TONI //
    //SQL REQUIRED//
    private void DoInsertQuery(MyTable table) {
        String sql = table.GetInsetQuery(problemNameTF.getText(),problemPriceTF.getText(),problemDurationTF.getText());
        System.out.println(sql);
        DBConnection.insertData(sql);

        //in the end display success message
        MessageBox.Message("Insert is successful!");
    }

    //Setting the table data
    private void SetTable(){
        Problems = new MyTable(Columns.GetProblemTableName(),Columns.GetProblemsColumns());
        problemNameTF.setName(Problems.getColumnName(1));
        problemPriceTF.setName(Problems.getColumnName(2));
        problemDurationTF.setName(Problems.getColumnName(3));
    }
}
