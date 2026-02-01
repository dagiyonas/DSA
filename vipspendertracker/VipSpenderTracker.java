import java.util.*;

public class VipSpenderTracker {

    
    private static final int K = 3; // We want the Top 3

    // ---
    
    // 1. Hash Map: UserID -> TotalMoney (The Source of Truth)
    private Map<String, Double> spendingMap;

    // 2. Min-Heap: Stores (Total, UserID). Root is the smallest of the top K.
    private PriorityQueue<Customer> topVips;

    // --- Constructor ---
    public VipSpenderTracker() {
        this.spendingMap = new HashMap<>();
        
        // Initialize Min-Heap ordered by amount (ascending)
        this.topVips = new PriorityQueue<>(Comparator.comparingDouble(c -> c.amount));
    }

    // --- Helper Class for Heap Storage ---
    // Represents a snapshot of a user's spending
    static class Customer {
        String userId;
        double amount;

        public Customer(String userId, double amount) {
            this.userId = userId;
            this.amount = amount;
        }

        @Override
        public String toString() {
            return String.format("%s: $%.2f", userId, amount);
        }
    }

    // --- Core Logic: PURCHASE ---
    public void processPurchase(String userId, double amount) {
        // 1. Update the TotalSpend[User] in HashMap
        double oldTotal = spendingMap.getOrDefault(userId, 0.0);
        double newTotal = oldTotal + amount;
        spendingMap.put(userId, newTotal);

        System.out.println(">> Processed: " + userId + " spent $" + amount + " (Total: $" + newTotal + ")");

        // 2. Update the Leaderboard (Min-Heap)
        updateLeaderboard(userId, oldTotal, newTotal);
    }

    private void updateLeaderboard(String userId, double oldTotal, double newTotal) {
        // Check if this user is ALREADY in the heap
        // Note: Removing from PQ is O(K), which is fine for small K (like 3 or 10)
        boolean wasInHeap = false;
        Iterator<Customer> it = topVips.iterator();
        while (it.hasNext()) {
            Customer c = it.next();
            if (c.userId.equals(userId)) {
                it.remove(); // Remove the old entry to update it
                wasInHeap = true;
                break;
            }
        }

        if (wasInHeap) {
            // User was already a VIP, just re-insert with new total
            topVips.offer(new Customer(userId, newTotal));
        } else {
            // User was NOT in the heap. Should they enter?
            if (topVips.size() < K) {
                // Heap isn't full yet, just add them
                topVips.offer(new Customer(userId, newTotal));
            } else {
                // Heap is full. Compare with the "Poorest of the Rich" (Root)
                Customer poorestVip = topVips.peek();
                if (newTotal > poorestVip.amount) {
                    // New total is better than the worst VIP. Replace them.
                    topVips.poll(); // Remove root
                    topVips.offer(new Customer(userId, newTotal)); // Add new star
                }
            }
        }
    }

    // --- Core Logic: SHOW_VIP ---
    public void showVip() {
        if (topVips.isEmpty()) {
            System.out.println("Leaderboard is empty.");
            return;
        }

        // We create a temporary list to sort them Descending for display purposes
        // (Because the Heap stores them Ascending/Min-first)
        List<Customer> sortedVips = new ArrayList<>(topVips);
        sortedVips.sort((a, b) -> Double.compare(b.amount, a.amount));

        System.out.println("=== TOP " + K + " SPENDERS ===");
        int rank = 1;
        for (Customer c : sortedVips) {
            System.out.printf("#%d %s ($%.2f)\n", rank++, c.userId, c.amount);
        }
        System.out.println("========================");
    }

    // --- CLI Driver ---
    public static void main(String[] args) {
        VipSpenderTracker system = new VipSpenderTracker();
        Scanner scanner = new Scanner(System.in);

        System.out.println("VIP Tracker System Started (Top " + K + ")");
        System.out.println("Commands: PURCHASE <user> <amount>  |  SHOW_VIP  |  EXIT");

        while (true) {
            System.out.print("\n> ");
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) continue;

            String[] parts = line.split("\\s+");
            String command = parts[0].toUpperCase();

            try {
                switch (command) {
                    case "PURCHASE":
                        if (parts.length < 3) {
                            System.out.println("Usage: PURCHASE <user> <amount>");
                            break;
                        }
                        String user = parts[1];
                        double amount = Double.parseDouble(parts[2]);
                        system.processPurchase(user, amount);
                        break;

                    case "SHOW_VIP":
                        system.showVip();
                        break;

                    case "EXIT":
                        System.out.println("Shutting down...");
                        return;

                    default:
                        System.out.println("Unknown command.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Amount must be a number.");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}