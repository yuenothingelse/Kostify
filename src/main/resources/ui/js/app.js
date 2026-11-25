const state = {
  screen: "splash",
  kamarFilter: "Semua",
  penghuniFilter: "Semua",
  pembayaranFilter: "Semua",
  kamarCache: [],
  penghuniCache: [],
  pembayaranCache: []
};

function formatRupiah(num) {
  const n = Number(num || 0);
  return "Rp " + n.toLocaleString("id-ID");
}

function showToast(msg) {
  const el = document.getElementById("toast");
  el.textContent = msg;
  el.classList.remove("hidden");
  clearTimeout(window.__toastTimer);
  window.__toastTimer = setTimeout(() => el.classList.add("hidden"), 2400);
}

function requestApi(endpoint, params = {}) {
  const qs = new URLSearchParams();
  Object.entries(params).forEach(([k, v]) => {
    if (v !== undefined && v !== null) qs.set(k, String(v));
  });
  const url = `/api/${endpoint}${qs.toString() ? "?" + qs.toString() : ""}`;
  const xhr = new XMLHttpRequest();
  xhr.open("GET", url, false);
  xhr.send(null);
  if (xhr.status < 200 || xhr.status >= 300) {
    throw new Error(`HTTP ${xhr.status} saat mengakses ${endpoint}`);
  }
  const parsed = JSON.parse(xhr.responseText);
  if (!parsed.ok) {
    throw new Error(parsed.error || "Operasi gagal.");
  }
  return parsed.data;
}

function callBridge(method, ...args) {
  switch (method) {
    case "login":
      return requestApi("login", { email: args[0], password: args[1] });
    case "logout":
      return requestApi("logout");
    case "getDashboardData":
      return requestApi("dashboard");
    case "getDaftarKamar":
      return requestApi("kamar/list", { status: args[0], keyword: args[1] });
    case "tambahKamar":
      return requestApi("kamar/tambah", { nomor: args[0], tipe: args[1], harga: args[2] });
    case "ubahKamar":
      return requestApi("kamar/ubah", { id: args[0], nomor: args[1], tipe: args[2], harga: args[3], status: args[4] });
    case "hapusKamar":
      return requestApi("kamar/hapus", { id: args[0] });
    case "getDaftarPenghuni":
      return requestApi("penghuni/list", { status: args[0], keyword: args[1] });
    case "tambahPenghuni":
      return requestApi("penghuni/tambah", {
        nama: args[0], noHp: args[1], alamat: args[2],
        idKamar: args[3], tanggalMasuk: args[4], tanggalKeluar: args[5]
      });
    case "ubahPenghuni":
      return requestApi("penghuni/ubah", {
        id: args[0], nama: args[1], noHp: args[2], alamat: args[3],
        idKamar: args[4], tanggalMasuk: args[5], tanggalKeluar: args[6], statusAktif: args[7]
      });
    case "keluarPenghuni":
      return requestApi("penghuni/keluar", { id: args[0], tanggalKeluar: args[1] });
    case "hapusPenghuni":
      return requestApi("penghuni/hapus", { id: args[0] });
    case "getDaftarPembayaran":
      return requestApi("pembayaran/list", { status: args[0], keyword: args[1] });
    case "bayarTagihan":
      return requestApi("pembayaran/bayar", { id: args[0], tanggalBayar: args[1] });
    default:
      throw new Error("Metode tidak dikenal: " + method);
  }
}

function navigateTo(target) {
  document.querySelectorAll(".screen").forEach((s) => s.classList.remove("active"));
  const screenEl = document.getElementById(`screen-${target}`);
  if (screenEl) screenEl.classList.add("active");
  state.screen = target;

  const showNav = !["splash", "login"].includes(target);
  document.getElementById("bottomNav").classList.toggle("hidden", !showNav);

  document.querySelectorAll(".nav-item").forEach((btn) => {
    btn.classList.toggle("active", btn.dataset.nav === target);
  });

  if (target === "beranda") loadDashboard();
  if (target === "kamar") loadKamar();
  if (target === "penghuni") loadPenghuni();
  if (target === "pembayaran") loadPembayaran();
}

function loadDashboard() {
  try {
    const d = callBridge("getDashboardData");
    document.getElementById("adminGreeting").textContent = `${d.adminNama} 👋`;
    document.getElementById("kpiTotalKamar").textContent = d.totalKamar;
    document.getElementById("kpiKamarTerisi").textContent = d.kamarTerisi;
    document.getElementById("kpiKamarKosong").textContent = d.kamarKosong;
    document.getElementById("kpiPendapatan").textContent = formatRupiah(d.totalPendapatan);
    document.getElementById("kpiTunggakan").textContent = formatRupiah(d.totalTunggakan);

    const listEl = document.getElementById("dashboardKamarList");
    const items = d.previewKamar || [];
    if (!items.length) {
      listEl.innerHTML = `<p class="data-card-meta">Belum ada data kamar.</p>`;
      return;
    }
    listEl.innerHTML = items.map((k) => {
      const badgeClass = k.status === "Terisi" ? "terisi" : "kosong";
      const penghuniLabel = k.status === "Terisi" ? (k.namaPenghuniAktif || "Terisi") : "-";
      return `
        <div class="room-row" onclick="navigateTo('kamar')">
          <div>
            <strong>${k.nomor}</strong>
            <span style="margin-left:10px;color:#64748b;font-size:13px;">${penghuniLabel}</span>
          </div>
          <span class="badge ${badgeClass}">${k.status}</span>
        </div>
      `;
    }).join("");
  } catch (err) {
    showToast(err.message);
  }
}

function loadKamar() {
  try {
    const q = document.getElementById("searchKamar").value || "";
    const list = callBridge("getDaftarKamar", state.kamarFilter, q);
    state.kamarCache = list;

    const container = document.getElementById("kamarListContainer");
    if (!list.length) {
      container.innerHTML = `<div class="data-card"><span class="data-card-meta">Tidak ada kamar sesuai filter.</span></div>`;
      return;
    }

    container.innerHTML = list.map((k) => {
      const badgeClass = k.status === "Terisi" ? "terisi" : "kosong";
      return `
        <div class="data-card">
          <div class="data-card-top">
            <div>
              <strong>Kamar ${k.nomor}</strong>
              <div class="data-card-meta">${k.tipe} &bull; <b>${formatRupiah(k.harga)}</b>/bln</div>
              <div class="data-card-meta">Penghuni: ${k.namaPenghuniAktif || "-"}</div>
            </div>
            <span class="badge ${badgeClass}">${k.status}</span>
          </div>
          <div class="data-card-actions">
            <button type="button" class="btn-secondary" onclick="openEditKamar(${k.idKamar})">Ubah</button>
            <button type="button" class="btn-danger" onclick="hapusKamar(${k.idKamar})">Hapus</button>
          </div>
        </div>
      `;
    }).join("");
  } catch (err) {
    showToast(err.message);
  }
}

function openTambahKamar() {
  document.getElementById("modalKamarTitle").textContent = "Tambah Kamar";
  document.getElementById("kamarId").value = "";
  document.getElementById("kamarNomor").value = "";
  document.getElementById("kamarTipe").value = "";
  document.getElementById("kamarHarga").value = "";
  document.getElementById("kamarStatusGroup").classList.add("hidden");
  document.getElementById("modalKamar").classList.remove("hidden");
}

function openEditKamar(id) {
  const k = state.kamarCache.find((item) => item.idKamar === id);
  if (!k) return;
  document.getElementById("modalKamarTitle").textContent = `Ubah Kamar ${k.nomor}`;
  document.getElementById("kamarId").value = k.idKamar;
  document.getElementById("kamarNomor").value = k.nomor;
  document.getElementById("kamarTipe").value = k.tipe;
  document.getElementById("kamarHarga").value = Number(k.harga);
  document.getElementById("kamarStatus").value = k.status;
  document.getElementById("kamarStatusGroup").classList.remove("hidden");
  document.getElementById("modalKamar").classList.remove("hidden");
}

function hapusKamar(id) {
  try {
    callBridge("hapusKamar", id);
    showToast("Data kamar dihapus.");
    loadKamar();
  } catch (err) {
    showToast(err.message);
  }
}

function populateKamarSelect(selectedIdKamar, includeCurrentId) {
  const kamars = callBridge("getDaftarKamar", "Semua", "");
  const select = document.getElementById("penghuniKamar");
  const available = kamars.filter((k) => k.status === "Kosong" || k.idKamar === includeCurrentId);
  select.innerHTML = available.map((k) =>
    `<option value="${k.idKamar}" ${k.idKamar === selectedIdKamar ? "selected" : ""}>${k.nomor} — ${k.tipe} (${k.status})</option>`
  ).join("");
}

function loadPenghuni() {
  try {
    const q = document.getElementById("searchPenghuni").value || "";
    const list = callBridge("getDaftarPenghuni", state.penghuniFilter, q);
    state.penghuniCache = list;

    const container = document.getElementById("penghuniListContainer");
    if (!list.length) {
      container.innerHTML = `<div class="data-card"><span class="data-card-meta">Tidak ada data penghuni.</span></div>`;
      return;
    }

    container.innerHTML = list.map((p) => {
      const badgeClass = p.statusAktif === "Aktif" ? "aktif" : "keluar";
      return `
        <div class="data-card">
          <div class="data-card-top">
            <div>
              <strong>${p.nama}</strong>
              <div class="data-card-meta">Kamar: <b>${p.noKamar || "-"}</b> &bull; HP: ${p.noHp}</div>
              <div class="data-card-meta">Masuk: ${p.tanggalMasuk || "-"}${p.tanggalKeluar ? ` &bull; Keluar: ${p.tanggalKeluar}` : ""}</div>
              <div class="data-card-meta">${p.alamat}</div>
            </div>
            <span class="badge ${badgeClass}">${p.statusAktif}</span>
          </div>
          <div class="data-card-actions">
            <button type="button" class="btn-secondary" onclick="openEditPenghuni(${p.idPenghuni})">Ubah</button>
            ${p.statusAktif === "Aktif"
              ? `<button type="button" class="btn-secondary" onclick="keluarPenghuni(${p.idPenghuni})">Keluar</button>`
              : ""}
            <button type="button" class="btn-danger" onclick="hapusPenghuni(${p.idPenghuni})">Hapus</button>
          </div>
        </div>
      `;
    }).join("");
  } catch (err) {
    showToast(err.message);
  }
}

function openTambahPenghuni() {
  try {
    populateKamarSelect(null, null);
    document.getElementById("modalPenghuniTitle").textContent = "Tambah Penghuni";
    document.getElementById("penghuniId").value = "";
    document.getElementById("penghuniNama").value = "";
    document.getElementById("penghuniNoHp").value = "";
    document.getElementById("penghuniAlamat").value = "";
    document.getElementById("penghuniMasuk").value = new Date().toISOString().slice(0, 10);
    document.getElementById("penghuniKeluar").value = "";
    document.getElementById("penghuniStatusGroup").classList.add("hidden");
    document.getElementById("modalPenghuni").classList.remove("hidden");
  } catch (err) {
    showToast(err.message);
  }
}

function openEditPenghuni(id) {
  try {
    const p = state.penghuniCache.find((item) => item.idPenghuni === id);
    if (!p) return;
    populateKamarSelect(p.idKamar, p.idKamar);
    document.getElementById("modalPenghuniTitle").textContent = `Ubah Penghuni — ${p.nama}`;
    document.getElementById("penghuniId").value = p.idPenghuni;
    document.getElementById("penghuniNama").value = p.nama;
    document.getElementById("penghuniNoHp").value = p.noHp;
    document.getElementById("penghuniAlamat").value = p.alamat;
    document.getElementById("penghuniMasuk").value = p.tanggalMasuk || "";
    document.getElementById("penghuniKeluar").value = p.tanggalKeluar || "";
    document.getElementById("penghuniStatus").value = p.statusAktif;
    document.getElementById("penghuniStatusGroup").classList.remove("hidden");
    document.getElementById("modalPenghuni").classList.remove("hidden");
  } catch (err) {
    showToast(err.message);
  }
}

function keluarPenghuni(id) {
  try {
    const today = new Date().toISOString().slice(0, 10);
    callBridge("keluarPenghuni", id, today);
    showToast("Penghuni ditandai keluar & kamar diperbarui.");
    loadPenghuni();
  } catch (err) {
    showToast(err.message);
  }
}

function hapusPenghuni(id) {
  try {
    callBridge("hapusPenghuni", id);
    showToast("Data penghuni dihapus.");
    loadPenghuni();
  } catch (err) {
    showToast(err.message);
  }
}

function loadPembayaran() {
  try {
    const q = document.getElementById("searchPembayaran").value || "";
    const list = callBridge("getDaftarPembayaran", state.pembayaranFilter, q);
    state.pembayaranCache = list;

    let lunas = 0;
    let belum = 0;
    for (const item of list) {
      if (item.status === "Lunas") lunas += Number(item.nominal || 0);
      else belum += Number(item.nominal || 0);
    }
    document.getElementById("payTotalLunas").textContent = formatRupiah(lunas);
    document.getElementById("payTotalBelum").textContent = formatRupiah(belum);

    const container = document.getElementById("pembayaranListContainer");
    if (!list.length) {
      container.innerHTML = `<div class="data-card"><span class="data-card-meta">Tidak ada data pembayaran.</span></div>`;
      return;
    }

    container.innerHTML = list.map((pay) => {
      const cls = pay.status === "Lunas"
        ? "lunas"
        : pay.status === "Jatuh Tempo" ? "jatuh-tempo" : "belum-lunas";
      return `
        <div class="data-card">
          <div class="data-card-top">
            <div>
              <strong>${pay.penghuni} &bull; Kamar ${pay.noKamar}</strong>
              <div class="data-card-meta">${pay.kodePembayaran} &bull; Periode: <b>${pay.periode}</b></div>
              <div class="data-card-meta">Nominal: <b>${formatRupiah(pay.nominal)}</b> &bull; Tempo: ${pay.tanggalJatuhTempo || "-"}</div>
              ${pay.tanggalBayar ? `<div class="data-card-meta">Dibayar: ${pay.tanggalBayar}</div>` : ""}
            </div>
            <span class="badge ${cls}">${pay.status}</span>
          </div>
          ${pay.status !== "Lunas"
            ? `<div class="data-card-actions">
                 <button type="button" class="btn-primary btn-sm" onclick="bayarTagihan(${pay.idPembayaran})">Bayar Sekarang</button>
               </div>`
            : ""}
        </div>
      `;
    }).join("");
  } catch (err) {
    showToast(err.message);
  }
}

function bayarTagihan(id) {
  try {
    const today = new Date().toISOString().slice(0, 10);
    callBridge("bayarTagihan", id, today);
    showToast("Pembayaran berhasil dicatat sebagai Lunas.");
    loadPembayaran();
  } catch (err) {
    showToast(err.message);
  }
}

document.addEventListener("DOMContentLoaded", () => {
  document.getElementById("btnMulai").addEventListener("click", () => navigateTo("login"));

  document.getElementById("loginForm").addEventListener("submit", (e) => {
    e.preventDefault();
    const email = document.getElementById("loginEmail").value;
    const password = document.getElementById("loginPassword").value;
    const errBox = document.getElementById("loginError");
    try {
      callBridge("login", email, password);
      errBox.classList.add("hidden");
      showToast("Login berhasil!");
      navigateTo("beranda");
    } catch (err) {
      errBox.textContent = err.message;
      errBox.classList.remove("hidden");
    }
  });

  document.getElementById("btnLogout").addEventListener("click", () => {
    try { callBridge("logout"); } catch (_) {}
    showToast("Anda telah keluar.");
    navigateTo("login");
  });

  document.querySelectorAll("[data-nav]").forEach((el) => {
    el.addEventListener("click", () => navigateTo(el.dataset.nav));
  });

  document.querySelectorAll("[data-close]").forEach((btn) => {
    btn.addEventListener("click", () => {
      document.getElementById(btn.dataset.close).classList.add("hidden");
    });
  });

  document.getElementById("btnOpenTambahKamar").addEventListener("click", openTambahKamar);
  document.getElementById("searchKamar").addEventListener("input", loadKamar);
  document.querySelectorAll("#filterKamarPills .pill").forEach((pill) => {
    pill.addEventListener("click", () => {
      document.querySelectorAll("#filterKamarPills .pill").forEach((p) => p.classList.remove("active"));
      pill.classList.add("active");
      state.kamarFilter = pill.dataset.status;
      loadKamar();
    });
  });

  document.getElementById("formKamar").addEventListener("submit", (e) => {
    e.preventDefault();
    try {
      const id = document.getElementById("kamarId").value;
      const nomor = document.getElementById("kamarNomor").value;
      const tipe = document.getElementById("kamarTipe").value;
      const harga = document.getElementById("kamarHarga").value;
      const status = document.getElementById("kamarStatus").value;
      if (id) {
        callBridge("ubahKamar", Number(id), nomor, tipe, harga, status);
        showToast("Data kamar diperbarui.");
      } else {
        callBridge("tambahKamar", nomor, tipe, harga);
        showToast("Kamar baru ditambahkan.");
      }
      document.getElementById("modalKamar").classList.add("hidden");
      loadKamar();
    } catch (err) {
      showToast(err.message);
    }
  });

  document.getElementById("btnOpenTambahPenghuni").addEventListener("click", openTambahPenghuni);
  document.getElementById("searchPenghuni").addEventListener("input", loadPenghuni);
  document.querySelectorAll("#filterPenghuniPills .pill").forEach((pill) => {
    pill.addEventListener("click", () => {
      document.querySelectorAll("#filterPenghuniPills .pill").forEach((p) => p.classList.remove("active"));
      pill.classList.add("active");
      state.penghuniFilter = pill.dataset.status;
      loadPenghuni();
    });
  });

  document.getElementById("formPenghuni").addEventListener("submit", (e) => {
    e.preventDefault();
    try {
      const id = document.getElementById("penghuniId").value;
      const nama = document.getElementById("penghuniNama").value;
      const noHp = document.getElementById("penghuniNoHp").value;
      const alamat = document.getElementById("penghuniAlamat").value;
      const idKamar = Number(document.getElementById("penghuniKamar").value);
      const masuk = document.getElementById("penghuniMasuk").value;
      const keluar = document.getElementById("penghuniKeluar").value;
      const statusAktif = document.getElementById("penghuniStatus").value;

      if (id) {
        callBridge("ubahPenghuni", Number(id), nama, noHp, alamat, idKamar, masuk, keluar, statusAktif);
        showToast("Data penghuni diperbarui.");
      } else {
        callBridge("tambahPenghuni", nama, noHp, alamat, idKamar, masuk, keluar);
        showToast("Penghuni ditambahkan, kamar Terisi & tagihan dibuat.");
      }
      document.getElementById("modalPenghuni").classList.add("hidden");
      loadPenghuni();
    } catch (err) {
      showToast(err.message);
    }
  });

  document.getElementById("searchPembayaran").addEventListener("input", loadPembayaran);
  document.querySelectorAll("#filterPembayaranPills .pill").forEach((pill) => {
    pill.addEventListener("click", () => {
      document.querySelectorAll("#filterPembayaranPills .pill").forEach((p) => p.classList.remove("active"));
      pill.classList.add("active");
      state.pembayaranFilter = pill.dataset.status;
      loadPembayaran();
    });
  });
});
