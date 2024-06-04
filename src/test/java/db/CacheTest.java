package db;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class CacheTest {
    final Random random = new Random();
    String randomString() {
        final byte[] content = new byte[random.nextInt(0, 32)];
        random.nextBytes(content);
        return new String(content);
    }

    record RecordData(Long[] accounts, String[] names, Double[] values) {}

    RecordData generateData(final int records) {
        final Long[] accounts = new Long[records];
        final String[] names = new String[records];
        final Double[] values = new Double[records];
        for (int i = 0; i < records; ++i) {
            accounts[i] = random.nextLong(0, 1024) * records + i;
            names[i] = STR."\{i}_\{randomString()}";
            values[i] = random.nextDouble(0, 2048) * records + i;
        }
        return new RecordData(accounts, names, values);
    }

    void testAddRecord(final int records) {
        final RecordData data = generateData(records);
        final Cache cache = new Cache();
        IntStream.range(0, records).forEach(i -> cache.addRecord(new Cache.CacheRecord(data.accounts[i], data.names[i], data.values[i])));
        assertEquals(records, cache.size(), "Invalid size");
        for (int i = 0; i < records; ++i) {
            final Cache.CacheRecord[] recordArray = {
                    cache.getRecordByAccount(data.accounts[i]),
                    cache.getRecordByName(data.names[i]),
                    cache.getRecordByValue(data.values[i])};
            for (final Cache.CacheRecord record : recordArray) {
                assertEquals(data.accounts[i], record.getAccount(), "Invalid account");
                assertEquals(data.names[i], record.getName(), "Invalid name");
                assertEquals(data.values[i], record.getValue(), "Invalid value");
            }
        }
    }

    void testAddDuplicate(final int records) {
        final RecordData data = generateData(records);
        final Cache cache = new Cache();
        IntStream.range(0, records).forEach(i -> cache.addRecord(new Cache.CacheRecord(data.accounts[i], data.names[i], data.values[i])));
        for (int i = 0; i < records; ++i) {
            cache.addRecord(new Cache.CacheRecord(data.accounts[i], randomString(), random.nextDouble()));
            cache.addRecord(new Cache.CacheRecord(random.nextLong(), data.names[i], random.nextDouble()));
            cache.addRecord(new Cache.CacheRecord(random.nextLong(), randomString(), data.values[i]));
        }
        assertEquals(records, cache.size(), "Duplicate increased size");
    }

    void testUpdate(int records) {
        final RecordData data = generateData(records);
        final Cache cache = new Cache();
        IntStream.range(0, records).forEach(i -> cache.addRecord(new Cache.CacheRecord(data.accounts[i], data.names[i], data.values[i])));
        for (int i = 0; i < records; ++i) {
            cache.updateAccount(data.accounts[i], data.accounts[i] + records);
        }
        for (int i = 0; i < records; ++i) {
            assertEquals(data.accounts[i] + records, cache.getRecordByAccount(data.accounts[i] + records).getAccount(), "Invalid account");
        }
        for (int i = 0; i < records; ++i) {
            cache.updateName(data.names[i], STR."prefix\{data.names[i]}");
        }
        for (int i = 0; i < records; ++i) {
            assertEquals(STR."prefix\{data.names[i]}", cache.getRecordByName(STR."prefix\{data.names[i]}").getName(), "Invalid name");
        }
        for (int i = 0; i < records; ++i) {
            cache.updateValue(data.values[i], data.values[i] + records);
        }
        for (int i = 0; i < records; ++i) {
            assertEquals(data.values[i] + records, cache.getRecordByValue(data.values[i] + records).getValue(), "Invalid value");
        }
        assertEquals(records, cache.size(), "Size changed");
    }

    void testRemove(int records) {
        final RecordData data = generateData(records);
        final Cache cache = new Cache();
        IntStream.range(0, records).forEach(i -> cache.addRecord(new Cache.CacheRecord(data.accounts[i], data.names[i], data.values[i])));
        for (int i = 0; i < records; ++i) {
            cache.removeRecord(cache.getRecordByAccount(data.accounts[i]));
        }
        assertEquals(0, cache.size(), "Not all records removed");
        for (int i = 0; i < records; ++i) {
            final Cache.CacheRecord[] recordArray = {
                    cache.getRecordByAccount(data.accounts[i]),
                    cache.getRecordByName(data.names[i]),
                    cache.getRecordByValue(data.values[i])};
            for (final Cache.CacheRecord record : recordArray) {
                assertNull(record, "Dangling record");
            }
        }
    }


    @Test
    @DisplayName("Create record with random data")
    void testRecord() {
        final Long account = random.nextLong();
        final String name = randomString();
        final Double value = random.nextDouble();
        final Cache.CacheRecord record = new Cache.CacheRecord(account, name, value);
        assertEquals(account, record.getAccount(), "Invalid account");
        assertEquals(name, record.getName(), "Invalid name");
        assertEquals(value, record.getValue(), "Invalid value");
    }

    @Test
    @DisplayName("Add record with random data to cache")
    void test_addRecord() {
        testAddRecord(1);
    }

    @Test
    @DisplayName("Add same record to cache twice")
    void test_addDuplicate() {
        testAddDuplicate(1);
    }
    @Test
    @DisplayName("Add many records with random data to cache")
    void test_addManyRecords() {
        testAddRecord(1000);
    }

    @Test
    @DisplayName("Add many same records to cache")
    void test_addManyDuplicates() {
        testAddDuplicate(1000);
    }

    @Test
    @DisplayName("Update one record")
    void test_update() {
        testUpdate(1);
    }

    @Test
    @DisplayName("Update many records")
    void test_updateMany() {
        testUpdate(1000);
    }

    @Test
    @DisplayName("Remove one record")
    void test_remove() {
        testRemove(1);
    }

    @Test
    @DisplayName("Remove many records")
    void test_removeMany() {
        testRemove(1000);
    }
}