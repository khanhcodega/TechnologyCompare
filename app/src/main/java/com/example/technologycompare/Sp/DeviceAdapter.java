package com.example.technologycompare.Sp;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.technologycompare.MainActivity;
import com.example.technologycompare.R;
import com.example.technologycompare.ui.BuyDeviceActivity;
import com.example.technologycompare.ui.TestActivity;

import java.text.DecimalFormat;
import java.util.List;

// Custom adapter class
public class DeviceAdapter extends ArrayAdapter<Device> {
    private Context context;
    private List<Device> devices;

    public DeviceAdapter(Context context, int resource, List<Device> devices) {
        super(context, resource, devices);
        this.context = context;
        this.devices = devices;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(context).inflate(R.layout.layout_item_device1, parent, false);
        }

        ImageView imageView = listItem.findViewById(R.id.imgDevice);
        TextView tvName = listItem.findViewById(R.id.tvNameDevice);
        TextView tvPrice = listItem.findViewById(R.id.tvPriceDevice);
        TextView tvBattery = listItem.findViewById(R.id.tvBattery);
        TextView tvCamera = listItem.findViewById(R.id.tvCamera);
        TextView tvMemory = listItem.findViewById(R.id.tvMemory);
        TextView tvRam = listItem.findViewById(R.id.tvRam);
        TextView tvProcessor = listItem.findViewById(R.id.tvProcessor);
        TextView tvScreen = listItem.findViewById(R.id.tvScreen);
        Device currentDevice = devices.get(position);
        LinearLayout btnBuy = listItem.findViewById(R.id.btnBuy);
        RelativeLayout relativeLayout = listItem.findViewById(R.id.relativeLayout); // Đổi relativeLayout thành ID của RelativeLayout trong file XML của bạn

        Glide.with(context).load(currentDevice.getImageUrl()).into(imageView);
        tvName.setText(currentDevice.getName());
        tvPrice.setText(formatPrice(currentDevice.getPrice()));
        tvBattery.setText("Battery: " + currentDevice.getBattery());
        tvCamera.setText("Camera: " + currentDevice.getCamera());
        tvMemory.setText("Memory: " + currentDevice.getStorage());
        tvRam.setText("RAM: " + currentDevice.getRam());
        tvProcessor.setText("Processor: " + currentDevice.getChip());
        tvScreen.setText("Screen: " + currentDevice.getScreen());

        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String link = currentDevice.getLink();
                // Thực hiện hành động mong muốn với link ở đây, ví dụ chuyển đến một trang web hoặc màn hình mới
                // Ví dụ:
                Intent intent = new Intent(context, BuyDeviceActivity.class);
                intent.putExtra("link", link);
                context.startActivity(intent);
            }
        });

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Device clickedDevice = devices.get(position); // Lấy thiết bị được nhấn
                String id = clickedDevice.getId(); // Lấy ID của thiết bị
                Intent intent = new Intent(context, TestActivity.class);
                intent.putExtra("PRODUCT_ID", Integer.parseInt(id)); // Chuyển ID dưới dạng int
                context.startActivity(intent); // Sử dụng context để gọi startActivity
                Toast.makeText(context, "Clicked on the item, link: " + id, Toast.LENGTH_SHORT).show();
            }
        });





        return listItem;

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
