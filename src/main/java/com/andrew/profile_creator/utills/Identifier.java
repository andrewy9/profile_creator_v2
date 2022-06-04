package com.andrew.profile_creator.utills;

import java.util.Optional;

public record Identifier(Optional<Long> userId, Optional<String> userEmail) {

}
