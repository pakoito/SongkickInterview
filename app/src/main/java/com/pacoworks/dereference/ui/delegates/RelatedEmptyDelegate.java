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

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hannesdorfmann.adapterdelegates.AdapterDelegate;
import com.pacoworks.dereference.R;
import com.pacoworks.dereference.model.Artist;

public class RelatedEmptyDelegate implements AdapterDelegate<List<Artist>> {
    public static final int TYPE = 987987;

    @Override
    public boolean isForViewType(@NonNull List<Artist> items, int position) {
        return items.size() < 2 && position == 1;
    }

    @Override
    public int getItemViewType() {
        return TYPE;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup) {
        return new EmptyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.element_empty, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull List<Artist> recordAndReports, int i,
            @NonNull RecyclerView.ViewHolder viewHolder) {
        EmptyViewHolder holder = (EmptyViewHolder)viewHolder;
        ((TextView)holder.itemView).setText(viewHolder.itemView.getContext().getText(
                R.string.empty_element_details));
    }

    private class EmptyViewHolder extends RecyclerView.ViewHolder {
        public EmptyViewHolder(View view) {
            super(view);
        }
    }
}
