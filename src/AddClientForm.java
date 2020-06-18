

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddClientForm extends JFrame {
    //main panel
    private JPanel AddClientPanel;
    //Text fields
    private JTextField LnameTextField;
    private JTextField CarNameTextField;
    private JTextField FnameTextField;
    private JTextField CarModelTextField;
    //drop down
    private JComboBox ProblemsDropDown;
    //buttons
    private JButton addButton;
    private JButton backButton;
    //tables
    private MyTable Clients;
    private MyTable Cars;
    private MyTable Problems;

    public AddClientForm(){
        Init();
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MainForm mainF = new MainForm();
                dispose();
            }
        });
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                CheckTextFields();
            }
        });
    }
    //initial form data
    private void Init() {
        //Adding the panel to the form
        this.add(AddClientPanel);
        //Setting the properties for the form
        this.setTitle("Car service-add client");
        this.setSize(530,350);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        SetTableData();
        //adding options to the drop down
        AddItemsToComboBox(Problems,ProblemsDropDown);
    }

    private void SetTableData() {
        Clients=new MyTable(Columns.GetClientsTableName(),Columns.GetClientsColumns());
        FnameTextField.setName(Clients.getColumnName(1));
        LnameTextField.setName(Clients.getColumnName(2));
        Cars = new MyTable(Columns.GetCarsTableName(),Columns.GetCarColumns());
        CarNameTextField.setName(Cars.getColumnName(1));
        CarModelTextField.setName(Cars.getColumnName(2));
        Problems = new MyTable(Columns.GetProblemTableName(),Columns.GetProblemsColumns());
    }

    //Check is all textFields are filled
    private void CheckTextFields() {
        if (FnameTextField.getText().equals("")||LnameTextField.getText().equals("")|| CarNameTextField.getText().equals("")||CarModelTextField.getText().equals("")){
            MessageBox.Message("All fields are required to be filled");
        }
        else {
            if (ProblemsDropDown.getSelectedIndex()==0){
                MessageBox.Message("Selected problem not valid");
            }else{
                CreateNewClient();
                MessageBox.Message("Client Added");
                //Clear the fields after adding the client
                ClearFields();
            }
        }
    }

    private void ClearFields(){
        FnameTextField.setText("");
        LnameTextField.setText("");
        CarNameTextField.setText("");
        CarModelTextField.setText("");
        ProblemsDropDown.setSelectedIndex(0);
    }
    private void CreateNewClient(){
        //For Toni - here are all the values for the creation of the new client
        String firstName = FnameTextField.getText();
        String lastName = LnameTextField.getText();
        String carName = CarNameTextField.getText();
        String carModel = CarModelTextField.getText();
        //-For Toni get the problem index ,find it in the DB and connect it to the new client
        //-For Toni,when you want to use the comboBoxItem you have to cast it --- ((ComboBoxItem)comboBoxItem).getValue() or .getKey()
        Object selectedComboBoxItem= ProblemsDropDown.getSelectedItem();
        int problemId = ((ComboBoxItem)selectedComboBoxItem).getKey();
        System.out.println(firstName+lastName+carName+carModel+((ComboBoxItem)selectedComboBoxItem).getKey());//checking if the values are right
        //First we have to crete the new car and then the client so we can take the index of the new car
        CreateCar(Cars,carName,carModel,problemId);
        CreateClient(Clients,Cars,firstName,lastName,carName);
    }

    //FOR TONI//
    //SQL REQUIRED//
    private void CreateClient(MyTable table,MyTable searchFromTable, String firstName, String lastName, String carName) {
        //first make the query to take the id of the car
        String carIdSql =searchFromTable.GetIdByFirstColumn(CarNameTextField.getName(),carName);
        System.out.println(carIdSql);
        String carid = DBConnection.selectId(carIdSql);
        //I've this statement order by DESC ,so if there are more then one car with the same name take the
        // newest car(which is the car we created in the previous method)
        String createClientSql = table.GetInsetQuery(firstName,lastName,carid);
        System.out.println(createClientSql);
        DBConnection.insertData(createClientSql);
    }

    //For toni//
    //SQL REQUIRED//
    private void CreateCar(MyTable table, String carName, String carModel, int problemId) {
        String sql = table.GetInsetQuery(carName,carModel,problemId);
        System.out.println(sql);
        DBConnection.insertData(sql);
    }


    //For toni//
    //SQL REQUIRED//
    private void AddItemsToComboBox(MyTable table,JComboBox box){
        //-For Toni get all the values and index from the
        // problems table in the DB and add them to the combobox using a loop
        //while the requst has values
        //this is for test
        box.addItem("Select");//dont delete this
        String sql = table.SelectTwoColumns(0,1);//takes the id and the name of the problem
        //after you make the query inset the 2 columns here ComboBoxItem(id,value)
        System.out.println(sql);
//        int test = 5;
//        int id = 1;
//        String value = "pepe";
//        while (test>0){
//            box.addItem(new ComboBoxItem(id,value));
//            id++;
//            value+="e";
//            test--;
//        }
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        model.addElement("Select");
        box.setModel(DBConnection.fillDropDown(model,sql));

    }
}
