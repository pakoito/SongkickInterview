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

import java.util.List;

import rx.Observable;
import rx.functions.Action1;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;

import com.jakewharton.rxbinding.view.RxView;
import com.pacoworks.dereference.ApplicationInjectionComponent;
import com.pacoworks.dereference.R;
import com.pacoworks.dereference.dependencies.ActivityInjectionComponent;
import com.pacoworks.dereference.dependencies.skeleton.ZimplBaseActivity;
import com.pacoworks.dereference.model.Artist;
import com.pacoworks.dereference.ui.adapters.RelatedArtistListAdapter;

public class SongkickDetailsActivity extends ZimplBaseActivity implements ISongkickDetailsUI {
    public static final String ARTIST = "ARTIST";

    @Bind(R.id.details_tlb)
    Toolbar toolbar;

    @Bind(R.id.details_ryv)
    RecyclerView artistRecyclerView;

    @Bind(R.id.offline_txv)
    TextView offlineText;

    @Bind(R.id.loading_hld)
    View loadingView;

    private Artist artist;

    public static void start(Activity activity, Artist artist) {
        final Intent intent = new Intent(activity, SongkickDetailsActivity.class);
        intent.putExtra(ARTIST, artist);
        activity.startActivity(intent);
    }

    @Override
    protected ActivityInjectionComponent newActivityInjector(
            ApplicationInjectionComponent applicationInjectionComponent) {
        return SongkickDetailsInjectionComponent.Initializer.init(applicationInjectionComponent,
                this);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_details;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        artist = (Artist)getIntent().getSerializableExtra(ARTIST);
        if (null == artist) {
            toastMessage().call("No artist available");
            finish();
        } else {
            toolbar.setTitle(R.string.activity_title_details);
            if (null == artistRecyclerView.getAdapter()) {
                artistRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                artistRecyclerView.setAdapter(new RelatedArtistListAdapter());
            }
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
    public Action1<? super Boolean> showOfflineOverlay() {
        return RxView.visibility(offlineText);
    }

    @Override
    public Action1<List<Artist>> swapAdapter() {
        RelatedArtistListAdapter adapter = (RelatedArtistListAdapter)artistRecyclerView
                .getAdapter();
        return adapter.swapElements();
    }

    @Override
    public Observable<Artist> getArtist() {
        return Observable.just(artist);
    }

    @Override
    public Observable<Artist> getArtistClicks() {
        final RelatedArtistListAdapter adapter = (RelatedArtistListAdapter)artistRecyclerView
                .getAdapter();
        return adapter.getArtistClicks();
    }

    @Override
    public Observable<String> getHeaderUrlClicks() {
        RelatedArtistListAdapter adapter = (RelatedArtistListAdapter)artistRecyclerView
                .getAdapter();
        return adapter.getHeaderUrlClicks();
    }

    @Override
    public Action1<String> navigateToUrl() {
        return new Action1<String>() {
            @Override
            public void call(String url) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        };
    }

    @Override
    public Action1<? super Artist> navigateToArtistDetails() {
        return new Action1<Artist>() {
            @Override
            public void call(Artist artist) {
                /* Could potentially be replaced by a fragment, dialog... */
                SongkickDetailsActivity.start(SongkickDetailsActivity.this, artist);
            }
        };
    }
}
