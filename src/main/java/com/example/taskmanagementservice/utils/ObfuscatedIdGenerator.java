package com.example.taskmanagementservice.utils;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import java.io.Serializable;

public class ObfuscatedIdGenerator implements IdentifierGenerator {

    private static final long RANDOM_SEED = 0x7F5A3C1DL; // Соль (измените на свою)
    private static final int ROUNDS = 4;

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) {
        long rawId = getNextSequenceValue(session); // Получаем следующий ID из последовательности
        return feistelEncrypt(rawId, RANDOM_SEED, ROUNDS);
    }

    private long getNextSequenceValue(SharedSessionContractImplementor session) {
        return session.createNativeQuery(
                        "SELECT nextval('task_id_seq')", Long.class
                )
                .setComment("ObfuscatedIdGenerator sequence fetch")
                .setCacheable(false)
                .getSingleResult();
    }

    // Feistel-сеть для шифрования
    private static long feistelEncrypt(long value, long seed, int rounds) {
        long left = (value >> 32) & 0xFFFFFFFFL;
        long right = value & 0xFFFFFFFFL;

        for (int i = 0; i < rounds; i++) {
            long temp = left;
            left = right;
            right = temp ^ (hash(right, seed + i) & 0xFFFFFFFFL);
        }

        return (left << 32) | right;
    }

    private static long hash(long value, long seed) {
        return (value * 0x5DEECE66DL + seed) & 0xFFFFFFFFL;
    }
}
