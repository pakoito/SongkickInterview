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

import javax.inject.Singleton;

import android.content.Context;

import com.pacoworks.dereference.DereferenceApplication;
import com.pacoworks.dereference.FlavourInitialiser;
import com.pacoworks.dereference.MainInitialiser;
import com.squareup.okhttp.OkHttpClient;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {
    DereferenceApplication application;

    public ApplicationModule(DereferenceApplication app) {
        application = app;
    }

    @Provides
    @Singleton
    DereferenceApplication provideApplication() {
        return application;
    }

    @Provides
    @Singleton
    Context provideContext() {
        return application;
    }

    @Provides
    @Singleton
    MainInitialiser provideMainInitialiser(DereferenceApplication application,
            OkHttpClient okHttpClient) {
        return new MainInitialiser(application, okHttpClient);
    }

    @Provides
    @Singleton
    FlavourInitialiser provideFlavourInitialiser(DereferenceApplication application) {
        return new FlavourInitialiser(application);
    }
}
