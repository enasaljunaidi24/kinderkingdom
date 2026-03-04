package com.example.kinderkingdom;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class bustour extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient locationClient;
    private LocationCallback locationCallback;

    private Marker busMarker;
    private Polyline busPolyline;
    private final List<LatLng> pathPoints = new ArrayList<>();

    private static final int LOCATION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bustour);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        locationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_REQUEST_CODE
            );
        }
    }
    // بدء الخريطة من الاردن
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // خريطة الأردن
        LatLng jordan = new LatLng(31.95, 35.91);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(jordan, 7));

        // حدود الأردن (منع الخروج)
        LatLngBounds jordanBounds = new LatLngBounds(
                new LatLng(29.18, 34.95), // جنوب غرب (العقبة)
                new LatLng(33.37, 39.30)  // شمال شرق
        );

        mMap.setLatLngBoundsForCameraTarget(jordanBounds);
        mMap.setMinZoomPreference(6);
        mMap.setMaxZoomPreference(18);
    }


    // ================= تتبع السائق =================
    private void startTracking() {

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        DatabaseReference busRef =
                FirebaseDatabase.getInstance().getReference("bus");
        DatabaseReference locationRef = busRef.child("location");

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {

                Location location = locationResult.getLastLocation();
                if (location == null) return;

                LatLng newPos = new LatLng(
                        location.getLatitude(),
                        location.getLongitude()
                );

                pathPoints.add(newPos);

                if (busMarker == null) {
                    busMarker = mMap.addMarker(
                            new MarkerOptions()
                                    .position(newPos)
                                    .title("باص المدرسة")
                    );
                    mMap.animateCamera(
                            CameraUpdateFactory.newLatLngZoom(newPos, 15)
                    );
                } else {
                    busMarker.setPosition(newPos);
                }

                if (busPolyline == null) {
                    busPolyline = mMap.addPolyline(
                            new PolylineOptions()
                                    .addAll(pathPoints)
                                    .color(Color.BLUE)
                                    .width(8)
                    );
                } else {
                    busPolyline.setPoints(pathPoints);
                }

                Map<String, Object> map = new HashMap<>();
                map.put("latitude", newPos.latitude);
                map.put("longitude", newPos.longitude);
                locationRef.updateChildren(map);
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            locationClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.getMainLooper()
            );
        }
    }

    // ================= تتبع للأهل =================
    private void listenBusLocation() {

        DatabaseReference ref =
                FirebaseDatabase.getInstance().getReference("bus/location");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (!snapshot.exists()) return;

                Double lat = snapshot.child("latitude").getValue(Double.class);
                Double lng = snapshot.child("longitude").getValue(Double.class);

                if (lat == null || lng == null) return;

                moveBusMarkerSmooth(new LatLng(lat, lng));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    // ================= تحريك ناعم =================
    private void moveBusMarkerSmooth(LatLng newPos) {

        if (busMarker == null) {
            busMarker = mMap.addMarker(
                    new MarkerOptions()
                            .position(newPos)
                            .title("باص المدرسة")
            );
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(newPos, 15));
            return;
        }

        LatLng startPos = busMarker.getPosition();
        long startTime = SystemClock.uptimeMillis();
        long duration = 1000;

        Handler handler = new Handler(Looper.getMainLooper());

        handler.post(new Runnable() {
            @Override
            public void run() {
                float t = (SystemClock.uptimeMillis() - startTime) / (float) duration;
                if (t > 1) t = 1;

                double lat = startPos.latitude + t * (newPos.latitude - startPos.latitude);
                double lng = startPos.longitude + t * (newPos.longitude - startPos.longitude);

                busMarker.setPosition(new LatLng(lat, lng));

                if (t < 1) handler.postDelayed(this, 16);
            }
        });
    }

    // ================= صلاحيات =================
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_REQUEST_CODE &&
                grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED && mMap != null) {

                mMap.setMyLocationEnabled(true);
                startTracking();
                listenBusLocation();
            }

        } else {
            Toast.makeText(this,
                    "يرجى تفعيل صلاحية الموقع",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationClient != null && locationCallback != null) {
            locationClient.removeLocationUpdates(locationCallback);
        }
    }
}
