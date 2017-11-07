package nl.anwb.menoa.ui;

import android.app.KeyguardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.anwb.menoa.ActionBarSupport;
import nl.anwb.menoa.BuildConfig;
import nl.anwb.menoa.R;
import nl.anwb.menoa.comm.MenosComm;
import nl.anwb.menoa.manager.PowerManager;
import nl.anwb.menoa.services.LocationService;
import nl.anwb.menoa.services.MediaPlayerService;
import nl.anwb.menoa.util.AlertDialogUtil;
import nl.anwb.menoa.util.PermissionsUtil;
import nl.anwb.menoa.util.ToastUtil;
import nl.anwb.menoa.util.ToolbarUtil;
import nl.anwb.menoa.util.VibratorUtil;

/**
 * De Main class van de applicatie
 */
public class MainActivity extends AppCompatActivity implements ActionBarSupport {

    private static final int KEY_CODE_XCOVER = 1015;   //De key_code van de xcover knop
    private static final int KEY_CODE_ZEBRA = 285;
    private static final int KEY_CODE_ZEBRA2 = 286;

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final int maxValueProcesbar = 49;   // Ongeveer 3 seconden TODO:Instelbaar maken

    //Status variable
    private boolean sendSucces;  // Of de noodoproep al een keer is sendSucces voorkomt dubbel bericht
    private int presscount;     // Counter hoelang de knop wordt ingedrukt
    private boolean firstTimePermissionDialog = true; //Permission dialog

    //Variable van het systeem
    private MenosComm menosComm;  // De class die alle communicatie doet richting Menos
    private ProgressBar progressbar;// De progressbar die laat zien hoelang er al een knop is ingedrukt

    //Locatie
    private LocationManager locatieManager; //Nodig om te kijken of locatie instellingen aan staan.
    private boolean locatieSettingsGeopend; //Of de locatie settings geopend zijn.
    private LocationService locationService;//De service die locaties moet gaan ophalen doet nog niks
    //Audio
//    private AudioRecordManager audioRecordManager;  //De audiorecord manager

    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            log.info("LocationService verbonden.");
            LocationService.LocalBinder mLocalBinder = (LocationService.LocalBinder) iBinder;
            locationService = mLocalBinder.getLocationServiceInstance();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            log.warn("LocationService verbinding verbroken!");
            locationService = null;
        }
    };

    @Override
    public void onAttachedToWindow() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
    }

    /**
     * Status verandering van de app naar onCreate
     * Maakt de app klaar voor gebruik
     * Zet de variablen en checkt permission.
     *
     * @param savedInstanceState opgeslagen state activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        log.info(" onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ToolbarUtil.setupActionBar(this, this, getString(R.string.app_name), false);
        PermissionsUtil.checkAndRequestPermissions(this);
        PowerManager.wakePhone(this);
        resetVariables();
    }

    /**
     * Status verandering naar onResume
     * Checkt of de locatie al enabled is
     */
    @Override
    public void onResume() {
        log.info(" onResume()");
        super.onResume();
        onReset();
        checkForLocationEnabled();
        startService(new Intent(this, LocationService.class));
        startService(new Intent(this, MediaPlayerService.class));
        log.info("Binding LocationService...");
        bindService(new Intent(this, LocationService.class), serviceConnection, BIND_AUTO_CREATE);
        PowerManager.wakePhone(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        log.info("onStart()");
    }

    /**
     * Status verandering naar onstop
     * checkt of de audio recorder al gestopt is en reset hem
     */
    @Override
    public void onStop() {
        super.onStop();
        log.info(" onStop()");
        unbindService(serviceConnection);
        log.info("Unbinding LocationService...");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    showGeenPermissieDialog();
                }
            }
        }
    }

    /**
     * Handelt de onbackbutton en sluit de openstaande intent
     */
    @Override
    public void onBackPressed() {
//        if (audioRecordManager.isRecording()) {
//            ToastUtil.displayToast(this.getString(R.string.toast_noodmelding_actief), Toast.LENGTH_LONG, this);
//        } else {
        resetVariables();
        super.onBackPressed();
        this.finish();
//        }
    }

    /**
     * Maakt het menu aan
     *
     * @param menu geeft het lijstje opties
     * @return altijd waar
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(nl.anwb.menoa.R.menu.menu, menu);
        return true;
    }

    /**
     * De onclick functie van de menu opties
     * in een switch alle opties
     *
     * @param item het item waarom geklickt is
     * @return roept de super aan
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.preferences: {
                KeyguardManager myKM = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
                if (myKM.inKeyguardRestrictedInputMode()) {
//                    AlertDialogUtil.showAlertDialog(this, "Telefoon vergrendeld!", "Het is niet mogelijk om naar de instellingen te gaan als de telefoon vergrendeld is.");
                    ToastUtil.displayToast("Telefoon is vergrendeld!", Toast.LENGTH_LONG, this);
                } else {
                    Intent i = new Intent(this, SettingsActivity.class);
                    startActivity(i);
                }
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Als er een hardware toets/knop is ingedrukt
     *
     * @param keycode de code van die toets/knop
     * @param e       het bijbehorende event
     * @return de super methode
     */
    @Override
    public boolean onKeyDown(int keycode, KeyEvent e) {
        switch (keycode) {
            case KEY_CODE_XCOVER:
            case KEY_CODE_ZEBRA:
            case KEY_CODE_ZEBRA2:
            case KeyEvent.KEYCODE_VOLUME_UP:
            case KeyEvent.KEYCODE_HEADSETHOOK:
                updatepresscount();
                return true;
        }
        return super.onKeyDown(keycode, e);
    }

    /**
     * Elke keyup zorgt ervoor dat de timer reset wordt.
     *
     * @param keycode de code van de hardwaretoets
     * @param e       het event van de hardwaretoets
     * @return de supermethode
     */
    @Override
    public boolean onKeyUp(int keycode, KeyEvent e) {
        //resetVariables();
        presscount = -1;
        updatepresscount();
        return super.onKeyUp(keycode, e);
    }

    /**
     * Deze methode zet alle variablen
     * Voor het eerst starten van de applicatie en het resetten
     */
    private void resetVariables() {
        presscount = -1;
        sendSucces = false;
        locatieSettingsGeopend = false;
//        audioRecordManager = new AudioRecordManager(this);
        menosComm = new MenosComm(this);
        locatieManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        log.info("Variablelen gereset.");
    }

    /**
     * Deze methode kijkt of de noodmelding al sendSucces is
     * en als deze al sendSucces is verzend hij deze niet een tweede keer.
     */
    private void noodmeldingVerzenden() {
        Location lastKnownLocation = locationService.getLastLocation();
        if (lastKnownLocation != null) {
            if (!sendSucces) {
//                int mannr = Integer.getInteger(SharedPreferenceUtil.getPreferenceValue(this, getString(R.string.mannr_summary)));
//                int hvnr = Integer.getInteger(SharedPreferenceUtil.getPreferenceValue(this, getString(R.string.hulpverlenernummer_preference)));
//                String url = SharedPreferenceUtil.getPreferenceValue(this, getString(R.string.url_preference)) + "/noodMelding";
//                sendSucces = menosComm.noodmeldingNaarServer(lastKnownLocation,url,hvnr,mannr);
                sendSucces = menosComm.noodmeldingNaarServer(lastKnownLocation, "https://hvapp-ontw.anwb.local/menos/noodMelding", 0, 0);
                onReset();
                //Fixme: recording gedeelte uitgeschakeld
//                if (!audioRecordManager.isRecording()) {
//                    audioRecordManager.startRecord();
//                }
//                noodButtonBackground.setBackground(getDrawable(R.drawable.button_green));
//                noodoproepbutton.setText(R.string.annuleren);
            } else {
                ToastUtil.displayToast(this.getString(R.string.toast_noodmelding_beeindigd), Toast.LENGTH_LONG, this);
                onReset();
            }
        } else {
            ToastUtil.displayToast(this.getString(R.string.toast_geen_locatie), Toast.LENGTH_LONG, this);
            VibratorUtil.vibratePhone(this, 5000);// 5 seconden trillen niet sendSucces
            checkForLocationEnabled();
        }
    }

    /**
     * Deze methode roept de reset variables aan en geeft via een toast
     * weer dat de reset is aangeroepen.
     */
    public void onReset() {
//        audioRecordManager.endNoodoproepRecord();
        resetVariables();
        updatepresscount();
        ToastUtil.displayToast(this.getString(R.string.toast_reset), Toast.LENGTH_SHORT, this);
    }

    /**
     * Wordt aangeroepen bij elke onclick event wat afkomstig is van de onButtonPressed
     * Tellt de presscount op( de timer)
     * zet de progressbar bovenaan
     * en roept het trillsigniaal aan.
     */
    private void updatepresscount() {
        if (!sendSucces) {
            presscount++;
            progressbar = findViewById(R.id.progressBar2);
            progressbar.setMax(maxValueProcesbar);
            progressbar.setProgress(presscount);
            progressbar.setScaleY(3f);
            if (presscount == maxValueProcesbar) {
                noodmeldingVerzenden();
            }
        }
    }

    /**
     * Checkt of de locatie instellingen aanstaan en maakt de alertdialog daarvoor aan.
     */
    private void checkForLocationEnabled() {
        log.info("Checkt locatie instellingen.");
        if (!(locatieManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locatieManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) && !locatieSettingsGeopend) {
            log.info("Toont locatie instelling dialog");
            final AlertDialog locatiedialog = AlertDialogUtil.makeAlertDialog(this, getString(R.string.titel_locatie_intelling), getString(R.string.bericht_locatie_tekst));
            locatiedialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.positive_button_locatie), (dialog, which) -> {
                log.info("Opent locatie settings");
                locatieSettingsGeopend = true;
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            });
            locatiedialog.setOnDismissListener(dialog -> checkForLocationEnabled());
            locatiedialog.show();
        }
    }

    private void showGeenPermissieDialog() {
        log.info("showGeenPermissieDialog()");
        final AlertDialog dialog = AlertDialogUtil.makeAlertDialog(this, getString(R.string.geen_permission), getString(R.string.geen_permission_dialog));
        final Context context = this;
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.sluiten_button_dialog), (dialogInterface, i) -> dialog.dismiss());
        dialog.setOnDismissListener(dialogInterface -> PermissionsUtil.checkAndRequestPermissions(context));
        if (!firstTimePermissionDialog) {
            dialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.permission_button_dialog), (dialogInterface, i) -> {
                startActivity(new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + BuildConfig.APPLICATION_ID)));
                finishActivity();
            });
        }
        dialog.show();
        firstTimePermissionDialog = false;
    }

    private void finishActivity() {
        this.finish();
    }

    /**
     * De methode die de onclick afhandelt van de noodmeld knoppen
     * waaronder het plaatje
     *
     * @param view de knop waarop is gedrukt, niet nodig maar is nodig voor android.
     */
    public void onNoodmeldingPressed(View view) {
        noodmeldingVerzenden();
    }

    /**
     * Anuleren knop ingedrukt
     *
     * @param view de button die is ingedrukt
     */
    public void onResetPressed(View view) {
        AlertDialog alertDialog = AlertDialogUtil.makeAlertDialog(this, getString(R.string.titel_noodmeld_annuleren), getString(R.string.bericht_annuleren_tekst));
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.ok_button_dialog), (dialog, which) -> onReset());
        alertDialog.show();
    }

    /**
     * Beeindigen pressed
     *
     * @param view button die is ingedrukt
     */
    public void onSluitenPressed(View view) {
        //createAndShowAlertDialog(R.string.titel_noodmeld_beeindigen, R.string.bericht_beeindigen_tekst);
        onBackPressed();
    }
}
