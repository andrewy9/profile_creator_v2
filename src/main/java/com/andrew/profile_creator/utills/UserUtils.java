package com.andrew.profile_creator.utills;

import com.andrew.profile_creator.exception.RoleTypeNotFoundException;
import com.andrew.profile_creator.repository.roles.RoleTypes;

import java.util.Arrays;

public class UserUtils {

    public static RoleTypes roleTypeCheck (String roleName) throws RoleTypeNotFoundException {
        return Arrays.stream(RoleTypes.values()).filter(roleTypes ->
                        roleTypes.name().equals(roleName))
                .findAny()
                .orElseThrow(() -> new RoleTypeNotFoundException("ROLE NOT FOUND", roleName));
    }
}
