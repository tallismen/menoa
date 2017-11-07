package nl.anwb.menoa.manager;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import nl.anwb.menoa.model.NoodoproepTelefoon;
import nl.anwb.menoa.util.JsonUtil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * JsonUtilTest test de JsonUtil class die van object naar JSON en van JSON naar object convert.
 */
public class JsonUtilTest {

    private NoodoproepTelefoon noodoproepTelefoon;
    private JSONObject jsonObject;

    @Before
    public void setUp() throws Exception {
        noodoproepTelefoon = new NoodoproepTelefoon();
        noodoproepTelefoon.setHvnr(285143);
        noodoproepTelefoon.setAdres("Den Haag");
        noodoproepTelefoon.setlat((float) 1.2345);
        noodoproepTelefoon.setlng((float) 1.2345);
        noodoproepTelefoon.setmannr(15);
    }

    @Test
    public void noodoproepTelefoonToJsonTest() throws Exception {
        jsonObject = JsonUtil.noodoproepTelefoonToJson(noodoproepTelefoon);
        assertNotNull("Converten naar JSON niet geluk!", jsonObject);
        assertEquals(noodoproepTelefoon.getHvnr(), jsonObject.getInt("hvnr"));
        assertEquals(noodoproepTelefoon.getmannr(), jsonObject.getInt("mannr"));
        assertEquals(noodoproepTelefoon.getAdres(), jsonObject.getString("adres"));
    }

    @Test
    public void noodoproepTelefoonToObjectTest() throws Exception {
        jsonObject = JsonUtil.noodoproepTelefoonToJson(noodoproepTelefoon);
        NoodoproepTelefoon noodoproepTelefoon1 = JsonUtil.noodoproepTelefoonToObject(jsonObject);
        assertNotNull("Converten van JSON niet gelukt!", noodoproepTelefoon1);
        assertEquals(noodoproepTelefoon.getAdres(), noodoproepTelefoon1.getAdres());
        assertEquals(noodoproepTelefoon.getHvnr(), noodoproepTelefoon1.getHvnr());
        assertEquals(noodoproepTelefoon.getmannr(), noodoproepTelefoon1.getmannr());
    }

}