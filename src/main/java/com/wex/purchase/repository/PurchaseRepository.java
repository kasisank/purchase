package com.wex.purchase.repository;

/*
 * Purchase Repository class for CRUD operations
 */

import com.wex.purchase.entity.Purchase;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseRepository extends CrudRepository<Purchase, Long> {
}
