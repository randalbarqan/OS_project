import java.util.Scanner;

public class SRTF {
    static class Process {
        int id;
        int arrivalTime;
        int burstTime;
        int remainingTime;
        int completionTime;

        Process(int id, int arrivalTime, int burstTime) {
            this.id = id;
            this.arrivalTime = arrivalTime;
            this.burstTime = burstTime;
            this.remainingTime = burstTime;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();
        Process[] processes = new Process[n];

        for (int i = 0; i < n; i++) {
            System.out.print("Enter Arrival Time for P" + (i + 1) + ": ");
            int arrivalTime = sc.nextInt();
            System.out.print("Enter Burst Time for P" + (i + 1) + ": ");
            int burstTime = sc.nextInt();
            processes[i] = new Process(i + 1, arrivalTime, burstTime);
        }

        int currentTime = 0;
        int completedProcesses = 0;
        Process lastProcess = null;
        int startTime = 0; // Track when a process starts running

        System.out.println("\nScheduling Algorithm: Shortest Remaining Time First");
        System.out.println("Context Switch: 1 ms");
        System.out.println("Time Process/CS");

        while (completedProcesses < n) {
            Process selectedProcess = null;
            int minRemainingTime = Integer.MAX_VALUE;

            // Select the process with the shortest remaining time
            for (Process p : processes) {
                if (p.arrivalTime <= currentTime && p.remainingTime > 0) {
                    if (p.remainingTime < minRemainingTime) {
                        minRemainingTime = p.remainingTime;
                        selectedProcess = p;
                    }
                }
            }

            // If no process is available, increment time
            if (selectedProcess == null) {
                currentTime++;
                continue;
            }

            // If switching to a new process, print the previous process execution range
            if (lastProcess != null && lastProcess != selectedProcess) {
                System.out.println(startTime + "-" + currentTime + " P" + lastProcess.id);
                System.out.println(currentTime + "-" + (currentTime + 1) + " CS"); // Print CS
                currentTime++; // Add CS time
                startTime = currentTime; // Reset start time for new process
            }

            // Execute the selected process until it is preempted or finished
            while (selectedProcess.remainingTime > 0) {
                selectedProcess.remainingTime--;
                currentTime++;

                // Check if a different process should take over
                Process nextProcess = null;
                minRemainingTime = Integer.MAX_VALUE;
                for (Process p : processes) {
                    if (p.arrivalTime <= currentTime && p.remainingTime > 0) {
                        if (p.remainingTime < minRemainingTime) {
                            minRemainingTime = p.remainingTime;
                            nextProcess = p;
                        }
                    }
                }

                // If a different process should take over, break the loop
                if (nextProcess != null && nextProcess != selectedProcess) {
                    break;
                }
            }

            // If the process finished execution, mark it as completed
            if (selectedProcess.remainingTime == 0) {
                selectedProcess.completionTime = currentTime;
                completedProcesses++;
            }

            lastProcess = selectedProcess;
        }

        // Print the last process execution
        System.out.println(startTime + "-" + currentTime + " P" + lastProcess.id);

        sc.close();
    }
}
