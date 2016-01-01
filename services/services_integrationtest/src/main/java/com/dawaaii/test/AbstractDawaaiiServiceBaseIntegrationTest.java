package com.dawaaii.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;

@ContextConfiguration({"classpath:applicationContext-services_integrationtest.xml"})
public abstract class AbstractDawaaiiServiceBaseIntegrationTest extends AbstractTransactionalJUnit4SpringContextTests {

    //	@PersistenceContext(type = javax.persistence.PersistenceContextType.EXTENDED,
//	        properties = @PersistenceProperty(name="org.hibernate.flushMode", value="COMMIT"))
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    protected EntityManagerFactory emf;

    @Autowired
    protected JpaTransactionManager transactionManager;

    protected void flush() {

        entityManager.flush();
    }

    protected void clear() {
        entityManager.clear();
    }

    protected void flushAndClear() {
        flush();
        clear();
    }
}
