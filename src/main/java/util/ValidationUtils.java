package util;

import exceptions.ValidationException;
import java.util.regex.Pattern;

public class ValidationUtils {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static void validateNotEmpty(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new ValidationException("Le champ " + fieldName + " est obligatoire.");
        }
    }

    public static void validateEmail(String email) {
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new ValidationException("L'adresse email est invalide.");
        }
    }

    public static void validatePositive(double value, String fieldName) {
        if (value < 0) {
            throw new ValidationException("Le champ " + fieldName + " doit être positif.");
        }
    }
    
    public static void validateMinLength(String value, int min, String fieldName) {
        if (value == null || value.length() < min) {
            throw new ValidationException("Le champ " + fieldName + " doit contenir au moins " + min + " caractères.");
        }
    }
}
