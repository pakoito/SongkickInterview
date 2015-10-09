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

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

import com.hannesdorfmann.adapterdelegates.AdapterDelegate;
import com.pacoworks.dereference.R;
import com.pacoworks.dereference.model.Artist;

public class ArtistDelegate implements AdapterDelegate<List<Artist>> {
    public static final int TYPE = 48984;

    SerializedSubject<Artist, Artist> artistClicks = new SerializedSubject<>(
            PublishSubject.<Artist> create());

    @Override
    public int getItemViewType() {
        return TYPE;
    }

    @Override
    public boolean isForViewType(@NonNull List<Artist> items, int position) {
        return (0 != position || items.size() != 0);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        return new ArtistViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.element_artist, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull List<Artist> items, int position,
            @NonNull RecyclerView.ViewHolder holder) {
        ArtistViewHolder artistViewHolder = (ArtistViewHolder)holder;
        final Artist artist = items.get(position);
        artistViewHolder.name.setText(artist.getDisplayName());
        artistViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                artistClicks.onNext(artist);
            }
        });
    }

    public Observable<Artist> getArtistClicks() {
        return artistClicks.asObservable();
    }

    static class ArtistViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.artist_name_txv)
        TextView name;

        public ArtistViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
