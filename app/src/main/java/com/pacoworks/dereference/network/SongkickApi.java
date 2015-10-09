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

package com.pacoworks.dereference.network;

import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

import com.pacoworks.dereference.model.SearchResult;

public interface SongkickApi {
    @GET("search/artists.json")
    Observable<SearchResult> getSearchResult(@Query("apikey") String apikey,
            @Query("query") String url);

    @GET("artists/{artist_id}/similar_artists.json")
    Observable<SearchResult> getRelatedArtists(@Path("artist_id") String artistId,
            @Query("apikey") String apikey);
}
