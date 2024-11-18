package volunteer__system.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Optional;

import javax.swing.JOptionPane;

public class InputRequester {
    private final static String DEFAULT_INVALID_INPUT_MESSAGE = "Entrada inválida. Inténtalo de nuevo";
    private final static String DEFAULT_INVALID_INPUT_PANE_TITLE = "Entrada inválida";

    // String
    public static String requestString(String prompt) {
        return requestString(prompt, DEFAULT_INVALID_INPUT_MESSAGE, false);
    }

    public static String requestString(String prompt, boolean allowEmpty) {
        return requestString(prompt, DEFAULT_INVALID_INPUT_MESSAGE, allowEmpty);
    }

    public static String requestString(String prompt, String invalidInputMessage, boolean allowEmpty) {
        while (true) {
            String inputString = JOptionPane.showInputDialog(null, prompt);
            String trimmedInput = inputString == null ? "" : inputString.trim();
            if (allowEmpty)
                return trimmedInput;

            if (!trimmedInput.isEmpty())
                return trimmedInput;

            JOptionPane.showMessageDialog(null, invalidInputMessage, DEFAULT_INVALID_INPUT_PANE_TITLE,
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    // LocalDate

    public static Date requestDate(String message) {
        while (true) {
            try {
                String input = JOptionPane.showInputDialog(null, message);
                if (input == null) { // Si el usuario presiona cancelar
                    return null;
                }

                // Parseamos el LocalDate usando el formato especificado
                LocalDate localDate = LocalDate.parse(input, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                // Convertimos LocalDate a Date
                return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(null, "Invalid date format. Please use yyyy-MM-dd.");
            }
        }
    }

    public static Optional<LocalDate> requestLocalDate(String prompt, boolean allowEmpty) {
        return requestLocalDate(prompt, "Formato de fecha inválido. Inténtalo de nuevo", allowEmpty);
    }

    public static Optional<LocalDate> requestLocalDate(String prompt, String invalidInputMessage) {
        return requestLocalDate(prompt, invalidInputMessage, false);
    }

    public static Optional<LocalDate> requestLocalDate(String prompt, String invalidInputMessage, boolean allowEmpty) {
        while (true) {
            String inputLocalDate = requestString(prompt + "\nFormat: YYYY-MM-DD", allowEmpty);
            if (allowEmpty && inputLocalDate.isEmpty())
                return Optional.empty();

            var localDate = DateStringParser.parseLocalDate(inputLocalDate);
            if (localDate.isPresent())
                return localDate;
            JOptionPane.showMessageDialog(null, invalidInputMessage, DEFAULT_INVALID_INPUT_PANE_TITLE,
                    JOptionPane.WARNING_MESSAGE);
        }

    }

    // LocalDateTime
    public static Optional<LocalDateTime> requestLocalDateTime(String prompt, String invalidInputMessage) {
        return requestLocalDateTime(prompt, invalidInputMessage, false);
    }

    public static Optional<LocalDateTime> requestLocalDateTime(String prompt, boolean allowEmpty) {
        return requestLocalDateTime(prompt, "Formato de fecha y hora inválido. Inténtalo de nuevo", allowEmpty);
    }

    public static Optional<LocalDateTime> requestLocalDateTime(String prompt) {
        return requestLocalDateTime(prompt, "Formato de fecha y hora inválido. Inténtalo de nuevo", false);
    }

    public static Optional<LocalDateTime> requestLocalDateTime(String prompt, String invalidInputMessage,
            boolean allowEmpty) {
        while (true) {
            String inputLocalDateTime = requestString(prompt + "\nFormat: YYYY-MM-DDThh:mm:ss", allowEmpty);
            if (allowEmpty && inputLocalDateTime.isEmpty())
                return Optional.empty();

            var localDateTime = DateStringParser.parseLocalDateTime(inputLocalDateTime);
            if (localDateTime.isPresent())
                return localDateTime;
            JOptionPane.showMessageDialog(null, invalidInputMessage, DEFAULT_INVALID_INPUT_PANE_TITLE,
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    // LocalTime
    public static Optional<LocalTime> requestLocalTime(String prompt, String invalidInputMessage) {
        return requestLocalTime(prompt, invalidInputMessage, false);
    }

    public static Optional<LocalTime> requestLocalTime(String prompt, boolean allowEmpty) {
        return requestLocalTime(prompt, "Formato de hora inválido. Inténtalo de nuevo", allowEmpty);
    }

    public static Optional<LocalTime> requestLocalTime(String prompt) {
        return requestLocalTime(prompt, "Formato de hora inválido. Inténtalo de nuevo", false);
    }

    public static Optional<LocalTime> requestLocalTime(String prompt, String invalidInputMessage, boolean allowEmpty) {
        while (true) {
            String inputLocalTime = requestString(prompt + "\nFormat: hh:mm[:ss]", allowEmpty);
            if (allowEmpty && inputLocalTime.isEmpty())
                return Optional.empty();

            var localTime = TimeStringParser.parseLocalTime(inputLocalTime);
            if (localTime.isPresent())
                return localTime;
            JOptionPane.showMessageDialog(null, invalidInputMessage, DEFAULT_INVALID_INPUT_PANE_TITLE,
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    // Integer
    public static int requestInteger(String prompt, String invalidInputMessage, boolean allowEmpty, int defaultValue) {
        while (true) {
            String inputInteger = requestString(prompt, allowEmpty);
            if (allowEmpty && (inputInteger == null || inputInteger.isEmpty())) {
                return defaultValue; // Devuelve el valor por defecto si se permite vacío
            }

            try {
                return Integer.parseInt(inputInteger); // Convierte y retorna el valor ingresado
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, invalidInputMessage, "Invalid Input",
                        JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    public static int requestInteger(String prompt, String invalidInputMessage, int defaultValue) {
        return requestInteger(prompt, invalidInputMessage, false, defaultValue);
    }

    public static int requestInteger(String prompt, int defaultValue) {
        return requestInteger(prompt, "La entrada no es un entero. Inténtalo de nuevo", false, defaultValue);
    }

    public static int requestInteger(String prompt) {
        return requestInteger(prompt, "La entrada no es un entero. Inténtalo de nuevo", false, 0);
    }

    // Double
    public static Optional<Double> requestDouble(String prompt, String invalidInputMessage) {
        return requestDouble(prompt, invalidInputMessage, false);
    }

    public static Optional<Double> requestDouble(String prompt, boolean allowEmpty) {
        return requestDouble(prompt, "La entrada no es un número válido. Inténtalo de nuevo", allowEmpty);
    }

    public static Optional<Double> requestDouble(String prompt) {
        return requestDouble(prompt, "La entrada no es un número válido. Inténtalo de nuevo", false);
    }

    public static Optional<Double> requestDouble(String prompt, String invalidInputMessage, boolean allowEmpty) {
        while (true) {
            String inputDouble = requestString(prompt, allowEmpty);
            if (allowEmpty && inputDouble.isEmpty())
                return Optional.empty();

            try {
                return Optional.of(Double.parseDouble(inputDouble));
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, invalidInputMessage, DEFAULT_INVALID_INPUT_PANE_TITLE,
                        JOptionPane.WARNING_MESSAGE);
            }
        }
    }

}
