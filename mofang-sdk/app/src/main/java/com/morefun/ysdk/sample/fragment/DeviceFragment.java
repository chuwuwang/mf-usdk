package com.morefun.ysdk.sample.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.RemoteException;
import android.telephony.CellIdentityCdma;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.leon.lfilepickerlibrary.LFilePicker;
import com.leon.lfilepickerlibrary.utils.Constant;
import com.morefun.yapi.engine.DeviceInfoConstrants;
import com.morefun.yapi.engine.OnUninstallAppListener;
import com.morefun.ysdk.sample.R;
import com.morefun.ysdk.sample.device.DeviceHelper;
import com.morefun.ysdk.sample.utils.DialogUtils;
import com.morefun.ysdk.sample.utils.ToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DeviceFragment extends Fragment {
    @BindView(R.id.tv_tip)
    TextView tvTip;

    private final int REQUEST_CODE = 0;
    private final String TAG = DeviceFragment.class.getName();
    private String mFilePath;
    private Fragment mFragment;
    private final String ACTION_USB_HOST = "com.morefun.usbhost";
    private final String ACTION_USB_DEVICE = "com.morefun.usbdevice";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_device, null);
        ButterKnife.bind(this, view);
        mFragment = this;
        return view;
    }

    @OnClick({R.id.btn_deviceInfo, R.id.btn_install, R.id.btn_uninstall, R.id.btn_chooseFile,
            R.id.btn_ota, R.id.btn_switch_device, R.id.btn_switch_host,
            R.id.btn_openRecorder, R.id.btn_closeRecorder,})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_deviceInfo:
                getDeviceInfo();
                break;
            case R.id.btn_chooseFile:
                showFileChooser(REQUEST_CODE);
                break;
            case R.id.btn_install:
                install();
                break;
            case R.id.btn_uninstall:
                uninstall();
                break;
            case R.id.btn_ota:
                otaUpgrade();
                break;
            case R.id.btn_switch_device:
                switchToDevice();
                break;
            case R.id.btn_switch_host:
                switchToHost();
                break;
            case R.id.btn_openRecorder:
                openRecorder();
                break;
            case R.id.btn_closeRecorder:
                closeRecorder();
                break;
        }
    }

    private void getDeviceInfo() {
        try {
            String[] telInfo = telephonyCellLocation(getContext());

            Bundle devInfo = DeviceHelper.getDeviceService().getDevInfo();
            String vendor = devInfo.getString(DeviceInfoConstrants.COMMOM_VENDOR);
            String model = devInfo.getString(DeviceInfoConstrants.COMMOM_MODEL_EX);
            String osVer = devInfo.getString(DeviceInfoConstrants.COMMOM_OS_VER);
            String sn = devInfo.getString(DeviceInfoConstrants.COMMOM_SN);
            String versionCode = devInfo.getString(DeviceInfoConstrants.COMMON_SERVICE_VER);
            String sp = devInfo.getString(DeviceInfoConstrants.COMMOM_HARDWARE);
            String hardwareVer = devInfo.getString(DeviceInfoConstrants.COMMOM_HARDWARE_VER);
            String imei = devInfo.getString(DeviceInfoConstrants.IMEI);
            String imsi = devInfo.getString(DeviceInfoConstrants.IMSI);
            String iccid = devInfo.getString(DeviceInfoConstrants.ICCID);
            String imei2 = devInfo.getString(DeviceInfoConstrants.IMEI2);
            String imsi2 = devInfo.getString(DeviceInfoConstrants.IMSI2);
            String iccid2 = devInfo.getString(DeviceInfoConstrants.ICCID2);
            StringBuilder builder = new StringBuilder();

            builder.append("Manufacture:" + vendor + "\n");
            builder.append("Model:" + model + "\n");
            builder.append("System:" + osVer + "\n");
            builder.append("Sn:" + sn + "\n");
            builder.append("Sp:" + sp + "\n");
            builder.append("Version Code:" + versionCode + "\n");
            builder.append("Hardware Version:" + hardwareVer + "\n");
            if (!TextUtils.isEmpty(imei)) {
                builder.append("IMEI:" + imei + "\n");
            }

            if (!TextUtils.isEmpty(imei2)) {
                builder.append("IMEI2:" + imei2 + "\n");
            }

            if (!TextUtils.isEmpty(imsi)) {
                builder.append("IMSI:" + imsi + "\n");
            }

            if (!TextUtils.isEmpty(imsi2)) {
                builder.append("IMSI2:" + imsi2 + "\n");
            }

            if (!TextUtils.isEmpty(iccid)) {
                builder.append("ICCID:" + iccid + "\n");
            }

            if (!TextUtils.isEmpty(iccid2)) {
                builder.append("ICCID2:" + iccid2 + "\n");
            }
            builder.append("App Key:" + devInfo.getString("AppKey") + "\n");
            builder.append("Pub Key:" + devInfo.getString("PubKey") + "\n");

            String mcc = String.format("%03d", Integer.parseInt(telInfo[3]));
            String mnc = String.format("%02d", Integer.parseInt(telInfo[2]));
            String lac = String.format("%d", Integer.parseInt(telInfo[1]));
            String cid = String.format("%d", Integer.parseInt(telInfo[0]));

            builder.append("MCC:" + mcc + "\n");
            builder.append("MNC:" + mnc + "\n");
            builder.append("LAC:" + lac + "\n");
            builder.append("CID:" + cid + "\n");

            DialogUtils.showAlertDialog(getActivity(), builder.toString());
        } catch (Exception e) {
        }
    }

    private void showFileChooser(int requestCode) {
        new LFilePicker()
                .withSupportFragment(mFragment)
                .withRequestCode(requestCode)
                .withTitle("Please Choose APK")
                .withMutilyMode(false)
                .withStartPath(Environment.getExternalStorageDirectory().getPath())//指定初始显示路径
                .withNotFoundBooks("Select at least one file")
                .withChooseMode(true)
                .withFileFilter(new String[]{".apk", ".zip"})
                .start();
    }

    private void install() {
        if (mFilePath == null) {
            ToastUtils.show(getContext(), "Please Choose An APK");
            return;
        }

        try {
            Log.i(TAG, String.format("File Path:%s", mFilePath));
            DeviceHelper.getDeviceService().installApp(mFilePath, "", "");
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    private void uninstall() {
        try {
            String packageName = "Name of your application package";

            DeviceHelper.getDeviceService().uninstallApp(packageName, new OnUninstallAppListener.Stub() {
                @Override
                public void onUninstallAppResult(int code) throws RemoteException {
                    Log.e(TAG, String.format("onUninstallAppResult:%d", code));
                }
            });
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    private void otaUpgrade() {
        if (mFilePath == null) {
            ToastUtils.show(getContext(), "Please choose an OTA package");
            return;
        }
        Intent intent = new Intent("com.morefun.upgrade");
        intent.putExtra("filepath", mFilePath);
        getActivity().sendBroadcast(intent);
    }

    private void switchToHost() {
        Intent intent = new Intent(ACTION_USB_HOST);
        getActivity().sendBroadcast(intent);
    }

    private void switchToDevice() {
        Intent intent = new Intent(ACTION_USB_DEVICE);
        getActivity().sendBroadcast(intent);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            List<String> list = data.getStringArrayListExtra(Constant.RESULT_INFO);
            if (requestCode == REQUEST_CODE) {
                mFilePath = list.get(0);
                tvTip.setText("File Path:" + mFilePath + "\n");
            }
        }
    }

    @SuppressLint("MissingPermission")
    private String[] telephonyCellLocation(Context context) {
        String[] ret = new String[4];
        ret[0] = "0";
        ret[1] = "0";
        ret[2] = "0";
        ret[3] = "0";
        try {
            TelephonyManager tel = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

            List<CellInfo> cellInfos = tel.getAllCellInfo();
            if (cellInfos != null) {
                for (CellInfo cellInfo : cellInfos) {
                    if (cellInfo instanceof CellInfoGsm) {
                        CellIdentityGsm cellIdentityGsm = ((CellInfoGsm) cellInfo).getCellIdentity();
                        int cid = cellIdentityGsm.getCid();
                        int lac = cellIdentityGsm.getLac();
                        int mcc = cellIdentityGsm.getMcc();
                        int mnc = cellIdentityGsm.getMnc();
                        Log.d(TAG, "cid:" + cid + " lac:" + lac + " mcc:" + mcc + " mnc:" + mnc);
                        ret[0] = String.valueOf(cid);
                        ret[1] = String.valueOf(lac);
                        ret[2] = String.valueOf(mnc);
                        ret[3] = String.valueOf(mcc);
                        return ret;
                    } else if (cellInfo instanceof CellInfoCdma) {
                        CellIdentityCdma cellIdentityCdma = ((CellInfoCdma) cellInfo).getCellIdentity();
                        int cid = cellIdentityCdma.getBasestationId();
                        int lac = cellIdentityCdma.getSystemId();
                        int mcc = cellIdentityCdma.getNetworkId();
                        int mnc = cellIdentityCdma.getSystemId();
                        Log.d(TAG, "cid:" + cid + " lac:" + lac + " mcc:" + mcc + " mnc:" + mnc);
                        ret[0] = String.valueOf(cid);
                        ret[1] = String.valueOf(lac);
                        ret[2] = String.valueOf(mnc);
                        ret[3] = String.valueOf(mcc);
                        return ret;
                    } else if (cellInfo instanceof CellInfoLte) {
                        CellIdentityLte cellIdentityLte = ((CellInfoLte) cellInfo).getCellIdentity();
                        int ci = cellIdentityLte.getCi();
                        int tac = cellIdentityLte.getTac();
                        int mcc = cellIdentityLte.getMcc();
                        int mnc = cellIdentityLte.getMnc();
                        Log.d(TAG, "cid:" + ci + " lac:" + tac + " mcc:" + mcc + " mnc:" + mnc);
                        ret[0] = String.valueOf(ci);
                        ret[1] = String.valueOf(tac);
                        ret[2] = String.valueOf(mnc);
                        ret[3] = String.valueOf(mcc);
                        return ret;
                    } else if (cellInfo instanceof CellInfoWcdma) {
                        CellIdentityWcdma cellIdentityWcdma = ((CellInfoWcdma) cellInfo).getCellIdentity();
                        int cid = cellIdentityWcdma.getCid();
                        int lac = cellIdentityWcdma.getLac();
                        int mcc = cellIdentityWcdma.getMcc();
                        int mnc = cellIdentityWcdma.getMnc();
                        Log.d(TAG, "cid:" + cid + " lac:" + lac + " mcc:" + mcc + " mnc:" + mnc);
                        ret[0] = String.valueOf(cid);
                        ret[1] = String.valueOf(lac);
                        ret[2] = String.valueOf(mnc);
                        ret[3] = String.valueOf(mcc);
                        return ret;
                    }
                }
            } else {
                Log.d(TAG, "cellList is null");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    private void openRecorder() {
        try {
            DeviceHelper.getDeviceService().getLogRecorder().openRecorder(new Bundle());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void closeRecorder() {
        try {
            DeviceHelper.getDeviceService().getLogRecorder().closeRecorder(new Bundle());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
