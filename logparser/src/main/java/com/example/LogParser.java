package com.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class LogParser {
    public static void main(String[] args) {
        List<String> logs = readLogFile("log.txt");
        Date firstLogTime = null;
        Date lastLogTime = null;
        Map<String, Integer> exceptionHistogram = new HashMap<>();
        List<String> severeLogs = new ArrayList<>();
        Map<Long, List<String>> groupedSevereLogs = new HashMap<>();

        for (String log : logs) {
            
            // Extract log time
            Date logTime = parseLogTime(log);
            if (firstLogTime == null) {
                firstLogTime = logTime;
            }
            lastLogTime = logTime;


            if (log.contains("[SEVERE]")) {
                String contentInsideBrackets = getFirstLineOfMessage(log);
                severeLogs.add(contentInsideBrackets);

                // Group SEVERE logs within 10 seconds of each other
                long timestampSeconds = logTime.getTime() / 1000;
                groupedSevereLogs.computeIfAbsent(timestampSeconds, k -> new ArrayList<>()).add(contentInsideBrackets);
            }

            // Find and analyze Java exceptions
            if (log.contains("Exception") || log.contains("Error")) {
                String exceptionType = getExceptionType(log);
                if (exceptionType != null) {
                    exceptionHistogram.put(exceptionType, exceptionHistogram.getOrDefault(exceptionType, 0) + 1);
                }
            }
        }

        // Output total number of log messages
        System.out.println("Total number of log messages: " + logs.size());

        System.out.println("*****************************************************************");

        // Output first and last log times
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        System.out.println("First log time: " + dateFormat.format(firstLogTime));
        System.out.println("Last log time: " + dateFormat.format(lastLogTime));

        System.out.println("*****************************************************************");

        // Output SEVERE logs grouped within 10 seconds
        System.out.println("\nSEVERE log messages:");
        for (Map.Entry<Long, List<String>> entry : groupedSevereLogs.entrySet()) {
            // System.out.println("Time: " + dateFormat.format(new Date(entry.getKey() * 1000)));
            for (String log : entry.getValue()) {
                System.out.println("Log: " + log);
            }
        }

        System.out.println("*****************************************************************");

        // Output exception histogram
        System.out.println("\nException histogram:");
        for (Map.Entry<String, Integer> entry : exceptionHistogram.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
        displayHistogram(exceptionHistogram);

        System.out.println("*****************************************************************");

        // Generate HTML report
        generateHTMLReport(severeLogs, groupedSevereLogs);
    }

    // Method to parse log time from a log entry
    static Date parseLogTime(String line) {
        String timestamp = line.substring(1, 24);
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            return dateFormat.parse(timestamp);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Method to extract the first line of the message within [[]] brackets using regex
    static String getFirstLineOfMessage(String text) {
        Pattern pattern = Pattern.compile("\\[\\[(.*?)\\]\\]", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            String content = matcher.group(1).trim();
            int indexOfNewLine = content.indexOf('\n');
            if (indexOfNewLine != -1) {
                // Check if there's a second line
                int indexOfSecondNewLine = content.indexOf('\n', indexOfNewLine + 1);
                if (indexOfSecondNewLine != -1) {
                    return content.substring(0, indexOfSecondNewLine);
                } else {
                    return content;
                }
            } else {
                return content;
            }
        } else {
            return null;
        }
    }


    // Method to extract exception type from log entry
    static String getExceptionType(String log) {
        Pattern pattern = Pattern.compile("([\\w.]+Exception):");
        Matcher matcher = pattern.matcher(log);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    // Method to generate ASCII histogram for exception histogram
    private static void displayHistogram(Map<String, Integer> exceptionHistogram) {
        System.out.println("\nHistogram plotting each exception against the number of occurrences:");
    
        // Find maximum value in histogram to determine the height of the chart
        int maxValue = Collections.max(exceptionHistogram.values());
    
        // Iterate from maxValue down to 0 to create each row of the histogram
        for (int i = maxValue; i >= 0; i--) {
            // Print the row number
            System.out.printf("%2d |", i);
    
            // Iterate over each exception type and print 'x' if its count matches the row number
            for (Map.Entry<String, Integer> entry : exceptionHistogram.entrySet()) {
                if (entry.getValue() >= i) {
                    System.out.print("  x  ");
                } else {
                    System.out.print("     ");
                }
            }
            System.out.println();
        }
    
        // Print the x-axis
        System.out.println("---------------------------------------");
    
        // Print the legend
        System.out.print("    ");
        for (Map.Entry<String, Integer> entry : exceptionHistogram.entrySet()) {
            System.out.printf("%2s  ", entry.getKey());
        }
        System.out.println();
    }
    


    // Method to generate HTML report
    static void generateHTMLReport(List<String> severeLogs, Map<Long, List<String>> groupedSevereLogs) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("report.html"))) {
            // Write HTML header
            writer.println("<!DOCTYPE html>");
            writer.println("<html>");
            writer.println("<head>");
            writer.println("<title>Log Report</title>");
            writer.println("</head>");
            writer.println("<body>");

            // Write index with links to each group
            writer.println("<h2>Index</h2>");
            for (long timestamp : groupedSevereLogs.keySet()) {
                writer.println("<a href=\"#group_" + timestamp + "\">Group " + new Date(timestamp * 1000) + "</a><br>");
            }

            // Write grouped SEVERE logs
            for (Map.Entry<Long, List<String>> entry : groupedSevereLogs.entrySet()) {
                writer.println("<h2 id=\"group_" + entry.getKey() + "\">Group " + new Date(entry.getKey() * 1000) + "</h2>");
                writer.println("<ul>");
                for (String log : entry.getValue()) {
                    writer.println("<li>" + log + "</li>");
                }
                writer.println("</ul>");
            }

            // Write closing HTML tags
            writer.println("</body>");
            writer.println("</html>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Method to read log file and return list of log entries
    static List<String> readLogFile(String fileName) {
        List<String> logEntries = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            StringBuilder logEntryBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    if (logEntryBuilder.length() > 0) {
                        logEntries.add(logEntryBuilder.toString());
                        logEntryBuilder.setLength(0); // Clear the StringBuilder
                    }
                } else {
                    logEntryBuilder.append(line).append("\n"); // Append the line to the log entry
                }
            }
            // Add the last log entry if it's not empty
            if (logEntryBuilder.length() > 0) {
                logEntries.add(logEntryBuilder.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error reading log file: " + e.getMessage());
            return null;
        }
        return logEntries;
    }
}