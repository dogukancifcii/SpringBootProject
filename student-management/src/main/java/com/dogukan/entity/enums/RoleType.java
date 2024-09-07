package com.dogukan.entity.enums;


public enum RoleType {
    ADMIN("Admin"),
    TEACHER("Teacher"),
    STUDENT("Student"),
    MANAGER("Dean"),
    ASSISTANT_MANAGER("ViceDean");

    public final String name;//String degerleri aciklayici olsun diye atamak icin yazilir.

    RoleType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
