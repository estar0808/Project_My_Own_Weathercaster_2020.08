package com.bh.myownweathercaster;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Fragment2 extends Fragment {

    private TextView tv_khai;
    private ImageView iv_khai;

    private TextView tv_pm10;
    private ImageView iv_pm10;

    private TextView tv_pm25;
    private ImageView iv_pm25;

    private TextView tv_o3;
    private ImageView iv_o3;

    private TextView tv_co;
    private ImageView iv_co;

    private TextView tv_so2;
    private ImageView iv_so2;

    private TextView tv_no2;
    private ImageView iv_no2;

    public Fragment2() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment2, container, false);

        tv_khai = rootView.findViewById(R.id.tv_khai);
        iv_khai = rootView.findViewById(R.id.iv_khai);

        tv_pm10 = rootView.findViewById(R.id.tv_pm10);
        iv_pm10 = rootView.findViewById(R.id.iv_pm10);

        tv_pm25 = rootView.findViewById(R.id.tv_pm25);
        iv_pm25 = rootView.findViewById(R.id.iv_pm25);

        tv_o3 = rootView.findViewById(R.id.tv_o3);
        iv_o3 = rootView.findViewById(R.id.iv_o3);

        tv_co = rootView.findViewById(R.id.tv_co);
        iv_co = rootView.findViewById(R.id.iv_co);

        tv_so2 = rootView.findViewById(R.id.tv_so2);
        iv_so2 = rootView.findViewById(R.id.iv_so2);

        tv_no2 = rootView.findViewById(R.id.tv_no2);
        iv_no2 = rootView.findViewById(R.id.iv_no2);

        return rootView;
    }

    public void setKhai(String value) {
        tv_khai.setText(value);
    }

    public void setKhaiFace(Double value) {
        if (value>=0.0 && value<=50.0) {
            iv_khai.setBackgroundResource(R.drawable.face_good_icon);
        } else if (value>50.0 && value<=100.0) {
            iv_khai.setBackgroundResource(R.drawable.face_normal_icon);
        } else if (value>100.0 && value<=250.0) {
            iv_khai.setBackgroundResource(R.drawable.face_bad_icon);
        } else if (value>250.0) {
            iv_khai.setBackgroundResource(R.drawable.face_bad2_icon);
        }
    }

    public void setPM10(String value) {
        tv_pm10.setText(value);
    }

    public void setPM10Face(Double value) {
        if (value>=0.0 && value<=30.0) {
            iv_pm10.setBackgroundResource(R.drawable.face_good_icon);
        } else if (value>30.0 && value<=80.0) {
            iv_pm10.setBackgroundResource(R.drawable.face_normal_icon);
        } else if (value>80.0 && value<=150.0) {
            iv_pm10.setBackgroundResource(R.drawable.face_bad_icon);
        } else if (value>150.0) {
            iv_pm10.setBackgroundResource(R.drawable.face_bad2_icon);
        }
    }

    public void setPM25(String value) {
        tv_pm25.setText(value);
    }

    public void setPM25Face(Double value) {
        if (value>=0.0 && value<=15.0) {
            iv_pm25.setBackgroundResource(R.drawable.face_good_icon);
        } else if (value>15.0 && value<=35.0) {
            iv_pm25.setBackgroundResource(R.drawable.face_normal_icon);
        } else if (value>35.0 && value<=75.0) {
            iv_pm25.setBackgroundResource(R.drawable.face_bad_icon);
        } else if (value>75.0) {
            iv_pm25.setBackgroundResource(R.drawable.face_bad2_icon);
        }
    }

    public void setO3(String value) {
        tv_o3.setText(value);
    }

    public void setO3Face(Double value) {
        if (value>=0.0 && value<=0.03) {
            iv_o3.setBackgroundResource(R.drawable.face_good_icon);
        } else if (value>0.03 && value<=0.09) {
            iv_o3.setBackgroundResource(R.drawable.face_normal_icon);
        } else if (value>0.09 && value<=0.15) {
            iv_o3.setBackgroundResource(R.drawable.face_bad_icon);
        } else if (value>0.15) {
            iv_o3.setBackgroundResource(R.drawable.face_bad2_icon);
        }
    }

    public void setCO(String value){
        tv_co.setText(value);
    }

    public void setCOFace(Double value) {
        if (value>=0.0 && value<=2.0) {
            iv_co.setBackgroundResource(R.drawable.face_good_icon);
        } else if (value>2.0 && value<=9.0) {
            iv_co.setBackgroundResource(R.drawable.face_normal_icon);
        } else if (value>9.0 && value<=15.0) {
            iv_co.setBackgroundResource(R.drawable.face_bad_icon);
        } else if (value>15.0) {
            iv_co.setBackgroundResource(R.drawable.face_bad2_icon);
        }
    }

    public void setSO2(String value){
        tv_so2.setText(value);
    }

    public void setSO2Face(Double value) {
        if (value>=0.0 && value<=0.02) {
            iv_so2.setBackgroundResource(R.drawable.face_good_icon);
        } else if (value>0.02 && value<=0.05) {
            iv_so2.setBackgroundResource(R.drawable.face_normal_icon);
        } else if (value>0.05 && value<=0.15) {
            iv_so2.setBackgroundResource(R.drawable.face_bad_icon);
        } else if (value>0.15) {
            iv_so2.setBackgroundResource(R.drawable.face_bad2_icon);
        }
    }

    public void setNO2(String value) {
        tv_no2.setText(value);
    }

    public void setNO2Face(Double value) {
        if (value>=0.0 && value<=0.03) {
            iv_no2.setBackgroundResource(R.drawable.face_good_icon);
        } else if (value>0.03 && value<=0.06) {
            iv_no2.setBackgroundResource(R.drawable.face_normal_icon);
        } else if (value>0.06 && value<=0.2) {
            iv_no2.setBackgroundResource(R.drawable.face_bad_icon);
        } else if (value>0.2) {
            iv_no2.setBackgroundResource(R.drawable.face_bad2_icon);
        }
    }

}
