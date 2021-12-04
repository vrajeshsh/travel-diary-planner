package edu.vt.EntityBeans;
import edu.vt.EntityBeans.User;
import edu.vt.FacadeBeans.AbstractFacade;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/*
The @Entity annotation designates this class as a JPA Entity POJO class
representing the UserPhoto table in the CloudDriveDB database.
 */
@Entity

// Name of the database table represented
@Table(name = "TravelNote")

@NamedQueries({
    /*
    private User userId;    --> userId is the object reference of the User object.
    userId.id               --> User object's database primary key
     */
        @NamedQuery(name = "TravelNote.findTravelNotesByUserDatabasePrimaryKey", query = "SELECT n FROM TravelNote n WHERE n.userId = :userId")
        , @NamedQuery(name = "TravelNote.findById", query = "SELECT n FROM TravelNote n WHERE n.id = :id")
})
public class TravelNote implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "id")
  private Integer id;

  @Basic(optional = false)
  @NotNull(message = "Text is required!")
  @Column(name = "text")
  @Size(min = 1, max = 10000)
  private String text;

  @JoinColumn(name = "user_id", referencedColumnName = "id")
  @ManyToOne
  private User userId;

  @Basic(optional = false)
  @NotNull(message = "Video publication date is required!")
  @Column(name = "date_created")
  private Date dateCreated;

  @Basic(optional = false)
  @NotNull(message = "Latitude is required!")
  @Column(name = "lat")
  private float lat;

  @Basic(optional = false)
  @NotNull(message = "Longitude is required!")
  @Column(name = "lng")
  private float lng;


  /*
    ===================================================================
    Class constructors for instantiating a UserFile entity object to
    represent a row in the UserFile table in the CloudDriveDB database.
    ===================================================================
     */
  public TravelNote() {
  }

  // Used in handleFileUpload() method of FileUploadManager
  public TravelNote(String text, User id) {
    this.text = text;
    userId = id;
  }

  /*
    ======================================================
    Getter and Setter methods
    ======================================================
   */

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public User getUserId() {
    return userId;
  }

  public void setUserId(User userId) {
    this.userId = userId;
  }

  public Date getDateCreated() {
    return dateCreated;
  }

  public void setDateCreated(Date dateCreated) {
    this.dateCreated = dateCreated;
  }

  public float getLat() {
    return lat;
  }

  public void setLat(float lat) {
    this.lat = lat;
  }

  public float getLng() {
    return lng;
  }

  public void setLng(float lng) {
    this.lng = lng;
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
    if (!(object instanceof TravelNote)) {
      return false;
    }
    TravelNote other = (TravelNote) object;
    return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
  }

  // Return String representation of database primary key id
  @Override
  public String toString() {
    return id.toString();
  }
}
