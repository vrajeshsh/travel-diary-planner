/*
 * Created by Osman Balci on 2021.7.18
 * Copyright © 2021 Osman Balci. All rights reserved.
 */
package edu.vt.managers;

import edu.vt.EntityBeans.UserFile;
import edu.vt.controllers.UserFileController;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.inject.Inject;

// Needed for PrimeFaces fileDownload
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@Named(value = "fileDownloadManager")
@RequestScoped
public class FileDownloadManager implements Serializable {
    /*
    ===============================
    Instance Variables (Properties)
    ===============================
     */

    /*
    The @Inject annotation directs the CDI Container Manager to inject (store) the object reference of the
    CDI container-managed UserFileController bean into the instance variable 'userFileController' after it is instantiated at runtime.
     */
    @Inject
    private UserFileController userFileController;

    /*
    DefaultStreamedContent and StreamedContent classes are
    imported from the org.primefaces.model packages above.
     */
    private StreamedContent file;

    /*
    =========================
    Getter and Setter Methods
    =========================
     */
    public StreamedContent getFile() throws FileNotFoundException {

        UserFile fileToDownload = userFileController.getSelected();

        // getFilename() and getFilePath() are given in UserFile.java
        String nameOfFileToDownload = fileToDownload.getFilename();
        String absolutePathOfFileToDownload = fileToDownload.getFilePath();

        // Returns the MIME type of the specified file or null if the MIME type is not known
        String contentMimeType = FacesContext.getCurrentInstance().getExternalContext().getMimeType(absolutePathOfFileToDownload);

        // Obtain the filename without the prefix "userId_" inserted to associate the file to a user
        String downloadedFileName = nameOfFileToDownload.substring(nameOfFileToDownload.indexOf("_") + 1);

        // FileInputStream must be used here since the files are stored in a directory external to our application
        FileInputStream streamOfFileToDownload = new FileInputStream(absolutePathOfFileToDownload);

        file = DefaultStreamedContent.builder().contentType(contentMimeType).name(downloadedFileName).stream(() -> streamOfFileToDownload).build();

        return file;
    }

    public void setFile(StreamedContent file) {
        this.file = file;
    }
}
