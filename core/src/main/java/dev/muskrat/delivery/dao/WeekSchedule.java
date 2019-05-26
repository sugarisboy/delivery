package dev.muskrat.delivery.dao;

import java.time.DayOfWeek;
import java.util.Date;
import java.util.Map;

public interface WeekSchedule {

    boolean isOpen(DayOfWeek day);

    boolean isClose(DayOfWeek day);

    Map<DayOfWeek, Date[]> getSchedule();
}
