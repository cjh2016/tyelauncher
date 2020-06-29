package com.boll.tyelauncher.util;


import android.text.TextUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class ListUtils {

    public interface Converter<T1, T2> {
        T2 convert(T1 t1);
    }

    public static <T> void randomOrder(List<T> list) {
        if (!isEmpty((List) list)) {
            List<T> tmp = new ArrayList<>(list);
            list.clear();
            Random random = new Random();
            while (!tmp.isEmpty()) {
                list.add(tmp.remove(random.nextInt(tmp.size())));
            }
        }
    }

    public static <T> void swapForward(List<T> list, int count) {
        if (!isEmpty((List) list) && count < list.size()) {
            if (count <= 2) {
                while (count > 0) {
                    list.add(list.remove(0));
                    count--;
                }
                return;
            }
            List<T> prev = list.subList(0, count);
            List<T> tail = list.subList(count, list.size());
            List<T> tmp = new ArrayList<>(list.size());
            tmp.addAll(tail);
            tmp.addAll(prev);
            list.clear();
            list.addAll(tmp);
        }
    }

    public static <T> List<T> empty() {
        return new ArrayList();
    }

    public static <T> T getItem(List<T> list, int index) {
        if (list != null && index >= 0 && index < list.size()) {
            return list.get(index);
        }
        return null;
    }

    public static <T> boolean checkRange(List<T> list, int index) {
        return list != null && index >= 0 && index < list.size();
    }

    public static <T> T getLastItem(List<T> list) {
        if (isEmpty((List) list)) {
            return null;
        }
        return list.get(size(list) - 1);
    }

    public static <T> T removeLastItem(List<T> list) {
        if (isEmpty((List) list)) {
            return null;
        }
        return list.remove(list.size() - 1);
    }

    public static boolean isEmpty(List list) {
        return list == null || list.isEmpty();
    }

    public static boolean isNotEmpty(List list) {
        return !isEmpty(list);
    }

    public static <T> boolean isEmpty(T[] t) {
        return t == null || t.length == 0;
    }

    public static boolean isEmpty(byte[] t) {
        return t == null || t.length == 0;
    }

    public static <T> int size(List<T> list) {
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    public static String toString(List list) {
        StringBuilder sb = new StringBuilder();
        if (!isEmpty(list)) {
            Iterator iterator = list.iterator();
            while (iterator.hasNext()) {
                sb.append(iterator.next().toString());
                if (iterator.hasNext()) {
                    sb.append(",");
                }
            }
        }
        return sb.toString();
    }

    public static <T1, T2> String toString(List<T1> list, Converter<T1, T2> converter) {
        StringBuilder sb = new StringBuilder();
        if (!isEmpty((List) list)) {
            Iterator iterator = list.iterator();
            while (iterator.hasNext()) {
                sb.append(converter.convert((T1) iterator.next()).toString());
                if (iterator.hasNext()) {
                    sb.append(",");
                }
            }
        }
        return sb.toString();
    }

    public static <T> void swap(List<T> list, int pos1, int pos2) {
        T tmp = list.get(pos1);
        list.set(pos1, list.get(pos2));
        list.set(pos2, tmp);
    }

    public static List<String> toList(String listStr, String split) {
        if (TextUtils.isEmpty(listStr)) {
            return null;
        }
        if (!listStr.contains(",")) {
            List<String> stringList = new ArrayList<>();
            stringList.add(listStr);
            return stringList;
        }
        List<String> stringList2 = new ArrayList<>();
        String[] listArr = listStr.split(split);
        for (String add : listArr) {
            stringList2.add(add);
        }
        return stringList2;
    }

    public static <T> List<T> clear(List<T> list) {
        if (list == null) {
            return new ArrayList<>();
        }
        list.clear();
        return list;
    }

    public static <T> List<T> newList() {
        return new ArrayList();
    }

    public static <T> boolean equals(List<T> list1, List<T> list2) {
        if (list1 == list2) {
            return true;
        }
        if (list1 == null || list2 == null) {
            return false;
        }
        if (list1.size() != list2.size()) {
            return false;
        }
        int size = list1.size();
        for (int i = 0; i < size; i++) {
            T t1 = list1.get(i);
            T t2 = list2.get(i);
            if (t1 != t2) {
                if (t1 == null || t2 == null) {
                    return false;
                }
                if (!t1.equals(t2)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static <T> List<T> toList(T[] arr) {
        if (arr == null || arr.length == 0) {
            return null;
        }
        List<T> list = new ArrayList<>(arr.length);
        for (T t : arr) {
            list.add(t);
        }
        return list;
    }

    public static class DefStringConverter<T> implements Converter<T, String> {
        @Override
        public String convert(T t) {
            if (t == null) {
                return "null";
            }
            return t.toString();
        }
    }

    public static <T, R> List<R> convert(List<T> list, Converter<T, R> converter) {
        if (isEmpty((List) list)) {
            return null;
        }
        if (converter == null) {
            throw new IllegalArgumentException("null == converter");
        }
        ArrayList<R> result = new ArrayList<>();
        for (T item : list) {
            R newItem = converter.convert(item);
            if (item != null) {
                result.add(newItem);
            }
        }
        return result;
    }

    public static <T> List<T> newArrayList(T... items) {
        if (isEmpty(items)) {
            return new ArrayList();
        }
        ArrayList<T> result = new ArrayList<>(items.length);
        for (T t : items) {
            if (t != null) {
                result.add(t);
            }
        }
        return result;
    }

    public static <T> void addAll(List<T> list, List<T> sub) {
        if (sub != null) {
            list.addAll(sub);
        }
    }
}
