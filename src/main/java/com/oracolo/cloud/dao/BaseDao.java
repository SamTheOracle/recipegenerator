package com.oracolo.cloud.dao;

import com.oracolo.cloud.entities.Metadata;
import com.oracolo.cloud.entities.MetadataEntity;
import com.oracolo.cloud.entities.Recipe;
import liquibase.pro.packaged.M;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.util.Collection;
import java.util.Optional;

@ApplicationScoped
public class BaseDao<T> {

    @Inject
    EntityManager entityManager;

    public void insert(T entity){
        if(entity instanceof MetadataEntity){
            Metadata metadata = new Metadata();
            metadata.setInsertDate(Instant.now());
            ((MetadataEntity) entity).setMetadata(metadata);
        }
        entityManager.persist(entity);
    }

    public void update(T entity){
        if(entity instanceof MetadataEntity){
            Metadata metadata = ((MetadataEntity) entity).getMetadata();
            metadata.setUpdateDate(Instant.now());
        }
        entityManager.merge(entity);
    }

    public void delete(T entity){
        entityManager.remove(entity);
    }

    public void insert(Collection<T> entities){
        entities.forEach(this::insert);
    }
    public void delete(Collection<T> entities){
        entities.forEach(this::delete);
    }
    public void update(Collection<T> entities){
        entities.forEach(this::update);
    }

    public Optional<T> getById(Object id, Class<T> entityClass) {
        try{
            return Optional.of(entityManager.find(entityClass,id));
        }catch (Exception e){
            return Optional.empty();
        }
    }
}
