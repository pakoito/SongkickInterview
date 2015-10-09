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

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.Bind;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.pacoworks.dereference.ApplicationInjectionComponent;
import com.pacoworks.dereference.R;
import com.pacoworks.dereference.dependencies.ActivityInjectionComponent;
import com.pacoworks.dereference.dependencies.skeleton.ZimplBaseActivity;
import com.pacoworks.dereference.model.Artist;
import com.pacoworks.dereference.screens.songkickdetails.SongkickDetailsActivity;
import com.pacoworks.dereference.ui.adapters.ArtistListAdapter;

public class SongkickListActivity extends ZimplBaseActivity implements ISongkickListUI {
    @Bind(R.id.start_tlb)
    Toolbar toolbar;

    @Bind(R.id.start_ryv)
    RecyclerView artistRecyclerView;

    @Bind(R.id.offline_txv)
    TextView offlineText;

    @Bind(R.id.start_artist_edt)
    EditText searchBox;

    @Bind(R.id.loading_hld)
    View loadingView;

    @Override
    protected ActivityInjectionComponent newActivityInjector(
            ApplicationInjectionComponent applicationInjectionComponent) {
        return SongkickListInjectionComponent.Initializer.init(applicationInjectionComponent, this);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_start;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar.setTitle(R.string.activity_title_start);
        if (null == artistRecyclerView.getAdapter()) {
            artistRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            artistRecyclerView.setAdapter(new ArtistListAdapter());
        }
    }

    @Override
    public <T> Action1<T> showLoading() {
        return new Action1<T>() {
            @Override
            public void call(T t) {
                loadingView.setVisibility(View.VISIBLE);
            }
        };
    }

    @Override
    public <T> Action1<T> hideLoading() {
        return new Action1<T>() {
            @Override
            public void call(T t) {
                loadingView.setVisibility(View.GONE);
            }
        };
    }

    @Override
    public Observable<Artist> getArtistClicks() {
        ArtistListAdapter adapter = (ArtistListAdapter)artistRecyclerView.getAdapter();
        return adapter.getArtistClicks();
    }

    @Override
    public Action1<? super List<Artist>> swapAdapter() {
        ArtistListAdapter adapter = (ArtistListAdapter)artistRecyclerView.getAdapter();
        return adapter.swapElements();
    }

    @Override
    public Observable<CharSequence> getSearchBoxInputs() {
        return RxTextView.textChanges(searchBox);
    }

    @Override
    public Action1<? super Boolean> showOfflineOverlay() {
        return RxView.visibility(offlineText);
    }

    @Override
    public Action1<? super Artist> navigateToArtistDetails() {
        return new Action1<Artist>() {
            @Override
            public void call(Artist artist) {
                /* Could potentially be replaced by a fragment, dialog... */
                SongkickDetailsActivity.start(SongkickListActivity.this, artist);
            }
        };
    }
}
