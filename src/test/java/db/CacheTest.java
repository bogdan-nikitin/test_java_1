package db;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class CacheTest {
    final Random random = new Random();
    String randomString() {
        final byte[] content = new byte[random.nextInt(0, 32)];
        random.nextBytes(content);
        return new String(content);
    }

    void testAddRecord(final int records, boolean duplicate) {
        final Cache cache = new Cache();
        final Long[] accounts = new Long[records];
        final String[] names = new String[records];
        final Double[] values = new Double[records];
        for (int i = 0; i < records; ++i) {
            accounts[i] = random.nextLong(0, 1024) * records + i;
            names[i] = STR."\{i}_\{randomString()}";
            values[i] = random.nextDouble(0, 2048) * records + i;
            cache.addRecord(new Cache.CacheRecord(accounts[i], names[i], values[i]));
        }
        assertEquals(records, cache.size(), "Invalid size");
        for (int i = 0; i < records; ++i) {
            final Cache.CacheRecord[] recordArray = {
                    cache.getRecordByAccount(accounts[i]),
                    cache.getRecordByName(names[i]),
                    cache.getRecordByValue(values[i])};
            for (final Cache.CacheRecord record : recordArray) {
                assertEquals(accounts[i], record.getAccount(), "Invalid account");
                assertEquals(names[i], record.getName(), "Invalid name");
                assertEquals(values[i], record.getValue(), "Invalid value");
            }
        }
        if (!duplicate) {
            return;
        }
        for (int i = 0; i < records; ++i) {
            cache.addRecord(new Cache.CacheRecord(accounts[i], names[i], values[i]));
        }
        assertEquals(records, cache.size(), "Duplicate increased size");
        for (int i = 0; i < records; ++i) {
            cache.addRecord(new Cache.CacheRecord(accounts[i], randomString(), random.nextDouble()));
            cache.addRecord(new Cache.CacheRecord(random.nextLong(), names[i], random.nextDouble()));
            cache.addRecord(new Cache.CacheRecord(random.nextLong(), randomString(), values[i]));
        }
        assertEquals(records, cache.size(), "Duplicate increased size");
    }

    @Test
    @DisplayName("Create record with random data")
    void testRecord() {
        final Long account = random.nextLong();
        final String name = randomString();
        final Double value = random.nextDouble();
        final Cache.CacheRecord record = new Cache.CacheRecord(account, name, value);
        assertAll(
                () -> assertEquals(account, record.getAccount(), "Invalid account"),
                () -> assertEquals(name, record.getName(), "Invalid name"),
                () -> assertEquals(value, record.getValue(), "Invalid value"));
    }

    @Test
    @DisplayName("Add record with random data to cache")
    void test_addRecord() {
        testAddRecord(1, false);
    }

    @Test
    @DisplayName("Add same record to cache twice")
    void test_addDuplicate() {
        testAddRecord(1, true);
    }
    @Test
    @DisplayName("Add many records with random data to cache")
    void test_addManyRecords() {
        testAddRecord(1000, false);
    }

    @Test
    @DisplayName("Add many same records to cache")
    void test_addManyDuplicates() {
        testAddRecord(1000, true);
    }

    @Test
    @DisplayName("Update record")
    void test_update() {
    }

}