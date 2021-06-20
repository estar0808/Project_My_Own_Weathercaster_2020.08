package com.bh.myownweathercaster;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Fragment1 extends Fragment {

    private TextView tv_rainfall;
    private TextView tv_humidity;
    private TextView tv_wind_speed;
    private TextView tv_wind_direction;

    public Fragment1() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment1, container, false);

        tv_rainfall = rootView.findViewById(R.id.tv_rainfall);
        tv_humidity = rootView.findViewById(R.id.tv_humidity);
        tv_wind_speed = rootView.findViewById(R.id.tv_wind_speed);
        tv_wind_direction = rootView.findViewById(R.id.tv_wind_direction);

        return rootView;
    }

    public void setRainfall(String value) {
        tv_rainfall.setText(value);
    }

    public void setHumidity(String value) {
        tv_humidity.setText(value);
    }

    public void setWindSpeed(String value) {
        tv_wind_speed.setText(value);
    }

    public void setWindDirection(String value) {

        double temp_value = Double.parseDouble(value);
        double convert_value = (temp_value + 11.25) / 22.5;

        if ((0<=convert_value && convert_value<=1) || 15<convert_value) {
            tv_wind_direction.setText("풍향 : 북");
        } else if (1<convert_value && convert_value<=3) {
            tv_wind_direction.setText("풍향 : 북동");
        } else if (3<convert_value && convert_value<=5) {
            tv_wind_direction.setText("풍향 : 동");
        } else if (5<convert_value && convert_value<=7) {
            tv_wind_direction.setText("풍향 : 남동");
        } else if (7<convert_value && convert_value<=9) {
            tv_wind_direction.setText("풍향 : 남");
        } else if (9<convert_value && convert_value<=11) {
            tv_wind_direction.setText("풍향 : 남서");
        } else if (11<convert_value && convert_value<=13) {
            tv_wind_direction.setText("풍향 : 서");
        } else if (13<convert_value && convert_value<=15) {
            tv_wind_direction.setText("풍향 : 북서");
        }
    }

}