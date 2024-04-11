package fr.kinan.saad.services;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ModelLoading {
    public static void run(String inputDir, String outputDir){
        long startTime = System.nanoTime();

        System.out.println("------------------------------------------------------------------------");
        System.out.println("------------------------------------------------------------------------");
        System.out.println("Executing the command...");
        System.out.println("------------------------------------------------------------------------");
        System.out.println("------------------------------------------------------------------------");

        String finalCommand = "python test_env.py --input_dir " + inputDir + " --output_dir " + outputDir;

        try {
            ProcessBuilder builder = new ProcessBuilder().command("cmd", "/c", finalCommand);
            builder.redirectErrorStream(true);
            Process process = builder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            int character;
            while((character = reader.read()) != -1)
                System.out.print((char)character);

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
}
