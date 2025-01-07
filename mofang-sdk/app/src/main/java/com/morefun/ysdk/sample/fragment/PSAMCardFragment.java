package com.morefun.ysdk.sample.fragment;

import android.os.Bundle;
import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.morefun.ysdk.sample.R;
import com.morefun.ysdk.sample.device.DeviceHelper;
import com.morefun.ysdk.sample.utils.DialogUtils;
import com.morefun.ysdk.sample.utils.HexUtil;
import com.morefun.ysdk.sample.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PSAMCardFragment extends Fragment {
    private final String TAG = PSAMCardFragment.class.getName();

    @BindView(R.id.et_psamSlot)
    EditText et_psamSlot;

    @BindView(R.id.et_data)
    EditText et_data;

    private int mSlot = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_psam_card, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick({R.id.btn_psamCard})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_psamCard:
                psamCard();
                break;
        }
    }

    private void psamCard() {
        try {
            mSlot = Integer.parseInt(et_psamSlot.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.show(getContext(), e.getMessage());
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    exchangeCmd(mSlot);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void exchangeCmd(int slot) throws RemoteException {
        try {
            DeviceHelper.getPSAM().select((byte) slot);

            String cmd = et_data.getText().toString();

            DialogUtils.showProgressDialog(getActivity(), "Reading Card...");

            DeviceHelper.getPSAM().powerOff();

            byte[] atr = new byte[64];
            boolean powerOnResult = DeviceHelper.getPSAM().powerOn(atr);
            if (!powerOnResult) {
                DialogUtils.dismissProgressDialog(getActivity());
                ToastUtils.show(getContext(), "Power On Fail!");
                return;
            }

            byte[] cmdBytes = HexUtil.hexStringToByte(cmd);
            byte[] tmp = new byte[256];
            int ret = DeviceHelper.getPSAM().exchangeCmd(tmp, cmdBytes, cmdBytes.length);

            DialogUtils.dismissProgressDialog(getActivity());
            if (ret > 0) {
                DialogUtils.showAlertDialog(getActivity(), HexUtil.bytesToHexString(HexUtil.subByte(tmp, 0, ret)));
            } else {
                ToastUtils.show(getContext(), "Exchange Fail:" + ret);
            }
        } catch (Exception e) {
            e.printStackTrace();
            DialogUtils.dismissProgressDialog(getActivity());
            ToastUtils.show(getContext(), e.getMessage());
        } finally {
            DeviceHelper.getPSAM().powerOff();
        }
    }
}
