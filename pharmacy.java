import java.util.*;
import java.io.*;
import java.time.*;

class Medicine implements Serializable {
    int id;
    String name;
    String category;
    double price;
    int quantity;
    LocalDate expiryDate;

    Medicine(int id, String name, String category, double price, int quantity, LocalDate expiryDate) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.expiryDate = expiryDate;
    }

    void display() {
        System.out.println("ID: " + id);
        System.out.println("Name: " + name);
        System.out.println("Category: " + category);
        System.out.println("Price: " + price);
        System.out.println("Quantity: " + quantity);
        System.out.println("ExpiryDate: " + expiryDate);
        System.out.println("-----------------");
    }
}

public class pharmacy {
    ArrayList<Medicine> list = new ArrayList<>();
    ArrayList<String> billItems = new ArrayList<>();
    double billTotal = 0;
    Scanner sc = new Scanner(System.in);
    int lowStockLimit = 10;

    Medicine findById(int id) {
        for (Medicine m : list) {
            if (m.id == id) {
                return m;
            }
        }
        return null;
    }

    int readInt(String msg) {
        System.out.print(msg);
        return Integer.parseInt(sc.nextLine().trim());
    }

    double readDouble(String msg) {
        System.out.print(msg);
        return Double.parseDouble(sc.nextLine().trim());
    }

    String readText(String msg) {
        System.out.print(msg);
        return sc.nextLine().trim();
    }

    // 1
    void addMedicine() {
        int id = readInt("Enter ID: ");
        if (findById(id) != null) {
            System.out.println("ID already exists!");
            return;
        }
        String name = readText("Enter name: ");
        String category = readText("Enter category: ");
        double price = readDouble("Enter price: ");
        int quantity = readInt("Enter quantity: ");
        LocalDate expiry = LocalDate.parse(readText("Enter expiry date (yyyy-MM-dd): "));
        list.add(new Medicine(id, name, category, price, quantity, expiry));
        System.out.println("Medicine added.");
    }

    // 2
    void displayAll() {
        if (list.isEmpty()) {
            System.out.println("No medicines available.");
            return;
        }
        for (Medicine m : list) {
            m.display();
        }
    }

    // 3
    void searchByName() {
        String key = readText("Enter name: ").toLowerCase();
        boolean found = false;
        for (Medicine m : list) {
            if (m.name.toLowerCase().contains(key)) {
                m.display();
                found = true;
            }
        }
        if (!found) System.out.println("No medicine found.");
    }

    // 4
    void searchByCategory() {
        String key = readText("Enter category: ").toLowerCase();
        boolean found = false;
        for (Medicine m : list) {
            if (m.category.toLowerCase().equals(key)) {
                m.display();
                found = true;
            }
        }
        if (!found) System.out.println("No medicine found in this category.");
    }

    // 5
    void updateQuantity() {
        int id = readInt("Enter ID: ");
        Medicine m = findById(id);
        if (m == null) {
            System.out.println("Medicine not found.");
            return;
        }
        m.quantity = readInt("Enter new quantity: ");
        System.out.println("Quantity updated.");
    }

    // 6
    void sellMedicine() {
        int id = readInt("Enter ID: ");
        Medicine m = findById(id);
        if (m == null) {
            System.out.println("Medicine not found.");
            return;
        }
        if (m.expiryDate.isBefore(LocalDate.now())) {
            System.out.println("This medicine is expired. Cannot sell.");
            return;
        }
        int qty = readInt("Enter quantity to sell: ");
        if (qty <= 0 || qty > m.quantity) {
            System.out.println("Invalid quantity. Available: " + m.quantity);
            return;
        }
        m.quantity -= qty;
        double cost = qty * m.price;
        billTotal += cost;
        billItems.add(m.name + " x " + qty + " = " + cost);
        System.out.println("Sold. Cost: " + cost);
    }

    // 7
    void displayLowStock() {
        boolean found = false;
        for (Medicine m : list) {
            if (m.quantity < lowStockLimit) {
                m.display();
                found = true;
            }
        }
        if (!found) System.out.println("No low-stock medicines.");
    }

    // 8
    void checkExpired() {
        boolean found = false;
        for (Medicine m : list) {
            if (m.expiryDate.isBefore(LocalDate.now())) {
                m.display();
                found = true;
            }
        }
        if (!found) System.out.println("No expired medicines.");
    }

    // 9
    void generateBill() {
        if (billItems.isEmpty()) {
            System.out.println("No items sold yet.");
            return;
        }
        System.out.println("\n===== CUSTOMER BILL =====");
        for (String item : billItems) {
            System.out.println(item);
        }
        System.out.println("Total: " + billTotal);
        System.out.println("=========================");
        billItems.clear();
        billTotal = 0;
    }

    // 10
    void removeMedicine() {
        int id = readInt("Enter ID: ");
        Medicine m = findById(id);
        if (m == null) {
            System.out.println("Medicine not found.");
            return;
        }
        list.remove(m);
        System.out.println("Medicine removed.");
    }

    // 11
    void saveMedicines() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("medicines.dat"))) {
            out.writeObject(list);
            System.out.println("Saved to medicines.dat");
        } catch (IOException e) {
            System.out.println("Error saving: " + e.getMessage());
        }
    }

    // 12
    @SuppressWarnings("unchecked")
    void loadMedicines() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("medicines.dat"))) {
            list = (ArrayList<Medicine>) in.readObject();
            System.out.println("Loaded " + list.size() + " medicines.");
        } catch (FileNotFoundException e) {
            System.out.println("No saved file found.");
        } catch (Exception e) {
            System.out.println("Error loading: " + e.getMessage());
        }
    }

    void run() {
        while (true) {
            System.out.println("\n== PHARMACY MANAGEMENT SYSTEM ==");
            System.out.println("1. Add medicine");
            System.out.println("2. Display all medicine");
            System.out.println("3. Search by name");
            System.out.println("4. Search by category");
            System.out.println("5. Update quantity");
            System.out.println("6. Sell medicine");
            System.out.println("7. Display low-stock medicines");
            System.out.println("8. Check expired medicine");
            System.out.println("9. Generate customer bill");
            System.out.println("10. Remove medicine");
            System.out.println("11. Save medicine information");
            System.out.println("12. Load medicine information");
            System.out.println("0. Exit");
            try {
                int choice = readInt("Enter choice: ");
                switch (choice) {
                    case 1: addMedicine(); break;
                    case 2: displayAll(); break;
                    case 3: searchByName(); break;
                    case 4: searchByCategory(); break;
                    case 5: updateQuantity(); break;
                    case 6: sellMedicine(); break;
                    case 7: displayLowStock(); break;
                    case 8: checkExpired(); break;
                    case 9: generateBill(); break;
                    case 10: removeMedicine(); break;
                    case 11: saveMedicines(); break;
                    case 12: loadMedicines(); break;
                    case 0:
                        System.out.println("Thank you!");
                        return;
                    default:
                        System.out.println("Invalid choice!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid number entered");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        new pharmacy().run();
    }
}