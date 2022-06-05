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

    public static void oneOfIdOrEmailIsProvidedOrThrowException(Long userId, String userEmail) throws Exception {
        if (((userEmail == null || userEmail.isBlank()) && userId == null)
                || (userEmail !=null && !userEmail.isBlank() && userId != null)) {
            throw new Exception("must provide one of the two possible parameters: userEmail or userId");
        }
    }
}
