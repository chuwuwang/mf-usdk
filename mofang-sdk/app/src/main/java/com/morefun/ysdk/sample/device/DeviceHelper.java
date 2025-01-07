package com.morefun.ysdk.sample.device;

import android.annotation.SuppressLint;
import android.os.RemoteException;
import android.util.Log;

import com.morefun.yapi.card.at24cxx.IAT24CxxCard;
import com.morefun.yapi.card.cpu.CPUTypeAHandler;
import com.morefun.yapi.card.cpu.ICpuCard;
import com.morefun.yapi.card.cpu.ICpuRFCard;
import com.morefun.yapi.card.emulate.EmulateCardHandler;
import com.morefun.yapi.card.felica.IFelica;
import com.morefun.yapi.card.m1card.IM1Card;
import com.morefun.yapi.card.mfulc.IMFULCCard;
import com.morefun.yapi.card.mfulev1.IMFULEv1Card;
import com.morefun.yapi.card.ntag215.INTAG215Card;
import com.morefun.yapi.card.psam.IPSAM;
import com.morefun.yapi.card.sle4442.ISLE4442Card;
import com.morefun.yapi.device.beeper.Beeper;
import com.morefun.yapi.device.dukpt.IDukpt;
import com.morefun.yapi.device.hsm.Hsm;
import com.morefun.yapi.device.led.LEDDriver;
import com.morefun.yapi.device.logrecorder.LogRecorder;
import com.morefun.yapi.device.ped.IPed;
import com.morefun.yapi.device.pinpad.PinPad;
import com.morefun.yapi.device.pinpad.SecureArea;
import com.morefun.yapi.device.printer.MultipleAppPrinter;
import com.morefun.yapi.device.printer.Printer;
import com.morefun.yapi.device.reader.icc.IccCardReader;
import com.morefun.yapi.device.reader.mag.MagCardReader;
import com.morefun.yapi.device.scanner.InnerScanner;
import com.morefun.yapi.device.serialport.SerialPort;
import com.morefun.yapi.device.serialport.SerialPortDriver;
import com.morefun.yapi.device.tpm.TpmManager;
import com.morefun.yapi.emv.EmvHandler;
import com.morefun.yapi.emv.EmvRupayService;
import com.morefun.yapi.engine.DeviceServiceEngine;
import com.morefun.ysdk.sample.MyApplication;
import com.morefun.ysdk.sample.utils.ToastUtils;


public class DeviceHelper {

    private final static String TAG = DeviceHelper.class.getName();

    private static PinPad pinpad;
    private static IccCardReader iccCardReader;

    private static SecureArea mSecureArea;
    private static Hsm mHsm;
    private static MagCardReader magCardReader;
    private static LEDDriver ledDriver;
    private static MultipleAppPrinter printer;
    private static SerialPortDriver serialPortDriver;
    private static Beeper beeper;
    private static EmvHandler emvHandler;
    private static InnerScanner innerScanner;
    private static LogRecorder logRecorder;
    private static EmvRupayService emvRupayService;
    private static Printer printer1;
    private static IPed ped;
    private static CPUTypeAHandler cpuTypeAHandler;
    private static EmulateCardHandler emulateCardHandler;
    private static MyApplication application;
    private static SerialPort usbSerialPort;
    private static IDukpt dukpt;
    private static TpmManager tpmManager;
    private static IAT24CxxCard at24CxxCard;
    private static IM1Card m1CardHandler;
    private static ICpuCard cpuCard;
    private static ICpuRFCard cpuRFCard;
    private static IFelica felica;
    private static IPSAM psam;
    private static IMFULEv1Card mfulEv1Card;
    private static IMFULCCard mfulCCard;
    private static ISLE4442Card sle4442Card;
    private static INTAG215Card ntag215Card;

    @SuppressLint("NewApi")
    public static void initDevices(MyApplication application) throws RemoteException {
        DeviceHelper.application = application;

        if (application == null) {
            return;
        }

        if (application.getDeviceService() != null) {
            try {
                pinpad = getPinpad();
                magCardReader = getMagCardReader();
                ledDriver = getLedDriver();
                printer = getPrinter();
                beeper = getBeeper();
                emvHandler = getEmvHandler();
                innerScanner = getInnerScanner();
                logRecorder = getLogRecorder();
                emvRupayService = getEmvRupayService();
                cpuTypeAHandler = getCpuTypeAHandler();
                emulateCardHandler = getEmulateCardHandler();
                dukpt = getDukpt();
                tpmManager = getTpmManager();
                at24CxxCard = getAT24CxxCard();
                cpuCard = getCpuCardHandler();
                cpuRFCard = getCpuRFCard();
                felica = getFelica();
                psam = getPSAM();
                mfulEv1Card = getMFULEv1Card();
                mfulCCard = getMFULCCard();
                sle4442Card = getSLE4442Card();
                ntag215Card = getNTAGCard();
                m1CardHandler = getM1CardHandler();
            } catch (RemoteException e) {
                e.printStackTrace();
                throw e;
            }
        } else {
            application.bindDeviceService();
            reset();
        }
    }

    public static DeviceServiceEngine getDeviceService() throws RemoteException {
        Log.d(TAG, "application:" + application);
        if (application == null) {
            ToastUtils.show(MyApplication.getInstance(), "application is null, please try again later.");
            throw new RemoteException("application is null, please try again later.");
        }

        if (application.getDeviceService() == null) {
            checkState();
        }

        return application.getDeviceService();
    }

    public static void checkState() throws RemoteException {
        if (application == null) {
            Log.d(TAG, "============checkState application == null");
            throw new RemoteException("Please restart the application.");
        }

        if (application.getDeviceService() == null) {
            application.bindDeviceService();
            reset();
            throw new RemoteException("Device service connection failed, please try again later.");
        }
    }


    @SuppressLint("NewApi")
    public static PinPad getPinpad() throws RemoteException {
        if (pinpad == null) {
            checkState();
            try {
                return application.getDeviceService().getPinPad();
            } catch (RemoteException e) {
                throw new RemoteException("PinPad service acquisition failed, please try again later.");
            }
        } else {
            return pinpad;
        }
    }

    @SuppressLint("NewApi")
    public static IPed getPed() throws RemoteException {
        if (ped == null) {
            checkState();
            try {
                return application.getDeviceService().getPed();
            } catch (RemoteException e) {
                throw new RemoteException("Ped service acquisition failed, please try again later.");
            }
        } else {
            return ped;
        }
    }


    @SuppressLint("NewApi")
    public static IccCardReader getIccCardReader(int cardType) throws RemoteException {
        if (iccCardReader == null) {
            checkState();
            try {
                return application.getDeviceService().getIccCardReader(cardType);
            } catch (RemoteException e) {
                throw new RemoteException("PinPad service acquisition failed, please try again later.");
            }
        } else {
            return iccCardReader;
        }
    }


    @SuppressLint("NewApi")
    public static SecureArea getSecureArea() throws RemoteException {
        if (mSecureArea == null) {
            checkState();
            try {
                return application.getDeviceService().getSecureAreaHandler();
            } catch (RemoteException e) {
                throw new RemoteException("SecureArea service acquisition failed, please try again later.");
            }
        } else {
            return mSecureArea;
        }
    }

    @SuppressLint("NewApi")
    public static Hsm getHsm() throws RemoteException {
        if (mHsm == null) {
            checkState();
            try {
                return application.getDeviceService().getHsmDevice();
            } catch (RemoteException e) {
                throw new RemoteException("SecureArea service acquisition failed, please try again later.");
            }
        } else {
            return mHsm;
        }
    }


    @SuppressLint("NewApi")
    public static MagCardReader getMagCardReader() throws RemoteException {
        if (magCardReader == null) {
            checkState();
            try {
                return application.getDeviceService().getMagCardReader();
            } catch (RemoteException e) {
                throw new RemoteException("PinPad service acquisition failed, please try again later.");
            }
        } else {
            return magCardReader;
        }
    }

    @SuppressLint("NewApi")
    public static LEDDriver getLedDriver() throws RemoteException {
        if (ledDriver == null) {
            checkState();
            try {
                return application.getDeviceService().getLEDDriver();
            } catch (RemoteException e) {
                throw new RemoteException("PinPad service acquisition failed, please try again later.");
            }
        } else {
            return ledDriver;
        }
    }

    @SuppressLint("NewApi")
    public static MultipleAppPrinter getPrinter() throws RemoteException {
        if (printer == null) {
            checkState();
            try {
                return application.getDeviceService().getMultipleAppPrinter();
            } catch (RemoteException e) {
                throw new RemoteException("PinPad service acquisition failed, please try again later.");
            }
        } else {
            return printer;
        }
    }

    @SuppressLint("NewApi")
    public static SerialPortDriver getSerialPortDriver(int port) throws RemoteException {
        if (serialPortDriver == null) {
            checkState();
            try {
                return application.getDeviceService().getSerialPortDriver(port);
            } catch (RemoteException e) {
                throw new RemoteException("PinPad service acquisition failed, please try again later.");
            }
        } else {
            return serialPortDriver;
        }
    }

    @SuppressLint("NewApi")
    public static SerialPort getUsbSerialPort(String path) throws RemoteException {
        if (usbSerialPort == null) {
            checkState();
            try {
                return application.getDeviceService().getSerialPort(path);
            } catch (RemoteException e) {
                throw new RemoteException("PinPad service acquisition failed, please try again later.");
            }
        } else {
            return usbSerialPort;
        }
    }

    @SuppressLint("NewApi")
    public static Beeper getBeeper() throws RemoteException {
        if (beeper == null) {
            checkState();
            try {
                return application.getDeviceService().getBeeper();
            } catch (RemoteException e) {
                throw new RemoteException("PinPad service acquisition failed, please try again later.");
            }
        } else {
            return beeper;
        }
    }

    @SuppressLint("NewApi")
    public static EmvHandler getEmvHandler() throws RemoteException {
        if (emvHandler == null) {
            checkState();
            try {
                return application.getDeviceService().getEmvHandler();
            } catch (RemoteException e) {
                throw new RemoteException("PinPad service acquisition failed, please try again later.");
            }
        } else {
            return emvHandler;
        }
    }

    @SuppressLint("NewApi")
    public static EmvRupayService getEmvRupayService() throws RemoteException {
        if (emvRupayService == null) {
            checkState();
            try {
                return application.getDeviceService().getEmvRupayService();
            } catch (RemoteException e) {
                throw new RemoteException("PinPad service acquisition failed, please try again later.");
            }
        } else {
            return emvRupayService;
        }
    }

    @SuppressLint("NewApi")
    public static InnerScanner getInnerScanner() throws RemoteException {
        if (innerScanner == null) {
            checkState();
            try {
                return application.getDeviceService().getInnerScanner();
            } catch (RemoteException e) {
                throw new RemoteException("PinPad service acquisition failed, please try again later.");
            }
        } else {
            return innerScanner;
        }
    }

    @SuppressLint("NewApi")
    public static LogRecorder getLogRecorder() throws RemoteException {
        if (logRecorder == null) {
            checkState();
            try {
                return application.getDeviceService().getLogRecorder();
            } catch (RemoteException e) {
                throw new RemoteException("PinPad service acquisition failed, please try again later.");
            }
        } else {
            return logRecorder;
        }
    }

    @SuppressLint("NewApi")
    public static CPUTypeAHandler getCpuTypeAHandler() throws RemoteException {
        if (cpuTypeAHandler == null) {
            checkState();
            try {
                return application.getDeviceService().getCPUTypeAHandler();
            } catch (RemoteException e) {
                throw new RemoteException("PinPad service acquisition failed, please try again later.");
            }
        } else {
            return cpuTypeAHandler;
        }
    }

    @SuppressLint("NewApi")
    public static EmulateCardHandler getEmulateCardHandler() throws RemoteException {
        if (emulateCardHandler == null) {
            checkState();
            try {
                return application.getDeviceService().getEmulateCardHandler();
            } catch (RemoteException e) {
                throw new RemoteException("PinPad service acquisition failed, please try again later.");
            }
        } else {
            return emulateCardHandler;
        }
    }

    @SuppressLint("NewApi")
    public static IDukpt getDukpt() throws RemoteException {
        if (dukpt == null) {
            checkState();
            try {
                return application.getDeviceService().getDukpt();
            } catch (RemoteException e) {
                throw new RemoteException("PinPad service acquisition failed, please try again later.");
            }
        } else {
            return dukpt;
        }
    }

    @SuppressLint("NewApi")
    public static TpmManager getTpmManager() throws RemoteException {
        if (tpmManager == null) {
            checkState();
            try {
                return application.getDeviceService().getTpmManager();
            } catch (RemoteException e) {
                throw new RemoteException("Tpm service acquisition failed, please try again later.");
            }
        } else {
            return tpmManager;
        }
    }

    @SuppressLint("NewApi")
    public static IAT24CxxCard getAT24CxxCard() throws RemoteException {
        if (at24CxxCard == null) {
            checkState();
            try {
                return application.getDeviceService().getAT24CxxCard();
            } catch (RemoteException e) {
                throw new RemoteException("Tpm service acquisition failed, please try again later.");
            }
        } else {
            return at24CxxCard;
        }
    }

    @SuppressLint("NewApi")
    public static ICpuCard getCpuCardHandler() throws RemoteException {
        if (cpuCard == null) {
            checkState();
            try {
                return application.getDeviceService().getCpuCard();
            } catch (RemoteException e) {
                throw new RemoteException("PinPad service acquisition failed, please try again later.");
            }
        } else {
            return cpuCard;
        }
    }

    @SuppressLint("NewApi")
    public static IM1Card getM1CardHandler() throws RemoteException {
        if (m1CardHandler == null) {
            checkState();
            try {
                return application.getDeviceService().getM1Card();
            } catch (RemoteException e) {
                throw new RemoteException("PinPad service acquisition failed, please try again later.");
            }
        } else {
            return m1CardHandler;
        }
    }


    @SuppressLint("NewApi")
    public static ICpuRFCard getCpuRFCard() throws RemoteException {
        if (cpuRFCard == null) {
            checkState();
            try {
                return application.getDeviceService().getCpuRFCard();
            } catch (RemoteException e) {
                throw new RemoteException("PinPad service acquisition failed, please try again later.");
            }
        } else {
            return cpuRFCard;
        }
    }

    @SuppressLint("NewApi")
    public static IFelica getFelica() throws RemoteException {
        if (felica == null) {
            checkState();
            try {
                return application.getDeviceService().getFelica();
            } catch (RemoteException e) {
                throw new RemoteException("PinPad service acquisition failed, please try again later.");
            }
        } else {
            return felica;
        }
    }


    public static IPSAM getPSAM() throws RemoteException {
        if (psam == null) {
            checkState();
            try {
                return application.getDeviceService().getPSAM();
            } catch (RemoteException e) {
                throw new RemoteException("PinPad service acquisition failed, please try again later.");
            }
        } else {
            return psam;
        }
    }

    public static IMFULEv1Card getMFULEv1Card() throws RemoteException {
        if (mfulEv1Card == null) {
            checkState();
            try {
                return application.getDeviceService().getMFULEV1Card();
            } catch (RemoteException e) {
                throw new RemoteException("PinPad service acquisition failed, please try again later.");
            }
        } else {
            return mfulEv1Card;
        }
    }

    public static IMFULCCard getMFULCCard() throws RemoteException {
        if (mfulCCard == null) {
            checkState();
            try {
                return application.getDeviceService().getMFULCCard();
            } catch (RemoteException e) {
                throw new RemoteException("PinPad service acquisition failed, please try again later.");
            }
        } else {
            return mfulCCard;
        }
    }

    public static ISLE4442Card getSLE4442Card() throws RemoteException {
        if (sle4442Card == null) {
            checkState();
            try {
                return application.getDeviceService().getSLE4442Card();
            } catch (RemoteException e) {
                throw new RemoteException("PinPad service acquisition failed, please try again later.");
            }
        } else {
            return sle4442Card;
        }
    }

    public static INTAG215Card getNTAGCard() throws RemoteException {
        if (ntag215Card == null) {
            checkState();
            try {
                return application.getDeviceService().getNTAG215Card();
            } catch (RemoteException e) {
                throw new RemoteException("PinPad service acquisition failed, please try again later.");
            }
        } else {
            return ntag215Card;
        }
    }

    public static void reset() {
        pinpad = null;
        iccCardReader = null;
        magCardReader = null;
        ledDriver = null;
        printer = null;
        serialPortDriver = null;
        beeper = null;
        emvHandler = null;
        innerScanner = null;
        logRecorder = null;
        ped = null;
        emulateCardHandler = null;
        usbSerialPort = null;
        dukpt = null;
        tpmManager = null;
        at24CxxCard = null;
        m1CardHandler = null;
        cpuCard = null;
        cpuRFCard = null;
        felica = null;
        psam = null;
        mfulEv1Card = null;
        mfulCCard = null;
        sle4442Card = null;
        ntag215Card = null;
    }

}
