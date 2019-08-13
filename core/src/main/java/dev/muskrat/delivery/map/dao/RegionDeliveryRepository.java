package dev.muskrat.delivery.map.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface RegionDeliveryRepository extends JpaRepository<RegionDelivery, Long> {

}
