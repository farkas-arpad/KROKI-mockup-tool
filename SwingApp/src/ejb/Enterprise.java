package ejb;

import static javax.persistence.GenerationType.IDENTITY;
import static javax.persistence.CascadeType.ALL;
import javax.persistence.CascadeType;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.ManyToMany;
import javax.persistence.JoinTable;

   /** 
   Class generated using Kroki EJBGenerator 
   @Author mrd 
   Creation date: 14.03.2013  11:33:23h
   **/

@Entity
@Table(name = "WS_ENTERPRISE")
public class Enterprise implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private java.lang.Long id;

	@Column(name = "ENT_ENTERPRISE_NAME", unique = false, nullable = false)
	private java.lang.String enterpriseName;
	
	@Column(name = "ENT_ADDRESS", unique = false, nullable = false)
	private java.lang.String address;
	
	
	@ManyToOne
	@JoinColumn(name="city", referencedColumnName="ID", nullable=true)
	private City city;
	
	
	public Enterprise(){
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public java.lang.String getEnterpriseName() {
		return this.enterpriseName;
	}
	
	public void setEnterpriseName(java.lang.String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}
	
	public java.lang.String getAddress() {
		return this.address;
	}
	
	public void setAddress(java.lang.String address) {
		this.address = address;
	}
	
	public City getCity() {
		return this.city;
	}
	
	public void setCity(City city) {
		this.city = city;
	}
	
}