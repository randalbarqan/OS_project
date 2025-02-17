import java.util.Scanner;

public class SRTF {
    static class Process {
        int id;
        int arrivalTime;
        int burstTime;
        int remainingTime;

        Process(int id, int arrivalTime, int burstTime) {
            this.id = id;
            this.arrivalTime = arrivalTime;
            this.burstTime = burstTime;
            this.remainingTime = burstTime;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Input for number of processes
        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();
        Process[] processes = new Process[n];

        // Input arrival and burst times for each process
        for (int i = 0; i < n; i++) {
            System.out.print("Enter Arrival Time for P" + (i + 1) + ": ");
            int arrivalTime = sc.nextInt();
            System.out.print("Enter Burst Time for P" + (i + 1) + ": ");
            int burstTime = sc.nextInt();
            processes[i] = new Process(i + 1, arrivalTime, burstTime);
        }

        // Scheduling logic
        int currentTime = 0;
        int completedProcesses = 0;
        boolean[] isCompleted = new boolean[n];

        System.out.println("Scheduling Algorithm: Shortest remaining time first");
        System.out.println("Context Switch: 1 ms");
        System.out.println("Time Process/CS");

        while (completedProcesses < n) {
            Process selectedProcess = null;
            int minRemainingTime = Integer.MAX_VALUE;

            // Find the process with the shortest remaining time that has arrived
            for (Process p : processes) {
                if (p.arrivalTime <= currentTime && p.remainingTime > 0) {
                    if (p.remainingTime < minRemainingTime) {
                        minRemainingTime = p.remainingTime;
                        selectedProcess = p;
                    }
                }
            }

            // If no process is found, increment time
            if (selectedProcess == null) {
                currentTime++;
                continue;
            }

            // Calculate time slice until either the process completes or a new one arrives
            int timeSlice = 1; // Minimum execution time
            int nextTime = currentTime + timeSlice;

            // Execute the selected process
            System.out.print(currentTime + "-" + nextTime + " P" + selectedProcess.id + "\n");
            selectedProcess.remainingTime--;

            // Check if the process is completed
            if (selectedProcess.remainingTime == 0) {
                isCompleted[selectedProcess.id - 1] = true;
                completedProcesses++;
            }

            currentTime = nextTime;

            // Log context switch if there are remaining processes
            Process nextProcess = null;
            for (Process p : processes) {
                if (p.arrivalTime <= currentTime && p.remainingTime > 0) {
                    if (nextProcess == null || p.remainingTime < nextProcess.remainingTime) {
                        nextProcess = p;
                    }
                }
            }

            if (nextProcess != null && nextProcess != selectedProcess) {
                System.out.print(currentTime + "-" + (currentTime + 1) + " CS\n");
                currentTime++; // Increment for context switch
            }
        }

        sc.close();
    }
}
