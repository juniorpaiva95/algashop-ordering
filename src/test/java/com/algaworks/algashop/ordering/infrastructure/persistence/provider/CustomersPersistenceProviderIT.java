package com.algaworks.algashop.ordering.infrastructure.persistence.provider;

import com.algaworks.algashop.ordering.domain.model.entity.Customer;
import com.algaworks.algashop.ordering.domain.model.entity.CustomerTestDataBuilder;
import com.algaworks.algashop.ordering.domain.model.valueobject.LoyaltyPoints;
import com.algaworks.algashop.ordering.domain.model.valueobject.Phone;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.infrastructure.persistence.assembler.CustomerPersistenceEntityAssembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.config.SpringDataAuditingConfig;
import com.algaworks.algashop.ordering.infrastructure.persistence.disassembler.CustomerPersistenceEntityDisassembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.repository.CustomerPersistenceEntityRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@DataJpaTest
@Import({
        CustomersPersistenceProvider.class,
        CustomerPersistenceEntityAssembler.class,
        CustomerPersistenceEntityDisassembler.class,
        SpringDataAuditingConfig.class
})
class CustomersPersistenceProviderIT {

    private CustomersPersistenceProvider persistenceProvider;
    private CustomerPersistenceEntityRepository entityRepository;

    @Autowired
    public CustomersPersistenceProviderIT(CustomersPersistenceProvider persistenceProvider,
                                          CustomerPersistenceEntityRepository entityRepository) {
        this.persistenceProvider = persistenceProvider;
        this.entityRepository = entityRepository;
    }

    @Test
    public void shouldUpdateAndKeepPersistenceEntityState() {
        Customer customer = CustomerTestDataBuilder.existingCustomer().build();
        UUID customerId = customer.id().value();
        persistenceProvider.add(customer);

        var persistenceEntity = entityRepository.findById(customerId).orElseThrow();

        Assertions.assertThat(persistenceEntity.getLoyaltyPoints()).isZero();

        Assertions.assertThat(persistenceEntity.getCreatedByUserId()).isNotNull();
        Assertions.assertThat(persistenceEntity.getLastModifiedAt()).isNotNull();
        Assertions.assertThat(persistenceEntity.getLastModifiedByUserId()).isNotNull();

        customer = persistenceProvider.ofId(customer.id()).orElseThrow();
        customer.addLoyaltyPoints(new LoyaltyPoints(50));
        persistenceProvider.add(customer);

        persistenceEntity = entityRepository.findById(customerId).orElseThrow();

        Assertions.assertThat(persistenceEntity.getLoyaltyPoints()).isEqualTo(50);

        Assertions.assertThat(persistenceEntity.getCreatedByUserId()).isNotNull();
        Assertions.assertThat(persistenceEntity.getLastModifiedAt()).isNotNull();
        Assertions.assertThat(persistenceEntity.getLastModifiedByUserId()).isNotNull();
    }

    @Test
    public void shouldUpdateDomainVersionAfterPersist() {
        Customer customer = CustomerTestDataBuilder.existingCustomer().build();

        persistenceProvider.add(customer);
        Assertions.assertThat(customer.version()).isZero();

        customer = persistenceProvider.ofId(customer.id()).orElseThrow();
        customer.changePhone(new Phone("111-111-1111"));
        persistenceProvider.add(customer);

        Assertions.assertThat(customer.version()).isEqualTo(1L);
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void shouldAddFindAndNotFailWhenNoTransaction() {
        // sem transacao o add commita de verdade, entao usa um id proprio e limpa no final
        // para nao colidir com o DEFAULT_CUSTOMER_ID usado pelos demais testes
        Customer customer = CustomerTestDataBuilder.existingCustomer().id(new CustomerId()).build();
        persistenceProvider.add(customer);

        Assertions.assertThatNoException().isThrownBy(
                ()-> persistenceProvider.ofId(customer.id()).orElseThrow()
        );

        entityRepository.deleteById(customer.id().value());
    }
}
