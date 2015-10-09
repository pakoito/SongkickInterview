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

package com.pacoworks.dereference.model;

import lombok.ToString;

import com.google.gson.annotations.SerializedName;

@ToString
public class ResultsPage {
    @SerializedName("results")
    private final Results results;

    @SerializedName("totalEntries")
    private final int totalEntries;

    @SerializedName("perPage")
    private final int perPage;

    @SerializedName("page")
    private final int page;

    @SerializedName("status")
    private final String status;

    public ResultsPage(Results results, int totalEntries, int perPage, int page, String status) {
        this.results = results;
        this.totalEntries = totalEntries;
        this.perPage = perPage;
        this.page = page;
        this.status = status;
    }

    public Results getResults() {
        return results;
    }

    public int getTotalEntries() {
        return totalEntries;
    }

    public int getPerPage() {
        return perPage;
    }

    public int getPage() {
        return page;
    }

    public String getStatus() {
        return status;
    }
}
