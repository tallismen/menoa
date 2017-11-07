package nl.anwb.menoa.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Deze class checkt en requestPermissions
 */
public class PermissionsUtil {

    private static final Logger log = LoggerFactory.getLogger("PermissionsUtil");
    private static String[] listPermissionChecked = {
            Manifest.permission.ACCESS_FINE_LOCATION,   // GPS uitlezen toegang
            Manifest.permission.WRITE_EXTERNAL_STORAGE, //Toegang geheugen voor opslaan bestanden
            Manifest.permission.RECORD_AUDIO};          //Toegang microfoon opnemen geluid

    public static boolean checkAndRequestPermissions(Context context) {
        log.info("checkAndRequestPermissions()");
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String permission : listPermissionChecked) {
            log.info("Permissie: " + permission);
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(permission);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions((Activity) context, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 1);
            return false;
        }
        return true;
    }

}
