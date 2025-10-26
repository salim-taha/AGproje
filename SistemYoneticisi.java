// SistemYoneticisi.java
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;
import javax.swing.JOptionPane;

public class SistemYoneticisi {

    private HGSVeritabani veritabani;
    private Map<String, Kullanici> kullanicilar;
    private Random rastgele = new Random();
    private ArrayList<String> suAnIcrideOlanAraclar;

    private int sonGirisID = 0;
    private int sonCikisID = 0;
    private int gunSayaci = 0;

    private static final String ARAC_VERITABANI_DOSYASI = "arac_veritabani.txt";
    private static final String GECIS_KAYIT_DOSYASI = "gecis_kayitlari.txt";
    private static final String SISTEM_OZET_DOSYASI = "sistem_ozeti.txt";
    private static final String KULLANICI_VERITABANI_DOSYASI = "kullanici_veritabani.txt";

    private static final String YONETICI_SIFRESI = "1234";

    private static final List<String> ROTA_LISTESI = Arrays.asList(
            "Otoyol", "Avrasya Tuneli", "Osmangazi Koprusu"
    );

    public SistemYoneticisi() {
        this.veritabani = new HGSVeritabani();
        this.kullanicilar = new HashMap<>();
        this.suAnIcrideOlanAraclar = new ArrayList<>();

        System.out.println("SistemYoneticisi başlatıldı. Veriler yükleniyor...");
        sistemOzetiniYukle();
        veritabaniniDosyadanYukle();
        gecisKayitlariniDosyadanYukle();
        kullaniciVeritabaniYukle();
    }

    // --- KULLANICI YÖNETİMİ METOTLARI ---

    // GÜNCELLENDİ: 'kullaniciSil' metodu artık SADECE String alıyor
    public boolean kullaniciSil(String kullaniciAdi) {
        if (kullaniciAdi == null || kullaniciAdi.trim().isEmpty()) {
            System.err.println("HATA: Silinecek kullanıcı adı boş olamaz.");
            return false;
        }
        if (!kullanicilar.containsKey(kullaniciAdi)) {
            System.err.println("HATA: Silinecek kullanıcı bulunamadı: " + kullaniciAdi);
            return false;
        }

        // Son kullanıcı kontrolü artık KullaniciYonetimEkrani'nda yapıldığı için
        // burada tekrar yapmaya gerek yok.

        // Kullanıcıyı hafızadan sil
        kullanicilar.remove(kullaniciAdi);
        System.out.println("Kullanıcı '" + kullaniciAdi + "' hafızadan silindi.");

        // Dosyayı güncelle
        kullaniciVeritabaniKaydet();
        return true; // Başarılı
    }


    // --- Diğer Tüm Metotlar (Değişiklik yok) ---
    // (Aşağıdaki metotlar bir önceki cevaptaki ile aynı)

    public HGSVeritabani getVeritabani() { return veritabani; }
    public int getGunSayaci() { return this.gunSayaci; }
    public Kullanici getKullanici(String kullaniciAdi) { return kullanicilar.get(kullaniciAdi); }
    public boolean yoneticiSifreDogrula(String denenenSifre) { return YONETICI_SIFRESI.equals(denenenSifre); }
    public boolean kullaniciKaydet(String kullaniciAdi, String sifre, String isim) { if (kullaniciAdi == null || kullaniciAdi.trim().isEmpty()) { return false; } if (sifre == null || sifre.isEmpty()) { return false; } if (isim == null || isim.trim().isEmpty()) { return false; } if (kullanicilar.containsKey(kullaniciAdi)) { return false; } Kullanici yeniKullanici = new Kullanici(kullaniciAdi, sifre, isim); kullanicilar.put(kullaniciAdi, yeniKullanici); kullaniciVeritabaniKaydet(); System.out.println("Yeni kullanıcı başarıyla kaydedildi: " + kullaniciAdi); return true; }
    public int getToplamKullaniciSayisi() { return kullanicilar.size(); }
    public void tumKullanicilariSil() { System.out.println("Tüm kullanıcılar siliniyor..."); this.kullanicilar.clear(); kullaniciVeritabaniKaydet(); System.out.println("Tüm kullanıcılar başarıyla silindi."); }
    public Object[][] getKullaniciListesiVerisi() { Object[][] veri = new Object[kullanicilar.size()][3]; int i = 0; List<Kullanici> siraliListe = new ArrayList<>(kullanicilar.values()); Collections.sort(siraliListe, Comparator.comparing(Kullanici::getKullaniciAdi)); for (Kullanici k : siraliListe) { veri[i][0] = Boolean.FALSE; veri[i][1] = k.getKullaniciAdi(); veri[i][2] = k.getIsim(); i++; } return veri; }
    private void kullaniciVeritabaniYukle() { System.out.println(KULLANICI_VERITABANI_DOSYASI + " dosyasından kullanıcılar yükleniyor..."); try (BufferedReader br = new BufferedReader(new FileReader(KULLANICI_VERITABANI_DOSYASI))) { String satir; int sayac = 0; while ((satir = br.readLine()) != null) { if (satir.trim().isEmpty()) continue; String[] p = satir.split(";", 3); if (p.length == 3) { Kullanici k = new Kullanici(p[0], p[1], p[2]); this.kullanicilar.put(k.getKullaniciAdi(), k); sayac++; } } System.out.println(sayac + " kullanıcı yüklendi."); } catch (FileNotFoundException e) { System.out.println(KULLANICI_VERITABANI_DOSYASI + " bulunamadı."); } catch (IOException e) { System.err.println("Kullanıcı veritabanı okunurken HATA: " + e.getMessage()); } }
    public void kullaniciVeritabaniKaydet() { System.out.println(KULLANICI_VERITABANI_DOSYASI + " dosyasına kullanıcılar kaydediliyor..."); try (BufferedWriter bw = new BufferedWriter(new FileWriter(KULLANICI_VERITABANI_DOSYASI, false))) { for (Kullanici k : this.kullanicilar.values()) { bw.write(k.toDosyaFormati()); bw.newLine(); } System.out.println(this.kullanicilar.size() + " kullanıcı kaydedildi."); } catch (IOException e) { System.err.println("Kullanıcı veritabanı dosyasına yazılırken HATA: " + e.getMessage()); } }
    public int[] gunlukSimulasyonuBaslat() { System.out.println("Yeni gün başlıyor... Önceki günden kalanlar kontrol ediliyor..."); kalanAraclariCikisYap("Otomatik Gün Sonu Çıkışı (Önceki Gün)"); this.gunSayaci++; int basariliGirisSayisi = 0; int basariliCikisSayisi_Simulasyon = 0; System.out.println("KULLANICI TETİKLEDİ: GÜN " + this.gunSayaci + " SİMÜLASYONU BAŞLIYOR."); int girisDenemeSayisi = 50; for (int i = 0; i < girisDenemeSayisi; i++) { String aracID = String.valueOf(rastgele.nextInt(40) + 1); if (giseIslemYap(aracID, "GIRIS", null)) { basariliGirisSayisi++; } } int cikisDenemeSayisi = 25; for (int i = 0; i < cikisDenemeSayisi; i++) { if (suAnIcrideOlanAraclar.isEmpty()) { break; } int rastgeleIndex = rastgele.nextInt(suAnIcrideOlanAraclar.size()); String aracID = suAnIcrideOlanAraclar.get(rastgeleIndex); if (giseIslemYap(aracID, "CIKIS", getRandomRotalar())) { basariliCikisSayisi_Simulasyon++; } } System.out.println("Günlük simülasyon tamamlandı. Gün sonu temizliği yapılıyor..."); kalanAraclariCikisYap("Otomatik Gün Sonu Çıkışı (Mevcut Gün)"); System.out.println("Gün " + this.gunSayaci + " tamamlandı. " + "İçeride kalan araç sayısı: " + suAnIcrideOlanAraclar.size()); return new int[]{basariliGirisSayisi}; }
    private void kalanAraclariCikisYap(String logMesaji) { int kalanAracSayisi = suAnIcrideOlanAraclar.size(); if (kalanAracSayisi > 0) { System.out.println(logMesaji + ": İçeride kalan " + kalanAracSayisi + " araç için otomatik çıkış yapılıyor..."); while (!this.suAnIcrideOlanAraclar.isEmpty()) { String aracID = this.suAnIcrideOlanAraclar.get(0); giseIslemYap(aracID, "CIKIS", getRandomRotalar()); System.out.println(logMesaji + ": ID " + aracID + " çıkış yaptı."); } System.out.println("Tüm kalan araçlar çıkış yaptı."); } }
    private void sistemOzetiniYukle() { try (BufferedReader br = new BufferedReader(new FileReader(SISTEM_OZET_DOSYASI))) { String kazancSatiri = br.readLine(); String girisIdSatiri = br.readLine(); String cikisIdSatiri = br.readLine(); String gunSatiri = br.readLine(); if (kazancSatiri != null) { veritabani.setToplamKazanc(Double.parseDouble(kazancSatiri)); } if (girisIdSatiri != null) { this.sonGirisID = Integer.parseInt(girisIdSatiri); } if (cikisIdSatiri != null) { this.sonCikisID = Integer.parseInt(cikisIdSatiri); } if (gunSatiri != null) { this.gunSayaci = Integer.parseInt(gunSatiri); } System.out.println("Sistem özeti yüklendi. Gün: " + this.gunSayaci + ", Kazanç: " + veritabani.getToplamKazanc()); } catch (FileNotFoundException e) { System.out.println("Sistem özeti dosyası bulunamadı. Tüm sayaçlar 0'dan başlıyor."); } catch (IOException | NumberFormatException e) { System.err.println("Sistem özeti dosyası bozuk veya okunamadı. 0'dan başlıyor."); } }
    public void sistemOzetiniKaydet() { try (BufferedWriter bw = new BufferedWriter(new FileWriter(SISTEM_OZET_DOSYASI))) { bw.write(String.format(java.util.Locale.US, "%.2f", veritabani.getToplamKazanc())); bw.newLine(); bw.write(String.valueOf(this.sonGirisID)); bw.newLine(); bw.write(String.valueOf(this.sonCikisID)); bw.newLine(); bw.write(String.valueOf(this.gunSayaci)); bw.newLine(); System.out.println("Sistem özeti (gün, kazanç ve işlem ID'leri) kaydedildi."); } catch (IOException e) { System.err.println("Sistem özeti dosyasına yazılırken HATA oluştu: " + e.getMessage()); } }
    private boolean giseIslemYap(String aracID, String islemTipi, String[] gecilenYerler) { Arac arac = veritabani.aracSorgula(aracID); if (arac == null && islemTipi.equals("GIRIS")) { String aracSinifi = Ucretlendirme.getRandomAracSinifi(); String sahipAdi = "Yeni Kullanici " + aracID; String sahipEposta = "yeni_kullanici" + aracID + "@mail.com"; arac = new Arac(aracID, aracSinifi, sahipAdi, sahipEposta, 0.0); veritabani.aracKaydet(arac); } else if (arac == null && islemTipi.equals("CIKIS")) { return false; } if (islemTipi.equals("GIRIS")) { if (this.suAnIcrideOlanAraclar.contains(arac.getAracID())) { return false; } int yeniGirisID = ++this.sonGirisID; GecisKaydi kayit = new GecisKaydi(yeniGirisID, arac.getAracID(), "GIRIS", LocalDateTime.now(), 0.0, null); veritabani.girisKaydet(kayit); gecisKaydiniDosyayaYaz(kayit); this.suAnIcrideOlanAraclar.add(arac.getAracID()); } else if (islemTipi.equals("CIKIS")) { if (!this.suAnIcrideOlanAraclar.contains(arac.getAracID())) { return false; } double ucret = Ucretlendirme.ucretHesapla(arac.getAracSinifi(), gecilenYerler); arac.ucretTahsilEt(ucret); int yeniCikisID = ++this.sonCikisID; GecisKaydi kayit = new GecisKaydi(yeniCikisID, arac.getAracID(), "CIKIS", LocalDateTime.now(), ucret, gecilenYerler); veritabani.cikisKaydet(kayit); gecisKaydiniDosyayaYaz(kayit); veritabani.kazancEkle(ucret); this.suAnIcrideOlanAraclar.remove(arac.getAracID()); } return true; }
    private void gecisKayitlariniDosyadanYukle() { int girisSayisi = 0; int cikisSayisi = 0; try (BufferedReader br = new BufferedReader(new FileReader(GECIS_KAYIT_DOSYASI))) { String satir; while ((satir = br.readLine()) != null) { if (satir.trim().isEmpty()) continue; String[] p = satir.split(";"); if (p.length == 6) { int islemID = Integer.parseInt(p[0]); LocalDateTime tarih = LocalDateTime.parse(p[1]); String aracID = p[2]; String tip = p[3]; double ucret = Double.parseDouble(p[4]); String[] yollar = p[5].equals("N/A") ? null : p[5].split(","); GecisKaydi kayit = new GecisKaydi(islemID, aracID, tip, tarih, ucret, yollar); if (tip.equals("GIRIS")) { veritabani.girisKaydet(kayit); girisSayisi++; } else { veritabani.cikisKaydet(kayit); cikisSayisi++; } } } System.out.println("Geçmiş geçiş kayıtları yüklendi. (" + girisSayisi + " Giriş, " + cikisSayisi + " Çıkış)"); } catch (FileNotFoundException e) { System.out.println("Geçiş kayıtları dosyası bulunamadı. (İlk çalıştırma)"); } catch (IOException | DateTimeParseException | NumberFormatException e) { System.err.println("Geçiş kayıtları dosyası okunurken hata oluştu: " + e.getMessage()); } }
    private void veritabaniniDosyadanYukle() { try (BufferedReader br = new BufferedReader(new FileReader(ARAC_VERITABANI_DOSYASI))) { String satir; while ((satir = br.readLine()) != null) { if (satir.trim().isEmpty()) continue; String[] parcalar = satir.split(";"); if (parcalar.length == 5) { Arac arac = new Arac(parcalar[0], parcalar[2], parcalar[1], parcalar[3], Double.parseDouble(parcalar[4])); veritabani.aracKaydet(arac); } } System.out.println("Araç veritabanı yüklendi. Kayıtlı araç sayısı: " + veritabani.getToplamAracSayisi()); } catch (FileNotFoundException e) { System.out.println("Araç veritabanı dosyası bulunamadı. Varsayılan veritabanı oluşturuluyor..."); varsayilanVeritabaniOlustur(); } catch (IOException | NumberFormatException e) { System.err.println("Araç veritabanı dosyası okunurken hata oluştu: " + e.getMessage()); } }
    private void varsayilanVeritabaniOlustur() { List<Integer> idListesi = new ArrayList<>(); for (int i = 1; i <= 20; i++) { String aracID = String.valueOf(i); String aracSinifi = Ucretlendirme.getRandomAracSinifi(); String sahipAdi = "Kullanici " + aracID; String sahipEposta = "kullanici" + aracID + "@mail.com"; double bakiye = rastgele.nextDouble() * 500 + 50; veritabani.aracKaydet(new Arac(aracID, aracSinifi, sahipAdi, sahipEposta, bakiye)); } System.out.println("Varsayılan araçlar (ID 1-20) oluşturuldu."); veritabaniniDosyayaKaydet(); }
    public void veritabaniniDosyayaKaydet() { try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARAC_VERITABANI_DOSYASI))) { for (Arac arac : veritabani.getKayitliAraclarMap().values()) { bw.write(arac.toDosyaFormati()); bw.newLine(); } System.out.println("Araç kayıtları tamamlandı."); } catch (IOException e) { System.err.println("Araç veritabanı dosyasına yazılırken HATA oluştu: " + e.getMessage()); } }
    private void gecisKaydiniDosyayaYaz(GecisKaydi kayit) { try (BufferedWriter bw = new BufferedWriter(new FileWriter(GECIS_KAYIT_DOSYASI, true))) { bw.write(kayit.toDosyaFormati()); bw.newLine(); } catch (IOException e) { System.err.println("Geçiş kaydı dosyasına yazılırken HATA oluştu: " + e.getMessage()); } }
    private String[] getRandomRotalar() { int rotaSayisi = rastgele.nextInt(3) + 1; String[] rotalar = new String[rotaSayisi]; for (int i = 0; i < rotaSayisi; i++) { rotalar[i] = ROTA_LISTESI.get(rastgele.nextInt(ROTA_LISTESI.size())); } return rotalar; }
    public void tumAraclariCikisYap() { kalanAraclariCikisYap("Otomatik Sistem Kapanışı Çıkışı"); }
    public Kullanici kullaniciDogrula(String kullaniciAdi, String sifre) { Kullanici kullanici = kullanicilar.get(kullaniciAdi); if (kullanici != null && kullanici.sifreKontrol(sifre)) { return kullanici; } return null; }
    public void sistemiSifirla() { System.out.println("SİSTEM SIFIRLAMA BAŞLATILDI..."); this.veritabani.sifirla(); this.suAnIcrideOlanAraclar.clear(); this.sonGirisID = 0; this.sonCikisID = 0; this.gunSayaci = 0; gecisKayitlariniSifirla(); varsayilanVeritabaniOlustur(); sistemOzetiniKaydet(); System.out.println("SİSTEM SIFIRLAMA TAMAMLANDI."); }
    private void gecisKayitlariniSifirla() { try (BufferedWriter bw = new BufferedWriter(new FileWriter(GECIS_KAYIT_DOSYASI, false))) { bw.write(""); } catch (IOException e) { System.err.println("Geçiş kayıtları dosyası sıfırlanırken HATA oluştu: " + e.getMessage()); } }

} // Sınıfın sonu