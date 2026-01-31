## Project Structure

```
DSA/
├── data_structures/          # Core data structure implementations
├── Boarding system/          # Airline boarding system simulation
│   ├── BoardingSystem.java   # Multi-class passenger boarding system
│   └── README.md            # Project-specific documentation
└── README.md                # This file
```

## Featured Projects

### Airline Boarding System

A sophisticated multi-class passenger boarding system that demonstrates practical application of queues, priority management, and group handling algorithms.

**Key Features:**
- **Priority-based boarding**: 7 priority levels from wheelchair assistance to standby
- **Group management**: Families and groups board together
- **Dynamic gate capacity**: Configurable boarding capacity per round
- **Real-time status tracking**: Monitor queue status and boarding progress

**Priority Classes:**
- **Class 0**: Wheelchair assistance
- **Class 1**: First Class
- **Class 2**: Business Class
- **Class 3**: Premium Economy
- **Class 4**: Economy Plus
- **Class 5**: Economy
- **Class 6**: Standby

#### Quick Start

```bash
cd "Boarding system"
javac BoardingSystem.java
java BoardingSystem
```

#### Example Usage

```
> CHECKIN Alice 2 101
Alice checked in to Business Class in group 101

> CHECKIN Bob 2 101
Bob checked in to Business Class in group 101


> BOARD
 Boarding passengers...
Boarding group 101:
  Alice
  Bob
  Charlie
 Total boarded this round: 3 passengers
```

#### Available Commands

- `CHECKIN <name> <class> [group_id]` - Add passenger to queue
- `SET_GATE_CAPACITY <number>` - Set gate capacity (default: 5)
- `BOARD` - Board next group of passengers
- `STATUS` - Show current queue status
- `HELP` - Show help menu
- `EXIT` - Exit the system

## Technical Implementation

### Data Structures Used

- **Queues**: Priority-based passenger queues using `LinkedList`
- **HashMap**: Group member tracking and lookup
- **ArrayList**: Boarded passenger tracking
- **Linked Lists**: Group member chaining

### Algorithm Highlights

1. **Priority Queue Implementation**: Multi-level priority system ensuring fair boarding order
2. **Group Boarding Algorithm**: Efficient group detection and boarding to keep families together
3. **Capacity Management**: Dynamic boarding capacity with overflow handling

## Learning Objectives

This repository is designed to help you understand:

- **Queue operations** and priority-based processing
- **Hash map applications** for group management
- **Linked list traversal** for group member chaining
- **Real-world algorithm design** for practical problems
- **User interface design** for command-line applications

## Use Cases

The boarding system demonstrates practical applications of data structures in:

- **Airline operations**: Realistic passenger boarding simulation
- **Priority scheduling**: Understanding multi-level priority systems
- **Resource management**: Gate capacity and passenger flow optimization
- **Group coordination**: Keeping related entities together in processing

## Contributing

This repository serves as both a learning resource and a collection of algorithm implementations. Feel free to:

- Study the implementations
- Suggest improvements
- Add new data structure implementations
- Report issues or bugs

## Future Enhancements

Planned additions to this repository:

- [ ] More data structure implementations in `data_structures/`
- [ ] Algorithm complexity analysis
- [ ] Unit tests for all implementations
- [ ] Performance benchmarks
- [ ] Additional real-world applications