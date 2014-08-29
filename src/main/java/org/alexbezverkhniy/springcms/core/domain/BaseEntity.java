package org.alexbezverkhniy.springcms.core.domain;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
public abstract class BaseEntity<PK extends Serializable> { //extends AbstractPersistable<PK> {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private PK id;

    @Column(columnDefinition = "TINYINT")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean enabled;

    @Temporal(TemporalType.TIMESTAMP)
	protected Date created;

	@Temporal(TemporalType.TIMESTAMP)
	protected Date lastModified;
	
	public BaseEntity() {
		this(new Date(), new Date());		
	}

	public BaseEntity(Date created, Date lastModified) {
		this.created = created;
		this.lastModified = lastModified;
	}
	
	public PK getId() {
		return id;
	}

	public void setId(PK id) {
		this.id = id;
	}

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}	
}
