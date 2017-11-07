package nl.anwb.menoa.util;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class VibratorUtil {

    private static Logger log = LoggerFactory.getLogger("VibratorUtil");
    private static final long[] sosTrill = new long[]{    // SOS tril patroon
            0, 250,
            50, 250,
            50, 250,
            250, 500,
            50, 500,
            50, 500,
            100, 250,
            50, 250,
            50, 250
    };

    private VibratorUtil() {
    }

    /**
     * Laat de telefoon voor een bepaalde tijd trillen
     *
     * @param duration tijd dat de telefoon trilt
     */
    public static void vibratePhone(Context context, long duration) {
        log.info("vibratePhone() " + duration);
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //Deprecaded code vanaf Android 26
            vibrator.vibrate(duration);
        }
    }

    /**
     * Laat de telefoon ...---... trillen
     */
    public static void vibratePhoneSOS(Context context) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(sosTrill, -1);
        log.info("Laat SOS telefoon trillen");
    }

}

