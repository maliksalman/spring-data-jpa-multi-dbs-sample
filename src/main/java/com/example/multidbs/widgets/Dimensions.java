package com.example.multidbs.widgets;

import jakarta.persistence.Embeddable;

@Embeddable
public record Dimensions(
        int width,
        int height,
        String unit
) {}

