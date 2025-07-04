package com.chiragbhisikar.shopease.Repository;


import com.chiragbhisikar.shopease.Model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AddressRepository extends JpaRepository<Address, UUID> {
    Optional<Address> findById(UUID id);

    Optional<Address> findAddressByIdAndUserId(UUID id, UUID userId);

}