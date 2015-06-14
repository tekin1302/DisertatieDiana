package ro.diana.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import ro.diana.util.CustomDateTimeDeserializer;
import ro.diana.util.CustomDateTimeSerializer;

import javax.persistence.*;
import java.io.UnsupportedEncodingException;
import java.sql.Blob;

/**
 * Created by diana on 6/7/2015.
 */
@Entity
@Table(name = "T_FILE_HISTORY")
public class FileHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private Integer id;

    @Column(name="path")
    private String path;

    @JsonIgnore
    @Column(name="content", columnDefinition="blob")
    private byte[] content;

    @ManyToOne
    @JoinColumn(name="user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name="extension_id", referencedColumnName = "id")
    private Extension extension;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "date")
    private DateTime date;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public DateTime getDate() {
        return date;
    }

    public void setDate(DateTime date) {
        this.date = date;
    }

    public Extension getExtension() {
        return extension;
    }

    public void setExtension(Extension extension) {
        this.extension = extension;
    }
    public String getStringContent() {
        if (this.getExtension().getIsText()) {
            try {
                return new String(this.getContent(), "UTF-8");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }
}
