import java.util.*;

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

    @Override
    public int compareTo(ScoreEntry other) {
        return Integer.compare(this.score, other.score);
    }
}

class Leaderboard {
    private PriorityQueue<ScoreEntry> minHeap;
    private int capacity;
    private boolean isInitialized;

    public Leaderboard() {
        this.isInitialized = false;
    }
    
    public void init(int k) {
        if (k <= 0) {
            System.out.println("Error: K must be positive.");
            return;
        }
        
        this.minHeap = new PriorityQueue<>();
        this.capacity = k;
        this.isInitialized = true;
        System.out.println("Leaderboard initialized with size " + k);
    }

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
            ScoreEntry gatekeeper = minHeap.peek();

            if (gatekeeper != null && score > gatekeeper.getScore()) {
                ScoreEntry removed = minHeap.poll();
                minHeap.offer(newEntry);
                System.out.println("Score qualified! Removed " + removed + " and added " + newEntry);
            } else {
                System.out.println("Score ignored (Too low to make Top " + capacity + ").");
            }
        }
    }

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
        
        List<ScoreEntry> sortedList = new ArrayList<>(minHeap);
        sortedList.sort(Collections.reverseOrder());

        int rank = 1;
        for (ScoreEntry entry : sortedList) {
            System.out.printf("#%d %s%n", rank++, entry);
        }
        System.out.println("==================================");
    }
}

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