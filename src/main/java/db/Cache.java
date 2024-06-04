package db;

import java.util.Map;
import java.util.TreeMap;

public class Cache {
    private final Map<Long, CacheRecord> accountMap = new TreeMap<>();
    private final Map<String, CacheRecord> nameMap = new TreeMap<>();
    private final Map<Double, CacheRecord> valueMap = new TreeMap<>();

    public Cache() {
    }

    public boolean addRecord(final CacheRecord record) {
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

    public CacheRecord getRecordByAccount(final Long account) {
        return accountMap.get(account);
    }

    public CacheRecord getRecordByName(final String name) {
        return nameMap.get(name);
    }

    public CacheRecord getRecordByValue(final Double value) {
        return valueMap.get(value);
    }

    public CacheRecord updateAccount(final Long oldAccount, final Long newAccount) {
        return updateField(accountMap, oldAccount, newAccount);
    }

    public CacheRecord updateName(final String oldName, final String newName) {
        return updateField(nameMap, oldName, newName);
    }

    public CacheRecord updateValue(final Double oldValue, final Double newValue) {
        return updateField(valueMap, oldValue, newValue);
    }

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
