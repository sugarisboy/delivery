package dev.muskrat.delivery.converter;

import dev.muskrat.delivery.dao.shop.WorkDay;
import dev.muskrat.delivery.dto.shop.WorkDayDTO;
import org.springframework.stereotype.Component;

@Component
public class WorkDayDTOToWorkDayConverter implements ObjectConverter<WorkDayDTO, WorkDay> {

    @Override
    public WorkDay convert(WorkDayDTO workDayDTO) {
        WorkDay day = new WorkDay();
        day.setOpen(workDayDTO.getOpen());
        day.setClose(workDayDTO.getClose());
        return day;
    }
}
