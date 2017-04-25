package com.che58.ljb.rxjava.utils;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 2017/4/25 改进日志输出，更人性化
 */
public class MyLog {
    private static boolean sDebug = true;
    private static String sTag = "marsToken";
    private static final int JSON_INDENT = 2;


    public static void init(boolean debug, String tag) {
        MyLog.sDebug = debug;
        MyLog.sTag = tag;
    }

    public static void e(String msg, Object... params) {
        e(null, msg, params);
    }

    public static void e(String tag, String msg, Object[] params) {
        if (!sDebug) return;
        LogText.e(getFinalTag(tag), String.format(msg, params));
    }

    public static void json(String json) {
        json(null, json);
    }

    public static void json(String tag, String json) {
        if (!sDebug) return;
        LogText.e(getFinalTag(tag), getPrettyJson(json));
    }

    private static String getPrettyJson(String jsonStr) {
        try {
            jsonStr = jsonStr.trim();
            if (jsonStr.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(jsonStr);
                return jsonObject.toString(JSON_INDENT);
            }
            if (jsonStr.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(jsonStr);
                return jsonArray.toString(JSON_INDENT);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "Invalid Json, Please Check: " + jsonStr;
    }


    private static String getFinalTag(String tag) {
        if (!TextUtils.isEmpty(tag)) {
            return tag;
        }
        return sTag;
    }

    private static class LogText {
        private static final String HEAD_DIVIDER =
                "beginning══════════════════════beginning══════════════════════beginning\n";
        private static final String FOOT_DIVIDER =
                "finished═════════════════════finished═══════════════════════finished\n";
        private String mTag;

        public LogText(String tag) {
            mTag = tag;
        }


        public static void e(String tag, String content) {
            LogText logText = new LogText(tag);
            logText.setup(content);
        }

        public void setup(String content) {
            setUpHeader();
            setUpContent(content);
            setUpFooter();

        }

        private void setUpHeader() {
            Log.e(mTag, HEAD_DIVIDER);
        }

        private void setUpFooter() {
            Log.e(mTag, FOOT_DIVIDER);
        }

        public void setUpContent(String content) {
            StackTraceElement targetStackTraceElement = getTargetStackTraceElement();
            Log.e(mTag, "包名参考：" + targetStackTraceElement.getClassName() + targetStackTraceElement
                    .getMethodName());
            Log.e(mTag, "(" + targetStackTraceElement.getFileName() + ":"
                    + targetStackTraceElement.getLineNumber() + ")");
            Log.e(mTag, content);
        }

        private StackTraceElement getTargetStackTraceElement() {
            // find the target invoked method
            StackTraceElement targetStackTrace = null;
            boolean shouldTrace = false;
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            for (StackTraceElement stackTraceElement : stackTrace) {
                boolean isLogMethod = stackTraceElement.getClassName().equals(MyLog.class.getName
                        ());
                if (shouldTrace && !isLogMethod) {
                    targetStackTrace = stackTraceElement;
                    break;
                }
                shouldTrace = isLogMethod;
            }
            return targetStackTrace;
        }
    }
}
