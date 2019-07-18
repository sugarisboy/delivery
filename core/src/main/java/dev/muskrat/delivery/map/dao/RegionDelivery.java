package dev.muskrat.delivery.map.dao;

import lombok.Data;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Data
@Embeddable
@Table(name = "regions")
public class RegionDelivery {

    @Embedded
    private List<RegionPoint> points;

    public boolean isRegionAvailable(RegionPoint point) {
        int size = points.size();
        double[] pointX = points.stream().mapToDouble(RegionPoint::getX).toArray();
        double[] pointY = points.stream().mapToDouble(RegionPoint::getY).toArray();
        double x = point.getX();
        double y = point.getY();
        boolean c = false;
        
        for (int i = 0, j = size - 1; i < size; j = i++) {
            if ((((pointY[i] <= y) && (y < pointY[j])) || ((pointY[j] <= y) && (y < pointY[i]))) &&
                (pointY[j] - pointY[i] != 0 && x > (pointX[j] - pointX[i]) * (y - pointY[i]) / (pointY[j] - pointY[i]) + pointX[i]))
                c = !c;
        }
        return c;
    }

    public static RegionDelivery getEmpty() {
        RegionDelivery regionDelivery = new RegionDelivery();
        regionDelivery.setPoints(new ArrayList<>());
        return regionDelivery;
    }
}
