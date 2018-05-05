package com.xcb.cookiemusic.crash;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Process;

import com.xcb.cookiemusic.log.Logger;
import com.xcb.cookiemusic.utils.AppUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 捕获全局异常类
 * Created by zhs on 2018/5/4.
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {
    //log文件的文件名
    private static final String LOG_FILE_NAME = "crash_";
    //log文件的后缀名
    private static final String FILE_NAME_SUFFIX = ".txt";
    private static CrashHandler instance;
    private Context mContext;
    /**
     * 系统默认的异常处理器
     */
    private Thread.UncaughtExceptionHandler uncaughtExceptionHandler;

    /**
     * 构造方法私有化
     */
    private CrashHandler() {

    }

    public static CrashHandler getInstance() {
        if (instance == null) {
            synchronized (CrashHandler.class) {
                if (instance == null) {
                    instance = new CrashHandler();
                }
            }
        }
        return instance;
    }

    public void init(Context context) {
        uncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        mContext = context.getApplicationContext();
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        try {
            dumpExceptionToSDCard(e);
        } catch (IOException e1) {
            e.printStackTrace();
        }

        //打印出当前调用栈信息
        e.printStackTrace();

        //如果系统提供了默认的异常处理器，则交给系统去结束我们的程序，否则就由我们自己结束自己
        if (uncaughtExceptionHandler != null) {
            uncaughtExceptionHandler.uncaughtException(t, e);
        } else {
            Process.killProcess(Process.myPid());
        }
    }

    /**
     * @param ex 异常
     * @throws IOException
     */
    private void dumpExceptionToSDCard(Throwable ex) throws IOException {
        long currentTime = System.currentTimeMillis();
        String time = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINESE).format(new Date(currentTime));
        //以当前时间创建log文件
        String filePath = AppUtils.getAppCacheDir(mContext) + File.separator + LOG_FILE_NAME + time + FILE_NAME_SUFFIX;
        Logger.d("错误日志保存路径：" + filePath);
        File file = new File(filePath);

        try {
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            //导出发生异常的时间
            pw.println(time);

            //导出手机信息
            dumpPhoneInfo(pw);

            pw.println();
            //导出异常的调用栈信息
            ex.printStackTrace(pw);

            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void dumpPhoneInfo(PrintWriter pw) throws PackageManager.NameNotFoundException {
        //应用的版本名称和版本号
        PackageManager pm = mContext.getPackageManager();
        PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
        pw.print("App Version: ");
        pw.print(pi.versionName);
        pw.print('_');
        pw.println(pi.versionCode);

        //android版本号
        pw.print("OS Version: ");
        pw.print(Build.VERSION.RELEASE);
        pw.print("_");
        pw.println(Build.VERSION.SDK_INT);

        //手机制造商
        pw.print("Vendor: ");
        pw.println(Build.MANUFACTURER);

        //手机型号
        pw.print("Model: ");
        pw.println(Build.MODEL);

        //cpu架构
        pw.print("CPU ABI: ");
        pw.println(Build.CPU_ABI);

        pw.print("Device Id：");
        pw.println(AppUtils.getDeviceId(mContext));

    }
}
