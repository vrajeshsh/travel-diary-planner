package edu.vt.controllers;

import edu.vt.EntityBeans.User;
import edu.vt.EntityBeans.UserFile;
import edu.vt.controllers.util.JsfUtil;
import edu.vt.controllers.util.JsfUtil.PersistAction;
import edu.vt.FacadeBeans.UserFileFacade;
import edu.vt.globals.Constants;
import edu.vt.globals.Methods;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@Named("userFileController")
@SessionScoped

public class UserFileController implements Serializable {
    /*
    ===============================
    Instance Variables (Properties)
    ===============================

    /*
    The @EJB annotation directs the EJB Container Manager to inject (store) the object reference of the
    UserFileFacade bean into the instance variable 'userFileFacade' after it is instantiated at runtime.
     */
    @EJB
    private UserFileFacade userFileFacade;

    // 'selected' contains the object reference of the selected User File object
    private UserFile selected;

    // 'listOfUserFiles' is a List containing the object references of User File objects
    private List<UserFile> listOfUserFiles = null;

    /*
    cleanedFileNameHashMap<KEY, VALUE>
        KEY   = Integer fileId
        VALUE = String cleanedFileNameForSelected
     */
    HashMap<Integer, String> cleanedFileNameHashMap = null;

    // Selected row number in p:dataTable in ListUserFiles.xhtml
    private String selectedRowNumber = "0";

    /*
    =========================
    Getter and Setter Methods
    =========================
     */
    public UserFile getSelected() {
        return selected;
    }

    public void setSelected(UserFile selected) {
        this.selected = selected;
    }

    public String getSelectedRowNumber() {
        return selectedRowNumber;
    }

    public void setSelectedRowNumber(String selectedRowNumber) {
        this.selectedRowNumber = selectedRowNumber;
    }

    /*
    ***************************************************************
    Return the List of User Files that Belong to the Signed-In User
    ***************************************************************
     */
    public List<UserFile> getListOfUserFiles() {

        if (listOfUserFiles == null) {
            /*
            'user', the object reference of the signed-in user, was put into the SessionMap
            in the initializeSessionMap() method in LoginManager upon user's sign in.
             */
            Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
            User signedInUser = (User) sessionMap.get("user");

            // Obtain the database primary key of the signedInUser object
            Integer primaryKey = signedInUser.getId();

            // Obtain only those files from the database that belong to the signed-in user
            listOfUserFiles = userFileFacade.findUserFilesByUserPrimaryKey(primaryKey);

            // Instantiate a new hash map object
            cleanedFileNameHashMap = new HashMap<>();

            /*
            cleanedFileNameHashMap<KEY, VALUE>
                KEY   = Integer fileId
                VALUE = String cleanedFileNameForSelected
             */
            listOfUserFiles.forEach(userFile -> {

                // Obtain the filename stored in CloudStorage/FileStorage as 'userId_filename'
                String storedFileName = userFile.getFilename();

                // Remove the "userId_" (e.g., "4_") prefix in the stored filename
                String cleanedFileName = storedFileName.substring(storedFileName.indexOf("_") + 1);

                // Obtain the file database Primary Key id
                Integer fileId = userFile.getId();

                // Create an entry in the hash map as a key-value pair
                cleanedFileNameHashMap.put(fileId, cleanedFileName);
            });
        }
        return listOfUserFiles;
    }

    public List<UserFile> getListOfUserTravelNoteFiles(Integer travelNoteId) {

        if (listOfUserFiles == null) {
            /*
            'user', the object reference of the signed-in user, was put into the SessionMap
            in the initializeSessionMap() method in LoginManager upon user's sign in.
             */
            Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
            User signedInUser = (User) sessionMap.get("user");

            // Obtain the database primary key of the signedInUser object
            Integer userPrimaryKey = signedInUser.getId();

            // Obtain only those files from the database that belong to the signed-in user
            listOfUserFiles = userFileFacade.findUserFilesByUserPrimaryKeyAndTravelNotePrimaryKey(userPrimaryKey, travelNoteId);

            // Instantiate a new hash map object
            cleanedFileNameHashMap = new HashMap<>();

            /*
            cleanedFileNameHashMap<KEY, VALUE>
                KEY   = Integer fileId
                VALUE = String cleanedFileNameForSelected
             */
            listOfUserFiles.forEach(userFile -> {

                // Obtain the filename stored in CloudStorage/FileStorage as 'userId_filename'
                String storedFileName = userFile.getFilename();

                // Remove the "userId_" (e.g., "4_") prefix in the stored filename
                String cleanedFileName = storedFileName.substring(storedFileName.indexOf("_") + 1);

                // Obtain the file database Primary Key id
                Integer fileId = userFile.getId();

                // Create an entry in the hash map as a key-value pair
                cleanedFileNameHashMap.put(fileId, cleanedFileName);
            });
        }
        List<UserFile> copy = new ArrayList<UserFile>(listOfUserFiles);
        clearListOfUserFiles();
        return copy;
    }

    public void clearListOfUserFiles() {
        listOfUserFiles = null;
    }    /*
    ================
    Instance Methods
    ================
     */

    // The constants CREATE, DELETE and UPDATE are defined in JsfUtil.java

    /*
    **********************
    Create a New User File
    **********************
     */
    public void create() {
        persist(PersistAction.CREATE,"User File was Successfully Created!");
        /*
        JsfUtil.isValidationFailed() returns TRUE if the validationFailed() method has been called
        for the current request. Return of FALSE means that the create operation was successful and 
        we can reset listOfUserFiles to null so that it will be recreated with the newly created user file.
         */
        if (!JsfUtil.isValidationFailed()) {
            selected = null;            // Remove selection
            listOfUserFiles = null;     // Invalidate listOfUserFiles to trigger re-query.
        }
    }

    // We do not allow update of a user file.
    public void update() {
        persist(PersistAction.UPDATE,"User File was Successfully Updated!");
    }

    /*
     ****************************************************************************
     *   Perform CREATE, EDIT (UPDATE), and DELETE Operations in the Database   *
     ****************************************************************************
     */
    /**
     * @param persistAction refers to CREATE, UPDATE (Edit) or DELETE action
     * @param successMessage displayed to inform the user about the result
     */
    private void persist(PersistAction persistAction, String successMessage) {

        if (selected != null) {
            try {
                if (persistAction != PersistAction.DELETE) {
                    /*
                     -------------------------------------------------
                     Perform CREATE or EDIT operation in the database.
                     -------------------------------------------------
                     The edit(selected) method performs the SAVE (STORE) operation of the "selected"
                     object in the database regardless of whether the object is a newly
                     created object (CREATE) or an edited (updated) object (EDIT or UPDATE).
                    
                     UserFileFacade inherits the edit(selected) method from the AbstractFacade class.
                     */
                    userFileFacade.edit(selected);
                } else {
                    /*
                     -----------------------------------------
                     Perform DELETE operation in the database.
                     -----------------------------------------
                     The remove method performs the DELETE operation in the database.
                    
                     UserFileFacade inherits the remove(selected) method from the AbstractFacade class.
                     */
                    userFileFacade.remove(selected);
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
                    JsfUtil.addErrorMessage(ex,"A Persistence Error Occurred!");
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex,"A Persistence Error Occurred!");
            }
        }
    }

    public UserFile getUserFile(Integer id) {
        return userFileFacade.find(id);
    }

    @FacesConverter(forClass = UserFile.class)
    public static class UserFileControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            UserFileController controller = (UserFileController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "userFileController");
            return controller.getUserFile(getKey(value));
        }

        Integer getKey(String value) {
            int key;
            key = Integer.parseInt(value);
            return key;
        }

        String getStringKey(Integer value) {
            return String.valueOf(value);
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof UserFile) {
                UserFile o = (UserFile) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE,
                        "object {0} is of type {1}; expected type: {2}",
                        new Object[]{object, object.getClass().getName(), UserFile.class.getName()});
                return null;
            }
        }
    }

    /*
    =========================
    Delete Selected User File
    =========================
     */
    public String deleteSelectedUserFile() {

        UserFile userFileToDelete = selected;
        /*
        We need to preserve the messages since we will redirect to show a
        different JSF page after successful deletion of the user file.
         */
        Methods.preserveMessages();

        if (userFileToDelete == null) {
            Methods.showMessage("Fatal Error", "No File Selected!", "You do not have a file to delete!");
            return "";
        } else {
            try {
                // Delete the file from CloudStorage/FileStorage
                Files.deleteIfExists(Paths.get(userFileToDelete.getFilePath()));

                // Delete the user file record from the database
                userFileFacade.remove(userFileToDelete);
                // UserFileFacade inherits the remove() method from AbstractFacade

                Methods.showMessage("Information", "Success!", "Selected File is Successfully Deleted!");

                // See method below
                refreshFileList();

                return "/userFile/ListUserFiles?faces-redirect=true";

            } catch (IOException ex) {
                Methods.showMessage("Fatal Error", "Something went wrong while deleting the user file!",
                        "See: " + ex.getMessage());
                return "";
            }
        }
    }

    /*
    ========================
    Refresh User's File List
    ========================
     */
    public void refreshFileList() {
        /*
        By setting the listOfUserFiles to null, we force the getListOfUserFiles
        method above to retrieve all of the user's files again.
         */
        selected = null;            // Remove selection
        listOfUserFiles = null;     // Invalidate listOfUserFiles to trigger re-query.
    }

    /*
    =======================================
    Return Image or Icon based on File Type
    =======================================
    fileId is the database primary key value for a user file
    Return image if it is an image file; otherwise, return file type icon

    Any type of file can be uploaded or downloaded.
    We identify the types of files that can be displayed or played in the web browser.
    */
    public String fileTypeIcon(Integer fileId) {

        // Obtain the object reference of the UserFile whose primary key = fileId
        UserFile userFile = userFileFacade.getUserFile(fileId);

        // Obtain the userFile's filename as it is stored in the CloudDrive DB database
        String imageFileName = userFile.getFilename();

        // Extract the file extension from the filename
        String fileExtension = imageFileName.substring(imageFileName.lastIndexOf(".") + 1);

        // Convert file extension to uppercase
        String fileExtensionInCaps = fileExtension.toUpperCase();

        switch (fileExtensionInCaps) {
            case "JPG":
            case "JPEG":
            case "PNG":
            case "GIF":
                // If it is an image file, return the image file URI
                return Constants.FILES_URI + imageFileName;
            case "MP4":
            case "OGG":
            case "WEBM":
                // If it is a video file, return the videoFile icon
                return "/resources/images/videoFile.png";
            default:
                // If it is another file type, return the viewFile icon
                return "/resources/images/viewFile.png";
        }
    }

    /*
    =====================================
    Return Cleaned Filename given File Id
    =====================================
     */
    public String cleanedFilenameForFileId(Integer fileId) {
        /*
        cleanedFileNameHashMap<KEY, VALUE>
            KEY   = Integer fileId
            VALUE = String cleanedFileNameForSelected
         */
        // Return the cleaned filename for the given fileId
        return cleanedFileNameHashMap.get(fileId);
    }

    /*
    =========================================
    Return Cleaned Filename for Selected File
    =========================================
     */
    // This method is called from UserFiles.xhtml by passing the fileId as a parameter.
    public String cleanedFileNameForSelected() {

        Integer fileId = selected.getId();
        /*
        cleanedFileNameHashMap<KEY, VALUE>
            KEY   = Integer fileId
            VALUE = String cleanedFileNameForSelected
         */

        // Return the cleaned filename for the selected file
        return cleanedFileNameHashMap.get(fileId);
    }

    /*
    ==========================
    Return Selected File's URI
    ==========================
     */
    public String selectedFileURI() {
        return Constants.FILES_URI + selected.getFilename();
    }

    /*
    =============================================
    Return True if Selected File is an Image File
    =============================================
    Any type of file can be uploaded or downloaded.
    We identify the types of image files that can be displayed in the web browser.
     */
    public boolean isImage() {

        switch (extensionOfSelectedFileInCaps()) {
            case "JPG":
            case "JPEG":
            case "PNG":
            case "GIF":
                // Selected file is an acceptable image file
                return true;
            default:
                return false;
        }
    }

    /*
    ========================================
    Return True if Selected File is Viewable
    ========================================
    Any type of file can be uploaded or downloaded.
    We identify the types of files that can be displayed in the web browser.
     */
    public boolean isViewable() {

        switch (extensionOfSelectedFileInCaps()) {
            case "HTML":
            case "PDF":
            case "TXT":
                // Selected file is viewable within the web browser
                return true;
            default:
                return false;
        }
    }

    /*
    =====================================
    The HTML5 <video> tag can play the
    following video files: MP4, OGG, WEBM
    =====================================
    Any type of file can be uploaded or downloaded.
    We identify the types of video files that can be played in the web browser.
     */
    public boolean isVideo() {
        String fileExtension = extensionOfSelectedFileInCaps();
        return (fileExtension.equals("MP4") || fileExtension.equals("OGG") || fileExtension.equals("WEBM"));
    }

    /*
    ========================================================
    Return Extension of the Selected File in Capital Letters
    ========================================================
     */
    public String extensionOfSelectedFileInCaps() {

        // Obtain the selected filename
        String userFileName = selected.getFilename();

        // Extract the file extension from the filename
        String fileExtension = userFileName.substring(userFileName.lastIndexOf(".") + 1);

        // Return file extension in capital letters
        return fileExtension.toUpperCase();
    }

    public String fileURL(String filename){
        return Constants.FILES_URI + filename;
    }


}
