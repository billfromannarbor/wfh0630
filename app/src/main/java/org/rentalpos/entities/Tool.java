package org.rentalpos.entities;

import javax.annotation.Nonnull;

/**
 * Represents a Tool
 * @param toolCode - A unique code representing a tool
 * @param toolType - type of tool
 * @param brand - the brand of the tool
 */
public record Tool(@Nonnull String toolCode, @Nonnull String toolType, @Nonnull String brand) {
}