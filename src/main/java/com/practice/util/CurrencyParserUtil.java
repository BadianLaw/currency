package com.practice.util;

import javafx.util.Pair;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class CurrencyParserUtil {
    private static final Set<Character> uppercaseLetter = new HashSet<>(
            Arrays.asList('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
                    'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
                    'U', 'V', 'W', 'X', 'Y', 'Z'));

    public static Pair<String, Long> parseInputLine(String line) {
        // check format
        line = line.trim();
        if (Objects.isNull(line) || line.length() < 4) {
            return null;
        }
        String[] split = line.split(" ");
        if (split.length != 2) {
            return null;
        }
        // check currencyCode
        String currencyCode = split[0];
        if (currencyCode.length() != 3) {
            return null;
        }
        for (int i = 0; i < currencyCode.length(); i++) {
            if (!uppercaseLetter.contains(currencyCode.charAt(i))) {
                return null;
            }
        }
        // check value
        String value = split[1];
        Long parseValue = null;
        try {
            parseValue = Long.valueOf(value);
        } catch (NumberFormatException e) {
        }
        if (Objects.isNull(parseValue)) {
            return null;
        }
        return new Pair<>(currencyCode, parseValue);

    }
}
