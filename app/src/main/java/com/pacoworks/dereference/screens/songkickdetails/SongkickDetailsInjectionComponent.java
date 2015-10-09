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

package com.pacoworks.dereference.screens.songkickdetails;

import com.pacoworks.dereference.ApplicationInjectionComponent;
import com.pacoworks.dereference.dependencies.ActivityInjectionComponent;
import com.pacoworks.dereference.dependencies.ForActivity;
import com.pacoworks.dereference.dependencies.modules.ActivityModule;

import dagger.Component;

@ForActivity
@Component(dependencies = ApplicationInjectionComponent.class, modules = {
        ActivityModule.class, SongkickDetailsModule.class
})
public interface SongkickDetailsInjectionComponent extends
        ActivityInjectionComponent<SongkickDetailsPresenter> {
    final class Initializer {
        private Initializer() {
            /* No instances. */
        }

        public static ActivityInjectionComponent init(
                ApplicationInjectionComponent applicationInjectionComponent,
                SongkickDetailsActivity activity) {
            return DaggerSongkickDetailsInjectionComponent
                    .builder()
                    .applicationInjectionComponent(applicationInjectionComponent)
                    .songkickDetailsModule(new SongkickDetailsModule())
                    .activityModule(
                            new ActivityModule(activity, applicationInjectionComponent
                                    .okHttpDownloader())).build();
        }
    }
}
