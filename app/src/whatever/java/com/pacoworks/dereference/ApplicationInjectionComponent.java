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

import javax.inject.Singleton;

import com.pacoworks.dereference.dependencies.DependencyDescription;
import com.pacoworks.dereference.dependencies.modules.ApplicationModule;
import com.pacoworks.dereference.dependencies.modules.NetworkModule;
import com.pacoworks.dereference.dependencies.modules.SerializationModule;

import dagger.Component;

@Singleton
@Component(modules = {
        ApplicationModule.class, SerializationModule.class, NetworkModule.class
})
public interface ApplicationInjectionComponent extends DependencyDescription {
    final class Initializer {
        /* 10 MiB */
        private static final long CACHE_SIZE = 10 * 1024 * 1024;

        private Initializer() {
            /* No instances. */
        }

        static ApplicationInjectionComponent init(DereferenceApplication app) {
            return DaggerApplicationInjectionComponent
                    .builder()
                    .applicationModule(new ApplicationModule(app))
                    .serializationModule(new SerializationModule())
                    .networkModule(
                            new NetworkModule(BuildConfig.ENDPOINT, app.getCacheDir(), CACHE_SIZE))
                    .build();
        }
    }
}
