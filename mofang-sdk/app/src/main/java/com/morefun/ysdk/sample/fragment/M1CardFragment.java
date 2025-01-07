package com.morefun.ysdk.sample.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.morefun.yapi.ServiceResult;
import com.morefun.yapi.card.m1card.M1CardConstants;
import com.morefun.ysdk.sample.R;
import com.morefun.ysdk.sample.device.DeviceHelper;
import com.morefun.ysdk.sample.utils.BytesUtil;
import com.morefun.ysdk.sample.utils.DialogUtils;
import com.morefun.ysdk.sample.utils.HexUtil;
import com.morefun.ysdk.sample.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class M1CardFragment extends Fragment {
    @BindView(R.id.tv_tip)
    TextView tvTip;

    @BindView(R.id.et_selector)
    EditText et_selector;

    @BindView(R.id.et_block)
    EditText et_block;

    @BindView(R.id.et_password)
    EditText et_password;

    @BindView(R.id.et_data)
    EditText et_data;

    private boolean bStop = false;

    private final String TAG = M1CardFragment.class.getName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_m1_card, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick({R.id.btn_m1Card})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_m1Card:
                if (TextUtils.isEmpty(et_selector.getText().toString())) {
                    ToastUtils.show(getContext(), "Please Input Selector");
                    return;
                }
                showResult(getString(R.string.tip_tap_card));
                searchM1Card();
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            bStop = true;
            DeviceHelper.getM1CardHandler().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void searchM1Card() {
        bStop = false;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    DialogUtils.showProgressDialog(getActivity(), getString(R.string.tip_tap_card));
                    long currentTime = System.currentTimeMillis();

                    while (true && !bStop) {
                        DeviceHelper.getM1CardHandler().close();
                        if (DeviceHelper.getM1CardHandler().open() == 0) {
                            m1Card();
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
                }
            }
        }).start();

    }

    private void m1Card() {
        int sector = 1;

        try {
            byte[] key = HexUtil.hexStringToByte(et_password.getText().toString());
            byte[] uid = new byte[64];

            StringBuilder builder = new StringBuilder();

            if (DeviceHelper.getM1CardHandler() == null) {
                DialogUtils.dismissProgressDialog(getActivity());
                DialogUtils.showAlertDialog(getActivity(), "M1 Card Handler Is Null");
                return;
            }
            try {
                sector = Integer.parseInt(et_selector.getText().toString());
            } catch (Exception e) {
                e.printStackTrace();
            }

            DialogUtils.setProgressMessage(getActivity(), "Authority...");
            builder.append("M1 Card Test\n");
            int ret = DeviceHelper.getM1CardHandler().authority(M1CardConstants.TYPE_A, sector, key, uid);
            showResult("M1 card authority:" + ret);
            if (ret != ServiceResult.Success) {
                DialogUtils.dismissProgressDialog(getActivity());
                DialogUtils.showAlertDialog(getActivity(), "M1 Card Auth Fail");
                return;
            }

            byte[] data = HexUtil.hexStringToByte(et_data.getText().toString());
            byte[] write = new byte[16];
            byte[] buf = new byte[16];
            byte[] operate = new byte[16];

            System.arraycopy(data, 0, write, 0, data.length);

            operate[4] = (byte) 0xff;
            operate[5] = (byte) 0xff;
            operate[6] = (byte) 0xff;
            operate[7] = (byte) 0xff;
            operate[12] = ~0;
            operate[13] = 0;
            operate[14] = ~0;
            operate[15] = 0;

            int blockIndex = Integer.parseInt(et_block.getText().toString());
            Log.d(TAG, "block:" + blockIndex);

            builder.append("Selector:").append(sector);
            builder.append(" Block:").append(blockIndex).append("\n");

            DialogUtils.setProgressMessage(getActivity(), "Exchange...");

            ret = DeviceHelper.getM1CardHandler().readBlock(blockIndex, buf);
            builder.append("Read Block[" + blockIndex + "] (")
                    .append(ret).append(")")
                    .append(HexUtil.bytesToHexString(buf))
                    .append("\n");

            ret = DeviceHelper.getM1CardHandler().writeBlock(blockIndex, write);
            builder.append("Write Block[" + (blockIndex) + "] (")
                    .append(ret).append(")")
                    .append(HexUtil.bytesToHexString(write))
                    .append("\n");

            ret = DeviceHelper.getM1CardHandler().readBlock(blockIndex, buf);
            builder.append("Read Block[" + (blockIndex) + "] (")
                    .append(ret).append(")")
                    .append(HexUtil.bytesToHexString(buf))
                    .append("\n");


            ret = DeviceHelper.getM1CardHandler().writeBlock(blockIndex, operate);
            builder.append("Write Block[" + (blockIndex) + "] (")
                    .append(ret).append(")")
                    .append(HexUtil.bytesToHexString(operate))
                    .append("\n");

            ret = DeviceHelper.getM1CardHandler().readBlock(blockIndex, buf);
            builder.append("Read Block[" + (blockIndex) + "] (")
                    .append(ret).append(")")
                    .append(HexUtil.bytesToHexString(buf))
                    .append("\n");

            ret = DeviceHelper.getM1CardHandler().operateBlock(M1CardConstants.OperateType.INCREMENT, blockIndex, BytesUtil.intToBytes(10), 0);
            builder.append("INCREMENT[" + (blockIndex) + "] (")
                    .append(ret).append(")")
                    .append(HexUtil.bytesToHexString(BytesUtil.intToBytes(10)))
                    .append("\n");

            ret = DeviceHelper.getM1CardHandler().readBlock(blockIndex, buf);
            builder.append("Read Block[" + (blockIndex) + "] (")
                    .append(ret).append(")")
                    .append(HexUtil.bytesToHexString(buf))
                    .append("\n");


            DeviceHelper.getM1CardHandler().operateBlock(M1CardConstants.OperateType.DECREMENT, blockIndex, BytesUtil.intToBytes(10), 0);
            builder.append("DECREMENT[" + (blockIndex) + "] (")
                    .append(ret).append(")")
                    .append(HexUtil.bytesToHexString(BytesUtil.intToBytes(10)))
                    .append("\n");

            ret = DeviceHelper.getM1CardHandler().readBlock(blockIndex, buf);
            builder.append("Read Block[" + (blockIndex) + "] (")
                    .append(ret).append(")")
                    .append(HexUtil.bytesToHexString(buf))
                    .append("\n");

            showResult(builder.toString());
            DeviceHelper.getM1CardHandler().close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DialogUtils.dismissProgressDialog(getActivity());
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
