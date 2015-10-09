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

package com.pacoworks.dereference.ui.adapters;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.hannesdorfmann.adapterdelegates.AdapterDelegatesManager;
import com.pacoworks.dereference.model.Artist;
import com.pacoworks.dereference.ui.delegates.ArtistDelegate;
import com.pacoworks.dereference.ui.delegates.EmptyDelegate;

public class ArtistListAdapter extends RecyclerView.Adapter {
    private final List<Artist> elementsMap = new ArrayList<>();

    private final AdapterDelegatesManager<List<Artist>> delegatesManager = new AdapterDelegatesManager<>();

    private final ArtistDelegate artistDelegate;

    public ArtistListAdapter() {
        artistDelegate = new ArtistDelegate();
        delegatesManager.addDelegate(artistDelegate);
        delegatesManager.addDelegate(new EmptyDelegate<Artist>());
    }

    @Override
    public int getItemViewType(int position) {
        return delegatesManager.getItemViewType(elementsMap, position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return delegatesManager.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        delegatesManager.onBindViewHolder(elementsMap, position, holder);
    }

    @Override
    public int getItemCount() {
        return elementsMap.size() == 0 ? 1 : elementsMap.size();
    }

    public Action1<List<Artist>> swapElements() {
        return new Action1<List<Artist>>() {
            @Override
            public void call(List<Artist> newElements) {
                elementsMap.clear();
                elementsMap.addAll(newElements);
                notifyDataSetChanged();
            }
        };
    }

    public Observable<Artist> getArtistClicks() {
        return artistDelegate.getArtistClicks();
    }
}
