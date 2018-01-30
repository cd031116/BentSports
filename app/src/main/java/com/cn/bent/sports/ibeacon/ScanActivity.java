package com.cn.bent.sports.ibeacon;

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
import com.minew.beaconset.BluetoothState;
import com.minew.beaconset.MinewBeacon;
import com.minew.beaconset.MinewBeaconManager;
import com.minew.beaconset.MinewBeaconManagerListener;

import java.util.Collections;
import java.util.List;

import butterknife.Bind;

/**
 * Created by dawn on 2018/1/29.
 */

public class ScanActivity extends BaseActivity {
    private MinewBeaconManager mMinewBeaconManager;
    private static final int REQUEST_ENABLE_BT = 2;
    private boolean isScanning;
    private BeaconListAdapter mAdapter;
    UserRssi comp = new UserRssi();

    @Bind(R.id.scan)
    TextView scan;
    @Bind(R.id.recyeler)
    RecyclerView mRecycle;


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

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecycle.setLayoutManager(layoutManager);
        mAdapter = new BeaconListAdapter();
        mRecycle.setAdapter(mAdapter);
        mRecycle.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager
                .HORIZONTAL));

        mMinewBeaconManager = MinewBeaconManager.getInstance(this);
        checkBluetooth();
        initListener();
    }

    @Override
    public void initData() {
        super.initData();
    }

    private void initManager() {
        mMinewBeaconManager = MinewBeaconManager.getInstance(this);
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
                break;
        }
    }

    private void initListener() {
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isScanning) {
                    isScanning = false;
                    scan.setText("Start");
                    if (mMinewBeaconManager != null) {
                        mMinewBeaconManager.stopScan();
                    }
                } else {
                    isScanning = true;
                    scan.setText("Stop");
                    try {
                        mMinewBeaconManager.startScan();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        mMinewBeaconManager.setMinewbeaconManagerListener(new MinewBeaconManagerListener() {
            @Override
            public void onUpdateBluetoothState(BluetoothState state) {
                switch (state) {
                    case BluetoothStatePowerOff:
                        Toast.makeText(getApplicationContext(), "bluetooth off", Toast.LENGTH_SHORT).show();
                        break;
                    case BluetoothStatePowerOn:
                        Toast.makeText(getApplicationContext(), "bluetooth on", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onRangeBeacons(List<MinewBeacon> beacons) {
                Log.e("dasd", "dasd: " + beacons.size());
                Collections.sort(beacons, comp);
                mAdapter.setData(beacons);
//                if (beacons != null && beacons.size() > 0) {
//                    Log.e("dasd", "距离:" + beacons.get(0).getDistance() + ",did:" + beacons.get(0).getDeviceId());
//                    scan.setText("距离:" + beacons.get(0).getDistance() + ",did:" + beacons.get(0).getDeviceId());
//                }
            }

            @Override
            public void onAppearBeacons(List<MinewBeacon> beacons) {

            }

            @Override
            public void onDisappearBeacons(List<MinewBeacon> beacons) {

            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isScanning) {
            mMinewBeaconManager.stopScan();
        }

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
                break;
        }
    }
}
