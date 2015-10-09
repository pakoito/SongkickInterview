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

package com.pacoworks.dereference.ui.delegates;

import java.util.List;

import android.support.annotation.NonNull;

import com.pacoworks.dereference.model.Artist;

public class RelatedArtistDelegate extends ArtistDelegate {
    public static final int TYPE = 98845;

    @Override
    public boolean isForViewType(@NonNull List<Artist> items, int position) {
        return items.size() > 1 && position > 0;
    }

    @Override
    public int getItemViewType() {
        return TYPE;
    }
}
