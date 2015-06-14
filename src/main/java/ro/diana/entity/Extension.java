package ro.diana.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import ro.diana.util.CustomDateTimeDeserializer;
import ro.diana.util.CustomDateTimeSerializer;

import javax.persistence.*;

/**
 * Created by diana on 6/7/2015.
 */
@Entity
@Table(name = "T_EXTENSION")
public class Extension {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private Integer id;

    @Column(name="extension")
    private String extension;

    @Column(name="is_text")
    private Boolean isText;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public Boolean getIsText() {
        return isText;
    }

    public void setIsText(Boolean isText) {
        this.isText = isText;
    }
}
