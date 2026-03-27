/**
 * UseCase3InventorySetup.java
 *
 * This program demonstrates centralized room inventory management
 * using a HashMap to store and manage availability.
 *
 * @author YourName
 * @version 3.1
 */

import java.util.HashMap;
import java.util.Map;

// Inventory class encapsulating availability logic
class RoomInventory {
    private Map<String, Integer> inventory;

    // Constructor initializes availability
    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 2);
    }

    // Method to get availability
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    // Method to update availability
    public void updateAvailability(String roomType, int newCount) {
        if (inventory.containsKey(roomType)) {
            inventory.put(roomType, newCount);
        } else {
            System.out.println("Room type not found in inventory.");
        }
    }

    // Method to display current inventory
    public void displayInventory() {
        System.out.println("Current Room Inventory:");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " - Available: " + entry.getValue());
        }
    }
}

// Application entry point
public class CentralizedRoomInventoryManagement {
    public static void main(String[] args) {
        System.out.println("Welcome to the Hotel Booking System v3.1");
        System.out.println("Initializing centralized room inventory...\n");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Display initial inventory
        inventory.displayInventory();

        System.out.println("\nUpdating inventory...");
        inventory.updateAvailability("Single Room", 4); // Example update
        inventory.updateAvailability("Suite Room", 1);

        // Display updated inventory
        System.out.println();
        inventory.displayInventory();

        System.out.println("\nEnd of program. Application terminated.");
    }
}