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

package com.pacoworks.dereference.dependencies.modules;

import rx.Observable;

import android.app.Activity;

import com.github.pwittchen.reactivenetwork.library.ConnectivityStatus;
import com.github.pwittchen.reactivenetwork.library.ReactiveNetwork;
import com.pacoworks.dereference.dependencies.ForActivity;
import com.squareup.picasso.Downloader;
import com.squareup.picasso.Picasso;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {
    private final Activity activity;

    private final Downloader okHttpDownloader;

    public ActivityModule(Activity activity, Downloader okHttpDownloader) {
        this.activity = activity;
        this.okHttpDownloader = okHttpDownloader;
    }

    @Provides
    @ForActivity
    Activity provideActivity() {
        return activity;
    }

    @Provides
    @ForActivity
    Picasso providePicasso() {
        return new Picasso.Builder(activity).downloader(okHttpDownloader).build();
    }

    @Provides
    @ForActivity
    Observable<ConnectivityStatus> provideReactiveNetwork() {
        return new ReactiveNetwork().observeConnectivity(activity);
    }
}
