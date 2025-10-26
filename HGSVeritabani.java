// HGSVeritabani.java
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Collections;
import java.util.Comparator;

public class HGSVeritabani {
    
    private Map<String, Arac> kayitliAraclar;
    private ArrayList<GecisKaydi> girisKayitlari;
    private ArrayList<GecisKaydi> cikisKayitlari;
    private double toplamKazanc;

    public HGSVeritabani() {
        this.kayitliAraclar = new HashMap<>();
        this.girisKayitlari = new ArrayList<>();
        this.cikisKayitlari = new ArrayList<>();
        this.toplamKazanc = 0.0;
    }

    // YENİ METOT: Hafızayı temizlemek için
    public void sifirla() {
        this.kayitliAraclar.clear();
        this.girisKayitlari.clear();
        this.cikisKayitlari.clear();
        this.toplamKazanc = 0.0;
    }

    // --- Değişiklik olmayan diğer metotlar ---
    public void aracKaydet(Arac arac) { kayitliAraclar.put(arac.getAracID(), arac); }
    public Arac aracSorgula(String aracID) { return kayitliAraclar.get(aracID); }
    public Map<String, Arac> getKayitliAraclarMap() { return this.kayitliAraclar; }
    public void girisKaydet(GecisKaydi kayit) { girisKayitlari.add(kayit); }
    public void cikisKaydet(GecisKaydi kayit) { cikisKayitlari.add(kayit); }
    public void kazancEkle(double ucret) { this.toplamKazanc += ucret; }
    public double getToplamKazanc() { return toplamKazanc; }
    public int getToplamAracSayisi() { return kayitliAraclar.size(); }
    public void setToplamKazanc(double kazanc) { this.toplamKazanc = kazanc; }

    public Object[][] getAracListesiVerisi() {
        ArrayList<Arac> aracListesi = new ArrayList<>(kayitliAraclar.values());
        Collections.sort(aracListesi, new Comparator<Arac>() {
            @Override
            public int compare(Arac a1, Arac a2) {
                try {
                    int id1 = Integer.parseInt(a1.getAracID());
                    int id2 = Integer.parseInt(a2.getAracID());
                    return Integer.compare(id1, id2);
                } catch (NumberFormatException e) {
                    return a1.getAracID().compareTo(a2.getAracID());
                }
            }
        });
        Object[][] veri = new Object[aracListesi.size()][4];
        int i = 0;
        for (Arac arac : aracListesi) { 
            veri[i][0] = arac.getAracID();
            veri[i][1] = arac.getSahipAdi();
            veri[i][2] = arac.getAracSinifi();
            veri[i][3] = String.format("%.2f", arac.getBakiye());
            i++;
        }
        return veri;
    }
    public Object[][] getGirisListesiVerisi() {
        Object[][] veri = new Object[girisKayitlari.size()][5]; 
        int i = 0;
        for (GecisKaydi kayit : girisKayitlari) {
            veri[i][0] = kayit.getIslemID(); 
            veri[i][1] = kayit.getAracID();
            veri[i][2] = kayit.getTip();
            veri[i][3] = kayit.getFormatliTarih();
            veri[i][4] = kayit.getGecilenYerlerMetni();
            i++;
        }
        return veri;
    }
    public Object[][] getCikisListesiVerisi() {
        Object[][] veri = new Object[cikisKayitlari.size()][6]; 
        int i = 0;
        for (GecisKaydi kayit : cikisKayitlari) {
            veri[i][0] = kayit.getIslemID(); 
            veri[i][1] = kayit.getAracID();
            veri[i][2] = kayit.getTip();
            veri[i][3] = kayit.getFormatliTarih();
            veri[i][4] = String.format("%.2f", kayit.getUcret());
            veri[i][5] = kayit.getGecilenYerlerMetni();
            i++;
        }
        return veri;
    }
}