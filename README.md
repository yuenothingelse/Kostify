# Kostify
System Management Boarding House

mak## Menjalankan Proyek

Gunakan perintah berikut untuk menyiapkan database, mengunduh dependensi, serta mengompilasi dan menjalankan aplikasi **Kostify**:

| Perintah | Fungsi |
| :--- | :--- |
| `make db-up` | Menyalakan atau membuat *container* MySQL Docker `kostify-mysql`. |
| `make deps` | Mengunduh dependensi `.jar` yang dibutuhkan (jika belum ada). |
| `make compile` | Mengompilasi seluruh file `.java` dan menyalin `src/main/resources`. |
| `make run` | Otomatis `compile` lalu menjalankan `com.kostify.MainApp`. |
| `make clean` | Membersihkan folder `out/` sebelum *zip* atau *push* ke GitHub. |
