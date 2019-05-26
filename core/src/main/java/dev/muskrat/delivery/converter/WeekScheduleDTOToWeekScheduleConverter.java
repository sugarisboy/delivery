package dev.muskrat.delivery.converter;

import dev.muskrat.delivery.dao.WeekSchedule;
import dev.muskrat.delivery.dao.WeekScheduleImpl;
import dev.muskrat.delivery.dto.shop.WeekScheduleDTO;
import org.springframework.stereotype.Component;

@Component
public class WeekScheduleDTOToWeekScheduleConverter implements ObjectConverter<WeekScheduleDTO, WeekSchedule> {

    @Override
    public WeekSchedule convert(WeekScheduleDTO dto) {
        return WeekScheduleImpl.builder()
                .startWednesday(dto.getStartWednesday())
                .endWednesday(dto.getEndWednesday())
                .startTuesday(dto.getStartTuesday())
                .endTuesday(dto.getEndTuesday())
                .startThursday(dto.getStartThursday())
                .endThursday(dto.getStartThursday())
                .startSunday(dto.getStartSunday())
                .endSunday(dto.getStartSunday())
                .startSaturday(dto.getStartSaturday())
                .endSaturday(dto.getStartSaturday())
                .startMonday(dto.getStartMonday())
                .endMonday(dto.getEndMonday())
                .startFriday(dto.getStartFriday())
                .endFriday(dto.getEndFriday())
                .build();
    }
}
