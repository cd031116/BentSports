package com.cn.bent.sports.ibeacon;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cn.bent.sports.R;
import com.cn.bent.sports.base.BaseActivity;
import com.cn.bent.sports.view.activity.LoginActivity;
import com.cn.bent.sports.view.activity.SettingActivity;
import com.cn.bent.sports.widget.GameDialog;
import com.cn.bent.sports.widget.ToastDialog;
import com.minew.beacon.BeaconValueIndex;
import com.minew.beacon.BluetoothState;
import com.minew.beacon.MinewBeacon;
import com.minew.beacon.MinewBeaconManager;
import com.minew.beacon.MinewBeaconManagerListener;

import java.util.Collections;
import java.util.List;

import butterknife.Bind;

/**
 * Created by dawn on 2018/1/29.
 */

public class ScanActivity extends BaseActivity {
    private MinewBeaconManager mMinewBeaconManager;
    private static final int REQUEST_ENABLE_BT = 2;
    UserRssi comp = new UserRssi();

    @Bind(R.id.scan)
    TextView scan;


    @Override
    protected int getLayoutId() {
        return R.layout.scan_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        super.initView();

        mMinewBeaconManager = MinewBeaconManager.getInstance(this);
        checkBluetooth();
    }

    @Override
    public void initData() {
        super.initData();
    }

    /**
     * check Bluetooth state
     */
    private void checkBluetooth() {
        BluetoothState bluetoothState = mMinewBeaconManager.checkBluetoothState();
        switch (bluetoothState) {
            case BluetoothStateNotSupported:
                Toast.makeText(this, "Not Support BLE", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case BluetoothStatePowerOff:
                showBLEDialog();
                break;
            case BluetoothStatePowerOn:
                initListener();
                break;
        }
    }

    private void initListener() {

        mMinewBeaconManager.startScan();

        mMinewBeaconManager.setDeviceManagerDelegateListener(new MinewBeaconManagerListener() {
            /**
             *   if the manager find some new beacon, it will call back this method.
             *
             *  @param minewBeacons  new beacons the manager scanned
             */
            @Override
            public void onAppearBeacons(List<MinewBeacon> minewBeacons) {

            }

            /**
             *  if a beacon didn't update data in 10 seconds, we think this beacon is out of rang, the manager will call back this method.
             *
             *  @param minewBeacons beacons out of range
             */
            @Override
            public void onDisappearBeacons(List<MinewBeacon> minewBeacons) {
                /*for (MinewBeacon minewBeacon : minewBeacons) {
                    String deviceName = minewBeacon.getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Name).getStringValue();
                    Toast.makeText(getApplicationContext(), deviceName + "  out range", Toast.LENGTH_SHORT).show();
                }*/
            }

            /**
             *  the manager calls back this method every 1 seconds, you can get all scanned beacons.
             *
             *  @param minewBeacons all scanned beacons
             */
            @Override
            public void onRangeBeacons(List<MinewBeacon> minewBeacons) {
                Log.e("dasd", "dasd: " + minewBeacons.size());
                if (minewBeacons != null && minewBeacons.size() > 0) {
                    Log.e("dasd", "dasd: " + minewBeacons.size() + ",MinewBeaconValueIndex_MAC:" + minewBeacons.get(0).getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_MAC).getStringValue());
                    if ("C6:6B:36:63:D2:8E".equals(minewBeacons.get(0).getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_MAC).getStringValue()))
                        showDialogMsg("开始游戏");
                }

                Collections.sort(minewBeacons, comp);

            }

            /**
             *  the manager calls back this method when BluetoothStateChanged.
             *
             *  @param state BluetoothState
             */
            @Override
            public void onUpdateState(BluetoothState state) {
                switch (state) {
                    case BluetoothStatePowerOn:
                        Toast.makeText(getApplicationContext(), "BluetoothStatePowerOn", Toast.LENGTH_SHORT).show();
                        break;
                    case BluetoothStatePowerOff:
                        Toast.makeText(getApplicationContext(), "BluetoothStatePowerOff", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    GameDialog gameDialog;
    boolean is = true;

    private void showDialogMsg(String names) {
        new GameDialog(this, R.style.dialog, new GameDialog.OnCloseListener() {
            @Override
            public void onClick(Dialog dialog, boolean confirm) {
                if (confirm) {
                    startActivity(new Intent(ScanActivity.this, LoginActivity.class));
                } else {

                }
                dialog.dismiss();
            }
        }).setTitle("说明").show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMinewBeaconManager.stopScan();
    }

    private void showBLEDialog() {
        Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                initListener();
                break;
        }
    }
}
