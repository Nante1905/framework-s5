package akanjov2.entities;
import veda.godao.annotations.Column;
import veda.godao.annotations.Table;
import veda.godao.annotations.PrimaryKey;

@Table("produit")

public class Produit {
    @PrimaryKey
	@Column("idproduit")
	private Integer idproduit;
	@Column("nom")
	private String nom;
	@Column("prix")
	private Double prix;
	@veda.godao.annotations.ForeignKey(recursive=true)
	@Column("marque")
	private Marque marque;
	@Column("disponibilite")
	private Integer disponibilite;

	public Produit(){}
	public Produit(Integer o){ idproduit=o; }

	public void setIdproduit(Integer o){ idproduit=o; }
	public Integer getIdproduit(){ return idproduit; }
	public void setNom(String o){ nom=o; }
	public String getNom(){ return nom; }
	public void setPrix(Double o){ prix=o; }
	public Double getPrix(){ return prix; }
	public void setMarque(Marque o){ marque=o; }
	public Marque getMarque(){ return marque; }
	public void setDisponibilite(Integer o){ disponibilite=o; }
	public Integer getDisponibilite(){ return disponibilite; }

    public String getLabel() throws Exception {
        throw new Exception("Unimplemented method getLabel");   
    }
}

