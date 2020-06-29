package com.boll.tyelauncher.easytrans;

import java.util.Map;

public class Params {
    private static final String TAG = "Params";

    public static class EventParams {
        String eventType;
        Map<String, String> map;
        String name;

        public EventParams(String eventType2, String name2, Map<String, String> map2) {
            this.eventType = eventType2;
            this.name = name2;
            this.map = map2;
        }

        @Override
        public String toString() {
            return "EventParams{eventType='" + this.eventType + '\'' + ", name='" + this.name + '\'' + ", map=" + this.map + '}';
        }
    }

    public static class StatusEventParams {
        String controlCode;
        int count = 0;
        String eventType;
        String name;

        @Override
        public String toString() {
            return "StatusEventParams{eventType='" + this.eventType + '\'' + ", controlCode='" + this.controlCode + '\'' + ", name='" + this.name + '\'' + ", count=" + this.count + '}';
        }

        public StatusEventParams(String eventType2, String controlCode2, String name2, int count2) {
            this.eventType = eventType2;
            this.controlCode = controlCode2;
            this.name = name2;
            this.count = count2;
        }
    }

    public static class ActiveEventParams {
        String appId;
        String appVersion;
        String bundleInfo;
        String channelId;
        String name;

        public ActiveEventParams(String name2, String appId2, String appVersion2, String channelId2, String bundleInfo2) {
            this.name = name2;
            this.appId = appId2;
            this.appVersion = appVersion2;
            this.channelId = channelId2;
            this.bundleInfo = bundleInfo2;
        }

        public String toString() {
            return "ActiveEventParams{name='" + this.name + '\'' + ", appId='" + this.appId + '\'' + ", appVersion='" + this.appVersion + '\'' + ", channelId='" + this.channelId + '\'' + ", bundleInfo='" + this.bundleInfo + '\'' + '}';
        }
    }

    public static class ListEventParams {
        String controlCode;
        String eventType;
        String jsonArrayLogs;

        public ListEventParams(String eventType2, String controlCode2, String jsonArrayLogs2) {
            this.eventType = eventType2;
            this.controlCode = controlCode2;
            this.jsonArrayLogs = jsonArrayLogs2;
        }

        @Override
        public String toString() {
            return "ListEventParams{eventType='" + this.eventType + '\'' + ", controlCode='" + this.controlCode + '\'' + ", jsonArrayLogs='" + this.jsonArrayLogs + '\'' + '}';
        }
    }

    public static class ControlEventParams {
        String controlCode;
        String eventType;
        Map<String, String> map;
        String name;

        public ControlEventParams(String eventType2, String controlCode2, String name2, Map<String, String> map2) {
            this.eventType = eventType2;
            this.controlCode = controlCode2;
            this.name = name2;
            this.map = map2;
        }

        public String toString() {
            return "ControlEventParams{eventType='" + this.eventType + '\'' + ", name='" + this.name + '\'' + ", controlCode='" + this.controlCode + '\'' + ", map=" + this.map + '}';
        }
    }

    public static class EventJsonParams {
        String controlCode;
        String eventType;
        String name;
        String object;

        public EventJsonParams(String eventType2, String controlCode2, String name2, String jsonObject) {
            this.eventType = eventType2;
            this.controlCode = controlCode2;
            this.name = name2;
            this.object = jsonObject;
        }

        @Override
        public String toString() {
            return "EventJsonParams{eventType='" + this.eventType + '\'' + ", name='" + this.name + '\'' + ", controlCode='" + this.controlCode + '\'' + ", object='" + this.object + '\'' + '}';
        }
    }
}
