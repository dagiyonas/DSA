import java.util.*;

// Passenger class
class Passenger {
    String name;
    int pClass;        // 0-6 priority (0 = wheelchair, 1 = first, ..., 5 = basic, 6 = standby)
    int groupId;       // 0 = no group
    Passenger next;    // For linked list in group

    Passenger(String name, int pClass, int groupId) {
        this.name = name;
        this.pClass = pClass;
        this.groupId = groupId;
        this.next = null;
    }
}

// Main system
public class BoardingSystem {
    static final int MAX_PRIORITY = 7; // 0-6
    static Queue<Passenger>[] queues = new LinkedList[MAX_PRIORITY]; // Array of queues
    static Map<Integer, Passenger> groupMap = new HashMap<>(); // Map to link group members
    static List<String> boarded = new ArrayList<>(); // Array to track boarded passengers
    static int gateCapacity = 5; // default

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Initialize queues
        for (int i = 0; i < MAX_PRIORITY; i++) {
            queues[i] = new LinkedList<>();
        }

        System.out.println("\nWelcome to Multi-Class Boarding System");
        System.out.println("==========================================");
        System.out.println("\nAvailable Commands:");
        System.out.println("  CHECKIN <name> <class> [group_id]  - Add passenger to queue");
        System.out.println("    Classes: 0=Wheelchair, 1=First, 2=Business, 3=Premium, 4=Economy+, 5=Economy, 6=Standby");
        System.out.println("  SET_GATE_CAPACITY <number>        - Set gate capacity (default: 5)");
        System.out.println("  BOARD                             - Board next group of passengers");
        System.out.println("  STATUS                            - Show current queue status");
        System.out.println("  HELP                              - Show this help menu");
        System.out.println("  EXIT                              - Exit the system");
        System.out.println("\nExample: CHECKIN John 2 101  (adds John to business class in group 101)");
        System.out.println("==========================================\n");

        while (true) {
            System.out.print("> ");
            String line = sc.nextLine().trim();
            if (line.isEmpty()) continue;
            String[] parts = line.split(" ");
            String command = parts[0].toUpperCase();

            switch (command) {
                case "CHECKIN":
                    handleCheckin(parts);
                    break;
                case "SET_GATE_CAPACITY":
                    handleSetGate(parts);
                    break;
                case "BOARD":
                    handleBoard();
                    break;
                case "STATUS":
                    handleStatus();
                    break;
                case "HELP":
                    showHelp();
                    break;
                case "EXIT":
                    System.out.println("\nThank you for using the Boarding System. Goodbye!");
                    sc.close();
                    return;
                default:
                    System.out.println("Unknown command. Type 'HELP' for available commands.");
            }
        }
    }

    // Handle CHECKIN <name> <class> [group_id]
    static void handleCheckin(String[] parts) {
        if (parts.length < 3) {
            System.out.println("Usage: CHECKIN <name> <class> [group_id]");
            System.out.println("   Example: CHECKIN Alice 2 101");
            return;
        }

        String name = parts[1];
        int pClass;
        try {
            pClass = Integer.parseInt(parts[2]);
        } catch (NumberFormatException e) {
            System.out.println("Class must be a number 0-6");
            System.out.println("   0=Wheelchair, 1=First, 2=Business, 3=Premium, 4=Economy+, 5=Economy, 6=Standby");
            return;
        }

        if (pClass < 0 || pClass > 6) {
            System.out.println("Class must be between 0 and 6");
            return;
        }

        int groupId = 0; // default no group
        if (parts.length >= 4) {
            try {
                groupId = Integer.parseInt(parts[3]);
            } catch (NumberFormatException e) {
                System.out.println("Group ID must be a number");
                return;
            }
        }

        Passenger p = new Passenger(name, pClass, groupId);

        // If in group, link to existing group
        if (groupId != 0) {
            if (groupMap.containsKey(groupId)) {
                Passenger head = groupMap.get(groupId);
                // Find tail
                Passenger tail = head;
                while (tail.next != null) tail = tail.next;
                tail.next = p;
            } else {
                groupMap.put(groupId, p);
            }
        }

        queues[pClass].offer(p);
        String className = getClassName(pClass);
        System.out.println("+ " + name + " checked in to " + className + (groupId != 0 ? " in group " + groupId : ""));
    }

    // Handle SET_GATE_CAPACITY <g>
    static void handleSetGate(String[] parts) {
        if (parts.length != 2) {
            System.out.println("Usage: SET_GATE_CAPACITY <number>");
            System.out.println("   Example: SET_GATE_CAPACITY 8");
            return;
        }
        try {
            int capacity = Integer.parseInt(parts[1]);
            if (capacity <= 0) {
                System.out.println("Gate capacity must be a positive number");
                return;
            }
            gateCapacity = capacity;
            System.out.println("Gate capacity set to " + gateCapacity + " passengers");
        } catch (NumberFormatException e) {
            System.out.println("Gate capacity must be a number");
        }
    }

    // Handle BOARD
    static void handleBoard() {
        int boardedThisTick = 0;
        System.out.println("\nBoarding passengers...");

        for (int i = 0; i < MAX_PRIORITY && boardedThisTick < gateCapacity; i++) {
            while (!queues[i].isEmpty() && boardedThisTick < gateCapacity) {
                Passenger p = queues[i].poll();
                // If in group, board whole group
                if (p.groupId != 0 && groupMap.containsKey(p.groupId)) {
                    Passenger groupMember = groupMap.get(p.groupId);
                    System.out.println("Boarding group " + p.groupId + ":");
                    while (groupMember != null && boardedThisTick < gateCapacity) {
                        boarded.add(groupMember.name);
                        System.out.println("  " + groupMember.name);
                        boardedThisTick++;
                        groupMember = groupMember.next;
                    }
                    groupMap.remove(p.groupId);
                } else {
                    boarded.add(p.name);
                    System.out.println("  " + p.name);
                    boardedThisTick++;
                }
            }
        }

        if (boardedThisTick == 0) {
            System.out.println("No passengers to board.");
        } else {
            System.out.println("Total boarded this round: " + boardedThisTick + " passengers");
        }
        System.out.println();
    }

    // Handle STATUS
    static void handleStatus() {
        System.out.println("\nCurrent Boarding Status");
        System.out.println("==========================");
        
        int totalWaiting = 0;
        for (int i = 0; i < MAX_PRIORITY; i++) {
            int count = queues[i].size();
            totalWaiting += count;
            String className = getClassName(i);
            System.out.println("  " + className + ": " + count + " passengers waiting");
        }
        
        System.out.println("  --------------------------");
        System.out.println("  Total waiting: " + totalWaiting + " passengers");
        System.out.println("  Total boarded: " + boarded.size() + " passengers");
        System.out.println("  Gate capacity: " + gateCapacity + " passengers per round");
        System.out.println("==========================\n");
    }

    // Show help menu
    static void showHelp() {
        System.out.println("\nAvailable Commands:");
        System.out.println("==========================");
        System.out.println("  CHECKIN <name> <class> [group_id]  - Add passenger to queue");
        System.out.println("    Classes: 0=Wheelchair, 1=First, 2=Business, 3=Premium, 4=Economy+, 5=Economy, 6=Standby");
        System.out.println("  SET_GATE_CAPACITY <number>        - Set gate capacity (default: 5)");
        System.out.println("  BOARD                             - Board next group of passengers");
        System.out.println("  STATUS                            - Show current queue status");
        System.out.println("  HELP                              - Show this help menu");
        System.out.println("  EXIT                              - Exit the system");
        System.out.println("\nExamples:");
        System.out.println("  CHECKIN Alice 2 101  (adds Alice to business class in group 101)");
        System.out.println("  CHECKIN Bob 5       (adds Bob to economy class, no group)");
        System.out.println("  SET_GATE_CAPACITY 8");
        System.out.println("==========================\n");
    }

    // Get class name for display
    static String getClassName(int pClass) {
        switch (pClass) {
            case 0: return "Wheelchair";
            case 1: return "First Class";
            case 2: return "Business Class";
            case 3: return "Premium Economy";
            case 4: return "Economy Plus";
            case 5: return "Economy";
            case 6: return "Standby";
            default: return "Unknown Class " + pClass;
        }
    }
}
