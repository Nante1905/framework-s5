package akanjov2.entities;
import veda.godao.annotations.Column;
import veda.godao.annotations.Table;
import veda.godao.annotations.PrimaryKey;

@Table("commande")

public class Commande {
    @PrimaryKey
	@Column("id")
	private Integer id;
	@veda.godao.annotations.ForeignKey(recursive=true)
	@Column("id_utilisateur")
	private Utilisateur utilisateur;
	@Column("jour")
	private java.time.LocalDate jour;

	public Commande(){}
	public Commande(Integer o){ id=o; }

	public void setId(Integer o){ id=o; }
	public Integer getId(){ return id; }
	public void setUtilisateur(Utilisateur o){ utilisateur=o; }
	public Utilisateur getUtilisateur(){ return utilisateur; }
	public void setJour(java.time.LocalDate o){ jour=o; }
	public java.time.LocalDate getJour(){ return jour; }

    public String getLabel() throws Exception {
        throw new Exception("Unimplemented method getLabel");   
    }
}

