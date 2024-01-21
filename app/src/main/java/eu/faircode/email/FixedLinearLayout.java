package eu.faircode.email;

/*
    This file is part of FairEmail.

    FairEmail is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FairEmail is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with FairEmail.  If not, see <http://www.gnu.org/licenses/>.

    Copyright 2018-2024 by Marcel Bokhorst (M66B)
*/

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import java.util.Map;
import java.util.WeakHashMap;

public class FixedLinearLayout extends LinearLayout {
    public FixedLinearLayout(Context context) {
        super(context);
    }

    public FixedLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FixedLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public FixedLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        try {
            return super.dispatchTouchEvent(ev);
        } catch (Throwable ex) {
            Log.e(ex);
            return false;
        }
    }

    private final Map<Runnable, Runnable> mapRunnable = new WeakHashMap<>();

    @Override
    public boolean post(Runnable action) {
        Runnable wrapped = new RunnableEx("post") {
            @Override
            protected void delegate() {
                action.run();
            }
        };
        mapRunnable.put(action, wrapped);
        return super.post(wrapped);
    }

    @Override
    public boolean postDelayed(Runnable action, long delayMillis) {
        Runnable wrapped = new RunnableEx("postDelayed") {
            @Override
            protected void delegate() {
                action.run();
            }
        };
        mapRunnable.put(action, wrapped);
        return super.postDelayed(wrapped, delayMillis);
    }

    @Override
    public boolean removeCallbacks(Runnable action) {
        Runnable wrapped = mapRunnable.get(action);
        if (wrapped == null)
            return super.removeCallbacks(action);
        else
            return super.removeCallbacks(wrapped);
    }
}
