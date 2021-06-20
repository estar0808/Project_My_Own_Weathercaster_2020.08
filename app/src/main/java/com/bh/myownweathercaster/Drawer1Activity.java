package com.bh.myownweathercaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class Drawer1Activity extends AppCompatActivity {

    private FragmentSatellite fragment_satellite = new FragmentSatellite();
    private FragmentRadar fragment_radar = new FragmentRadar();
    private ViewFlipper vf_satellite;
    private ViewFlipper vf_radar;

    private WeatherKorea_Satellite weatherKorea_satellite;
    private WeatherKorea_Radar weatherKorea_radar;

    private int int_totalCount = 0;
    private int int_totalCount2 = 0;
    private String[] SatelliteUrl = new String[24];
    private int int_totalCount3 = 0;
    private int int_totalCount4 = 0;
    private String[] RadarUrl = new String[24];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer1);

        getWindow().setStatusBarColor(Color.rgb(246,246,246));

        ViewPager pager = findViewById(R.id.vp_drawer1);
        pager.setOffscreenPageLimit(2);

        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());
        adapter.addItem(0, fragment_satellite);
        adapter.addItem(1, fragment_radar);
        pager.setAdapter(adapter);

        for(int i = 0; i < 24; i++) {
            SatelliteUrl[i] = "";
            RadarUrl[i] = "";
        }

        start_WeatherKorea_Satellite();
        start_WeatherKorea_Radar();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0,0);
        weatherKorea_satellite.cancel(true);
        weatherKorea_radar.cancel(true);
    }

    private String getTagValue(String tag, Element eElement) {
        NodeList nlList = eElement.getElementsByTagName(tag).item(0).getChildNodes();
        Node nValue = nlList.item(0);
        if (nValue == null) return null;
        return nValue.getNodeValue();
    }

    private void start_WeatherKorea_Satellite() {
        Date today = new Date();
        Date utc_today = new Date(today.getTime()+(1000*60*60*-9));
        SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
        String str_utc_today = yyyyMMdd.format(utc_today);

        String url_satellite;
        url_satellite = "http://apis.data.go.kr/1360000/SatlitImgInfoService/getInsightSatlit?serviceKey=" + getResources().getString(R.string.data_key) + "&numOfRows=10&pageNo=1&sat=g2&data=ir105&area=ko&time=" + str_utc_today;

        weatherKorea_satellite = new WeatherKorea_Satellite(url_satellite);
        weatherKorea_satellite.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private class WeatherKorea_Satellite extends AsyncTask<Void, Void, String> {

        private String url;

        public WeatherKorea_Satellite(String url) {
            this.url = url;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
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

                NodeList nList = doc.getElementsByTagName("body");
                Node nNode = nList.item(0);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    String totalCount = getTagValue("totalCount", eElement);
                    int_totalCount = Integer.parseInt(totalCount) - 1;
                }

                nList = doc.getElementsByTagName("item");
                nNode = nList.item(0);

                final int[] temp = {0};
                int temp2 = 0;

                String satelliteUrl_Value;
                if (int_totalCount >= 23) {
                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        for (int i = 23; i >= 0; i--) {
                            satelliteUrl_Value = doc.getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(1).getChildNodes().item(0).getChildNodes().item(int_totalCount - i).getChildNodes().item(0).getNodeValue();
                            SatelliteUrl[23 - i] = satelliteUrl_Value;
                        }
                    }
                } else {
                    for (int i = 23; i > 22 - int_totalCount; i--) {
                        satelliteUrl_Value = doc.getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(1).getChildNodes().item(0).getChildNodes().item(int_totalCount - temp[0]).getChildNodes().item(0).getNodeValue();
                        SatelliteUrl[i] = satelliteUrl_Value;
                        temp[0]++;
                    }

                    Date today = new Date();
                    Date utc_yesterday = new Date(today.getTime() + (1000 * 60 * 60 * -9) + (1000 * 60 * 60 * 24 * -1));
                    SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
                    String str_utc_yesterday = yyyyMMdd.format(utc_yesterday);

                    String url_yesterday = "http://apis.data.go.kr/1360000/SatlitImgInfoService/getInsightSatlit?serviceKey=" + getResources().getString(R.string.data_key) + "&numOfRows=10&pageNo=1&sat=g2&data=ir105&area=ko&time=" + str_utc_yesterday;

                    Document doc2 = null;
                    DocumentBuilderFactory dbFactoty2 = DocumentBuilderFactory.newInstance();
                    DocumentBuilder dBuilder2 = null;

                    try {
                        dBuilder2 = dbFactoty2.newDocumentBuilder();
                    } catch (ParserConfigurationException e) {
                        e.printStackTrace();
                    }

                    try {
                        doc2 = dBuilder2.parse(url_yesterday);
                    } catch (IOException | SAXException e) {
                        e.printStackTrace();
                    }

                    doc2.getDocumentElement().normalize();

                    NodeList nList2 = doc2.getElementsByTagName("body");
                    Node nNode2 = nList2.item(0);

                    if (nNode2.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) nNode2;
                        String totalCount2 = getTagValue("totalCount", eElement);
                        int_totalCount2 = Integer.parseInt(totalCount2) - 1;
                    }

                    for (int i = 23 - int_totalCount; i >= 0; i--) {
                        satelliteUrl_Value = doc2.getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(1).getChildNodes().item(0).getChildNodes().item(int_totalCount2 - temp2).getChildNodes().item(0).getNodeValue();
                        SatelliteUrl[i] = satelliteUrl_Value;
                        temp2++;
                    }
                }

                final Bitmap[] arr_bitmap = new Bitmap[24];
                for (int i = 0; i <= 23; i++) {
                    do {
                        Bitmap bm = null;
                        try {
                            URL url = new URL(SatelliteUrl[i]);
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                            conn.setConnectTimeout(1000);
                            conn.setReadTimeout(1000);
                            conn.setDoOutput(true);
                            conn.connect();

                            BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
                            bm = BitmapFactory.decodeStream(bis);

                            if (isCancelled()) {
                                return null;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        arr_bitmap[i] = bm;
                    } while (arr_bitmap[i] == null);
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                vf_satellite = findViewById(R.id.vf_satellite);

                                for (int i = 0; i < 24; i++) {
                                    try {
                                        ImageView image = new ImageView(getApplicationContext());
                                        image.setImageBitmap(arr_bitmap[i]);
                                        vf_satellite.addView(image);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                                final int[] temp = {0};
                                try {
                                    vf_satellite.setFlipInterval(100);
                                    vf_satellite.setDisplayedChild(23);
                                    vf_satellite.setInAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.default_anim));
                                    vf_satellite.setOutAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.default_anim));

                                    final Button btn_play_pause = findViewById(R.id.btn_play_pause);
                                    btn_play_pause.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (temp[0] == 0) {
                                                btn_play_pause.setBackgroundResource(R.drawable.pause_icon);
                                                vf_satellite.showNext();
                                                vf_satellite.startFlipping();
                                                temp[0]++;
                                            } else if (temp[0] == 1) {
                                                btn_play_pause.setBackgroundResource(R.drawable.play_icon);
                                                vf_satellite.stopFlipping();
                                                temp[0]--;
                                            }
                                        }
                                    });

                                    vf_satellite.getInAnimation().setAnimationListener(new Animation.AnimationListener() {
                                        public void onAnimationStart(Animation animation) {}

                                        public void onAnimationRepeat(Animation animation) {}

                                        public void onAnimationEnd(Animation animation) {
                                            int displayedChild = vf_satellite.getDisplayedChild();
                                            int childCount = vf_satellite.getChildCount();
                                            if (displayedChild == childCount - 1) {
                                                btn_play_pause.setBackgroundResource(R.drawable.play_icon);
                                                vf_satellite.stopFlipping();
                                                temp[0]--;
                                            }
                                        }
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }).start();
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), "위성 영상을 가져오는데 실패했어요\n잠시 후에 다시 시도해 주세요", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            fragment_satellite.setVisibility();
        }
    }

    private void start_WeatherKorea_Radar() {
        Date today = new Date();
        SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
        String str_utc_today = yyyyMMdd.format(today);

        String url_radar;
        url_radar = "http://apis.data.go.kr/1360000/RadarImgInfoService/getCmpImg?serviceKey=" + getResources().getString(R.string.data_key) + "&numOfRows=10&pageNo=1&data=CMP_WRC&time=" + str_utc_today;

        weatherKorea_radar = new WeatherKorea_Radar(url_radar);
        weatherKorea_radar.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private class WeatherKorea_Radar extends AsyncTask<Void, Void, String> {

        private String url;

        public WeatherKorea_Radar(String url) {
            this.url = url;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
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

                NodeList nList = doc.getElementsByTagName("body");
                Node nNode = nList.item(0);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    String totalCount3 = getTagValue("totalCount", eElement);
                    int_totalCount3 = Integer.parseInt(totalCount3) - 1;
                }

                nList = doc.getElementsByTagName("item");
                nNode = nList.item(0);

                final int[] temp3 = {0};
                int temp4 = 0;

                String radarUrl_Value = null;
                if (int_totalCount3 >= 23) {
                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        for (int i = 23; i >= 0; i--) {
                            radarUrl_Value = doc.getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(1).getChildNodes().item(0).getChildNodes().item(int_totalCount3 - i).getChildNodes().item(0).getNodeValue();
                            RadarUrl[23 - i] = radarUrl_Value;
                        }
                    }
                } else {
                    for (int i = 23; i > 22 - int_totalCount3; i--) {
                        radarUrl_Value = doc.getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(1).getChildNodes().item(0).getChildNodes().item(int_totalCount3 - temp3[0]).getChildNodes().item(0).getNodeValue();
                        RadarUrl[i] = radarUrl_Value;
                        temp3[0]++;
                    }

                    Date today = new Date();
                    Date utc_yesterday = new Date(today.getTime() + (1000 * 60 * 60 * 24 * -1));
                    SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
                    String str_utc_yesterday = yyyyMMdd.format(utc_yesterday);

                    String url_yesterday = "http://apis.data.go.kr/1360000/RadarImgInfoService/getCmpImg?serviceKey=" + getResources().getString(R.string.data_key) + "&numOfRows=10&pageNo=1&data=CMP_WRC&time=" + str_utc_yesterday;

                    Document doc2 = null;
                    DocumentBuilderFactory dbFactoty2 = DocumentBuilderFactory.newInstance();
                    DocumentBuilder dBuilder2 = null;

                    try {
                        dBuilder2 = dbFactoty2.newDocumentBuilder();
                    } catch (ParserConfigurationException e) {
                        e.printStackTrace();
                    }

                    try {
                        doc2 = dBuilder2.parse(url_yesterday);
                    } catch (IOException | SAXException e) {
                        e.printStackTrace();
                    }

                    doc2.getDocumentElement().normalize();

                    NodeList nList2 = doc2.getElementsByTagName("body");
                    Node nNode2 = nList2.item(0);

                    if (nNode2.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) nNode2;
                        String totalCount4 = getTagValue("totalCount", eElement);
                        int_totalCount4 = Integer.parseInt(totalCount4) - 1;
                    }

                    for (int i = 23 - int_totalCount3; i >= 0; i--) {
                        radarUrl_Value = doc2.getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(1).getChildNodes().item(0).getChildNodes().item(int_totalCount4 - temp4).getChildNodes().item(0).getNodeValue();
                        RadarUrl[i] = radarUrl_Value;
                        temp4++;
                    }
                }

                final Bitmap[] arr_bitmap = new Bitmap[24];
                for (int i = 0; i <= 23; i++) {
                    do {
                        Bitmap bm = null;
                        try {
                            URL url = new URL(RadarUrl[i]);
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setConnectTimeout(1000);
                            conn.setReadTimeout(1000);
                            conn.setDoOutput(true);
                            conn.connect();

                            BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
                            bm = BitmapFactory.decodeStream(bis);

                            if (isCancelled()) {
                                return null;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        arr_bitmap[i] = bm;
                    } while (arr_bitmap[i] == null);
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                vf_radar = findViewById(R.id.vf_radar);

                                for (int i = 0; i < 24; i++) {
                                    try {
                                        ImageView image = new ImageView(getApplicationContext());
                                        image.setImageBitmap(arr_bitmap[i]);
                                        vf_radar.addView(image);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                                final int[] temp = {0};
                                try {
                                    vf_radar.setFlipInterval(100);
                                    vf_radar.setDisplayedChild(23);
                                    vf_radar.setInAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.default_anim));
                                    vf_radar.setOutAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.default_anim));

                                    final Button btn_play_pause = findViewById(R.id.btn_play_pause2);
                                    btn_play_pause.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (temp[0] == 0) {
                                                btn_play_pause.setBackgroundResource(R.drawable.pause_icon);
                                                vf_radar.showNext();
                                                vf_radar.startFlipping();
                                                temp[0]++;
                                            } else if (temp[0] == 1) {
                                                btn_play_pause.setBackgroundResource(R.drawable.play_icon);
                                                vf_radar.stopFlipping();
                                                temp[0]--;
                                            }
                                        }
                                    });

                                    vf_radar.getInAnimation().setAnimationListener(new Animation.AnimationListener() {
                                        public void onAnimationStart(Animation animation) {}

                                        public void onAnimationRepeat(Animation animation) {}

                                        public void onAnimationEnd(Animation animation) {
                                            int displayedChild = vf_radar.getDisplayedChild();
                                            int childCount = vf_radar.getChildCount();
                                            if (displayedChild == childCount - 1) {
                                                btn_play_pause.setBackgroundResource(R.drawable.play_icon);
                                                vf_radar.stopFlipping();
                                                temp[0]--;
                                            }
                                        }
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }).start();
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), "레이더 영상을 가져오는데 실패했어요\n잠시 후에 다시 시도해 주세요", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            fragment_radar.setVisibility();
        }
    }
}