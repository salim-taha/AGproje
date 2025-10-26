// GirisEkrani.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
// --- EKSİK OLAN IMPORT'LAR BURAYA EKLENDİ ---
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
// --- ---

public class GirisEkrani extends JFrame {

    private JTextField idAlani;
    private JPasswordField sifreAlani;
    private JButton girisButonu;
    private JButton kayitButonu;
    private SistemYoneticisi yonetici;

    private static final String ID_PLACEHOLDER = "Kullanıcı ID Girin";
    private static final String SIFRE_PLACEHOLDER = "Şifre Girin";
    private static final Color PLACEHOLDER_COLOR = Color.GRAY;
    private Color normalIdForeColor;
    private Color normalSifreForeColor;
    private char defaultEchoChar;

    public GirisEkrani() {
        yonetici = new SistemYoneticisi();

        setTitle("HGS Yönetim Paneli - Giriş");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // --- Orta Panel: Giriş Alanları ---
        JPanel girisPaneli = new JPanel(new GridLayout(2, 2, 10, 10));
        girisPaneli.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        girisPaneli.add(new JLabel("Kullanıcı ID:"));
        idAlani = new JTextField();
        normalIdForeColor = idAlani.getForeground();
        idAlani.setText(ID_PLACEHOLDER);
        idAlani.setForeground(PLACEHOLDER_COLOR);
        girisPaneli.add(idAlani);

        girisPaneli.add(new JLabel("Şifre:"));
        sifreAlani = new JPasswordField();
        normalSifreForeColor = sifreAlani.getForeground();
        defaultEchoChar = sifreAlani.getEchoChar();
        sifreAlani.setEchoChar((char) 0);
        sifreAlani.setText(SIFRE_PLACEHOLDER);
        sifreAlani.setForeground(PLACEHOLDER_COLOR);
        girisPaneli.add(sifreAlani);

        add(girisPaneli, BorderLayout.CENTER);

        // --- Alt Panel: Butonlar ---
        JPanel butonPaneli = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        girisButonu = new JButton("Giriş Yap");
        kayitButonu = new JButton("Yeni Kullanıcı Kaydet");
        butonPaneli.add(girisButonu);
        butonPaneli.add(kayitButonu);
        add(butonPaneli, BorderLayout.SOUTH);

        // --- Focus Listener'ları Ekle ---
        idAlani.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (idAlani.getText().equals(ID_PLACEHOLDER)) {
                    idAlani.setText("");
                    idAlani.setForeground(normalIdForeColor);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (idAlani.getText().isEmpty()) {
                    idAlani.setForeground(PLACEHOLDER_COLOR);
                    idAlani.setText(ID_PLACEHOLDER);
                }
            }
        });
        sifreAlani.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                String currentText = new String(sifreAlani.getPassword());
                if (currentText.equals(SIFRE_PLACEHOLDER)) {
                    sifreAlani.setText("");
                    sifreAlani.setEchoChar(defaultEchoChar);
                    sifreAlani.setForeground(normalSifreForeColor);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (new String(sifreAlani.getPassword()).isEmpty()) {
                    sifreAlani.setForeground(PLACEHOLDER_COLOR);
                    sifreAlani.setEchoChar((char) 0);
                    sifreAlani.setText(SIFRE_PLACEHOLDER);
                }
            }
        });

        // --- Buton Eylemleri ---
        girisButonu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = idAlani.getText();
                String sifre = new String(sifreAlani.getPassword());
                if (id.equals(ID_PLACEHOLDER) || sifre.equals(SIFRE_PLACEHOLDER)) {
                     JOptionPane.showMessageDialog(GirisEkrani.this, "Lütfen ID ve Şifre girin!",
                            "Giriş Hatası", JOptionPane.WARNING_MESSAGE);
                     return;
                }
                Kullanici kullanici = yonetici.kullaniciDogrula(id, sifre);
                if (kullanici != null) {
                    dispose();
                    new AnaPanel(kullanici, yonetici);
                } else {
                    JOptionPane.showMessageDialog(GirisEkrani.this, "Geçersiz ID veya Şifre!",
                            "Giriş Hatası", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        kayitButonu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPasswordField pf = new JPasswordField();
                int action = JOptionPane.showConfirmDialog(GirisEkrani.this, pf,
                                "Yeni Kullanıcı Oluşturmak İçin Yönetici Şifresi:",
                                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (action == JOptionPane.OK_OPTION) {
                    String girilenSifre = new String(pf.getPassword());
                    if (yonetici.yoneticiSifreDogrula(girilenSifre)) {
                        new YeniKullaniciEkrani(GirisEkrani.this, yonetici);
                    } else {
                        JOptionPane.showMessageDialog(GirisEkrani.this,
                                "Yanlış Yönetici Şifresi!",
                                "Yetkilendirme Hatası",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GirisEkrani();
            }
        });
    }
}