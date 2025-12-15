package car.selling.pruebas.base;

public class InsideAreaCalculator {
    
    public static boolean isInsideArea(double lat, double lon, double latMin, double lonMin, double latMax, double lonMax) {
        return lat >= latMin && lat <= latMax && lon >= lonMin && lon <= lonMax;
    }
}
