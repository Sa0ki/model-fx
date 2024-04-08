package fr.kinan.saad.iaapplication.services;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.TextArea;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ModelLoading {
//    private String inputDirectory;
//    private String outputDirectory;

    public static void run(TextArea logs){
        long startTime = System.nanoTime();

//        System.out.println("Opening Anaconda...");
//
//        String command = "anaconda-navigator";
//        try {
//            Process process = new ProcessBuilder().command("cmd", "/c", command).start();
//            process.waitFor(10, TimeUnit.SECONDS);
//
//            if (process.isAlive())
//                process.destroy();
//
//        } catch (Exception e) {
//            System.out.println("Error in the anaconda process.");
//        }
//        System.out.println("Anaconda is opened !");

        System.out.println("------------------------------------------------------------------------");
        System.out.println("------------------------------------------------------------------------");
        System.out.println("Executing the command...");
        System.out.println("------------------------------------------------------------------------");
        System.out.println("------------------------------------------------------------------------");

        String environmentName = "myenv";
        String cdCommand = "cd C:/Users/Fanny Orlhac/Desktop/kibrom-repo/ai4elife";
        String activateEnvCommand = "activate " + environmentName;
        String pythonCommand = "python test_env.py --input_dir ../../input --output_dir ../../output";
        String finalCommand = cdCommand + " && " + activateEnvCommand + " && " + pythonCommand;

        try {
            ProcessBuilder builder = new ProcessBuilder().command("cmd", "/c", finalCommand);
            builder.redirectErrorStream(true);
            Process process = builder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//            String line;
//
//            while ((line = reader.readLine()) != null){
//                printLogs(process.getInputStream(), logs);
//                System.out.println(line);
//            }

            StringBuilder currentLine = new StringBuilder();
            int character;

            while((character = reader.read()) != -1){

                if(character == '\n'){
                    final String line = currentLine.toString();
                    Platform.runLater(() -> logs.appendText(line + "\n"));
                    currentLine = new StringBuilder();
                }
                else
                    currentLine.append((char) character);
                System.out.print((char)character);
            }

            int exitCode = process.waitFor();

            if (process.isAlive())
                process.destroy();

            if (exitCode != 0)
                System.err.println("Error: The process exited with non-zero exit code " + exitCode);

        } catch (Exception e) {
            System.out.println("Error in environment process: " + e.getMessage());
        }

        System.out.println("------------------------------------------------------------------------");
        System.out.println("------------------------------------------------------------------------");
        System.out.println("Command has been executed.");
        System.out.println("------------------------------------------------------------------------");
        System.out.println("------------------------------------------------------------------------");

        long endTime = System.nanoTime();

        System.out.println("Execution time -> " + ((endTime - startTime) / 1_000_000_000) + " seconds.");
    }

    public static void printLogs(InputStream inputStream, TextArea logs) throws InterruptedException {
        Task<Void> logsTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try{
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null){
                    String finalLine = line;
                    Platform.runLater(() -> logs.appendText(finalLine));
                    System.out.println(line);
                }

            }catch(Exception e){
                System.out.println("Error in the buffer.");
            }
                return null;
            }
        };
        new Thread(logsTask).start();
    }
}
