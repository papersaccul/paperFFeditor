package com.papersaccul.paperffeditor.util;

import com.papersaccul.paperffeditor.model.TaskStatus;
import com.papersaccul.paperffeditor.model.VideoSettings;
//import com.papersaccul.paperffeditor.util.FFmpegCommandBuilder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TaskRunner {

    private VideoSettings videoSettings;
    private TaskStatus taskStatus;

    public TaskRunner(VideoSettings videoSettings, TaskStatus taskStatus) {
        this.videoSettings = videoSettings;
        this.taskStatus = taskStatus;
    }

    public void render(boolean overWrite) {
        String command;

        if (overWrite) {
            command = FFmpegCommandBuilder.buildCommand(videoSettings.getInputFilePath(), videoSettings.getOutputFilePath(), videoSettings) + " -y";
        } else {
            command = FFmpegCommandBuilder.buildCommand(videoSettings.getInputFilePath(), videoSettings.getOutputFilePath(), videoSettings);
        }

        ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
        processBuilder.redirectErrorStream(true);
        try {
            Process process = processBuilder.start();
            new Thread(() -> {
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                double progress = 0.0;
                try {
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                        // I know that progress can be measured by total number of frames 
                        // and by processed frames, but this looks okay too ;D 
                        if (progress < 0.5) {
                            progress += 0.01; // Faster 
                        } else if (progress < 0.8) {
                            progress += 0.005; // Slower 
                        } else {
                            progress += 0.001; // Slowest
                        }
                        taskStatus.setProgress(Math.min(progress, 1.0));
                        taskStatus.setMessage("  " + line +"\n\nProcessing");
                    }
                    process.waitFor();
                    taskStatus.setProgress(1.0);
                    taskStatus.setMessage("Completed");
                } catch (IOException | InterruptedException ioException) {
                    ioException.printStackTrace();
                    taskStatus.setMessage("Error: " + ioException.getMessage());
                }
            }).start();
        } catch (IOException ioException) {
            ioException.printStackTrace();
            taskStatus.setMessage("Error: " + ioException.getMessage());
        }
    }
}