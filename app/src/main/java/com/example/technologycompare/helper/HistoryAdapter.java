package com.example.technologycompare.helper;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.technologycompare.R;
import com.example.technologycompare.ui.CompareActivity;
import com.example.technologycompare.ui.HistoryCompareActivity;

import java.util.List;

public class HistoryAdapter extends BaseAdapter {
    private Context context;
    private Device device1, device2;
    private int layout;

    DataManager dataManager = DataManager.getInstance();
    List<Device> devices = dataManager.getDevices();
    private List<History> histories;

    public HistoryAdapter(Context context, int layout, List<History> histories) {
        this.context = context;
        this.layout = layout;
        this.histories = histories;
    }


    @Override
    public int getCount() {
        Log.d("history", histories.size() + "");
        return histories.size();
    }

    @Override
    public Object getItem(int i) {
        Log.d("history", histories.get(i) + "");
        return histories.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private class ViewHoler {
        ImageView imgDevice1, imgDevice2;
        TextView tvDevice1, tvDevice2;
        Button btnCancel;
        LinearLayout layoutHistory;
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(History history);
    }

    private OnDeleteClickListener onDeleteClickListener;

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.onDeleteClickListener = listener;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHoler holder;
        if (view == null) {
            holder = new ViewHoler();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, null);
            holder.imgDevice1 = view.findViewById(R.id.imgDevice1);
            holder.imgDevice2 = view.findViewById(R.id.imgDevice2);
            holder.tvDevice1 = view.findViewById(R.id.tvCompareDevice1);
            holder.tvDevice2 = view.findViewById(R.id.tvCompareDevice2);
            holder.btnCancel = view.findViewById(R.id.btnCancel);
            holder.layoutHistory = view.findViewById(R.id.layoutHistory);
            view.setTag(holder);
        } else {
            holder = (ViewHoler) view.getTag();
        }

        String id1 = null;
        String id2 = null;
        id1 = histories.get(i).getDevice1();
        id2 = histories.get(i).getDevice2();
        device1 = devices.get(Integer.parseInt(id1)-1);

        device2 = devices.get(Integer.parseInt(id2)-1);


        Glide.with(context)
                .load(device1.getImageDevice())
                .into(holder.imgDevice1);
        // Hiển thị tên thiết bị
        holder.tvDevice1.setText(device1.getNameDevice());

        Glide.with(context)
                .load(device2.getImageDevice())
                .into(holder.imgDevice2);
        // Hiển thị tên thiết bị
        holder.tvDevice2.setText(device2.getNameDevice());

        holder.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onDeleteClickListener != null) {
                    onDeleteClickListener.onDeleteClick(histories.get(i));
                }
                Log.d("HistoryCompareActivity", "Selected History: " + devices.get(Integer.parseInt(histories.get(i).getDevice1())).getIdDevice() + ", " + devices.get(Integer.parseInt(histories.get(i).getDevice2())).getIdDevice() + "," + histories.get(i).getIdHistory());
            }
        });
        holder.layoutHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("HistoryCompareActivity", "Selected History: " + devices.get(Integer.parseInt(histories.get(i).getDevice1())).getIdDevice() + ", " + devices.get(Integer.parseInt(histories.get(i).getDevice2())).getIdDevice() + "," + histories.get(i).getIdHistory());

                // Xử lý khi mục được chọn
                int idDevice1 = Integer.parseInt(histories.get(i).getDevice1())-1;
                int idDevice2 = Integer.parseInt(histories.get(i).getDevice2())-1;
                Intent intent = new Intent(context, CompareActivity.class);
                intent.putExtra("deviceIndex1", idDevice1);
                intent.putExtra("deviceIndex2", idDevice2);
                context.startActivity(intent);
            }
        });

        return view;
    }
}
