package dev.muskrat.delivery.partner.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartnerStatsDTO {

    List<Map<String, Object>> shopStats = new ArrayList<>();
}
