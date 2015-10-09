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

import java.io.Serializable;

import lombok.ToString;

import com.google.gson.annotations.SerializedName;

@ToString
public class Identifier implements Serializable {
    static final long serialVersionUID = 5216212L;

    @SerializedName("eventsHref")
    private final String eventsHref;

    @SerializedName("href")
    private final String href;

    @SerializedName("mbid")
    private final String mbid;

    @SerializedName("setlistsHref")
    private final String setlistsHref;

    public Identifier(String eventsHref, String href, String mbid, String setlistsHref) {
        this.eventsHref = eventsHref;
        this.href = href;
        this.mbid = mbid;
        this.setlistsHref = setlistsHref;
    }

    public String getEventsHref() {
        return eventsHref;
    }

    public String getHref() {
        return href;
    }

    public String getMbid() {
        return mbid;
    }

    public String getSetlistsHref() {
        return setlistsHref;
    }
}
