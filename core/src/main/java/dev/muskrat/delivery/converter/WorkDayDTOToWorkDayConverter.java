/*

package dev.muskrat.delivery.converter;

import dev.muskrat.delivery.dao.shop.WorkDay;
import dev.muskrat.delivery.dto.shop.ShopScheduleUpdateDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WorkDayDTOToWorkDayConverter implements ObjectConverter<ShopScheduleUpdateDTO, List<WorkDay>> {

    @Override
    public WorkDay convert(WorkDayDTO workDayDTO) {
        WorkDay day = new WorkDay();
        day.setOpen(workDayDTO.getOpen());
        day.setClose(workDayDTO.getClose());
        return day;
    }

    @Override
    public List<WorkDay> convert(ShopScheduleUpdateDTO shopScheduleUpdateDTO) {
        return null;
    }
}

*/
