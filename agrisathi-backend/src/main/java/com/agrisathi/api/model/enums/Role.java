package com.agrisathi.api.model.enums;

/**
 * Platform user security roles:
 * - ROLE_ADMIN: System Administrator / Owner (Master app oversight; cannot alter user personal information)
 * - ROLE_FARMER: Primary User (Full access to all farming-related features: crops, profiles, disease scans, recommendations, marketplace posting)
 * - ROLE_BUYER: Produce Purchaser (Access to browse/search marketplace listings, send seller contact inquiries, weather, schemes)
 */
public enum Role {
    ROLE_ADMIN,
    ROLE_FARMER,
    ROLE_BUYER
}
