package fu.hr.utils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JpaUtil {
    private static final String PERS_UNIT_NAME = "hrManagement";

    /*
        Create/get EM from factory - read persistence.xml filw
     */
    public static EntityManager getEntityManager(){
        EntityManagerFactory managerFactory = Persistence.createEntityManagerFactory(PERS_UNIT_NAME);

        return managerFactory.createEntityManager();
    }
}

