package dev.muskrat.delivery.dto.shop;

import lombok.Data;

import javax.persistence.Entity;
import java.util.Date;

@Data
@Entity
public class WeekScheduleDTO {

    /*
        TODO Обсудить и решить как лучше реализовать
        Долго думал как реализовать это говно
        мне кажется такой вариант говно но
        ничего лучше я не смог придумать для простоты хранения
        как вариант двумерный массив Date[Day][0-start|1-end]
        но я хз если честно.
     */

    private Date startMonday;
    private Date startTuesday;
    private Date startWednesday;
    private Date startThursday;
    private Date startFriday;
    private Date startSaturday;
    private Date startSunday;

    private Date endMonday;
    private Date endTuesday;
    private Date endWednesday;
    private Date endThursday;
    private Date endFriday;
    private Date endSaturday;
    private Date endSunday;
}
