package ptr.hf.ui.map;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.android.PolyUtil;
import com.google.maps.errors.ApiException;
import com.google.maps.model.Bounds;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;

import org.joda.time.DateTime;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import ptr.hf.R;
import ptr.hf.helper.GPSTracker;
import ptr.hf.network.ApiHelper;
import ptr.hf.network.ErrorResponse;
import ptr.hf.network.IApiResultListener;
import ptr.hf.network.Station;
import ptr.hf.ui.ReservationFragment;

import static ptr.hf.ui.auth.LoginActivity.TAG;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private static final int overview = 0;

    Polyline polylineFinal;
    Marker marker;
    Marker addedMarker;

    private List<Station> stations = new ArrayList<>();
    private MapView mapView;
    private GoogleMap map;
    private GeoDataClient geoDataClient;
    private PlaceDetectionClient placeDetectionClient;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private View popup;

    private ProgressDialog dialog;


    public LayoutInflater layoutInflater;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        ButterKnife.bind(view);
        dialog = ProgressDialog.show(getContext(), "",
                "Térkép betöltése, kérem várjon...", true);
        dialog.setCancelable(true);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());


        getMyLocation();

//        geoDataClient = Places.getGeoDataClient(getContext(), null);
//
//        placeDetectionClient = Places.getPlaceDetectionClient(getContext(), null);
//        if ( ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
//
//            ActivityCompat.requestPermissions( this, new String[] {  android.Manifest.permission.ACCESS_COARSE_LOCATION  },
//                    LocationService.MY_PERMISSION_ACCESS_COURSE_LOCATION );
//        }
//


        return view;
    }

    private com.google.maps.model.LatLng getMyLocation() {

        Location location = new Location("myLoc");
        location.setLatitude(47.5);
        location.setLongitude(19);

        GPSTracker tracker = new GPSTracker(getContext());
        if (!tracker.canGetLocation()) {
            tracker.showSettingsAlert();
        } else {
            location.setLatitude(tracker.getLatitude());
            location.setLongitude(tracker.getLongitude());
        }
        return new com.google.maps.model.LatLng(location.getLatitude(), location.getLongitude());
    }

    private GeoApiContext getGeoContext() {
        GeoApiContext geoApiContext = new GeoApiContext();

        return geoApiContext.setQueryRateLimit(3)
                .setApiKey(getString(R.string.directionsApiKey))
                .setConnectTimeout(1, TimeUnit.SECONDS)
                .setReadTimeout(1, TimeUnit.SECONDS)
                .setWriteTimeout(1, TimeUnit.SECONDS);
    }

    private DirectionsResult getDirectionsDetails(com.google.maps.model.LatLng origin, com.google.maps.model.LatLng destination, TravelMode mode) {
        DateTime now = new DateTime();
        try {
            return DirectionsApi.newRequest(getGeoContext())
                    .mode(mode)
//                    .origin(new com.google.maps.model.LatLng(47,19))
                    .origin(origin)
                    .destination(destination)
                    .departureTime(now)
                    .await();
        } catch (ApiException e) {
            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    private void addMarkersToMap(DirectionsResult results, GoogleMap mMap) {
//        mMap.addMarker(new MarkerOptions()
//                .position(new LatLng(
//                        results
//                                .routes[overview]
//                                .legs[overview]
//                                .startLocation.lat,
//                        results
//                                .routes[overview]
//                                .legs[overview]
//                                .startLocation.lng))
//                .title(results.routes[overview].legs[overview].startAddress)
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pin)));
//        mMap.addMarker(new MarkerOptions()
//                .position(new LatLng(
//                        results
//                                .routes[overview]
//                                .legs[overview]
//                                .endLocation.lat,
//                        results
//                                .routes[overview]
//                                .legs[overview]
//                                .endLocation.lng))
//                .title(results
//                        .routes[overview]
//                        .legs[overview]
//                        .endAddress)
//                .snippet(getEndLocationTitle(results))
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pin)));
//        positionCamera(new LatLng(
//                results
//                        .routes[overview]
//                        .legs[overview]
//                        .startLocation.lat,
//                results
//                        .routes[overview]
//                        .legs[overview]
//                        .startLocation.lng), map);
        for (int i = 1; i < results.routes[overview].legs.length; i++) {
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(
                            results
                                    .routes[overview]
                                    .legs[i]
                                    .endLocation.lat,
                            results
                                    .routes[overview]
                                    .legs[i]
                                    .endLocation.lng))
                    .title(results
                            .routes[overview]
                            .legs[i]
                            .startAddress)
                    .title(results.routes[overview].summary)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pin)));
        }

        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(new LatLngBounds(new LatLng(results.routes[overview].bounds.southwest.lat, results.routes[overview].bounds.southwest.lng),
                new LatLng(results.routes[overview].bounds.northeast.lat, results.routes[overview].bounds.northeast.lng)), 80);
        mMap.animateCamera(cu);

    }

    private void positionCamera(LatLng route, GoogleMap mMap) {
        if (mMap != null)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route, 12));
    }

    PolylineOptions polylineOptions;


    private void addPolyline(DirectionsResult results, GoogleMap mMap) {
        if (polylineFinal != null)
            polylineFinal.remove();
        if (results.routes[overview].overviewPolyline == null)
            return;
        List<LatLng> decodedPath = PolyUtil.decode(results.routes[overview].overviewPolyline.getEncodedPath());
        PolylineOptions rectLine = new PolylineOptions().width(10).color(getResources().getColor(R.color.white));
        polylineOptions = rectLine.addAll(decodedPath);
        polylineFinal = mMap.addPolyline(polylineOptions);

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (int i = 0; i < decodedPath.size(); i++)
            builder.include(decodedPath.get(i));
        LatLngBounds bounds = builder.build();
        /**create the camera with bounds and padding to set into map*/
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));

//        final CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 50);

    }

    private String getEndLocationTitle(DirectionsResult results) {
        return "Time :" + results.routes[overview].legs[overview].duration.humanReadable + " Distance :" + results.routes[overview].legs[overview].distance.humanReadable;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);
    }

    private void getStationsFromOCM() {

        ApiHelper.INSTANCE.getStations(getMyLocation().lat, getMyLocation().lng, 1500, 5, new IApiResultListener<List<Station>>() {
            //        ApiHelper.INSTANCE.getStations(getMyLocation().lat, getMyLocation().lng, 300000, 100, new IApiResultListener<List<Station>>() {
            //        ApiHelper.INSTANCE.getStations(47.5, 19.0, 25, new IApiResultListener<ArrayList<Station>>() {
            @Override
            public void success(List<Station> result) {
                if (getContext() != null) {
                    stations.clear();
                    stations.addAll(result);
                    if (stations != null) {
                        for (Station station : stations) {
                            MarkerOptions markerOptions = new MarkerOptions().position(station.getLatLng()).title(station.getAddressTitle());
                            markerOptions.snippet(station.getAddress());
                            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pin));
                            Marker marker = map.addMarker(markerOptions);
                            positionCamera(new LatLng(getMyLocation().lat, getMyLocation().lng), map);
                        }
                    }
                    dialog.dismiss();
                }

            }

            @Override
            public void error(ErrorResponse errorResponse) {
                dialog.dismiss();
                Snackbar
                        .make(getActivity().findViewById(android.R.id.content),
                                "Megszakadt a kapcsolat a kiszolgálóval, Kérem próbálja újra később",
                                Snackbar.LENGTH_LONG)
                        .setAction("Újra", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.show();
                                getStationsFromOCM();
                            }
                        })
                        .show();

            }

            @Override
            public void fail() {
                dialog.dismiss();
                Snackbar
                        .make(getActivity().findViewById(android.R.id.content),
                                "Megszakadt a kapcsolat a kiszolgálóval, Kérem próbálja újra később",
                                Snackbar.LENGTH_LONG)
                        .setAction("Újra", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.show();
                                getStationsFromOCM();
                            }
                        })
                        .show();

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (!isAdded()) {
            return;
        }
        map = googleMap;

        try {
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            getContext(), R.raw.style_json)
            );
            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }

        setupGoogleMapScreenSettings(googleMap);

        map.setOnMarkerClickListener(this);

//        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//            @Override
//            public void onMapClick(LatLng latLng) {
//                if (addedMarker != null)
//                    addedMarker.remove();
//                addedMarker = map.addMarker(new MarkerOptions().position(latLng));
//            }
//        });

        map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter()

        {
            @Override
            public View getInfoWindow(Marker marker) {
                popup = getActivity().getLayoutInflater().inflate(R.layout.popup, null);
                TextView title = popup.findViewById(R.id.marker_info_title);
                title.setText(marker.getTitle());
                TextView address = popup.findViewById(R.id.marker_info_address);
                address.setText(marker.getSnippet());
                if (address.getText().equals("Time :null Distance :null"))
                    address.setVisibility(View.GONE);

                return (popup);
            }

            @Override
            public View getInfoContents(final Marker marker) {
                popup = getActivity().getLayoutInflater().inflate(R.layout.popup, null);
                TextView title = popup.findViewById(R.id.marker_info_title);
                title.setText(marker.getTitle());
                TextView address = popup.findViewById(R.id.marker_info_address);
                address.setText(marker.getSnippet());

//                return (popup);
                return null;

            }
        });

        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                getDirections(map, new com.google.maps.model.LatLng(marker.getPosition().latitude, marker.getPosition().longitude));
            }
        });

        positionCamera(new LatLng(getMyLocation().lat, getMyLocation().lng), map);
        getStationsFromOCM();
//        getDirections(map);
    }

    private void getDirections(final GoogleMap googleMap, final com.google.maps.model.LatLng latLng) {

        googleMap.clear();
        final ProgressDialog dialog2 = ProgressDialog.show(getContext(), "",
                "Tervezés, kérem várjon...", true);
        com.google.maps.model.LatLng myLoc = getMyLocation();
//        DirectionsResult results = getDirectionsDetails(
//                myLoc,
//                latLng, TravelMode.DRIVING);
        ApiHelper.INSTANCE.getRoute(100000, getMyLocation().lat, getMyLocation().lng, latLng.lat, latLng.lng, new IApiResultListener<DirectionsResult>() {
            @Override
            public void success(DirectionsResult result) {
                DirectionsResult results = result;
                if (results != null) {
                    addPolyline(results, googleMap);
                    addMarkersToMap(results, googleMap);
                    dialog2.dismiss();
//                    positionCamera(new LatLng(47.4734695, 19.0595492), googleMap);
                }
            }

            @Override
            public void error(ErrorResponse errorResponse) {
                dialog2.dismiss();
                Snackbar
                        .make(getActivity().findViewById(android.R.id.content),
                                "Megszakadt a kapcsolat a kiszolgálóval, Kérem próbálja újra később",
                                Snackbar.LENGTH_LONG)
                        .show();

            }

            @Override
            public void fail() {
                dialog2.dismiss();
                Snackbar
                        .make(getActivity().findViewById(android.R.id.content),
                                "Megszakadt a kapcsolat a kiszolgálóval, Kérem próbálja újra később",
                                Snackbar.LENGTH_LONG)
                        .show();

            }
        });
    }

//    private void getDirections(final GoogleMap googleMap/*,final com.google.maps.model.LatLng latLng*/) {
//
//        googleMap.clear();
//        com.google.maps.model.LatLng myLoc = getMyLocation();
////        DirectionsResult results = getDirectionsDetails(
////                myLoc,
////                latLng, TravelMode.DRIVING);
//        ApiHelper.INSTANCE.getRoute(100000,getMyLocation().lat, getMyLocation().lng,latLng.lat,latLng.lng,new IApiResultListener<DirectionsResult>() {
//            @Override
//            public void success(DirectionsResult result) {
//                DirectionsResult results = result;
//                if (results != null) {
//                    addPolyline(results, googleMap);
//                    addMarkersToMap(results, googleMap);
//                    positionCamera(new LatLng(47.4734695, 19.0595492), googleMap);
//                }
//            }
//
//            @Override
//            public void error(ErrorResponse errorResponse) {
//
//            }
//
//            @Override
//            public void fail() {
//
//            }
//        });
//        DirectionsResult results = getDirectionsDetails(
//                myLoc,
//                latLng, TravelMode.DRIVING);
//        if (results != null) {
//            addPolyline(results, googleMap);
//            addMarkersToMap(results, googleMap);
//            positionCamera(new LatLng(latLng.lat, latLng.lng), googleMap);
//        }

//    }

    private void setupGoogleMapScreenSettings(GoogleMap mMap) {
        mMap.setBuildingsEnabled(false);
        mMap.setIndoorEnabled(false);
        mMap.setTrafficEnabled(false);
        mMap.setMyLocationEnabled(true);
        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setMapToolbarEnabled(false);
        uiSettings.setRotateGesturesEnabled(false);
        uiSettings.setZoomControlsEnabled(false);
        uiSettings.setCompassEnabled(false);
        uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setScrollGesturesEnabled(true);
        uiSettings.setZoomGesturesEnabled(true);
        uiSettings.setTiltGesturesEnabled(true);
        uiSettings.setRotateGesturesEnabled(true);
    }

    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        float speed = location.getSpeed();

        LatLng latLng = new LatLng(latitude, longitude);

        positionCamera(latLng, map);
    }


    @Override
    public void onPause() {
        super.onPause();
        onDetach();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
}
