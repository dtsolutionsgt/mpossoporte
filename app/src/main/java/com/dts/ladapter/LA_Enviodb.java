package com.dts.ladapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dts.base.AppMethods;
import com.dts.base.DateUtils;
import com.dts.base.MiscUtils;
import com.dts.base.clsClasses;
import com.dts.mpossop.PBase;
import com.dts.mpossop.R;

import java.util.ArrayList;

public class LA_Enviodb extends BaseAdapter {

    private MiscUtils mu;
    private DateUtils du;
    private AppMethods app;

    private ArrayList<clsClasses.clsfbVersion> items= new ArrayList<clsClasses.clsfbVersion>();
    private int selectedIndex;
    private LayoutInflater l_Inflater;

    public LA_Enviodb(Context context, PBase owner, ArrayList<clsClasses.clsfbVersion> results) {
        items = results;
        l_Inflater = LayoutInflater.from(context);
        selectedIndex = -1;

        mu=owner.mu;
        du=owner.du;
        app=owner.app;
    }

    public void setSelectedIndex(int ind) {
        selectedIndex = ind;
        notifyDataSetChanged();
    }

    public void refreshItems() {
        notifyDataSetChanged();
    }

    public int getCount() {
        return items.size();
    }

    public Object getItem(int position) {
        return items.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        int rid=R.drawable.blank;

        if (convertView == null) {

            convertView = l_Inflater.inflate(R.layout.lv_enviodb, null);
            holder = new ViewHolder();

            holder.rel1 = convertView.findViewById(R.id.relwrap);
            holder.lbl1 = convertView.findViewById(R.id.lblV1);
            holder.lbl2 = convertView.findViewById(R.id.lblV3);
            holder.lbl3 = convertView.findViewById(R.id.lblV2);
            holder.lbl4 = convertView.findViewById(R.id.lblV4);
            holder.lbl5 = convertView.findViewById(R.id.lblV5);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.lbl1.setText(""+items.get(position).actver);
        holder.lbl2.setText("Caja: "+items.get(position).rid);
        holder.lbl3.setText(""+items.get(position).enombre);
        holder.lbl4.setText(""+items.get(position).snombre);
        holder.lbl5.setText(""+items.get(position).rnombre);

        if(selectedIndex!= -1 && position == selectedIndex) {
            holder.rel1.setBackgroundResource(R.drawable.frame_round_grad);
        } else {
            holder.rel1.setBackgroundResource(R.drawable.frame_round);
        }

        return convertView;
    }

    static class ViewHolder {
        TextView lbl1,lbl2,lbl3,lbl4,lbl5;
        RelativeLayout rel1;
    }

}

