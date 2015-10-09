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
import rx.functions.Func2;

import com.pacoworks.dereference.reactive.tuples.Quadriple;
import com.pacoworks.dereference.reactive.tuples.Triple;
import com.pacoworks.dereference.reactive.tuples.Tuple;

@UtilityClass
public class RxTuples {
    public static <T, U> Func2<T, U, Tuple<T, U>> singleToTuple() {
        return new Func2<T, U, Tuple<T, U>>() {
            @Override
            public Tuple<T, U> call(T first, U second) {
                return new Tuple<>(first, second);
            }
        };
    }

    public static <T, U> Func2<T, U, Tuple<U, T>> singleToTupleInverse() {
        return new Func2<T, U, Tuple<U, T>>() {
            @Override
            public Tuple<U, T> call(T first, U second) {
                return new Tuple<>(second, first);
            }
        };
    }

    public static <T, U, V> Func2<T, Tuple<U, V>, Triple<T, U, V>> singleToTriple() {
        return new Func2<T, Tuple<U, V>, Triple<T, U, V>>() {
            @Override
            public Triple<T, U, V> call(T first, Tuple<U, V> second) {
                return new Triple<>(first, second.first, second.second);
            }
        };
    }

    public static <T, U, V> Func2<Tuple<T, U>, V, Triple<T, U, V>> tupleToTriple() {
        return new Func2<Tuple<T, U>, V, Triple<T, U, V>>() {
            @Override
            public Triple<T, U, V> call(Tuple<T, U> first, V second) {
                return new Triple<>(first.first, first.second, second);
            }
        };
    }

    public static <T, U, V, X> Func2<T, Triple<U, V, X>, Quadriple<T, U, V, X>> singleToQuadruple() {
        return new Func2<T, Triple<U, V, X>, Quadriple<T, U, V, X>>() {
            @Override
            public Quadriple<T, U, V, X> call(T first, Triple<U, V, X> second) {
                return new Quadriple<>(first, second.first, second.second, second.third);
            }
        };
    }

    public static <T, U, V, X> Func2<Tuple<T, U>, Tuple<V, X>, Quadriple<T, U, V, X>> tupleToQuadruple() {
        return new Func2<Tuple<T, U>, Tuple<V, X>, Quadriple<T, U, V, X>>() {
            @Override
            public Quadriple<T, U, V, X> call(Tuple<T, U> first, Tuple<V, X> second) {
                return new Quadriple<>(first.first, first.second, second.first, second.second);
            }
        };
    }

    public static <T, U, V, X> Func2<Triple<T, U, V>, X, Quadriple<T, U, V, X>> tripleToQuadruple() {
        return new Func2<Triple<T, U, V>, X, Quadriple<T, U, V, X>>() {
            @Override
            public Quadriple<T, U, V, X> call(Triple<T, U, V> first, X second) {
                return new Quadriple<>(first.first, first.second, first.third, second);
            }
        };
    }
}
