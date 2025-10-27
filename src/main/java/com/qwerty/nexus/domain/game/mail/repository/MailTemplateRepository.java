package com.qwerty.nexus.domain.game.mail.repository;

import com.qwerty.nexus.domain.game.mail.entity.MailTemplateEntity;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class MailTemplateRepository {
    private final AtomicLong sequence = new AtomicLong(0);
    private final ConcurrentMap<Long, MailTemplateEntity> storage = new ConcurrentHashMap<>();

    public MailTemplateEntity save(MailTemplateEntity entity) {
        Long templateId = entity.templateId();
        OffsetDateTime createdAt = entity.createdAt();

        if (templateId == null) {
            templateId = sequence.incrementAndGet();
            createdAt = OffsetDateTime.now();
        } else if (createdAt == null) {
            createdAt = OffsetDateTime.now();
        }

        MailTemplateEntity persisted = entity.toBuilder()
                .templateId(templateId)
                .createdAt(createdAt)
                .build();

        storage.put(templateId, persisted);
        return persisted;
    }

    public Optional<MailTemplateEntity> findById(Long templateId) {
        return Optional.ofNullable(storage.get(templateId));
    }

    public List<MailTemplateEntity> findAll() {
        return storage.values().stream()
                .sorted(Comparator.comparing(MailTemplateEntity::createdAt).reversed())
                .toList();
    }
}

