package nl.anwb.menoa.util;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.anwb.menoa.model.NoodoproepTelefoon;

/**
 * In deze class staan alle conversies van JSON naar Object
 * of van object naar JSON
 */
public class JsonUtil {

    private static final Logger log = LoggerFactory.getLogger("JsonUtil");

    private JsonUtil() {
    }

    /**
     * Deze methode maakt van een NoodoproepTelefoon een JSONObject
     * klaar om te versturen naar de server.
     *
     * @param noodoproepTelefoon de input
     * @return het JSONObject
     */
    public static JSONObject noodoproepTelefoonToJson(NoodoproepTelefoon noodoproepTelefoon) {
        log.info("noodoproepTelefoonToJson()");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mannr", noodoproepTelefoon.getMannr());
            jsonObject.put("hvnr", noodoproepTelefoon.getHvnr());
            jsonObject.put("lat", noodoproepTelefoon.getLat());
            jsonObject.put("lng", noodoproepTelefoon.getLng());
            jsonObject.put("adres", noodoproepTelefoon.getAdres());

        } catch (JSONException ex) {
            log.error("Convert naar JSON fout: " + ex.toString());
        }
        return jsonObject;
    }

    /**
     * Omzetten van een JSON string naar noodoproepTelefoon als antwoord server
     */
    public static NoodoproepTelefoon noodoproepTelefoonToObject(JSONObject jsonObject) {
        log.info("noodoproepTelefoonToObject()");
        try {
            NoodoproepTelefoon noodoproepTelefoon = new NoodoproepTelefoon();
            noodoproepTelefoon.setMannr(jsonObject.getInt("mannr"));
            noodoproepTelefoon.setHvnr(jsonObject.getInt("hvnr"));
            noodoproepTelefoon.setLat((float) (jsonObject.getDouble("lat")));
            noodoproepTelefoon.setLng((float) (jsonObject.getDouble("lng")));
            noodoproepTelefoon.setAdres(jsonObject.getString("adres"));
            return noodoproepTelefoon;
        } catch (JSONException ex) {
            log.error("Convert naar object fout: " + ex.toString());
        }
        return null;
    }
}
