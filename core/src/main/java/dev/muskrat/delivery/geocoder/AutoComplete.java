package dev.muskrat.delivery.geocoder;

import dev.muskrat.delivery.dto.mapping.AutoCompleteDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AutoComplete {

    private final int MAX_RESULT = 5;
    private final String APP_ID = "PwhAzeVFHuSMGdcjtFvQ";
    private final String APP_CODE = "yE6QWws10hfiJKPyLE-hIQ";
    private final String COUNTRY = "MYS";
    private final String URL_TEMPLATE = "http://autocomplete.geocoder.api.here.com/6.2/" +
        "suggest.json?" +
        "app_id=[APP_ID]" +
        "&app_code=[APP_CODE]" +
        "&query=[FIELD]" +
        "&country=[COUNTRY]" +
        "&maxresults=[MAX_RESULT]";

    public AutoCompleteDTO complete(String label) {
        RestTemplate restTemplate = new RestTemplate();
        AutoCompleteDTO dto = restTemplate.getForObject(url(label), AutoCompleteDTO.class);
        System.out.println();
        return dto;
    }

    private String url(String label) {
        return URL_TEMPLATE
            .replace("[APP_ID]", APP_ID)
            .replace("[APP_CODE]", APP_CODE)
            .replace("[COUNTRY]", COUNTRY)
            .replace("[MAX_RESULT]", MAX_RESULT + "")
            .replace("[FIELD]", label);
    }
}
