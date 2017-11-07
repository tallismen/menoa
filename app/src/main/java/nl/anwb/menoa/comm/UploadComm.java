package nl.anwb.menoa.comm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

import nl.anwb.menoa.ui.MainActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Deze class gebruikt de Okhttp API om files te uploaden
 */
public class UploadComm {

    private static final MediaType MEDIA_TYPE_MP3 = MediaType.parse("audio/mp3");
    private static final Logger log = LoggerFactory.getLogger("UploadComm");

    /**
     * Deze methode vraagt het lijstje bestanden op die bekend zijn op de server.
     *
     * @param mainActivity de mainactivity nodig voor het ophalen van de Menoslink
     */
    public static void uploadFilesToServer(final MainActivity mainActivity) {
        //final String url = SettingsActivity.getDefaultString("MenosLink", mainActivity);
        String url = "";
        if (url != null) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(url + "/uploadlist").build();

            final String[] filelist = new String[1];
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    log.error("Ophalen mislukt! " + e);
                    filelist[0] = "";
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    filelist[0] = response.body().string();
                    log.info("Response: " + filelist[0]);
                    uploadFilesNotInList(mainActivity, filelist[0]);
                }
            });
        }
    }

    /**
     * Deze methode voert de upload uit van files die niet al op de server bekend is.
     *
     * @param mainActivity de mainactivity nodig voor het ophalen van de folder
     * @param filelist     een String met alle files die op de server bekend zijn
     */
    private static void uploadFilesNotInList(MainActivity mainActivity, String filelist) {
        String path = mainActivity.getExternalMediaDirs()[0].getAbsolutePath();
        File directory = new File(path);
        File[] files = directory.listFiles();
        for (File file : files) {
            String filename = file.getName();
            if (!filelist.contains(filename)) {
                log.info("File: " + filename + " Nog niet op server");
                UploadComm.uploadFile(file, file.getName(), mainActivity);
            } else {
                log.info("File: " + filename + " al op server, lokaal verwijderd: " + file.delete());
            }
        }
    }

    /**
     * Deze methode upload de meegegeven file naar de server
     *
     * @param file         de file die geupload wordt.
     * @param filename     de naam van de file
     * @param mainActivity mainactivity nodig voor het ophalen van de Menoslink
     */
    private static void uploadFile(final File file, final String filename, MainActivity mainActivity) {
        // final String url = SettingsActivity.getDefaultString("MenosLink", mainActivity);
        final String url = "";
        final OkHttpClient client = new OkHttpClient();
        Thread conn = new Thread() {
            public void run() {
                log.info("Thread aangeroepen");
                RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("file", filename, RequestBody.create(MEDIA_TYPE_MP3, file))
                        .build();

                Request request = new Request.Builder().url(url + "/upload")
                        .post(requestBody).build();
                Response response;
                try {
                    response = client.newCall(request).execute();
                    if (!response.isSuccessful()) {
                        log.error("File: " + filename + " niet gelukt!");
                        log.error("Unexpected code " + response);
                    }
                } catch (IOException e) {
                    log.error("File: " + filename + " niet gelukt!" + e);
                }
                log.info("Uploaden: " + filename + " klaar.");
            }
        };
        conn.start();
    }
}
