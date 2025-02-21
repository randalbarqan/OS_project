import java.util.Scanner;

public class SRTF {
    static class Process {
        int id, arrivalTime, burstTime, remainingTime, completionTime, waitingTime, turnaroundTime;

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

        int currentTime = 0, completedProcesses = 0;
        Process lastProcess = null;
        int startTime = 0;
        int totalTurnaroundTime = 0, totalWaitingTime = 0;

        System.out.println("\nScheduling Algorithm: Shortest Remaining Time First");
        System.out.println("Context Switch: 1 ms");
        System.out.println("Time\tProcess/CS");

        while (completedProcesses < n) {
            Process selectedProcess = null;
            int minRemainingTime = Integer.MAX_VALUE;

            for (Process p : processes) {
                if (p.arrivalTime <= currentTime && p.remainingTime > 0) {
                    if (p.remainingTime < minRemainingTime) {
                        minRemainingTime = p.remainingTime;
                        selectedProcess = p;
                    }
                }
            }

            if (selectedProcess == null) {
                currentTime++;
                continue;
            }

            if (lastProcess != null && lastProcess != selectedProcess) {
                System.out.println(startTime + "-" + currentTime + "\tP" + lastProcess.id);
                System.out.println(currentTime + "-" + (currentTime + 1) + "\tCS");
                currentTime++;
                startTime = currentTime;
            }

            while (selectedProcess.remainingTime > 0) {
                selectedProcess.remainingTime--;
                currentTime++;

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

                if (nextProcess != null && nextProcess != selectedProcess) {
                    break;
                }
            }

            if (selectedProcess.remainingTime == 0) {
                selectedProcess.completionTime = currentTime;
                selectedProcess.turnaroundTime = selectedProcess.completionTime - selectedProcess.arrivalTime;
                selectedProcess.waitingTime = selectedProcess.turnaroundTime - selectedProcess.burstTime;
                totalTurnaroundTime += selectedProcess.turnaroundTime;
                totalWaitingTime += selectedProcess.waitingTime;
                completedProcesses++;
            }

            lastProcess = selectedProcess;
        }

        System.out.println(startTime + "-" + currentTime + "\tP" + lastProcess.id);
        
        double avgTurnaroundTime = (double) totalTurnaroundTime / n;
        double avgWaitingTime = (double) totalWaitingTime / n;
        double cpuUtilization = ((double) (currentTime - (n - 1))) / currentTime * 100;
        
        System.out.println("\nPerformance Metrics");
        System.out.printf("Average Turnaround Time: %.2f\n", avgTurnaroundTime);
        System.out.printf("Average Waiting Time: %.2f\n", avgWaitingTime);
        System.out.printf("CPU Utilization: %.2f%%\n", cpuUtilization);

        sc.close();
    }
}
