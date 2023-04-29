package src;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BookshopInterface extends JFrame implements ActionListener {

    private JTextField bookNameField, editionField, priceField, searchField,qtyField;
    private JButton saveButton, clearButton, exitButton, searchButton, deleteButton;
    private JTable bookTable;
    private DefaultTableModel model;

    public BookshopInterface() {
        // The main window
        setTitle("BookCenter");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create the registration panel
        JPanel registrationPanel = new JPanel(new GridBagLayout());
        registrationPanel.setBorder(BorderFactory.createTitledBorder("Registration"));
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(5, 5, 5, 5);

        JLabel bookNameLabel = new JLabel("Book Name:");
        registrationPanel.add(bookNameLabel, c);
        c.gridx++;
        bookNameField = new JTextField(20);
        registrationPanel.add(bookNameField, c);
        c.gridx = 0;
        c.gridy++;

        JLabel editionLabel = new JLabel("Edition:");
        registrationPanel.add(editionLabel, c);
        c.gridx++;
        editionField = new JTextField(20);
        registrationPanel.add(editionField, c);
        c.gridx = 0;
        c.gridy++;

        JLabel qtyLabel = new JLabel("Qty:");
        registrationPanel.add(qtyLabel, c);
        c.gridx++;
        qtyField = new JTextField(20);
        registrationPanel.add(qtyField, c);
        c.gridx = 0;
        c.gridy++;

        JLabel priceLabel = new JLabel("Price:");
        registrationPanel.add(priceLabel, c);
        c.gridx++;
        priceField = new JTextField(20);
        registrationPanel.add(priceField, c);
        c.gridx = 0;
        c.gridy++;

        //c.anchor = GridBagConstraints.LINE_START;
        //c.gridwidth = 1;
        //registrationPanel.add(new JLabel(), c); // Empty Label for spacing
        //c.gridx++;

        saveButton = new JButton("Save");
        saveButton.addActionListener(this);
        registrationPanel.add(saveButton, c);
        c.gridx++;

        clearButton = new JButton("Clear");
        clearButton.addActionListener(this);
        registrationPanel.add(clearButton, c);
        c.gridx++;

        // Create the search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search"));
        JLabel searchLabel = new JLabel("Book ID:");
        searchField = new JTextField(10);
        searchButton = new JButton("Search");
        searchButton.addActionListener(this);
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        // Create the table
        model = new DefaultTableModel();
        model.addColumn("Book ID");
        model.addColumn("Book Name");
        model.addColumn("Edition");
        model.addColumn("Quantity");
        model.addColumn("Price");
        model.addColumn("Total");


        bookTable = new JTable(model);
        
        JScrollPane scrollPane = new JScrollPane(bookTable);
        model.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                int row = e.getFirstRow();
                int col = e.getColumn();
                if (col == 3 || col == 4) {
                    double price = (Double) model.getValueAt(row, 4);
                    int qty = (Integer) model.getValueAt(row, 3);
                    double total = qty*price;
                    model.setValueAt(total, row, 5); 
                }
            }
        });
        
        // Create the delete and exit buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        exitButton = new JButton("Exit");
        exitButton.addActionListener(this);
        deleteButton = new JButton("Delete Line");
        deleteButton.addActionListener(this);
        buttonPanel.add(exitButton);
        buttonPanel.add(deleteButton);

        // Add all the components to the main window
        setLayout(new BorderLayout());
        add(registrationPanel, BorderLayout.NORTH);
        add(searchPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.WEST);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        BookshopInterface bookshopInterface = new BookshopInterface();
        bookshopInterface.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == saveButton) {
            // Save the registration inputs to the table
            String bookName = bookNameField.getText();
            String edition = editionField.getText();
            double price = 0;
            int qty = 0;
            
            // Check for empty fields
            if (bookName.isEmpty() || edition.isEmpty() || qtyField.getText().isEmpty() || priceField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Make sure all your inputs are entered!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            //Verify qty is an int
            try {
                qty = Integer.parseInt(qtyField.getText());
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please verify that your Quantity input is a number!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            //Verify price is a double
            try {
                price = Double.parseDouble(priceField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please verify that your Price input is a number!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            model.addRow(new Object[]{model.getRowCount() + 1, bookName, edition, qty, price, price * qty}); // Add total column
            clearInputs();
            qtyField.setText(""); 
            bookNameField.requestFocus();
        }
        
        else if (e.getSource() == searchButton) {
            // Search for a book by ID
            try {
                int id = Integer.parseInt(searchField.getText());
                boolean idFound = false;
                for (int i = 0; i < model.getRowCount(); i++) {
                    if (Integer.parseInt(model.getValueAt(i, 0).toString()) == id) {
                        bookTable.setRowSelectionInterval(i, i);
                        idFound = true;
                        break;
                    }
                }
                if (!idFound) {
                    JOptionPane.showMessageDialog(this, "ID not found", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "ID should be an integer", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } 
        else if (e.getSource()== clearButton){
            if(bookNameField.getText().isEmpty() && editionField.getText().isEmpty() && priceField.getText().isEmpty() && searchField.getText().isEmpty() && qtyField.getText().isEmpty()){
                
                JOptionPane.showMessageDialog(null, "Please fill in the fields to clear!", "Error", JOptionPane.ERROR_MESSAGE);
    } else {
        // Clear the registration inputs
        clearInputs();
            }
    }
    else if (e.getSource() == exitButton) {
        // Exit the application
        System.exit(0);
    }
    else if (e.getSource() == deleteButton) {
        // Delete the selected row from the table
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow != -1) {
            model.removeRow(selectedRow);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a line to delete", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

private void clearInputs() {
    bookNameField.setText("");
    editionField.setText("");
    qtyField.setText("");
    priceField.setText("");
}
}
