package org.example.core.models;

import java.time.Period;

/**
 * ������������, �������������� ������� ���������� ��������.
 * �������� ��� ��������: DAILY � WEEKLY
 */
public enum HabitFrequency {
    DAILY("���������", Period.ofDays(1)),
    WEEKLY("�����������", Period.ofWeeks(1));

    private final String str;

    private final Period period;

    /**
     * ����������� ��� �������� ������� {@code HabitFrequency}.
     *
     * @param str    ��������� ������������� ������� ���������� ��������
     * @param period {@link Period}, �������� ������������� ����� ������������ ��������
     */
    HabitFrequency(String str, Period period) {
        this.str = str;
        this.period = period;
    }

    /**
     * ���������� ��������� ������������� �������.
     *
     * @return ��������� ��������
     */
    @Override
    public String toString() {
        return str;
    }

    /**
     * ���������� ������, ��������� � �������� ���������� ��������.
     *
     * @return {@link Period} ������� ���������� ��������
     */
    public Period toPeriod() {
        return period;
    }
}
