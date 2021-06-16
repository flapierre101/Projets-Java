package f.l.tpfinal_c54;

import java.io.Serializable;
// classe sérializable pour enregistrer et créer des ville dans la mémoire
public class UneVille implements Serializable {

    private String nomVille, regionVille, nomFichier, shortDesc, siteWeb;

    // Plusieur constructeur dépendant des information que l'utilisateur a.
    // Il doit au minimum avoir le nom, la région et le nom du fichier pour
    // créer une ville

    UneVille(String nom, String region, String fichier) {
        nomVille = nom;
        regionVille = region;
        nomFichier = fichier;
        siteWeb = "";
        shortDesc = "";
    }

    UneVille(String nom, String region, String fichier, String web) {
        nomVille = nom;
        regionVille = region;
        nomFichier = fichier;
        shortDesc = "";
        siteWeb = web;
    }

    UneVille(String nom, String region, String fichier, String desc, String web) {
        nomVille = nom;
        regionVille = region;
        nomFichier = fichier;
        shortDesc = desc;
        siteWeb = web;
    }

    public String getNomFichier() {
        return nomFichier;
    }

    public void setNomFichier(String nomFichier) {
        this.nomFichier = nomFichier;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getNomVille() {
        return nomVille;
    }

    public void setNomVille(String nomVille) {
        this.nomVille = nomVille;
    }

    public String getRegionVille() {
        return regionVille;
    }

    public void setRegionVille(String regionVille) {
        this.regionVille = regionVille;
    }

    public String getSiteWeb() {
        return siteWeb;
    }

    public void setSiteWeb(String siteWeb) {
        this.siteWeb = siteWeb;
    }

}
