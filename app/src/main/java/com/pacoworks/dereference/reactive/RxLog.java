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
import timber.log.Timber;

@UtilityClass
public class RxLog {
    public static Action1<Object> logMessage(final String message, final Object... args) {
        return new Action1<Object>() {
            @Override
            public void call(Object object) {
                /* Varargs only works with one array, so we have to mush them together */
                Object[] allArgs;
                if (null != args) {
                    allArgs = new Object[args.length + 1];
                    allArgs[0] = object;
                    System.arraycopy(args, 0, allArgs, 1, args.length);
                } else {
                    allArgs = new Object[] {
                            object
                    };
                }
                Timber.d(message, allArgs);
            }
        };
    }

    public static Action1<Object> logToString() {
        return new Action1<Object>() {
            @Override
            public void call(Object object) {
                Timber.d("%s", object.toString());
            }
        };
    }

    public static Action1<Throwable> logError() {
        return new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Timber.e(throwable, "");
            }
        };
    }

    public static Action1<Throwable> logErrorMessage(final String message) {
        return new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Timber.e(throwable, message);
            }
        };
    }

    public static Action1<Throwable> logErrorMessage(final String message, final Object... params) {
        return new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Timber.e(throwable, message, params);
            }
        };
    }
}
