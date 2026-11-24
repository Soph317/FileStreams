import javax.swing.*;
import java.awt.*;
import java.io.*;

public class RandProductMaker {
    private static final String FILE_NAME = "ProductRandomData.dat";

    JFrame frame;
    JPanel formPanel, buttonPanel;
    JLabel nameLabel, descLabel, idLabel, costLabel, countLabel;
    JTextField nameField, descField, idField, costField, countField;
    JButton addButton, quitButton;

    int recordCount = 0;

    public RandProductMaker() {
        frame = new JFrame("Random Product Maker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));

        formPanel = new JPanel(new GridLayout(5, 2, 8, 8));
        formPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 6, 12));

        nameLabel = new JLabel("Product Name (max 35):");
        nameField = new JTextField(25);
        formPanel.add(nameLabel);
        formPanel.add(nameField);

        descLabel = new JLabel("Description (max 75):");
        descField = new JTextField(25);
        formPanel.add(descLabel);
        formPanel.add(descField);

        idLabel = new JLabel("Product ID (max 6):");
        idField = new JTextField(15);
        formPanel.add(idLabel);
        formPanel.add(idField);

        costLabel = new JLabel("Cost:");
        costField = new JTextField(15);
        formPanel.add(costLabel);
        formPanel.add(costField);

        countLabel = new JLabel("Record Count:");
        countField = new JTextField("0", 10);
        countField.setEditable(false);
        initializeExistingRecordCount();
        formPanel.add(countLabel);
        formPanel.add(countField);

        buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 8));

        addButton = new JButton("Add");
        quitButton = new JButton("Quit");
        buttonPanel.add(addButton);
        buttonPanel.add(quitButton);

        frame.add(formPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> handleAddRecord());
        quitButton.addActionListener(e -> System.exit(0));

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void initializeExistingRecordCount() {
        File file = new File(FILE_NAME);
        if (file.exists() && file.length() > 0) {
            recordCount = (int) (file.length() / Product.RECORD_BYTES);
            countField.setText(String.valueOf(recordCount));
        }
    }

    private void handleAddRecord() {
        String name = nameField.getText().trim();
        String desc = descField.getText().trim();
        String id = idField.getText().trim();
        String costText = costField.getText().trim();

        if (name.isEmpty() || desc.isEmpty() || id.isEmpty() || costText.isEmpty()) {
            JOptionPane.showMessageDialog(
                    frame,
                    "Please fill in all fields before adding a product.",
                    "Missing Input",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        double cost;
        try {
            cost = Double.parseDouble(costText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    frame,
                    "Cost must be a valid number (e.g., 19.99).",
                    "Invalid Cost",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        Product product = new Product(name, desc, id, cost);

        try (RandomAccessFile raf = new RandomAccessFile(FILE_NAME, "rw")) {
            raf.seek(raf.length());
            product.writeToRandomAccess(raf);

            recordCount++;
            countField.setText(String.valueOf(recordCount));

            nameField.setText("");
            descField.setText("");
            idField.setText("");
            costField.setText("");
            nameField.requestFocusInWindow();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(
                    frame,
                    "Error writing to file: " + ex.getMessage(),
                    "File Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RandProductMaker::new);
    }
}