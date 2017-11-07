package nl.anwb.menoa.services;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocationService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final IBinder _binder = new LocalBinder();    //Binder dat de mainActivity deze service kan bereiken.

    private GoogleApiClient _apiClient;
    private Location _lastLocation;

    public LocationService() {
    }

    @Override
    public void onCreate() {
        log.info("onCreate()");
        if (_apiClient == null) {
            _apiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        _apiClient.connect();
    }

    @Override
    public void onDestroy() {
        log.info("onDestroy");
        if (_apiClient != null) {
            _apiClient.disconnect();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return _binder;
    }

    public LocationRequest createLocationRequest() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        return locationRequest;
    }

    @Override
    public void onLocationChanged(Location location) {
        log.info("LocationChanged: (" + location.getLatitude() + "," + location.getLongitude() + ")");
        _lastLocation = location;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        try {
            _lastLocation = LocationServices.FusedLocationApi.getLastLocation(_apiClient);
//            mLatitudeText.setText(String.valueOf(_lastLocation.getLatitude()));
//            mLongitudeText.setText(String.valueOf(_lastLocation.getLongitude()));
            LocationServices.FusedLocationApi.requestLocationUpdates(_apiClient, createLocationRequest(), this);
        } catch (SecurityException e) {
            // Handle potential security exception
            log.error(e.toString());
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        log.info("onConnectionSuspended() " + i);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        log.info("onConnectionFailed() " + connectionResult.toString());
    }

    /**
     * De Binder class die nodig is bij het binden van de service met de mainActivity
     */
    public class LocalBinder extends Binder {
        public LocationService getLocationServiceInstance() {
            return LocationService.this;
        }
    }

    public Location getLastLocation() {
        log.info("getLastLocation() " + _lastLocation.getLatitude() + "," + _lastLocation.getLongitude());
        return _lastLocation;
    }
}
