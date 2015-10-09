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

package com.pacoworks.dereference.dependencies.skeleton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import lombok.AccessLevel;
import lombok.Getter;
import rx.Subscription;

import com.google.gson.Gson;

public abstract class ZimplBasePresenter<T extends IUi, U extends ZimpleBaseState> {
    private final Class<U> stateClass;

    private final List<Subscription> pauseSubscriptions = new ArrayList<>();

    private final List<Subscription> destroySubscriptions = new ArrayList<>();

    @Inject
    @Getter(AccessLevel.PROTECTED)
    Gson gson;

    @Getter(AccessLevel.PROTECTED)
    private T ui;

    @Getter(AccessLevel.PROTECTED)
    private U state;

    public ZimplBasePresenter(Class<U> stateClass) {
        this.stateClass = stateClass;
    }

    // DEPENDENCY BINDING METHODS
    final void bindUi(T activity) {
        ui = activity;
    }

    final void unbindUi() {
        ui = null;
    }

    final void restoreState(String restoredState) {
        if (restoredState == null || restoredState.isEmpty()) {
            state = createNewState();
        } else {
            state = gson.fromJson(restoredState, stateClass);
        }
    }

    final String saveState() {
        return gson.toJson(state);
    }

    // LIFECYCLE
    final void createBase() {
        create();
    }

    final void resumeBase() {
        resume();
    }

    final void pauseBase() {
        unsubscribeAll(pauseSubscriptions);
        pause();
    }

    final void destroyBase() {
        unsubscribeAll(destroySubscriptions);
        destroy();
    }

    private void unsubscribeAll(List<Subscription> subscriptions) {
        for (Subscription subscription : subscriptions) {
            subscription.unsubscribe();
        }
        subscriptions.clear();
    }

    // PUBLIC API
    public void bindUntilPause(Subscription... subscriptions) {
        if (null != subscriptions) {
            Collections.addAll(pauseSubscriptions, subscriptions);
        }
    }

    public void bindUntilDestroy(Subscription... subscriptions) {
        if (null != subscriptions) {
            Collections.addAll(destroySubscriptions, subscriptions);
        }
    }

    // ABSTRACTS
    protected abstract U createNewState();

    public abstract void create();

    public abstract void resume();

    public abstract void pause();

    public abstract void destroy();
}
