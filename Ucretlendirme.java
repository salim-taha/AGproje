// Ucretlendirme.java
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Arrays;
import java.util.Random;

public class Ucretlendirme {

    // YENİ: Rastgele araç sınıfı seçmek için bir liste
    public static final List<String> ARAC_SINIFLARI = Arrays.asList(
            "Otomobil", "Minibüs", "Kamyon", "Tır", "Motor"
    );

    // GÜNCELLENDİ: Key (anahtar) artık Integer değil, String
    private static final Map<String, Map<String, Double>> UCRET_TARIFESI = new HashMap<>();

    static {
        // Otomobil (Eski Sınıf 1)
        Map<String, Double> otomobilTarife = new HashMap<>();
        otomobilTarife.put("Avrasya Tuneli", 80.0);
        otomobilTarife.put("Osmangazi Koprusu", 180.0);
        otomobilTarife.put("Otoyol", 15.0);
        UCRET_TARIFESI.put("Otomobil", otomobilTarife);

        // Minibüs (Eski Sınıf 2)
        Map<String, Double> minibusTarife = new HashMap<>();
        minibusTarife.put("Avrasya Tuneli", 120.0);
        minibusTarife.put("Osmangazi Koprusu", 290.0);
        minibusTarife.put("Otoyol", 25.0);
        UCRET_TARIFESI.put("Minibüs", minibusTarife);

        // Kamyon (Eski Sınıf 3)
        Map<String, Double> kamyonTarife = new HashMap<>();
        kamyonTarife.put("Avrasya Tuneli", 150.0);
        kamyonTarife.put("Osmangazi Koprusu", 350.0);
        kamyonTarife.put("Otoyol", 40.0);
        UCRET_TARIFESI.put("Kamyon", kamyonTarife);
        
        // YENİ: Tır
        Map<String, Double> tirTarife = new HashMap<>();
        tirTarife.put("Avrasya Tuneli", 220.0); // Tahmini
        tirTarife.put("Osmangazi Koprusu", 500.0); // Tahmini
        tirTarife.put("Otoyol", 60.0); // Tahmini
        UCRET_TARIFESI.put("Tır", tirTarife);
        
        // YENİ: Motor
        Map<String, Double> motorTarife = new HashMap<>();
        motorTarife.put("Avrasya Tuneli", 40.0); // Tahmini (%50 otomobil)
        motorTarife.put("Osmangazi Koprusu", 90.0); // Tahmini
        motorTarife.put("Otoyol", 7.5); // Tahmini
        UCRET_TARIFESI.put("Motor", motorTarife);
    }

    // GÜNCELLENDİ: Parametre artık 'int' değil 'String'
    public static double ucretHesapla(String aracSinifi, String[] gecilenYerler) {
        double toplamUcret = 0;
        // getOrDefault, eğer o araç sınıfı yoksa boş bir harita döner, hata vermez
        Map<String, Double> tarife = UCRET_TARIFESI.getOrDefault(aracSinifi, new HashMap<>());
        
        for (String yer : gecilenYerler) {
            toplamUcret += tarife.getOrDefault(yer, 0.0);
        }
        return toplamUcret;
    }
    
    // YENİ: Rastgele bir araç sınıfı döndüren yardımcı metot
    public static String getRandomAracSinifi() {
        Random rand = new Random();
        return ARAC_SINIFLARI.get(rand.nextInt(ARAC_SINIFLARI.size()));
    }

    // GÜNCELLENDİ: Tarife metnini yeni String key'lere göre hazırlar
    public static String getTarifeMetni() {
        StringBuilder sb = new StringBuilder();
        sb.append("--- HGS FİYATLANDIRMA TARİFESİ ---\n\n");
        
        for (String sinif : UCRET_TARIFESI.keySet()) {
            sb.append(String.format("ARAÇ SINIFI: %s\n", sinif.toUpperCase()));
            Map<String, Double> tarife = UCRET_TARIFESI.get(sinif);
            for (String yer : tarife.keySet()) {
                sb.append(String.format("  - %-20s : %.2f TL\n", yer, tarife.get(yer)));
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}