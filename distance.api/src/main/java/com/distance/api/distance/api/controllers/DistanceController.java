package com.distance.api.distance.api.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

@RestController
public class DistanceController {

    @GetMapping("/info")
    public Map<String, Object> getInfo() {
        Map<String, Object> response = new HashMap<>();

        Map<String, Double> coordenadasAgencia = new HashMap<>();
        coordenadasAgencia.put("lat", 42.50886738457441);
        coordenadasAgencia.put("lon", 1.5347139324337429);
        response.put("coordenadasAgencia", coordenadasAgencia);

        response.put("radioAdmitidoKm", 5);

        List<Map<String, Object>> zonasRestringidas = new ArrayList<>();

        Map<String, Object> zona1 = new HashMap<>();
        Map<String, Double> noroeste1 = new HashMap<>();
        noroeste1.put("lat", 42.5100061756744);
        noroeste1.put("lon", 1.5366548639320794);
        Map<String, Double> sureste1 = new HashMap<>();
        sureste1.put("lat", 42.50874384583355);
        sureste1.put("lon", 1.5387755676026835);
        zona1.put("noroeste", noroeste1);
        zona1.put("sureste", sureste1);

        Map<String, Object> zona2 = new HashMap<>();
        Map<String, Double> noroeste2 = new HashMap<>();
        noroeste2.put("lat", 42.507647709544536);
        noroeste2.put("lon", 1.5341898505922056);
        Map<String, Double> sureste2 = new HashMap<>();
        sureste2.put("lat", 42.50724930962572);
        sureste2.put("lon", 1.5378015588544913);
        zona2.put("noroeste", noroeste2);
        zona2.put("sureste", sureste2);

        Map<String, Object> zona3 = new HashMap<>();
        Map<String, Double> noroeste3 = new HashMap<>();
        noroeste3.put("lat", 42.5103818437401);
        noroeste3.put("lon", 1.529033233491418);
        Map<String, Double> sureste3 = new HashMap<>();
        sureste3.put("lat", 42.50964884074852);
        sureste3.put("lon", 1.5321785196039148);
        zona3.put("noroeste", noroeste3);
        zona3.put("sureste", sureste3);

        zonasRestringidas.add(zona1);
        zonasRestringidas.add(zona2);
        zonasRestringidas.add(zona3);

        response.put("zonasRestringidas", zonasRestringidas);

        return response;
    }
    
    
}
