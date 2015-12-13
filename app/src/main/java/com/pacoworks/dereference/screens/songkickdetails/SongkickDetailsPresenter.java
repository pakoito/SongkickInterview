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
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import timber.log.Timber;

import com.github.pwittchen.reactivenetwork.library.ConnectivityStatus;
import com.pacoworks.dereference.BuildConfig;
import com.pacoworks.dereference.dependencies.skeleton.ZimplBasePresenter;
import com.pacoworks.dereference.model.Artist;
import com.pacoworks.dereference.model.SearchResult;
import com.pacoworks.dereference.network.EmptyResultException;
import com.pacoworks.dereference.network.SongkickApi;
import com.pacoworks.dereference.reactive.RxActions;
import com.pacoworks.dereference.reactive.RxLog;
import com.pacoworks.dereference.reactive.RxTuples;
import com.pacoworks.dereference.reactive.tuples.Tuple;

public class SongkickDetailsPresenter extends
        ZimplBasePresenter<ISongkickDetailsUI, SongkickDetailsState> {
    public static final int CONNECTIVITY_DEBOUNCE_POLICY = 1;

    private static final int TIMEOUT_POLICY = 60;
    
    private final Action1<List<Artist>> logAndShowError = RxActions.<List<Artist>>doMultiple(RxLog.logError(), toastForErrorType());

    @Inject
    Observable<ConnectivityStatus> connectivity;

    @Inject
    SongkickApi songkickApi;

    public SongkickDetailsPresenter() {
        super(SongkickDetailsState.class);
    }

    @Override
    protected SongkickDetailsState createNewState() {
        return new SongkickDetailsState();
    }

    @Override
    public void create() {
        /* Start the screen with our current artist */
        bindUntilDestroy(getUi().getArtist().toList().subscribe(getUi().swapAdapter()));
    }

    @Override
    public void resume() {
        bindUntilPause(observeConnectivity(connectivity),
                refreshData(songkickApi, TIMEOUT_POLICY, BuildConfig.API_KEY),
                handleArtistClicks(), handleUrlClicks());
    }

    private Subscription observeConnectivity(Observable<ConnectivityStatus> connectivity) {
        return connectivity.map(ConnectivityStatus.isEqualTo(ConnectivityStatus.OFFLINE))
                .subscribe(getUi().showOfflineOverlay());
    }

    private Subscription refreshData(final SongkickApi songkickApi, final int timeoutPolicy,
            final String apiKey) {
        return Observable
                .combineLatest(getProcessedConnectivityObservable(), getArtistId(),
                        RxTuples.<ConnectivityStatus, String> singleToTuple())
                .observeOn(AndroidSchedulers.mainThread()).doOnNext(getUi().showLoading())
                .flatMap(new Func1<Tuple<ConnectivityStatus, String>, Observable<List<Artist>>>() {
                    @Override
                    public Observable<List<Artist>> call(Tuple<ConnectivityStatus, String> status) {
                        if (ConnectivityStatus.isEqualTo(ConnectivityStatus.OFFLINE).call(
                                status.first))
                            return requestLocal().flatMap(checkCurrentArtistAvailable())
                                    .map(stateToCache()).onErrorReturn(giveJustArtist())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .doOnNext(getUi().hideLoading());
                        else
                            return requestArtists(songkickApi, timeoutPolicy, status.second, apiKey)
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .doOnError(logAndShowError)
                                    .onErrorReturn(giveJustArtist())
                                    .doOnNext(getUi().hideLoading());
                    }
                }).subscribe(getUi().swapAdapter());
    }

    Observable<ConnectivityStatus> getProcessedConnectivityObservable() {
        return connectivity.distinctUntilChanged()
                .debounce(CONNECTIVITY_DEBOUNCE_POLICY, TimeUnit.SECONDS)
                .doOnNext(RxLog.logMessage("Network status changed to %s"));
    }

    Observable<String> getArtistId() {
        return getUi().getArtist().map(new Func1<Artist, String>() {
            @Override
            public String call(Artist artist) {
                return artist.getId();
            }
        });
    }

    private Func1<SongkickDetailsState, Observable<SongkickDetailsState>> checkCurrentArtistAvailable() {
        return new Func1<SongkickDetailsState, Observable<SongkickDetailsState>>() {
            @Override
            public Observable<SongkickDetailsState> call(SongkickDetailsState songkickDetailsState) {
                return songkickDetailsState.getCached() != null
                        && songkickDetailsState.getCached().size() > 0 ? Observable
                        .just(songkickDetailsState) : Observable
                        .<SongkickDetailsState> error(new IllegalStateException(
                                "Current artist not cached"));
            }
        };
    }

    Observable<List<Artist>> requestArtists(SongkickApi songkickApi, int timeoutPolicy,
            String artistId, String apiKey) {
        return Observable
                .concat(requestLocal(),
                        requestNetwork(songkickApi, timeoutPolicy, artistId, apiKey).doOnNext(
                                storeState())).takeFirst(stalePolicy()).map(stateToCache());
    }

    private Func1<SongkickDetailsState, Boolean> stalePolicy() {
        return new Func1<SongkickDetailsState, Boolean>() {
            @Override
            public Boolean call(SongkickDetailsState recordAndReports) {
                final boolean isFresh = recordAndReports.getCached().size() > 1;
                Timber.d("Is this cache fresh? %s", isFresh);
                return isFresh;
            }
        };
    }

    Observable<SongkickDetailsState> requestNetwork(SongkickApi songkickApi, int timeoutPolicy,
            String artistId, String apiKey) {
        return songkickApi.getRelatedArtists(artistId, apiKey).subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.computation())
                .timeout(timeoutPolicy, TimeUnit.SECONDS)
                .flatMap(toCorrectResult()).zipWith(getUi().getArtist(), addAtStart())
                .map(new Func1<List<Artist>, SongkickDetailsState>() {
                    @Override
                    public SongkickDetailsState call(List<Artist> artists) {
                        return new SongkickDetailsState(artists);
                    }
                });
    }

    private Func1<SearchResult, Observable<List<Artist>>> toCorrectResult() {
        return new Func1<SearchResult, Observable<List<Artist>>>() {
            @Override
            public Observable<List<Artist>> call(SearchResult searchResult) {
                return nullCheck(searchResult) ? Observable.just(searchResult.getResultsPage()
                        .getResults().getArtist()) : Observable
                        .<List<Artist>> error(new EmptyResultException(
                                "Server returned invalid result"));
            }
        };
    }

    private boolean nullCheck(SearchResult searchResult) {
        return (searchResult != null && searchResult.getResultsPage() != null
                && searchResult.getResultsPage().getResults() != null
                && searchResult.getResultsPage().getResults().getArtist() != null && searchResult
                .getResultsPage().getResults().getArtist().size() > 0);
    }

    private Func2<List<Artist>, Artist, List<Artist>> addAtStart() {
        return new Func2<List<Artist>, Artist, List<Artist>>() {
            @Override
            public List<Artist> call(List<Artist> artists, Artist artist) {
                artists.add(0, artist);
                return artists;
            }
        };
    }

    Observable<SongkickDetailsState> requestLocal() {
        return Observable.just(getState());
    }

    private Func1<SongkickDetailsState, List<Artist>> stateToCache() {
        return new Func1<SongkickDetailsState, List<Artist>>() {
            @Override
            public List<Artist> call(SongkickDetailsState startState) {
                return startState.getCached();
            }
        };
    }

    private Action1<List<Artist>> storeList() {
        return new Action1<List<Artist>>() {
            @Override
            public void call(List<Artist> artists) {
                getState().setCached(artists);
            }
        };
    }

    private Action1<SongkickDetailsState> storeState() {
        return new Action1<SongkickDetailsState>() {
            @Override
            public void call(SongkickDetailsState state) {
                getState().setCached(state.getCached());
            }
        };
    }

    private Action1<Throwable> toastForErrorType() {
        return new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                if (throwable instanceof EmptyResultException) {
                    getUi().toastMessage().call("No results");
                } else {
                    getUi().toastError().call(throwable);
                }
            }
        };
    }

    private Func1<Throwable, ? extends List<Artist>> giveJustArtist() {
        return new Func1<Throwable, List<Artist>>() {
            @Override
            public List<Artist> call(Throwable throwable) {
                return getUi().getArtist().toList().toBlocking().first();
            }
        };
    }

    private Subscription handleArtistClicks() {
        return getUi().getArtistClicks().subscribe(getUi().navigateToArtistDetails());
    }

    private Subscription handleUrlClicks() {
        return getUi().getHeaderUrlClicks().subscribe(getUi().navigateToUrl());
    }

    @Override
    public void pause() {
    }

    @Override
    public void destroy() {
    }
}
