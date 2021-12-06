package edu.vt.EntityBeans;

import edu.vt.globals.Constants;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/*
The @Entity annotation designates this class as a JPA Entity POJO class
representing the UserFile table in the TravelDiaryPlannerDB database.
 */
@Entity

// Name of the database table represented
@Table(name = "UserFile")

@NamedQueries({
    /*
    private User userId;    --> userId is the object reference of the User object.
    userId.id               --> User object's database primary key
     */
    @NamedQuery(name = "UserFile.findAll", query = "SELECT u FROM UserFile u")
    , @NamedQuery(name = "UserFile.findById", query = "SELECT u FROM UserFile u WHERE u.id = :id")
        , @NamedQuery(name = "UserFile.findByFilename", query = "SELECT u FROM UserFile u WHERE u.filename = :filename")
        , @NamedQuery(name = "UserFile.findByFilenameAndTravelNoteId", query = "SELECT u FROM UserFile u WHERE u.filename = :filename AND u.travelNoteId = :travelNoteId")
    , @NamedQuery(name = "UserFile.findUserFilesByUserId", query = "SELECT u FROM UserFile u WHERE u.userId.id = :userId")
    , @NamedQuery(name = "UserFile.findUserFilesByUserIdAndTravelNoteId", query = "SELECT u FROM UserFile u WHERE u.userId.id = :userId AND u.travelNoteId = :travelNoteId")
})

public class UserFile implements Serializable {
    /*
    ========================================================
    Instance variables representing the attributes (columns)
    of the UserFile table in the TravelDiaryPlannerDB database.

    CREATE TABLE UserFile
    (
           id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT NOT NULL,
           filename VARCHAR(256) NOT NULL,
           user_id INT UNSIGNED,
           FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE CASCADE
    );

    ========================================================
     */
    private static final long serialVersionUID = 1L;

    // id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT NOT NULL
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    // filename VARCHAR(256) NOT NULL
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 256)
    @Column(name = "filename")
    private String filename;

    // user_id INT UNSIGNED
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne
    private User userId;

    @Basic(optional = false)
    @Column(name = "travel_note_id")
    private Integer travelNoteId;

    /*
    ===================================================================
    Class constructors for instantiating a UserFile entity object to
    represent a row in the UserFile table in the TravelDiaryPlannerDB database.
    ===================================================================
     */
    public UserFile() {
    }

    // Used in handleFileUpload() method of FileUploadManager
    public UserFile(String filename, User id) {
        this.filename = filename;
        userId = id;
    }

    // Used in handleFileUpload() method of FileUploadManager
    public UserFile(String filename, User id, Integer travelNoteId) {
        this.filename = filename;
        userId = id;
        this.travelNoteId = travelNoteId;
    }
    /*
    ======================================================
    Getter and Setter methods for the attributes (columns)
    of the UserFile table in the TravelDiaryPlannerDB database.
    ======================================================
     */
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public Integer getTravelNoteId() {
        return travelNoteId;
    }

    public void setTravelNoteId(Integer travelNoteId) {
        this.travelNoteId = travelNoteId;
    }

    /*
    ================================
    Instance Methods Used Internally
    ================================
     */

    // Generate and return a hash code value for the object with database primary key id
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    /*
     Checks if the UserFile object identified by 'object' is the same as the UserFile object identified by 'id'
     Parameter object = UserFile object identified by 'object'
     Returns True if the UserFile 'object' and 'id' are the same; otherwise, return False
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof UserFile)) {
            return false;
        }
        UserFile other = (UserFile) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

    // Return String representation of database primary key id
    @Override
    public String toString() {
        return id.toString();
    }

    /*
    =============
    Other Methods
    =============
     */

    public String getFilePath() {
        return Constants.FILES_ABSOLUTE_PATH + getFilename();
    }

}
