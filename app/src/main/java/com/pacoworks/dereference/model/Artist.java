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
import java.util.List;

import lombok.ToString;

import com.google.gson.annotations.SerializedName;

@ToString
public class Artist implements Serializable {
    static final long serialVersionUID = 4653212L;

    @SerializedName("displayName")
    private final String displayName;

    @SerializedName("id")
    private final String id;

    @SerializedName("identifier")
    private final List<Identifier> identifier;

    @SerializedName("onTourUntil")
    private final String onTourUntil;

    @SerializedName("uri")
    private final String uri;

    public Artist(String displayName, String id, List<Identifier> identifier, String onTourUntil,
            String uri) {
        this.displayName = displayName;
        this.id = id;
        this.identifier = identifier;
        this.onTourUntil = onTourUntil;
        this.uri = uri;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getId() {
        return id;
    }

    public List<Identifier> getIdentifier() {
        return identifier;
    }

    public String getOnTourUntil() {
        return onTourUntil;
    }

    public String getUri() {
        return uri;
    }
}
