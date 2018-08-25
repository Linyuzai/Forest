package eason.linyuzai.easonbar.entity;

import android.os.Bundle;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import eason.linyuzai.easonbar.common.Logger;

/**
 * Created by linyuzai on 2018/5/9.
 *
 * @author linyuzai
 */

public class EasonEntity implements DataAccess {
    private static final String EASON = "eason_linyuzai_easonbar_";
    private Map<String, Object> valueMap = new HashMap<>();

    public void add(String key, Object value) {
        valueMap.put(key, value);
    }

    @SuppressWarnings("unchecked")
    public Object get(String key) {
        return valueMap.get(key);
    }

    public String getKey(String key) {
        return EASON + key;
    }

    public Bundle toBundle() {
        return toBundle(true);
    }

    public Bundle toBundle(boolean onlyCustom) {
        Bundle bundle = new Bundle();
        for (Map.Entry<String, Object> entry : valueMap.entrySet()) {
            String key = entry.getKey();
            if (onlyCustom && key.contains(EASON))
                continue;
            Object value = entry.getValue();
            if (value instanceof Integer) {
                bundle.putInt(key, (int) value);
            } else if (value instanceof Integer[]) {
                bundle.putIntArray(key, (int[]) value);
            } else if (value instanceof Short) {
                bundle.putShort(key, (short) value);
            } else if (value instanceof Short[]) {
                bundle.putShortArray(key, (short[]) value);
            } else if (value instanceof Character) {
                bundle.putChar(key, (char) value);
            } else if (value instanceof Character[]) {
                bundle.putCharArray(key, (char[]) value);
            } else if (value instanceof Float) {
                bundle.putFloat(key, (float) value);
            } else if (value instanceof Float[]) {
                bundle.putFloatArray(key, (float[]) value);
            } else if (value instanceof Double) {
                bundle.putDouble(key, (double) value);
            } else if (value instanceof Double[]) {
                bundle.putDoubleArray(key, (double[]) value);
            } else if (value instanceof Long) {
                bundle.putLong(key, (long) value);
            } else if (value instanceof Long[]) {
                bundle.putLongArray(key, (long[]) value);
            } else if (value instanceof Boolean) {
                bundle.putBoolean(key, (boolean) value);
            } else if (value instanceof Boolean[]) {
                bundle.putBooleanArray(key, (boolean[]) value);
            } else if (value instanceof Byte) {
                bundle.putByte(key, (byte) value);
            } else if (value instanceof Byte[]) {
                bundle.putByteArray(key, (byte[]) value);
            } else if (value instanceof String) {
                bundle.putString(key, (String) value);
            } else if (value instanceof String[]) {
                bundle.putStringArray(key, (String[]) value);
            } else if (value instanceof CharSequence) {
                bundle.putCharSequence(key, (CharSequence) value);
            } else if (value instanceof CharSequence[]) {
                bundle.putCharSequenceArray(key, (CharSequence[]) value);
            } else if (value instanceof Serializable) {
                bundle.putSerializable(key, (Serializable) value);
            } else if (value instanceof Parcelable) {
                bundle.putParcelable(key, (Parcelable) value);
            } else if (value instanceof Parcelable[]) {
                bundle.putParcelableArray(key, (Parcelable[]) value);
            } else {
                Logger.log("The type [ " + value.getClass() + " ] can not transfer to bundle data");
            }
        }
        return bundle;
    }
}
