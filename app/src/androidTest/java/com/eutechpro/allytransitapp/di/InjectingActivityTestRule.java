package com.eutechpro.allytransitapp.di;

import android.app.Activity;
import android.app.Application;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;

public class InjectingActivityTestRule<T extends Activity> extends ActivityTestRule<T> {

    private final OnBeforeActivityLaunchedListener<T> listener;

    public InjectingActivityTestRule(Class<T> activityClass, @NonNull OnBeforeActivityLaunchedListener<T> listener) {
        this(activityClass, false, listener);
    }
    public InjectingActivityTestRule(Class<T> activityClass, boolean initialTouchMode, @NonNull OnBeforeActivityLaunchedListener<T> listener) {
        this(activityClass, initialTouchMode, true, listener);
    }

    public InjectingActivityTestRule(Class<T> activityClass, boolean initialTouchMode, boolean launchActivity, @NonNull OnBeforeActivityLaunchedListener<T> listener) {
        super(activityClass, initialTouchMode, launchActivity);
        this.listener = listener;
    }

    @Override
    protected void beforeActivityLaunched() {
        super.beforeActivityLaunched();
        this.listener.beforeActivityLaunched((Application) InstrumentationRegistry
                .getInstrumentation()
                .getTargetContext()
                .getApplicationContext(), getActivity());
    }

    public interface OnBeforeActivityLaunchedListener<T> {
        void beforeActivityLaunched(@NonNull Application application, @NonNull T activity);
    }
}