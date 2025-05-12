package com.github.nits42.atmservice.utils;

import com.github.nits42.atmservice.entities.Branch;

import java.security.SecureRandom;
import java.util.List;

public class BranchUtils {

    private static final SecureRandom random = new SecureRandom();

    // Generates a random branch code
    public static String generateBranchCode() {
        // Generate a 6-digit random number
        int branchCode = 100000 + random.nextInt(900000);  // ensures it is 6 digits

        return String.valueOf(branchCode);
    }

    // Method to generate 4 random uppercase letters for the bank code
    private static String generateRandomBankCode() {
        StringBuilder bankCode = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            // Random uppercase letter between 'A' and 'Z'
            bankCode.append((char) ('A' + random.nextInt(26)));
        }
        return bankCode.toString();
    }

    // Method to generate a random IFSC code
    public static String generateIFSCCode(String branchCode) {
        // Step 1: Generate 4 random uppercase letters for the bank code
        String bankCode = generateRandomBankCode();

        // Step 2: The 5th character is always '0'
        char reservedChar = '0';

        // Combine the parts to form the IFSC code
        return "STAR" + reservedChar + branchCode;
    }

    // Choose a random branch code for account
    public static Branch getBranchCode(List<Branch> branchList) {
        // Get a random index from the list
        int randomIndex = random.nextInt(branchList.size());

        // Get the random branch code from the list
        return branchList.get(randomIndex);
    }

}
