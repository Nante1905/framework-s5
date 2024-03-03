package akanjov2.entities;
import veda.godao.annotations.Column;
import veda.godao.annotations.Table;
import veda.godao.annotations.PrimaryKey;

@Table("utilisateur")

public class Utilisateur {
    @PrimaryKey
	@Column("id")
	private Integer id;
	@Column("nom")
	private String nom;
	@Column("jour")
	private java.time.LocalDate jour;

	public Utilisateur(){}
	public Utilisateur(Integer o){ id=o; }

	public void setId(Integer o){ id=o; }
	public Integer getId(){ return id; }
	public void setNom(String o){ nom=o; }
	public String getNom(){ return nom; }
	public void setJour(java.time.LocalDate o){ jour=o; }
	public java.time.LocalDate getJour(){ return jour; }

    public String getLabel() throws Exception {
        throw new Exception("Unimplemented method getLabel");   
    }
}

