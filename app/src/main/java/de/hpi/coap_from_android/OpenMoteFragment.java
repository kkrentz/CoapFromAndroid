package de.hpi.coap_from_android;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import java.net.URISyntaxException;

public class OpenMoteFragment extends Fragment implements TemperatureObserver, HumidityObserver, LightObserver {

    public static final String IPV6_ADDRESS_KEY = OpenMoteFragment.class.getName() + "#IPV6_ADDRESS_KEY";
    public static final String LOG_TAG = "OpenMoteFragment";
    private OpenMote openMote;
    private TextView temperatureTextView;
    private TextView humidityTextView;
    private TextView lightTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.openmote_fragment, container, false);

        try {
            openMote = new OpenMote(getArguments().getCharSequence(IPV6_ADDRESS_KEY).toString());
        } catch (URISyntaxException e) {
            Log.e(LOG_TAG, "", e);
        }

        new LedSwitchController(getContext(), (Switch) view.findViewById(R.id.redSwitch), LedColor.RED, openMote);
        new LedSwitchController(getContext(), (Switch) view.findViewById(R.id.greenSwitch), LedColor.GREEN, openMote);

        temperatureTextView = view.findViewById(R.id.temperatureTextView);
        humidityTextView = view.findViewById(R.id.humidityTextView);
        lightTextView = view.findViewById(R.id.lightTextView);

        openMote.registerForTemperatureChanges(this);
        openMote.registerForHumidityChanges(this);
        openMote.registerForLightChanges(this);

        return view;
    }

    @Override
    public void onNewTemperature(final Double temperature) {
        Log.d(LOG_TAG, "onNewTemperature");
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                temperatureTextView.setText(temperature + " °C");
            }});
    }

    @Override
    public void onNewHumidity(final Double humidity) {
        Log.d(LOG_TAG, "onNewHumidity");
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                humidityTextView.setText(humidity + " %");
            }});
    }

    @Override
    public void onNewLight(final Double light) {
        Log.d(LOG_TAG, "onNewLight");
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                lightTextView.setText(light + " lx");
            }});
    }
}
