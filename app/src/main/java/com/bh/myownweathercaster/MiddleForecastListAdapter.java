package com.bh.myownweathercaster;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class MiddleForecastListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<MiddleForecastListItem> mList;

    MiddleForecastListAdapter (ArrayList<MiddleForecastListItem> list) {
        mList = new ArrayList<>();
        this.mList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (viewType == 1) {
            view = inflater.inflate(R.layout.middle_forecast_item1, parent, false);
            return new FirstViewHolder(view);
        } else if(viewType == 2) {
            view = inflater.inflate(R.layout.middle_forecast_item2, parent, false);
            return new SecondViewHolder(view);
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof FirstViewHolder) {
            String dateConverter = mList.get(position).getFD();
            String dateConverter1;
            if (dateConverter.charAt(0) == '0') {
                dateConverter1 = dateConverter.substring(1, 2);
            } else {
                dateConverter1 = dateConverter.substring(0, 2);
            }
            String dateConverter2;
            if (dateConverter.charAt(2) == '0') {
                dateConverter2 = dateConverter.substring(3, 4);
            } else {
                dateConverter2 = dateConverter.substring(2, 4);
            }
            ((FirstViewHolder) viewHolder).tv_mf1_date.setText(dateConverter1 + "/" + dateConverter2);

            if (mList.get(position).getSKY_AM().equals("맑음")) {
                ((FirstViewHolder) viewHolder).iv_mf1_sky_am.setBackgroundResource(R.drawable.sunny_icon);
            } else if (mList.get(position).getSKY_AM().equals("구름많음")) {
                ((FirstViewHolder) viewHolder).iv_mf1_sky_am.setBackgroundResource(R.drawable.cloudy1_icon);
            } else if (mList.get(position).getSKY_AM().equals("흐림")) {
                ((FirstViewHolder) viewHolder).iv_mf1_sky_am.setBackgroundResource(R.drawable.cloudy2_icon);
            } else if (mList.get(position).getSKY_AM().contains("비") && !mList.get(position).getSKY_AM().contains("눈")) {
                ((FirstViewHolder) viewHolder).iv_mf1_sky_am.setBackgroundResource(R.drawable.rainy_icon);
            } else if (!mList.get(position).getSKY_AM().contains("비") && mList.get(position).getSKY_AM().contains("눈")) {
                ((FirstViewHolder) viewHolder).iv_mf1_sky_am.setBackgroundResource(R.drawable.snow_icon);
            } else if (mList.get(position).getSKY_AM().contains("비") && mList.get(position).getSKY_AM().contains("눈")) {
                ((FirstViewHolder) viewHolder).iv_mf1_sky_am.setBackgroundResource(R.drawable.snowrainy_icon);
            }

            if (mList.get(position).getSKY_PM().equals("맑음")) {
                ((FirstViewHolder) viewHolder).iv_mf1_sky_pm.setBackgroundResource(R.drawable.sunny_icon);
            } else if (mList.get(position).getSKY_PM().equals("구름많음")) {
                ((FirstViewHolder) viewHolder).iv_mf1_sky_pm.setBackgroundResource(R.drawable.cloudy1_icon);
            } else if (mList.get(position).getSKY_PM().equals("흐림")) {
                ((FirstViewHolder) viewHolder).iv_mf1_sky_pm.setBackgroundResource(R.drawable.cloudy2_icon);
            } else if (mList.get(position).getSKY_PM().contains("비") && !mList.get(position).getSKY_AM().contains("눈")) {
                ((FirstViewHolder) viewHolder).iv_mf1_sky_pm.setBackgroundResource(R.drawable.rainy_icon);
            } else if (!mList.get(position).getSKY_PM().contains("비") && mList.get(position).getSKY_AM().contains("눈")) {
                ((FirstViewHolder) viewHolder).iv_mf1_sky_pm.setBackgroundResource(R.drawable.snow_icon);
            } else if (mList.get(position).getSKY_PM().contains("비") && mList.get(position).getSKY_AM().contains("눈")) {
                ((FirstViewHolder) viewHolder).iv_mf1_sky_pm.setBackgroundResource(R.drawable.snowrainy_icon);
            }

            ((FirstViewHolder) viewHolder).tv_mf1_temp.setText(mList.get(position).getTEMP_MIN() + "°/" + mList.get(position).getTEMP_MAX() + "°");
            ((FirstViewHolder) viewHolder).tv_mf1_rainfall.setText(mList.get(position).getRAIN_AM() + "%/" + mList.get(position).getRAIN_PM() + "%");
        } else if (viewHolder instanceof SecondViewHolder) {
            String dateConverter = mList.get(position).getFD();
            String dateConverter1;
            if (dateConverter.charAt(0) == '0') {
                dateConverter1 = dateConverter.substring(1, 2);
            } else {
                dateConverter1 = dateConverter.substring(0, 2);
            }
            String dateConverter2;
            if (dateConverter.charAt(2) == '0') {
                dateConverter2 = dateConverter.substring(3, 4);
            } else {
                dateConverter2 = dateConverter.substring(2, 4);
            }
            ((SecondViewHolder) viewHolder).tv_mf2_date.setText(dateConverter1 + "/" + dateConverter2);

            if (mList.get(position).getSKY_AM().equals("맑음")) {
                ((SecondViewHolder) viewHolder).iv_mf2_sky.setBackgroundResource(R.drawable.sunny_icon);
            } else if(mList.get(position).getSKY_AM().equals("구름많음")) {
                ((SecondViewHolder) viewHolder).iv_mf2_sky.setBackgroundResource(R.drawable.cloudy1_icon);
            } else if(mList.get(position).getSKY_AM().equals("흐림")) {
                ((SecondViewHolder) viewHolder).iv_mf2_sky.setBackgroundResource(R.drawable.cloudy2_icon);
            } else if(mList.get(position).getSKY_AM().contains("비") && !mList.get(position).getSKY_AM().contains("눈")) {
                ((SecondViewHolder) viewHolder).iv_mf2_sky.setBackgroundResource(R.drawable.rainy_icon);
            } else if(!mList.get(position).getSKY_AM().contains("비") && mList.get(position).getSKY_AM().contains("눈")) {
                ((SecondViewHolder) viewHolder).iv_mf2_sky.setBackgroundResource(R.drawable.snow_icon);
            } else if(mList.get(position).getSKY_AM().contains("비") && mList.get(position).getSKY_AM().contains("눈")) {
                ((SecondViewHolder) viewHolder).iv_mf2_sky.setBackgroundResource(R.drawable.snowrainy_icon);
            }

            ((SecondViewHolder) viewHolder).tv_mf2_temp.setText(mList.get(position).getTEMP_MIN() + "°/" + mList.get(position).getTEMP_MAX() + "°");
            ((SecondViewHolder) viewHolder).tv_mf2_rainfall.setText(mList.get(position).getRAIN_AM() + "%");
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mList.get(position).getViewType();
    }

    public class FirstViewHolder extends RecyclerView.ViewHolder {
        TextView tv_mf1_date;
        ImageView iv_mf1_sky_am;
        ImageView iv_mf1_sky_pm;
        TextView tv_mf1_temp;
        TextView tv_mf1_rainfall;

        FirstViewHolder(View itemView) {
            super(itemView);
            tv_mf1_date = itemView.findViewById(R.id.tv_mf1_date);
            iv_mf1_sky_am = itemView.findViewById(R.id.iv_mf1_sky_am);
            iv_mf1_sky_pm = itemView.findViewById(R.id.iv_mf1_sky_pm);
            tv_mf1_temp = itemView.findViewById(R.id.tv_mf1_temp);
            tv_mf1_rainfall = itemView.findViewById(R.id.tv_mf1_rainfall);
        }
    }

    public class SecondViewHolder extends RecyclerView.ViewHolder {
        TextView tv_mf2_date;
        ImageView iv_mf2_sky;
        TextView tv_mf2_temp;
        TextView tv_mf2_rainfall;

        SecondViewHolder(View itemView) {
            super(itemView);
            tv_mf2_date = itemView.findViewById(R.id.tv_mf2_date);
            iv_mf2_sky = itemView.findViewById(R.id.iv_mf2_sky);
            tv_mf2_temp = itemView.findViewById(R.id.tv_mf2_temp);
            tv_mf2_rainfall = itemView.findViewById(R.id.tv_mf2_rainfall);
        }
    }

}