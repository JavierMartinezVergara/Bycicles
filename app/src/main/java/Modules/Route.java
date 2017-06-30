package Modules;

/**
 * Created by reyes on 21/05/2017.
 */


import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.nearby.messages.Distance;
import com.google.maps.model.Duration;

import java.util.List;

/**
 * Created by Mai Thanh Hiep on 4/3/2016.
 */
public class Route {
    public Modules.Distance distance;
    public Modules.Duration duration;
    public String endAddress;
    public LatLng endLocation;
    public String startAddress;
    public LatLng startLocation;

    public List<LatLng> points;
}