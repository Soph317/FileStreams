import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Serializable;

public class Product implements Serializable {
    public static int NAME_LEN = 35;
    public static final int DESC_LEN = 75;
    public static final int ID_LEN = 6;

    public static final int RECORD_BYTES = (NAME_LEN + DESC_LEN + ID_LEN) * 2 + 8;

    private String name;
    private String description;
    private final String ID;
    private double cost;

    public Product(String name, String description, String ID, double cost) {
        this.name = name;
        this.description = description;
        this.ID = ID;
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getID() {
        return ID;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }


    public static String padToLength(String value, int length) {
        String safe = (value == null) ? "" : value.trim();
        if (safe.length() > length) {
            return safe.substring(0, length);
        }
        StringBuilder sb = new StringBuilder(safe);
        while (sb.length() < length) {
            sb.append(' ');
        }
        return sb.toString();
    }

    public String getFormattedName() {
        return padToLength(name, NAME_LEN);
    }

    public String getFormattedDescription() {
        return padToLength(description, DESC_LEN);
    }

    public String getFormattedID() {
        return padToLength(ID, ID_LEN);
    }


    public void writeToRandomAccess(RandomAccessFile raf) throws IOException {
        raf.writeChars(getFormattedName());
        raf.writeChars(getFormattedDescription());
        raf.writeChars(getFormattedID());
        raf.writeDouble(cost);
    }


    public static Product readFromRandomAccess(RandomAccessFile raf) throws IOException {
        String name = readFixedChars(raf, NAME_LEN).trim();
        String desc = readFixedChars(raf, DESC_LEN).trim();
        String id = readFixedChars(raf, ID_LEN).trim();
        double cost = raf.readDouble();
        return new Product(name, desc, id, cost);
    }

    private static String readFixedChars(RandomAccessFile raf, int length) throws IOException {
        char[] chars = new char[length];
        for (int i = 0; i < length; i++) {
            chars[i] = raf.readChar();
        }
        return new String(chars);
    }

    @Override
    public String toString() {
        return String.format("ID: %s | Name: %s | Cost: $%.2f | Desc: %s",
                ID, name, cost, description);
    }
}