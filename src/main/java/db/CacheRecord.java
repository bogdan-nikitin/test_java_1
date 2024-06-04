package db;

public class CacheRecord {
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
