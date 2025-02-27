import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SRTF_GUI{
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
    private JTextField numProcessesField;
    private JPanel processInputPanel;
    private List<JTextField> arrivalFields;
    private List<JTextField> burstFields;

    public SRTF_GUI() {
        frame = new JFrame("SRTF Scheduling");
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        numProcessesField = new JTextField(5);
        JButton startButton = new JButton("Generate Inputs");
        inputPanel.add(new JLabel("Number of Processes:"));
        inputPanel.add(numProcessesField);
        inputPanel.add(startButton);
        frame.add(inputPanel, BorderLayout.NORTH);

        processInputPanel = new JPanel();
        processInputPanel.setLayout(new GridLayout(0, 3));
        frame.add(new JScrollPane(processInputPanel), BorderLayout.CENTER);

        startButton.addActionListener(e -> generateProcessInputs());

        frame.setVisible(true);
    }

    private void generateProcessInputs() {
        processInputPanel.removeAll();
        arrivalFields = new ArrayList<>();
        burstFields = new ArrayList<>();
        
        int n;
        try {
            n = Integer.parseInt(numProcessesField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Invalid number of processes.");
            return;
        }

        processInputPanel.add(new JLabel("Process"));
        processInputPanel.add(new JLabel("Arrival Time"));
        processInputPanel.add(new JLabel("Burst Time"));

        for (int i = 0; i < n; i++) {
            processInputPanel.add(new JLabel("P" + (i + 1)));
            JTextField arrivalField = new JTextField(5);
            JTextField burstField = new JTextField(5);
            arrivalFields.add(arrivalField);
            burstFields.add(burstField);
            processInputPanel.add(arrivalField);
            processInputPanel.add(burstField);
        }

        JButton scheduleButton = new JButton("Start Scheduling");
        scheduleButton.addActionListener(e -> startScheduling(n));
        processInputPanel.add(scheduleButton);

        processInputPanel.revalidate();
        processInputPanel.repaint();
    }

    private void startScheduling(int n) {
        Process[] processes = new Process[n];

        for (int i = 0; i < n; i++) {
            try {
                int arrivalTime = Integer.parseInt(arrivalFields.get(i).getText());
                int burstTime = Integer.parseInt(burstFields.get(i).getText());
                processes[i] = new Process(i + 1, arrivalTime, burstTime);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Invalid input for process P" + (i + 1));
                return;
            }
        }

        int currentTime = 0, completedProcesses = 0;
        Process lastProcess = null;
        int startTime = 0;
        int totalTurnaroundTime = 0, totalWaitingTime = 0;
        int totalExecutionTime = 0;

        JFrame outputFrame = new JFrame("Scheduling Output");
        outputFrame.setSize(500, 400);
        JTextArea outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputFrame.add(new JScrollPane(outputArea));
        outputFrame.setVisible(true);

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
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SRTF_GUI::new);
    }
}
