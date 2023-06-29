package org.aa.gnome2.models

enum class ActivityCategory(
        val displayName: String,
        val description: String
) {
    SHOP("Shop", "Buy items from various shops"),
    MIX("Mix", "Mix various gnome cocktails"),
    COOK("Cook", "Cook various gnome dishes");
}