package apis.Manga.API.service;

import java.security.SecureRandom;

public class PasswordGeneratorService {

    public static String generateRandomPassword() {
        String lowercaseLetters = "abcdefghijklmnopqrstuvwxyz";
        String uppercaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String numbers = "1234567890";
        String specialCharacters = "!@#$%^&*()";
        String allCharacters = lowercaseLetters + uppercaseLetters + numbers + specialCharacters;

        int minLength = 12;
        SecureRandom random = new SecureRandom();
        StringBuilder stringBuilder = new StringBuilder(minLength);

        stringBuilder.append(lowercaseLetters.charAt(random.nextInt(lowercaseLetters.length())));
        stringBuilder.append(uppercaseLetters.charAt(random.nextInt(uppercaseLetters.length())));
        stringBuilder.append(numbers.charAt(random.nextInt(numbers.length())));
        stringBuilder.append(specialCharacters.charAt(random.nextInt(specialCharacters.length())));

        for (int i = 4; i < minLength; i++) {
            stringBuilder.append(allCharacters.charAt(random.nextInt(allCharacters.length())));
        }

        char[] shuffledChars = stringBuilder.toString().toCharArray();
        for (int i = 0; i < shuffledChars.length; i++) {
            int randomIndex = random.nextInt(shuffledChars.length);
            char temp = shuffledChars[i];
            shuffledChars[i] = shuffledChars[randomIndex];
            shuffledChars[randomIndex] = temp;
        }

        return String.valueOf(shuffledChars);
    }
}
