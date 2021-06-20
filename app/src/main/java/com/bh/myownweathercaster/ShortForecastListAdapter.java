package com.bh.myownweathercaster;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import static com.bh.myownweathercaster.WeatherActivity.sunrise;
import static com.bh.myownweathercaster.WeatherActivity.sunset;

public class ShortForecastListAdapter extends RecyclerView.Adapter<ShortForecastListAdapter.MyViewHolder> {

    private ArrayList<ShortForecastListItem> mList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        protected TextView tv_sf_date;
        protected TextView tv_sf_time;
        protected ImageView iv_sf_sky;
        protected TextView tv_sf_temp;
        protected TextView tv_sf_rain_per;

        public MyViewHolder(View view) {
            super(view);
            this.tv_sf_date = view.findViewById(R.id.tv_sf_date);
            this.tv_sf_time = view.findViewById(R.id.tv_sf_time);
            this.iv_sf_sky = view.findViewById(R.id.iv_sf_sky);
            this.tv_sf_temp = view.findViewById(R.id.tv_sf_temp);
            this.tv_sf_rain_per = view.findViewById(R.id.tv_sf_rain_per);
        }

    }

    public ShortForecastListAdapter(ArrayList<ShortForecastListItem> list) {
        mList = new ArrayList<>();
        this.mList = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.short_forecast_item, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        Context context = parent.getContext();

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            String dateConverter = mList.get(position).getFD();

            String dateConverter1;
            if (dateConverter.substring(4, 5).equals("0")) {
                dateConverter1 = dateConverter.substring(5, 6);
            } else {
                dateConverter1 = dateConverter.substring(4, 6);
            }

            String dateConverter2;
            if (dateConverter.substring(6, 7).equals("0")) {
                dateConverter2 = dateConverter.substring(7, 8);
            } else {
                dateConverter2 = dateConverter.substring(6, 8);
            }

            String timeConverter = mList.get(position).getFT();
            if (timeConverter.substring(0, 1).equals("0")) {
                timeConverter = timeConverter.substring(1, 2);
            } else {
                timeConverter = timeConverter.substring(0, 2);
            }

            if (timeConverter.equals("0")) {
                timeConverter = "24";
                try {
                    dateConverter = yesterday_Date(dateConverter);
                    if (dateConverter.substring(4, 5).equals("0")) {
                        dateConverter1 = dateConverter.substring(5, 6);
                    } else {
                        dateConverter1 = dateConverter.substring(4, 6);
                    }
                    if (dateConverter.substring(6, 7).equals("0")) {
                        dateConverter2 = dateConverter.substring(7, 8);
                    } else {
                        dateConverter2 = dateConverter.substring(6, 8);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            String pty = mList.get(position).getPTY();
            String skyConverter = mList.get(position).getSKY();

            if (pty != null && skyConverter != null) {
                if (pty.equals("0")) {
                    switch (skyConverter) {
                        case "1":
                            if (Double.parseDouble(sunrise) <= 600) {
                                if(Double.parseDouble(sunset) >= 1800) {
                                    if (timeConverter.equals("21") || timeConverter.equals("24") || timeConverter.equals("3")) {
                                        holder.iv_sf_sky.setBackgroundResource(R.drawable.sunny_night_icon);
                                    } else {
                                        holder.iv_sf_sky.setBackgroundResource(R.drawable.sunny_icon);
                                    }
                                } else {
                                    if (timeConverter.equals("18") || timeConverter.equals("21") || timeConverter.equals("24") || timeConverter.equals("3")) {
                                        holder.iv_sf_sky.setBackgroundResource(R.drawable.sunny_night_icon);
                                    } else {
                                        holder.iv_sf_sky.setBackgroundResource(R.drawable.sunny_icon);
                                    }
                                }
                            } else {
                                if(Double.parseDouble(sunset) >= 1800) {
                                    if (timeConverter.equals("21") || timeConverter.equals("24") || timeConverter.equals("3") || timeConverter.equals("6")) {
                                        holder.iv_sf_sky.setBackgroundResource(R.drawable.sunny_night_icon);
                                    } else {
                                        holder.iv_sf_sky.setBackgroundResource(R.drawable.sunny_icon);
                                    }
                                } else {
                                    if (timeConverter.equals("18") || timeConverter.equals("21") || timeConverter.equals("24") || timeConverter.equals("3") || timeConverter.equals("6")) {
                                        holder.iv_sf_sky.setBackgroundResource(R.drawable.sunny_night_icon);
                                    } else {
                                        holder.iv_sf_sky.setBackgroundResource(R.drawable.sunny_icon);
                                    }
                                }
                            }
                            break;
                        case "3":
                            if (Double.parseDouble(sunrise) <= 600) {
                                if(Double.parseDouble(sunset) >= 1800) {
                                    if (timeConverter.equals("21") || timeConverter.equals("24") || timeConverter.equals("3")) {
                                        holder.iv_sf_sky.setBackgroundResource(R.drawable.cloudy1_night_icon);
                                    } else {
                                        holder.iv_sf_sky.setBackgroundResource(R.drawable.cloudy1_icon);
                                    }
                                } else {
                                    if (timeConverter.equals("18") || timeConverter.equals("21") || timeConverter.equals("24") || timeConverter.equals("3")) {
                                        holder.iv_sf_sky.setBackgroundResource(R.drawable.cloudy1_night_icon);
                                    } else {
                                        holder.iv_sf_sky.setBackgroundResource(R.drawable.cloudy1_icon);
                                    }
                                }
                            } else {
                                if(Double.parseDouble(sunset) >= 1800) {
                                    if (timeConverter.equals("21") || timeConverter.equals("24") || timeConverter.equals("3") || timeConverter.equals("6")) {
                                        holder.iv_sf_sky.setBackgroundResource(R.drawable.cloudy1_night_icon);
                                    } else {
                                        holder.iv_sf_sky.setBackgroundResource(R.drawable.cloudy1_icon);
                                    }
                                } else {
                                    if (timeConverter.equals("18") || timeConverter.equals("21") || timeConverter.equals("24") || timeConverter.equals("3") || timeConverter.equals("6")) {
                                        holder.iv_sf_sky.setBackgroundResource(R.drawable.cloudy1_night_icon);
                                    } else {
                                        holder.iv_sf_sky.setBackgroundResource(R.drawable.cloudy1_icon);
                                    }
                                }
                            }
                            break;
                        case "4":
                            holder.iv_sf_sky.setBackgroundResource(R.drawable.cloudy2_icon);
                            break;
                    }
                } else {
                    holder.iv_sf_sky.setBackgroundResource(R.drawable.rainy_icon);
                }
            }

            holder.tv_sf_date.setText(dateConverter1 + "/" + dateConverter2);
            holder.tv_sf_time.setText(timeConverter + "시");
            holder.tv_sf_temp.setText(mList.get(position).getT3H() + "°");
            holder.tv_sf_rain_per.setText(mList.get(position).getPOP() + "%");
    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }

    private String yesterday_Date(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar c = Calendar.getInstance();
        Date d = sdf.parse(date);

        c.setTime(d);
        c.add(Calendar.DATE,-1);
        date = sdf.format(c.getTime());

        return date;
    }
}
