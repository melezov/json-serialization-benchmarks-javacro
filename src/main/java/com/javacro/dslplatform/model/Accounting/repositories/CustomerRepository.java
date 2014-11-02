package com.javacro.dslplatform.model.Accounting.repositories;

public class CustomerRepository extends
        com.dslplatform.client.ClientPersistableRepository<com.javacro.dslplatform.model.Accounting.Customer> {
    public CustomerRepository(final com.dslplatform.patterns.ServiceLocator locator) {
        super(com.javacro.dslplatform.model.Accounting.Customer.class, locator);
    }
}
