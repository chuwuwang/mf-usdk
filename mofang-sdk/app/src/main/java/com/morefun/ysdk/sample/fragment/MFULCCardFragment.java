package com.morefun.ysdk.sample.fragment;

import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.morefun.ysdk.sample.R;
import com.morefun.ysdk.sample.device.DeviceHelper;
import com.morefun.ysdk.sample.utils.BytesUtil;
import com.morefun.ysdk.sample.utils.Cipher;
import com.morefun.ysdk.sample.utils.DialogUtils;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MFULCCardFragment extends Fragment {
    @BindView(R.id.tv_tip)
    TextView tvTip;

    private final String TAG = MFULEv1CardFragment.class.getName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mful_c_card, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick({R.id.btn_open, R.id.btn_close, R.id.btn_read, R.id.btn_write,
            R.id.btn_compatible_write, R.id.btn_auth})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_open:
                open();
                break;
            case R.id.btn_close:
                close();
                break;
            case R.id.btn_read:
                read();
                break;
            case R.id.btn_write:
                write();
                break;
            case R.id.btn_compatible_write:
                compatibleWrite();
                break;
            case R.id.btn_auth:
                auth();
                break;

        }
    }

    @Override
    public void onPause() {
        super.onPause();

        try {
            DeviceHelper.getMFULCCard().close();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private void open() {
        try {
            boolean ret = DeviceHelper.getMFULCCard().open();
            DialogUtils.showAlertDialog(getActivity(), ret ? "Open Success" : "Open Fail");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private void close() {
        try {
            boolean ret = DeviceHelper.getMFULCCard().close();
            DialogUtils.showAlertDialog(getActivity(), ret ? "Close Success" : "Close Fail");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private void read() {
        try {
            int page = 0;
            byte[] result = new byte[16];
            int ret = DeviceHelper.getMFULCCard().read(page, result);
            DialogUtils.showAlertDialog(getActivity(), BytesUtil.bytes2HexString(result));
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private void write() {
        try {
            int page = 0;
            byte[] data = "12345678".getBytes();
            int ret = DeviceHelper.getMFULCCard().write(page, data);
            DialogUtils.showAlertDialog(getActivity(), ret >= 0 ? "Write Success" : "Write Fail");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private void compatibleWrite() {
        try {
            int page = 0;
            byte[] data = "12345678".getBytes();
            int ret = DeviceHelper.getMFULCCard().writeCompatible(page, data, data.length);
            DialogUtils.showAlertDialog(getActivity(), ret >= 0 ? "Write Success" : "Write Fail");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private void auth() {
        try {
            byte[] KEY = BytesUtil.hexString2Bytes("49454D4B41455242214E4143554F5946");
            byte[] randomA = BytesUtil.hexString2Bytes("A8AF3B256C75ED40");

            byte[] auth1Rst = new byte[8];
            DeviceHelper.getMFULCCard().auth1(auth1Rst);
            Log.d(TAG, "auth1 rst:" + BytesUtil.bytes2HexString(auth1Rst));

            byte[] randomB = new Cipher().setAlgorithm(Cipher.Algorithm.DES_Triple)
                    .setMode(Cipher.Mode.CBC)
                    .setKey(KEY)
                    .setPadding(Cipher.Padding.NoPadding)
                    .setData(auth1Rst)
                    .deCrypt();

            Log.d(TAG, "randomB:" + BytesUtil.bytes2HexString(randomB));
            byte[] randomBConvert = BytesUtil.first2Last(randomB);
            Log.d(TAG, "randomB convert:" + BytesUtil.bytes2HexString(randomBConvert));

            Log.d(TAG, "data:" + BytesUtil.bytes2HexString(BytesUtil.merage(randomA, randomBConvert)));

            byte[] auth2 = new Cipher()
                    .setAlgorithm(Cipher.Algorithm.DES_Triple)
                    .setMode(Cipher.Mode.CBC)
                    .setKey(KEY)
                    .setPadding(Cipher.Padding.NoPadding)
                    .setData(BytesUtil.merage(randomA, randomBConvert))
                    .setIv(auth1Rst)
                    .encrypt();

            Log.d(TAG, "auth2 encrypt:" + BytesUtil.bytes2HexString(auth2));

            byte[] ivAuth2 = new byte[8];
            System.arraycopy(auth2, 8, ivAuth2, 0, 8);

            byte[] auth2Rst = new byte[8];
            DeviceHelper.getMFULCCard().auth2(auth2, auth2Rst);

            Log.d(TAG, "auth2 result:" + BytesUtil.bytes2HexString(auth2Rst));
            Log.d(TAG, "iv auth2:" + BytesUtil.bytes2HexString(ivAuth2));
            byte[] plain = new Cipher().setAlgorithm(Cipher.Algorithm.DES_Triple)
                    .setMode(Cipher.Mode.CBC)
                    .setKey(KEY)
                    .setData(auth2Rst)
                    .setIv(ivAuth2)
                    .setPadding(Cipher.Padding.NoPadding)
                    .deCrypt();
            Log.d(TAG, "plain random:" + BytesUtil.bytes2HexString(plain));

            if (Arrays.equals(randomA, BytesUtil.last2First(plain))) {
                DialogUtils.showAlertDialog(getActivity(), "Auth Success");
            } else {
                DialogUtils.showAlertDialog(getActivity(), "Auth Fail");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void showResult(final String text) {
        Log.d(TAG, text);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvTip.append("### " + text + "\r\n");
            }
        });
    }
}
