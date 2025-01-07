package com.morefun.nysdk.sample.fragment;

import android.os.Bundle;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.morefun.ysdk.sample.R;
import com.morefun.ysdk.sample.device.DeviceHelper;
import com.morefun.ysdk.sample.utils.BytesUtil;
import com.morefun.ysdk.sample.utils.DialogUtils;
import com.morefun.ysdk.sample.utils.HexUtil;
import com.morefun.ysdk.sample.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CpuCardFragment extends Fragment {
    @BindView(R.id.tv_tip)
    TextView tvTip;

    @BindView(R.id.et_timeout)
    EditText etTimeout;

    @BindView(R.id.et_data)
    EditText etData;

    private final String TAG = CpuCardFragment.class.getName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cpu_card, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick({R.id.btn_cpuCard, R.id.btn_rfCard, R.id.btn_typeACardClose, R.id.btn_typeAExchange})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_cpuCard:
                checkCpuCard();
                break;
            case R.id.btn_rfCard:
                checkRFCard();
                break;
            case R.id.btn_typeACardOpen:
                typeACardOpen();
                break;
            case R.id.btn_typeACardClose:
                typeACardClose();
                break;
            case R.id.btn_typeAExchange:
                typeAExchange();
                break;
        }
    }

    private void typeACardOpen() {
        if (TextUtils.isEmpty(etData.getText().toString())) {
            ToastUtils.show(getActivity(), "Please Input Timeout");
            return;
        }

        DialogUtils.showProgressDialog(getActivity(), getString(R.string.tip_dip_tap_card));
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int timeout = Integer.parseInt(etTimeout.getText().toString());
                    int ret = DeviceHelper.getCpuTypeAHandler().open(timeout);
                    DialogUtils.dismissProgressDialog(getActivity());
                    if (ret != 0) {
                        DialogUtils.showAlertDialog(getActivity(), "Open Fail");
                        return;
                    }
                    DialogUtils.showAlertDialog(getActivity(), "Open Success");
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    DialogUtils.dismissProgressDialog(getActivity());
                    ToastUtils.show(getContext(), e.getMessage());
                }
            }
        }).start();
    }

    private void typeACardClose() {
        try {
            DeviceHelper.getCpuTypeAHandler().close();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void typeAExchange() {
        if (TextUtils.isEmpty(etData.getText().toString())) {
            ToastUtils.show(getActivity(), "Data Is Empty");
            return;
        }

        DialogUtils.showProgressDialog(getActivity(), "TypeA Card Exchange...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    byte[] result = new byte[128];
                    byte[] cmd = BytesUtil.hexString2Bytes(etData.getText().toString());

                    int ret = DeviceHelper.getCpuTypeAHandler().exchangeCmd(result, cmd, cmd.length);

                    DialogUtils.dismissProgressDialog(getActivity());
                    if (ret > 0) {
                        DialogUtils.showAlertDialog(getActivity(), "Exchange Success");
                        return;
                    }
                    DialogUtils.showAlertDialog(getActivity(), "Exchange Fail");
                } catch (Exception e) {
                    e.printStackTrace();
                    DialogUtils.dismissProgressDialog(getActivity());
                    DialogUtils.showAlertDialog(getActivity(), e.getMessage());
                }
            }
        }).start();
    }

    private void checkRFCard() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    DialogUtils.showProgressDialog(getActivity(), getString(R.string.tip_tap_card));
                    long currentTime = System.currentTimeMillis();

                    while (true) {
                        DeviceHelper.getCpuRFCard().close();
                        if (DeviceHelper.getCpuRFCard().open() == 0) {
                            DialogUtils.setProgressMessage(getActivity(), "Exchange...");

                            try {
                                byte[] cmd = BytesUtil.hexString2Bytes(etData.getText().toString());
                                byte[] result = new byte[256];
                                int ret = DeviceHelper.getCpuRFCard().exchangeCmd(result, cmd, cmd.length);

                                byte[] uid = new byte[16];
                                int len = DeviceHelper.getCpuRFCard().getUid(uid);
                                Log.e(TAG, String.format("rf uid len:%d, %s", len, BytesUtil.bytes2HexString(uid)));

                                byte[] ats = new byte[64];
                                int atsLen = DeviceHelper.getCpuRFCard().getAts(ats);
                                Log.e(TAG, String.format("rf ats len:%d, %s", atsLen, BytesUtil.bytes2HexString(BytesUtil.subBytes(ats, 0, atsLen))));

                                DeviceHelper.getCpuRFCard().close();

                                DialogUtils.dismissProgressDialog(getActivity());
                                if (ret >= 0) {
                                    StringBuilder builder = new StringBuilder();

                                    builder.append("UID: ").append(BytesUtil.bytes2HexString(uid)).append("\r\n");
                                    builder.append("ATS: ").append(BytesUtil.bytes2HexString(BytesUtil.subBytes(ats, 0, atsLen))).append("\r\n");
                                    builder.append("DATA: ").append(BytesUtil.bytes2HexString(BytesUtil.subBytes(result, 0, ret))).append("\r\n");

                                    DialogUtils.showAlertDialog(getActivity(), builder.toString());
                                } else {
                                    DialogUtils.showAlertDialog(getActivity(), "Exchange Fail!");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                DialogUtils.dismissProgressDialog(getActivity());
                                DialogUtils.showAlertDialog(getActivity(), "Exchange Fail!");
                            } finally {
                                DeviceHelper.getCpuRFCard().close();
                            }
                            return;
                        }
                        if (System.currentTimeMillis() - currentTime > 10 * 1000) {
                            DialogUtils.showAlertDialog(getActivity(), "Timeout");
                            DialogUtils.dismissProgressDialog(getActivity());
                            return;
                        }
                        Thread.sleep(200);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    DialogUtils.dismissProgressDialog(getActivity());
                    ToastUtils.show(getActivity(), e.getMessage());
                }
            }
        }).start();
    }

    private void checkCpuCard() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    DialogUtils.showProgressDialog(getActivity(), getString(R.string.tip_dip_card));
                    long currentTime = System.currentTimeMillis();

                    DeviceHelper.getCpuCardHandler().setPowerOff();
                    while (true) {
                        if (DeviceHelper.getCpuCardHandler().isExist()) {
                            DialogUtils.setProgressMessage(getActivity(), "Exchange...");

                            try {
                                byte[] cmd = BytesUtil.hexString2Bytes(etData.getText().toString());

                                byte[] atr = new byte[256];
                                int atrLen = DeviceHelper.getCpuCardHandler().setPowerOn(atr);
                                if (atrLen < 0) {
                                    DialogUtils.dismissProgressDialog(getActivity());
                                    DialogUtils.showAlertDialog(getActivity(), "Power On Fail!");
                                    return;
                                }

                                byte[] result = new byte[256];
                                int ret = DeviceHelper.getCpuCardHandler().exchangeCmd(result, cmd, cmd.length);
                                DeviceHelper.getCpuCardHandler().setPowerOff();

                                DialogUtils.dismissProgressDialog(getActivity());
                                if (ret >= 0) {
                                    StringBuilder builder = new StringBuilder();
                                    builder.append("ATR: " + HexUtil.bytesToHexString(BytesUtil.subBytes(atr, 0, atrLen))).append("\n");
                                    builder.append("DATA: " + HexUtil.bytesToHexString(BytesUtil.subBytes(result, 0, ret)));

                                    DialogUtils.showAlertDialog(getActivity(), builder.toString());
                                } else {
                                    DialogUtils.showAlertDialog(getActivity(), "Exchange Fail!");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                DialogUtils.dismissProgressDialog(getActivity());
                                DialogUtils.showAlertDialog(getActivity(), "Exchange Fail!");
                            } finally {
                                DeviceHelper.getCpuCardHandler().setPowerOff();
                            }
                            return;
                        }
                        if (System.currentTimeMillis() - currentTime > 10 * 1000) {
                            DialogUtils.showAlertDialog(getActivity(), "Timeout");
                            DialogUtils.dismissProgressDialog(getActivity());
                            return;
                        }
                        Thread.sleep(200);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    DialogUtils.dismissProgressDialog(getActivity());
                    ToastUtils.show(getActivity(), e.getMessage());
                }
            }
        }).start();
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
