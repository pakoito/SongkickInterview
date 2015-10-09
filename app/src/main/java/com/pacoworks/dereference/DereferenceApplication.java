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

import javax.inject.Inject;

import android.app.Application;
import android.content.Context;

public class DereferenceApplication extends Application {
    @Inject
    MainInitialiser mainInitialiser;

    @Inject
    FlavourInitialiser flavourInitialiser;
    private ApplicationInjectionComponent component;

    public static DereferenceApplication get(Context context) {
        return (DereferenceApplication)context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        buildComponentAndInject();
    }

    private void buildComponentAndInject() {
        component = ApplicationInjectionComponent.Initializer.init(this);
        /* Force initialisers to be created */
        component.inject(this);
    }

    public ApplicationInjectionComponent component() {
        return component;
    }
}
