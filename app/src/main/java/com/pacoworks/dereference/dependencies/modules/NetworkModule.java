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

import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

import com.facebook.stetho.okhttp.StethoInterceptor;
import com.google.gson.Gson;
import com.pacoworks.dereference.network.LoggerInterceptor;
import com.pacoworks.dereference.network.SongkickApi;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.OkHttpDownloader;

import dagger.Module;
import dagger.Provides;

@Module(includes = SerializationModule.class)
public class NetworkModule {
    static final int DEFAULT_READ_TIMEOUT_MILLIS = 20 * 1000; // 20s

    static final int DEFAULT_WRITE_TIMEOUT_MILLIS = 20 * 1000; // 20s

    static final int DEFAULT_CONNECT_TIMEOUT_MILLIS = 15 * 1000; // 15s

    private final String endpointUrl;

    private final long cacheSize;

    private File cacheDir;

    public NetworkModule(String endpointUrl, File cacheDir, long cacheSize) {
        this.cacheDir = cacheDir;
        this.endpointUrl = endpointUrl;
        this.cacheSize = cacheSize;
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttp(final Cache cache, LoggerInterceptor loggerInterceptor,
            StethoInterceptor stethoInterceptor) {
        final OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setCache(cache);
        okHttpClient.networkInterceptors().add(loggerInterceptor);
        okHttpClient.networkInterceptors().add(stethoInterceptor);
        okHttpClient.setConnectTimeout(DEFAULT_CONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
        okHttpClient.setReadTimeout(DEFAULT_READ_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
        okHttpClient.setWriteTimeout(DEFAULT_WRITE_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
        return okHttpClient;
    }

    @Provides
    @Singleton
    OkHttpDownloader provideOkHttpDownloader(OkHttpClient okHttpClient) {
        // In-memory caching on this example
        return new OkHttpDownloader(okHttpClient);
    }

    @Provides
    @Singleton
    Cache provideCache() {
        return new Cache(cacheDir, cacheSize);
    }

    @Provides
    @Singleton
    StethoInterceptor stethoInterceptor() {
        return new StethoInterceptor();
    }

    @Provides
    @Singleton
    LoggerInterceptor loggerInterceptor() {
        return new LoggerInterceptor();
    }

    @Provides
    @Named("songkick")
    @Singleton
    Retrofit provideRetrofitSongkick(OkHttpClient client, Gson gson) {
        return new Retrofit.Builder().client(client).baseUrl(endpointUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).build();
    }

    @Provides
    @Singleton
    SongkickApi provideSongkickApi(@Named("songkick") Retrofit retrofit) {
        return retrofit.create(SongkickApi.class);
    }
}
