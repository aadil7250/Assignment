import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class EmployeeScheduleAnalyzer {

    // Function to calculate the time difference between two datetime strings with
    // the new format
    public static double calculateTimeDifference(String startTime, String endTime) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy hh:mm a");
        try {
            // Check if the date strings are empty before parsing
            if (startTime != null && !startTime.isEmpty() && endTime != null && !endTime.isEmpty()) {
                Date start = dateFormat.parse(startTime);
                Date end = dateFormat.parse(endTime);
                long diffInMillis = end.getTime() - start.getTime();
                return (double) diffInMillis / (60 * 60 * 1000); // Convert milliseconds to hours
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0; // Return 0 if there's an error or empty date strings
    }

    // ...

    // Function to analyze employee schedules
    public static void analyzeEmployeeSchedules(String filePath, String outputPath) {
        Set<String> sevenConsecutiveDays = new HashSet<>();
        Set<String> lessThan10HoursBetweenShifts = new HashSet<>();
        Set<String> moreThan14HoursSingleShift = new HashSet<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy hh:mm a");

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            String[] headers = null;
            String[] previousEntry = null;
            String[] currentEntry;

            while ((line = reader.readLine()) != null) {
                currentEntry = line.split(",");
                if (headers == null) {
                    headers = currentEntry;
                    continue;
                }

                String employeeName = currentEntry[7]; // "Employee Name"
                String timeIn = currentEntry[2]; // "Time"
                String timeOut = currentEntry[3]; // "Time Out"

                // Check if there is less than 10 hours between shifts but greater than 1 hour
                // Check if there is less than 10 hours between shifts but greater than 1 hour
                if (timeOut != null && !timeOut.isEmpty() && previousEntry != null) {
                    double timeDifference = calculateTimeDifference(previousEntry[3], timeIn);
                    if (timeDifference > 1 && timeDifference < 10) {
                        lessThan10HoursBetweenShifts.add(employeeName);
                    }
                }

                // Check if an employee has worked for more than 14 hours in a single shift
                if (timeIn != null && !timeIn.isEmpty() && timeOut != null && !timeOut.isEmpty()) {
                    double timeDifference = calculateTimeDifference(timeIn, timeOut);
                    if (timeDifference > 14) {
                        moreThan14HoursSingleShift.add(employeeName);
                    }
                }

                // Update consecutive days count
                // (You can add code to check for 7 consecutive days here)
                previousEntry = currentEntry;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Output results to the console
        System.out.println("Employees who have worked for 7 consecutive days:");
        for (String employee : sevenConsecutiveDays) {
            System.out.println(employee);
        }

        System.out.println("\nEmployees who have less than 10 hours between shifts but greater than 1 hour:");
        for (String employee : lessThan10HoursBetweenShifts) {
            System.out.println(employee);
        }

        System.out.println("\nEmployees who have worked for more than 14 hours in a single shift:");
        for (String employee : moreThan14HoursSingleShift) {
            System.out.println(employee);
        }

        // Write results to the output file
        try (FileWriter writer = new FileWriter(outputPath)) {
            writer.write("Employees who have worked for 7 consecutive days:\n");
            for (String employee : sevenConsecutiveDays) {
                writer.write(employee + "\n");
            }

            writer.write("\nEmployees who have less than 10 hours between shifts but greater than 1 hour:\n");
            for (String employee : lessThan10HoursBetweenShifts) {
                writer.write(employee + "\n");
            }

            writer.write("\nEmployees who have worked for more than 14 hours in a single shift:\n");
            for (String employee : moreThan14HoursSingleShift) {
                writer.write(employee + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String filePath = "Assignment_Timecard.csv"; // Update with your CSV file name
        String outputFilePath = "output.txt";
        analyzeEmployeeSchedules(filePath, outputFilePath);
    }
}
