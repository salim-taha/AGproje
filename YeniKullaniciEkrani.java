// YeniKullaniciEkrani.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YeniKullaniciEkrani extends JDialog {

    private JTextField txtKullaniciAdi;
    private JPasswordField txtSifre;
    private JTextField txtIsim;
    private JButton btnOlustur;
    private SistemYoneticisi yonetici;

    private static final Pattern ISIM_PATTERN = Pattern.compile("^[a-zA-ZçÇğĞıİöÖşŞüÜ ]+$");

    public YeniKullaniciEkrani(Frame parent, SistemYoneticisi yonetici) {
        super(parent, "Yeni Kullanıcı Oluştur", true);
        this.yonetici = yonetici;

        setSize(450, 250); // Genişliği 450'ye geri çektim, HTML ile sığıyor
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        // --- Orta Panel: Giriş Alanları ---
        JPanel formPaneli = new JPanel(new GridLayout(3, 2, 10, 10));
        formPaneli.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        formPaneli.add(new JLabel("<html>Yeni Kullanıcı ID (Min 5, Max 10 Rakam):</html>"));
        txtKullaniciAdi = new JTextField();
        formPaneli.add(txtKullaniciAdi);

        // GÜNCELLENDİ: Etiket metni güncellendi (Min 5)
        formPaneli.add(new JLabel("Yeni Şifre (Min 5 Karakter):"));
        txtSifre = new JPasswordField();
        formPaneli.add(txtSifre);

        formPaneli.add(new JLabel("Kullanıcının Tam Adı:"));
        txtIsim = new JTextField();
        formPaneli.add(txtIsim);

        add(formPaneli, BorderLayout.CENTER);

        // --- Alt Panel: Buton ---
        JPanel butonPaneli = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnOlustur = new JButton("Kullanıcıyı Oluştur");
        butonPaneli.add(btnOlustur);
        add(butonPaneli, BorderLayout.SOUTH);

        // GÜNCELLENDİ: Buton Eylemi (Minimum Şifre Uzunluğu Kontrolü Eklendi)
        btnOlustur.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String kullaniciAdi = txtKullaniciAdi.getText().trim();
                String sifre = new String(txtSifre.getPassword()); // Şifreyi alırken değişiklik yok
                String isim = txtIsim.getText().trim();

                // --- KULLANICI ID KONTROLLERİ ---
                if (kullaniciAdi.isEmpty()) { JOptionPane.showMessageDialog(YeniKullaniciEkrani.this, "Kullanıcı ID boş bırakılamaz!", "Giriş Hatası", JOptionPane.WARNING_MESSAGE); return; }
                if (!kullaniciAdi.matches("\\d+")) { JOptionPane.showMessageDialog(YeniKullaniciEkrani.this, "Kullanıcı ID sadece rakamlardan oluşmalıdır!", "Giriş Hatası", JOptionPane.WARNING_MESSAGE); return; }
                if (kullaniciAdi.length() < 5) { JOptionPane.showMessageDialog(YeniKullaniciEkrani.this, "Kullanıcı ID en az 5 rakam olmalıdır!", "Giriş Hatası", JOptionPane.WARNING_MESSAGE); return; }
                if (kullaniciAdi.length() > 10) { JOptionPane.showMessageDialog(YeniKullaniciEkrani.this, "Kullanıcı ID en fazla 10 rakam olabilir!", "Giriş Hatası", JOptionPane.WARNING_MESSAGE); return; }

                // --- ŞİFRE KONTROLLERİ ---
                 if (sifre.isEmpty()) { JOptionPane.showMessageDialog(YeniKullaniciEkrani.this, "Şifre boş bırakılamaz!", "Giriş Hatası", JOptionPane.WARNING_MESSAGE); return; }
                 // YENİ KONTROL: Minimum şifre uzunluğu
                 if (sifre.length() < 5) {
                      JOptionPane.showMessageDialog(YeniKullaniciEkrani.this,
                            "Şifre en az 5 karakter uzunluğunda olmalıdır!", "Giriş Hatası", JOptionPane.WARNING_MESSAGE);
                     return;
                 }

                // --- İSİM KONTROLLERİ ---
                 if (isim.isEmpty()) { JOptionPane.showMessageDialog(YeniKullaniciEkrani.this, "Kullanıcının tam adı boş bırakılamaz!", "Giriş Hatası", JOptionPane.WARNING_MESSAGE); return; }
                 if (isim.length() > 50) { JOptionPane.showMessageDialog(YeniKullaniciEkrani.this, "Kullanıcının tam adı en fazla 50 karakter olabilir!", "Giriş Hatası", JOptionPane.WARNING_MESSAGE); return; }
                 Matcher matcher = ISIM_PATTERN.matcher(isim);
                 if (!matcher.matches()) { JOptionPane.showMessageDialog(YeniKullaniciEkrani.this, "Kullanıcının tam adı sadece harf ve boşluk içerebilir!", "Giriş Hatası", JOptionPane.WARNING_MESSAGE); return; }
                // --- KONTROLLER SONU ---

                // Kontrolleri geçtiyse, yöneticiye kaydetmeyi dene
                boolean sonuc = yonetici.kullaniciKaydet(kullaniciAdi, sifre, isim);

                if (sonuc) {
                    JOptionPane.showMessageDialog(YeniKullaniciEkrani.this,
                            "Kullanıcı '" + kullaniciAdi + "' başarıyla oluşturuldu.", "Kayıt Başarılı",
                            JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(YeniKullaniciEkrani.this,
                            "Kullanıcı oluşturulamadı!\nBu Kullanıcı ID zaten mevcut olabilir.",
                            "Kayıt Hatası", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        setVisible(true);
    }
}