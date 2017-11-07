package nl.anwb.menoa.ui;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.anwb.menoa.ActionBarSupport;
import nl.anwb.menoa.R;
import nl.anwb.menoa.util.AlertDialogUtil;
import nl.anwb.menoa.util.SharedPreferenceUtil;
import nl.anwb.menoa.util.ToolbarUtil;

public class SettingsActivity extends AppCompatPreferenceActivity implements ActionBarSupport {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        log.info("onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ToolbarUtil.setupActionBar(this, this, getString(R.string.Instellingen), true);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (!super.onMenuItemSelected(featureId, item)) {
                onBackPressed();
            }
            return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    public void onBackPressed() {
        if (!SharedPreferenceUtil.isPreferenceEmpty(this, "hvnr")) {
            NavUtils.navigateUpFromSameTask(this);
        } else {
            AlertDialogUtil.showAlertDialog(this, getString(R.string.niet_ingevuld_personeelsnummer), getString(R.string.geen_personeelsnummer_dialog));
        }
    }
}