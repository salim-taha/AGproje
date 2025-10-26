// GecisKaydi.java
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GecisKaydi {
    private int islemID; // YENİ: İşlem Numarası
    private String aracID;
    private String tip; // "GIRIS" veya "CIKIS"
    private LocalDateTime tarih;
    private double ucret; 
    private String[] gecilenYerler; 

    // GÜNCELLENDİ: Constructor'a 'islemID' eklendi
    public GecisKaydi(int islemID, String aracID, String tip, LocalDateTime tarih, double ucret, String[] gecilenYerler) {
        this.islemID = islemID;
        this.aracID = aracID;
        this.tip = tip;
        this.tarih = tarih;
        this.ucret = ucret;
        this.gecilenYerler = gecilenYerler;
    }
    
    // --- Get metotları ---
    public int getIslemID() { return islemID; } // YENİ
    public String getAracID() { return aracID; }
    public String getTip() { return tip; }
    public double getUcret() { return ucret; }
    
    public String getFormatliTarih() {
        return tarih.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
    }
    
    // GÜNCELLENDİ: getGecilenYerlerMetni
    /**
     * 'tip.equals("GIRIS")' kontrolü kaldırıldı.
     * Artık 'gecilenYerler' dizisi doluysa (null veya boş değilse), 
     * tip ne olursa olsun onu gösterecek.
     */
    public String getGecilenYerlerMetni() {
        if (gecilenYerler == null || gecilenYerler.length == 0) {
            return "N/A";
        }
        // Giriş için tek bir yer, Çıkış için virgülle ayrılmış yerler gösterilecek.
        return String.join(",", gecilenYerler);
    }
    
    // GÜNCELLENDİ: Dosya formatına 'islemID' eklendi
    /**
     * Kaydı "IslemID;Tarih;AracID;Tip;Ucret;KullanilanYollar" formatına çevirir.
     */
    public String toDosyaFormati() {
        String tarihStr = this.tarih.toString(); 
        String ucretStr = String.format(java.util.Locale.US, "%.2f", this.ucret);
        String yollarStr = getGecilenYerlerMetni();
        
        return String.join(";", 
            String.valueOf(this.islemID), // YENİ
            tarihStr,
            this.aracID,
            this.tip,
            ucretStr,
            yollarStr
        );
    }
}