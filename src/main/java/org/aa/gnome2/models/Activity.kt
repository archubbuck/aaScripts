package org.aa.gnome2.models

enum class Activity(
        val category: ActivityCategory,
        val displayName: String,
        val description: String
) {
    SHOP_HECKEL_FUNCH(ActivityCategory.SHOP, "Heckel Funch", "Buy items from the shop owned by Heckel Funch");
}