package model;

import java.util.Set;

public record UserFullRecord(
        String id,
        String name,
        String surname,
        String username,
        String createdOn,
        String lastSeenOn,
        Set<String> roles
) {
}
