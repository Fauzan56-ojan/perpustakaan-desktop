# Testing QA Report

## Versi
v1

## Deskripsi Bug
Pada fitur peminjaman buku, aplikasi mengalami error ketika pengguna menekan tombol Pinjam Buku. Sistem mencoba memuat file icon untuk menampilkan pesan notifikasi, namun file icon tidak ditemukan sehingga menyebabkan NullPointerException dan proses peminjaman gagal.

## Langkah Reproduksi
1. Jalankan aplikasi  
2. Masuk ke menu Peminjaman Buku
3. Pilih buku yang akan dipinjam  
4. Klik tombol Pinjam Buku

## Hasil Aktual
Aplikasi mengalami error NullPointerException dan proses peminjaman buku tidak dapat dilanjutkan.

## Hasil yang Diharapkan
Sistem berhasil memproses peminjaman buku dan menampilkan pesan notifikasi keberhasilan tanpa mengalami error.

## Solusi
- Memastikan file icon tersedia di dalam folder resource project  
- Menggunakan metode getResource() dengan path yang benar  
- Menambahkan pengecekan null sebelum memuat icon  
- Alternatif: menampilkan pesan tanpa icon untuk mencegah aplikasi crash
