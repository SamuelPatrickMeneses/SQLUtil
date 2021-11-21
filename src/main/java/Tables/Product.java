
package Tables;

import core.ColumnGetter;
import core.Table;

@Table("product")
public  class Product {
    private Integer id;
    private float presso;
    private String nome;
    private String desc;

    public Product( float presso, String nome) {
        this.presso = presso;
        this.nome = nome;
    }

    public Product(int id, float presso, String nome, String desc) {
        this(presso,nome);
        this.id = id;
        this.desc = desc;
    }

    public Product(float presso, String nome, String desc) {
        this(presso,nome);
        this.desc = desc;
    }

    public Product(int id, float presso, String nome) {
        this(presso,nome);
        this.id = id;
    }
    
    @ColumnGetter("N_proid")
    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    @ColumnGetter("F_proprice")
    public float getPresso() {
        return presso;
    }

    public void setPresso(float presso) {
        this.presso = presso;
    }
    @ColumnGetter("T_proname")
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    @ColumnGetter("T_prodesc")   
    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
