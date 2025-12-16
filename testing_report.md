# Testing QA Report

## Project
Aplikasi Perpustakaan Java

## Version
v1

## Deskripsi Bug
Pada fitur peminjaman buku, sistem tidak melakukan pengecekan ketersediaan stok buku sehingga buku dengan stok 0 masih dapat dipinjam.

## Hasil Aktual
Peminjaman berhasil meskipun stok buku tidak tersedia.

## Hasil yang Diharapkan
Sistem menolak peminjaman dan menampilkan pesan "Buku tidak tersedia".

## Solusi
Menambahkan validasi stok buku sebelum proses peminjaman dilakukan.
