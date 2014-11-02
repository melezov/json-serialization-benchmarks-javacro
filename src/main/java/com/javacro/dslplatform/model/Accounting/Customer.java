package com.javacro.dslplatform.model.Accounting;

public class Customer implements java.io.Serializable, com.dslplatform.patterns.AggregateRoot {
    public Customer() {
        URI = java.util.UUID.randomUUID().toString();
        this.id = 0L;
        this.name = "";
        this.profile = new com.javacro.dslplatform.model.Accounting.Profile();
    }

    private transient com.dslplatform.patterns.ServiceLocator _serviceLocator;

    private String URI;

    @com.fasterxml.jackson.annotation.JsonProperty("URI")
    public String getURI() {
        return this.URI;
    }

    @Override
    public int hashCode() {
        return URI.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;

        if (getClass() != obj.getClass()) return false;
        final Customer other = (Customer) obj;

        return URI.equals(other.URI);
    }

    @Override
    public String toString() {
        return "Customer(" + URI + ')';
    }

    @com.fasterxml.jackson.annotation.JsonCreator
    public Customer(
            @com.fasterxml.jackson.annotation.JacksonInject("_serviceLocator") final com.dslplatform.patterns.ServiceLocator _serviceLocator,
            @com.fasterxml.jackson.annotation.JsonProperty("URI") final String URI,
            @com.fasterxml.jackson.annotation.JsonProperty("id") final long id,
            @com.fasterxml.jackson.annotation.JsonProperty("name") final String name,
            @com.fasterxml.jackson.annotation.JsonProperty("profile") final com.javacro.dslplatform.model.Accounting.Profile profile,
            @com.fasterxml.jackson.annotation.JsonProperty("accounts") final java.util.List<com.javacro.dslplatform.model.Accounting.Account> accounts) {
        this._serviceLocator = _serviceLocator;
        this.URI = URI != null ? URI : new java.util.UUID(0L, 0L).toString();
        this.id = id;
        this.name = name == null ? "" : name;
        this.profile = profile == null ? new com.javacro.dslplatform.model.Accounting.Profile() : profile;
        this.accounts = accounts;
    }

    private static final long serialVersionUID = 0x0097000a;

    public boolean isNewAggregate() {
        return _serviceLocator == null;
    }

    public static Customer find(final String uri) throws java.io.IOException {
        return find(uri, com.dslplatform.client.Bootstrap.getLocator());
    }

    public static Customer find(final String uri, final com.dslplatform.patterns.ServiceLocator locator)
            throws java.io.IOException {
        try {
            return (locator != null ? locator : com.dslplatform.client.Bootstrap.getLocator())
                    .resolve(com.dslplatform.client.CrudProxy.class).read(Customer.class, uri).get();
        } catch (final InterruptedException e) {
            throw new java.io.IOException(e);
        } catch (final java.util.concurrent.ExecutionException e) {
            throw new java.io.IOException(e);
        }
    }

    public static java.util.List<Customer> find(final Iterable<String> uris) throws java.io.IOException {
        return find(uris, com.dslplatform.client.Bootstrap.getLocator());
    }

    public static java.util.List<Customer> find(
            final Iterable<String> uris,
            final com.dslplatform.patterns.ServiceLocator locator) throws java.io.IOException {
        try {
            return (locator != null ? locator : com.dslplatform.client.Bootstrap.getLocator())
                    .resolve(com.dslplatform.client.DomainProxy.class).find(Customer.class, uris).get();
        } catch (final InterruptedException e) {
            throw new java.io.IOException(e);
        } catch (final java.util.concurrent.ExecutionException e) {
            throw new java.io.IOException(e);
        }
    }

    public static java.util.List<Customer> search() throws java.io.IOException {
        return search(null, null, com.dslplatform.client.Bootstrap.getLocator());
    }

    public static java.util.List<Customer> search(final com.dslplatform.patterns.ServiceLocator locator)
            throws java.io.IOException {
        return search(null, null, locator);
    }

    public static java.util.List<Customer> search(final Integer limit, final Integer offset) throws java.io.IOException {
        return search(limit, offset, com.dslplatform.client.Bootstrap.getLocator());
    }

    public static java.util.List<Customer> search(
            final Integer limit,
            final Integer offset,
            final com.dslplatform.patterns.ServiceLocator locator) throws java.io.IOException {
        try {
            return (locator != null ? locator : com.dslplatform.client.Bootstrap.getLocator())
                    .resolve(com.dslplatform.client.DomainProxy.class).search(Customer.class, limit, offset, null)
                    .get();
        } catch (final InterruptedException e) {
            throw new java.io.IOException(e);
        } catch (final java.util.concurrent.ExecutionException e) {
            throw new java.io.IOException(e);
        }
    }

    public static java.util.List<Customer> search(final com.dslplatform.patterns.Specification<Customer> specification)
            throws java.io.IOException {
        return search(specification, null, null, com.dslplatform.client.Bootstrap.getLocator());
    }

    public static java.util.List<Customer> search(
            final com.dslplatform.patterns.Specification<Customer> specification,
            final com.dslplatform.patterns.ServiceLocator locator) throws java.io.IOException {
        return search(specification, null, null, locator);
    }

    public static java.util.List<Customer> search(
            final com.dslplatform.patterns.Specification<Customer> specification,
            final Integer limit,
            final Integer offset) throws java.io.IOException {
        return search(specification, limit, offset, com.dslplatform.client.Bootstrap.getLocator());
    }

    public static java.util.List<Customer> search(
            final com.dslplatform.patterns.Specification<Customer> specification,
            final Integer limit,
            final Integer offset,
            final com.dslplatform.patterns.ServiceLocator locator) throws java.io.IOException {
        try {
            return (locator != null ? locator : com.dslplatform.client.Bootstrap.getLocator())
                    .resolve(com.dslplatform.client.DomainProxy.class).search(specification, limit, offset, null).get();
        } catch (final InterruptedException e) {
            throw new java.io.IOException(e);
        } catch (final java.util.concurrent.ExecutionException e) {
            throw new java.io.IOException(e);
        }
    }

    public static long count() throws java.io.IOException {
        return count(com.dslplatform.client.Bootstrap.getLocator());
    }

    public static long count(final com.dslplatform.patterns.ServiceLocator locator) throws java.io.IOException {
        try {
            return (locator != null ? locator : com.dslplatform.client.Bootstrap.getLocator())
                    .resolve(com.dslplatform.client.DomainProxy.class).count(Customer.class).get().longValue();
        } catch (final InterruptedException e) {
            throw new java.io.IOException(e);
        } catch (final java.util.concurrent.ExecutionException e) {
            throw new java.io.IOException(e);
        }
    }

    public static long count(final com.dslplatform.patterns.Specification<Customer> specification)
            throws java.io.IOException {
        return count(specification, com.dslplatform.client.Bootstrap.getLocator());
    }

    public static long count(
            final com.dslplatform.patterns.Specification<Customer> specification,
            final com.dslplatform.patterns.ServiceLocator locator) throws java.io.IOException {
        try {
            return (locator != null ? locator : com.dslplatform.client.Bootstrap.getLocator())
                    .resolve(com.dslplatform.client.DomainProxy.class).count(specification).get().longValue();
        } catch (final InterruptedException e) {
            throw new java.io.IOException(e);
        } catch (final java.util.concurrent.ExecutionException e) {
            throw new java.io.IOException(e);
        }
    }

    private long id;

    @com.fasterxml.jackson.annotation.JsonProperty("id")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
    public long getId() {
        return id;
    }

    public Customer setId(final long value) {
        this.id = value;

        return this;
    }

    private String name;

    @com.fasterxml.jackson.annotation.JsonProperty("name")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
    public String getName() {
        return name;
    }

    public Customer setName(final String value) {
        if (value == null) throw new IllegalArgumentException("Property \"name\" cannot be null!");
        this.name = value;

        return this;
    }

    private com.javacro.dslplatform.model.Accounting.Profile profile;

    @com.fasterxml.jackson.annotation.JsonProperty("profile")
    public com.javacro.dslplatform.model.Accounting.Profile getProfile() {
        return profile;
    }

    public Customer setProfile(final com.javacro.dslplatform.model.Accounting.Profile value) {
        if (value == null) throw new IllegalArgumentException("Property \"profile\" cannot be null!");
        this.profile = value;

        return this;
    }

    private java.util.List<com.javacro.dslplatform.model.Accounting.Account> accounts;

    @com.fasterxml.jackson.annotation.JsonProperty("accounts")
    public java.util.List<com.javacro.dslplatform.model.Accounting.Account> getAccounts() {
        return accounts;
    }

    public Customer setAccounts(final java.util.List<com.javacro.dslplatform.model.Accounting.Account> value) {
        com.javacro.dslplatform.model.Guards.checkNulls(value);
        this.accounts = value;

        return this;
    }

    public Customer(
            final long id,
            final String name,
            final com.javacro.dslplatform.model.Accounting.Profile profile,
            final java.util.List<com.javacro.dslplatform.model.Accounting.Account> accounts) {
        setId(id);
        setName(name);
        setProfile(profile);
        setAccounts(accounts);
    }
}
