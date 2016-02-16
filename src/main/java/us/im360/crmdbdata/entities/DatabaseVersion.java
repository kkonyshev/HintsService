package us.im360.crmdbdata.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the database_version database table.
 * 
 */
@Entity
@Table(name="database_version")
@NamedQuery(name="DatabaseVersion.findAll", query="SELECT d FROM DatabaseVersion d")
public class DatabaseVersion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	private String author;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="date_executed")
	private Date dateExecuted;

	private String description;

	public DatabaseVersion() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAuthor() {
		return this.author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Date getDateExecuted() {
		return this.dateExecuted;
	}

	public void setDateExecuted(Date dateExecuted) {
		this.dateExecuted = dateExecuted;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}