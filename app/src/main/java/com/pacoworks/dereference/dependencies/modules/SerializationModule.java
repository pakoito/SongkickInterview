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

import javax.inject.Named;
import javax.inject.Singleton;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import dagger.Module;
import dagger.Provides;

@Module
public class SerializationModule {
    @Provides
    @Singleton
    Gson provideDefaultGson() {
        return new Gson();
    }

    @Provides
    @Named("default")
    @Singleton
    ObjectMapper provideDefaultMapper() {
        return new ObjectMapper();
    }

    @Provides
    @Named("fields")
    @Singleton
    ObjectMapper provideFieldsMapper() {
        return new ObjectMapper() {
            {
                setVisibilityChecker(this.getSerializationConfig().getDefaultVisibilityChecker()
                        .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                        .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                        .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                        .withCreatorVisibility(JsonAutoDetect.Visibility.NONE));
            }
        };
    }

    @Provides
    @Named("unknown")
    @Singleton
    ObjectMapper provideUnknownMapper() {
        return new ObjectMapper() {
            {
                configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            }
        };
    }
}
