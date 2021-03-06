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

import java.util.List;

import lombok.ToString;

import com.google.gson.annotations.SerializedName;

@ToString
public class Results {
    @SerializedName("artist")
    private final List<Artist> artist;

    public Results(List<Artist> artist) {
        this.artist = artist;
    }

    public List<Artist> getArtist() {
        return artist;
    }
}
