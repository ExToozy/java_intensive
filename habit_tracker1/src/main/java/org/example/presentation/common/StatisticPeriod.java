package org.example.presentation.common;

public enum StatisticPeriod {
    DAY("Per day"),
    WEEK("Per week"),
    MONTH("Per month"),
    YEAR("Per year");
    private final String str;

    StatisticPeriod(String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return str;
    }
}
