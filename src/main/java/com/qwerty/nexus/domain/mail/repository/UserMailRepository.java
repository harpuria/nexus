package com.qwerty.nexus.domain.mail.repository;

import com.qwerty.nexus.domain.mail.entity.UserMailEntity;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class UserMailRepository {
    private final AtomicLong sequence = new AtomicLong(0);
    private final ConcurrentMap<Long, UserMailEntity> storage = new ConcurrentHashMap<>();

    public UserMailEntity save(UserMailEntity entity) {
        Long id = entity.userMailId();
        OffsetDateTime createdAt = entity.createdAt();
        if (id == null) {
            id = sequence.incrementAndGet();
            createdAt = OffsetDateTime.now();
        } else if (createdAt == null) {
            createdAt = OffsetDateTime.now();
        }

        UserMailEntity persisted = entity.toBuilder()
                .userMailId(id)
                .createdAt(createdAt)
                .build();

        storage.put(id, persisted);
        return persisted;
    }

    public Optional<UserMailEntity> findById(Long userMailId) {
        return Optional.ofNullable(storage.get(userMailId));
    }

    public List<UserMailEntity> findByUserId(Long userId) {
        return storage.values().stream()
                .filter(mail -> mail.userId().equals(userId))
                .sorted(Comparator.comparing(UserMailEntity::createdAt).reversed())
                .toList();
    }

    public UserMailEntity update(UserMailEntity entity) {
        storage.put(entity.userMailId(), entity);
        return entity;
    }

    public boolean delete(Long userMailId) {
        return storage.remove(userMailId) != null;
    }
}

