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
import android.widget.ImageView;
import android.widget.TextView;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class Drawer2Activity extends AppCompatActivity {

    private FragmentWthrWrn1 fragment_wthr_wrn1 = new FragmentWthrWrn1();
    private FragmentWthrWrn2 fragment_wthr_wrn2 = new FragmentWthrWrn2();

    private String tmFc;
    private String tmSeq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer2);

        getWindow().setStatusBarColor(Color.rgb(246,246,246));

        ViewPager pager = findViewById(R.id.vp_drawer2);
        pager.setOffscreenPageLimit(2);

        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());
        adapter.addItem(0, fragment_wthr_wrn1);
        adapter.addItem(1, fragment_wthr_wrn2);
        pager.setAdapter(adapter);

        start_WeatherKorea_Wthr_Wrn();
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
    }

    private String getTagValue(String tag, Element eElement) {
        NodeList nlList = eElement.getElementsByTagName(tag).item(0).getChildNodes();
        Node nValue = nlList.item(0);
        if(nValue == null) return null;
        return nValue.getNodeValue();
    }

    private void start_WeatherKorea_Wthr_Wrn() {
        Date temp_today = new Date();
        Date temp_yesterday = new Date(temp_today.getTime()+(1000*60*60*24*-1));
        SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
        String yesterday = yyyyMMdd.format(temp_yesterday);
        String today = yyyyMMdd.format(temp_today);

        String url_parsing_wrn1;
        url_parsing_wrn1 = "http://apis.data.go.kr/1360000/WthrWrnInfoService/getWthrWrnMsg" + "?serviceKey=" + getResources().getString(R.string.data_key) + "&numOfRows=10&pageNo=1" + "&stnId=108&fromTmFc=" + yesterday + "&toTmFc=" + today;
        new WeatherKorea_Wthr_Wrn1(url_parsing_wrn1).execute();

        String url_parsing_wrn2;
        url_parsing_wrn2 = "http://apis.data.go.kr/1360000/WthrWrnInfoService/getWthrPwn" + "?serviceKey=" + getResources().getString(R.string.data_key) + "&numOfRows=10&pageNo=1" + "&stnId=108&fromTmFc=" + yesterday + "&toTmFc=" + today;
        new WeatherKorea_Wthr_Wrn2(url_parsing_wrn2).execute();
    }

    private class WeatherKorea_Wthr_Wrn1 extends AsyncTask<Void, Void, String> {

        private String url;

        public WeatherKorea_Wthr_Wrn1(String url) {
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

                NodeList nList = doc.getElementsByTagName("item");
                Node nNode = nList.item(0);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    final Element eElement = (Element) nNode;

                    tmFc = getTagValue("tmFc", eElement);
                    tmSeq = getTagValue("tmSeq", eElement);
                    final String t6 = getTagValue("t6", eElement);
                    final String other = getTagValue("other", eElement);

                    Bitmap bm = null;
                    Bitmap bm_wrn1;
                    do {
                        try {
                            URL url = new URL("https://www.weather.go.kr/repositary/image/wrn/img/KTKO50_" + tmFc + "_108_" + tmSeq + ".png");
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setConnectTimeout(1000);
                            conn.setReadTimeout(1000);
                            conn.setDoOutput(true);
                            conn.connect();
                            InputStream is = conn.getInputStream();
                            bm = BitmapFactory.decodeStream(is);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        bm_wrn1 = bm;
                    } while (bm_wrn1 == null);

                    final Bitmap finalBm = bm;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ImageView iv_wrn1 = findViewById(R.id.iv_wrn1);
                                    iv_wrn1.setImageBitmap(finalBm);
                                    TextView tv_t6 = findViewById(R.id.tv_t6);
                                    tv_t6.setText("\n<기상특보현황>\n" + t6 + "\n");
                                    TextView tv_other = findViewById(R.id.tv_other);
                                    tv_other.setText("<참고사항>\n" + other);
                                }
                            });
                        }
                    }).start();
                }
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    public void run() {
                        ImageView iv_wrn1 = findViewById(R.id.iv_wrn1);
                        iv_wrn1.setBackgroundResource(R.drawable.wthr_wrn1_default);
                        TextView tv_t6 = findViewById(R.id.tv_t6);
                        tv_t6.setText("\n<기상특보현황>\n" + "o 없음" + "\n");
                        TextView tv_other = findViewById(R.id.tv_other);
                        tv_other.setText("<참고사항>\n" + "o 없음");
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            fragment_wthr_wrn1.setINVISIBLE();
        }
    }

    private class WeatherKorea_Wthr_Wrn2 extends AsyncTask<Void, Void, String> {

        private String url;

        public WeatherKorea_Wthr_Wrn2(String url) {
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

                NodeList nList = doc.getElementsByTagName("item");
                Node nNode = nList.item(0);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    final Element eElement = (Element) nNode;

                    tmFc = getTagValue("tmFc", eElement);
                    tmSeq = getTagValue("tmSeq", eElement);
                    final String pwn = getTagValue("pwn", eElement);
                    final String rem = getTagValue("rem", eElement);

                    Bitmap bm = null;
                    Bitmap bm_wrn2;
                    do {
                        try {
                            URL url = new URL("https://www.weather.go.kr/repositary/image/wrn/img/KTKO52_" + tmFc + "_108_" + tmSeq + ".png");
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                            conn.setConnectTimeout(1000);
                            conn.setReadTimeout(1000);
                            conn.setDoOutput(true);
                            conn.connect();

                            InputStream is = conn.getInputStream();
                            bm = BitmapFactory.decodeStream(is);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        bm_wrn2 = bm;
                    } while (bm_wrn2 == null);

                    final Bitmap finalBm = bm;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ImageView iv_wrn2 = findViewById(R.id.iv_wrn2);
                                    iv_wrn2.setImageBitmap(finalBm);
                                    TextView tv_pwn = findViewById(R.id.tv_pwn);
                                    tv_pwn.setText("\n<예비특보현황>\n" + pwn + "\n");
                                    TextView tv_rem = findViewById(R.id.tv_rem);
                                    tv_rem.setText("<참고사항>\n" + rem);
                                }
                            });
                        }
                    }).start();
                }
            } catch (Exception e) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        ImageView iv_wrn2 = findViewById(R.id.iv_wrn2);
                        iv_wrn2.setBackgroundResource(R.drawable.wthr_wrn2_default);
                        TextView tv_pwn = findViewById(R.id.tv_pwn);
                        tv_pwn.setText("\n<예비특보현황>\n" + "o 없음" + "\n");
                        TextView tv_rem = findViewById(R.id.tv_rem);
                        tv_rem.setText("<참고사항>\n" + "o 없음");
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            fragment_wthr_wrn2.setINVISIBLE();
        }
    }

}