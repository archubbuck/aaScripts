package org.aa.fletching.items;

public enum Bow {

    MAGIC_LONGBOW("Magic longbow", 859, "Magic longbow (u)", 70),
    MAGIC_SHORTBOW("Magic shortbow", 861, "Magic shortbow (u)", 72),
    YEW_LONGBOW("Yew longbow", 855, "Yew longbow (u)", 66),
    YEW_SHORTBOW("Yew shortbow", 857, "Yew shortbow (u)", 68),
    MAPLE_LONGBOW("Maple longbow", 851, "Maple longbow (u)", 62),
    MAPLE_SHORTBOW("Maple shortbow", 853, "Maple shortbow (u)", 64),
    WILLOW_LONGBOW("Willow longbow", 847, "Willow longbow (u)", 58),
    WILLOW_SHORTBOW("Willow shortbow", 849, "Willow shortbow (u)", 60),
    OAK_LONGBOW("Oak longbow", 845, "Oak longbow (u)", 56),
    OAK_SHORTBOW("Oak shortbow", 843, "Oak shortbow (u)", 54),
    LONGBOW("Longbow", 839, "Longbow (u)", 48),
    SHORTBOW("Shortbow", 841, "Shortbow (u)", 50);

    private final String strungName;
    private final int strungId;
    private final String unstrungName;
    private final int unstrungId;

    Bow(String strungName, int strungId, String unstrungName, int unstrungId) {
        this.strungName = strungName;
        this.strungId = strungId;
        this.unstrungName = unstrungName;
        this.unstrungId = unstrungId;
    }

    public String getStrungName() {
        return strungName;
    }
    public int getStrungId() {
        return strungId;
    }
    public String getUnstrungName() {
        return unstrungName;
    }
    public int getUnstrungId() {
        return unstrungId;
    }
}
