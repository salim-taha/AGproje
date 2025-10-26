// KullaniciYonetimEkrani.java
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.Dimension;


public class KullaniciYonetimEkrani extends JDialog {

    private SistemYoneticisi yoneticisi;
    private JTable kullaniciTablosu;
    private DefaultTableModel tabloModeli;

    public KullaniciYonetimEkrani(Frame parent, SistemYoneticisi yoneticisi) {
        super(parent, "Kullanıcı Yönetimi", true);
        this.yoneticisi = yoneticisi;

        setSize(650, 450); // Genişlik yeterli olmalı
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        // --- 1. Kullanıcı Tablosu ---
        String[] sutunBasliklari = {"Seç", "Kullanıcı ID", "İsim"};
        tabloModeli = new DefaultTableModel(sutunBasliklari, 0) {
            @Override public Class<?> getColumnClass(int columnIndex) { if (columnIndex == 0) { return Boolean.class; } return super.getColumnClass(columnIndex); }
            @Override public boolean isCellEditable(int row, int column) { return column == 0; }
        };
        kullaniciTablosu = new JTable(tabloModeli);
        kullaniciTablosu.getTableHeader().setReorderingAllowed(false);
        kullaniciTablosu.setCellSelectionEnabled(true);
        kullaniciTablosu.getColumnModel().getColumn(0).setPreferredWidth(40);
        kullaniciTablosu.getColumnModel().getColumn(0).setMaxWidth(50);
        tabloyuGuncelle();
        JScrollPane scrollPane = new JScrollPane(kullaniciTablosu);
        add(scrollPane, BorderLayout.CENTER);

        // GÜNCELLENDİ: --- 2. Buton Paneli (GridLayout Kullanıldı) ---
        JPanel butonPaneli = new JPanel(new GridLayout(1, 4, 10, 5)); // FlowLayout -> GridLayout
        JButton btnYeniKullanici = new JButton("Yeni Kullanıcı Ekle");
        JButton btnSifreGoster = new JButton("Seçili Şifreleri Göster");
        JButton btnSecilenleriSil = new JButton("Seçili Kullanıcıları Sil");
        JButton btnTumunuSil = new JButton("Tüm Kullanıcıları Sil");

        btnYeniKullanici.setBackground(new Color(0, 150, 0)); btnYeniKullanici.setForeground(Color.WHITE);
        btnSifreGoster.setBackground(Color.CYAN);
        btnSecilenleriSil.setBackground(Color.ORANGE);
        btnTumunuSil.setBackground(new Color(200, 0, 0)); btnTumunuSil.setForeground(Color.WHITE);

        butonPaneli.add(btnYeniKullanici);
        butonPaneli.add(btnSifreGoster);
        butonPaneli.add(btnSecilenleriSil);
        butonPaneli.add(btnTumunuSil);
        add(butonPaneli, BorderLayout.SOUTH);

        // --- Buton Eylemleri (Değişiklik yok) ---
        btnYeniKullanici.addActionListener(e -> { JPasswordField pf = new JPasswordField(); int action = JOptionPane.showConfirmDialog(this, pf, "Yönetici Şifresi Girin:", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE); if (action == JOptionPane.OK_OPTION) { String girilenSifre = new String(pf.getPassword()); if (yoneticisi.yoneticiSifreDogrula(girilenSifre)) { new YeniKullaniciEkrani((Frame) getOwner(), yoneticisi); tabloyuGuncelle(); } else { JOptionPane.showMessageDialog(this, "Yanlış Yönetici Şifresi!", "Yetkilendirme Hatası", JOptionPane.ERROR_MESSAGE); } } });
        btnSifreGoster.addActionListener(e -> { List<String> seciliAdlar = getSeciliKullaniciAdlari(); if (seciliAdlar.isEmpty()) { JOptionPane.showMessageDialog(this, "Lütfen şifresini görmek istediğiniz kullanıcı(lar)ı seçin.", "Seçim Yapılmadı", JOptionPane.WARNING_MESSAGE); return; } JPasswordField pf = new JPasswordField(); int action = JOptionPane.showConfirmDialog(this, pf, seciliAdlar.size() + " KULLANICININ ŞİFRESİNİ GÖRMEK İÇİN YÖNETİCİ ŞİFRESİ:", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE); if (action == JOptionPane.OK_OPTION) { String girilenSifre = new String(pf.getPassword()); if (yoneticisi.yoneticiSifreDogrula(girilenSifre)) { StringBuilder sifreListesi = new StringBuilder("Seçili Kullanıcı Şifreleri:\n\n"); boolean kullaniciBulunamadi = false; for (String kullaniciAdi : seciliAdlar) { Kullanici secilenKullanici = yoneticisi.getKullanici(kullaniciAdi); if (secilenKullanici != null) { sifreListesi.append("- ").append(kullaniciAdi).append(" : ").append(secilenKullanici.getSifre()).append("\n"); } else { sifreListesi.append("- ").append(kullaniciAdi).append(" : (Kullanıcı bulunamadı!)\n"); kullaniciBulunamadi = true; } } JTextArea textArea = new JTextArea(sifreListesi.toString()); textArea.setEditable(false); JScrollPane listScrollPane = new JScrollPane(textArea); listScrollPane.setPreferredSize(new Dimension(350, 150)); JOptionPane.showMessageDialog(this, listScrollPane, "Şifreler Gösterildi", JOptionPane.INFORMATION_MESSAGE); if (kullaniciBulunamadi) { JOptionPane.showMessageDialog(this, "Bazı kullanıcılar bulunamadı!", "Uyarı", JOptionPane.WARNING_MESSAGE); } } else { JOptionPane.showMessageDialog(this, "Yanlış Yönetici Şifresi!", "Yetkilendirme Hatası", JOptionPane.ERROR_MESSAGE); } } });
        btnSecilenleriSil.addActionListener(e -> { List<String> seciliAdlar = getSeciliKullaniciAdlari(); if (seciliAdlar.isEmpty()) { JOptionPane.showMessageDialog(this, "Lütfen silmek istediğiniz kullanıcıları seçin.", "Seçim Yapılmadı", JOptionPane.WARNING_MESSAGE); return; } if (yoneticisi.getToplamKullaniciSayisi() - seciliAdlar.size() < 1) { JOptionPane.showMessageDialog(this, "Bu işlem sonucunda sistemde kullanıcı kalmayacaktır...", "Silme Engellendi", JOptionPane.WARNING_MESSAGE); return; } JPasswordField pf = new JPasswordField(); int action = JOptionPane.showConfirmDialog(this, pf, "Yönetici Şifresi Girin:", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE); if (action == JOptionPane.OK_OPTION) { String girilenSifre = new String(pf.getPassword()); if (yoneticisi.yoneticiSifreDogrula(girilenSifre)) { String kullaniciListesi = String.join("\n- ", seciliAdlar); int cevap = JOptionPane.showConfirmDialog(this, "Aşağıdaki " + seciliAdlar.size() + " kullanıcıyı silmek istediğinize emin misiniz?\n\n- " + kullaniciListesi, "Çoklu Silme Onayı", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE); if (cevap == JOptionPane.YES_OPTION) { int basariliSilme = 0; for (String kullaniciAdi : seciliAdlar) { if (yoneticisi.kullaniciSil(kullaniciAdi)) { basariliSilme++; } } tabloyuGuncelle(); JOptionPane.showMessageDialog(this, basariliSilme + " kullanıcı başarıyla silindi.", "İşlem Tamamlandı", JOptionPane.INFORMATION_MESSAGE); } } else { JOptionPane.showMessageDialog(this, "Yanlış Yönetici Şifresi! Silme işlemi iptal edildi.", "Yetkilendirme Hatası", JOptionPane.ERROR_MESSAGE); } } });
        btnTumunuSil.addActionListener(e -> { JPasswordField pf = new JPasswordField(); int action = JOptionPane.showConfirmDialog(this, pf, "Yönetici Şifresi Girin:", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE); if (action == JOptionPane.OK_OPTION) { String girilenSifre = new String(pf.getPassword()); if (yoneticisi.yoneticiSifreDogrula(girilenSifre)) { int cevap = JOptionPane.showConfirmDialog(this, "DİKKAT! TÜM kullanıcılar silinecek. Emin misiniz?", "SON ONAY", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE); if (cevap == JOptionPane.YES_OPTION) { yoneticisi.tumKullanicilariSil(); tabloyuGuncelle(); JOptionPane.showMessageDialog(this, "Tüm kullanıcılar başarıyla silindi.", "İşlem Başarılı", JOptionPane.INFORMATION_MESSAGE); } } else { JOptionPane.showMessageDialog(this, "Yanlış Yönetici Şifresi! Silme işlemi iptal edildi.", "Yetkilendirme Hatası", JOptionPane.ERROR_MESSAGE); } } });

        setVisible(true);
    }

    // Seçili olan (tick'li) TÜM kullanıcı adlarını bulur.
    private List<String> getSeciliKullaniciAdlari() {
        List<String> seciliAdlar = new ArrayList<>();
        for (int i = 0; i < tabloModeli.getRowCount(); i++) {
            Boolean seciliMi = (Boolean) tabloModeli.getValueAt(i, 0);
            if (seciliMi != null && seciliMi) {
                seciliAdlar.add((String) tabloModeli.getValueAt(i, 1));
            }
        }
        return seciliAdlar;
    }

    // Tabloyu güncelleyen metot
    private void tabloyuGuncelle() {
        tabloModeli.setRowCount(0);
        Object[][] veri = yoneticisi.getKullaniciListesiVerisi();
        for (Object[] satir : veri) {
            tabloModeli.addRow(satir);
        }
    }
}