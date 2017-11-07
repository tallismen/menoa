package nl.anwb.menoa.services;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.session.MediaSessionManager;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.media.session.MediaSessionCompat;

import org.slf4j.LoggerFactory;

import nl.anwb.menoa.manager.PowerManager;
import nl.anwb.menoa.ui.MainActivity;

public class MediaPlayerService extends Service {

    private final org.slf4j.Logger log = LoggerFactory.getLogger(getClass());

    private Context context;
    private MediaSessionManager mediaSessionManager;
    private MediaSessionCompat mediaSession;

    private void initMediaSession() throws RemoteException {
        log.info("initMediaSession()");
        if (mediaSessionManager != null) return; //mediaSessionManager exists

        mediaSessionManager = (MediaSessionManager) getSystemService(Context.MEDIA_SESSION_SERVICE);
        // Create a new MediaSession
        mediaSession = new MediaSessionCompat(getApplicationContext(), "AudioPlayer");
        //set MediaSession -> ready to receive media commands
        mediaSession.setActive(true);
        //indicate that the MediaSession handles transport control commands
        // through its MediaSessionCompat.Callback.
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS);

        // Attach Callback to receive MediaSession updates
        mediaSession.setMediaButtonReceiver(null);
        mediaSession.setCallback(new MediaSessionCompat.Callback() {
            @Override
            public boolean onMediaButtonEvent(Intent mediaButtonEvent) {
                log.info("MediabuttonEvent: " + mediaButtonEvent.getAction() + " Extra's: " + mediaButtonEvent.getExtras().toString());
                PowerManager.wakePhone(context);
                Intent intent1 = new Intent(context, MainActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent1);
                return super.onMediaButtonEvent(mediaButtonEvent);
            }
        });
    }


    @Override
    public void onCreate() {
        log.info("onCreate()");
        context = this;
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        log.info("onDestroy()");
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        log.info("onStartCommand()");
        if (mediaSessionManager == null) {
            log.info("mediaSessionManager == null");
            try {
                initMediaSession();
            } catch (RemoteException e) {
                log.error("InitMediaSession Failed! " + e.getMessage());
                stopSelf();
            }
        }
        log.info("mediaSessionManager = " + mediaSessionManager.toString());
        //MediaButtonReceiver.handleIntent(mediaSession, intent);
        return super.onStartCommand(intent, flags, startId);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
