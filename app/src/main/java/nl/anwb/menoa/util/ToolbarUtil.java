package nl.anwb.menoa.util;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import android.widget.TextView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.anwb.menoa.ActionBarSupport;
import nl.anwb.menoa.R;

public class ToolbarUtil {

    private static final Logger log = LoggerFactory.getLogger("ToolbarUtil");

    private ToolbarUtil() {
    }

    public static void setupActionBar(ActionBarSupport actionBarSupport, Activity activity, String title, boolean homeEnabled) {
        log.info("setupActionBar() "+title);
        activity.getLayoutInflater().inflate(R.layout.toolbar, (ViewGroup) activity.findViewById(android.R.id.content));
        Toolbar toolbar = activity.findViewById(R.id.toolbar);
        actionBarSupport.setSupportActionBar(toolbar);
        ActionBar actionBar = actionBarSupport.getSupportActionBar();
        if (actionBar != null) {
            TextView textView = activity.findViewById(R.id.toolbar_title);
            textView.setText(title);
            actionBar.setDisplayHomeAsUpEnabled(homeEnabled);
            actionBar.setHomeAsUpIndicator(ContextCompat.getDrawable(activity, R.drawable.ic_arrow_back));
        }
    }
}
