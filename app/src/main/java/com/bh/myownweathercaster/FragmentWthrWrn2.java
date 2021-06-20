package com.bh.myownweathercaster;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentWthrWrn2 extends Fragment {

    private ViewGroup rootView;

    public FragmentWthrWrn2() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_wthr_wrn2, container, false);

        return rootView;
    }

    public void setINVISIBLE() {
        ProgressBar pb_wrn2 = rootView.findViewById(R.id.pb_wrn2);
        pb_wrn2.setVisibility(View.INVISIBLE);
    }

}