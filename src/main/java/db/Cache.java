package db;

import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * In memory cache.
 *
 * Allows effectively store, find, update records of the following format:
 * <pre>
 * {
 *     "account": "234678", //long
 *     "name": "Иванов Иван Иванович", //string
 *     "value": "2035.34" //double
 * }
 * </pre>
 *
 * All operations on cache have <var>log(n)</var> time cost,
 * except {@link #size()}, which has constant time complexity.
 *
 * @author Bogdan Nikitin
 */
public class Cache {
    public static class CacheRecord {
        private Long account;
        private String name;
        private Double value;
        public CacheRecord(final Long account, final String name, final Double value) {
            this.account = account;
            this.name = name;
            this.value = value;
        }

        public Double getValue() {
            return value;
        }

        public Long getAccount() {
            return account;
        }

        public String getName() {
            return name;
        }
    }
    private final Map<Long, CacheRecord> accountMap = new TreeMap<>();
    private final Map<String, CacheRecord> nameMap = new TreeMap<>();
    private final Map<Double, CacheRecord> valueMap = new TreeMap<>();

    /**
     * Constructs a new, empty cache.
     */
    public Cache() {
    }

    /**
     * Returns the number of records in cache.
     * @return number of records in cache.
     */
    public int size() {
        return accountMap.size();
    }

    /**
     * Adds the specified record to this cache.
     * The record is added to the cache only if all its field are unique to this cache.
     * @param record record to add.
     * @return {@code true} if all fields of the record are unique.
     * @throws NullPointerException if record is null.
     */
    public boolean addRecord(final CacheRecord record) {
        Objects.requireNonNull(record);
        if (accountMap.containsKey(record.getAccount()) ||
                nameMap.containsKey(record.getName()) ||
        valueMap.containsKey(record.getValue())) {
            return false;
        }
        accountMap.put(record.getAccount(), record);
        nameMap.put(record.getName(), record);
        valueMap.put(record.getValue(), record);
        return true;
    }

    /**
     * Returns the record with specified <var>account</var>, or {@code null} if there is no such record.
     * @param account <var>account</var> of record.
     * @return record with specified <var>account</var>, or {@code null} if there is no such record.
     */
    public CacheRecord getRecordByAccount(final Long account) {
        return accountMap.get(account);
    }

    /**
     * Returns the record with specified <var>name</var>, or {@code null} if there is no such record.
     * @param name <var>name</var> of record.
     * @return record with specified <var>name</var>, or {@code null} if there is no such record.
     */
    public CacheRecord getRecordByName(final String name) {
        return nameMap.get(name);
    }

    /**
     * Returns the record with specified <var>value</var>, or {@code null} if there is no such record.
     * @param value <var>value</var> of record.
     * @return record with specified <var>value</var>, or {@code null} if there is no such record.
     */
    public CacheRecord getRecordByValue(final Double value) {
        return valueMap.get(value);
    }

    /**
     * Updates <var>account</var> field of the record with specified {@code oldAccount} <var>account</var>
     * only if cache contains such a record.
     * @param oldAccount record <var>account</var>.
     * @param newAccount <var>account</var> to be set.
     * @return updated record, or {@code null} if there is no such record.
     */
    public CacheRecord updateAccount(final Long oldAccount, final Long newAccount) {
        final CacheRecord record = updateField(accountMap, oldAccount, newAccount);
        if (record != null) {
            record.account = newAccount;
        }
        return record;
    }

    /**
     * Updates <var>name</var> field of the record with specified {@code oldName} <var>name</var>
     * only if cache contains such a record.
     * @param oldName record <var>name</var>.
     * @param newName <var>name</var> to be set.
     * @return updated record, or {@code null} if there is no such record.
     */
    public CacheRecord updateName(final String oldName, final String newName) {
        final CacheRecord record = updateField(nameMap, oldName, newName);
        if (record != null) {
            record.name = newName;
        }
        return record;
    }

    /**
     * Updates <var>value</var> field of the record with specified {@code oldValue} <var>value</var>
     * only if cache contains such a record.
     * @param oldValue record <var>value</var>.
     * @param newValue <var>value</var> to be set.
     * @return updated record, or {@code null} if there is no such record.
     */
    public CacheRecord updateValue(final Double oldValue, final Double newValue) {
        final CacheRecord record = updateField(valueMap, oldValue, newValue);
        if (record != null) {
            record.value = newValue;
        }
        return record;
    }

    /**
     * Removes the record from this cache if present.
     * @param record record to remove.
     * @return {@code true} if this cache contained the specified record.
     */
    public boolean removeRecord(final CacheRecord record) {
        if (accountMap.containsKey(record.getAccount()) &&
                nameMap.containsKey(record.getName()) &&
                valueMap.containsKey(record.getValue())) {
            accountMap.remove(record.getAccount());
            nameMap.remove(record.getName());
            valueMap.remove(record.getValue());
            return true;
        }
        return false;
    }

    private <T> CacheRecord updateField(final Map<T, CacheRecord> map, final T oldField, final T newField) {
        final CacheRecord record = map.get(oldField);
        if (record != null && !map.containsKey(newField)) {
            map.remove(oldField);
            map.put(newField, record);
        }
        return record;
    }
}
