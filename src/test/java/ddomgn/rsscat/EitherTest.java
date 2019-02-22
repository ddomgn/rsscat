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

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class EitherTest {

    @Test
    @DisplayName("Left value")
    public void leftValue() {
        final Either<Integer, String> value = Either.left(30);
        assertEquals(Optional.of(30), value.getLeft());
        assertEquals(Optional.empty(), value.getRight());
        assertTrue(value.isLeft());
        assertFalse(value.isRight());
    }

    @Test
    @DisplayName("Left value")
    public void rightValue() {
        final Either<Integer, String> value = Either.right("thirty");
        assertEquals(Optional.empty(), value.getLeft());
        assertEquals(Optional.of("thirty"), value.getRight());
        assertFalse(value.isLeft());
        assertTrue(value.isRight());
    }
}