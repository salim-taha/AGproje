// Kullanici.java
public class Kullanici {
    private String kullaniciAdi; // ID
    private String sifre;
    private String isim; 

    public Kullanici(String kullaniciAdi, String sifre, String isim) {
        this.kullaniciAdi = kullaniciAdi;
        this.sifre = sifre;
        this.isim = isim;
    }

    public String getKullaniciAdi() { return kullaniciAdi; }
    public String getIsim() { return isim; }
    public boolean sifreKontrol(String denenenSifre) {
        return this.sifre.equals(denenenSifre);
    }
    
    // YENİ EKLENDİ: Şifreyi (maskesiz) döndürür
    public String getSifre() {
        return sifre;
    }
    
    // Dosyaya kaydetmek için (Değişiklik yok)
    public String toDosyaFormati() {
        return String.join(";", this.kullaniciAdi, this.sifre, this.isim);
    }
}