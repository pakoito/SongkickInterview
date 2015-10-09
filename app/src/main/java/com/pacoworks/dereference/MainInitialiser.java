/*
 * Copyright (c) pakoito 2015
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.pacoworks.dereference;

import timber.log.Timber;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.okhttp.OkHttpClient;

import io.palaima.debugdrawer.log.data.LumberYard;

public class MainInitialiser {
    private final Application.ActivityLifecycleCallbacks initialiserCallbacks = new Application.ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        }

        @Override
        public void onActivityStarted(Activity activity) {
        }

        @Override
        public void onActivityResumed(Activity activity) {
        }

        @Override
        public void onActivityPaused(Activity activity) {
        }

        @Override
        public void onActivityStopped(Activity activity) {
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
        }
    };

    public MainInitialiser(DereferenceApplication application, OkHttpClient okHttpClient) {
        application.registerActivityLifecycleCallbacks(initialiserCallbacks);
        LeakCanary.install(application);
        if (BuildConfig.DEBUG) {
            // FIXME no stetho for now
            // Stetho.initialize(Stetho.newInitializerBuilder(application)
            // .enableDumpapp(Stetho.defaultDumperPluginsProvider(application)).build());
            LumberYard lumberYard = LumberYard.getInstance(application);
            lumberYard.cleanUp();
            Timber.plant(lumberYard.tree());
            Timber.plant(new Timber.DebugTree());
        }
    }
}
