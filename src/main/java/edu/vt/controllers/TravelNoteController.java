/*
 * Created by Arpit Thool on 2021.10.23
 * Copyright © 2021 Arpit Thool. All rights reserved.
 */
package edu.vt.controllers;

import edu.vt.EntityBeans.TravelNote;
import edu.vt.EntityBeans.User;
import edu.vt.EntityBeans.UserFile;
import edu.vt.FacadeBeans.TravelNoteFacade;
import edu.vt.controllers.util.JsfUtil;
import edu.vt.controllers.util.JsfUtil.PersistAction;
import edu.vt.FacadeBeans.UserFileFacade;
import edu.vt.globals.Constants;
import edu.vt.globals.Methods;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.el.MethodExpression;
import javax.faces.event.ActionListener;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.persistence.criteria.CriteriaBuilder;

@Named("travelNoteController")
@SessionScoped
public class TravelNoteController implements Serializable {
    /*
    ===============================
    Instance Variables (Properties)
    ===============================

    /*
    The @EJB annotation directs the EJB Container Manager to inject (store) the object reference of the
    UserFileFacade bean into the instance variab
    le 'userFileFacade' after it is instantiated at runtime.
     */
    @EJB
    private TravelNoteFacade travelNoteFacade;

    @EJB
    private UserFileFacade userFileFacade;

    private TravelNote selected;

    private List<TravelNote> listOfTravelNotes = null;

    private List<UserFile> listOfTravelNoteFiles = null;

    private String searchString = null;
    private String searchString2 = null;

    private String searchField = null;

    private Date searchDate;

    private Integer searchType;

    private List<TravelNote> searchItems = null;

    //  indicating if travel note data changed or not
    private Boolean travelNoteDataChanged;
    /*
    =========================
    Getter and Setter Methods
    =========================
     */

    public String getSearchString2() {
        return searchString2;
    }

    public void setSearchString2(String searchString2) {
        this.searchString2 = searchString2;
    }

    public Date getSearchDate() {
        return searchDate;
    }

    public void setSearchDate(Date searchDate) {
        this.searchDate = searchDate;
    }

    public Integer getSearchType() {
        return searchType;
    }

    public void setSearchType(Integer searchType) {
        this.searchType = searchType;
    }

    public void setSearchItems(List<TravelNote> searchItems) {
        this.searchItems = searchItems;
    }

    public String getSearchField() {
        return searchField;
    }

    public void setSearchField(String searchField) {
        this.searchField = searchField;
    }

    public String getSearchString() {
        return searchString;
    }

    public TravelNoteFacade getTravelNoteFacade() {
        return travelNoteFacade;
    }

    public void setTravelNoteFacade(TravelNoteFacade travelNoteFacade) {
        this.travelNoteFacade = travelNoteFacade;
    }

    public UserFileFacade getUserFileFacade() {
        return userFileFacade;
    }

    public void setUserFileFacade(UserFileFacade userFileFacade) {
        this.userFileFacade = userFileFacade;
    }

    public void setListOfTravelNoteFiles(List<UserFile> listOfTravelNoteFiles) {
        this.listOfTravelNoteFiles = listOfTravelNoteFiles;
    }

    public Boolean getTravelNoteDataChanged() {
        return travelNoteDataChanged;
    }

    public void setTravelNoteDataChanged(Boolean travelNoteDataChanged) {
        this.travelNoteDataChanged = travelNoteDataChanged;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public TravelNote getSelected() {
        return selected;
    }

    public void setSelected(TravelNote selected) {
        this.selected = selected;
    }

    public List<TravelNote> getListOfTravelNotes() {
        if (listOfTravelNotes == null) {
            /*
            'user', the object reference of the signed-in user, was put into the SessionMap
            in the initializeSessionMap() method in LoginManager upon user's sign in.
             */
            Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
            User signedInUser = (User) sessionMap.get("user");

            // Obtain only those files from the database that belong to the signed-in user
            listOfTravelNotes = travelNoteFacade.findTravelNotesByUserPrimaryKey(signedInUser);

        }
        return listOfTravelNotes;
    }

    public void setListOfTravelNotes(List<TravelNote> listOfTravelNotes) {
        this.listOfTravelNotes = listOfTravelNotes;
    }

    /*
     *************************************
     *   Cancel and Display ListOfFavorites.xhtml   *
     *************************************
     */
    public String cancel() {
        // Unselect previously selected Public Video object if any
        selected = null;
        return "/travelNote/ListUserTravelNotes?faces-redirect=true";
    }

    /*
     ================
     Instance Methods
     ================
     */
    /*
     **************************************
     *   Unselect Selected Public Video Object   *
     **************************************
     */
    public void unselect() {
        selected = null;
    }

    /*
     **********************************************************************************************
     *   Perform CREATE, UPDATE (EDIT), and DELETE (DESTROY, REMOVE) Operations in the Database   *
     **********************************************************************************************
     */
    /**
     * @param persistAction refers to CREATE, UPDATE (Edit) or DELETE action
     * @param successMessage displayed to inform the user about the result
     */
    private void persist(JsfUtil.PersistAction persistAction, String successMessage) {
        if (selected != null) {
            Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
            User signedInUser = (User) sessionMap.get("user");
            selected.setUserId(signedInUser);
            try {
                if (persistAction != JsfUtil.PersistAction.DELETE) {
                    /*
                     -------------------------------------------------
                     Perform CREATE or EDIT operation in the database.
                     -------------------------------------------------
                     The edit(selected) method performs the SAVE (STORE) operation of the "selected"
                     object in the database regardless of whether the object is a newly
                     created object (CREATE) or an edited (updated) object (EDIT or UPDATE).

                     */
                    travelNoteFacade.edit(selected);
                } else {
                    /*
                     -----------------------------------------
                     Perform DELETE operation in the database.
                     -----------------------------------------
                     The remove(selected) method performs the DELETE operation of the "selected"
                     object in the database.

                     */
                    travelNoteFacade.remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, "A persistence error occurred!");
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, "A persistence error occurred");
            }
        }
    }
    /*
     ********************************************
     *   CREATE a New Public Video in the Database   *
     ********************************************
     */
    public void create() {
        Methods.preserveMessages();

        /*
        An enum is a special Java type used to define a group of constants.
        The constants CREATE, DELETE and UPDATE are defined as follows in JsfUtil.java

                public enum PersistAction {
                    CREATE,
                    DELETE,
                    UPDATE
                }
         */

        /*
         The object reference of the Public Video to be created is stored in the instance variable 'selected'
         See the persist method below.
         */
        persist(JsfUtil.PersistAction.CREATE, "Travel Note was successfully created.");

        if (!JsfUtil.isValidationFailed()) {
            // No JSF validation error. The CREATE operation is successfully performed.
            selected = null;            // Remove selection
            listOfTravelNotes = null;
            travelNoteDataChanged = true;
        }
    }

    /*
     ***********************************************
     *   UPDATE Selected Public Video in the Database   *
     ***********************************************
     */
    public void update() {
        Methods.preserveMessages();
        /*
         */
        persist(JsfUtil.PersistAction.UPDATE, "Travel Note was successfully updated.");

        if (!JsfUtil.isValidationFailed()) {
            // No JSF validation error. The UPDATE operation is successfully performed.
            selected = null;        // Remove selection
            listOfTravelNotes = null;
            travelNoteDataChanged = true;
        }
    }

    /*
     *************************************************
     *   DELETE Selected Public Video from the Database   *
     *************************************************
     */
    public void destroy() {
        this.deleteAllTravelNoteFiles();
        Methods.preserveMessages();
        /*
         */
        persist(JsfUtil.PersistAction.DELETE, "Travel Note was Successfully deleted.");

        if (!JsfUtil.isValidationFailed()) {
            // No JSF validation error. The DELETE operation is successfully performed.
            selected = null;            // Remove selection
            listOfTravelNotes = null;
            travelNoteDataChanged = true;
        }
    }

    /*
    *****************************
    Prepare to Create a New Public Video
    *****************************
    */
    public void prepareCreate() {
        selected = new TravelNote();
    }

    public void clearListOfUserTravelNotes() {
        listOfTravelNotes = null;
    }

    /*
    ***************************************************************
    Return the List of User Files that Belong to the Signed-In User
    ***************************************************************
     */
    public List<UserFile> getListOfTravelNoteFiles() {

        if (listOfTravelNoteFiles == null && selected != null) {
            /*
            'user', the object reference of the signed-in user, was put into the SessionMap
            in the initializeSessionMap() method in LoginManager upon user's sign in.
             */
            Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
            User signedInUser = (User) sessionMap.get("user");

            // Obtain the database primary key of the signedInUser object
            Integer userPrimaryKey = signedInUser.getId();

            // Obtain only those files from the database that belong to the signed-in user
            listOfTravelNoteFiles = userFileFacade.findUserFilesByUserPrimaryKeyAndTravelNotePrimaryKey(userPrimaryKey, selected.getId());

        }
        return listOfTravelNoteFiles;
    }

    /*
    ***********************************************
    Delete all the files that belong to the User
    object whose database primary key is primaryKey
    ***********************************************
     */
    public void deleteAllTravelNoteFiles() {

        Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        User signedInUser = (User) sessionMap.get("user");

        // Obtain the database primary key of the signedInUser object
        Integer userPrimaryKey = signedInUser.getId();
        // Obtain the List of files that belongs to the user with primaryKey
        List<UserFile> userTravelNoteFiles = userFileFacade.findUserFilesByUserPrimaryKeyAndTravelNotePrimaryKey(userPrimaryKey, selected.getId());

        if (!userTravelNoteFiles.isEmpty()) {
            // Java 8 looping over a list with lambda
            userTravelNoteFiles.forEach(userFile -> {
                try {
                    /*
                    Delete the user file if it exists.
                    getFilePath() is given in UserFile.java.
                     */
                    Files.deleteIfExists(Paths.get(userFile.getFilePath()));

                    // Remove the user's file record from the database
                    userFileFacade.remove(userFile);

                } catch (IOException ex) {
                    Methods.showMessage("Fatal Error",
                            "Something went wrong while deleting user files!",
                            "See: " + ex.getMessage());
                }
            });
        }
    }

    /*
     ******************************************
     *   Display the Search Results JSF Page  *
     ******************************************
     */
    public String search(Integer type) {

        // Set search type given as input parameter
        searchType = type;

        // Unselect previously selected country if any before showing the search results
        selected = null;

        // Invalidate list of search items to trigger re-query.
        searchItems = null;

        return "/databaseSearch/DatabaseSearchResults?faces-redirect=true";
    }

    /*
     ****************************************************************************************************
     *   Return the list of object references of all those Travel notes that satisfy the search criteria   *
     ****************************************************************************************************
     */
    // This is the Getter method for the instance variable searchItems
    public List<TravelNote> getSearchItems() {

        User signedInUser = this.getSignedInUserID();

        if(signedInUser == null){
            Methods.showMessage("Fatal Error", "Search Type is Out of Range!",
                    "");
            return null;
        }

        if (searchItems == null) {
            switch (searchType) {
                case 1: // Search Type 1
                    switch (searchField) {
                        case "title":
                            // Return the list of object references of all those Travel Notes where
                            // title contains the searchString entered by the user.
                            searchItems = travelNoteFacade.titleQuery(searchString, signedInUser);
                            break;
                        case "note":
                            // Return the list of object references of all those Travel Notes where
                            //  text contains the searchString entered by the user.
                            searchItems = travelNoteFacade.noteQuery(searchString, signedInUser);
                            break;
                        default:
                            // Return the list of object references of all those Travel Notes where
                            // title or text contains the searchString entered by the user.
                            searchItems = travelNoteFacade.allQuery(searchString, signedInUser);
                    }
                    break;
                case 4:
                    // Return the list of object references of all those Travel Notes where
                    // date < the searchString entered by the user.
                    searchItems = travelNoteFacade.type4SearchQuery(searchString, searchString2, signedInUser);
                default:
                    Methods.showMessage("Fatal Error", "Search Type is Out of Range!",
                            "");
            }
        }
        searchString = "";
        searchString2 = "";
        searchField = "";
        return searchItems;
    }

    private User getSignedInUserID(){
        try{
            /*
            'user', the object reference of the signed-in user, was put into the SessionMap
            in the initializeSessionMap() method in LoginManager upon user's sign in.
             */
            Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
            User signedInUser = (User) sessionMap.get("user");

            // Obtain the database primary key of the signedInUser object
            return signedInUser;
        }
        catch (Exception e){
            return null;
        }
    }

}
