/*
 * Created by Osman Balci on 2021.7.14
 * Copyright © 2021 Osman Balci. All rights reserved.
 */
package edu.vt.managers;

import edu.vt.EntityBeans.User;
import edu.vt.EntityBeans.UserFile;
import edu.vt.FacadeBeans.UserFacade;
import edu.vt.FacadeBeans.UserFileFacade;
import edu.vt.controllers.UserFileController;
import edu.vt.globals.Constants;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;

// Needed for PrimeFaces fileUpload
import org.primefaces.model.file.UploadedFile;
import org.primefaces.event.FileUploadEvent;

@Named(value = "fileUploadManager")
@SessionScoped
public class FileUploadManager implements Serializable {
    /*
    ===============================
    Instance Variables (Properties)
    ===============================
     */

    // Used by PrimeFaces fileUpload
    private UploadedFile uploadedFile;

    /*
    The @EJB annotation directs the EJB Container Manager to inject (store) the object reference of the
    UserFacade bean into the instance variable 'userFacade' after it is instantiated at runtime.
     */
    @EJB
    private UserFacade userFacade;

    /*
    The @EJB annotation directs the EJB Container Manager to inject (store) the object reference of the
    UserFileFacade bean into the instance variable 'userFileFacade' after it is instantiated at runtime.
     */
    @EJB
    private UserFileFacade userFileFacade;

    /*
    The @Inject annotation directs the CDI Container Manager to inject (store) the object reference of the
    CDI container-managed UserFileController bean into the instance variable 'userFileController' after it is instantiated at runtime.
     */
    @Inject
    private UserFileController userFileController;

    /*
    =========================
    Getter and Setter Methods
    =========================
     */

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    /*
    ================
    Instance Methods
    ================
     */

    /*
     **************************
     *   Handle File Upload   *
     **************************
     */
    public String handleFileUpload(FileUploadEvent event) throws IOException {

        try {
            String user_name = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("username");

            // user is the object reference of the signed-in User object
            User user = userFacade.findByUsername(user_name);

            /*
            To associate the file to the user, record "userId_filename" in the database.
            Since each file has its own primary key (unique id), the user can upload
            multiple files with the same name.
             */
            String userId_filename = user.getId() + "_" + event.getFile().getFileName();

            /*
            "The try-with-resources statement is a try statement that declares one or more resources. 
            A resource is an object that must be closed after the program is finished with it. 
            The try-with-resources statement ensures that each resource is closed at the end of the
            statement." [Oracle] 
             */
            try (InputStream inputStream = event.getFile().getInputStream();) {
                // The method inputStreamToFile is given below.
                inputStreamToFile(inputStream, userId_filename);
            }

            /*
            Create a new UserFile object with the following attributes:
            (See UserFile entity class representing the UserFile table in the database)
                <> id = auto generated as the unique Primary key for the user file object
                <> filename = userId_filename
                <> user_id = user
             */
            UserFile newUserFile = new UserFile(userId_filename, user);

            /*
            ----------------------------------------------------------------
            If the userId_filename was used before, delete the earlier file.
            ----------------------------------------------------------------
             */
            List<UserFile> filesFound = userFileFacade.findByFilename(userId_filename);

            // If the userId_filename already exists in the database, the filesFound List will not be empty.
            if (!filesFound.isEmpty()) {
                // Remove the file with the same name from the database
                userFileFacade.remove(filesFound.get(0));
            }

            // Create the new UserFile entity (row) in the database
            userFileFacade.create(newUserFile);

            // This sets the necessary flag to ensure the messages are preserved.
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

            // Ask the UserFileController bean to refresh the files list
            userFileController.refreshFileList();

            FacesMessage infoMessage = new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Success!", "File(s) Uploaded Successfully!");
            FacesContext.getCurrentInstance().addMessage(null, infoMessage);

        } catch (IOException e) {
            FacesMessage fatalErrorMessage = new FacesMessage(FacesMessage.SEVERITY_FATAL,
                    "Something went wrong during file upload!", "See: " + e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, fatalErrorMessage);
        }
        
        return "/userFile/ListUserFiles?faces-redirect=true";
    }

    /*
     ***************************
     *   Write Uploaded File   *
     ***************************
     */
    // Writes the uploaded file into the Constants.FILES_ABSOLUTE_PATH directory
    private File inputStreamToFile(InputStream inputStream, String file_name) throws IOException {

        // Read the series of bytes from the input stream into the buffer
        byte[] buffer = new byte[inputStream.available()];
        inputStream.read(buffer);

        // Set target file to be located in Constants.FILES_ABSOLUTE_PATH directory with given name file_name
        File targetFile = new File(Constants.FILES_ABSOLUTE_PATH, file_name);

        // Declare local variable
        OutputStream outStream;

        // Set output stream to be the target file
        outStream = new FileOutputStream(targetFile);

        // Write the series of bytes from the buffer into the output stream which is the target file
        outStream.write(buffer);
        outStream.close();

        return targetFile;
    }

}
