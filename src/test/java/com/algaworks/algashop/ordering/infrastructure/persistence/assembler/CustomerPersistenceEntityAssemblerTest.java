package com.algaworks.algashop.ordering.infrastructure.persistence.assembler;

import com.algaworks.algashop.ordering.domain.model.entity.Customer;
import com.algaworks.algashop.ordering.domain.model.entity.CustomerTestDataBuilder;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.CustomerPersistenceEntity;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.CustomerPersistenceEntityTestDataBuilder;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerPersistenceEntityAssemblerTest {

    private final CustomerPersistenceEntityAssembler assembler = new CustomerPersistenceEntityAssembler();

    @Test
    void shouldConvertFromDomain() {
        Customer customer = CustomerTestDataBuilder.existingCustomer().build();
        CustomerPersistenceEntity persistenceEntity = assembler.fromDomain(customer);

        assertThat(persistenceEntity).satisfies(
                s -> assertThat(s.getId()).isEqualTo(customer.id().value()),
                s -> assertThat(s.getFirstName()).isEqualTo(customer.fullName().firstName()),
                s -> assertThat(s.getLastName()).isEqualTo(customer.fullName().lastName()),
                s -> assertThat(s.getBirthDate()).isEqualTo(customer.birthDate().value()),
                s -> assertThat(s.getEmail()).isEqualTo(customer.email().value()),
                s -> assertThat(s.getPhone()).isEqualTo(customer.phone().value()),
                s -> assertThat(s.getDocument()).isEqualTo(customer.document().value()),
                s -> assertThat(s.getPromotionNotificationsAllowed())
                        .isEqualTo(customer.isPromotionNotificationsAllowed()),
                s -> assertThat(s.getArchived()).isEqualTo(customer.isArchived()),
                s -> assertThat(s.getRegisteredAt()).isEqualTo(customer.registeredAt()),
                s -> assertThat(s.getArchivedAt()).isEqualTo(customer.archivedAt()),
                s -> assertThat(s.getLoyaltyPoints()).isEqualTo(customer.loyaltyPoints().value()),
                s -> assertThat(s.getAddress().getZipCode()).isEqualTo(customer.address().zipCode().value())
        );
    }

    @Test
    void givenAnonymizedCustomer_shouldConvertNullBirthDate() {
        Customer customer = CustomerTestDataBuilder.existingAnonymizedCustomer().build();

        assertThat(customer.birthDate()).isNull();

        CustomerPersistenceEntity persistenceEntity = assembler.fromDomain(customer);

        assertThat(persistenceEntity.getBirthDate()).isNull();
        assertThat(persistenceEntity.getArchived()).isTrue();
    }

    @Test
    void shouldMergeIntoExistingPersistenceEntity() {
        Customer customer = CustomerTestDataBuilder.existingCustomer().build();
        CustomerPersistenceEntity persistenceEntity = CustomerPersistenceEntityTestDataBuilder.aCustomer().build();

        assembler.merge(persistenceEntity, customer);

        assertThat(persistenceEntity.getId()).isEqualTo(customer.id().value());
        assertThat(persistenceEntity.getEmail()).isEqualTo(customer.email().value());
        assertThat(persistenceEntity.getLoyaltyPoints()).isEqualTo(customer.loyaltyPoints().value());
    }

}
