package com.development.napptime.paydebt;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Napptime on 10/11/14.
 *
 * This class serves as the host of the fragment that displays information about the
 * app and it's developers.
 */
public class About extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.lay_about, container, false);
    }
}
