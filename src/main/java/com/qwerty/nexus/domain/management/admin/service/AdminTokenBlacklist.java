package com.qwerty.nexus.domain.management.admin.service;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AdminTokenBlacklist {
    private final Map<String, Instant> blacklist = new ConcurrentHashMap<>();

    public void blacklist(String token, long ttlMillis) {
        if (token == null || token.isBlank()) {
            return;
        }

        Instant expiry = ttlMillis > 0
                ? Instant.now().plusMillis(ttlMillis)
                : Instant.now();

        blacklist.put(token, expiry);
    }

    public boolean isBlacklisted(String token) {
        if (token == null || token.isBlank()) {
            return false;
        }

        Instant expiry = blacklist.get(token);
        if (expiry == null) {
            return false;
        }

        if (expiry.isBefore(Instant.now())) {
            blacklist.remove(token);
            return false;
        }

        return true;
    }

    public void remove(String token) {
        if (token == null || token.isBlank()) {
            return;
        }

        blacklist.remove(token);
    }

    public void purgeExpired() {
        Instant now = Instant.now();
        blacklist.entrySet().removeIf(entry -> entry.getValue().isBefore(now));
    }
}
