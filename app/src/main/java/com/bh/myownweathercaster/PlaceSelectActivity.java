package com.bh.myownweathercaster;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class PlaceSelectActivity extends AppCompatActivity {

    private EditText et_search_place;
    private Button btn_search_place;

    private String dongUrl;

    private ArrayList<PlaceItem> data;
    private ListView lv_place_list;
    private PlaceAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_select);

        getWindow().setStatusBarColor(Color.rgb(255,255,255));

        WeatherActivity.isMoved = 0;
        WeatherActivity.isMoved++;

        loadData();

        et_search_place = findViewById(R.id.et_search_place);
        btn_search_place = findViewById(R.id.btn_search_place);

        et_search_place.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    btn_search_place.callOnClick();
                    return true;
                }
                return false;
            }
        });

        btn_search_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dongUrl = "http://api.vworld.kr/req/data?service=data&request=GetFeature&data=LT_C_ADEMD_INFO&key=" + getResources().getString(R.string.v_world_key) + "&format=xml&size=1000&domain=" + getResources().getString(R.string.v_world_key) + "&attrFilter=emd_kor_nm:like:" + et_search_place.getText().toString();

                if (et_search_place.getText().toString().equals("")) {
                    Toast.makeText(PlaceSelectActivity.this, "검색어를 입력해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    new V_World_place(dongUrl).execute();
                }

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(et_search_place.getWindowToken(), 0);
            }
        });

        lv_place_list = findViewById(R.id.lv_place_list);
        lv_place_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), WeatherActivity.class);
                intent.putExtra("place", data.get(position).getPlace());
                intent.putExtra("lon", data.get(position).getLon());
                intent.putExtra("lat", data.get(position).getLat());
                checkInternetState(intent);
            }
        });

        lv_place_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                data.remove(position);
                lv_place_list = findViewById(R.id.lv_place_list);
                adapter = new PlaceAdapter(PlaceSelectActivity.this, R.layout.place_list_item, data);
                lv_place_list.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                return true;
            }
        });

        Button btn_move_to_gps = findViewById(R.id.btn_move_to_gps);
        btn_move_to_gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WeatherActivity.isMoved = 0;
                Intent intent = new Intent(getApplicationContext(), WeatherActivity.class);
                checkInternetState(intent);
            }
        });

    }

    private class V_World_place extends AsyncTask<Void, Void, String> {

        private String url;

        String statusVaule;

        String[] placeValue;
        String[] lowerCornerValue;
        String[] upperCornerValue;
        Double[] lonValue;
        Double[] latValue;

        int Selected_item_num;

        public V_World_place(String url) {
            this.url = url;
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

            placeValue = new String[nList.getLength()];
            lowerCornerValue = new String[nList.getLength()];
            upperCornerValue = new String[nList.getLength()];
            lonValue = new Double[nList.getLength()];
            latValue = new Double[nList.getLength()];

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;

                    placeValue[temp] = getTagValue("full_nm", eElement);

                    lowerCornerValue[temp] = getTagValue("gml:lowerCorner", eElement);
                    upperCornerValue[temp] = getTagValue("gml:upperCorner", eElement);

                    String[] temp1 = lowerCornerValue[temp].split("\\s");
                    String[] temp2 = upperCornerValue[temp].split("\\s");

                    lonValue[temp] = (Double.parseDouble(temp1[0]) + Double.parseDouble(temp2[0]))/2;
                    latValue[temp] = (Double.parseDouble(temp1[1]) + Double.parseDouble(temp2[1]))/2;
                }
            }

            NodeList nList2 = doc.getElementsByTagName("response");

            for (int temp = 0; temp < nList2.getLength(); temp++) {
                Node nNode = nList2.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;

                    statusVaule = getTagValue("status", eElement);
                }
            }

            if (statusVaule.equals("OK")) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        AlertDialog.Builder ab = new AlertDialog.Builder(PlaceSelectActivity.this);
                        ab.setTitle("위치를 선택해주세요");
                        ab.setSingleChoiceItems(placeValue, -1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int whichBtn) {
                                Selected_item_num = whichBtn;
                            }
                        }).setPositiveButton("선택", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int whichBtn) {
                                PlaceItem placeItem = new PlaceItem(placeValue[Selected_item_num], String.valueOf(latValue[Selected_item_num]), String.valueOf(lonValue[Selected_item_num]));
                                data.add(placeItem);
                                adapter = new PlaceAdapter(PlaceSelectActivity.this, R.layout.place_list_item, data);
                                lv_place_list.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                                et_search_place.setText("");
                            }
                        }).setNegativeButton("취소", null);
                        ab.show();
                    }
                });
            } else {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(PlaceSelectActivity.this, "검색 결과가 없습니다", Toast.LENGTH_SHORT).show();
                    }
                });
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
        }
    }

    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(data);
        editor.putString("task list", json);
        editor.apply();
    }

    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list", null);
        Type type = new TypeToken <ArrayList<PlaceItem>>() {}.getType();
        data = gson.fromJson(json, type);

        if (data == null) {
            data = new ArrayList<>();
        } else {
            lv_place_list = findViewById(R.id.lv_place_list);
            adapter = new PlaceAdapter(PlaceSelectActivity.this, R.layout.place_list_item, data);
            lv_place_list.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveData();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    private void checkInternetState(Intent i) {
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        if (!(connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected())) {
            networkCloseNow();
        } else {
            startActivity(i);
            overridePendingTransition(0, 0);
            finish();
        }
    }

    private void networkCloseNow() {
        Toast toast_permission = Toast.makeText(this, "네트워크가 연결되어 있지 않거나 문제가 있습니다.\n잠시 후에 다시 시도해주세요.", Toast.LENGTH_LONG);
        toast_permission.show();
        Intent intent = new Intent(this, SplashErrorActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }

}