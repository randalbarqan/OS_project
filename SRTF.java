import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

    private JFrame frame;
    private JTextArea outputArea;
    private JTextField numProcessesField;
    private JButton startButton;

    public SRTF() {
        frame = new JFrame("SRTF Scheduling");
        frame.setSize(600, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());
        numProcessesField = new JTextField(5);
        startButton = new JButton("Start Scheduling");
        inputPanel.add(new JLabel("Number of Processes:"));
        inputPanel.add(numProcessesField);
        inputPanel.add(startButton);
        frame.add(inputPanel, BorderLayout.NORTH);

        outputArea = new JTextArea(20, 50);
        outputArea.setEditable(false);
        JScrollPane outputScroll = new JScrollPane(outputArea);
        frame.add(outputScroll, BorderLayout.CENTER);

        startButton.addActionListener(e -> startScheduling());

        frame.setVisible(true);
    }

    private void startScheduling() {
        outputArea.setText("");
        int n;
        try {
            n = Integer.parseInt(numProcessesField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Invalid number of processes.");
            return;
        }

        Scanner sc = new Scanner(System.in);
        Process[] processes = new Process[n];

        for (int i = 0; i < n; i++) {
            int arrivalTime = Integer.parseInt(JOptionPane.showInputDialog("Enter Arrival Time for P" + (i + 1) + ":"));
            int burstTime = Integer.parseInt(JOptionPane.showInputDialog("Enter Burst Time for P" + (i + 1) + ":"));
            processes[i] = new Process(i + 1, arrivalTime, burstTime);
        }

        int currentTime = 0, completedProcesses = 0;
        Process lastProcess = null;
        int startTime = 0;
        int totalTurnaroundTime = 0, totalWaitingTime = 0;
        int totalExecutionTime = 0;

        outputArea.append("\nScheduling Algorithm: Shortest Remaining Time First\n");
        outputArea.append("Context Switch: 1 ms\n");
        outputArea.append("Time\tProcess/CS\n");

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
                outputArea.append(startTime + "-" + currentTime + "\tP" + lastProcess.id + "\n");
                outputArea.append(currentTime + "-" + (currentTime + 1) + "\tCS\n");
                currentTime++;
                startTime = currentTime;
            }

            while (selectedProcess.remainingTime > 0) {
                selectedProcess.remainingTime--;
                currentTime++;
                totalExecutionTime++;

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

        outputArea.append(startTime + "-" + currentTime + "\tP" + lastProcess.id + "\n");

        double avgTurnaroundTime = (double) totalTurnaroundTime / n;
        double avgWaitingTime = (double) totalWaitingTime / n;
        double cpuUtilization = ((double) totalExecutionTime / currentTime) * 100;

        outputArea.append("\nPerformance Metrics\n");
        outputArea.append("Average Turnaround Time: " + avgTurnaroundTime + "\n");
        outputArea.append("Average Waiting Time: " + avgWaitingTime + "\n");
        outputArea.append("CPU Utilization: " + cpuUtilization + "%\n");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SRTF::new);
    }
}

