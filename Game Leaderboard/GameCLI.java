import java.util.*;

/**
 * Represents a single score submission.
 * Implements Comparable to allow sorting by score.
 */
class ScoreEntry implements Comparable<ScoreEntry> {
    private final String playerId;
    private final int score;

    public ScoreEntry(String playerId, int score) {
        this.playerId = playerId;
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public String getPlayerId() {
        return playerId;
    }

    @Override
    public String toString() {
        return String.format("%s: %d", playerId, score);
    }

    /**
     * Comparison logic for the PriorityQueue.
     * We want natural ordering (ascending) for the Min-Heap.
     */
    @Override
    public int compareTo(ScoreEntry other) {
        return Integer.compare(this.score, other.score);
    }
}

/**
 * Core Logic for the Leaderboard.
 * Uses a Min-Heap to maintain the Top K scores.
 */
class Leaderboard {
    private PriorityQueue<ScoreEntry> minHeap;
    private int capacity;
    private boolean isInitialized;

    public Leaderboard() {
        this.isInitialized = false;
    }

    /**
     * INIT <k>
     * Initializes the leaderboard with a fixed capacity.
     */
    public void init(int k) {
        if (k <= 0) {
            System.out.println("Error: K must be positive.");
            return;
        }
        // Min-Heap (Default behavior of PriorityQueue is ascending order)
        this.minHeap = new PriorityQueue<>();
        this.capacity = k;
        this.isInitialized = true;
        System.out.println("Leaderboard initialized with size " + k);
    }

    /**
     * SCORE <player> <val>
     * Logic:
     * 1. If heap is not full, add it.
     * 2. If heap is full, compare new score with Root (Gatekeeper).
     * 3. If New Score > Root, replace Root.
     */
    public void submitScore(String player, int score) {
        if (!isInitialized) {
            System.out.println("Error: Please run INIT <k> first.");
            return;
        }

        ScoreEntry newEntry = new ScoreEntry(player, score);

        if (minHeap.size() < capacity) {
            minHeap.offer(newEntry);
            System.out.println("Score added.");
        } else {
            // The heap is full. Check the "Gatekeeper" (the smallest of the top K)
            ScoreEntry gatekeeper = minHeap.peek();

            if (gatekeeper != null && score > gatekeeper.getScore()) {
                // New score is better than the lowest on the board.
                // Remove the lowest and add the new one.
                ScoreEntry removed = minHeap.poll();
                minHeap.offer(newEntry);
                System.out.println("Score qualified! Removed " + removed + " and added " + newEntry);
            } else {
                // New score is not high enough to beat the Gatekeeper.
                System.out.println("Score ignored (Too low to make Top " + capacity + ").");
            }
        }
    }

    /**
     * SHOW_TOP
     * Prints the leaderboard.
     * Note: We must sort a copy of the data for display because
     * the Heap only guarantees the Root is the minimum, not total sorted order.
     */
    public void showTop() {
        if (!isInitialized) {
            System.out.println("Error: Leaderboard not initialized.");
            return;
        }

        if (minHeap.isEmpty()) {
            System.out.println("Leaderboard is empty.");
            return;
        }

        System.out.println("=== LEADERBOARD (Top " + capacity + ") ===");
        
        // Create a copy to sort for display (High to Low) without ruining the Heap
        List<ScoreEntry> sortedList = new ArrayList<>(minHeap);
        sortedList.sort(Collections.reverseOrder());

        int rank = 1;
        for (ScoreEntry entry : sortedList) {
            System.out.printf("#%d %s%n", rank++, entry);
        }
        System.out.println("==================================");
    }
}

/**
 * Main Interface to handle CLI Commands.
 */
public class GameCLI {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Leaderboard leaderboard = new Leaderboard();

        System.out.println("System Started. Commands: INIT <k>, SCORE <player> <val>, SHOW_TOP, EXIT");

        while (true) {
            System.out.print("> ");
            if (!scanner.hasNextLine()) break;
            
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) continue;

            String[] parts = line.split("\\s+");
            String command = parts[0].toUpperCase();

            try {
                switch (command) {
                    case "INIT":
                        if (parts.length < 2) {
                            System.out.println("Usage: INIT <k>");
                        } else {
                            int k = Integer.parseInt(parts[1]);
                            leaderboard.init(k);
                        }
                        break;

                    case "SCORE":
                        if (parts.length < 3) {
                            System.out.println("Usage: SCORE <player> <val>");
                        } else {
                            String player = parts[1];
                            int val = Integer.parseInt(parts[2]);
                            leaderboard.submitScore(player, val);
                        }
                        break;

                    case "SHOW_TOP":
                        leaderboard.showTop();
                        break;

                    case "EXIT":
                        System.out.println("System shutting down.");
                        scanner.close();
                        return;

                    default:
                        System.out.println("Unknown command.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Invalid number format.");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}