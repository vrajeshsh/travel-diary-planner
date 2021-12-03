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

            // Obtain the database primary key of the signedInUser object
            Integer primaryKey = signedInUser.getId();

            // Obtain only those files from the database that belong to the signed-in user
            listOfTravelNotes = travelNoteFacade.findTravelNotesByUserPrimaryKey(primaryKey);

        }
        return listOfTravelNotes;
    }

    public void setListOfTravelNotes(List<TravelNote> listOfTravelNotes) {
        this.listOfTravelNotes = listOfTravelNotes;
    }
}
