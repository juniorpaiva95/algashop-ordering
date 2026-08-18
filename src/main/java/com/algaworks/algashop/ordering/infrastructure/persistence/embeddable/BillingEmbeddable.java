package com.algaworks.algashop.ordering.infrastructure.persistence.embeddable;

import com.algaworks.algashop.ordering.domain.model.valueobject.Address;
import com.algaworks.algashop.ordering.domain.model.valueobject.FullName;
import com.algaworks.algashop.ordering.domain.model.valueobject.Phone;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class BillingEmbeddable {
    private String firstName;
    private String lastName;
    private String document;
    private String phone;
    //private String email;

    @Embedded
    private AddressEmbeddable address;
}
