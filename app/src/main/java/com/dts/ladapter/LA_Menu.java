package com.dts.ladapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dts.base.AppMethods;
import com.dts.base.DateUtils;
import com.dts.base.MiscUtils;
import com.dts.base.clsClasses;
import com.dts.mpossop.PBase;
import com.dts.mpossop.R;


import java.util.ArrayList;

public class LA_Menu extends BaseAdapter {

    private MiscUtils mu;
    private DateUtils du;
    private AppMethods app;

    private ArrayList<clsClasses.clsMenu> items= new ArrayList<clsClasses.clsMenu>();
    private int selectedIndex;
    private LayoutInflater l_Inflater;

    public LA_Menu(Context context, PBase owner, ArrayList<clsClasses.clsMenu> results) {
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

            convertView = l_Inflater.inflate(R.layout.lv_menu, null);
            holder = new ViewHolder();

            holder.rel1 = convertView.findViewById(R.id.relwrap);
            holder.lbl1 = convertView.findViewById(R.id.lblV1);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.lbl1.setText(""+items.get(position).nombre);

        if(selectedIndex!= -1 && position == selectedIndex) {
            holder.rel1.setBackgroundResource(R.drawable.frame_round_grad);
        } else {
            holder.rel1.setBackgroundResource(R.drawable.frame_round);
        }

        return convertView;
    }

    static class ViewHolder {
        TextView lbl1;
        RelativeLayout rel1;
    }

}

