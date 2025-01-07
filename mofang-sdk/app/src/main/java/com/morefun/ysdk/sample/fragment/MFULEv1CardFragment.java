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
import com.morefun.ysdk.sample.utils.DialogUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MFULEv1CardFragment extends Fragment {
    @BindView(R.id.tv_tip)
    TextView tvTip;

    private final String TAG = MFULEv1CardFragment.class.getName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mful_ev1_card, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick({R.id.btn_open, R.id.btn_close, R.id.btn_get_version, R.id.btn_read, R.id.btn_fast_read,
            R.id.btn_write, R.id.btn_compatible_write, R.id.btn_read_count, R.id.btn_increase_count,
            R.id.btn_auth, R.id.btn_read_sign, R.id.btn_tear, R.id.btn_vcsl})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_open:
                open();
                break;
            case R.id.btn_close:
                close();
                break;
            case R.id.btn_get_version:
                getVersion();
                break;
            case R.id.btn_read:
                read();
                break;
            case R.id.btn_fast_read:
                fastRead();
                break;
            case R.id.btn_write:
                write();
                break;
            case R.id.btn_compatible_write:
                compatibleWrite();
                break;
            case R.id.btn_read_count:
                readCount();
                break;
            case R.id.btn_increase_count:
                increaseCount();
                break;
            case R.id.btn_auth:
                auth();
                break;
            case R.id.btn_read_sign:
                readSign();
                break;
            case R.id.btn_tear:
                tear();
                break;
            case R.id.btn_vcsl:
                vcsl();
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        try {
            DeviceHelper.getMFULEv1Card().close();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private void open() {
        try {
            boolean ret = DeviceHelper.getMFULEv1Card().open();
            DialogUtils.showAlertDialog(getActivity(), ret ? "Open Success" : "Open Fail");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private void close() {
        try {
            boolean ret = DeviceHelper.getMFULEv1Card().close();
            DialogUtils.showAlertDialog(getActivity(), ret ? "Close Success" : "Close Fail");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private void getVersion() {
        try {
            byte[] version = DeviceHelper.getMFULEv1Card().getVersion();
            DialogUtils.showAlertDialog(getActivity(), BytesUtil.bytes2HexString(version));
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private void read() {
        try {
            int page = 0;
            byte[] result = new byte[16];
            int ret = DeviceHelper.getMFULEv1Card().read(page, result);
            DialogUtils.showAlertDialog(getActivity(), BytesUtil.bytes2HexString(result));
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private void fastRead() {
        try {
            int page = 0;
            int no = 0;
            byte[] result = new byte[16];
            int ret = DeviceHelper.getMFULEv1Card().fastRead(page, no, result);
            DialogUtils.showAlertDialog(getActivity(), BytesUtil.bytes2HexString(result));
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private void write() {
        try {
            int page = 0;
            byte[] data = "12345678".getBytes();
            int ret = DeviceHelper.getMFULEv1Card().write(page, data);
            DialogUtils.showAlertDialog(getActivity(), ret >= 0 ? "Write Success" : "Write Fail");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private void compatibleWrite() {
        try {
            int page = 0;
            byte[] data = "12345678".getBytes();
            int ret = DeviceHelper.getMFULEv1Card().compatibleWrite(page, data, data.length);
            DialogUtils.showAlertDialog(getActivity(), ret >= 0 ? "Write Success" : "Write Fail");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private void readCount() {
        try {
            int page = 0;
            byte[] result = new byte[4];
            int ret = DeviceHelper.getMFULEv1Card().readCount(page, result);
            DialogUtils.showAlertDialog(getActivity(), BytesUtil.bytes2HexString(result));
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private void increaseCount() {
        try {
            byte[] data = new byte[4];
            byte[] result = new byte[16];
            int ret = DeviceHelper.getMFULEv1Card().increaseCount(data, result);
            DialogUtils.showAlertDialog(getActivity(), BytesUtil.bytes2HexString(result));
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private void auth() {
        try {
            byte[] password = "1234".getBytes();
            byte[] result = new byte[2];
            int ret = DeviceHelper.getMFULEv1Card().auth(password, result);
            DialogUtils.showAlertDialog(getActivity(), BytesUtil.bytes2HexString(result));
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private void readSign() {
        try {
            byte[] result = new byte[32];
            int ret = DeviceHelper.getMFULEv1Card().readSign(result);
            DialogUtils.showAlertDialog(getActivity(), BytesUtil.bytes2HexString(result));
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private void tear() {
        try {
            int addr = 0;
            byte[] result = new byte[1];
            int ret = DeviceHelper.getMFULEv1Card().detectTear(addr, result);
            DialogUtils.showAlertDialog(getActivity(), BytesUtil.bytes2HexString(result));
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private void vcsl() {
        try {
            byte[] iid = new byte[16];
            byte[] pcd = new byte[4];

            byte[] result = new byte[1];
            int ret = DeviceHelper.getMFULEv1Card().vcsl(iid, pcd, result);
            DialogUtils.showAlertDialog(getActivity(), BytesUtil.bytes2HexString(result));
        } catch (RemoteException e) {
            throw new RuntimeException(e);
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
