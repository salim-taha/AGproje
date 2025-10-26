// AnaPanel.java
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AnaPanel extends JFrame {

    private SistemYoneticisi yonetici;
    private Kullanici kullanici;
    
    private JLabel lblKazanc;
    private JLabel lblAracSayisi;
    private JButton btnSimulasyon;
    private JLabel lblGunSayaci; 
    private JPanel ozetPanel;

    public AnaPanel(Kullanici kullanici, SistemYoneticisi yonetici) {
        this.kullanici = kullanici;
        this.yonetici = yonetici;
        
        setTitle("HGS Yönetim Paneli - Ana Ekran");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); 
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // --- 1. Üst Panel (Değişiklik yok) ---
        JPanel ustPanel = new JPanel(new BorderLayout(15, 10));
        JLabel lblWelcome = new JLabel("  Hoşgeldiniz, " + kullanici.getIsim());
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 14));
        ustPanel.add(lblWelcome, BorderLayout.WEST);
        JPanel simulasyonKontrolPaneli = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        lblGunSayaci = new JLabel("Yükleniyor..."); lblGunSayaci.setFont(new Font("Arial", Font.BOLD, 14));
        simulasyonKontrolPaneli.add(lblGunSayaci);
        btnSimulasyon = new JButton("Yükleniyor..."); btnSimulasyon.setBackground(new Color(0, 150, 0)); btnSimulasyon.setForeground(Color.WHITE);
        simulasyonKontrolPaneli.add(btnSimulasyon);
        ustPanel.add(simulasyonKontrolPaneli, BorderLayout.EAST);
        add(ustPanel, BorderLayout.NORTH);

        // GÜNCELLENDİ: 2. Orta Panel (5 butona yer açmak için GridLayout değişti)
        JPanel butonPaneli = new JPanel(new GridLayout(5, 1, 10, 10)); // 4 -> 5
        butonPaneli.setBorder(BorderFactory.createTitledBorder("İşlemler"));
        
        JButton btnGirisler = new JButton("Tüm Girişleri Göster");
        JButton btnCikislar = new JButton("Tüm Çıkışları Göster");
        JButton btnAraclar = new JButton("Tüm Kayıtlı Araçlar");
        JButton btnKullanicilar = new JButton("Kullanıcıları Yönet"); // YENİ BUTON
        JButton btnTarife = new JButton("Fiyatlandırma Politikası");
        
        butonPaneli.add(btnGirisler); 
        butonPaneli.add(btnCikislar);
        butonPaneli.add(btnAraclar);
        butonPaneli.add(btnKullanicilar); // YENİ BUTONU EKLE
        butonPaneli.add(btnTarife);
        add(butonPaneli, BorderLayout.CENTER);

        // --- 3. Alt Panel (Değişiklik yok) ---
        ozetPanel = new JPanel(new BorderLayout(10, 10)); 
        ozetPanel.setBorder(BorderFactory.createTitledBorder("Sistem Özeti"));
        lblKazanc = new JLabel("Yükleniyor..."); lblKazanc.setFont(new Font("Arial", Font.BOLD, 16)); lblKazanc.setHorizontalAlignment(JLabel.CENTER);
        lblAracSayisi = new JLabel("Yükleniyor..."); lblAracSayisi.setFont(new Font("Arial", Font.BOLD, 16)); lblAracSayisi.setHorizontalAlignment(JLabel.CENTER);
        JButton btnReset = new JButton("Sistemi Sıfırla (Reset)"); btnReset.setBackground(new Color(200, 0, 0)); btnReset.setForeground(Color.WHITE);
        ozetPanel.add(lblKazanc, BorderLayout.WEST); ozetPanel.add(lblAracSayisi, BorderLayout.CENTER); ozetPanel.add(btnReset, BorderLayout.EAST); 
        add(ozetPanel, BorderLayout.SOUTH);
        
        paneliGuncelle(); // Başlangıç verilerini yükle

        // --- 4. Buton Eylemleri ---
        
        // Reset butonu (Değişiklik yok)
        btnReset.addActionListener(e -> {
            int cevap = JOptionPane.showConfirmDialog(this, "TÜM VERİLER SİLİNECEK! Emin misiniz?", "SİSTEM SIFIRLAMA ONAYI", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (cevap == JOptionPane.YES_OPTION) { yonetici.sistemiSifirla(); paneliGuncelle(); JOptionPane.showMessageDialog(this, "Sistem başarıyla sıfırlandı.", "Sıfırlama Tamamlandı", JOptionPane.INFORMATION_MESSAGE); }
        });

        // Start butonu (Değişiklik yok)
        btnSimulasyon.addActionListener(e -> {
            int[] sonuclar = yonetici.gunlukSimulasyonuBaslat(); int yapilanGiris = sonuclar[0];
            paneliGuncelle(); String mesaj = String.format("Gün %d simülasyonu tamamlandı.\n\nBaşarılı Giriş İşlemi: %d\n\nYeni verileri görmek için tablo butonlarını kullanabilirsiniz.", yonetici.getGunSayaci(), yapilanGiris );
            JOptionPane.showMessageDialog(this, mesaj, "Simülasyon Tamamlandı", JOptionPane.INFORMATION_MESSAGE);
        });
        
        // YENİ: Kullanıcıları Yönet Butonu Eylemi
        btnKullanicilar.addActionListener(e -> {
            new KullaniciYonetimEkrani(this, yonetici); // Yeni ekranı aç
        });

        // Diğer buton eylemleri (Değişiklik yok)
        btnAraclar.addActionListener(e -> gosterTabloDialog("Tüm Kayıtlı Araçlar", new String[]{"Araç ID", "Sahip Adı", "Araç Sınıfı", "Bakiye (TL)"}, yonetici.getVeritabani().getAracListesiVerisi()));
        btnGirisler.addActionListener(e -> gosterTabloDialog("Tüm Giriş Kayıtları", new String[]{"İşlem ID", "Araç ID", "İşlem Tipi", "Tarih/Saat", "Kullanılan Yollar"}, yonetici.getVeritabani().getGirisListesiVerisi()));
        btnCikislar.addActionListener(e -> gosterTabloDialog("Tüm Çıkışları Göster", new String[]{"İşlem ID", "Araç ID", "İşlem Tipi", "Tarih/Saat", "Kesilen Ücret (TL)", "Kullanılan Yollar"}, yonetici.getVeritabani().getCikisListesiVerisi()));
        btnTarife.addActionListener(e -> { JDialog d = new JDialog(this, "Fiyatlandırma Politikası", true); d.setSize(450, 300); d.setLocationRelativeTo(this); JTextArea t = new JTextArea(Ucretlendirme.getTarifeMetni()); t.setFont(new Font("Monospaced", Font.PLAIN, 12)); t.setEditable(false); d.add(new JScrollPane(t)); d.setVisible(true); });

        // GÜNCELLENDİ: Kapanışta 'kullaniciVeritabaniKaydet' de çağrılıyor
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                yonetici.tumAraclariCikisYap();
                yonetici.veritabaniniDosyayaKaydet(); // Araçları kaydet
                yonetici.sistemOzetiniKaydet();    // Özeti kaydet
                yonetici.kullaniciVeritabaniKaydet(); // YENİ: Kullanıcıları kaydet
                System.exit(0);
            }
        });
        
        setVisible(true);
    }
    
    // paneliGuncelle (Değişiklik yok)
    private void paneliGuncelle() { double toplamKazanc = yonetici.getVeritabani().getToplamKazanc(); int toplamArac = yonetici.getVeritabani().getToplamAracSayisi(); int guncelGun = yonetici.getGunSayaci(); lblKazanc.setText(String.format("Toplam Kazanç: %.2f TL", toplamKazanc)); lblAracSayisi.setText("Toplam Kayıtlı Araç: " + toplamArac); lblGunSayaci.setText("Sistem Günü: " + guncelGun); btnSimulasyon.setText("Simülasyonu Çalıştır (Gün " + (guncelGun + 1) + ")"); }
    
    // gosterTabloDialog (Değişiklik yok)
    private void gosterTabloDialog(String baslik, String[] sutunBasliklari, Object[][] veri) { JDialog dialog = new JDialog(this, baslik, true); dialog.setSize(750, 400); dialog.setLocationRelativeTo(this); JTable tablo = new JTable(veri, sutunBasliklari); tablo.setEnabled(false); tablo.getTableHeader().setReorderingAllowed(false); JScrollPane scrollPane = new JScrollPane(tablo); dialog.add(scrollPane); dialog.setVisible(true); }
}