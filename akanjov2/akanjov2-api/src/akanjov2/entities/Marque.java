package akanjov2.entities;
import veda.godao.annotations.Column;
import veda.godao.annotations.Table;
import veda.godao.annotations.PrimaryKey;

@Table("marque")

public class Marque {
    @PrimaryKey
	@Column("idmarque")
	private Integer idmarque;
	@Column("nom")
	private String nom;

	public Marque(){}
	public Marque(Integer o){ idmarque=o; }

	public void setIdmarque(Integer o){ idmarque=o; }
	public Integer getIdmarque(){ return idmarque; }
	public void setNom(String o){ nom=o; }
	public String getNom(){ return nom; }

    public String getLabel() throws Exception {
        throw new Exception("Unimplemented method getLabel");   
    }
}

