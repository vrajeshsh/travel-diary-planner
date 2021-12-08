/*
 * Created by Arpit Thool on 2021.10.15
 * Copyright © 2021 Arpit Thool. All rights reserved.
 */
package edu.vt.FacadeBeans;

import edu.vt.EntityBeans.TravelNote;
import edu.vt.EntityBeans.User;

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

    // Returns the object reference of the TravelNote object whose primary key is id
    public TravelNote getUserTravelNote(int id) {
        return entityManager.find(TravelNote.class, id);
    }

    // Returns a list of object references of TravelNote objects that belong to
    // the User object whose database Primary Key = primaryKey
    public List<TravelNote> findTravelNotesByUserPrimaryKey(User signedInUser) {
        /*
        The following statement obtains the results from the named database query.
         */
        return entityManager.createNamedQuery("TravelNote.findTravelNotesByUserDatabasePrimaryKey")
                .setParameter("userId", signedInUser)
                .getResultList();
    }

    /*
     ***************************
     *   Search Query Type 1   *
     ***************************
     */

    /*
    -----------------------------
    Search Category: Travel Note title
    -----------------------------
     */
    // Searches TravelDiaryPlannerDB where Travel Note title contains the searchString entered by the user.
    public List<TravelNote> titleQuery(String searchString, User signedInUserID) {
        // Place the % wildcard before and after the search string to search for it anywhere in the Travel Note name
        searchString = "%" + searchString + "%";
        // Conduct the search in a case-insensitive manner and return the results in a list.
        return getEntityManager().createQuery(
                        "SELECT t FROM TravelNote t WHERE ( t.title LIKE :searchString ) AND t.userId = :userId")
                .setParameter("userId", signedInUserID)
                .setParameter("searchString", searchString)
                .getResultList();
    }

    /*
    -----------------------------
    Search Category: Travel Note note
    -----------------------------
     */
    // Searches TravelDiaryPlannerDB where Travel Note text contains the searchString entered by the user.
    public List<TravelNote> noteQuery(String searchString, User signedInUserID) {
        // Place the % wildcard before and after the search string to search for it anywhere in the Travel Note name
        searchString = "%" + searchString + "%";
        // Conduct the search in a case-insensitive manner and return the results in a list.
        return getEntityManager().createQuery(
                        "SELECT t FROM TravelNote t WHERE ( t.text LIKE :searchString ) AND t.userId = :userId")
                .setParameter("userId", signedInUserID)
                .setParameter("searchString", searchString)
                .getResultList();
    }

    /*
    -----------------------------
    Search Category: Travel Note All
    -----------------------------
     */
    // Searches TravelDiaryPlannerDB where Travel Note title or text contains the searchString entered by the user.
    public List<TravelNote> allQuery(String searchString, User signedInUserID) {
        // Place the % wildcard before and after the search string to search for it anywhere in the Travel Note name
        searchString = "%" + searchString + "%";
        // Conduct the search in a case-insensitive manner and return the results in a list.
        return getEntityManager().createQuery(
                        "SELECT t FROM TravelNote t WHERE ( t.title LIKE :searchString OR t.text LIKE :searchString ) AND t.userId = :userId")
                .setParameter("searchString", searchString)
                .setParameter("userId", signedInUserID)
                .getResultList();
    }

    /*
    -----------------------------
    Search Category: Travel Note Date
    -----------------------------
     */
    // Searches TravelDiaryPlannerDB where Travel Note title or text contains the searchString entered by the user.
    public List<TravelNote> type2SearchQuery(String searchString, User signedInUser) {
        // Conduct the search in a case-insensitive manner and return the results in a list.
        return getEntityManager().createQuery(
                        "SELECT t FROM TravelNote t WHERE ( t.dateCreated >= '"+searchString+"' ) AND t.userId = "+signedInUser.getId())
                .getResultList();
    }

    /*
    -----------------------------
    Search Category: Travel Note Date
    -----------------------------
     */
    // Searches TravelDiaryPlannerDB where Travel Note title or text contains the searchString entered by the user.
    public List<TravelNote> type3SearchQuery(String searchString, User signedInUser) {
        // Conduct the search in a case-insensitive manner and return the results in a list.
        return getEntityManager().createQuery(
                        "SELECT t FROM TravelNote t WHERE ( t.dateCreated < '"+searchString+"' ) AND t.userId = "+signedInUser.getId())
                .getResultList();
    }

    /*
    -----------------------------
    Search Category: Travel Note Date
    -----------------------------
     */
    // Searches TravelDiaryPlannerDB where Travel Note title or text contains the searchString entered by the user.
    public List<TravelNote> type4SearchQuery(String searchString, String searchString2, User signedInUser) {
        // Conduct the search in a case-insensitive manner and return the results in a list.
        return getEntityManager().createQuery(
                        "SELECT t FROM TravelNote t WHERE ( t.dateCreated BETWEEN '"+searchString+"' AND '"+searchString2+"' ) AND t.userId = "+signedInUser.getId())
                .getResultList();
    }
}
