package com.example.technologycompare.helper;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.example.technologycompare.R;

import java.text.DecimalFormat;
import java.util.List;

public class DeviceAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<Device> devices;
    private LinearLayout layoutCompare;
    private int compareButtonClickCount, id1 = -1, id2 = -2;

    private Device selectedDevice1, selectedDevice2;
    private OnCompareCountChangeListener compareCountChangeListener;

    public void setOnCompareCountChangeListener(OnCompareCountChangeListener listener) {
        this.compareCountChangeListener = listener;
    }

    public interface OnCompareCountChangeListener {
        void onCompareCountChanged(int count);
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }

    public DeviceAdapter(Context context, int layout, List<Device> devices, LinearLayout layoutCompare,
                         Device Device1, Device Device2) {
        this.context = context;
        this.layout = layout;
        this.devices = devices;
        this.layoutCompare = layoutCompare; // Khởi tạo tham chiếu đến layoutCompare
        this.selectedDevice1 = Device1;
        this.selectedDevice2 = Device2;
    }

    public Device getSelectedDevice1() {
        return selectedDevice1;
    }

    public Device getSelectedDevice2() {
        return selectedDevice2;
    }

    public void setCompareButtonClickCount(int compareButtonClickCount) {
        this.compareButtonClickCount = compareButtonClickCount;
    }

    public int getCompareButtonClickCount() {
        return compareButtonClickCount;
    }

    @Override
    public int getCount() {
        return devices.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    private class ViewHolder {
        ImageView imgDevice;
        TextView tvNameDevice, tvPriceDevice;
        LinearLayout btnCompare;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, null);
            holder.imgDevice = (ImageView) view.findViewById(R.id.imgDevice);
            holder.tvNameDevice = (TextView) view.findViewById(R.id.tvNameDevice);
            holder.tvPriceDevice = (TextView) view.findViewById(R.id.tvPriceDevice);
            holder.btnCompare = view.findViewById(R.id.btnCompareDevice);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        Device device = devices.get(i);
        Glide.with(context)
                .load(device.getImageDevice())
                .into(holder.imgDevice);
        // Hiển thị tên thiết bị
        holder.tvNameDevice.setText(device.getNameDevice());

        // Hiển thị giá thiết bị

        holder.tvPriceDevice.setText(formatPrice(device.getPriceDevice()));

        //btn
        // Hiển thị bảng so sánh khi nhấp vào nút so sánh
        holder.btnCompare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Nếu lần ấn đầu tiên, hiển thị layoutCompare và thêm ảnh vào imgDevice1
                if (layoutCompare.getVisibility() == View.GONE) {
                    layoutCompare.setVisibility(View.VISIBLE);
                }

                // Lấy ID của thiết bị hiện tại
                int currentDeviceId = devices.get(i).getIdDevice();
                if (compareButtonClickCount == 0) {
                    id1 = -1;

                } else if (compareButtonClickCount == 1) {
                    id2 = -2;
                } else if (compareButtonClickCount == 2) {
                    id2 = -2;
                    id1 = -1;
                    compareButtonClickCount = 0;
                }
                if (id1 == -1) {
                    // Nếu id1 chưa được thiết lập, thiết lập id1 và tăng compareButtonClickCount
                    if (currentDeviceId != id2) {
                        id1 = currentDeviceId;
                        selectedDevice1 = devices.get(i);
                        compareButtonClickCount++;
                        setCompareButtonClickCount(compareButtonClickCount);
                    } else {
                        Toast.makeText(context, "Bạn đã chọn thiết bị này, vui lòng chọn thiết bị khác.", Toast.LENGTH_SHORT).show();
                    }
                } else if (id2 == -2) {
                    // Nếu id1 đã được thiết lập nhưng id2 chưa, kiểm tra xem id2 có trùng với id1 không
                    if (currentDeviceId != id1) {
                        id2 = currentDeviceId;
                        selectedDevice2 = devices.get(i);
                        compareButtonClickCount++;
                        setCompareButtonClickCount(compareButtonClickCount);
                    } else {
                        Toast.makeText(context, "Bạn đã chọn thiết bị này, vui lòng chọn thiết bị khác.", Toast.LENGTH_SHORT).show();
                    }
                }

                if (compareCountChangeListener != null) {
                    compareCountChangeListener.onCompareCountChanged(compareButtonClickCount);
                }
                Log.d("Adapter", "compareButtonClickCount: " + compareButtonClickCount);
                Log.d("Adapter", "selectedDevice1: " + selectedDevice1);
                Log.d("Adapter", "selectedDevice2: " + selectedDevice2);

            }
        });

        return view;
    }

    private String formatPrice(String price) {
        try {
            double priceValue = Double.parseDouble(price);
            DecimalFormat decimalFormat = new DecimalFormat("#,### đ");
            return decimalFormat.format(priceValue);
        } catch (NumberFormatException e) {
            return "Đang cập nhật";
        }
    }

}
