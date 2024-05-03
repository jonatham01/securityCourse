package com.jwtservice.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public enum Role {

    ADMINISTRATOR(Arrays.asList(
            RolePermission.READ_ALL_PRODUCTS,
            RolePermission.READ_ONE_PRODUCT,
            RolePermission.CREATE_ONE_PRODUCT,
            RolePermission.UPDATE_ONE_PRODUCT,
            RolePermission.DISABLE_ONE_PRODUCT,

            RolePermission.READ_ALL_CATEGORY,
            RolePermission.READ_ONE_CATEGORY,
            RolePermission.CREATE_ONE_CATEGORY,
            RolePermission.UPDATE_ONE_CATEGORY,
            RolePermission.DISABLE_ONE_CATEGORY,

            RolePermission.READ_MY_PROFILE
    )),
    ASSISTANT_ADMINISTRATOR(Arrays.asList(
            RolePermission.READ_ALL_PRODUCTS,
            RolePermission.READ_ONE_PRODUCT,
            RolePermission.UPDATE_ONE_PRODUCT,

            RolePermission.READ_ALL_CATEGORY,
            RolePermission.READ_ONE_CATEGORY,
            RolePermission.UPDATE_ONE_CATEGORY,

            RolePermission.READ_MY_PROFILE
    )),
    CUSTOMER(List.of(
            RolePermission.READ_MY_PROFILE
    ));

    private List<RolePermission> permissions;

    Role(List<RolePermission> permissions){
        this.permissions = permissions;
    }

    public List<RolePermission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<RolePermission> permissions) {
        this.permissions = permissions;
    }
}
