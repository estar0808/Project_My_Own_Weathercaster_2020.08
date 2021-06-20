package com.bh.myownweathercaster;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentWthrWrn1 extends Fragment {

    private ViewGroup rootView;

    public FragmentWthrWrn1() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_wthr_wrn1, container, false);

        return rootView;
    }

    public void setINVISIBLE() {
        ProgressBar pb_wrn1 = rootView.findViewById(R.id.pb_wrn1);
        pb_wrn1.setVisibility(View.INVISIBLE);
    }

}