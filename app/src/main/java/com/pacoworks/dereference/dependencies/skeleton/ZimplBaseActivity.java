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

package com.pacoworks.dereference.dependencies.skeleton;

import javax.inject.Inject;

import lombok.AccessLevel;
import lombok.Getter;
import rx.functions.Action1;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.Toast;

import butterknife.ButterKnife;

import com.pacoworks.dereference.ApplicationInjectionComponent;
import com.pacoworks.dereference.BuildConfig;
import com.pacoworks.dereference.DereferenceApplication;
import com.pacoworks.dereference.dependencies.ActivityInjectionComponent;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.Picasso;

import io.palaima.debugdrawer.DebugDrawer;
import io.palaima.debugdrawer.log.LogModule;
import io.palaima.debugdrawer.module.BuildModule;
import io.palaima.debugdrawer.module.DeviceModule;
import io.palaima.debugdrawer.module.NetworkModule;
import io.palaima.debugdrawer.module.SettingsModule;
import io.palaima.debugdrawer.okhttp.OkHttpModule;
import io.palaima.debugdrawer.picasso.PicassoModule;
import io.palaima.debugdrawer.scalpel.ScalpelModule;

public abstract class ZimplBaseActivity extends Activity implements IUi {
    private static final String PRESENTER_STATE = "presenter_state";

    @Inject
    Picasso picasso;

    @Inject
    OkHttpClient okHttpClient;

    @Getter(AccessLevel.PROTECTED)
    private ActivityInjectionComponent injector;

    private ZimplBasePresenter presenter;

    private DebugDrawer mDebugDrawer;

    @Override
    @SuppressWarnings("unchecked")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injector = startInjector();
        startUi();
        restorePresenter(savedInstanceState, injector);
    }

    @NonNull
    private ActivityInjectionComponent startInjector() {
        final ApplicationInjectionComponent applicationInjectionComponent = DereferenceApplication
                .get(this).component();
        final ActivityInjectionComponent injector = newActivityInjector(applicationInjectionComponent);
        injector.inject(this);
        return injector;
    }

    private void startUi() {
        setContentView(getContentView());
        ButterKnife.bind(this);
        if (BuildConfig.DEBUG) {
            mDebugDrawer = new DebugDrawer.Builder(this).modules(new LogModule(),
                    new NetworkModule(this), new OkHttpModule(okHttpClient),
                    new PicassoModule(picasso), new ScalpelModule(this), new DeviceModule(this),
                    new BuildModule(this), new SettingsModule(this)).build();
        }
    }

    private void restorePresenter(Bundle savedInstanceState, ActivityInjectionComponent injector) {
        presenter = injector.presenter();
        injector.inject(presenter);
        presenter.bindUi(this);
        final String state = null == savedInstanceState ? null : savedInstanceState
                .getString(PRESENTER_STATE);
        presenter.restoreState(state);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.createBase();
        if (mDebugDrawer != null) {
            mDebugDrawer.onStart();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.resumeBase();
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.pauseBase();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mDebugDrawer != null) {
            mDebugDrawer.onStop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destroyBase();
        presenter.unbindUi();
        ButterKnife.unbind(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(PRESENTER_STATE, presenter.saveState());
    }

    protected abstract ActivityInjectionComponent newActivityInjector(
            ApplicationInjectionComponent applicationInjectionComponent);

    protected abstract int getContentView();

    // UI IMPLEMENTATIONS
    @Override
    public Action1<Throwable> toastError() {
        return new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                String message = (TextUtils.isEmpty(throwable.getMessage())) ? null == throwable
                        .getCause() || TextUtils.isEmpty(throwable.getCause().getMessage()) ? "unknown error"
                        : throwable.getCause().getMessage()
                        : throwable.getMessage();
                Toast.makeText(ZimplBaseActivity.this, message, Toast.LENGTH_LONG).show();
            }
        };
    }

    @Override
    public Action1<String> toastMessage() {
        return new Action1<String>() {
            @Override
            public void call(String message) {
                Toast.makeText(ZimplBaseActivity.this, message, Toast.LENGTH_LONG).show();
            }
        };
    }
}
