package nl.anwb.menoa.comm;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import nl.anwb.menoa.R;
import nl.anwb.menoa.model.NoodoproepTelefoon;
import nl.anwb.menoa.util.GeoCoderUtil;
import nl.anwb.menoa.util.JsonUtil;
import nl.anwb.menoa.util.ToastUtil;
import nl.anwb.menoa.util.VibratorUtil;

/**
 * In deze class staat alle code voor de communicatie met de server.
 */
public class MenosComm {

    private static final Logger log = LoggerFactory.getLogger("MenosComm");
    private final Context context;    //Nodig voor preference
    private final RequestQueue requestQueue;    //De request queue van volley voor de uitgaande berichten

    private boolean sendSucces; //Of de noodmelding al sendSucces is
    //private static final String url = "http://10.34.1.240:8080/noodMelding";//Url voor de server

    public MenosComm(Context context) {
        log.info("MenosComm initialiseren...");
        this.context = context;
        requestQueue = Volley.newRequestQueue(context);
    }

    /**
     * Deze methode verstuurd met behulp van Volley een HTTP:POST naar de server.
     *
     * @param lastKnownLocation de laatst bekende locatie wordt meegegeven
     * @return boolean of het bericht sendSucces is
     */
    public boolean noodmeldingNaarServer(Location lastKnownLocation, String url,int hvnr,int mannr) {
        log.info("Noodmelding verzenden...");
        sendSucces = true;
        NoodoproepTelefoon noodoproepTelefoon = new NoodoproepTelefoon(
                mannr,
                hvnr,
                lastKnownLocation.getLatitude(),
                lastKnownLocation.getLongitude(),
                GeoCoderUtil.getAddressString(lastKnownLocation, context));

        JSONObject jsonObject = JsonUtil.noodoproepTelefoonToJson(noodoproepTelefoon);
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST, url, jsonObject,
                response -> {
                    if (response != null) {
                        log.info("Noodmelding ontvangen: " + response);
                        NoodoproepTelefoon noodoproepTelefoon1 = JsonUtil.noodoproepTelefoonToObject(response);
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle(context.getString(R.string.titel_locatie_noodoproep));
                        builder.setPositiveButton(context.getString(R.string.positive_button_noodoproep_locatie), (dialog, which) -> dialog.cancel());
                        builder.setIcon(R.mipmap.sirenerood);
                        assert noodoproepTelefoon1 != null;
                        builder.setMessage(noodoproepTelefoon1.toString());
                        builder.show(); //TODO: Dit nog verplaatsen naar AlertDialog manager
                    }
                    VibratorUtil.vibratePhoneSOS(context);
                    sendSucces = true;
                },
                error -> {
                    log.error("Noodmelding mislukt! : " + error);
                    ToastUtil.displayToast(context.getString(R.string.toast_noodmelding_mislukt), Toast.LENGTH_SHORT, context);
                    VibratorUtil.vibratePhone(context, 5000);
                    sendSucces = false;
                });
        requestQueue.add(request);
        log.info("POST request toevoegen aan Queue");
        return sendSucces;
    }
}
