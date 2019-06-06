package dev.muskrat.delivery.dao.shop;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.Embeddable;
import java.time.LocalTime;

@Data
@Embeddable
@RequiredArgsConstructor
public class WorkDay {

    private LocalTime open;
    private LocalTime close;
}
