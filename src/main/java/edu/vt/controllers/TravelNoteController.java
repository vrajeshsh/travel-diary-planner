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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

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

    private TravelNote selected;

    private List<TravelNote> listOfTravelNotes = null;

    //  indicating if travel note data changed or not
    private Boolean travelNoteDataChanged;
    /*
    =========================
    Getter and Setter Methods
    =========================
     */

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

}
