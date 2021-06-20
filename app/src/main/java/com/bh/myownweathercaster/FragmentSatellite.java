package com.bh.myownweathercaster;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentSatellite extends Fragment {

    private ViewGroup rootView;

    public FragmentSatellite() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_satellite, container, false);

        return rootView;
    }

    public void setVisibility() {
        ProgressBar pb_sat = rootView.findViewById(R.id.pb_sat);
        pb_sat.setVisibility(View.INVISIBLE);
        Button btn_play_pause = rootView.findViewById(R.id.btn_play_pause);
        btn_play_pause.setVisibility(View.VISIBLE);
    }

}