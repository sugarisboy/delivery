package dev.muskrat.delivery.dto.shop;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShopPageDTO {

    private List<ShopDTO> shops;
    private Integer currentPage;
    private Integer lastPage;
}
