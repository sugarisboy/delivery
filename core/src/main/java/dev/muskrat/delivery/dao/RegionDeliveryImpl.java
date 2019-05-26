package dev.muskrat.delivery.dao;

import javax.persistence.Embeddable;

@Embeddable
public class RegionDeliveryImpl implements RegionDelivery {

    @Override
    public boolean getAvailability(String address) {
        return true;
    }
}
