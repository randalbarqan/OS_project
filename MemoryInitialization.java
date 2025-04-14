
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

public class MemoryInitialization {

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
            int choice = scanner.nextInt();

            if (choice == 1) {
                System.out.print("Enter the process ID and size of process: ");
                String pid = scanner.next();
                int processSize = scanner.nextInt();

                int selectedIndex = -1;
                int maxSize = -1;
                
                //Do first-fit and best-fit in here

                if (strategy == 3) { // Worst Fit
                    for (int i = 0; i < M; i++) {
                        if (memoryBlocks[i].status.equals("free") && memoryBlocks[i].size >= processSize) {
                            if (memoryBlocks[i].size > maxSize) {
                                maxSize = memoryBlocks[i].size;
                                selectedIndex = i;
                            }
                        }
                    }

                    if (selectedIndex != -1) {
                        MemoryBlock selectedBlock = memoryBlocks[selectedIndex];
                        selectedBlock.status = "allocated";
                        selectedBlock.processID = pid;
                        selectedBlock.internalFragmentation = selectedBlock.size - processSize;
                        System.out.printf("%s Allocated at address %d, and the internal fragmentation is %d\n",
                                pid, selectedBlock.startAddress, selectedBlock.internalFragmentation);
                        System.out.println("============================================");
                    } else {
                        System.out.println("No suitable block found for the process.");
                        System.out.println("============================================");
                    }
                } else {
                    System.out.println("This allocation strategy is not implemented yet.");
                    System.out.println("============================================");
                }
            } else if (choice == 4) {
                System.out.println("Exiting...");
                System.out.println("============================================");
                break;
            } else {
                System.out.println("This option is not implemented.");
                System.out.println("============================================");
            }
        }

        scanner.close();
    }
}
