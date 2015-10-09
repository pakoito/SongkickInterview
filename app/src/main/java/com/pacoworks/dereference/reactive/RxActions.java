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

package com.pacoworks.dereference.reactive;

import lombok.experimental.UtilityClass;
import rx.functions.Action1;

@UtilityClass
public class RxActions {
    public static <T> Action1<T> doMultiple(final Action1<T> action1, final Action1<T> action2) {
        return new Action1<T>() {
            @Override
            public void call(T parameter) {
                action1.call(parameter);
                action2.call(parameter);
            }
        };
    }

    public static <T> Action1<T> doMultiple(final Action1<T> action1, final Action1<T> action2,
            final Action1<T> action3) {
        return new Action1<T>() {
            @Override
            public void call(T parameter) {
                action1.call(parameter);
                action2.call(parameter);
                action3.call(parameter);
            }
        };
    }
}
