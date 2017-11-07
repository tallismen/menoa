package nl.anwb.menoa.ui;


import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nl.anwb.menoa.R;
import nl.anwb.menoa.util.AlertDialogUtil;
import nl.anwb.menoa.util.SharedPreferenceUtil;

public class MyPreferenceFragment extends PreferenceFragment {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private Pattern pattern;

    private int versieClick;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        log.info("onCreate()");
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.activity_settings_fragment);
        setupSettings();
    }

    @Override
    public void onResume() {
        super.onResume();
        setPreferenceSummaries();
    }

    private void setupSettings() {
        log.info("setupSettings()");
        versieClick = 0;
        pattern = Pattern.compile(getString(R.string.personeelRegEx));
        setupAppVersionPreference();
        setupMailLogButton();
        setupPersoneelsnummerChangeListener();
        setupURLChangeListener();
        setupMannrPreference();
    }

    private void setPreferenceSummaries() {
        List<String> preferenceNames = new ArrayList<>();
        preferenceNames.add(getString(R.string.url_preference));
        preferenceNames.add(getString(R.string.hulpverlenernummer_preference));
        preferenceNames.add(getString(R.string.mannr_preference));
        updatePreferenceValue(preferenceNames);
    }

    private void updatePreferenceValue(List<String> preferenceNames) {
        for (String preferenceName : preferenceNames) {
            if (!SharedPreferenceUtil.isPreferenceEmpty(getActivity(), preferenceName)) {
                findPreference(preferenceName).setSummary(SharedPreferenceUtil.getPreferenceValue(getActivity(), preferenceName));
            }
        }
    }

    private void setupAppVersionPreference() {
        log.info("setupAppVersionPreference()");
        Preference versie = findPreference("versie");
        String versienaam = "";
        try {
            versienaam = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName;
        } catch (Exception e) {
            log.error("Versie naam niet opgehaald" + e);
        }
        versie.setSummary(versienaam);
        versie.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                versieClick++;
                if (versieClick > 8) {
                    versieClick = 0;
                    log.info("Easteregg open");
                    easterEgg();
                }
                log.info("Aantal keren geklikt:" + versieClick);
                return true;
            }
        });
    }

    private void easterEgg() {
        AlertDialog alertDialog = AlertDialogUtil.makeAlertDialog(this.getActivity(), "Easter Egg", getString(R.string.easteregg));
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Wegenwacht de Game®", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getActivity().startActivity(new Intent(getActivity(), GameActivity.class));
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Sluiten", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.setIcon(R.mipmap.sirenerood);
        alertDialog.show();
    }

    private void setupURLChangeListener() {
        log.info("setupURLChangeListener()");
        findPreference(getString(R.string.url_preference)).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                log.info("onPreferenceChange: " + preference.getKey() + "newValue: " + newValue.toString());
                if (TextUtils.isEmpty(newValue.toString())) {
                    preference.setSummary(getString(R.string.url_summary));
                } else {
                    preference.setSummary(newValue.toString());
                    SharedPreferenceUtil.setPreferenceValue(getActivity(), getString(R.string.url_preference), newValue.toString());
                }
                return true;
            }
        });
    }

    private void setupPersoneelsnummerChangeListener() {
        log.info("setupPersoneelsnummerChangeListener()");
        findPreference(getString(R.string.hulpverlenernummer_preference)).setOnPreferenceChangeListener((preference, o) -> {
            String personeelsNr = o.toString();
            try {
                Matcher m = pattern.matcher(personeelsNr);
                if (m.matches()) {
                    preference.setSummary(personeelsNr);
                    SharedPreferenceUtil.setPreferenceValue(getActivity(), getString(R.string.hulpverlenernummer_preference), m.group(3));
                    log.info("Personeelsnummer goed: " + personeelsNr);
                    AlertDialogUtil.showAlertDialog(getActivity(), getString(R.string.personeelsnummer_goed_dialog_title), getString(R.string.personeelsnummer_goed_dialog_message));
                    return true;
                } else {
                    log.info("Personeelsnummer fout: " + personeelsNr);
                    AlertDialogUtil.showAlertDialog(getActivity(), getString(R.string.foutpersoneelsnummer), getString(R.string.personeelsnummer_dialog));
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
            return false;
        });
    }

    private void setupMannrPreference() {
        log.info("setupMannrPreference()");
        findPreference(getString(R.string.mannr_preference)).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                log.info("onPreferenceChange: " + preference.getKey() + "newValue: " + newValue.toString());
                if (TextUtils.isEmpty(newValue.toString()) || newValue.toString().length()>4) {
                    preference.setSummary(getString(R.string.mannr_summary));
                    AlertDialogUtil.showAlertDialog(getActivity(),getString(R.string.foutmannr),getString(R.string.mannr_fout_dialog));
                } else {
                    preference.setSummary(newValue.toString());
                    SharedPreferenceUtil.setPreferenceValue(getActivity(), getString(R.string.mannr_preference), newValue.toString());
                }
                return true;
            }
        });
    }

    private void setupMailLogButton() {
        log.info("setupMailLogButton()");
        Preference mailLogButton = getPreferenceManager().findPreference("mailLog");
        mailLogButton.setOnPreferenceClickListener(arg0 -> {
            mailLogClicked();
            return true;
        });
    }

    private void mailLogClicked() {
        log.info("mailLogClicked()");
        String hvnr = findPreference("hvnr").getSummary().toString();

        File[] files = new File(this.getActivity().getFilesDir(), "logs").listFiles();
        ArrayList<Uri> logFiles = new ArrayList<>();
        if (files != null) {
            for (File file : files) {
                String fileName = file.getName();
                if (fileName.startsWith("uitbelapp.log")) {
                    logFiles.add(FileProvider.getUriForFile(
                            getActivity(),
                            "nl.anwb.hv.uitbelapp.fileprovider",
                            file));
                }
            }
        }
        log.debug("Files: " + logFiles);

        Intent emailIntent = new Intent();
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"disweg@anwb.nl"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Uitbelapp logging voor HV " + hvnr);
        emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        emailIntent.setType("message/rfc822");

        if (logFiles.isEmpty()) {
            emailIntent.setAction(Intent.ACTION_SEND);
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Geen log files gevonden");
        } else if (logFiles.size() == 1) {
            emailIntent.setAction(Intent.ACTION_SEND);
            emailIntent.putExtra(Intent.EXTRA_STREAM, logFiles.get(0));
        } else {
            emailIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
            emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, logFiles);
        }
        startActivity(Intent.createChooser(emailIntent, "Send mail..."));
    }

    private void makeSnackbar(String text) {
        Snackbar.make(getActivity().findViewById(R.id.toolbar), text, Snackbar.LENGTH_LONG).show();
    }
}
