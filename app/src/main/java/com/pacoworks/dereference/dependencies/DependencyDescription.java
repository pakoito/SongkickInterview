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

package com.pacoworks.dereference.dependencies;

import javax.inject.Named;

import retrofit.Retrofit;

import android.content.Context;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.pacoworks.dereference.DereferenceApplication;
import com.pacoworks.dereference.FlavourInitialiser;
import com.pacoworks.dereference.MainInitialiser;
import com.pacoworks.dereference.network.SongkickApi;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.OkHttpDownloader;

public interface DependencyDescription {
    void inject(DereferenceApplication application);

    DereferenceApplication application();

    Context baseContext();

    MainInitialiser initialiser();

    FlavourInitialiser flavourInitialiser();

    Gson gsonDefault();

    @Named("default")
    ObjectMapper jacksonDefault();

    @Named("unknown")
    ObjectMapper jacksonUnknown();

    @Named("fields")
    ObjectMapper jacksonFieldsOnly();

    OkHttpClient okHttpClient();

    OkHttpDownloader okHttpDownloader();

    @Named("songkick")
    Retrofit getSongkickRetrofit();

    SongkickApi songkickApi();
}
