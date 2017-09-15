package de.hpi.coap_from_android;

import android.content.Context;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;

public class LedSwitchController implements OnCheckedChangeListener {

    private Context context;
    private Switch toggle;
    private final LedColor color;
    private OpenMote openMote;

    public LedSwitchController(Context context, Switch toggle, LedColor color, OpenMote openMote) {
        this.context = context;
        this.toggle = toggle;
        this.color = color;
        this.openMote = openMote;
        toggle.setOnCheckedChangeListener(this);
        openMote.setLed(color, false);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        openMote.setLed(color, b);
    }
}
