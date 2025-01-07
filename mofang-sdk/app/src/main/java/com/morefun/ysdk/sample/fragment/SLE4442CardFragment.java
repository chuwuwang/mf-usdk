package com.morefun.ysdk.sample.fragment;

import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.morefun.yapi.card.sle4442.ISLE4442Card;
import com.morefun.ysdk.sample.R;
import com.morefun.ysdk.sample.device.DeviceHelper;
import com.morefun.ysdk.sample.utils.BytesUtil;
import com.morefun.ysdk.sample.utils.DialogUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SLE4442CardFragment extends Fragment {
    @BindView(R.id.et_address)
    EditText et_address;

    @BindView(R.id.et_data)
    EditText et_data;

    @BindView(R.id.et_key)
    EditText et_key;

    @BindView(R.id.tv_tip)
    TextView tvTip;

    @BindView(R.id.et_readLen)
    EditText et_readLen;

    private final String TAG = SLE4442CardFragment.class.getName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sle4442_card, null);
        ButterKnife.bind(this, view);

        return view;
    }

    @OnClick({R.id.btn_open, R.id.btn_close, R.id.btn_powerOn, R.id.btn_powerOff,
            R.id.btn_verify, R.id.btn_present, R.id.btn_pmRead, R.id.btn_pmWrite,
            R.id.btn_mmWrite, R.id.btn_mmRead, R.id.btn_smRead, R.id.btn_smWrite})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_open:
                open();
                break;
            case R.id.btn_close:
                close();
                break;
            case R.id.btn_powerOn:
                powerOn();
                break;
            case R.id.btn_powerOff:
                powerOff();
                break;
            case R.id.btn_verify:
                verify();
                break;
            case R.id.btn_present:
                isCardPresent();
                break;
            case R.id.btn_pmRead:
                pmRead();
                break;
            case R.id.btn_pmWrite:
                pmWrite();
                break;
            case R.id.btn_mmWrite:
                mmWrite();
                break;
            case R.id.btn_mmRead:
                mmRead();
                break;
            case R.id.btn_smRead:
                smRead();
                break;
            case R.id.btn_smWrite:
                smWrite();
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        try {
            getSLE4442Card().close();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private ISLE4442Card getSLE4442Card() {
        try {
            return DeviceHelper.getSLE4442Card();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void open() {
        try {
            boolean ret = getSLE4442Card().open();
            DialogUtils.showAlertDialog(getActivity(), ret ? "Open Success" : "Open Fail");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void close() {
        try {
            boolean ret = getSLE4442Card().close();
            DialogUtils.showAlertDialog(getActivity(), ret ? "Close Success" : "Close Fail");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void powerOn() {
        try {
            byte[] atr = new byte[256];
            int ret = getSLE4442Card().powerOn(atr);
            DialogUtils.showAlertDialog(getActivity(), ret == 0 ? "Power on Success" : "Power on Fail");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void powerOff() {
        try {
            int ret = getSLE4442Card().powerOff();
            DialogUtils.showAlertDialog(getActivity(), ret == 0 ? "Power Off Success" : "Power Off Fail");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void pmRead() {
        try {
            byte[] rst = new byte[256];
            int address = Integer.parseInt(et_address.getText().toString());
            int len = Integer.parseInt(et_readLen.getText().toString());
            int rstLen = getSLE4442Card().pmRead(address, len, rst);
            DialogUtils.showAlertDialog(getActivity(), BytesUtil.bytes2HexString(BytesUtil.subBytes(rst, 0, rstLen)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void pmWrite() {
        try {
            byte[] data = BytesUtil.hexString2Bytes(et_data.getText().toString());
            int address = Integer.parseInt(et_address.getText().toString());

            int ret = getSLE4442Card().pmWrite(address, data, data.length);
            DialogUtils.showAlertDialog(getActivity(), ret == 0 ? "Write Success" : "Write Fail");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mmRead() {
        try {
            byte[] rst = new byte[256];
            int address = Integer.parseInt(et_address.getText().toString());
            int len = Integer.parseInt(et_readLen.getText().toString());
            int rstLen = getSLE4442Card().mmRead(address, len, rst);
            DialogUtils.showAlertDialog(getActivity(), BytesUtil.bytes2HexString(BytesUtil.subBytes(rst, 0, rstLen)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mmWrite() {
        try {
            byte[] data = BytesUtil.hexString2Bytes(et_data.getText().toString());
            int address = Integer.parseInt(et_address.getText().toString());

            int ret = getSLE4442Card().mmWrite(address, data, data.length);
            DialogUtils.showAlertDialog(getActivity(), ret == 0 ? "Write Success" : "Write Fail");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void smRead() {
        try {
            byte[] rst = new byte[256];
            int address = Integer.parseInt(et_address.getText().toString());
            int len = Integer.parseInt(et_readLen.getText().toString());
            int rstLen = getSLE4442Card().smRead(address, len, rst);

            DialogUtils.showAlertDialog(getActivity(), rstLen > 0 ?
                    BytesUtil.bytes2HexString(BytesUtil.subBytes(rst, 0, rstLen)) : "SM read fail");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void smWrite() {
        try {
            byte[] data = BytesUtil.hexString2Bytes(et_key.getText().toString());
            int address = Integer.parseInt(et_address.getText().toString());

            int ret = getSLE4442Card().smWrite(address, data, data.length);
            DialogUtils.showAlertDialog(getActivity(), ret == 0 ? "Write Success" : "Write Fail");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void verify() {
        try {
            try {
                int address = Integer.parseInt(et_address.getText().toString());
                byte[] data = BytesUtil.hexString2Bytes(et_key.getText().toString());

                int ret = getSLE4442Card().auth(address, data, data.length);
                DialogUtils.showAlertDialog(getActivity(), ret == 0 ? "Write Success" : "Write Fail");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {

        }
    }

    private void isCardPresent() {
        try {
            int ret = getSLE4442Card().present();
            DialogUtils.showAlertDialog(getActivity(), ret == 0 ? "Card Exist" : "Not Exist");
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
