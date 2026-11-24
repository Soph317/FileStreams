import javax.swing.*;
import java.awt.*;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class RandProductSearch extends JFrame {
    String FILE_NAME = "ProductRandomData.dat";

    JTextField searchField = new JTextField(25);
    JButton searchButton = new JButton("Search");
    JButton quitButton = new JButton("Quit");
    JTextArea resultsArea = new JTextArea(12, 55);

    public RandProductSearch() {
        super("Random Product Search");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 10));
        topPanel.add(new JLabel("Partial Product Name:"));
        topPanel.add(searchField);
        topPanel.add(searchButton);
        topPanel.add(quitButton);

        resultsArea.setEditable(false);
        resultsArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(resultsArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Matching Products"));

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        searchButton.addActionListener(e -> handleSearch());
        searchField.addActionListener(e -> handleSearch());
        quitButton.addActionListener(e -> System.exit(0));

        pack();
        setLocationRelativeTo(null);
    }

    private void handleSearch() {
        String query = searchField.getText().trim().toLowerCase();
        resultsArea.setText("");

        if (query.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a partial product name to search.",
                    "Empty Search",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        File file = new File(FILE_NAME);
        if (!file.exists()) {
            resultsArea.setText("Data file not found: " + FILE_NAME);
            return;
        }

        int matches = 0;
        try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
            while (raf.getFilePointer() + Product.RECORD_BYTES <= raf.length()) {
                Product p = Product.readFromRandomAccess(raf);
                if (p.getName().toLowerCase().contains(query)) {
                    resultsArea.append(p + "\n");
                    matches++;
                }
            }
            if (matches == 0) {
                resultsArea.setText("No products found matching \"" + searchField.getText().trim() + "\".");
            }
        } catch (EOFException ignored) {
        } catch (IOException ex) {
            resultsArea.setText("Error reading file: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RandProductSearch().setVisible(true));
    }
}