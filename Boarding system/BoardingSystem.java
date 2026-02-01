import java.util.*;

// Passenger class
class Passenger {
    String name;
    int pClass;        // 0-6 priority
    int groupId;       // 0 = no group
    Passenger next;    // linked list for group

    Passenger(String name, int pClass, int groupId) {
        this.name = name;
        this.pClass = pClass;
        this.groupId = groupId;
        this.next = null;
    }
}

public class BoardingSystem {
    static final int MAX_PRIORITY = 7;
    static Queue<Passenger>[] queues = new LinkedList[MAX_PRIORITY];
    static Map<Integer, Passenger> groupMap = new HashMap<>();
    static Set<String> boardedNames = new HashSet<>();
    static int gateCapacity = 5;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // initialize queues
        for (int i = 0; i < MAX_PRIORITY; i++) {
            queues[i] = new LinkedList<>();
        }

        printWelcome();

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

    // Welcome message
    static void printWelcome() {
        System.out.println("\nWelcome to Multi-Class Boarding System");
        System.out.println("==========================================");
        System.out.println("\nAvailable Commands:");
        System.out.println("  CHECKIN <name> <class> [group_id]");
        System.out.println("    Classes: 0=Wheelchair, 1=First, 2=Business, 3=Premium, 4=Economy+, 5=Economy, 6=Standby");
        System.out.println("  SET_GATE_CAPACITY <number>");
        System.out.println("  BOARD");
        System.out.println("  STATUS");
        System.out.println("  HELP");
        System.out.println("  EXIT\n");
        System.out.println("Example: CHECKIN John 2 101");
        System.out.println("==========================================\n");
    }

    // Check-in handler
    static void handleCheckin(String[] parts) {
        if (parts.length < 3) {
            System.out.println("Usage: CHECKIN <name> <class> [group_id]");
            return;
        }

        String name = parts[1];
        int pClass;
        try {
            pClass = Integer.parseInt(parts[2]);
        } catch (NumberFormatException e) {
            System.out.println("Class must be a number 0-6");
            return;
        }
        if (pClass < 0 || pClass > 6) {
            System.out.println("Class must be between 0 and 6");
            return;
        }

        int groupId = 0;
        if (parts.length >= 4) {
            try {
                groupId = Integer.parseInt(parts[3]);
            } catch (NumberFormatException e) {
                System.out.println("Group ID must be a number");
                return;
            }
        }

        // Enforce same-class groups
        if (groupId != 0 && groupMap.containsKey(groupId)) {
            if (groupMap.get(groupId).pClass != pClass) {
                System.out.println("Error: All members of a group must have the same class.");
                return;
            }
        }

        Passenger p = new Passenger(name, pClass, groupId);

        // Add to group linked list
        if (groupId != 0) {
            if (groupMap.containsKey(groupId)) {
                Passenger tail = groupMap.get(groupId);
                while (tail.next != null) tail = tail.next;
                tail.next = p;
            } else {
                groupMap.put(groupId, p);
            }
        }

        queues[pClass].offer(p);
        System.out.println("+ " + name + " checked in to " + getClassName(pClass)
                + (groupId != 0 ? " in group " + groupId : ""));
    }

    // Gate capacity
    static void handleSetGate(String[] parts) {
        if (parts.length != 2) {
            System.out.println("Usage: SET_GATE_CAPACITY <number>");
            return;
        }
        try {
            int capacity = Integer.parseInt(parts[1]);
            if (capacity <= 0) {
                System.out.println("Gate capacity must be positive");
                return;
            }
            gateCapacity = capacity;
            System.out.println("Gate capacity set to " + gateCapacity);
        } catch (NumberFormatException e) {
            System.out.println("Gate capacity must be a number");
        }
    }

    // Board passengers
    static void handleBoard() {
        int boardedThisTick = 0;
        System.out.println("\nBoarding passengers...");

        for (int i = 0; i < MAX_PRIORITY && boardedThisTick < gateCapacity; i++) {
            Queue<Passenger> queue = queues[i];
            while (!queue.isEmpty() && boardedThisTick < gateCapacity) {
                Passenger p = queue.poll();

                // Skip if already boarded
                if (boardedNames.contains(p.name)) continue;

                // Group boarding
                if (p.groupId != 0 && groupMap.containsKey(p.groupId)) {
                    Passenger groupHead = groupMap.get(p.groupId);

                    // Count unboarded group size
                    int groupSize = 0;
                    Passenger temp = groupHead;
                    while (temp != null) {
                        if (!boardedNames.contains(temp.name)) groupSize++;
                        temp = temp.next;
                    }

                    // Check if group fits in remaining capacity
                    if (boardedThisTick + groupSize > gateCapacity) {
                        // Not enough space for entire group, skip group
                        continue;
                    }

                    System.out.println("Boarding group " + p.groupId + ":");
                    
                    // Remove all group members from ALL queues first
                    temp = groupHead;
                    while (temp != null) {
                        queues[temp.pClass].remove(temp);
                        temp = temp.next;
                    }
                    
                    // Then board all group members
                    Passenger gm = groupHead;
                    while (gm != null) {
                        if (!boardedNames.contains(gm.name)) {
                            boardedNames.add(gm.name);
                            System.out.println("  " + gm.name);
                            boardedThisTick++;
                        }
                        gm = gm.next;
                    }

                    groupMap.remove(p.groupId);

                } else {
                    boardedNames.add(p.name);
                    System.out.println("  " + p.name);
                    boardedThisTick++;
                }
            }
        }

        if (boardedThisTick == 0) {
            System.out.println("No passengers to board.");
        } else {
            System.out.println("Total boarded this round: " + boardedThisTick + " passengers\n");
        }
    }

    // Status
    static void handleStatus() {
        System.out.println("\nCurrent Boarding Status");
        int totalWaiting = 0;
        for (int i = 0; i < MAX_PRIORITY; i++) {
            int count = queues[i].size();
            totalWaiting += count;
            System.out.println("  " + getClassName(i) + ": " + count + " waiting");
        }
        System.out.println("  Total waiting: " + totalWaiting);
        System.out.println("  Gate capacity: " + gateCapacity + "\n");
    }

    static void showHelp() {
        System.out.println("\nAvailable Commands:");
        System.out.println("CHECKIN <name> <class> [group_id]");
        System.out.println("SET_GATE_CAPACITY <number>");
        System.out.println("BOARD");
        System.out.println("STATUS");
        System.out.println("HELP");
        System.out.println("EXIT\n");
    }

    static String getClassName(int pClass) {
        switch (pClass) {
            case 0: return "Wheelchair";
            case 1: return "First Class";
            case 2: return "Business Class";
            case 3: return "Premium Economy";
            case 4: return "Economy Plus";
            case 5: return "Economy";
            case 6: return "Standby";
            default: return "Unknown Class";
        }
    }
}
