# Game Leaderboard CLI

A simple, efficient command-line application to manage game high scores. This system uses a **Min-Heap** data structure to maintain a real-time list of the top `N` scores, ensuring optimal performance even with a large stream of score submissions.

## Features

- **Custom Leaderboard Size**: Initialize the leaderboard to track the top `K` players.
- **Efficient Ranking**: Uses a Min-Heap to quickly determine if a new score qualifies for the leaderboard.
- **Top Score Display**: View the current rankings sorted from highest to lowest.
- **Simple CLI**: Easy-to-use text commands for interaction.

## Prerequisites

- Java Development Kit (JDK) 8 or higher.

## How to Run

1. **Compile the code:**
   Open your terminal in this directory and run:
   ```bash
   javac GameCLI.java
   ```

2. **Run the application:**
   ```bash
   java GameCLI
   ```

## Usage

Once the application is running, you can use the following commands:

| Command | Description | Example |
|---------|-------------|---------|
| `INIT <k>` | Initializes the leaderboard with size `k`. **Must be run first.** | `INIT 3` |
| `SCORE <player> <val>` | Submits a score. If it qualifies, it's added. | `SCORE Alice 150` |
| `SHOW_TOP` | Displays the current top scores in descending order. | `SHOW_TOP` |
| `EXIT` | Exits the application. | `EXIT` |

## Example Session

```text
> INIT 3
Leaderboard initialized with size 3

> SCORE Alice 100
Score added.

> SCORE Bob 80
Score added.

> SCORE Charlie 120
Score added.

> SCORE Dave 150
Score qualified! Removed Bob: 80 and added Dave: 150

> SHOW_TOP
=== LEADERBOARD (Top 3) ===
#1 Dave: 150
#2 Charlie: 120
#3 Alice: 100
==================================

> EXIT
System shutting down.
```

## Implementation Details

The leaderboard maintains a **Priority Queue (Min-Heap)** of size `K`.
- **Insertion**: When the heap is not full, new scores are simply added.
- **Replacement**: When the heap is full, a new score is compared to the minimum element (the root). If the new score is larger, the root is removed and the new score is inserted. This ensures that we always keep the largest `K` elements.
