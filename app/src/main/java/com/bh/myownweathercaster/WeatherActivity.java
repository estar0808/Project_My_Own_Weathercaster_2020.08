package com.bh.myownweathercaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import kr.hyosang.coordinate.*;

public class WeatherActivity extends AppCompatActivity {

    private Fragment1 fragment1 = new Fragment1();
    private Fragment2 fragment2 = new Fragment2();
    private Fragment3 fragment3 = new Fragment3();
    private Fragment4 fragment4 = new Fragment4();

    public static String sunrise;
    public static String sunset;

    private double latitude;
    private double longitude;
    private String grid_X;
    private String grid_Y;
    private double tm_x;
    private double tm_y;
    private int TO_GRID = 0;
    private int TO_GPS = 1;

    private String SKYValue;
    private String PTYValue;
    private String T1HValue;
    private String RN1Value;
    private String REHValue;
    private String VECValue;
    private String WSDValue;
    private String pm10Value;
    private String pm25Value;
    private String so2Value;
    private String coValue;
    private String o3Value;
    private String no2Value;
    private String khaiValue;

    private int count;
    private int count2;
    private int count3;
    private int count4;

    private String[] FDValue_sf = new String[30];
    private String[] FTValue_sf = new String[30];
    private String[] POPValue_sf = new String[30];
    private String[] SKYValue_sf = new String[30];
    private String[] T3HValue_sf = new String[30];
    private String[] PTYValue_sf = new String[30];

    private String siName;
    private String fullName;

    private String regId_land;
    private String regId_ta;
    private String[] mf_date = new String[8];
    private String[] wf_am = new String[8];
    private String[] wf_pm = new String[8];
    private String[] rnSt_am = new String[8];
    private String[] rnSt_pm = new String[8];
    private String[] ta_max = new String[8];
    private String[] ta_min = new String[8];

    private String stationNameValue;

    private View v_loading_bg;
    private ProgressBar pb_loading_progress;
    private TextView tv_loading_announce;

    private Button btn_menu;
    private Button btn_place_sel;

    private DrawerLayout drawerLayout;
    private View drawerView;

    public static int isMoved = 0;

    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        ConstraintLayout cl_main = findViewById(R.id.cl_main);
        cl_main.setPadding(0, getStatusBarHeight() ,0, getNavigationBarHeight());

        ViewPager pager = findViewById(R.id.vp_main);
        pager.setOffscreenPageLimit(4);

        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());
        adapter.addItem(0, fragment1);
        adapter.addItem(1, fragment2);
        adapter.addItem(2, fragment3);
        adapter.addItem(3, fragment4);
        pager.setAdapter(adapter);

        set_Sunrise_Sunset();

        do {
            GPSTracker gpsTracker = new GPSTracker(WeatherActivity.this);
            Intent intent = getIntent();

            if (isMoved == 0) {
                longitude = gpsTracker.getLongitude();
                latitude = gpsTracker.getLatitude();
            } else if (isMoved == 1) {
                String get_lon = intent.getExtras().getString("lon");
                String get_lat = intent.getExtras().getString("lat");
                longitude = Double.parseDouble(get_lon);
                latitude = Double.parseDouble(get_lat);
            }

            CoordPoint WGS_PT = new CoordPoint(longitude, latitude);
            CoordPoint TM_PT = TransCoord.getTransCoord(WGS_PT, TransCoord.COORD_TYPE_WGS84, TransCoord.COORD_TYPE_TM);
            tm_x = TM_PT.x;
            tm_y = TM_PT.y;

            LatXLngY grid = convertGRID_GPS(TO_GRID, latitude, longitude);
            grid_X = String.valueOf((int)grid.x);
            grid_Y = String.valueOf((int)grid.y);

            if (isMoved == 0) {

            } else if(isMoved == 1) {
                String place = intent.getExtras().getString("place");
                TextView tv_place = findViewById(R.id.tv_place);
                tv_place.setText(place);
            }
        } while (longitude == 0.0);

        start_V_World();

        drawerLayout = findViewById(R.id.drawer_layout);
        drawerView = findViewById(R.id.drawer_view);

        btn_menu = findViewById(R.id.btn_menu);
        btn_menu.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(drawerView);
            }
        });

        btn_place_sel = findViewById(R.id.btn_place_sel);
        btn_place_sel.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WeatherActivity.this, PlaceSelectActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        Button btn_drawer1 = findViewById(R.id.btn_drawer1);
        btn_drawer1.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WeatherActivity.this, Drawer1Activity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                drawerLayout.closeDrawer(drawerView);
            }
        });

        Button btn_drawer2 = findViewById(R.id.btn_drawer2);
        btn_drawer2.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WeatherActivity.this, Drawer2Activity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                drawerLayout.closeDrawer(drawerView);
            }
        });

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                mAdView = findViewById(R.id.adView);
                AdRequest adRequest = new AdRequest.Builder().build();
                mAdView.loadAd(adRequest);
                mAdView.setAdListener(new AdListener() {
                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();

                        runJustBeforeBeingDrawn(mAdView, new Runnable() {
                            @Override
                            public void run() {
                                ConstraintLayout cl_main = findViewById(R.id.cl_main);
                                cl_main.setPadding(0, getStatusBarHeight() ,0, getNavigationBarHeight() + mAdView.getHeight());
                            }
                        });

                        ConstraintLayout.LayoutParams plControl = (ConstraintLayout.LayoutParams) mAdView.getLayoutParams();
                        plControl.bottomMargin = getNavigationBarHeight();
                    }
                });
            }
        });

    }

    private class PagerAdapter extends FragmentStatePagerAdapter {
        ArrayList<Fragment> items = new ArrayList<Fragment>();

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addItem(int position, Fragment item){
            items.add(position, item);
        }

        @Override
        public Fragment getItem(int position) {
            return items.get(position);
        }

        @Override
        public int getCount() {
            return items.size();
        }
    }

    private void set_Sunrise_Sunset() {
        Date today = new Date();
        SimpleDateFormat MM = new SimpleDateFormat("MM", Locale.KOREA);
        String today_month = MM.format(today);

        switch (today_month) {
            case "01":
                sunrise = "0741";
                sunset = "1739";
                break;
            case "02":
                sunrise = "0718";
                sunset = "1811";
                break;
            case "03":
                sunrise = "0642";
                sunset = "1837";
                break;
            case "04":
                sunrise = "0557";
                sunset = "1904";
                break;
            case "05":
                sunrise = "0524";
                sunset = "1930";
                break;
            case "06":
                sunrise = "0512";
                sunset = "1949";
                break;
            case "07":
                sunrise = "0524";
                sunset = "1948";
                break;
            case "08":
                sunrise = "0548";
                sunset = "1921";
                break;
            case "09":
                sunrise = "0612";
                sunset = "1838";
                break;
            case "10":
                sunrise = "0637";
                sunset = "1755";
                break;
            case "11":
                sunrise = "0707";
                sunset = "1722";
                break;
            case "12":
                sunrise = "0734";
                sunset = "1716";
                break;
        }
    }

    private class LatXLngY {
        public double lat;
        public double lng;
        public double x;
        public double y;
    }

    private LatXLngY convertGRID_GPS(int mode, double lat_X, double lng_Y ) {
        double RE = 6371.00877;
        double GRID = 5.0;
        double SLAT1 = 30.0;
        double SLAT2 = 60.0;
        double OLON = 126.0;
        double OLAT = 38.0;
        double XO = 43;
        double YO = 136;

        double DEGRAD = Math.PI / 180.0;
        double RADDEG = 180.0 / Math.PI;

        double re = RE / GRID;
        double slat1 = SLAT1 * DEGRAD;
        double slat2 = SLAT2 * DEGRAD;
        double olon = OLON * DEGRAD;
        double olat = OLAT * DEGRAD;

        double sn = Math.tan(Math.PI * 0.25 + slat2 * 0.5) / Math.tan(Math.PI * 0.25 + slat1 * 0.5);
        sn = Math.log(Math.cos(slat1) / Math.cos(slat2)) / Math.log(sn);
        double sf = Math.tan(Math.PI * 0.25 + slat1 * 0.5);
        sf = Math.pow(sf, sn) * Math.cos(slat1) / sn;
        double ro = Math.tan(Math.PI * 0.25 + olat * 0.5);
        ro = re * sf / Math.pow(ro, sn);
        LatXLngY rs = new LatXLngY();

        if (mode == TO_GRID) {
            rs.lat = lat_X;
            rs.lng = lng_Y;
            double ra = Math.tan(Math.PI * 0.25 + (lat_X) * DEGRAD * 0.5);
            ra = re * sf / Math.pow(ra, sn);
            double theta = lng_Y * DEGRAD - olon;
            if (theta > Math.PI) theta -= 2.0 * Math.PI;
            if (theta < -Math.PI) theta += 2.0 * Math.PI;
            theta *= sn;
            rs.x = Math.floor(ra * Math.sin(theta) + XO + 0.5);
            rs.y = Math.floor(ro - ra * Math.cos(theta) + YO + 0.5);
        }
        else {
            rs.x = lat_X;
            rs.y = lng_Y;
            double xn = lat_X - XO;
            double yn = ro - lng_Y + YO;
            double ra = Math.sqrt(xn * xn + yn * yn);
            if (sn < 0.0) {
                ra = -ra;
            }
            double alat = Math.pow((re * sf / ra), (1.0 / sn));
            alat = 2.0 * Math.atan(alat) - Math.PI * 0.5;

            double theta = 0.0;
            if (Math.abs(xn) <= 0.0) {
                theta = 0.0;
            }
            else {
                if (Math.abs(yn) <= 0.0) {
                    theta = Math.PI * 0.5;
                    if (xn < 0.0) {
                        theta = -theta;
                    }
                }
                else theta = Math.atan2(xn, yn);
            }
            double alon = theta / sn + olon;
            rs.lat = alat * RADDEG;
            rs.lng = alon * RADDEG;
        }
        return rs;
    }

    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height","dimen","android");

        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }

        return result;
    }

    private int getNavigationBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("navigation_bar_height","dimen","android");

        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }

        return result;
    }

    private String getTagValue(String tag, Element eElement) {
        NodeList nlList = eElement.getElementsByTagName(tag).item(0).getChildNodes();
        Node nValue = nlList.item(0);
        if(nValue == null)
            return null;
        return nValue.getNodeValue();
    }

    private void setRegId() {
        if (siName.contains("서울")) {
            regId_land = "11B00000";
            regId_ta = "11B10101";
        } else if (siName.contains("인천")) {
            regId_land = "11B00000";
            regId_ta = "11B20201";
        } else if (siName.contains("수원")) {
            regId_land = "11B00000";
            regId_ta = "11B20601";
        } else if (siName.contains("성남")) {
            regId_land = "11B00000";
            regId_ta = "11B20605";
        } else if (siName.contains("안양")) {
            regId_land = "11B00000";
            regId_ta = "11B20602";
        } else if (siName.contains("광명")) {
            regId_land = "11B00000";
            regId_ta = "11B10103";
        } else if (siName.contains("과천")) {
            regId_land = "11B00000";
            regId_ta = "11B101012";
        } else if (siName.contains("평택")) {
            regId_land = "11B00000";
            regId_ta = "11B20606";
        } else if (siName.contains("오산")) {
            regId_land = "11B00000";
            regId_ta = "11B20603";
        } else if (siName.contains("의왕")) {
            regId_land = "11B00000";
            regId_ta = "11B20609";
        } else if (siName.contains("용인")) {
            regId_land = "11B00000";
            regId_ta = "11B20612";
        } else if (siName.contains("군포")) {
            regId_land = "11B00000";
            regId_ta = "11B20610";
        } else if (siName.contains("안성")) {
            regId_land = "11B00000";
            regId_ta = "11B20611";
        } else if (siName.contains("화성")) {
            regId_land = "11B00000";
            regId_ta = "11B20604";
        } else if (siName.contains("양평")) {
            regId_land = "11B00000";
            regId_ta = "11B20503";
        } else if (siName.contains("구리")) {
            regId_land = "11B00000";
            regId_ta = "11B20501";
        } else if (siName.contains("남양주")) {
            regId_land = "11B00000";
            regId_ta = "11B20502";
        } else if (siName.contains("하남")) {
            regId_land = "11B00000";
            regId_ta = "11B20504";
        } else if (siName.contains("이천")) {
            regId_land = "11B00000";
            regId_ta = "11B20701";
        } else if (siName.contains("여주")) {
            regId_land = "11B00000";
            regId_ta = "11B20703";
        } else if (siName.contains("광주시")) {
            regId_land = "11B00000";
            regId_ta = "11B20702";
        } else if (siName.contains("의정부")) {
            regId_land = "11B00000";
            regId_ta = "11B20301";
        } else if (siName.contains("고양")) {
            regId_land = "11B00000";
            regId_ta = "11B20302";
        } else if (siName.contains("파주")) {
            regId_land = "11B00000";
            regId_ta = "11B20305";
        } else if (siName.contains("양주")) {
            regId_land = "11B00000";
            regId_ta = "11B20304";
        } else if (siName.contains("동두천")) {
            regId_land = "11B00000";
            regId_ta = "11B20401";
        } else if (siName.contains("연천")) {
            regId_land = "11B00000";
            regId_ta = "11B20402";
        } else if (siName.contains("포천")) {
            regId_land = "11B00000";
            regId_ta = "11B20403";
        } else if (siName.contains("가평")) {
            regId_land = "11B00000";
            regId_ta = "11B20404";
        } else if (siName.contains("강화")) {
            regId_land = "11B00000";
            regId_ta = "11B20101";
        } else if (siName.contains("김포")) {
            regId_land = "11B00000";
            regId_ta = "11B20102";
        } else if (siName.contains("시흥")) {
            regId_land = "11B00000";
            regId_ta = "11B20202";
        } else if (siName.contains("부천")) {
            regId_land = "11B00000";
            regId_ta = "11B20204";
        } else if (siName.contains("안산")) {
            regId_land = "11B00000";
            regId_ta = "11B20203";
        } else if (siName.contains("백령도")) {
            regId_land = "11B00000";
            regId_ta = "11A00101";
        } else if (siName.contains("부산")) {
            regId_land = "11H20000";
            regId_ta = "11H20201";
        } else if (siName.contains("울산")) {
            regId_land = "11H20000";
            regId_ta = "11H20101";
        } else if (siName.contains("김해")) {
            regId_land = "11H20000";
            regId_ta = "11H20304";
        } else if (siName.contains("양산")) {
            regId_land = "11H20000";
            regId_ta = "11H20102";
        } else if (siName.contains("창원")) {
            regId_land = "11H20000";
            regId_ta = "11H20301";
        } else if (siName.contains("밀양")) {
            regId_land = "11H20000";
            regId_ta = "11H20601";
        } else if (siName.contains("함안")) {
            regId_land = "11H20000";
            regId_ta = "11H20603";
        } else if (siName.contains("창녕")) {
            regId_land = "11H20000";
            regId_ta = "11H20604";
        } else if (siName.contains("의령")) {
            regId_land = "11H20000";
            regId_ta = "11H20602";
        } else if (siName.contains("진주")) {
            regId_land = "11H20000";
            regId_ta = "11H20701";
        } else if (siName.contains("하동")) {
            regId_land = "11H20000";
            regId_ta = "11H20604";
        } else if (siName.contains("사천")) {
            regId_land = "11H20000";
            regId_ta = "11H20402";
        } else if (siName.contains("거창")) {
            regId_land = "11H20000";
            regId_ta = "11H20502";
        } else if (siName.contains("합천")) {
            regId_land = "11H20000";
            regId_ta = "11H20503";
        } else if (siName.contains("산청")) {
            regId_land = "11H20000";
            regId_ta = "11H20703";
        } else if (siName.contains("함양")) {
            regId_land = "11H20000";
            regId_ta = "11H20501";
        } else if (siName.contains("통영")) {
            regId_land = "11H20000";
            regId_ta = "11H20401";
        } else if (siName.contains("거제")) {
            regId_land = "11H20000";
            regId_ta = "11H20403";
        } else if (siName.contains("고성")) {
            regId_land = "11H20000";
            regId_ta = "11H20404";
        } else if (siName.contains("남해")) {
            regId_land = "11H20000";
            regId_ta = "11H20405";
        } else if (siName.contains("대구")) {
            regId_land = "11H10000";
            regId_ta = "11H10701";
        } else if (siName.contains("영천")) {
            regId_land = "11H10000";
            regId_ta = "11H10702";
        } else if (siName.contains("경산")) {
            regId_land = "11H10000";
            regId_ta = "11H10703";
        } else if (siName.contains("청도")) {
            regId_land = "11H10000";
            regId_ta = "11H10704";
        } else if (siName.contains("칠곡")) {
            regId_land = "11H10000";
            regId_ta = "11H10705";
        } else if (siName.contains("김천")) {
            regId_land = "11H10000";
            regId_ta = "11H10601";
        } else if (siName.contains("구미")) {
            regId_land = "11H10000";
            regId_ta = "11H10602";
        } else if (siName.contains("군위")) {
            regId_land = "11H10000";
            regId_ta = "11H10603";
        } else if (siName.contains("고령")) {
            regId_land = "11H10000";
            regId_ta = "11H10604";
        } else if (siName.contains("성주")) {
            regId_land = "11H10000";
            regId_ta = "11H10605";
        } else if (siName.contains("안동")) {
            regId_land = "11H10000";
            regId_ta = "11H10501";
        } else if (siName.contains("의성")) {
            regId_land = "11H10000";
            regId_ta = "11H10502";
        } else if (siName.contains("청송")) {
            regId_land = "11H10000";
            regId_ta = "11H10503";
        } else if (siName.contains("상주")) {
            regId_land = "11H10000";
            regId_ta = "11H10302";
        } else if (siName.contains("문경")) {
            regId_land = "11H10000";
            regId_ta = "11H10301";
        } else if (siName.contains("예천")) {
            regId_land = "11H10000";
            regId_ta = "11H10303";
        } else if (siName.contains("영주")) {
            regId_land = "11H10000";
            regId_ta = "11H10401";
        } else if (siName.contains("봉화")) {
            regId_land = "11H10000";
            regId_ta = "11H10402";
        } else if (siName.contains("영양")) {
            regId_land = "11H10000";
            regId_ta = "11H10403";
        } else if (siName.contains("울진")) {
            regId_land = "11H10000";
            regId_ta = "11H10101";
        } else if (siName.contains("영덕")) {
            regId_land = "11H10000";
            regId_ta = "11H10102";
        } else if (siName.contains("포항")) {
            regId_land = "11H10000";
            regId_ta = "11H10201";
        } else if (siName.contains("경주")) {
            regId_land = "11H10000";
            regId_ta = "11H10202";
        } else if (siName.contains("울릉도")) {
            regId_land = "11H10000";
            regId_ta = "11E00101";
        } else if (siName.contains("독도")) {
            regId_land = "11H10000";
            regId_ta = "11E00102";
        } else if (siName.contains("광주광역시")) {
            regId_land = "11F20000";
            regId_ta = "11F20501";
        } else if (siName.contains("나주")) {
            regId_land = "11F20000";
            regId_ta = "11F20503";
        } else if (siName.contains("장성")) {
            regId_land = "11F20000";
            regId_ta = "11F20502";
        } else if (siName.contains("담양")) {
            regId_land = "11F20000";
            regId_ta = "11F20504";
        } else if (siName.contains("화순")) {
            regId_land = "11F20000";
            regId_ta = "11F20505";
        } else if (siName.contains("영광")) {
            regId_land = "11F20000";
            regId_ta = "21F20102";
        } else if (siName.contains("함평")) {
            regId_land = "11F20000";
            regId_ta = "21F20101";
        } else if (siName.contains("목포")) {
            regId_land = "11F20000";
            regId_ta = "21F20801";
        } else if (siName.contains("무안")) {
            regId_land = "11F20000";
            regId_ta = "21F20804";
        } else if (siName.contains("영암")) {
            regId_land = "11F20000";
            regId_ta = "21F20802";
        } else if (siName.contains("진도")) {
            regId_land = "11F20000";
            regId_ta = "21F20201";
        } else if (siName.contains("신안")) {
            regId_land = "11F20000";
            regId_ta = "21F20803";
        } else if (siName.contains("흑산도")) {
            regId_land = "11F20000";
            regId_ta = "11F20701";
        } else if (siName.contains("순천")) {
            regId_land = "11F20000";
            regId_ta = "11F20603";
        } else if (siName.contains("광양")) {
            regId_land = "11F20000";
            regId_ta = "11F20402";
        } else if (siName.contains("구례")) {
            regId_land = "11F20000";
            regId_ta = "11F20601";
        } else if (siName.contains("곡성")) {
            regId_land = "11F20000";
            regId_ta = "11F20602";
        } else if (siName.contains("완도")) {
            regId_land = "11F20000";
            regId_ta = "11F20301";
        } else if (siName.contains("강진")) {
            regId_land = "11F20000";
            regId_ta = "11F20303";
        } else if (siName.contains("장흥")) {
            regId_land = "11F20000";
            regId_ta = "11F20304";
        } else if (siName.contains("해남")) {
            regId_land = "11F20000";
            regId_ta = "11F20302";
        } else if (siName.contains("여수")) {
            regId_land = "11F20000";
            regId_ta = "11F20401";
        } else if (siName.contains("고흥")) {
            regId_land = "11F20000";
            regId_ta = "11F20403";
        } else if (siName.contains("보성")) {
            regId_land = "11F20000";
            regId_ta = "11F20404";
        } else if (siName.contains("전주")) {
            regId_land = "11F10000";
            regId_ta = "11F10201";
        } else if (siName.contains("익산")) {
            regId_land = "11F10000";
            regId_ta = "11F10202";
        } else if (siName.contains("군산")) {
            regId_land = "11F10000";
            regId_ta = "21F10501";
        } else if (siName.contains("정읍")) {
            regId_land = "11F10000";
            regId_ta = "11F10203";
        } else if (siName.contains("김제")) {
            regId_land = "11F10000";
            regId_ta = "21F10502";
        } else if (siName.contains("남원")) {
            regId_land = "11F10000";
            regId_ta = "11F10401";
        } else if (siName.contains("고창")) {
            regId_land = "11F10000";
            regId_ta = "21F10601";
        } else if (siName.contains("무주")) {
            regId_land = "11F10000";
            regId_ta = "11F10302";
        } else if (siName.contains("부안")) {
            regId_land = "11F10000";
            regId_ta = "21F10602";
        } else if (siName.contains("순창")) {
            regId_land = "11F10000";
            regId_ta = "11F10403";
        } else if (siName.contains("완주")) {
            regId_land = "11F10000";
            regId_ta = "11F10204";
        } else if (siName.contains("임실")) {
            regId_land = "11F10000";
            regId_ta = "11F10402";
        } else if (siName.contains("장수")) {
            regId_land = "11F10000";
            regId_ta = "11F10301";
        } else if (siName.contains("진안")) {
            regId_land = "11F10000";
            regId_ta = "11F10303";
        } else if (siName.contains("대전")) {
            regId_land = "11C20000";
            regId_ta = "11C20401";
        } else if (siName.contains("세종")) {
            regId_land = "11C20000";
            regId_ta = "11C20404";
        } else if (siName.contains("공주")) {
            regId_land = "11C20000";
            regId_ta = "11C20402";
        } else if (siName.contains("논산")) {
            regId_land = "11C20000";
            regId_ta = "11C20602";
        } else if (siName.contains("계룡")) {
            regId_land = "11C20000";
            regId_ta = "11C20403";
        } else if (siName.contains("금산")) {
            regId_land = "11C20000";
            regId_ta = "11C20601";
        } else if (siName.contains("천안")) {
            regId_land = "11C20000";
            regId_ta = "11C20301";
        } else if (siName.contains("아산")) {
            regId_land = "11C20000";
            regId_ta = "11C20302";
        } else if (siName.contains("예산")) {
            regId_land = "11C20000";
            regId_ta = "11C20303";
        } else if (siName.contains("서산")) {
            regId_land = "11C20000";
            regId_ta = "11C20101";
        } else if (siName.contains("태안")) {
            regId_land = "11C20000";
            regId_ta = "11C20102";
        } else if (siName.contains("당진")) {
            regId_land = "11C20000";
            regId_ta = "11C20103";
        } else if (siName.contains("홍성")) {
            regId_land = "11C20000";
            regId_ta = "11C20104";
        } else if (siName.contains("보령")) {
            regId_land = "11C20000";
            regId_ta = "11C20201";
        } else if (siName.contains("서천")) {
            regId_land = "11C20000";
            regId_ta = "11C20202";
        } else if (siName.contains("청양")) {
            regId_land = "11C20000";
            regId_ta = "11C20502";
        } else if (siName.contains("부여")) {
            regId_land = "11C20000";
            regId_ta = "11C20501";
        } else if (siName.contains("청주")) {
            regId_land = "11C10000";
            regId_ta = "11C10301";
        } else if (siName.contains("증평")) {
            regId_land = "11C10000";
            regId_ta = "11C10304";
        } else if (siName.contains("괴산")) {
            regId_land = "11C10000";
            regId_ta = "11C10303";
        } else if (siName.contains("진천")) {
            regId_land = "11C10000";
            regId_ta = "11C10102";
        } else if (siName.contains("충주")) {
            regId_land = "11C10000";
            regId_ta = "11C10101";
        } else if (siName.contains("음성")) {
            regId_land = "11C10000";
            regId_ta = "11C10103";
        } else if (siName.contains("제천")) {
            regId_land = "11C10000";
            regId_ta = "11C10201";
        } else if (siName.contains("단양")) {
            regId_land = "11C10000";
            regId_ta = "11C10202";
        } else if (siName.contains("보은")) {
            regId_land = "11C10000";
            regId_ta = "11C10302";
        } else if (siName.contains("옥천")) {
            regId_land = "11C10000";
            regId_ta = "11C10403";
        } else if (siName.contains("영동")) {
            regId_land = "11C10000";
            regId_ta = "11C10402";
        } else if (siName.contains("추풍령")) {
            regId_land = "11C10000";
            regId_ta = "11C10401";
        } else if (siName.contains("철원")) {
            regId_land = "11D10000";
            regId_ta = "11D10101";
        } else if (siName.contains("화천")) {
            regId_land = "11D10000";
            regId_ta = "11D10102";
        } else if (siName.contains("인제")) {
            regId_land = "11D10000";
            regId_ta = "11D10201";
        } else if (siName.contains("양구")) {
            regId_land = "11D10000";
            regId_ta = "11D10202";
        } else if (siName.contains("춘천")) {
            regId_land = "11D10000";
            regId_ta = "11D10301";
        } else if (siName.contains("홍천")) {
            regId_land = "11D10000";
            regId_ta = "11D10302";
        } else if (siName.contains("원주")) {
            regId_land = "11D10000";
            regId_ta = "11D10401";
        } else if (siName.contains("횡성")) {
            regId_land = "11D10000";
            regId_ta = "11D10402";
        } else if (siName.contains("영월")) {
            regId_land = "11D10000";
            regId_ta = "11D10501";
        } else if (siName.contains("정선")) {
            regId_land = "11D10000";
            regId_ta = "11D10502";
        } else if (siName.contains("평창")) {
            regId_land = "11D10000";
            regId_ta = "11D10503";
        } else if (siName.contains("대관령")) {
            regId_land = "11D20000";
            regId_ta = "11D20201";
        } else if (siName.contains("속초")) {
            regId_land = "11D20000";
            regId_ta = "11D20401";
        } else if (siName.contains("고성")) {
            regId_land = "11D20000";
            regId_ta = "11D20402";
        } else if (siName.contains("양양")) {
            regId_land = "11D20000";
            regId_ta = "11D20403";
        } else if (siName.contains("강릉")) {
            regId_land = "11D20000";
            regId_ta = "11D20501";
        } else if (siName.contains("동해")) {
            regId_land = "11D20000";
            regId_ta = "11D20601";
        } else if (siName.contains("삼척")) {
            regId_land = "11D20000";
            regId_ta = "11D20602";
        } else if (siName.contains("태백")) {
            regId_land = "11D20000";
            regId_ta = "11D20301";
        } else if (siName.contains("제주")) {
            regId_land = "11G00000";
            regId_ta = "11G00201";
        } else if (siName.contains("서귀포")) {
            regId_land = "11G00000";
            regId_ta = "11G00401";
        } else if (siName.contains("성산")) {
            regId_land = "11G00000";
            regId_ta = "11G00101";
        } else if (siName.contains("고산")) {
            regId_land = "11G00000";
            regId_ta = "11G00501";
        } else if (siName.contains("성판악")) {
            regId_land = "11G00000";
            regId_ta = "11G00302";
        } else if (siName.contains("이어도")) {
            regId_land = "11G00000";
            regId_ta = "11G00601";
        } else if (siName.contains("추자도")) {
            regId_land = "11G00000";
            regId_ta = "11G00800";
        }
    }

    private static void runJustBeforeBeingDrawn(final View view, final Runnable runnable) {
        final ViewTreeObserver vto = view.getViewTreeObserver();
        final ViewTreeObserver.OnPreDrawListener preDrawListener = new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                runnable.run();
                final ViewTreeObserver vto = view.getViewTreeObserver();
                vto.removeOnPreDrawListener(this);
                return true;
            }
        };
        vto.addOnPreDrawListener(preDrawListener);
    }

    private void start_V_World() {
        String url_getSiName = "http://api.vworld.kr/req/data?service=data&request=GetFeature&data=LT_C_ADSIGG_INFO&key=" + getResources().getString(R.string.v_world_key) + "&format=xml&size=1000&domain=A663FCD4-2951-3644-911E-E0C2D7D5CC6E&geomFilter=POINT(" + longitude + "%20" + latitude + ")";
        new V_World_getSiName(url_getSiName).execute();
    }

    private class V_World_getSiName extends AsyncTask<Void, Void, String> {

        private String url;

        public V_World_getSiName(String url) {
            this.url = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            btn_menu = findViewById(R.id.btn_menu);
            btn_menu.setVisibility(View.INVISIBLE);
            btn_place_sel = findViewById(R.id.btn_place_sel);
            btn_place_sel.setVisibility(View.INVISIBLE);
            v_loading_bg = findViewById(R.id.v_loading_bg);
            v_loading_bg.setVisibility(View.VISIBLE);
            pb_loading_progress = findViewById(R.id.pb_loading_progress);
            pb_loading_progress.setVisibility(View.VISIBLE);
            tv_loading_announce = findViewById(R.id.tv_loading_announce);
            tv_loading_announce.setVisibility(View.VISIBLE);
            mAdView = findViewById(R.id.adView);
            mAdView.setVisibility(View.INVISIBLE);
        }

        @Override
        protected String doInBackground(Void... params) {
            Document doc = null;
            DocumentBuilderFactory dbFactoty = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = null;

            try {
                dBuilder = dbFactoty.newDocumentBuilder();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }

            try {
                doc = dBuilder.parse(url);
            } catch (IOException | SAXException e) {
                e.printStackTrace();
            }

            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("gml:featureMember");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;

                    siName = getTagValue("full_nm", eElement);
                }
            }

            return null;
        }

        private String getTagValue(String tag, Element eElement) {
            NodeList nlList = eElement.getElementsByTagName(tag).item(0).getChildNodes();
            Node nValue = nlList.item(0);
            if(nValue == null)
                return null;
            return nValue.getNodeValue();
        }

        @Override
        protected void onPostExecute(String str) {
            super.onPostExecute(str);
            String url_getFullName = "http://api.vworld.kr/req/data?service=data&request=GetFeature&data=LT_C_ADEMD_INFO&key=" + getResources().getString(R.string.v_world_key) + "&format=xml&size=1000&domain=A663FCD4-2951-3644-911E-E0C2D7D5CC6E&geomFilter=POINT(" + longitude + "%20" + latitude + ")";
            new V_World_getFullName(url_getFullName).execute();
        }
    }

    private class V_World_getFullName extends AsyncTask<Void, Void, String> {

        private String url;

        public V_World_getFullName(String url) {
            this.url = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            Document doc = null;
            DocumentBuilderFactory dbFactoty = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = null;

            try {
                dBuilder = dbFactoty.newDocumentBuilder();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }

            try {
                doc = dBuilder.parse(url);
            } catch (IOException | SAXException e) {
                e.printStackTrace();
            }

            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("gml:featureMember");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;

                    fullName = getTagValue("full_nm", eElement);
                }
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable(){
                        @Override
                        public void run() {
                            TextView tv_place = findViewById(R.id.tv_place);
                            tv_place.setText(fullName);
                        }
                    });
                }
            }).start();

            return null;
        }

        private String getTagValue(String tag, Element eElement) {
            NodeList nlList = eElement.getElementsByTagName(tag).item(0).getChildNodes();
            Node nValue = nlList.item(0);
            if(nValue == null)
                return null;
            return nValue.getNodeValue();
        }

        @Override
        protected void onPostExecute(String str) {
            super.onPostExecute(str);
            start_WeatherKorea_current();
        }
    }

    private void start_WeatherKorea_current() {
        Date for_convert_base = new Date();
        SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
        SimpleDateFormat HHmm = new SimpleDateFormat("HHmm", Locale.KOREA);
        String base_date  = yyyyMMdd.format(for_convert_base);
        String base_time = HHmm.format(for_convert_base);
        String fcstTime = null;
        double double_set_base_time = Double.parseDouble(base_time);

        if (double_set_base_time >= 0 && double_set_base_time <= 44) {
            Date yesterday_Date = new Date();
            yesterday_Date = new Date(yesterday_Date.getTime()+(1000*60*60*24*-1));
            base_date = yyyyMMdd.format(yesterday_Date);
            base_time = "2330";
            fcstTime = "0000";
        } else if (double_set_base_time >= 45 && double_set_base_time <= 144) {
            base_time = "0030";
            fcstTime = "0100";
        } else if (double_set_base_time >= 145 && double_set_base_time <= 244) {
            base_time = "0130";
            fcstTime = "0200";
        } else if (double_set_base_time >= 245 && double_set_base_time <= 344) {
            base_time = "0230";
            fcstTime = "0300";
        } else if (double_set_base_time >= 345 && double_set_base_time <= 444) {
            base_time = "0330";
            fcstTime = "0400";
        } else if (double_set_base_time >= 445 && double_set_base_time <= 544) {
            base_time = "0430";
            fcstTime = "0500";
        } else if (double_set_base_time >= 545 && double_set_base_time <= 644) {
            base_time = "0530";
            fcstTime = "0600";
        } else if (double_set_base_time >= 645 && double_set_base_time <= 744) {
            base_time = "0630";
            fcstTime = "0700";
        } else if (double_set_base_time >= 745 && double_set_base_time <= 844) {
            base_time = "0730";
            fcstTime = "0800";
        } else if (double_set_base_time >= 845 && double_set_base_time <= 944) {
            base_time = "0830";
            fcstTime = "0900";
        } else if (double_set_base_time >= 945 && double_set_base_time <= 1044) {
            base_time = "0930";
            fcstTime = "1000";
        } else if (double_set_base_time >= 1045 && double_set_base_time <= 1144) {
            base_time = "1030";
            fcstTime = "1100";
        } else if (double_set_base_time >= 1145 && double_set_base_time <= 1244) {
            base_time = "1130";
            fcstTime = "1200";
        } else if (double_set_base_time >= 1245 && double_set_base_time <= 1344) {
            base_time = "1230";
            fcstTime = "1300";
        } else if (double_set_base_time >= 1345 && double_set_base_time <= 1444) {
            base_time = "1330";
            fcstTime = "1400";
        } else if (double_set_base_time >= 1445 && double_set_base_time <= 1544) {
            base_time = "1430";
            fcstTime = "1500";
        } else if (double_set_base_time >= 1545 && double_set_base_time <= 1644) {
            base_time = "1530";
            fcstTime = "1600";
        } else if (double_set_base_time >= 1645 && double_set_base_time <= 1744) {
            base_time = "1630";
            fcstTime = "1700";
        } else if (double_set_base_time >= 1745 && double_set_base_time <= 1844) {
            base_time = "1730";
            fcstTime = "1800";
        } else if (double_set_base_time >= 1845 && double_set_base_time <= 1944) {
            base_time = "1830";
            fcstTime = "1900";
        } else if (double_set_base_time >= 1945 && double_set_base_time <= 2044) {
            base_time = "1930";
            fcstTime = "2000";
        } else if (double_set_base_time >= 2045 && double_set_base_time <= 2144) {
            base_time = "2030";
            fcstTime = "2100";
        } else if (double_set_base_time >= 2145 && double_set_base_time <= 2244) {
            base_time = "2130";
            fcstTime = "2200";
        } else if (double_set_base_time >= 2245 && double_set_base_time <= 2344) {
            base_time = "2230";
            fcstTime = "2300";
        } else if (double_set_base_time >= 2345 && double_set_base_time <= 2359) {
            base_time = "2330";
            fcstTime = "0000";
        }

        String url_current_sky = "http://apis.data.go.kr/1360000/VilageFcstInfoService/getUltraSrtFcst?serviceKey=" + getResources().getString(R.string.data_key) + "&numOfRows=100&pageNo=1&base_date=" + base_date + "&base_time=" + base_time + "&nx=" + grid_X + "&ny=" + grid_Y;
        new WeatherKorea_GetSky(url_current_sky, fcstTime).execute();
    }

    private class WeatherKorea_GetSky extends AsyncTask<Void, Void, String> {

        private String url;
        private String fcsttime;

        public WeatherKorea_GetSky(String url, String fcsttime) {
            this.url = url;
            this.fcsttime = fcsttime;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            Document doc = null;
            DocumentBuilderFactory dbFactoty = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = null;

            try {
                dBuilder = dbFactoty.newDocumentBuilder();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }

            try {
                doc = dBuilder.parse(url);
            } catch (IOException | SAXException e) {
                e.printStackTrace();
            }

            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("item");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;

                    if (Objects.equals(getTagValue("category", eElement), "SKY") && Objects.equals(getTagValue("fcstTime", eElement), fcsttime)) {
                        SKYValue = getTagValue("fcstValue", eElement);
                    }
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Date for_convert_base = new Date();
            SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
            SimpleDateFormat HHmm = new SimpleDateFormat("HHmm", Locale.KOREA);
            String base_date  = yyyyMMdd.format(for_convert_base);
            String base_time = HHmm.format(for_convert_base);
            double double_set_base_time = Double.parseDouble(base_time);

            if (double_set_base_time >= 0 && double_set_base_time <= 40) {
                Date yesterday_Date = new Date();
                yesterday_Date = new Date(yesterday_Date.getTime()+(1000*60*60*24*-1));
                base_date = yyyyMMdd.format(yesterday_Date);
                base_time = "2300";
            } else if (double_set_base_time >= 41 && double_set_base_time <= 140) {
                base_time = "0000";
            } else if (double_set_base_time >= 141 && double_set_base_time <= 240) {
                base_time = "0100";
            } else if (double_set_base_time >= 241 && double_set_base_time <= 340) {
                base_time = "0200";
            } else if (double_set_base_time >= 341 && double_set_base_time <= 440) {
                base_time = "0300";
            } else if (double_set_base_time >= 441 && double_set_base_time <= 540) {
                base_time = "0400";
            } else if (double_set_base_time >= 541 && double_set_base_time <= 640) {
                base_time = "0500";
            } else if (double_set_base_time >= 641 && double_set_base_time <= 740) {
                base_time = "0600";
            } else if (double_set_base_time >= 741 && double_set_base_time <= 840) {
                base_time = "0700";
            } else if (double_set_base_time >= 841 && double_set_base_time <= 940) {
                base_time = "0800";
            } else if (double_set_base_time >= 941 && double_set_base_time <= 1040) {
                base_time = "0900";
            } else if (double_set_base_time >= 1041 && double_set_base_time <= 1140) {
                base_time = "1000";
            } else if (double_set_base_time >= 1141 && double_set_base_time <= 1240) {
                base_time = "1100";
            } else if (double_set_base_time >= 1241 && double_set_base_time <= 1340) {
                base_time = "1200";
            } else if (double_set_base_time >= 1341 && double_set_base_time <= 1440) {
                base_time = "1300";
            } else if (double_set_base_time >= 1441 && double_set_base_time <= 1540) {
                base_time = "1400";
            } else if (double_set_base_time >= 1541 && double_set_base_time <= 1640) {
                base_time = "1500";
            } else if (double_set_base_time >= 1641 && double_set_base_time <= 1740) {
                base_time = "1600";
            } else if (double_set_base_time >= 1741 && double_set_base_time <= 1840) {
                base_time = "1700";
            } else if (double_set_base_time >= 1841 && double_set_base_time <= 1940) {
                base_time = "1800";
            } else if (double_set_base_time >= 1941 && double_set_base_time <= 2040) {
                base_time = "1900";
            } else if (double_set_base_time >= 2041 && double_set_base_time <= 2140) {
                base_time = "2000";
            } else if (double_set_base_time >= 2141 && double_set_base_time <= 2240) {
                base_time = "2100";
            } else if (double_set_base_time >= 2241 && double_set_base_time <= 2340) {
                base_time = "2200";
            } else if (double_set_base_time >= 2341 && double_set_base_time <= 2359) {
                base_time = "2300";
            }

            String url_current_weather = "http://apis.data.go.kr/1360000/VilageFcstInfoService/getUltraSrtNcst?serviceKey=" + getResources().getString(R.string.data_key) + "&numOfRows=10&pageNo=1&base_date=" + base_date + "&base_time=" + base_time + "&nx=" + grid_X + "&ny=" + grid_Y;
            new WeatherKorea_Weather(url_current_weather).execute();
        }
    }

    private class WeatherKorea_Weather extends AsyncTask<Void, Void, String> {

        private String url;

        public WeatherKorea_Weather(String url) {
            this.url = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            Document doc = null;
            DocumentBuilderFactory dbFactoty = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = null;

            try {
                dBuilder = dbFactoty.newDocumentBuilder();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }

            try {
                doc = dBuilder.parse(url);
            } catch (IOException | SAXException e) {
                e.printStackTrace();
            }

            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("item");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;

                    if (Objects.equals(getTagValue("category", eElement), "PTY")) {
                        PTYValue = getTagValue("obsrValue", eElement);
                    }
                    if (Objects.equals(getTagValue("category", eElement), "T1H")) {
                        T1HValue = getTagValue("obsrValue", eElement);
                    }
                    if (Objects.equals(getTagValue("category", eElement), "RN1")) {
                        RN1Value = getTagValue("obsrValue", eElement);
                        if(RN1Value.equals("0")) {
                            RN1Value = "0.0";
                        }
                    }
                    if (Objects.equals(getTagValue("category", eElement), "REH")) {
                        REHValue = getTagValue("obsrValue", eElement);
                    }
                    if (Objects.equals(getTagValue("category", eElement), "VEC")) {
                        VECValue = getTagValue("obsrValue", eElement);
                    }
                    if (Objects.equals(getTagValue("category", eElement), "WSD")) {
                        WSDValue = getTagValue("obsrValue", eElement);
                    }
                }
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable(){
                        @Override
                        public void run() {
                            try {
                                View for_bg = findViewById(R.id.v_weather_bg);
                                ImageView for_wtr = findViewById(R.id.iv_weather);

                                Date now = new Date();
                                SimpleDateFormat HHmm = new SimpleDateFormat("HHmm", Locale.KOREA);
                                String time_now = HHmm.format(now);

                                if (PTYValue.equals("0")) {
                                    switch (SKYValue) {
                                        case "1":
                                            if (Double.parseDouble(time_now) >= Double.parseDouble(sunrise) && Double.parseDouble(time_now) < Double.parseDouble(sunset)) {
                                                for_bg.setBackgroundResource(R.drawable.sunny_background);
                                                for_wtr.setBackgroundResource(R.drawable.sunny_icon);
                                            } else {
                                                for_bg.setBackgroundResource(R.drawable.sunny_night_background);
                                                for_wtr.setBackgroundResource(R.drawable.sunny_night_icon);
                                            }
                                            break;
                                        case "3":
                                            if (Double.parseDouble(time_now) >= Double.parseDouble(sunrise) && Double.parseDouble(time_now) < Double.parseDouble(sunset)) {
                                                for_bg.setBackgroundResource(R.drawable.cloudy1_background);
                                                for_wtr.setBackgroundResource(R.drawable.cloudy1_icon);
                                            } else {
                                                for_bg.setBackgroundResource(R.drawable.cloudy1_night_background);
                                                for_wtr.setBackgroundResource(R.drawable.cloudy1_night_icon);
                                            }
                                            break;
                                        case "4":
                                            for_bg.setBackgroundResource(R.drawable.cloudy2_background);
                                            for_wtr.setBackgroundResource(R.drawable.cloudy2_icon);
                                            break;
                                    }
                                } else {
                                    switch (PTYValue) {
                                        case "1":
                                        case "4":
                                        case "5":
                                            for_bg.setBackgroundResource(R.drawable.rainy_background);
                                            for_wtr.setBackgroundResource(R.drawable.rainy_icon);
                                            break;
                                        case "2":
                                        case "6":
                                            for_bg.setBackgroundResource(R.drawable.snowrainy_background);
                                            for_wtr.setBackgroundResource(R.drawable.snowrainy_icon);
                                            break;
                                        case "3":
                                        case "7":
                                            for_bg.setBackgroundResource(R.drawable.snow_background);
                                            for_wtr.setBackgroundResource(R.drawable.snow_icon);
                                            break;
                                    }
                                }

                                TextView for_tmp = findViewById(R.id.tv_temperature);
                                for_tmp.setText(" " + T1HValue + "°");

                                fragment1.setRainfall("1시간 강수량 : " + RN1Value + "mm");
                                fragment1.setHumidity("습도 : " + REHValue + "%");
                                fragment1.setWindSpeed("풍속 : " + WSDValue + "m/s");
                                fragment1.setWindDirection(VECValue);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }).start();

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            start_WeatherKorea_forecast();
        }
    }

    private void start_WeatherKorea_forecast() {
        Date for_convert_base = new Date();
        SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
        SimpleDateFormat HHmm = new SimpleDateFormat("HHmm", Locale.KOREA);
        String base_date  = yyyyMMdd.format(for_convert_base);
        String base_time = HHmm.format(for_convert_base);
        double double_set_base_time = Double.parseDouble(base_time);

        if (double_set_base_time >= 0 && double_set_base_time <= 210) {
            Date yesterday_Date = new Date();
            yesterday_Date = new Date(yesterday_Date.getTime()+(1000*60*60*24*-1));
            base_date = yyyyMMdd.format(yesterday_Date);
            base_time = "2300";
        } else if (double_set_base_time >= 211 && double_set_base_time <= 510) {
            base_time = "0200";
        } else if (double_set_base_time >= 511 && double_set_base_time <= 810) {
            base_time = "0500";
        } else if (double_set_base_time >= 811 && double_set_base_time <= 1110) {
            base_time = "0800";
        } else if (double_set_base_time >= 1111 && double_set_base_time <= 1410) {
            base_time = "1100";
        } else if (double_set_base_time >= 1411 && double_set_base_time <= 1710) {
            base_time = "1400";
        } else if (double_set_base_time >= 1711 && double_set_base_time <= 2010) {
            base_time = "1700";
        } else if (double_set_base_time >= 2011 && double_set_base_time <= 2310) {
            base_time = "2000";
        } else if (double_set_base_time >= 2311 && double_set_base_time <= 2359) {
            base_time = "2300";
        }

        String url_short_forecast = "http://apis.data.go.kr/1360000/VilageFcstInfoService/getVilageFcst?serviceKey=" + getResources().getString(R.string.data_key) + "&numOfRows=500&pageNo=1&base_date="
                + base_date + "&base_time=" + base_time + "&nx=" + grid_X + "&ny=" + grid_Y;
        new WeatherKorea_Short_Forecast(url_short_forecast).execute();
    }

    private class WeatherKorea_Short_Forecast extends AsyncTask<Void, Void, String> {

        private String url;

        public WeatherKorea_Short_Forecast(String url) {
            this.url = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            Document doc = null;
            DocumentBuilderFactory dbFactoty = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = null;

            try {
                dBuilder = dbFactoty.newDocumentBuilder();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }

            try {
                doc = dBuilder.parse(url);
            } catch (IOException | SAXException e) {
                e.printStackTrace();
            }

            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("item");

            count = 0;
            count2 = 0;
            count3 = 0;
            count4 = 0;

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    if (Objects.equals(getTagValue("category", eElement), "POP")) {
                        FDValue_sf[count] = getTagValue("fcstDate", eElement);
                        FTValue_sf[count] = getTagValue("fcstTime", eElement);
                        POPValue_sf[count] = getTagValue("fcstValue", eElement);
                        count++;
                    }
                    if (Objects.equals(getTagValue("category", eElement), "SKY")) {
                        SKYValue_sf[count2] = getTagValue("fcstValue", eElement);
                        count2++;
                    }
                    if (Objects.equals(getTagValue("category", eElement), "T3H")) {
                        T3HValue_sf[count3] = getTagValue("fcstValue", eElement);
                        count3++;
                    }
                    if (Objects.equals(getTagValue("category", eElement), "PTY")) {
                        PTYValue_sf[count4] = getTagValue("fcstValue", eElement);
                        count4++;
                    }
                }
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable(){
                        @Override
                        public void run() {
                            RecyclerView mRecyclerView = findViewById(R.id.rv_short_forecast);
                            RecyclerView.LayoutManager mLayoutManager;
                            mRecyclerView.setHasFixedSize(true);
                            mLayoutManager = new LinearLayoutManager(WeatherActivity.this);
                            mRecyclerView.setLayoutManager(mLayoutManager);

                            ArrayList<ShortForecastListItem> SF_list = new ArrayList<>();
                            for(int i = 0; i<count; i++) {
                                SF_list.add(new ShortForecastListItem(FDValue_sf[i], FTValue_sf[i], POPValue_sf[i], SKYValue_sf[i], T3HValue_sf[i], PTYValue_sf[i]));
                            }

                            ShortForecastListAdapter shortForecastListAdapter = new ShortForecastListAdapter(SF_list);
                            mRecyclerView.setAdapter(shortForecastListAdapter);
                            shortForecastListAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }).start();

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            setRegId();

            Date today = new Date();
            SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
            SimpleDateFormat HHmm = new SimpleDateFormat("HHmm", Locale.KOREA);

            String tm_Fc = "";
            String temp1  = yyyyMMdd.format(today);
            String temp2 = HHmm.format(today);
            double double_temp2 = Double.parseDouble(temp2);

            if (double_temp2 >= 0 && double_temp2 < 600) {
                Date yesterday_Date = new Date();
                yesterday_Date = new Date(yesterday_Date.getTime()+(1000*60*60*24*-1));
                temp1 = yyyyMMdd.format(yesterday_Date);
                temp2 = "1800";
                tm_Fc = temp1 + temp2;
            } else if(double_temp2 >= 600 && double_temp2 < 1800) {
                temp2 = "0600";
                tm_Fc = temp1 + temp2;
            } else if(double_temp2 >= 1800 && double_temp2 < 2400) {
                temp2 = "1800";
                tm_Fc = temp1 + temp2;
            }

            String url_middle_land = "http://apis.data.go.kr/1360000/MidFcstInfoService/getMidLandFcst?serviceKey=" + getResources().getString(R.string.data_key) + "&numOfRows=10&pageNo=1&regId=" + regId_land + "&tmFc=" + tm_Fc;
            String url_middle_ta = "http://apis.data.go.kr/1360000/MidFcstInfoService/getMidTa?serviceKey=" + getResources().getString(R.string.data_key) + "&numOfRows=10&pageNo=1&regId=" + regId_ta + "&tmFc=" + tm_Fc;
            new WeatherKorea_Middle_Forecast(url_middle_land, url_middle_ta).execute();
        }
    }

    private class WeatherKorea_Middle_Forecast extends AsyncTask<Void, Void, String> {

        private String url_land;
        private String url_ta;

        public WeatherKorea_Middle_Forecast(String url_land, String url_ta) {
            this.url_land = url_land;
            this.url_ta = url_ta;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            Document doc = null;
            DocumentBuilderFactory dbFactoty = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = null;

            try {
                dBuilder = dbFactoty.newDocumentBuilder();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }

            try {
                doc = dBuilder.parse(url_land);
            } catch (IOException | SAXException e) {
                e.printStackTrace();
            }

            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("item");

            Node nNode = nList.item(0);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;

                Date temp_date = new Date();
                SimpleDateFormat MMdd = new SimpleDateFormat("MMdd", Locale.KOREA);
                SimpleDateFormat HHmm = new SimpleDateFormat("HHmm", Locale.KOREA);

                Date d_plus2 = new Date();
                d_plus2 = new Date(d_plus2.getTime()+(1000*60*60*24*2));
                String plus2 = MMdd.format(d_plus2);
                Date d_plus3 = new Date();
                d_plus3 = new Date(d_plus3.getTime()+(1000*60*60*24*3));
                String plus3 = MMdd.format(d_plus3);
                Date d_plus4 = new Date();
                d_plus4 = new Date(d_plus4.getTime()+(1000*60*60*24*4));
                String plus4 = MMdd.format(d_plus4);
                Date d_plus5 = new Date();
                d_plus5 = new Date(d_plus5.getTime()+(1000*60*60*24*5));
                String plus5 = MMdd.format(d_plus5);
                Date d_plus6 = new Date();
                d_plus6 = new Date(d_plus6.getTime()+(1000*60*60*24*6));
                String plus6 = MMdd.format(d_plus6);
                Date d_plus7 = new Date();
                d_plus7 = new Date(d_plus7.getTime()+(1000*60*60*24*7));
                String plus7 = MMdd.format(d_plus7);
                Date d_plus8 = new Date();
                d_plus8 = new Date(d_plus8.getTime()+(1000*60*60*24*8));
                String plus8 = MMdd.format(d_plus8);
                Date d_plus9 = new Date();
                d_plus9 = new Date(d_plus9.getTime()+(1000*60*60*24*9));
                String plus9 = MMdd.format(d_plus9);
                Date d_plus10 = new Date();
                d_plus10 = new Date(d_plus10.getTime()+(1000*60*60*24*10));
                String plus10 = MMdd.format(d_plus10);

                if (Double.parseDouble(HHmm.format(temp_date)) >= 0 && Double.parseDouble(HHmm.format(temp_date)) < 600) {
                    mf_date[0] = plus2;
                    mf_date[1] = plus3;
                    mf_date[2] = plus4;
                    mf_date[3] = plus5;
                    mf_date[4] = plus6;
                    mf_date[5] = plus7;
                    mf_date[6] = plus8;
                    mf_date[7] = plus9;
                } else {
                    mf_date[0] = plus3;
                    mf_date[1] = plus4;
                    mf_date[2] = plus5;
                    mf_date[3] = plus6;
                    mf_date[4] = plus7;
                    mf_date[5] = plus8;
                    mf_date[6] = plus9;
                    mf_date[7] = plus10;
                }

                wf_am[0] = getTagValue("wf3Am", eElement);
                wf_am[1] = getTagValue("wf4Am", eElement);
                wf_am[2] = getTagValue("wf5Am", eElement);
                wf_am[3] = getTagValue("wf6Am", eElement);
                wf_am[4] = getTagValue("wf7Am", eElement);
                wf_am[5] = getTagValue("wf8", eElement);
                wf_am[6] = getTagValue("wf9", eElement);
                wf_am[7] = getTagValue("wf10", eElement);

                wf_pm[0] = getTagValue("wf3Pm", eElement);
                wf_pm[1] = getTagValue("wf4Pm", eElement);
                wf_pm[2] = getTagValue("wf5Pm", eElement);
                wf_pm[3] = getTagValue("wf6Pm", eElement);
                wf_pm[4] = getTagValue("wf7Pm", eElement);
                wf_pm[5] = "";
                wf_pm[6] = "";
                wf_pm[7] = "";

                rnSt_am[0] = getTagValue("rnSt3Am", eElement);
                rnSt_am[1] = getTagValue("rnSt4Am", eElement);
                rnSt_am[2] = getTagValue("rnSt5Am", eElement);
                rnSt_am[3] = getTagValue("rnSt6Am", eElement);
                rnSt_am[4] = getTagValue("rnSt7Am", eElement);
                rnSt_am[5] = getTagValue("rnSt8", eElement);
                rnSt_am[6] = getTagValue("rnSt9", eElement);
                rnSt_am[7] = getTagValue("rnSt10", eElement);

                rnSt_pm[0] = getTagValue("rnSt3Pm", eElement);
                rnSt_pm[1] = getTagValue("rnSt4Pm", eElement);
                rnSt_pm[2] = getTagValue("rnSt5Pm", eElement);
                rnSt_pm[3] = getTagValue("rnSt6Pm", eElement);
                rnSt_pm[4] = getTagValue("rnSt7Pm", eElement);
                rnSt_pm[5] = "";
                rnSt_pm[6] = "";
                rnSt_pm[7] = "";
            }

            try {
                doc = dBuilder.parse(url_ta);
            } catch (IOException | SAXException e) {
                e.printStackTrace();
            }

            doc.getDocumentElement().normalize();
            NodeList nList2 = doc.getElementsByTagName("item");

            Node nNode2 = nList2.item(0);

            if (nNode2.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode2;

                ta_min[0] = getTagValue("taMin3", eElement);
                ta_min[1] = getTagValue("taMin4", eElement);
                ta_min[2] = getTagValue("taMin5", eElement);
                ta_min[3] = getTagValue("taMin6", eElement);
                ta_min[4] = getTagValue("taMin7", eElement);
                ta_min[5] = getTagValue("taMin8", eElement);
                ta_min[6] = getTagValue("taMin9", eElement);
                ta_min[7] = getTagValue("taMin10", eElement);

                ta_max[0] = getTagValue("taMax3", eElement);
                ta_max[1] = getTagValue("taMax4", eElement);
                ta_max[2] = getTagValue("taMax5", eElement);
                ta_max[3] = getTagValue("taMax6", eElement);
                ta_max[4] = getTagValue("taMax7", eElement);
                ta_max[5] = getTagValue("taMax8", eElement);
                ta_max[6] = getTagValue("taMax9", eElement);
                ta_max[7] = getTagValue("taMax10", eElement);
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable(){
                        @Override
                        public void run() {
                            RecyclerView mRecyclerView = findViewById(R.id.rv_middle_forecast);
                            RecyclerView.LayoutManager mLayoutManager;
                            mRecyclerView.setHasFixedSize(true);
                            mLayoutManager = new LinearLayoutManager(WeatherActivity.this);
                            mRecyclerView.setLayoutManager(mLayoutManager);

                            ArrayList<MiddleForecastListItem> MF_list = new ArrayList<>();
                            for(int i = 0; i <= 4; i++) {
                                MF_list.add(new MiddleForecastListItem(mf_date[i], wf_am[i], wf_pm[i], rnSt_am[i], rnSt_pm[i], ta_min[i], ta_max[i], 1));
                            }
                            for(int i = 5; i <= 7; i++) {
                                MF_list.add(new MiddleForecastListItem(mf_date[i], wf_am[i], wf_pm[i], rnSt_am[i], rnSt_pm[i], ta_min[i], ta_max[i], 2));
                            }

                            MiddleForecastListAdapter middleForecastListAdapter = new MiddleForecastListAdapter(MF_list);
                            mRecyclerView.setAdapter(middleForecastListAdapter);
                            middleForecastListAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }).start();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            start_AirKorea();
        }
    }

    private void start_AirKorea() {
        String url_parsing_getNearBy = "http://apis.data.go.kr/B552584/MsrstnInfoInqireSvc/getNearbyMsrstnList?tmX=" + tm_x + "&tmY=" + tm_y + "&returnType=xml&ServiceKey=" + getResources().getString(R.string.data_key);

        new AirKorea_GetNearBy(url_parsing_getNearBy).execute();
    }

    private class AirKorea_GetNearBy extends AsyncTask<Void, Void, String> {

        private String url;

        public AirKorea_GetNearBy(String url) {
            this.url = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            Document doc = null;
            DocumentBuilderFactory dbFactoty = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = null;

            try {
                dBuilder = dbFactoty.newDocumentBuilder();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }

            try {
                doc = dBuilder.parse(url);
            } catch (IOException | SAXException e) {
                e.printStackTrace();
            }

            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("item");
            Node nNode = nList.item(0);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;

                stationNameValue = getTagValue("stationName", eElement);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            String url_parsing_atmosphere = "http://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getMsrstnAcctoRltmMesureDnsty?stationName=" + stationNameValue + "&dataTerm=month&pageNo=1&numOfRows=100&returnType=xml&ServiceKey=" + getResources().getString(R.string.data_key);
            new AirKorea_Atmosphere(url_parsing_atmosphere).execute();
        }
    }

    private class AirKorea_Atmosphere extends AsyncTask<Void, Void, String> {

        private String url;

        public AirKorea_Atmosphere(String url) {
            this.url = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            Document doc = null;
            DocumentBuilderFactory dbFactoty = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = null;

            try {
                dBuilder = dbFactoty.newDocumentBuilder();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }

            try {
                doc = dBuilder.parse(url);
            } catch (IOException | SAXException e) {
                e.printStackTrace();
            }

            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("item");
            Node nNode = nList.item(0);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;

                try {
                    khaiValue = getTagValue("khaiValue", eElement);
                } catch (Exception e) {
                    khaiValue = "-";
                }
                try {
                    pm10Value = getTagValue("pm10Value", eElement);
                } catch (Exception e) {
                    pm10Value = "-";
                }
                try {
                    pm25Value = getTagValue("pm25Value", eElement);
                } catch (Exception e) {
                    pm25Value = "-";
                }
                try {
                    so2Value = getTagValue("so2Value", eElement);
                } catch (Exception e) {
                    so2Value = "-";
                }
                try {
                    coValue = getTagValue("coValue", eElement);
                } catch (Exception e) {
                    coValue = "-";
                }
                try {
                    o3Value = getTagValue("o3Value", eElement);
                } catch (Exception e) {
                    o3Value = "-";
                }
                try {
                    no2Value = getTagValue("no2Value", eElement);
                } catch (Exception e) {
                    no2Value = "-";
                }
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!khaiValue.equals("-")) {
                                fragment2.setKhai(khaiValue);
                                fragment2.setKhaiFace(Double.parseDouble(khaiValue));
                            }
                            if (!pm10Value.equals("-")) {
                                fragment2.setPM10(pm10Value);
                                fragment2.setPM10Face(Double.parseDouble(pm10Value));
                            }
                            if (!pm25Value.equals("-")) {
                                fragment2.setPM25(pm25Value);
                                fragment2.setPM25Face(Double.parseDouble(pm25Value));
                            }
                            if (!o3Value.equals("-")) {
                                fragment2.setO3(o3Value);
                                fragment2.setO3Face(Double.parseDouble(o3Value));
                            }
                            if (!coValue.equals("-")) {
                                fragment2.setCO(coValue);
                                fragment2.setCOFace(Double.parseDouble(coValue));
                            }
                            if (!so2Value.equals("-")) {
                                fragment2.setSO2(so2Value);
                                fragment2.setSO2Face(Double.parseDouble(so2Value));
                            }
                            if (!no2Value.equals("-")) {
                                fragment2.setNO2(no2Value);
                                fragment2.setNO2Face(Double.parseDouble(no2Value));
                            }
                        }
                    });
                }
            }).start();

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            btn_menu = findViewById(R.id.btn_menu);
            btn_menu.setVisibility(View.VISIBLE);
            btn_place_sel = findViewById(R.id.btn_place_sel);
            btn_place_sel.setVisibility(View.VISIBLE);
            v_loading_bg = findViewById(R.id.v_loading_bg);
            v_loading_bg.setVisibility(View.INVISIBLE);
            pb_loading_progress = findViewById(R.id.pb_loading_progress);
            pb_loading_progress.setVisibility(View.INVISIBLE);
            tv_loading_announce = findViewById(R.id.tv_loading_announce);
            tv_loading_announce.setVisibility(View.INVISIBLE);
            mAdView = findViewById(R.id.adView);
            mAdView.setVisibility(View.VISIBLE);
        }
    }

}