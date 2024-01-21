package com.example.technologycompare.helper;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.technologycompare.R;

import java.util.List;

public class MyLikeAdpter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<MyLike> myLikes;
    private Device device;
    DataManager dataManager = DataManager.getInstance();
    List<Device> devices = dataManager.getDevices();

    public MyLikeAdpter(Context context, int layout, List<MyLike> myLikes) {
        this.context = context;
        this.layout = layout;
        this.myLikes = myLikes;
    }

    @Override
    public int getCount() {
        return myLikes.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    class ViewHolder{
        ImageView imgDevice;
        TextView tvNameDevice,tvPriceDevice;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view ==  null){
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view =  inflater.inflate(layout,null);
            holder.imgDevice = view.findViewById(R.id.imgDevice);
            holder.tvNameDevice = view.findViewById(R.id.tvNameDevice);
            holder.tvPriceDevice = view.findViewById(R.id.tvPriceDevice);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        device = devices.get(myLikes.get(i).getIdPhone()-1);
        Glide.with(context)
                .load(device.getImageDevice())
                .into(holder.imgDevice);
        // Hiển thị tên thiết bị
        holder.tvNameDevice.setText(device.getNameDevice());
        holder.tvPriceDevice.setText(device.getPriceDevice());
        Log.d("MyLikeAdapter", "Item " + (i ) + ": idphone - " +  myLikes.get(i).getIdPhone() + ", i : - " + myLikes.get(i).getIdMylike());


        return view;
    }


}
