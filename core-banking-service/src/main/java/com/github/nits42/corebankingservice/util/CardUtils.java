package com.github.nits42.corebankingservice.util;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class CardUtils {

    private static final SecureRandom random = new SecureRandom();

    // Generates a random 4-digit pin for the ATM card
    public static int generatePIN() {
        // Generates a 4-digit pin
        return 1000 + random.nextInt(9000);
    }

    // Generates a random 3-digit CVV for the ATM card
    public static int generateCVV() {
        // Generates a 3-digit CVV
        return 100 + random.nextInt(900);
    }

    // Choose a random card provider for the ATM card
    public static String generateProvider() {
        HashSet<String> providers = new HashSet<>();
        providers.add("Mastercard");
        providers.add("Visa");
        providers.add("Rupay");

        // Convert HashSet to List
        List<String> list = new ArrayList<>(providers);

        // Create a Random object
        Random random = new Random();

        // Get a random index from the list
        int randomIndex = random.nextInt(list.size());

        // Get the random card provider from the list
        return list.get(randomIndex);
    }

    // Method to generate a 16-digit valid debit card number
    public static Long generateCardNumber() {
        Random random = new Random();

        // Generate first 15 digits randomly
        StringBuilder cardNumber = new StringBuilder();
        for (int i = 0; i < 15; i++) {
            cardNumber.append(random.nextInt(10)); // Add a random digit (0-9)
        }

        // Calculate the 16th digit using the Luhn algorithm
        int checksum = calculateLuhnChecksum(cardNumber.toString());

        // Append the checksum to the card number
        cardNumber.append(checksum);

        return Long.valueOf(cardNumber.toString());
    }

    // Luhn Algorithm to calculate the checksum (last digit)
    private static int calculateLuhnChecksum(String cardNumber) {
        int sum = 0;
        boolean alternate = false;

        // Iterate over the card number in reverse order
        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int digit = Integer.parseInt(cardNumber.substring(i, i + 1));

            if (alternate) {
                digit *= 2;
                if (digit > 9) {
                    digit -= 9;
                }
            }

            sum += digit;
            alternate = !alternate;
        }

        // Return the checksum (mod 10) to make the total divisible by 10
        int checksum = sum % 10;
        return (checksum == 0) ? 0 : 10 - checksum;
    }

}
