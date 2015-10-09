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

package com.pacoworks.dereference.screens.songkicklist;

import java.util.List;

import rx.Observable;
import rx.functions.Action1;

import com.pacoworks.dereference.dependencies.skeleton.IUi;
import com.pacoworks.dereference.model.Artist;

public interface ISongkickListUI extends IUi {
    <T> Action1<T> showLoading();

    <T> Action1<T> hideLoading();

    Observable<Artist> getArtistClicks();

    Action1<? super List<Artist>> swapAdapter();

    Observable<CharSequence> getSearchBoxInputs();

    Action1<? super Artist> navigateToArtistDetails();
}
