/*
 * Copyright (C) 2019 Dmitry Davletbaev <ddomgn@gmail.com>
 *
 * This file is part of rsscat.
 *
 * rsscat is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * rsscat is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with rsscat.  If not, see <https://www.gnu.org/licenses/>.
 */
package ddomgn.rsscat;

import java.util.Optional;

class Either<A, B> {

    private final A left;
    private final B right;

    private Either(A left, B right) {
        this.left = left;
        this.right = right;
    }

    static <A, B> Either<A, B> left(A left) {
        return new Either<>(left, null);
    }

    static <A, B> Either<A, B> right(B right) {
        return new Either<>(null, right);
    }

    Optional<A> getLeft() { return Optional.of(left); }
    Optional<B> getRight() { return Optional.of(right); }
    boolean isLeft() { return left != null; }
    boolean isRight() { return right != null; }
}
