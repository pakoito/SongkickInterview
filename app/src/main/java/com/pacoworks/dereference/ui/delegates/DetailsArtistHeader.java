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
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

import com.hannesdorfmann.adapterdelegates.AdapterDelegate;
import com.pacoworks.dereference.R;
import com.pacoworks.dereference.model.Artist;

public class DetailsArtistHeader implements AdapterDelegate<List<Artist>> {
    public static final int TYPE = 98469;

    SerializedSubject<String, String> urlClicks = new SerializedSubject<>(
            PublishSubject.<String> create());

    @Override
    public int getItemViewType() {
        return TYPE;
    }

    @Override
    public boolean isForViewType(@NonNull List<Artist> items, int position) {
        return position == 0;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        return new ArtistHeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.element_artist_header, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull List<Artist> items, int position,
            @NonNull RecyclerView.ViewHolder holder) {
        ArtistHeaderViewHolder artistViewHolder = (ArtistHeaderViewHolder)holder;
        final Artist artist = items.get(position);
        artistViewHolder.name.setText(artist.getDisplayName());
        artistViewHolder.onTour.setVisibility(visibilityOnEmpty(artist.getOnTourUntil()));
        artistViewHolder.onTour.setText(String.format("On tour until %s", artist.getOnTourUntil()));
        artistViewHolder.urlHolder.setVisibility(visibilityOnEmpty(artist.getUri()));
        artistViewHolder.url.setText(artist.getUri());
        artistViewHolder.url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                urlClicks.onNext(artist.getUri());
            }
        });
    }

    public Observable<String> getUrlClicks() {
        return urlClicks.asObservable();
    }

    private int visibilityOnEmpty(String text) {
        return TextUtils.isEmpty(text) ? View.GONE : View.VISIBLE;
    }

    static class ArtistHeaderViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.artist_name_txv)
        TextView name;

        @Bind(R.id.artist_tour_txv)
        TextView onTour;

        @Bind(R.id.artist_url_txv)
        TextView url;

        @Bind(R.id.artist_url_holder)
        LinearLayout urlHolder;

        public ArtistHeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
