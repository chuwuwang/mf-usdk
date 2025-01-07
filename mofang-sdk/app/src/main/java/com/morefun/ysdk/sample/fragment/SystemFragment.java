package com.morefun.ysdk.sample.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.morefun.yapi.engine.DeviceInfoConstrants;
import com.morefun.ysdk.sample.MyApplication;
import com.morefun.ysdk.sample.R;
import com.morefun.ysdk.sample.device.DeviceHelper;
import com.morefun.ysdk.sample.utils.DialogUtils;
import com.morefun.ysdk.sample.utils.FileUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SystemFragment extends Fragment {
    @BindView(R.id.tv_tip)
    TextView tvTip;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_system, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick({R.id.btn_enable_home_key, R.id.btn_disable_home_key, R.id.btn_enable_status_bar, R.id.btn_disable_status_bar,
            R.id.btn_wifi_off, R.id.btn_wifi_on, R.id.btn_sim1, R.id.btn_sim2,
            R.id.btn_force_sleep, R.id.btn_sleep_time, R.id.btn_enable_full_key, R.id.btn_disable_full_key,
            R.id.btn_reboot, R.id.btn_power_off, R.id.btn_autoStartAppEnable, R.id.btn_autoStartAppDisable,
            R.id.btn_kioskEnable, R.id.btn_kioskDisable, R.id.btn_setLauncher, R.id.btn_enableNaviBar,
            R.id.btn_disableNaviBar, R.id.btn_makeNaviItemVisible, R.id.btn_auxLcd, R.id.btn_openNFC,
            R.id.btn_closeNFC, R.id.btn_setAppPassword, R.id.btn_grantPermission, R.id.btn_disableGPS, R.id.btn_enableGPS,
            R.id.btn_disablePowerKey, R.id.btn_enablePowerKey})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_enable_home_key:
                enableHomeKey();
                break;
            case R.id.btn_disable_home_key:
                disableHomeKey();
                break;
            case R.id.btn_enable_status_bar:
                enableStatusBar();
                break;
            case R.id.btn_disable_status_bar:
                disableStatusBar();
                break;
            case R.id.btn_wifi_off:
                wifiOff();
                break;
            case R.id.btn_wifi_on:
                wifiOn();
                break;
            case R.id.btn_sim1:
                sim1();
                break;
            case R.id.btn_sim2:
                sim2();
                break;
            case R.id.btn_force_sleep:
                forceSleep();
                break;
            case R.id.btn_sleep_time:
                sleepTime();
                break;
            case R.id.btn_enable_full_key:
                enableFullKeyMode();
                break;
            case R.id.btn_disable_full_key:
                disableFullKeyMode();
                break;
            case R.id.btn_power_off:
                powerOff();
                break;
            case R.id.btn_reboot:
                reboot();
                break;
            case R.id.btn_autoStartAppEnable:
                autoStartAppEnable();
                break;
            case R.id.btn_autoStartAppDisable:
                autoStartAppDisable();
                break;
            case R.id.btn_kioskEnable:
                enableKioskMode();
                break;
            case R.id.btn_kioskDisable:
                disableKioskMode();
                break;
            case R.id.btn_setLauncher:
                setLauncher();
                break;
            case R.id.btn_enableNaviBar:
                enableNaviBar();
                break;
            case R.id.btn_disableNaviBar:
                disableNaviBar();
                break;
            case R.id.btn_makeNaviItemVisible:
                makeNaviItemVisible();
                break;
            case R.id.btn_auxLcd:
                showAuxLcdDialog(getActivity());
                break;
            case R.id.btn_openNFC:
                openNFC();
                break;
            case R.id.btn_closeNFC:
                closeNFC();
                break;
            case R.id.btn_setAppPassword:
                setAppPassword();
                break;
            case R.id.btn_grantPermission:
                grantPermission();
                break;
            case R.id.btn_disableGPS:
                disableGPS();
                break;
            case R.id.btn_enableGPS:
                enableGPS();
                break;
            case R.id.btn_disablePowerKey:
                disablePowerKey();
                break;
            case R.id.btn_enablePowerKey:
                enablePowerKey();
                break;
        }
    }


    private void disableHomeKey() {
        try {
            Bundle bundle = new Bundle();
            bundle.putBoolean(DeviceInfoConstrants.DISABLE_HOME, true);
            int ret = DeviceHelper.getDeviceService().setProperties(bundle);
            DialogUtils.showAlertDialog(getActivity(), ret == 0 ? "Success" : "Fail");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void enableHomeKey() {
        try {
            Bundle bundle = new Bundle();
            bundle.putBoolean(DeviceInfoConstrants.ENABLE_HOME, true);
            int ret = DeviceHelper.getDeviceService().setProperties(bundle);
            DialogUtils.showAlertDialog(getActivity(), ret == 0 ? "Success" : "Fail");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void disableStatusBar() {
        try {
            Bundle bundle = new Bundle();
            bundle.putBoolean(DeviceInfoConstrants.DISABLE_STATUS_BAR, true);
            int ret = DeviceHelper.getDeviceService().setProperties(bundle);
            DialogUtils.showAlertDialog(getActivity(), ret == 0 ? "Success" : "Fail");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void enableStatusBar() {
        try {
            Bundle bundle = new Bundle();
            bundle.putBoolean(DeviceInfoConstrants.ENABLE_STATUS_BAR, true);
            int ret = DeviceHelper.getDeviceService().setProperties(bundle);
            DialogUtils.showAlertDialog(getActivity(), ret == 0 ? "Success" : "Fail");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void reboot() {
        try {
            Bundle bundle = new Bundle();
            bundle.putBoolean(DeviceInfoConstrants.REBOOT, true);
            int ret = DeviceHelper.getDeviceService().setProperties(bundle);
            DialogUtils.showAlertDialog(getActivity(), ret == 0 ? "Success" : "Fail");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void powerOff() {
        try {
            Bundle bundle = new Bundle();
            bundle.putBoolean(DeviceInfoConstrants.POWER_OFF, true);
            int ret = DeviceHelper.getDeviceService().setProperties(bundle);
            DialogUtils.showAlertDialog(getActivity(), ret == 0 ? "Success" : "Fail");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }



    private void wifiOn() {
        try {
            Bundle bundle = new Bundle();
            bundle.putInt(DeviceInfoConstrants.SETTING_WIFI_SWITCH, 1);
            int ret = DeviceHelper.getDeviceService().setProperties(bundle);
            DialogUtils.showAlertDialog(getActivity(), ret == 0 ? "Success" : "Fail");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void wifiOff() {
        try {
            Bundle bundle = new Bundle();
            bundle.putInt(DeviceInfoConstrants.SETTING_WIFI_SWITCH, 0);
            int ret = DeviceHelper.getDeviceService().setProperties(bundle);
            DialogUtils.showAlertDialog(getActivity(), ret == 0 ? "Success" : "Fail");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void sim1() {
        try {
            Bundle bundle = new Bundle();
            bundle.putInt(DeviceInfoConstrants.SETTING_SIM_SLOT, 0);
            int ret = DeviceHelper.getDeviceService().setProperties(bundle);
            DialogUtils.showAlertDialog(getActivity(), ret == 0 ? "Success" : "Fail");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void sim2() {
        try {
            Bundle bundle = new Bundle();
            bundle.putInt(DeviceInfoConstrants.SETTING_SIM_SLOT, 1);
            int ret = DeviceHelper.getDeviceService().setProperties(bundle);
            DialogUtils.showAlertDialog(getActivity(), ret == 0 ? "Success" : "Fail");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void forceSleep() {
        try {
            Bundle bundle = new Bundle();
            bundle.putBoolean(DeviceInfoConstrants.FORCE_SLEEP, true);
            int ret = DeviceHelper.getDeviceService().setProperties(bundle);
            DialogUtils.showAlertDialog(getActivity(), ret == 0 ? "Success" : "Fail");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void sleepTime() {
        try {
            Bundle bundle = new Bundle();
            bundle.putInt(DeviceInfoConstrants.SLEEP_TIME, 60 * 1000);
            int ret = DeviceHelper.getDeviceService().setProperties(bundle);
            DialogUtils.showAlertDialog(getActivity(), ret == 0 ? "Success" : "Fail");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void enableFullKeyMode() {
        try {
            Bundle bundle = new Bundle();
            bundle.putBoolean(DeviceInfoConstrants.ENABLE_FULL_KEY_MODE, true);
            int ret = DeviceHelper.getDeviceService().setProperties(bundle);
            DialogUtils.showAlertDialog(getActivity(), ret == 0 ? "Success" : "Fail");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void disableFullKeyMode() {
        try {
            Bundle bundle = new Bundle();
            bundle.putBoolean(DeviceInfoConstrants.DISABLE_FULL_KEY_MODE, true);
            int ret = DeviceHelper.getDeviceService().setProperties(bundle);
            DialogUtils.showAlertDialog(getActivity(), ret == 0 ? "Success" : "Fail");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void autoStartAppEnable() {
        try {
            Bundle bundle = new Bundle();
            bundle.putBoolean(DeviceInfoConstrants.AUTO_START_APP_ENABLE, true);
            bundle.putString(DeviceInfoConstrants.AUTO_START_APP_PACKAGE, "com.morefun.ysdk.sample");

            int ret = DeviceHelper.getDeviceService().setProperties(bundle);
            DialogUtils.showAlertDialog(getActivity(), ret == 0 ? "Success" : "Fail");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void autoStartAppDisable() {
        try {
            Bundle bundle = new Bundle();
            bundle.putBoolean(DeviceInfoConstrants.AUTO_START_APP_DISABLE, true);

            int ret = DeviceHelper.getDeviceService().setProperties(bundle);
            DialogUtils.showAlertDialog(getActivity(), ret == 0 ? "Success" : "Fail");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * Long press the power button and enter the password to exit kioskMode
     * The next time the application is launched, it will still enter kioskMode
     */
    private void enableKioskMode() {
        try {
            Bundle bundle = new Bundle();
            bundle.putBoolean(DeviceInfoConstrants.KIOSK_MODE_ENABLE, true);
            bundle.putString(DeviceInfoConstrants.KIOSK_MODE_PACKAGE, "com.morefun.ysdk.sample");
            bundle.putString(DeviceInfoConstrants.KIOSK_MODE_PASSWORD, "123456");

            int ret = DeviceHelper.getDeviceService().setProperties(bundle);
            restartApp();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * Long press the power button and enter the empty password to exit kioskMode
     * The next time the application is launched, it will not enter kioskMode
     */
    private void disableKioskMode() {
        try {
            Bundle bundle = new Bundle();
            bundle.putBoolean(DeviceInfoConstrants.KIOSK_MODE_DISABLE, true);
            bundle.putString(DeviceInfoConstrants.KIOSK_MODE_PACKAGE, "com.morefun.ysdk.sample");

            int ret = DeviceHelper.getDeviceService().setProperties(bundle);
            getActivity().finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * In AndroidManifest.xml, it is necessary to declare the activity as a launcher
     * <category android:name="android.intent.category.HOME"/>
     * <category android:name="android.intent.category.DEFAULT"/>
     */
    private void setLauncher() {
        try {
            Bundle bundle = new Bundle();
            bundle.putBoolean(DeviceInfoConstrants.SET_LAUNCHER, true);
            bundle.putString(DeviceInfoConstrants.SET_LAUNCHER_PACKAGE, "com.morefun.launcher");

            int ret = DeviceHelper.getDeviceService().setProperties(bundle);
            DialogUtils.showAlertDialog(getActivity(), ret == 0 ? "Success" : "Fail");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void restartApp() {
        final Intent intent = MyApplication.getInstance().getPackageManager().getLaunchIntentForPackage(MyApplication.getInstance().getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        MyApplication.getInstance().startActivity(intent);
        System.exit(0);
    }

    private void enableNaviBar() {
        try {
            Bundle bundle = new Bundle();
            bundle.putBoolean(DeviceInfoConstrants.ENABLE_NAVI_BAR, true);

            int ret = DeviceHelper.getDeviceService().setProperties(bundle);
            DialogUtils.showAlertDialog(getActivity(), ret == 0 ? "Success" : "Fail");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void disableNaviBar() {
        try {
            Bundle bundle = new Bundle();
            bundle.putBoolean(DeviceInfoConstrants.DISABLE_NAVI_BAR, true);
            int ret = DeviceHelper.getDeviceService().setProperties(bundle);
            DialogUtils.showAlertDialog(getActivity(), ret == 0 ? "Success" : "Fail");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void makeNaviItemVisible() {
        try {
            Bundle bundle = new Bundle();
            bundle.putBoolean(DeviceInfoConstrants.SET_NAVI_BAR_KEY, true);

            bundle.putBoolean(DeviceInfoConstrants.SET_NAVI_BAR_KEY_BACK, true);
            bundle.putBoolean(DeviceInfoConstrants.SET_NAVI_BAR_KEY_HOME, false);
            bundle.putBoolean(DeviceInfoConstrants.SET_NAVI_BAR_KEY_RECENT, false);

            int ret = DeviceHelper.getDeviceService().setProperties(bundle);
            DialogUtils.showAlertDialog(getActivity(), ret == 0 ? "Success" : "Fail");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showAuxLcdDialog(Activity activity) {
        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setTitle("Aux Lcd")
                .setView(R.layout.dialog_auxlcd)
                .setCancelable(false)
                .setNegativeButton(activity.getString(R.string.tip_cancel), null)
                .create();
        dialog.show();

        ImageView iv_show = dialog.findViewById(R.id.iv_show);
        dialog.findViewById(R.id.btn_showAuxLcdBitmap).setOnClickListener(v -> {
            Bitmap bitmap = FileUtil.getImageFromAssetsFile(activity, "mf_logo.png");
            showAuxLcdBitmap(bitmap);

            iv_show.setImageBitmap(bitmap);
        });
        dialog.findViewById(R.id.btn_showAuxLcdMsg).setOnClickListener(v -> {
            Bitmap bitmap = createBitmapTxt(Color.BLUE, "Hello World", 40, Color.WHITE, Gravity.CENTER, Gravity.CENTER);
            showAuxLcdBitmap(bitmap);

            iv_show.setImageBitmap(bitmap);
        });
        dialog.findViewById(R.id.btn_clearAuxLcd).setOnClickListener(v -> {
            Bitmap bitmap = createBgBitmap(Color.WHITE);
            showAuxLcdBitmap(bitmap);

            iv_show.setImageBitmap(bitmap);
        });
        dialog.findViewById(R.id.btn_closeAuxLcd).setOnClickListener(v -> {
            closeAuxLcd();
        });
    }

    private Bitmap createBgBitmap(int color) {
        int width = 320;
        int height = 172;
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(color);
        return bitmap;
    }

    /**
     * bgColor: Background Color
     * msg: message text
     * textSize: Text Size
     * textColor: Text Color
     * horizontalGravity: Horizontal Gravity, Support Gravity.LEFT, Gravity.CENTER, Gravity.RIGHT
     * verticalGravity: Vertical Gravity, Support Gravity.TOP, Gravity.CENTER, Gravity.BOTTOM
     */
    private Bitmap createBitmapTxt(int bgColor, String msg, int textSize, int textColor, int horizontalGravity, int verticalGravity) {
        Bitmap bitmap = Bitmap.createBitmap(320, 172, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);

        Rect rect = new Rect(0, 0, 320, 172);
        Paint rectPaint = new Paint();
        rectPaint.setColor(bgColor);
        rectPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(rect, rectPaint);

        Paint textPaint = new Paint();
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);
        textPaint.setStyle(Paint.Style.FILL);

        float textWidth = textPaint.measureText(msg); //计算文本宽度
        Rect bound = new Rect();
        textPaint.getTextBounds(msg, 0, msg.length(), bound);
        int textHeight = bound.height();//计算文本高度

        float x = 0;
        float y = 0;
        switch (horizontalGravity) {
            case Gravity.LEFT:
                x = 0;
                break;
            case Gravity.CENTER:
                x = canvas.getWidth() / 2 - textWidth / 2;
                break;
            case Gravity.RIGHT:
                x = canvas.getWidth() - textWidth;
                break;
        }
        switch (verticalGravity) {
            case Gravity.TOP:
                y = textHeight;
                break;
            case Gravity.CENTER:
                y = canvas.getHeight() / 2 + textHeight / 2;
                break;
            case Gravity.BOTTOM:
                y = canvas.getHeight() - textHeight / 4;
                break;
        }

        canvas.drawText(msg, x, y, textPaint);
        return bitmap;
    }

    /**
     * note
     * To display an image, the following 3 steps must be performed
     * Image format requirements: bmp
     * Image size requirements: 320*172
     */
    private void showAuxLcdBitmap(Bitmap bitmap) {
        //step 1
        showAuxLcdLight();
        //step 2
        showAuxLcd(bitmap);
        //step 3
        showAuxLcdFlush();
    }

    /**
     * Load Images
     */
    private void showAuxLcd(Bitmap bitmap) {
        try {
            Bundle bundle = new Bundle();
            bundle.putBoolean(DeviceInfoConstrants.SHOW_AUXLCD, true);
            bundle.putParcelable(DeviceInfoConstrants.AUXLCD_BITMAP, bitmap);
            DeviceHelper.getDeviceService().setProperties(bundle);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * Startup screen
     */
    private void showAuxLcdLight() {
        try {
            Bundle bundle = new Bundle();
            bundle.putBoolean(DeviceInfoConstrants.LIGHT_AUXLCD, true);
            DeviceHelper.getDeviceService().setProperties(bundle);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * screen refresh
     */
    private void showAuxLcdFlush() {
        try {
            Bundle bundle = new Bundle();
            bundle.putBoolean(DeviceInfoConstrants.FLUSH_AUXLCD, true);
            DeviceHelper.getDeviceService().setProperties(bundle);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * Off screen
     */
    private void closeAuxLcd() {
        try {
            Bundle bundle = new Bundle();
            bundle.putBoolean(DeviceInfoConstrants.CLOSE_AUXLCD, true);
            DeviceHelper.getDeviceService().setProperties(bundle);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    private void openNFC() {
        try {
            Bundle bundle = new Bundle();
            bundle.putBoolean(DeviceInfoConstrants.SYSTEM_NFC_ENABLE, true);
            DeviceHelper.getDeviceService().setProperties(bundle);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void closeNFC() {
        try {
            Bundle bundle = new Bundle();
            bundle.putBoolean(DeviceInfoConstrants.SYSTEM_NFC_DISABLE, true);
            DeviceHelper.getDeviceService().setProperties(bundle);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void setAppPassword() {
        try {
            Bundle bundle = new Bundle();
            bundle.putBoolean(DeviceInfoConstrants.SET_APP_PASSWORD, true);
            bundle.putString(DeviceInfoConstrants.SET_APP_PASSWORD_PACKAGE, "com.morefun.ysdk.sample");
            bundle.putString(DeviceInfoConstrants.SET_APP_PASSWORD_PASSWORD, "123456");

            int ret = DeviceHelper.getDeviceService().setProperties(bundle);
            DialogUtils.showAlertDialog(getActivity(), ret == 0 ? "Success" : "Fail");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void grantPermission() {
        try {
            String[] REQUIRED_PERMISSION_LIST = new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
            };

            Bundle bundle = new Bundle();
            bundle.putBoolean(DeviceInfoConstrants.GRANT_PERMISSION, true);
            bundle.putString(DeviceInfoConstrants.GRANT_PERMISSION_PACKAGENAME, "com.morefun.ysdk.sample");

            bundle.putStringArray(DeviceInfoConstrants.GRANT_PERMISSION_PERMISSIONLIST, REQUIRED_PERMISSION_LIST);

            int ret = DeviceHelper.getDeviceService().setProperties(bundle);
            DialogUtils.showAlertDialog(getActivity(), ret == 0 ? "Success" : "Fail");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void disableGPS() {
        try {
            Bundle bundle = new Bundle();
            bundle.putBoolean(DeviceInfoConstrants.SYSTEM_GPS_DISABLE, true);
            DeviceHelper.getDeviceService().setProperties(bundle);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void enableGPS() {
        try {
            Bundle bundle = new Bundle();
            bundle.putBoolean(DeviceInfoConstrants.SYSTEM_GPS_ENABLE, true);
            DeviceHelper.getDeviceService().setProperties(bundle);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void disablePowerKey() {
        try {
            Bundle bundle = new Bundle();
            bundle.putBoolean(DeviceInfoConstrants.POWER_BUTTON_DISABLE, true);
            DeviceHelper.getDeviceService().setProperties(bundle);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void enablePowerKey() {
        try {
            Bundle bundle = new Bundle();
            bundle.putBoolean(DeviceInfoConstrants.POWER_BUTTON_ENABLE, true);
            DeviceHelper.getDeviceService().setProperties(bundle);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
