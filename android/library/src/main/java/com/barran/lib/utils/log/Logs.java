package com.barran.lib.utils.log;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import com.barran.lib.data.SPWrapper;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.Locale;

/**
 * 封装log功能，支持输出级别，输出到文件
 * 
 *
 * 级别分为：VERBOSE(Log.v), DEBUG(Log.d), INFO(Log.i), WARN(Log.w), ERROR(Log.e),
 * ASSERT(Log.wtf)<br/>
 *
 * <p>
 * 注意Log类使用2个参数的方法时，msg不能为null！
 * </p>
 * <p>
 * 其他情况备注：tag可以为null或者空字串，这时不会显示tag
 * </p>
 * <p>
 * 其他情况备注：msg可以使用空字串""，在2参数方法中，不会显示这个log信息
 * </p>
 * <p>
 * 三参数方法：msg是和exception拼装起来的，msg可以使用空字串""，相当于只输出异常堆栈；msg也可以为null，
 * 这时会输出一行显示null的log，然后是异常堆栈
 * 
 * Created by tanwei on 2017/10/19.
 */

public class Logs {
    
    private static final String STACK_INFO_FORMAT = "%s: %s.%s(L:%d) ";
    private static final int STACK_INDEX = 4;
    private static final String DEFAULT_TAG = "logs";
    
    // debug版本输出级别不限制
    private static boolean debug = false;
    // 控制台log输出级别
    private static int logLevel = Log.WARN;// 默认输出warning及以上级别的log
    // 文件保存的log级别
    private static int logSaveLevel = Log.WARN;// 默认输出warning及以上级别的log
    
    private static LogPersistent logPersistent;
    
    public static void init(Context context) {
        logPersistent = new LogPersistent(context);
    }
    
    public static void setDebug(boolean debug) {
        Logs.debug = debug;
    }
    
    /**
     * 设置log输出级别
     *
     * @param lever
     *            {@linkplain android.util.Log}
     */
    public static void setLogLever(int lever) {
        if (lever < Log.VERBOSE) {
            return;
        }
        logLevel = lever;
    }
    
    /**
     * 设置log输出到文件级别
     *
     * @param lever
     *            {@linkplain android.util.Log}
     */
    public static void setLogSaveLever(int lever) {
        if (lever < Log.VERBOSE) {
            return;
        }
        logSaveLevel = lever;
    }
    
    public static void v(String msg) {
        v(DEFAULT_TAG, msg);
    }
    
    public static void v(String tag, String msg) {
        String stackInfo = getLineInfo(Log.VERBOSE);
        showLog(Log.VERBOSE, tag, wrapMsgWithStackInfo(msg, stackInfo));
    }
    
    public static void d(String msg) {
        d(DEFAULT_TAG, msg);
    }
    
    public static void d(String tag, String msg) {
        String stackInfo = getLineInfo(Log.DEBUG);
        showLog(Log.DEBUG, tag, wrapMsgWithStackInfo(msg, stackInfo));
    }
    
    public static void i(String msg) {
        i(DEFAULT_TAG, msg);
    }
    
    public static void i(String tag, String msg) {
        String stackInfo = getLineInfo(Log.INFO);
        showLog(Log.INFO, tag, wrapMsgWithStackInfo(msg, stackInfo));
    }
    
    public static void w(String msg) {
        w(DEFAULT_TAG, msg);
    }
    
    public static void w(String tag, String msg) {
        String stackInfo = getLineInfo(Log.WARN);
        String wrapMsg = wrapMsgWithStackInfo(msg, stackInfo);
        showLog(Log.WARN, tag, wrapMsg);
        
        if (logSaveLevel >= Log.WARN) {
            saveLog(wrapMsg);
        }
    }
    
    public static void e(String msg) {
        e(DEFAULT_TAG, msg);
    }
    
    public static void e(String tag, String msg) {
        String stackInfo = getLineInfo(Log.ERROR);
        String wrapMsg = wrapMsgWithStackInfo(msg, stackInfo);
        
        showLog(Log.ERROR, tag, wrapMsg);
        if (logSaveLevel >= Log.ERROR) {
            saveLog(wrapMsg);
        }
    }
    
    private static String wrapMsgWithStackInfo(String msg, String stackInfo) {
        return String.format("[%s] %s", stackInfo, msg);
    }
    
    private static String getLogLevelString(int level) {
        switch (level) {
            case Log.VERBOSE:
                return "V";
            case Log.DEBUG:
                return "D";
            case Log.INFO:
            default:
                return "I";
            case Log.WARN:
                return "W";
            case Log.ERROR:
                return "E";
            case Log.ASSERT:
                return "A";
        }
    }
    
    private static String getLineInfo(int level) {
        StackTraceElement ele = Thread.currentThread().getStackTrace()[STACK_INDEX];
        if (ele != null) {
            String fullClassName = ele.getClassName();
            String className = fullClassName
                    .substring(fullClassName.lastIndexOf(".") + 1);
            return String.format(Locale.CHINA, STACK_INFO_FORMAT,
                    getLogLevelString(level), className, ele.getMethodName(),
                    ele.getLineNumber());
        }
        return "";
    }
    
    private static void showLog(int level, String tag, String msg) {
        if (debug || level >= logLevel) {
            switch (level) {
                case Log.VERBOSE:
                    Log.v(tag, msg);
                    break;
                case Log.DEBUG:
                    Log.d(tag, msg);
                    break;
                case Log.INFO:
                    Log.i(tag, msg);
                    break;
                case Log.WARN:
                    Log.w(tag, msg);
                    break;
                case Log.ERROR:
                    Log.e(tag, msg);
                    break;
                case Log.ASSERT:
                    Log.wtf(tag, msg);
                    break;
            }
        }
    }
    
    private static void saveLog(String msg) {
        logPersistent.saveLog(msg);
    }
    
    private static class LogPersistent {
        
        private static final String CHARSET = "UTF-8";
        
        private static final String SP_KEY_LOG_SEEK = "log_seek";
        private static final String LOG_FILE_NAME = "cache.log";
        
        private static final int MAX_FILE_LENGTH = 5 * 1024 * 1024;
        
        private static final int MSG_LOG = 1;
        private static final String MSG_KEY_LOG_MSG = "log_msg";
        
        private long mCurrentSeekPosition;
        
        private Handler mLogHandler;
        
        private RandomAccessFile mLogFileAccess;
        
        private SPWrapper seekPositionSP;
        
        private LogPersistent(Context context) {
            if (!Environment.MEDIA_MOUNTED
                    .equals(Environment.getExternalStorageState())) {
                Log.w(DEFAULT_TAG, "sd not exists");
                return;
            }
            seekPositionSP = new SPWrapper(context, DEFAULT_TAG);
            try {
                File logDir = null;
                File externalRootDir = Environment.getExternalStorageDirectory();
                if (externalRootDir != null) {
                    logDir = new File(externalRootDir, "log/" + context.getPackageName());
                    logDir.mkdirs();
                }
                File logFile = new File(logDir, LOG_FILE_NAME);
                mLogFileAccess = new RandomAccessFile(logFile, "rw");
                mCurrentSeekPosition = seekPositionSP.getLong(SP_KEY_LOG_SEEK, 0);
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
            
            // log thread
            HandlerThread thread = new HandlerThread(DEFAULT_TAG);
            thread.start();
            mLogHandler = new Handler(thread.getLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case MSG_LOG:
                            Bundle bd = msg.getData();
                            if (bd != null) {
                                saveLogFileInternal(bd.getString(MSG_KEY_LOG_MSG));
                            }
                            break;
                    }
                }
            };
        }
        
        private boolean saveLog(String msg) {
            if (mLogHandler == null) {
                return false;
            }
            Message message = Message.obtain(mLogHandler);
            message.what = MSG_LOG;
            Bundle bundle = new Bundle();
            bundle.putString(MSG_KEY_LOG_MSG, msg);
            message.setData(bundle);
            message.sendToTarget();
            
            return true;
        }
        
        private boolean saveLogFileInternal(String logString) {
            
            long fileLength;
            try {
                fileLength = mLogFileAccess.length();
            } catch (Exception e) {
                fileLength = -1;
            }
            
            if (fileLength < 0)
                return false;
            
            long seekPos;
            if (fileLength < MAX_FILE_LENGTH) {
                seekPos = fileLength;
            }
            else {
                seekPos = mCurrentSeekPosition;
            }
            
            try {
                mLogFileAccess.seek(seekPos);
                mLogFileAccess.write(logString.getBytes(CHARSET));
                if (fileLength >= MAX_FILE_LENGTH) {
                    mCurrentSeekPosition = mLogFileAccess.getFilePointer();
                    seekPositionSP.put(SP_KEY_LOG_SEEK, mCurrentSeekPosition);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            return true;
        }
        
    }
}
