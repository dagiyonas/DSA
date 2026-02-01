# VIP Spender Tracker

## Overview
The VIP Spender Tracker is a Java-based console application designed to track customer purchases and dynamically maintain a leaderboard of the top K highest spenders. The system efficiently updates and displays VIP customers based on their total spending.

This project demonstrates the practical use of HashMaps, Priority Queues (Min-Heaps), and object-oriented programming concepts in Java.

---

## Features
- Tracks total spending for each user
- Maintains the Top K spenders in real time
- Efficient leaderboard updates using a Min-Heap
- Command-line interface for user interaction
- Displays VIP customers in descending order of spending

---

## Technologies Used
- Java
- Java Collections Framework
  - HashMap
  - PriorityQueue
  - ArrayList
- Object-Oriented Programming (OOP)

---

## System Design

### Data Structures Used

1. HashMap (spendingMap)  
   - Stores each user’s total spending  
   - Acts as the main data source  

2. PriorityQueue (topVips)  
   - Implemented as a Min-Heap  
   - Keeps track of the top K spenders  
   - The smallest spender among the VIPs is always at the root  

---

## How the System Works

1. When a purchase is processed, the user’s total spending is updated in the HashMap.
2. The leaderboard is updated using the following logic:
   - If the user already exists in the Top K list, their value is updated.
   - If the user is not in the Top K list:
     - They are added if the leaderboard is not full.
     - They replace the lowest VIP spender if their total spending is higher.
3. When displayed, the VIP list is sorted in descending order for better readability.

---

## Available Commands

| Command | Description |
|-------|-------------|
| PURCHASE <user> <amount> | Records a purchase and updates the leaderboard |
| SHOW_VIP | Displays the Top K spenders |
| EXIT | Terminates the application |

---

## Example Usage

# VIP Spender Tracker

## Overview
The VIP Spender Tracker is a Java-based console application designed to track customer purchases and dynamically maintain a leaderboard of the top K highest spenders. The system efficiently updates and displays VIP customers based on their total spending.

This project demonstrates the practical use of HashMaps, Priority Queues (Min-Heaps), and object-oriented programming concepts in Java.

---

## Features
- Tracks total spending for each user
- Maintains the Top K spenders in real time
- Efficient leaderboard updates using a Min-Heap
- Command-line interface for user interaction
- Displays VIP customers in descending order of spending

---

## Technologies Used
- Java
- Java Collections Framework
  - HashMap
  - PriorityQueue
  - ArrayList
- Object-Oriented Programming (OOP)

---

## System Design

### Data Structures Used

1. HashMap (spendingMap)  
   - Stores each user’s total spending  
   - Acts as the main data source  

2. PriorityQueue (topVips)  
   - Implemented as a Min-Heap  
   - Keeps track of the top K spenders  
   - The smallest spender among the VIPs is always at the root  

---

## How the System Works

1. When a purchase is processed, the user’s total spending is updated in the HashMap.
2. The leaderboard is updated using the following logic:
   - If the user already exists in the Top K list, their value is updated.
   - If the user is not in the Top K list:
     - They are added if the leaderboard is not full.
     - They replace the lowest VIP spender if their total spending is higher.
3. When displayed, the VIP list is sorted in descending order for better readability.

---

## Available Commands

| Command | Description |
|-------|-------------|
| PURCHASE <user> <amount> | Records a purchase and updates the leaderboard |
| SHOW_VIP | Displays the Top K spenders |
| EXIT | Terminates the application |

---

## Example Usage

PURCHASE Alice 150
PURCHASE Bob 200
PURCHASE Charlie 120
PURCHASE Alice 100
SHOW_VIP

### Sample Output

=== TOP 3 SPENDERS ===
#1 Alice ($250.00)
#2 Bob ($200.00)
#3 Charlie ($120.00)

---

## Configuration

The number of VIP spenders tracked by the system can be changed by modifying the following constant in the source code:

```java
private static final int K = 3;

Error Handling

     Invalid commands are handled gracefully

     Non-numeric amounts generate an error message

     Incorrect command formats display usage instructions

Conclusion

The VIP Spender Tracker provides an efficient and structured approach to identifying high-value customers. It serves as a strong academic example of data structure usage and real-time data processing in Java.

---

 
