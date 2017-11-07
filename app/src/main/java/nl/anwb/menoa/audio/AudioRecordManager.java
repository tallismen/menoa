package nl.anwb.menoa.audio;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaRecorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import nl.anwb.menoa.comm.UploadComm;
import nl.anwb.menoa.ui.MainActivity;


/**
 * De manager voor de audioRecorder
 */
public class AudioRecordManager {

    private static final Logger log = LoggerFactory.getLogger("AudioRecordManager");
    private static String mFileName = null;
    private boolean recording = false;
    private boolean noodoproepAfgerond = false;
    private MediaRecorder mRecorder;
    private final Context context;
    private String mannr;

    public AudioRecordManager(Context context) {
        log.info("AudioRecordManager Intitialiseren...");
        this.context = context;
        //mannr = SettingsActivity.getDefaultString("mannr", context);
    }

    public boolean isRecording() {
        return recording;
    }

    /**
     * Haalt de huidige tijd op.
     *
     * @return huidige tijd
     */
    private static String getCurrentTimeStamp() {
        try {
            @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mmss");
            String currentDateTime = dateFormat.format(new Date()); // Find todays date
            log.info("Timestamp: " + currentDateTime);
            return currentDateTime;
        } catch (Exception e) {
            log.error(e.toString());
            return null;
        }
    }

    /**
     * Start recording en zet filenaam van dateStamp
     */
    public void startRecord() {
        if (!recording && !noodoproepAfgerond) {
            recording = true;
            mFileName = context.getExternalMediaDirs()[0].getAbsolutePath() + "/mannr" + mannr + "-" + getCurrentTimeStamp() + ".m4a";
            log.info("Start recording, Filename: " + mFileName);
            try {
                setupRecorder();
                Thread looper = new Thread() {
                    @Override
                    public void run() {
                        try {
                            mRecorder.start();
                            log.info("Sleep voor 10 secs.");
                            TimeUnit.SECONDS.sleep(10);
                        } catch (Exception e) {
                            log.error("Er is iets in de thread fout gegaan!: " + e);
                        }
                        stopRecord();
                        startRecord();
                    }
                };
                looper.start();
            } catch (Exception e) {
                log.error("Start record mislukt: " + e);
            }
        } else {
            stopRecord();
        }
    }

    /**
     * Stop recording
     */
    private void stopRecord() {
        if (mRecorder != null) {
            log.info("Stoprecording");
            try {
                mRecorder.stop();
                mRecorder.release();
                mRecorder = null;
                recording = false;
            } catch (Exception e) {
                log.error("Stop record mislukt: " + e);
            }
            UploadComm.uploadFilesToServer((MainActivity) context);
        }
    }

    public void endNoodoproepRecord() {
        this.noodoproepAfgerond = true;
    }

    /**
     * De setup van de controller.
     */
    private void setupRecorder() {
        log.info("setupRecorder()");
        mRecorder = new MediaRecorder();
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            mRecorder.prepare();
        } catch (IOException e) {
            log.error("prepare() failed");
        }
    }


}
