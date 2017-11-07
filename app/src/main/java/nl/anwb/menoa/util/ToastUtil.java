package nl.anwb.menoa.util;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.widget.Toast;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.anwb.menoa.R;

/**
 * Deze class heeft regelt alle Toast meldingen
 */
public class ToastUtil {

    private static final Logger log = LoggerFactory.getLogger("ToastUtil");

    private ToastUtil() {
    }

    /**
     * Deze methode wordt ook buiten de main class gebruikt.
     * Deze zorgt voor een aangepast toast bericht zodat deze over de hele applicatie gelijk is.
     *
     * @param text     is de tekst die in de toast komt te staan.
     * @param duration is hoe lang de toast zichtbaar moet zijn 1 voor kort, 2 voor lang
     */
    public static void displayToast(CharSequence text, int duration, Context context) {
        log.info("displayToast() " + text);
//        LayoutInflater inflater = mainActivity.getLayoutInflater();
//        View layout = inflater.inflate(R.layout.toast, (ViewGroup) mainActivity.findViewById(R.id.toast_layout_root));
//
//        TextView textView = (TextView) layout.findViewById(R.id.text);
//        textView.setText(text);
//
//        Toast toast = new Toast(mainActivity.getApplicationContext());
//        //toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0); //Eventueel andere positie van toast op scherm
//        if (duration != Toast.LENGTH_LONG && duration != Toast.LENGTH_SHORT) {
//            toast.setDuration(Toast.LENGTH_SHORT);
//        } else {
//            toast.setDuration(duration);
//        }
//        toast.setView(layout);
//        toast.show();
//
//        Snackbar snackbar = Snackbar.make(context.findViewById(R.id.noodOproep), text, Snackbar.LENGTH_LONG);
//        snackbar.show();
        Toast.makeText(context,text,duration).show();
    }

}