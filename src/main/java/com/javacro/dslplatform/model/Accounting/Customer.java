package com.javacro.dslplatform.model.Accounting;

public class Customer implements java.io.Serializable,
        com.dslplatform.patterns.AggregateRoot {
    public Customer() {
        _serviceLocator = com.dslplatform.client.Bootstrap.getLocator();
        _domainProxy = _serviceLocator
                .resolve(com.dslplatform.client.DomainProxy.class);
        _crudProxy = _serviceLocator
                .resolve(com.dslplatform.client.CrudProxy.class);
        this.id = 0L;
        this.name = "";
        this.profile = new com.javacro.dslplatform.model.Accounting.Profile();
    }

    private transient final com.dslplatform.patterns.ServiceLocator _serviceLocator;
    private transient final com.dslplatform.client.DomainProxy _domainProxy;
    private transient final com.dslplatform.client.CrudProxy _crudProxy;

    private String URI;

    @com.fasterxml.jackson.annotation.JsonProperty("URI")
    public String getURI() {
        return this.URI;
    }

    @Override
    public int hashCode() {
        return URI != null ? URI.hashCode() : super.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;

        if (getClass() != obj.getClass()) return false;
        final Customer other = (Customer) obj;

        return URI != null && URI.equals(other.URI);
    }

    @Override
    public String toString() {
        return URI != null ? "Customer(" + URI + ')' : "new Customer("
                + super.hashCode() + ')';
    }

    private static final long serialVersionUID = 0x0097000a;

    public Customer(
            final long id,
            final String name,
            final com.javacro.dslplatform.model.Accounting.Profile profile,
            final java.util.List<com.javacro.dslplatform.model.Accounting.Account> accounts) {
        _serviceLocator = com.dslplatform.client.Bootstrap.getLocator();
        _domainProxy = _serviceLocator
                .resolve(com.dslplatform.client.DomainProxy.class);
        _crudProxy = _serviceLocator
                .resolve(com.dslplatform.client.CrudProxy.class);
        setId(id);
        setName(name);
        setProfile(profile);
        setAccounts(accounts);
    }

    @com.fasterxml.jackson.annotation.JsonCreator
    private Customer(
            @com.fasterxml.jackson.annotation.JacksonInject("_serviceLocator") final com.dslplatform.patterns.ServiceLocator _serviceLocator,
            @com.fasterxml.jackson.annotation.JsonProperty("URI") final String URI,
            @com.fasterxml.jackson.annotation.JsonProperty("id") final long id,
            @com.fasterxml.jackson.annotation.JsonProperty("name") final String name,
            @com.fasterxml.jackson.annotation.JsonProperty("profile") final com.javacro.dslplatform.model.Accounting.Profile profile,
            @com.fasterxml.jackson.annotation.JsonProperty("accounts") final java.util.List<com.javacro.dslplatform.model.Accounting.Account> accounts) {
        this._serviceLocator = _serviceLocator;
        this._domainProxy = _serviceLocator
                .resolve(com.dslplatform.client.DomainProxy.class);
        this._crudProxy = _serviceLocator
                .resolve(com.dslplatform.client.CrudProxy.class);
        this.URI = URI;
        this.id = id;
        this.name = name == null ? "" : name;
        this.profile = profile == null
                ? new com.javacro.dslplatform.model.Accounting.Profile()
                : profile;
        this.accounts = accounts;
    }

    public static Customer find(final String uri) throws java.io.IOException {
        return find(uri, com.dslplatform.client.Bootstrap.getLocator());
    }

    public static Customer find(
            final String uri,
            final com.dslplatform.patterns.ServiceLocator locator)
            throws java.io.IOException {
        try {
            return (locator != null
                    ? locator
                    : com.dslplatform.client.Bootstrap.getLocator())
                    .resolve(com.dslplatform.client.CrudProxy.class)
                    .read(Customer.class, uri).get();
        } catch (final InterruptedException e) {
            throw new java.io.IOException(e);
        } catch (final java.util.concurrent.ExecutionException e) {
            throw new java.io.IOException(e);
        }
    }

    public static java.util.List<Customer> find(final Iterable<String> uris)
            throws java.io.IOException {
        return find(uris, com.dslplatform.client.Bootstrap.getLocator());
    }

    public static java.util.List<Customer> find(
            final Iterable<String> uris,
            final com.dslplatform.patterns.ServiceLocator locator)
            throws java.io.IOException {
        try {
            return (locator != null
                    ? locator
                    : com.dslplatform.client.Bootstrap.getLocator())
                    .resolve(com.dslplatform.client.DomainProxy.class)
                    .find(Customer.class, uris).get();
        } catch (final InterruptedException e) {
            throw new java.io.IOException(e);
        } catch (final java.util.concurrent.ExecutionException e) {
            throw new java.io.IOException(e);
        }
    }

    public static java.util.List<Customer> findAll() throws java.io.IOException {
        return findAll(null, null,
                com.dslplatform.client.Bootstrap.getLocator());
    }

    public static java.util.List<Customer> findAll(
            final com.dslplatform.patterns.ServiceLocator locator)
            throws java.io.IOException {
        return findAll(null, null, locator);
    }

    public static java.util.List<Customer> findAll(
            final Integer limit,
            final Integer offset) throws java.io.IOException {
        return findAll(limit, offset,
                com.dslplatform.client.Bootstrap.getLocator());
    }

    public static java.util.List<Customer> findAll(
            final Integer limit,
            final Integer offset,
            final com.dslplatform.patterns.ServiceLocator locator)
            throws java.io.IOException {
        try {
            return (locator != null
                    ? locator
                    : com.dslplatform.client.Bootstrap.getLocator())
                    .resolve(com.dslplatform.client.DomainProxy.class)
                    .findAll(Customer.class, limit, offset, null).get();
        } catch (final InterruptedException e) {
            throw new java.io.IOException(e);
        } catch (final java.util.concurrent.ExecutionException e) {
            throw new java.io.IOException(e);
        }
    }

    public static java.util.List<Customer> search(
            final com.dslplatform.patterns.Specification<Customer> specification)
            throws java.io.IOException {
        return search(specification, null, null,
                com.dslplatform.client.Bootstrap.getLocator());
    }

    public static java.util.List<Customer> search(
            final com.dslplatform.patterns.Specification<Customer> specification,
            final com.dslplatform.patterns.ServiceLocator locator)
            throws java.io.IOException {
        return search(specification, null, null, locator);
    }

    public static java.util.List<Customer> search(
            final com.dslplatform.patterns.Specification<Customer> specification,
            final Integer limit,
            final Integer offset) throws java.io.IOException {
        return search(specification, limit, offset,
                com.dslplatform.client.Bootstrap.getLocator());
    }

    public static java.util.List<Customer> search(
            final com.dslplatform.patterns.Specification<Customer> specification,
            final Integer limit,
            final Integer offset,
            final com.dslplatform.patterns.ServiceLocator locator)
            throws java.io.IOException {
        try {
            return (locator != null
                    ? locator
                    : com.dslplatform.client.Bootstrap.getLocator())
                    .resolve(com.dslplatform.client.DomainProxy.class)
                    .search(specification, limit, offset, null).get();
        } catch (final InterruptedException e) {
            throw new java.io.IOException(e);
        } catch (final java.util.concurrent.ExecutionException e) {
            throw new java.io.IOException(e);
        }
    }

    public static long count() throws java.io.IOException {
        return count(com.dslplatform.client.Bootstrap.getLocator());
    }

    public static long count(
            final com.dslplatform.patterns.ServiceLocator locator)
            throws java.io.IOException {
        try {
            return (locator != null
                    ? locator
                    : com.dslplatform.client.Bootstrap.getLocator())
                    .resolve(com.dslplatform.client.DomainProxy.class)
                    .count(Customer.class).get().longValue();
        } catch (final InterruptedException e) {
            throw new java.io.IOException(e);
        } catch (final java.util.concurrent.ExecutionException e) {
            throw new java.io.IOException(e);
        }
    }

    public static long count(
            final com.dslplatform.patterns.Specification<Customer> specification)
            throws java.io.IOException {
        return count(specification,
                com.dslplatform.client.Bootstrap.getLocator());
    }

    public static long count(
            final com.dslplatform.patterns.Specification<Customer> specification,
            final com.dslplatform.patterns.ServiceLocator locator)
            throws java.io.IOException {
        try {
            return (locator != null
                    ? locator
                    : com.dslplatform.client.Bootstrap.getLocator())
                    .resolve(com.dslplatform.client.DomainProxy.class)
                    .count(specification).get().longValue();
        } catch (final InterruptedException e) {
            throw new java.io.IOException(e);
        } catch (final java.util.concurrent.ExecutionException e) {
            throw new java.io.IOException(e);
        }
    }

    private void updateWithAnother(final com.javacro.dslplatform.model.Accounting.Customer result) {
        this.URI = result.URI;

        this.id = result.id;
        this.name = result.name;
        this.profile = result.profile;
        this.accounts = result.accounts;
    }

    public Customer persist() throws java.io.IOException {
        final Customer result;
        try {
            result = this.URI == null
                    ? _crudProxy.create(this).get()
                    : _crudProxy.update(this).get();
        } catch (final InterruptedException e) {
            throw new java.io.IOException(e);
        } catch (final java.util.concurrent.ExecutionException e) {
            throw new java.io.IOException(e);
        }
        this.updateWithAnother(result);
        return this;
    }

    public Customer delete() throws java.io.IOException {
        try {
            return _crudProxy.delete(Customer.class, URI).get();
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
        if (value == null)
            throw new IllegalArgumentException(
                    "Property \"name\" cannot be null!");
        this.name = value;

        return this;
    }

    private com.javacro.dslplatform.model.Accounting.Profile profile;

    @com.fasterxml.jackson.annotation.JsonProperty("profile")
    public com.javacro.dslplatform.model.Accounting.Profile getProfile() {
        return profile;
    }

    public Customer setProfile(final com.javacro.dslplatform.model.Accounting.Profile value) {
        if (value == null)
            throw new IllegalArgumentException(
                    "Property \"profile\" cannot be null!");
        this.profile = value;

        return this;
    }

    private java.util.List<com.javacro.dslplatform.model.Accounting.Account> accounts;

    @com.fasterxml.jackson.annotation.JsonProperty("accounts")
    public java.util.List<com.javacro.dslplatform.model.Accounting.Account> getAccounts() {
        return accounts;
    }

    public Customer setAccounts(
            final java.util.List<com.javacro.dslplatform.model.Accounting.Account> value) {
        com.javacro.dslplatform.model.Guards.checkNulls(value);
        this.accounts = value;

        return this;
    }
}
