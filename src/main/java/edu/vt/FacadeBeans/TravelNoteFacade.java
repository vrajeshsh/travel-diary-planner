package edu.vt.FacadeBeans;

import edu.vt.EntityBeans.TravelNote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

// @Stateless annotation implies that the conversational state with the client shall NOT be maintained.
@Stateless
public class TravelNoteFacade extends AbstractFacade<TravelNote> {
    /*
   ---------------------------------------------------------------------------------------------
   The EntityManager is an API that enables database CRUD (Create Read Update Delete) operations
   and complex database searches. An EntityManager instance is created to manage entities
   that are defined by a persistence unit. The @PersistenceContext annotation below associates
   the entityManager instance with the persistence unitName identified below.
   ---------------------------------------------------------------------------------------------
    */
    @PersistenceContext(unitName = "Travel-Diary-PlannerPU")
    private EntityManager entityManager;

    // Obtain the object reference of the EntityManager instance in charge of
    // managing the entities in the persistence context identified above.
    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    /*
    This constructor method invokes its parent AbstractFacade's constructor method,
    which in turn initializes its entity class type T and entityClass instance variable.
     */
    public TravelNoteFacade() {
        super(TravelNote.class);
    }

    /*
     *********************
     *   Other Methods   *
     *********************
     */

    // Returns the object reference of the UserFile object whose primary key is id
    public TravelNote getUserTravelNote(int id) {
        return entityManager.find(TravelNote.class, id);
    }

    // Returns a list of object references of UserFile objects that belong to
    // the User object whose database Primary Key = primaryKey
    public List<TravelNote> findTravelNotesByUserPrimaryKey(Integer primaryKey) {
        /*
        The following @NamedQuery definition is given in UserFile entity class file:
        @NamedQuery(name = "UserFile.findUserFilesByUserId", query = "SELECT u FROM UserFile u WHERE u.userId.id = :userId")

        The following statement obtains the results from the named database query.
         */
        return entityManager.createNamedQuery("TravelNote.findTravelNotesByUserDatabasePrimaryKey")
                .setParameter("userId", primaryKey)
                .getResultList();
    }
}
