package org.rentalpos.entities;

import javax.annotation.Nonnull;

public record Tool(@Nonnull String toolCode, @Nonnull String toolType, @Nonnull String brand) {
}