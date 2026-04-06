import java.util.*;

class Service {
    private String name;
    private double cost;

    public Service(String name, double cost) {
        this.name = name;
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public double getCost() {
        return cost;
    }

    @Override
    public String toString() {
        return name + " (₹" + cost + ")";
    }
}

public class UseCase7AddOnServiceSelection {

    // Reservation ID -> List of Services
    private Map<String, List<Service>> reservationServices = new HashMap<>();

    // Attach services to a reservation
    public void addServicesToReservation(String reservationId, List<Service> services) {
        reservationServices.putIfAbsent(reservationId, new ArrayList<>());
        reservationServices.get(reservationId).addAll(services);
        System.out.println("Services added to Reservation " + reservationId + ": " + services);
    }

    // Calculate total cost of services for a reservation
    public double calculateAdditionalCost(String reservationId) {
        if (!reservationServices.containsKey(reservationId)) {
            return 0.0;
        }
        double total = 0.0;
        for (Service s : reservationServices.get(reservationId)) {
            total += s.getCost();
        }
        return total;
    }

    // Display all services for a reservation
    public void displayReservationServices(String reservationId) {
        if (!reservationServices.containsKey(reservationId)) {
            System.out.println("No services selected for Reservation " + reservationId);
            return;
        }
        System.out.println("Reservation " + reservationId + " has services: " + reservationServices.get(reservationId));
        System.out.println("Total Additional Cost: ₹" + calculateAdditionalCost(reservationId));
    }

    // Main method for demonstration
    public static void main(String[] args) {
        UseCase7AddOnServiceSelection manager = new UseCase7AddOnServiceSelection();

        // Example reservations (from Use Case 6, assume IDs exist)
        String reservation1 = "Deluxe-1";
        String reservation2 = "Suite-1";

        // Define services
        Service breakfast = new Service("Breakfast", 500.0);
        Service spa = new Service("Spa Access", 1500.0);
        Service airportPickup = new Service("Airport Pickup", 1000.0);

        // Attach services
        manager.addServicesToReservation(reservation1, Arrays.asList(breakfast, spa));
        manager.addServicesToReservation(reservation2, Arrays.asList(airportPickup));

        // Display results
        manager.displayReservationServices(reservation1);
        manager.displayReservationServices(reservation2);
    }
}