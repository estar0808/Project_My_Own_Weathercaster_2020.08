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

public class FragmentRadar extends Fragment {

    private ViewGroup rootView;

    public FragmentRadar() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_radar, container, false);

        return rootView;
    }

    public void setVisibility() {
        ProgressBar pb_rdr = rootView.findViewById(R.id.pb_rdr);
        pb_rdr.setVisibility(View.INVISIBLE);
        Button btn_play_pause2 = rootView.findViewById(R.id.btn_play_pause2);
        btn_play_pause2.setVisibility(View.VISIBLE);
    }

}