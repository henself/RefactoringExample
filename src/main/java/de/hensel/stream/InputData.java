package de.hensel.stream;

import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
public class InputData {
    String name;
    Amount amount;

    public InputData(String name, double amount) {
        this.name = name;
        this.amount = new Amount(amount);
    }

    @RequiredArgsConstructor
    public static class Amount {
        private final double defaultCurrency;

        public double inDefaultCurrency() {
            return defaultCurrency;
        }
    }
}
