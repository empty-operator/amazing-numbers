package numbers;

import java.util.*;

public class Main {

    private static final List<String> availableProperties = Arrays.asList("EVEN", "ODD", "BUZZ", "DUCK", "PALINDROMIC", "GAPFUL",
            "SPY", "SUNNY", "SQUARE", "JUMPING", "HAPPY", "SAD");
    public static final String LS = System.lineSeparator();
    public static final String NUMBER_FORMAT = "%20s: %b%n";
    public static final String MUTUALLY_EXCLUSIVE_PROPERTIES = LS + "The request contains mutually exclusive properties: ";

    public static void main(String[] args) {
        System.out.println(LS + "Welcome to Amazing Numbers!");
        processUserRequests();
        System.out.println(LS + "Goodbye!");
    }

    public static void printSupportedRequests() {
        System.out.println(LS + "Supported requests:");
        System.out.println("- enter a natural number to know its properties;");
        System.out.println("- enter two natural numbers to obtain the properties of the list:");
        System.out.println("  * the first parameter represents a starting number;");
        System.out.println("  * the second parameter shows how many consecutive numbers are to be printed;");
        System.out.println("- two natural numbers and properties to search for;");
        System.out.println("- a property preceded by minus must not be present in numbers;");
        System.out.println("- separate the parameters with one space;");
        System.out.println("- enter 0 to exit.");
    }

    public static void processUserRequests() {
        printSupportedRequests();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print(LS + "Enter a request: ");
            try (Scanner request = new Scanner(scanner.nextLine())) {
                if (request.hasNextLong()) {
                    long number = request.nextLong();
                    if (request.hasNextInt()) {
                        int counter = request.nextInt();
                        if (request.hasNext())
                            searchForNumbersWithProperties(number, counter, request.nextLine().trim().split(" "));
                        else
                            processListOfNumbers(number, counter);
                    } else if (number != 0)
                        processNumber(number);
                    else
                        return;
                } else
                    printSupportedRequests();
            }
        }
    }

    public static void searchForNumbersWithProperties(long number, int counter, String[] properties) {
        for (int i = 0; i < properties.length; i++)
            properties[i] = properties[i].toUpperCase();
        if (areValidProperties(properties)) {
            System.out.println();
            for (int i = 0; i < counter; number++) {
                if (hasProperties(number, properties)) {
                    printProperties(number);
                    i++;
                }
            }
        }
    }

    public static boolean hasProperties(long number, String[] properties) {
        for (String property : properties) {
            if (property.startsWith("-")) {
                if (hasProperty(number, property.substring(1)))
                    return false;
            } else if (!hasProperty(number, property))
                return false;
        }
        return true;
    }

    public static boolean hasProperty(long number, String property) {
        return switch (property) {
            case "EVEN" -> (number & 1) == 0;
            case "ODD" -> (number & 1) != 0;
            case "BUZZ" -> isBuzz(number);
            case "DUCK" -> isDuck(number);
            case "PALINDROMIC" -> isPalindromic(number);
            case "GAPFUL" -> isGapful(number);
            case "SPY" -> isSpy(number);
            case "SUNNY" -> isSunny(number);
            case "SQUARE" -> isSquare(number);
            case "JUMPING" -> isJumping(number);
            case "HAPPY" -> isHappy(number);
            case "SAD" -> !isHappy(number);
            default -> false;
        };
    }

    public static boolean areValidProperties(String[] properties) {
        List<String> invalidProperties = new ArrayList<>();
        for (String property : properties)
            if (!isAvailableProperty(property))
                invalidProperties.add(property);
        if (!invalidProperties.isEmpty()) {
            if (invalidProperties.size() == 1)
                System.out.printf(LS + "The property [%s] is wrong" + LS, invalidProperties.get(0));
            else
                System.out.printf(LS + "The properties [%s] are wrong" + LS, String.join(", ", invalidProperties));
            System.out.printf("Available properties: [%s]" + LS, String.join(", ", availableProperties));
            return false;
        }
        if (hasExclusiveProperties(Arrays.asList(properties))) {
            System.out.println("There are no numbers with these properties.");
            return false;
        }
        return true;
    }

    private static boolean isAvailableProperty(String property) {
        return property.startsWith("-") ? availableProperties.contains(property.substring(1))
                                        : availableProperties.contains(property);
    }

    public static boolean hasExclusiveProperties(List<String> properties) {
        for (String property : properties) {
            if (properties.contains(property) && properties.contains("-" + property)) {
                System.out.printf(MUTUALLY_EXCLUSIVE_PROPERTIES + "[%s, %s]" + LS, property, "-" + property);
                return true;
            }
        }
        if (properties.contains("EVEN") && properties.contains("ODD")) {
            System.out.println(MUTUALLY_EXCLUSIVE_PROPERTIES + "[EVEN, ODD]");
            return true;
        } else if (properties.contains("DUCK") && properties.contains("SPY")) {
            System.out.println(MUTUALLY_EXCLUSIVE_PROPERTIES + "[DUCK, SPY]");
            return true;
        } else if (properties.contains("SUNNY") && properties.contains("SQUARE")) {
            System.out.println(MUTUALLY_EXCLUSIVE_PROPERTIES + "[SUNNY, SQUARE]");
            return true;
        } else if (properties.contains("HAPPY") && properties.contains("SAD")) {
            System.out.println(MUTUALLY_EXCLUSIVE_PROPERTIES + "[HAPPY, SAD]");
            return true;
        } else if (properties.contains("-EVEN") && properties.contains("-ODD")) {
            System.out.println(MUTUALLY_EXCLUSIVE_PROPERTIES + "[-EVEN, -ODD]");
            return true;
        } else if (properties.contains("-DUCK") && properties.contains("-SPY")) {
            System.out.println(MUTUALLY_EXCLUSIVE_PROPERTIES + "[-DUCK, -SPY]");
            return true;
        } else if (properties.contains("-SUNNY") && properties.contains("-SQUARE")) {
            System.out.println(MUTUALLY_EXCLUSIVE_PROPERTIES + "[-SUNNY, -SQUARE]");
            return true;
        } else if (properties.contains("-HAPPY") && properties.contains("-SAD")) {
            System.out.println(MUTUALLY_EXCLUSIVE_PROPERTIES + "[-HAPPY, -SAD]");
            return true;
        }
        return false;
    }

    public static void processNumber(long number) {
        if (number >= 0) {
            System.out.println(LS + "Properties of " + number + LS);
            boolean isEven = (number & 1) == 0;
            boolean isHappy = isHappy(number);
            System.out.printf(NUMBER_FORMAT, "even", isEven);
            System.out.printf(NUMBER_FORMAT, "odd", !isEven);
            System.out.printf(NUMBER_FORMAT, "buzz", isBuzz(number));
            System.out.printf(NUMBER_FORMAT, "duck", isDuck(number));
            System.out.printf(NUMBER_FORMAT, "palindromic", isPalindromic(number));
            System.out.printf(NUMBER_FORMAT, "gapful", isGapful(number));
            System.out.printf(NUMBER_FORMAT, "spy", isSpy(number));
            System.out.printf(NUMBER_FORMAT, "sunny", isSunny(number));
            System.out.printf(NUMBER_FORMAT, "square", isSquare(number));
            System.out.printf(NUMBER_FORMAT, "jumping", isJumping(number));
            System.out.printf(NUMBER_FORMAT, "happy", isHappy);
            System.out.printf(NUMBER_FORMAT, "sad", !isHappy);
        } else
            System.out.println(LS + "The first parameter should be a natural number or zero.");
    }

    public static void processListOfNumbers(long number, int counter) {
        if (number < 0)
            System.out.println(LS + "The first parameter should be a natural number or zero.");
        else if (counter <= 0)
            System.out.println(LS + "The second parameter should be a natural number.");
        else {
            System.out.println();
            for (int i = 0; i < counter; i++)
                printProperties(number + i);
        }
    }

    public static void printProperties(long number) {
        boolean isEven = (number & 1) == 0;
        StringJoiner properties = new StringJoiner(", ", number + " is ", "");
        if (isEven)
            properties.add("even");
        else
            properties.add("odd");
        if (isBuzz(number))
            properties.add("buzz");
        if (isDuck(number))
            properties.add("duck");
        if (isPalindromic(number))
            properties.add("palindromic");
        if (isGapful(number))
            properties.add("gapful");
        if (isSpy(number))
            properties.add("spy");
        if (isSunny(number))
            properties.add("sunny");
        if (isSquare(number))
            properties.add("square");
        if (isJumping(number))
            properties.add("jumping");
        if (isHappy(number))
            properties.add("happy");
        else
            properties.add("sad");
        System.out.println(properties);
    }

    public static boolean isBuzz(long number) {
        return number % 10 == 7 || number % 7 == 0;
    }

    public static boolean isDuck(long number) {
        if (number == 0)
            return false;
        if (number % 10 == 0)
            return true;
        return isDuck(number / 10);
    }

    public static boolean isPalindromic(long number) {
        char[] digits = String.valueOf(number).toCharArray();
        for (int i = 0, j = digits.length - 1; i <= j; i++, j--)
            if (digits[i] != digits[j])
                return false;
        return true;
    }

    public static boolean isGapful(long number) {
        if (number / 100 == 0)
            return false;
        long firstDigit = number;
        long lastDigit = number % 10;
        while (firstDigit >= 10)
            firstDigit /= 10;
        return number % (firstDigit * 10 + lastDigit) == 0;
    }

    public static boolean isSpy(long number) {
        int sumOfDigits = 0;
        int productOfDigits = 1;
        while (number > 0) {
            int digit = (int) (number % 10);
            sumOfDigits += digit;
            productOfDigits *= digit;
            number /= 10;
        }
        return sumOfDigits == productOfDigits;
    }

    public static boolean isSunny(long number) {
        return isSquare(number + 1);
    }

    public static boolean isSquare(long number) {
        return (long) Math.sqrt(number) * (long) Math.sqrt(number) == number;
    }

    public static boolean isJumping(long number) {
        if (number < 10)
            return true;
        if (Math.abs(number % 10 - number / 10 % 10) != 1)
            return false;
        return isJumping(number / 10);
    }

    public static boolean isHappy(long number) {
        if (number >= 10)
            return isHappy(sumOfDigitSquares(number));
        return number == 1 || number == 7;
    }

    public static long sumOfDigitSquares(long number) {
        int sumOfDigitSquares = 0;
        while (number > 0) {
            int digit = (int) (number % 10);
            sumOfDigitSquares += digit * digit;
            number /= 10;
        }
        return sumOfDigitSquares;
    }

}
