// Arac.java
public class Arac {
    private String aracID;
    private String aracSinifi;
    private String sahipAdi;
    private String sahipEposta;
    private double bakiye;

    public Arac(String aracID, String aracSinifi, String sahipAdi, String sahipEposta, double bakiye) {
        this.aracID = aracID;
        this.aracSinifi = aracSinifi;
        this.sahipAdi = sahipAdi;
        this.sahipEposta = sahipEposta;
        this.bakiye = bakiye;
    }

    // --- Get metotları ---
    public String getAracID() { return aracID; }
    public String getAracSinifi() { return aracSinifi; }
    public String getSahipAdi() { return sahipAdi; }
    public String getSahipEposta() { return sahipEposta; }
    public double getBakiye() { return bakiye; }

    // --- İş Mantığı Metotları ---
    public boolean bakiyeNegatifMi() { return this.bakiye < 0; }
    public void ucretTahsilEt(double ucret) { this.bakiye -= ucret; }
    
    // YENİ EKLENDİ: Aracı dosyaya kaydetmek için
    /**
     * Aracı "ID;SahipAdi;AracSinifi;SahipEposta;Bakiye" formatında bir string'e çevirir.
     */
    public String toDosyaFormati() {
        // String.format'ta Locale.US kullanmak, ondalık ayraç olarak ',' değil '.'
        // kullanılmasını garanti eder, bu da dosyadan okurken hata çıkmasını engeller.
        return String.join(";", 
            this.aracID, 
            this.sahipAdi, 
            this.aracSinifi, 
            this.sahipEposta, 
            String.format(java.util.Locale.US, "%.2f", this.bakiye)
        );
    }
}