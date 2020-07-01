package com.boll.tyelauncher.view.wheelview.adapter;

package com.toycloud.launcher.view.wheelview.adapter;

import android.content.Context;

public class ArrayWheelAdapter<T> extends AbstractWheelTextAdapter {
    private T[] items;

    public ArrayWheelAdapter(Context context, T[] items2) {
        super(context);
        this.items = items2;
    }

    public CharSequence getItemText(int index) {
        if (index < 0 || index >= this.items.length) {
            return null;
        }
        T item = this.items[index];
        if (item instanceof CharSequence) {
            return (CharSequence) item;
        }
        return item.toString();
    }

    public int getItemsCount() {
        return this.items.length;
    }
}