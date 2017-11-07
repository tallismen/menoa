package nl.anwb.menoa.manager;


import android.content.Context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PowerManager {
    private static final Logger log = LoggerFactory.getLogger("PowerManager");

    private PowerManager() {
    }

    public static void wakePhone(Context context) {
        log.info("wake()");
        android.os.PowerManager.WakeLock wakeLock = ((android.os.PowerManager) context.getSystemService(Context.POWER_SERVICE))
                .newWakeLock(android.os.PowerManager.SCREEN_BRIGHT_WAKE_LOCK | android.os.PowerManager.ACQUIRE_CAUSES_WAKEUP, "wake");
        wakeLock.acquire();
        wakeLock.release();
    }
}

