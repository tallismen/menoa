package nl.anwb.menoa.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * De Broadcastreceiver die elke keer zorgt als het scherm uit gaat de Mainactivity wordt opgestart.
 */
public class BroadcastReceiverManager extends BroadcastReceiver {

    private final Logger log = LoggerFactory.getLogger("BroadcastReceiverManager");

    // Constructor is mandatory
    public BroadcastReceiverManager() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        log.info("onReceive() aangeroepen");
        log.info("Intent: " + intent.getAction());
//        String intentAction = intent.getAction();
//        log.info(intentAction.toString() + " happended");
//        if (!Intent.ACTION_MEDIA_BUTTON.equals(intentAction)) {
//            log.info("no media button information");
//        }
//        KeyEvent event = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
//        if (event!= null) {
//            if(event.getAction() == KeyEvent.ACTION_DOWN){
//                Intent intent1 = new Intent(context, MainActivity.class);
//                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(intent1);
//            }
//            log.info("Keypress "+event.toString());
//            log.info("Longpress: "+event.isLongPress());
//        }
//        if (!PowerManager.instance(context).isScreenOn()) { //when screen is off ui volume change will not happen
//                PowerManager.instance(context).wake();
//                log.info("wake lock done");
//        }

    }


}
