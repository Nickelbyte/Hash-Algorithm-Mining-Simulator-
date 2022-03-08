import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.util.InputMismatchException;
import java.math.BigInteger;
import java.util.Scanner;

public class HashAlgo {
    // 4 is a good difficulty level
    // 5 takes a long time to compute
    // 6 - 5825 seconds to compute  
    static int difficulty = 4;
    static int average = 3;
    public static void main(String[] args) {
        getInput();
        int[] timesArray = new int[average];
        int[] numOfTries = new int[average];
        int[] numOfClose = new int[average];
        long totalTime = System.nanoTime();
        for (int times = 0; times < average; times++) {
            long startTime = System.nanoTime();
            String target = targetZeroes(difficulty);
            String closeString = targetZeroes(difficulty - 1);
            boolean found = false;
            BigInteger tries = BigInteger.valueOf(0);
            BigInteger close = BigInteger.valueOf(0);
            while (!found) {
                try {        
                    MessageDigest digest = MessageDigest.getInstance("SHA-256");
                    String input = randomString();
                    byte[] encodedhash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
                    String hash = bytesToHex(encodedhash);
                    System.out.println(hash);
                    tries = tries.add(BigInteger.ONE);
                    if (hash.substring(0, difficulty).equals(target)) {
                        long endTime = System.nanoTime();
                        System.out.println("Found it!");
                        System.out.println("Final hash: " + hash);
                        System.out.println("Input String: " + input);
                        System.out.println(tries + " tries this attempt.");
                        System.out.println("You were close on " + close + " tries.");
                        long time = (endTime - startTime) / 1000000000;
                        System.out.println("It took " + secondsToMinutes(time) + "\n");
                        timesArray[times] = (int) time;
                        numOfTries[times] = tries.intValue();
                        numOfClose[times] = close.intValue();
                        found = true;
                    } else if (hash.substring(0, difficulty - 1).equals(closeString)) {
                        close = close.add(BigInteger.ONE);
                    }
                } catch (NoSuchAlgorithmException e) {
                    System.out.println("Error");
                    found = false;
                }
            }
        }

        if (average > 1) {
            computeAverages(timesArray, numOfTries, numOfClose);
            System.out.println("In total it took " + secondsToMinutes((System.nanoTime() - totalTime) / 1000000000));
        }
    }

    private static void getInput() {
        Scanner input = new Scanner(System.in);
        System.out.println("1 - instant\n2 - very quick\n3 - quick\n4 - ~10 seconds\n5 - A long time\n6 - A very long time");
        System.out.println("Enter a difficulty level: ");
        try {
            difficulty = input.nextInt();
            System.out.println("How many times do you want to run the program? ");
            average = input.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Defaulting to 4.");
            difficulty = 4;
        }
        
    }

    private static void computeAverages(int[] timesArray, int[] numOfTries, int[] numOfClose) {
        int totalTime = 0;
        int totalTries = 0;
        int totalClose = 0;
        for (int i = 0; i < timesArray.length; i++) {
            totalTime += timesArray[i];
            totalTries += numOfTries[i];
            totalClose += numOfClose[i];
        }
        // long timeLong = (long) totalTime;
        System.out.println("Average time: " + totalTime / timesArray.length);
        System.out.println("Average tries: " + totalTries / timesArray.length);
        System.out.println("It took " + totalTries + " total tries.");
        System.out.println("Average close: " + totalClose / timesArray.length);
        System.out.println("It got close " + totalClose + " times.");
        // System.out.println("In total it took " + secondsToMinutes(timeLong));
    }

    private static String targetZeroes(int relativeDifficulty) {
        String target = "";
        for (int i = 1; i <= relativeDifficulty; i++)
            target += "0";

        return target;
    }
    
    private static String secondsToMinutes(Long seconds) {
        int minutes = (int) (seconds / 60);
        int secondsLeft = (int) (seconds % 60);
        if (minutes > 0)
            return minutes + " minute(s) and " + secondsLeft + " seconds.";
        return secondsLeft + " seconds.";
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    private static String randomString() {
        int leftLimit = 32; // numeral '0'
        int rightLimit = 126; // letter 'z'
        int targetStringLength = ((int) (Math.random() * 10)) + 1;
        String randomString = "";

        for (int letterNum = 0; letterNum < targetStringLength; letterNum++) {
            int randomNumber = ((int) (Math.random() * (rightLimit - leftLimit))) + leftLimit;
            char randomChar = (char) randomNumber;
            randomString += randomChar;
        }

        return randomString;
    }
}