package com.algaworks.algashop.ordering.infrastructure.persistence.disassembler;

import com.algaworks.algashop.ordering.domain.model.entity.Customer;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.CustomerPersistenceEntity;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.CustomerPersistenceEntityTestDataBuilder;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerPersistenceEntityDisassemblerTest {

    private final CustomerPersistenceEntityDisassembler disassembler = new CustomerPersistenceEntityDisassembler();

    @Test
    void shouldConvertFromPersistence() {
        CustomerPersistenceEntity persistenceEntity = CustomerPersistenceEntityTestDataBuilder.aCustomer().build();
        Customer customer = disassembler.toDomainEntity(persistenceEntity);

        assertThat(customer).satisfies(
                s -> assertThat(s.id().value()).isEqualTo(persistenceEntity.getId()),
                s -> assertThat(s.fullName().firstName()).isEqualTo(persistenceEntity.getFirstName()),
                s -> assertThat(s.fullName().lastName()).isEqualTo(persistenceEntity.getLastName()),
                s -> assertThat(s.birthDate().value()).isEqualTo(persistenceEntity.getBirthDate()),
                s -> assertThat(s.email().value()).isEqualTo(persistenceEntity.getEmail()),
                s -> assertThat(s.phone().value()).isEqualTo(persistenceEntity.getPhone()),
                s -> assertThat(s.document().value()).isEqualTo(persistenceEntity.getDocument()),
                s -> assertThat(s.isPromotionNotificationsAllowed())
                        .isEqualTo(persistenceEntity.getPromotionNotificationsAllowed()),
                s -> assertThat(s.isArchived()).isEqualTo(persistenceEntity.getArchived()),
                s -> assertThat(s.registeredAt()).isEqualTo(persistenceEntity.getRegisteredAt()),
                s -> assertThat(s.archivedAt()).isEqualTo(persistenceEntity.getArchivedAt()),
                s -> assertThat(s.loyaltyPoints().value()).isEqualTo(persistenceEntity.getLoyaltyPoints()),
                s -> assertThat(s.address().zipCode().value()).isEqualTo(persistenceEntity.getAddress().getZipCode())
        );
    }

    @Test
    void givenNullBirthDate_shouldConvertWithoutBirthDate() {
        CustomerPersistenceEntity persistenceEntity = CustomerPersistenceEntityTestDataBuilder.aCustomer()
                .birthDate(null)
                .build();

        Customer customer = disassembler.toDomainEntity(persistenceEntity);

        assertThat(customer.birthDate()).isNull();
    }

}
