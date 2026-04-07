package com.hotel.model;

import java.io.Serializable;

/**
 * HotelPackage model using enum for package types.
 * Demonstrates: Enums, Encapsulation
 */
public class HotelPackage implements Serializable {
    private static final long serialVersionUID = 3L;

    /**
     * PackageType enum — each variant carries its own description and cost.
     * Demonstrates: Enum with fields and methods.
     */
    public enum PackageType {
        NONE("No Package", "No additional package selected.", 0.0),
        BASIC("Basic", "Includes breakfast and Wi-Fi.", 50.0),
        PREMIUM("Premium", "Includes breakfast, Wi-Fi, airport transfer, and spa access.", 120.0),
        LUXURY("Luxury", "All-inclusive: meals, Wi-Fi, airport transfer, spa, city tour, and butler service.", 250.0);

        private final String displayName;
        private final String description;
        private final double cost;

        PackageType(String displayName, String description, double cost) {
            this.displayName = displayName;
            this.description = description;
            this.cost = cost;
        }

        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
        public double getCost() { return cost; }

        @Override
        public String toString() { return displayName; }
    }

    private PackageType packageType;

    public HotelPackage() { this.packageType = PackageType.NONE; }

    public HotelPackage(PackageType packageType) {
        this.packageType = packageType;
    }

    public PackageType getPackageType() { return packageType; }
    public void setPackageType(PackageType packageType) { this.packageType = packageType; }

    public String getDescription() { return packageType.getDescription(); }
    public double getCost() { return packageType.getCost(); }

    @Override
    public String toString() { return packageType.getDisplayName(); }
}
