package com.tnpfc.protean.API.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

@Repository
public class SequenceDao {
	
	 @PersistenceContext
	    private EntityManager entityManager;

	    public Long getNextVal() {
	        return ((Number) entityManager
	                .createNativeQuery("SELECT PROTEAN_SEQ.NEXTVAL FROM DUAL")
	                .getSingleResult()).longValue();
	    }

}
