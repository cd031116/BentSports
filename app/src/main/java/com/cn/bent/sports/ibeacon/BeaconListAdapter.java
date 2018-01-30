package com.cn.bent.sports.ibeacon;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cn.bent.sports.R;
import com.minew.beaconset.MinewBeacon;

import java.util.List;

//import com.minew.beacon.MinewBeacon;


public class BeaconListAdapter extends RecyclerView.Adapter<BeaconListAdapter.MyViewHolder> {

    private List<MinewBeacon> mMinewBeacons;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.scan_item, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.setDataAndUi(mMinewBeacons.get(position));
    }

    @Override
    public int getItemCount() {
        if (mMinewBeacons != null) {
            return mMinewBeacons.size();
        }
        return 0;
    }

    public void setData(List<MinewBeacon> minewBeacons) {
        this.mMinewBeacons = minewBeacons;
        notifyDataSetChanged();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView mDevice_mac;
        private MinewBeacon mMinewBeacon;
        private final TextView mDevice_temphumi;
        private final TextView mDevice_name;
        private final TextView mDevice_uuid;
        private final TextView mDevice_other;


        public MyViewHolder(View itemView) {
            super(itemView);
            mDevice_name = (TextView) itemView.findViewById(R.id.device_name);
            mDevice_uuid = (TextView) itemView.findViewById(R.id.device_uuid);
            mDevice_other = (TextView) itemView.findViewById(R.id.device_other);
            mDevice_temphumi = (TextView) itemView.findViewById(R.id.device_temphumi);
            mDevice_mac = (TextView) itemView.findViewById(R.id.device_mac);
        }

        public void setDataAndUi(MinewBeacon minewBeacon) {
            mMinewBeacon = minewBeacon;
            mDevice_name.setText(minewBeacon.getName());
            mDevice_uuid.setText(minewBeacon.getUuid());
            mDevice_mac.setText(minewBeacon.getMacAddress());
            mDevice_other.setText(minewBeacon.getDistance()+"");
//            mDevice_name.setText(mMinewBeacon.getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Name).getStringValue());
////            mDevice_name.setText(mMinewBeacon.getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_InRage).getStringValue());
////            mDevice_mac.setText(mMinewBeacon.getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_MAC).getStringValue());
//            mDevice_uuid.setText("UUID:" + mMinewBeacon.getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_UUID).getStringValue());
//            String battery = mMinewBeacon.getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_BatteryLevel).getStringValue();
//            int batt = Integer.parseInt(battery);
//            if (batt > 100) {
//                batt = 100;
//            }

//            String format = String.format("InRage:%s Major:%s Minor:%s Rssi:%s Battery:%s",
//                    mMinewBeacon.getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_InRage).getStringValue(),
//                    mMinewBeacon.getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Major).getStringValue(),
//                    mMinewBeacon.getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Minor).getStringValue(),
//                    mMinewBeacon.getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_RSSI).getStringValue(),
//                    batt + "");
//            mDevice_other.setText(format);

//            if (mMinewBeacon.getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Humidity).getFloatValue() == 0 &&
//                    mMinewBeacon.getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Temperature).getFloatValue() == 0) {
//                mDevice_temphumi.setVisibility(View.GONE);
//            } else {
//                mDevice_temphumi.setVisibility(View.VISIBLE);
//                String temphumi = String.format("Temperature:%s ℃   Humidity:%s ",
//                        mMinewBeacon.getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Temperature).getFloatValue() + "",
//                        mMinewBeacon.getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Humidity).getFloatValue() + "");
//
//                mDevice_temphumi.setText(temphumi + "%");
//            }

        }
    }
}
