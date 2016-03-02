package edu.avans.hartigehap.service;

import edu.avans.hartigehap.domain.Owner;

import java.util.List;

/**
 * Created by Rowdy on 16-2-2016.
 */
public interface OwnerService {
    List<Owner> findAll();
    Owner findById(Long id);
    List<Owner> findByName(String name);
    Owner save(Owner owner);
    void delete(Owner owner);
}
