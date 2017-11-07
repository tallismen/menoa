package nl.anwb.menoa.util;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.anwb.menoa.R;

public class AlertDialogUtil {

    private static final Logger log = LoggerFactory.getLogger("AlertDialogUtil");

    private AlertDialogUtil() {
    }

    public static AlertDialog makeAlertDialog(Context context, String title, String message) {
        log.info("makeAlertDialog()");
        AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.setIcon(R.drawable.ic_info_outline);
        dialog.setTitle(title);
        dialog.setMessage(message);
        VibratorUtil.vibratePhone(context, 100);
        return dialog;
    }

    public static void showAlertDialog(Context context, String title, String message) {
        final AlertDialog dialog = makeAlertDialog(context, title, message);
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, context.getString(R.string.ok_button_dialog), (dialogInterface, i) -> dialog.dismiss());
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, context.getString(R.string.sluiten_button_dialog), (dialog1, which) -> dialog1.dismiss());
        dialog.show();
    }
}
