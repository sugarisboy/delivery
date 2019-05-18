package dev.muskrat.delivery;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

//@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RestController
public class MainContoller {

    @GetMapping("/")
    public String main() {
        return "not used";
    }



}
