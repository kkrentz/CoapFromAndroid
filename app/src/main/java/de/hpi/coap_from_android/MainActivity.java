package de.hpi.coap_from_android;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.LinearLayout.LayoutParams;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Add OpenMoteFragments */
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment fragment;

        for(String address : getResources().getStringArray(R.array.addresses)) {
            Bundle bundle = new Bundle();
            bundle.putString(OpenMoteFragment.IPV6_ADDRESS_KEY, address);
            fragment = Fragment.instantiate(this, OpenMoteFragment.class.getName(), bundle);
            fragmentTransaction.add(R.id.openMoteFragmentContainer, fragment, address);
        }

        fragmentTransaction.commit();
    }

    @Override
    public void onStart() {
        super.onStart();

        /* Set LayoutParams of OpenMoteFragments */
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT,
                1.0f);
        for(String address : getResources().getStringArray(R.array.addresses)) {
            getFragmentManager().findFragmentByTag(address).getView().setLayoutParams(layoutParams);
        }
    }
}
