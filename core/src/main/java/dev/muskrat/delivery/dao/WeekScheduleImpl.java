package dev.muskrat.delivery.dao;

import lombok.Builder;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.time.DayOfWeek;
import java.util.Date;
import java.util.Map;

@Entity
@Builder
@Embeddable
public class WeekScheduleImpl implements WeekSchedule {

    private static final String ZONE_ID = "Asia/Kuala_Lumpur";

    @Temporal(TemporalType.TIMESTAMP)
    private Date startMonday;
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTuesday;
    @Temporal(TemporalType.TIMESTAMP)
    private Date startWednesday;
    @Temporal(TemporalType.TIMESTAMP)
    private Date startThursday;
    @Temporal(TemporalType.TIMESTAMP)
    private Date startFriday;
    @Temporal(TemporalType.TIMESTAMP)
    private Date startSaturday;
    @Temporal(TemporalType.TIMESTAMP)
    private Date startSunday;

    @Temporal(TemporalType.TIMESTAMP)
    private Date endMonday;
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTuesday;
    @Temporal(TemporalType.TIMESTAMP)
    private Date endWednesday;
    @Temporal(TemporalType.TIMESTAMP)
    private Date endThursday;
    @Temporal(TemporalType.TIMESTAMP)
    private Date endFriday;
    @Temporal(TemporalType.TIMESTAMP)
    private Date endSaturday;
    @Temporal(TemporalType.TIMESTAMP)
    private Date endSunday;

    @Override
    public boolean isOpen(DayOfWeek day) {
        //LocalDateTime time = LocalDateTime.now(ZoneId.of(ZONE_ID));
        return false;
    }

    @Override
    public boolean isClose(DayOfWeek day) {
        return false;
    }

    @Override
    public Map<DayOfWeek, Date[]> getSchedule() {
        return null;
    }

    private Date[] getScheduleInDay(DayOfWeek day) {
        switch (day) {
            case MONDAY: return new Date[]{startMonday, endMonday};
            case TUESDAY: return new Date[]{startThursday, startThursday};
            case WEDNESDAY: return new Date[]{startWednesday, endWednesday};
            case THURSDAY: return new Date[]{startThursday, endThursday};
            case FRIDAY: return new Date[]{startFriday, endFriday};
            case SATURDAY: return new Date[]{startSaturday, endSaturday};
            case SUNDAY: return new Date[]{startSunday, endSunday};
        }
        return null;
    }
}
