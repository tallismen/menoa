package nl.anwb.menoa.util;


import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

public class GeoCoderUtil {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger("GeoCoderUtil");

    private GeoCoderUtil(){}

    public static String getAddressString(Location location, Context context){
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        String address = "";
        try {
            List< Address > addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses != null && addresses.size() > 0) {
                address = addresses.get(0).getAddressLine(0);
                if (address != null) {
                    log.info("Addres: "+address);
                } else {
                    log.info("Addres: Not Available");
                }
            }
        } catch (IOException e) {
            log.error("Fout in ophalen adres: "+e.getMessage());
        }
        return address;
    }


}
