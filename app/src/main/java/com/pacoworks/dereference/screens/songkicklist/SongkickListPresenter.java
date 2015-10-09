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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
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

public class SongkickListPresenter extends ZimplBasePresenter<ISongkickListUI, SongkickListState> {
    public static final int INPUT_DEBOUNCE_POLICY = 1;

    public static final int CONNECTIVITY_DEBOUNCE_POLICY = 1;

    public static final int MIN_SEARCH_POLICY = 2;

    private static final int TIMEOUT_POLICY = 60;

    private static final long SECOND_IN_NANOS = 1000000000l;

    private static final long STALE_SECONDS = 60 * SECOND_IN_NANOS;

    @Inject
    Observable<ConnectivityStatus> connectivity;

    @Inject
    SongkickApi songkickApi;

    public SongkickListPresenter() {
        super(SongkickListState.class);
    }

    @Override
    protected SongkickListState createNewState() {
        return new SongkickListState();
    }

    @Override
    public void create() {
    }

    @Override
    public void resume() {
        bindUntilPause(observeConnectivity(connectivity),
                refreshData(songkickApi, TIMEOUT_POLICY, BuildConfig.API_KEY), handleClicks());
    }

    private Subscription observeConnectivity(Observable<ConnectivityStatus> connectivity) {
        return connectivity.map(ConnectivityStatus.isEqualTo(ConnectivityStatus.OFFLINE))
                .subscribe(getUi().showOfflineOverlay());
    }

    private Subscription refreshData(final SongkickApi songkickApi, final int timeoutPolicy,
            final String apiKey) {
        return Observable
                .combineLatest(getProcessedConnectivityObservable(), getDebouncedSearchBoxInputs(),
                        RxTuples.<ConnectivityStatus, String> singleToTuple())
                .observeOn(AndroidSchedulers.mainThread()).doOnNext(getUi().showLoading())
                .flatMap(new Func1<Tuple<ConnectivityStatus, String>, Observable<List<Artist>>>() {
                    @Override
                    public Observable<List<Artist>> call(Tuple<ConnectivityStatus, String> status) {
                        if (ConnectivityStatus.isEqualTo(ConnectivityStatus.OFFLINE).call(
                                status.first))
                            return requestLocal(status.second).map(stateToCache())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .doOnNext(getUi().hideLoading());
                        else
                            return requestArtists(songkickApi, timeoutPolicy, status.second, apiKey)
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .doOnError(
                                            RxActions.doMultiple(RxLog.logError(),
                                                    handleForErrorType()))
                                    .onErrorReturn(provideEmptyList())
                                    .doOnNext(getUi().hideLoading());
                    }
                }).subscribe(getUi().swapAdapter());
    }

    Observable<ConnectivityStatus> getProcessedConnectivityObservable() {
        return connectivity.distinctUntilChanged()
                .debounce(CONNECTIVITY_DEBOUNCE_POLICY, TimeUnit.SECONDS)
                .doOnNext(RxLog.logMessage("Network status changed to %s"));
    }

    Observable<String> getDebouncedSearchBoxInputs() {
        return getUi().getSearchBoxInputs().debounce(INPUT_DEBOUNCE_POLICY, TimeUnit.SECONDS)
                .filter(new Func1<CharSequence, Boolean>() {
                    @Override
                    public Boolean call(CharSequence charSequence) {
                        return charSequence.length() >= MIN_SEARCH_POLICY;
                    }
                }).map(new Func1<CharSequence, String>() {
                    @Override
                    public String call(CharSequence charSequence) {
                        return charSequence.toString();
                    }
                });
    }

    Observable<List<Artist>> requestArtists(SongkickApi songkickApi, int timeoutPolicy,
            String query, String apiKey) {
        return Observable
                .concat(requestLocal(query),
                        requestNetwork(songkickApi, timeoutPolicy, query, apiKey).doOnNext(
                                storeState())).takeFirst(stalePolicy(query)).map(stateToCache());
    }

    private Func1<SongkickListState, Boolean> stalePolicy(final String query) {
        return new Func1<SongkickListState, Boolean>() {
            @Override
            public Boolean call(SongkickListState songkickListState) {
                final boolean isFresh = query.equalsIgnoreCase(songkickListState.getQuery())
                        && songkickListState.getCached().size() != 0
                        && System.nanoTime() - songkickListState.getLastUpdate() < STALE_SECONDS;
                Timber.d("Is this cache fresh? %s", isFresh);
                return isFresh;
            }
        };
    }

    Observable<SongkickListState> requestNetwork(SongkickApi songkickApi, int timeoutPolicy,
            final String query, String apiKey) {
        return songkickApi.getSearchResult(apiKey, query).subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.computation()).timeout(timeoutPolicy, TimeUnit.SECONDS)
                .flatMap(toCorrectResult()).map(new Func1<List<Artist>, SongkickListState>() {
                    @Override
                    public SongkickListState call(List<Artist> artists) {
                        return new SongkickListState(query, artists, System.nanoTime());
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

    Observable<SongkickListState> requestLocal(String query) {
        return getState().getQuery().equalsIgnoreCase(query) ? Observable.just(getState())
                : Observable.just(new SongkickListState());
    }

    private Func1<SongkickListState, List<Artist>> stateToCache() {
        return new Func1<SongkickListState, List<Artist>>() {
            @Override
            public List<Artist> call(SongkickListState startState) {
                return startState.getCached();
            }
        };
    }

    private Action1<SongkickListState> storeState() {
        return new Action1<SongkickListState>() {
            @Override
            public void call(SongkickListState songkickListState) {
                getState().setQuery(songkickListState.getQuery());
                getState().setCached(songkickListState.getCached());
                getState().setLastUpdate(songkickListState.getLastUpdate());
            }
        };
    }

    private Action1<Throwable> handleForErrorType() {
        return new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                if (throwable instanceof EmptyResultException) {
                    getUi().toastMessage().call("No results");
                } else {
                    getUi().toastError();
                }
            }
        };
    }

    private Func1<Throwable, ? extends List<Artist>> provideEmptyList() {
        return new Func1<Throwable, List<Artist>>() {
            @Override
            public List<Artist> call(Throwable throwable) {
                return new ArrayList<>();
            }
        };
    }

    private Subscription handleClicks() {
        return getUi().getArtistClicks().subscribe(getUi().navigateToArtistDetails());
    }

    @Override
    public void pause() {
    }

    @Override
    public void destroy() {
    }
}
