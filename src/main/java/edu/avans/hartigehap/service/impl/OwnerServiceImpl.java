package edu.avans.hartigehap.service.impl;

import com.google.common.collect.Lists;
import edu.avans.hartigehap.domain.Owner;
import edu.avans.hartigehap.domain.Restaurant;
import edu.avans.hartigehap.repository.OwnerRepository;
import edu.avans.hartigehap.service.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service("ownerService")
@Repository
@Transactional

public class OwnerServiceImpl implements OwnerService {
    @Autowired private OwnerRepository ownerRepository;

    @Transactional
    public List<Owner> findAll() {
       return Lists.newArrayList(ownerRepository.findAll());
    }

    @Transactional
    public Owner findById(Long id) {
        return ownerRepository.findOne(id);
    }

    @Transactional
    public List<Owner> findByName(String name) {
        return Lists.newArrayList(ownerRepository.findByName(name));
    }

    @Transactional
    public Owner save(Owner owner) {
        return ownerRepository.save(owner);
    }

    @Transactional
    public void delete(Owner owner) {
        ownerRepository.delete(owner);
    }


}
