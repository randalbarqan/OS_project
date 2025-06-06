import java.util.Scanner;

class MemoryBlock {
    int size;
    int startAddress;
    int endAddress;
    String status; // "allocated" or "free"
    String processID; // "Null" when free
    int internalFragmentation;

    public MemoryBlock(int size, int startAddress) {
        this.size = size;
        this.startAddress = startAddress;
        this.endAddress = startAddress + size - 1;
        this.status = "free";
        this.processID = "Null";
        this.internalFragmentation = 0;
    }
}

public class Memory {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the total number of blocks: ");
        int M = scanner.nextInt();
        int[] sizes = new int[M];

        System.out.print("Enter the size of each block in KB: ");
        for (int i = 0; i < M; i++) {
            sizes[i] = scanner.nextInt();
        }

        MemoryBlock[] memoryBlocks = new MemoryBlock[M];
        int startAddress = 0;
        for (int i = 0; i < M; i++) {
            memoryBlocks[i] = new MemoryBlock(sizes[i], startAddress);
            startAddress += sizes[i];
        }

        System.out.print("Enter allocation strategy (1 for first-fit, 2 for best-fit, 3 for worst-fit): ");
        int strategy = scanner.nextInt();

        System.out.println("Memory blocks are created…");
        System.out.println("Memory blocks:");
        System.out.println("============================================");
        System.out.println("Block#  Size  Start-End  Status");
        System.out.println("============================================");
        for (int i = 0; i < M; i++) {
            System.out.printf("Block%d  %d KB  %d-%d  %s\n",
                    i, memoryBlocks[i].size, memoryBlocks[i].startAddress,
                    memoryBlocks[i].endAddress, memoryBlocks[i].status);
        }
        System.out.println("============================================");

        while (true) {
            System.out.println("1) Allocates memory blocks");
            System.out.println("2) De-allocates memory blocks");
            System.out.println("3) Print report about the current state of memory and internal Fragmentation");
            System.out.println("4) Exit");
            System.out.println("============================================");
            System.out.print("Enter your choice: ");
            
            try {
                int choice = scanner.nextInt();

                if (choice == 1) {
                    System.out.print("Enter the process ID and size of process: ");
                    String pid = scanner.next();
                    int processSize = scanner.nextInt();

                    int bestFitSize = Integer.MAX_VALUE;
                    int selectedIndex = -1;
                    int maxSize = -1;

                    if (strategy == 1) { // First Fit
                        for (int i = 0; i < M; i++) {
                            if (memoryBlocks[i].status.equals("free") && memoryBlocks[i].size >= processSize) {
                                selectedIndex = i;
                                break;
                            }
                        }
                    } else if (strategy == 2) { // Best Fit
                        for (int i = 0; i < M; i++) {
                            if (memoryBlocks[i].status.equals("free") && memoryBlocks[i].size >= processSize) {
                                if (memoryBlocks[i].size < bestFitSize) {
                                    bestFitSize = memoryBlocks[i].size;
                                    selectedIndex = i;
                                }
                            }
                        }
                    } else if (strategy == 3) { // Worst Fit
                        for (int i = 0; i < M; i++) {
                            if (memoryBlocks[i].status.equals("free") && memoryBlocks[i].size >= processSize) {
                                if (memoryBlocks[i].size > maxSize) {
                                    maxSize = memoryBlocks[i].size;
                                    selectedIndex = i;
                                }
                            }
                        }
                    }

                    if (selectedIndex != -1) {
                        MemoryBlock selectedBlock = memoryBlocks[selectedIndex];
                        selectedBlock.status = "allocated";
                        selectedBlock.processID = pid;
                        selectedBlock.internalFragmentation = selectedBlock.size - processSize;
                        System.out.println(pid + " Allocated at address " + selectedBlock.startAddress +
                                ", and the internal fragmentation is " + selectedBlock.internalFragmentation);
                        System.out.println("============================================");
                    } else {
                        System.out.println("No suitable block found for the process.");
                        System.out.println("============================================");
                    }
                } else if (choice == 2) {
                    System.out.print("Enter the process ID to deallocate: ");
                    String pidToRemove = scanner.next();
                    boolean found = false;

                    for (int i = 0; i < M; i++) {
                        if (memoryBlocks[i].processID.equals(pidToRemove)) {
                            memoryBlocks[i].status = "free";
                            memoryBlocks[i].processID = "Null";
                            memoryBlocks[i].internalFragmentation = 0;
                            found = true;
                            System.out.println("Process " + pidToRemove + " deallocated from Block" + i);
                            System.out.println("============================================");
                            break;
                        }
                    }

                    if (!found) {
                        System.out.println("Process ID not found.");
                        System.out.println("============================================");
                    }
                } else if (choice == 3) {
                    System.out.println("Memory blocks:");
                    System.out.println("==================================================================");
                    System.out.println("Block#  Size  Start-End  Status   ProcessID   InternalFragmentation");
                    System.out.println("==================================================================");

                    
                    for (int i = 0; i < M; i++) {
                        System.out.printf("%-7s %-6s %-11s %-10s %-10s %-5s\n",
                            "Block" + i,
                            memoryBlocks[i].size + "KB",
                            memoryBlocks[i].startAddress + "-" + memoryBlocks[i].endAddress,
                            memoryBlocks[i].status,
                            memoryBlocks[i].processID,
                            memoryBlocks[i].internalFragmentation
                        );

                    }

                    System.out.println("==================================================================");
                } else if (choice == 4) {
                    System.out.println("Exiting...");
                    System.out.println("============================================");
                    break;
                } else {
                    System.out.println("This option is not implemented.");
                    System.out.println("============================================");
                }
            } catch (java.util.InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number between 1 and 4.");
                scanner.nextLine(); // Clear the invalid input
            }
        }
        scanner.close();
    }
}
