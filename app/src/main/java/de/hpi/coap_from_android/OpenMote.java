package de.hpi.coap_from_android;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapObserveRelation;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.MediaTypeRegistry;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class OpenMote implements CoapHandler {

    private CoapClient pingCoapClient;
    private Map<LedColor, CoapClient> ledCoapClients;
    private CoapClient sensorsCoapClient;
    private TemperatureObserver temperatureObserver;
    private HumidityObserver humidityObserver;
    private LightObserver lightObserver;
    private CoapObserveRelation sensorsObserveRelation;

    public OpenMote(String ipv6Address) throws URISyntaxException {
        String baseUri = "coap://[" + ipv6Address + "]:5683/";
        pingCoapClient = new CoapClient(new URI(baseUri));
        ledCoapClients = new HashMap();
        ledCoapClients.put(LedColor.RED, new CoapClient(new URI(baseUri + "leds?color=r")));
        ledCoapClients.put(LedColor.GREEN, new CoapClient(new URI(baseUri + "leds?color=g")));
        sensorsCoapClient = new CoapClient(new URI(baseUri + "sensors"));
    }

    public void setLed(LedColor led, boolean enable) {
        ledCoapClients.get(led).post("mode=" + (enable ? "on" : "off"), MediaTypeRegistry.TEXT_PLAIN);
    }

    public void registerForTemperatureChanges(TemperatureObserver temperatureObserver) {
        if(sensorsObserveRelation == null) {
            sensorsObserveRelation = sensorsCoapClient.observe(this);
        }
        this.temperatureObserver = temperatureObserver;
    }

    public void registerForHumidityChanges(HumidityObserver humidityObserver) {
        if(sensorsObserveRelation == null) {
            sensorsObserveRelation = sensorsCoapClient.observe(this);
        }
        this.humidityObserver = humidityObserver;
    }

    public void registerForLightChanges(final LightObserver lightObserver) {
        if(sensorsObserveRelation == null) {
            sensorsObserveRelation = sensorsCoapClient.observe(this);
        }
        this.lightObserver = lightObserver;
    }

    @Override
    public void onLoad(CoapResponse coapResponse) {
        String responseText;
        responseText = coapResponse.getResponseText();
        if(humidityObserver != null) {
            humidityObserver.onNewHumidity(new Double(responseText.split(";")[1])/100);
        }
        if(temperatureObserver != null) {
            temperatureObserver.onNewTemperature(new Double(responseText.split(";")[0])/100);
        }
        if(temperatureObserver != null) {
            lightObserver.onNewLight(new Double(responseText.split(";")[2]));
        }
    }

    @Override
    public void onError() {

    }
}
